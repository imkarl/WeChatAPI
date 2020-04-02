package cn.imkarl.wechat.message

import cn.imkarl.core.common.json.JsonUtils
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

sealed class XmlMessage {

    class AppMsg {
        @XmlElement(name = "title")
        @JvmField
        var title: String? = null

        @XmlElement(name = "des")
        @JvmField
        var description: String? = null

        @XmlElement(name = "content")
        @JvmField
        var content: String? = null

        @XmlElement(name = "extinfo")
        @JvmField
        var extinfo: String? = null

        @XmlElement(name = "type")
        @JvmField
        var type: Int = -1

        @XmlElement(name = "mmreader")
        @JvmField
        var remarks: Remarks? = null

        @XmlElement(name = "wcpayinfo")
        @JvmField
        var payInfo: PayInfo? = null

        override fun toString(): String {
            return "AppMsg${JsonUtils.toJson(this)}"
        }
    }

    data class Remarks @JvmOverloads constructor(
        @XmlElement(name = "template_detail")
        var template_detail: RemarkDetail? = null
    )

    class RemarkDetail {
        @XmlElement(name = "line_content")
        @JvmField
        var content: RemarkDetailContent? = null

        override fun toString(): String {
            return "RemarkDetail(content=$content)"
        }
    }

    class RemarkDetailContent {
        @XmlElement(name = "topline")
        @JvmField
        var title: RemarkKeyValue? = null

        @XmlElement(name = "lines")
        @JvmField
        var lines: RemarkDetailContentLines? = null

        override fun toString(): String {
            return "RemarkDetailContent(title=$title, lines=$lines)"
        }
    }

    data class RemarkDetailContentLines @JvmOverloads constructor(
        @XmlElement(name = "line")
        var line: List<RemarkKeyValue>? = null
    )

    class RemarkKeyValue {
        @XmlElement(name = "key")
        @JvmField
        var key: RemarkDetailContentLineWord? = null  // 付款方备注  收款方备注  收款金额

        @XmlElement(name = "value")
        @JvmField
        var value: RemarkDetailContentLineWord? = null

        override fun toString(): String {
            return "RemarkKeyValue(key=$key, value=$value)"
        }
    }

    data class RemarkDetailContentLineWord @JvmOverloads constructor(
        @XmlElement(name = "word")
        @JvmField
        var word: String? = null
    )

    class PayInfo {
        @XmlElement(name = "paysubtype")
        @JvmField
        var paySubType: Int? = null

        @XmlElement(name = "feedesc")
        @JvmField
        var feedesc: String? = null

        @XmlElement(name = "transcationid")
        @JvmField
        var transcationId: String? = null

        @XmlElement(name = "transferid")
        @JvmField
        var transferId: String? = null

        @XmlElement(name = "invalidtime")
        @JvmField
        var invalidTime: String? = null

        @XmlElement(name = "begintransfertime")
        @JvmField
        var beginTransferTime: String? = null

        @XmlElement(name = "effectivedate")
        @JvmField
        var effectiveDate: String? = null

        @XmlElement(name = "pay_memo")
        @JvmField
        var payMemo: String? = null

        override fun toString(): String {
            return "PayInfo${JsonUtils.toJson(this)}"
        }

    }

    /**
     * XML消息的根节点
     */
    @XmlRootElement(name = "msg")
    class MessageRoot {

        @XmlElement(name = "appmsg")
        @JvmField
        var appmsg: AppMsg? = null

        @XmlElement(name = "fromusername")
        @JvmField
        var fromUserName: String? = null

        @XmlAttribute(name = "fromusername")
        @JvmField
        var fromWxid: String? = null

        @XmlAttribute(name = "fromnickname")
        @JvmField
        var fromNickName: String? = null

        @XmlAttribute(name = "alias")
        @JvmField
        var fromAliasName: String? = null

        @XmlAttribute(name = "weibo")
        @JvmField
        var weibo: String? = null

        @XmlAttribute(name = "bigheadimgurl")
        @JvmField
        var headImageUrl: String? = null

        @XmlAttribute(name = "snsbgimgid")
        @JvmField
        var snsBackgroundImageUrl: String? = null

        @XmlAttribute(name = "mhash")
        @JvmField
        var mhash: String? = null

        @XmlAttribute(name = "mfullhash")
        @JvmField
        var mfullhash: String? = null

        @XmlAttribute(name = "content")
        @JvmField
        var content: String? = null

        @XmlAttribute(name = "imagestatus")
        @JvmField
        var imageStatus: Int? = null

        @XmlAttribute(name = "scene")
        @JvmField
        var scene: Int? = null

        @XmlAttribute(name = "country")
        @JvmField
        var country: String? = null

        @XmlAttribute(name = "province")
        @JvmField
        var province: String? = null

        @XmlAttribute(name = "city")
        @JvmField
        var city: String? = null

        @XmlAttribute(name = "sign")
        @JvmField
        var slogan: String? = null

        @XmlAttribute(name = "sex")
        @JvmField
        var sex: Int? = null

        @XmlAttribute(name = "opcode")
        @JvmField
        var opcode: Int? = null

        @XmlAttribute(name = "encryptusername")
        @JvmField
        var encryptUserName: String? = null

        @XmlAttribute(name = "ticket")
        @JvmField
        var ticket: String? = null

        override fun toString(): String {
            return "MessageRoot${JsonUtils.toJson(this)}"
        }

    }

}
