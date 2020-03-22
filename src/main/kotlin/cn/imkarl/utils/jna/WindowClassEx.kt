package cn.imkarl.utils.jna

import cn.imkarl.core.common.log.LogUtils
import com.sun.jna.Callback
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class WindowClassEx : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    private val delegate = WinUser.WNDCLASSEX()
    private var loopGetMessageJob: Job? = null

    private var onWMCopyDataCallback: ((dwData: Int, lpData: Pointer?)->Unit)? = null

    init {
        // 创建一个新窗口类
        delegate.style = 1 or 2 //风格
        delegate.cbClsExtra = 0
        delegate.cbWndExtra = 0
        delegate.hInstance = null
        delegate.hIcon = null
        delegate.hCursor = null
        delegate.hbrBackground = null
        delegate.lpszMenuName = null

        // 窗口回调函数指针
        delegate.lpfnWndProc = object : OnWindowProcCallback {
            override fun onCallback(
                hwnd: WinDef.HWND,
                uMsg: WinDef.UINT,
                wParam: WinDef.WPARAM,
                lParam: WinDef.LPARAM
            ): WinDef.LRESULT {
                if (uMsg.toInt() == WinUser.WM_COPYDATA) {
                    val copyDataStruct = WinUser.COPYDATASTRUCT(Pointer(lParam.toLong()))
                    onWMCopyDataCallback?.let {
                        try {
                            it.invoke(copyDataStruct.dwData.toInt(), copyDataStruct.lpData)
                        } catch (throwable: Throwable) {
                            LogUtils.e(throwable)
                        }
                    }
                }
                return User32.INSTANCE.DefWindowProc(hwnd, uMsg.toInt(), wParam, lParam)
            }
        }
    }

    fun setTitle(title: String) {
        delegate.lpszClassName = title
    }

    fun setOnWMCopyDataCallback(callback: (dwData: Int, lpData: Pointer?)->Unit) {
        onWMCopyDataCallback = callback
    }

    fun show(x: Int = 0, y: Int = 0, width: Int = 100, height: Int = 100): Boolean {
        if (loopGetMessageJob?.isActive == true) {
            // 循环还在，则说明已经显示过窗口了
            return true
        }

        loopGetMessageJob = launch {
            // 注册窗口类
            if (User32.INSTANCE.RegisterClassEx(delegate).toInt() == 0) {
                LogUtils.e("register window failed!")
                throw RuntimeException("${Kernel32.INSTANCE.GetLastError()} \n ${Kernel32Util.getLastErrorMessage()}")
            }

            // 创建窗口
            val hWnd = User32Util.createWindowEx(0,
                delegate.lpszClassName, delegate.lpszClassName,
                WinUser.WS_OVERLAPPEDWINDOW,
                x, y, width, height,
                null, null, null, null
            )

            if (hWnd == null) {
                LogUtils.e("create window failed!")
                throw RuntimeException("${Kernel32.INSTANCE.GetLastError()} \n ${Kernel32Util.getLastErrorMessage()}")
            }

            // 更新显示窗口
            User32.INSTANCE.ShowWindow(hWnd, WinUser.SW_HIDE)
            User32.INSTANCE.UpdateWindow(hWnd)
            //LogUtils.i("registerWndProc success")

            // 消息循环（消息泵）
            val msg = WinUser.MSG()
            // 获取消息
            while (isActive && User32.INSTANCE.GetMessage(msg, hWnd, 0, 0) != 0) {
                // 翻译消息
                User32.INSTANCE.TranslateMessage(msg)
                // 转发到消息回调函数
                User32.INSTANCE.DispatchMessage(msg)
            }
        }
        return true
    }

    fun dismiss() {
        if (loopGetMessageJob?.isCancelled != true) {
            loopGetMessageJob?.cancel()
        }
        loopGetMessageJob = null
    }


    private interface OnWindowProcCallback : Callback {
        fun onCallback(
            hwnd: WinDef.HWND,
            uMsg: WinDef.UINT,
            wParam: WinDef.WPARAM,
            lParam: WinDef.LPARAM
        ): WinDef.LRESULT
    }

}