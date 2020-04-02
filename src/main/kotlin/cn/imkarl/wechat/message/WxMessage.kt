package cn.imkarl.wechat.message

sealed class WxMessage {

    /**
     * 用户信息结构体
     */
    data class UserInfo(
        var wxid: String = "",  // 微信ID
        var account: String = "",   // 微信账号
        var nickname: String = "",  // 昵称
        var remark: String = ""  // 备注
    ) : WxMessage()

    /**
     * 登录用户的个人信息
     */
    data class AccountInfo(
        val wxid: String?,  //微信ID
        val account: String?,  //微信账号
        val nickname: String?,  //微信昵称
        val gender: Gender?,  //性别
        val phone: String?,  //手机号
        val device: String?,  //登陆设备
        val nation: String?,  //国籍
        val province: String?,  //省份
        val city: String?,  //城市
        val portrait: String?  //头像
    ) : WxMessage()

    /**
     * 收到转账信息
     */
    data class TransferInfo(
        val wxid: String?,
        val transferId: String?,
        val transcationId: String?,
        val title: String?,
        val description: String?,
        val type: Int?,
        val payType: TransferPayType = TransferPayType.Unknown,
        val payDescription: String?,
        val payRemark: String?,
        val invalidTime: Long?,
        val beginTransferTime: Long?,
        val effectiveDate: Long?
    ) : WxMessage()


    /**
     * 收到收款信息
     */
    data class ReceiptOfMoneyInfo(
        val wxid: String?,
        val title: String?,
        val description: String?,
        val type: Int?,
        val subType: Int?,
        val myRemark: String?,
        val payRemark: String?,
        val money: String?
    ) : WxMessage()


    /**
     * 聊天消息
     */
    data class ChatMessage<T : ChatMessage.MsgContent>(
        val groupId: String? = null,  // 群ID，如果是群消息才会有此字段
        val senderUserId: String,
        val sourceType: SourceType,
        val content: T
    ) : WxMessage() {
        interface MsgContent

        data class TextMsgContent(
            val text: String
        ) : MsgContent

        data class ImageMsgContent(
            val imageUrl: String
        ) : MsgContent

        data class FileMsgContent(
            val fileUrl: String
        ) : MsgContent
    }

    /**
     * 请求添加好友
     */
    data class RequestAddFriend(
        val wxid: String?,
        val account: String?,
        val nickname: String?,
        val country: String?,
        val province: String?,
        val city: String?,
        val sex: Gender,
        val slogan: String?,
        val snsBackgroundImageUrl: String?,
        val headImageUrl: String?,
        val opcode: Int?,
        val v1: String?,
        val v2: String?,
        val requestContent: String?  // 申请内容文本
    ) : WxMessage()

}
