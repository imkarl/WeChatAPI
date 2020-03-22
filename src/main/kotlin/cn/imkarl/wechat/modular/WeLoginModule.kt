package cn.imkarl.wechat.modular

import cn.imkarl.core.common.file.FileUtils
import cn.imkarl.utils.qrcode.QrcodeUtils
import cn.imkarl.utils.CmdUtils
import cn.imkarl.wechat.internal.WeBinder
import cn.imkarl.wechat.internal.WeListener
import kotlinx.coroutines.delay
import java.io.File

/**
 * 微信登录 模块
 */
object WeLoginModule {

    private val loginQrcodeImageFile = File(System.getProperty("java.io.tmpdir"), "qrcode.png")

    /**
     * 设置登录状态改变的监听器
     */
    fun setOnLoginChangedListener(listener: (Boolean)->Unit) {
        WeListener.onLoginChanged = {
            listener.invoke(it)
        }
    }

    /**
     * 获取当前登录状态
     */
    fun isLogin(): Boolean {
        return WeListener.lastLoginStatus == true
    }

    /**
     * 获取登录的二维码内容
     */
    suspend fun getLoginQrCode(): String {
        FileUtils.deleteFile(loginQrcodeImageFile)

        delay(300)

        val result = WeBinder.INSTANCE.ExecCommand(1)
        if (result != 0) {
            throw IllegalStateException("无法获取登录二维码")
        }

        for (i in 0 until 60) {
            if (!loginQrcodeImageFile.exists()) {
                delay(50)
            }
        }

        if (!loginQrcodeImageFile.exists()) {
            throw IllegalStateException("获取登录二维码失败")
        }

        val qrcode = QrcodeUtils.decode(loginQrcodeImageFile)

        if (qrcode.isNullOrBlank()) {
            throw IllegalStateException("解析登录二维码失败")
        }

        return qrcode
    }

    /**
     * 退出登录
     */
    fun logout() {
        CmdUtils.execQuietly("taskkill /im WeChat.exe /f")
    }

}