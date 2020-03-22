package cn.imkarl.wechat.modular

import cn.imkarl.wechat.message.WMCopyData
import cn.imkarl.wechat.internal.WeBinder
import com.sun.jna.WString

/**
 * 微信转账模块
 */
object WeTransferModule {

    /**
     * 请求接收转账
     * @param transferId 转账ID
     */
    fun requestReciveTransfer(wxid: String, transferId: String) {
        WeBinder.INSTANCE.ExecCommand2(WMCopyData.WM_ReciveMoney.code, WString(wxid), WString(transferId))
    }

}