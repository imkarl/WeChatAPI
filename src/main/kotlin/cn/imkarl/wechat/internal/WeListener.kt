package cn.imkarl.wechat.internal

import cn.imkarl.core.common.log.LogUtils
import cn.imkarl.utils.jna.JnaStructure
import cn.imkarl.utils.jna.WindowClassEx
import cn.imkarl.wechat.message.WMCopyData
import cn.imkarl.wechat.message.WMMessage
import cn.imkarl.wechat.message.WxMessage
import com.sun.jna.Pointer
import kotlinx.coroutines.*

/**
 * 微信相关事件的监听器
 */
internal object WeListener {

    private const val WINDOW_TITLE = "WeHelper_Binder"

    private var windowClassEx: WindowClassEx? = null
    private var checkIsLoginJob: Job? = null

    var lastLoginStatus: Boolean? = null
    private val friends = mutableListOf<WxMessage.UserInfo>()

    var onLoginChanged: ((Boolean) -> Unit)? = null
    var onGetAccountInfo: ((WMMessage.AccountInfo) -> Unit)? = null
    var onReceiveMessage: ((WMMessage.WxReceiveOriginalMessage) -> Unit)? = null

    fun start() {
        startGetMessages()
        startCheckIsLogin()
    }

    /**
     * 启动循环获取微信相关事件消息
     */
    private fun startGetMessages() {
        if (windowClassEx != null) {
            return
        }

        windowClassEx = WindowClassEx()
        windowClassEx?.setTitle(WINDOW_TITLE)
        windowClassEx?.setOnWMCopyDataCallback { dwData: Int, lpData: Pointer? ->
            val code = WMCopyData.parse(dwData)
            //LogUtils.d("code: $code")

            when (code) {
                //登录成功
                WMCopyData.WM_Login,
                //目前是已登录状态
                WMCopyData.WM_IsLogin -> {
                    if (lastLoginStatus != true) {
                        friends.clear()
                        lastLoginStatus = true
                        onLoginChanged?.invoke(true)
                    }
                }

                //未登录
                WMCopyData.WM_NotLogin -> {
                    if (lastLoginStatus != false) {
                        lastLoginStatus = false
                        onLoginChanged?.invoke(false)
                    }
                }

                //收到好友列表
                WMCopyData.WM_GetFriendList -> {
                    lpData?.let {
                        val userInfo: WMMessage.UserInfo = JnaStructure.newInstance(lpData)
                        friends.add(WxMessage.UserInfo(userInfo.UserNumber, userInfo.UserId, userInfo.UserNickName, userInfo.UserRemark))
                    }
                }

                //收到消息
                WMCopyData.WM_ShowChatRecord -> {
                    lpData?.let {
                        val originalMessage: WMMessage.WxReceiveOriginalMessage = JnaStructure.newInstance(lpData)
                        onReceiveMessage?.invoke(originalMessage)
                    }
                }

                //收到登录用户信息
                WMCopyData.WM_GetInformation -> {
                    lpData?.let {
                        val accountInfo: WMMessage.AccountInfo = JnaStructure.newInstance(lpData)
                        onGetAccountInfo?.invoke(accountInfo)
                    }
                }

                //收到群成员列表信息
                WMCopyData.WM_ShowChatRoomMembers -> {
                    lpData?.let {
                        val userInfo: WMMessage.UserInfo = JnaStructure.newInstance(lpData)
                        LogUtils.e("userInfo: $userInfo")
                    }
                }

                else -> {
                    LogUtils.e("onMessageHandle   dwData: ${code ?: dwData}   lpData: $lpData")
                }
            }
        }
        windowClassEx?.show()
    }

    /**
     * 启动循环检查当前登录状态
     */
    private fun startCheckIsLogin() {
        if (checkIsLoginJob != null) {
            return
        }

        checkIsLoginJob = GlobalScope.launch(Dispatchers.IO) {
            while (isActive) {
                delay(200)
                WeBinder.INSTANCE.ExecCommand(2)
            }
        }
    }

    fun stop() {
        if (checkIsLoginJob?.isCancelled != true) {
            checkIsLoginJob?.cancel()
        }
        checkIsLoginJob = null

        windowClassEx?.dismiss()
        windowClassEx = null
    }


    fun getFriends(): List<WxMessage.UserInfo> {
        return friends
    }

}
