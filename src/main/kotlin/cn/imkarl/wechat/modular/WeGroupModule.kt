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
     * @param groupWxid 群ID
     * @param content 公告内容
     */
    fun setChatRoomAnnouncement(groupWxid: String, content: String) {
        WeBinder.INSTANCE.ExecCommand2(WMCopyData.WM_SetRoomAnnouncement.code, WString(groupWxid), WString(content))
    }

    /**
     * 添加群成员
     * @param groupWxid 群ID
     * @param wxid 需要添加到群的用户ID
     */
    fun addMember(groupWxid: String, wxid: String) {
        WeBinder.INSTANCE.ExecCommand2(WMCopyData.WM_AddGroupMember.code, WString(groupWxid), WString(wxid))
    }

}