package cn.imkarl.wechat.modular

import cn.imkarl.wechat.message.WMCopyData
import cn.imkarl.wechat.internal.WeBinder
import com.sun.jna.WString

/**
 * 微信群 模块
 */
object WeGroupModule {

    /**
     * 设置群公告
     * @param wxid 群ID
     * @param content 公告内容
     */
    fun setChatRoomAnnouncement(wxid: String, content: String) {
        WeBinder.INSTANCE.ExecCommand2(WMCopyData.WM_SetRoomAnnouncement.code, WString(wxid), WString(content))
    }

}