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
/*
<title>
    <![CDATA[微信支付收款0.01元(朋友到店)]]>
</title>
<des>
    <![CDATA[收款金额￥0.01
收款方备注测试一下收款码
付款方备注测试固定金额的收款码
汇总今日第2笔收款，共计￥0.02
备注收款成功，已存入零钱。点击可查看详情]]>
</des>
<type>5</type>
<showtype>1</showtype>
<mmreader>
</mmreader>
<template_id>
    <![CDATA[ey45ZWkUmYUBk_fMgxBLvyaFqVop1rmoWLFd62OXGiU]]>
</template_id>
 */
    }

    data class Remarks @JvmOverloads constructor(
        @XmlElement(name = "template_detail")
        var template_detail: RemarkDetail? = null
/*
<category type="0" count="1">
    <name>
        <![CDATA[微信支付]]>
    </name>
    <topnew>
        <digest>
            <![CDATA[收款金额￥0.01
收款方备注测试一下收款码
付款方备注测试固定金额的收款码
汇总今日第2笔收款，共计￥0.02
备注收款成功，已存入零钱。点击可查看详情]]>
        </digest>
    </topnew>
    <item>
        <itemshowtype>4</itemshowtype>
        <title>
            <![CDATA[收款到账通知]]>
        </title>
        <pub_time>1579323997</pub_time>
        <digest>
            <![CDATA[收款金额￥0.01
收款方备注测试一下收款码
付款方备注测试固定金额的收款码
汇总今日第2笔收款，共计￥0.02
备注收款成功，已存入零钱。点击可查看详情]]>
        </digest>
        <fileid>0</fileid>
        <sources>
            <source>
                <name>
                    <![CDATA[微信支付]]>
                </name>
            </source>
        </sources>
        <del_flag>0</del_flag>
        <contentattr>0</contentattr>
        <template_op_type>1</template_op_type>
        <weapp_username>
            <![CDATA[gh_fac0ad4c321d@app]]>
        </weapp_username>
        <appmsg_like_type>0</appmsg_like_type>
        <is_pay_subscribe>0</is_pay_subscribe>
    </item>
</category>
<publisher>
    <username>
        <![CDATA[wxzhifu]]>
    </username>
    <nickname>
        <![CDATA[微信支付]]>
    </nickname>
</publisher>
<template_header>
    <title>
        <![CDATA[收款到账通知]]>
    </title>
    <pub_time>1579323997</pub_time>
    <hide_title_and_time>1</hide_title_and_time>
    <show_icon_and_display_name>0</show_icon_and_display_name>
    <hide_icon_and_display_name_line>1</hide_icon_and_display_name_line>
    <ignore_hide_title_and_time>1</ignore_hide_title_and_time>
    <hide_time>1</hide_time>
    <pay_style>1</pay_style>
</template_header>
<template_detail>
</template_detail>
<forbid_forward>0</forbid_forward>
 */
    )

    class RemarkDetail {
        @XmlElement(name = "line_content")
        @JvmField
        var content: RemarkDetailContent? = null

        override fun toString(): String {
            return "RemarkDetail(content=$content)"
        }
/*
<template_show_type>1</template_show_type>
<opitems>
    <opitem>
        <word>
            <![CDATA[收款小账本]]>
        </word>
        <weapp_username>
            <![CDATA[gh_fac0ad4c321d@app]]>
        </weapp_username>
        <op_type>1</op_type>
    </opitem>
    <show_type>1</show_type>
</opitems>
 */
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


/*
    // 地理位置
    data class LocationInfo(
        @SerializedName("x")
        val x: Double?,
        @SerializedName("y")
        val y: Double?,
        @SerializedName("scale")
        val scale: Double?,
        @SerializedName("label")
        val label: String?,
        @SerializedName("poiid")
        val poiId: String?,
        @SerializedName("poiname")
        val poiName: String?,
        @SerializedName("maptype")
        val mapType: Int?
    ) : ClientProtocol(50)

    // 共享实时位置
    data class SyncLocationInfo(
        @SerializedName("type")
        val type: Int?,
        @SerializedName("title")
        val title: String?,
        @SerializedName("from_wxid", alternate = ["fromusername"])
        val fromWxid: String?
    ) : ClientProtocol(51)
*/

}
