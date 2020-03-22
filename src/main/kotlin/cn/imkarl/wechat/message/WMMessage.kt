package cn.imkarl.wechat.message

import cn.imkarl.utils.jna.JnaField
import cn.imkarl.utils.jna.JnaType

/**
 * WM_COPYDATA的消息
 */
sealed class WMMessage {

    data class WxReceiveOriginalMessage(
        @JnaField(1, JnaType.INT)
        var type: Int = -1, //消息类型

        @JnaField(2, JnaType.STRING, 8192)
        var source: String = "", //消息来源

        @JnaField(3, JnaType.STRING, 40)
        var wxid: String = "", //微信ID/群ID

        @JnaField(4, JnaType.STRING, 40)
        var msgSender: String = "", //消息发送者

        @JnaField(5, JnaType.STRING, 8192)
        var content: String = "" //消息内容
    ) : WMMessage()

    /**
     * 用户信息结构体
     */
    data class UserInfo(
        @JnaField(1, JnaType.STRING, 80)
        var UserId: String = "",   // 微信ID

        @JnaField(2, JnaType.STRING, 80)
        var UserNumber: String = "",  // 微信号

        @JnaField(3, JnaType.STRING, 80)
        var UserRemark: String = "",  // 备注

        @JnaField(4, JnaType.STRING, 80)
        var UserNickName: String = ""  // 昵称
    ) : WMMessage()

    /**
     * 登录用户的个人信息
     */
    data class AccountInfo(
        @JnaField(1, JnaType.STRING, 40)
        val wxid: String = "",  //微信ID

        @JnaField(2, JnaType.STRING, 40)
        val account: String = "",  //微信账号

        @JnaField(3, JnaType.STRING, 40)
        val nickname: String = "",  //微信昵称

        @JnaField(4, JnaType.INT)
        val gender: Int = 0,  //性别（1-男，2-女，0-保密）

        @JnaField(5, JnaType.STRING, 30)
        val phone: String = "",  //手机号

        @JnaField(6, JnaType.STRING, 15)
        val device: String = "",  //登陆设备

        @JnaField(7, JnaType.STRING, 10)
        val nation: String = "",  //国籍

        @JnaField(8, JnaType.STRING, 20)
        val province: String = "",  //省份

        @JnaField(9, JnaType.STRING, 20)
        val city: String = "",  //城市

        @JnaField(10, JnaType.STRING, 0x100)
        val portrait: String = ""  //头像
    ) : WMMessage()

}
