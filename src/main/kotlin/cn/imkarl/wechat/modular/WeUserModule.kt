package cn.imkarl.wechat.modular

import cn.imkarl.core.common.log.LogUtils
import cn.imkarl.wechat.message.Gender
import cn.imkarl.wechat.message.WxMessage
import cn.imkarl.wechat.internal.WeBinder
import cn.imkarl.wechat.internal.WeListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 微信用户模块
 */
object WeUserModule {

    private var accountInfo: WxMessage.AccountInfo? = null

    /**
     * 请求获取登录账户信息
     */
    suspend fun getAccountInfo(): WxMessage.AccountInfo {
        if (accountInfo == null) {
            try {
                withContext(Dispatchers.Unconfined) {
                    withTimeout(1000) {
                        accountInfo = suspendCoroutine<WxMessage.AccountInfo> { continuation ->
                            WeListener.onGetAccountInfo = {
                                continuation.resume(WxMessage.AccountInfo(
                                    it.wxid, it.account, it.nickname,
                                    Gender.parse(it.gender), it.phone, it.device,
                                    it.nation, it.province, it.city, it.portrait
                                ))
                            }

                            val result = WeBinder.INSTANCE.ExecCommand(3)
                            if (result != 0) {
                                continuation.resumeWithException(IllegalStateException("请求获取账户信息失败"))
                            }
                        }
                    }
                }
            } catch (throwable: Throwable) {
                LogUtils.e(throwable)
            } finally {
                WeListener.onGetAccountInfo = null
            }
        }
        return accountInfo!!
    }

    /**
     * 获取好友列表
     */
    fun getFriends(): List<WxMessage.UserInfo> {
        return WeListener.getFriends()
    }

}