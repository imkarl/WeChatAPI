package cn.imkarl.wechat

import cn.imkarl.core.common.app.AppUtils
import cn.imkarl.core.common.log.LogUtils
import cn.imkarl.wechat.message.SourceType
import cn.imkarl.wechat.message.TransferPayType
import cn.imkarl.wechat.message.WxMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * 应用程序入口
 */
object AppLauncher {

    @JvmStatic
    fun main(vararg args: String) {

        AppUtils.init("cn.imkarl.wechat", "WeConnect")
        AppUtils.setDebug(true)


        GlobalScope.launch {

            val weChatInstallDir = We.install.getInstallDir()
            LogUtils.d("weChatInstallDir: $weChatInstallDir")

            val wechatVersion = We.install.getVersion()
            LogUtils.d("wechatVersion: $wechatVersion")
            if (wechatVersion.toString() != "2.6.8.52") {
                throw IllegalStateException("不支持的微信版本，请下载WeChat2.6.8.52")
            }

            val launchWeChat = We.install.launch()
            LogUtils.d("launchWeChat: $launchWeChat")

            We.login.setOnLoginChangedListener { isLogin ->
                LogUtils.d("isLogin: $isLogin")

                if (isLogin) {
                    onLoginSuccess()
                }
            }

            val loginQrCode = We.login.getLoginQrCode()
            LogUtils.d("loginQrCode: http://qr.liantu.com/api.php?text=$loginQrCode")

        }

        System.`in`.read()
    }

    /**
     * 登录成功
     */
    private fun onLoginSuccess() {
        GlobalScope.launch {
            delay(1000)

            // 获取账户信息
            val accountInfo = We.userinfo.getAccountInfo()
            LogUtils.d("accountInfo: $accountInfo")

            // 获取好友列表
            val friends = We.userinfo.getFriends()
            LogUtils.d("friends: $friends")

            We.message.setOnReceiveMessageListener { message ->
                GlobalScope.launch {
                    onReceiveMessage(message)
                }
            }

        }
    }

    /**
     * 接受到微信消息
     */
    private suspend fun onReceiveMessage(message: WxMessage) {
        LogUtils.d("onRecvMessage: $message")
        if (message is WxMessage.ChatMessage<*>) {
            when (message.sourceType) {
                SourceType.FriendChat,
                SourceType.FileHelper -> {
                    // 自动回复
                    val sendUserInfo = We.userinfo.getFriends().firstOrNull { it.wxid == message.senderUserId } ?: return
                    We.message.sendTextMessage(message.senderUserId, "你是【${sendUserInfo.nickName}】，你给我发了一条【${message.content::class.simpleName}】消息")
                }
            }
        }
        if (message is WxMessage.TransferInfo) {
            if (message.payType == TransferPayType.Create) {
                // 延迟处理，避免微信限制
                delay((100 + Random.nextInt(1000)).toLong())

                // 自动接受转账
                We.transfer.requestReciveTransfer(message.wxid!!, message.transferId!!)
            }
        }
    }

}
