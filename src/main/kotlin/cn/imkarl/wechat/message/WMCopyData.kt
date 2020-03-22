package cn.imkarl.wechat.message

/**
 * WM_COPYDATA通讯消息
 */
enum class WMCopyData(val code: Int) {

    WM_Login(0),
    WM_GetFriendList(3),
    WM_ShowChatRecord(4),
    WM_SendTextMessage(5),
    WM_SendFileMessage(6),
    WM_GetInformation(7),
    WM_SendImageMessage(8),
    WM_SetRoomAnnouncement(9),
    WM_ShowChatRoomMembers(14),
    WM_ReciveMoney(200),

    WM_IsLogin(1001),
    WM_NotLogin(1002),
    ;

    companion object {
        fun parse(code: Int): WMCopyData? {
            return values().firstOrNull { it.code == code }
        }
    }

}
