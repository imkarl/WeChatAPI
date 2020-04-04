package cn.imkarl.wechat.internal

import cn.imkarl.core.common.log.LogLevel
import cn.imkarl.core.common.log.LogUtils
import cn.imkarl.utils.XmlUtils
import cn.imkarl.wechat.message.*

/**
 * 原始消息的加工处理器
 */
object OriginalMesageProcessor {

    fun process(originalMessage: WMMessage.WxReceiveOriginalMessage): WxMessage? {
        val sourceType = getSourceType(originalMessage)

        var msgContent: WxMessage.ChatMessage.MsgContent? = null
        when (originalMessage.type) {
            1 -> {
                // 文字
                msgContent = WxMessage.ChatMessage.TextMsgContent(originalMessage.content)
            }
            10000 -> {
                // 系统提示消息：文字
                msgContent = WxMessage.ChatMessage.TextMsgContent(originalMessage.content)
            }
            49 -> {
                if (originalMessage.content.contains("<title><![CDATA[微信转账]]></title>")) {
                    // 转账
                    val reciveMoneyMessage = XmlUtils.fromXml<XmlMessage.MessageRoot>(originalMessage.content)
                    return WxMessage.TransferInfo(
                        originalMessage.wxid,
                        reciveMoneyMessage?.appmsg?.payInfo?.transferId,
                        reciveMoneyMessage?.appmsg?.payInfo?.transcationId,
                        reciveMoneyMessage?.appmsg?.title,
                        reciveMoneyMessage?.appmsg?.description,
                        reciveMoneyMessage?.appmsg?.type,
                        TransferPayType.parse(reciveMoneyMessage?.appmsg?.payInfo?.paySubType),
                        reciveMoneyMessage?.appmsg?.payInfo?.feedesc,
                        reciveMoneyMessage?.appmsg?.payInfo?.payMemo,
                        reciveMoneyMessage?.appmsg?.payInfo?.invalidTime?.toLongOrNull(),
                        reciveMoneyMessage?.appmsg?.payInfo?.beginTransferTime?.toLongOrNull(),
                        reciveMoneyMessage?.appmsg?.payInfo?.effectiveDate?.toLongOrNull()
                    )

                } else if (originalMessage.content.contains("<title><![CDATA[收款到账通知]]></title>")
                    && originalMessage.content.contains("<title><![CDATA[微信支付]]></title>")) {
                    // 收款码
                    val reciveMoneyMessage = XmlUtils.fromXml<XmlMessage.MessageRoot>(originalMessage.content)

                    val money = reciveMoneyMessage?.appmsg?.remarks?.template_detail?.content?.title?.let {
                        if ("收款金额" == it.key?.word) {
                            it.value?.word
                        } else {
                            null
                        }
                    }

                    var myRemark: String? = null
                    var payRemark: String? = null
                    reciveMoneyMessage?.appmsg?.remarks?.template_detail?.content?.lines?.line?.forEach {
                        if ("收款方备注" == it.key?.word) {
                            myRemark = it.value?.word
                        }
                        if ("付款方备注" == it.key?.word) {
                            payRemark = it.value?.word
                        }
                    }

                    return WxMessage.ReceiptOfMoneyInfo(
                        originalMessage.wxid,
                        reciveMoneyMessage?.appmsg?.title,
                        reciveMoneyMessage?.appmsg?.description,
                        reciveMoneyMessage?.appmsg?.type,
                        reciveMoneyMessage?.appmsg?.payInfo?.paySubType,
                        myRemark,
                        payRemark,
                        money
                    )

                } else if (originalMessage.content.contains("<title><![CDATA[我发起了位置共享]]></title>")) {
                    // 共享实时位置
                    val xmlMessage = XmlUtils.fromXml<XmlMessage.MessageRoot>(originalMessage.content)

                }

            }
            37 -> {
                if (sourceType == SourceType.FriendMessage
                    && originalMessage.content.contains("encryptusername=\"v1_")
                    && originalMessage.content.contains("ticket=\"v2_")) {
                    // 请求添加好友
                    val xmlMessage = XmlUtils.fromXml<XmlMessage.MessageRoot>(originalMessage.content)
                    val requestAddFriend = WxMessage.RequestAddFriend(
                        xmlMessage?.fromWxid,
                        xmlMessage?.fromAliasName,
                        xmlMessage?.fromNickName,
                        xmlMessage?.country,
                        xmlMessage?.province,
                        xmlMessage?.city,
                        Gender.parse(xmlMessage?.sex),
                        xmlMessage?.slogan,
                        xmlMessage?.snsBackgroundImageUrl,
                        xmlMessage?.headImageUrl,
                        xmlMessage?.opcode,
                        xmlMessage?.encryptUserName,
                        xmlMessage?.ticket,
                        xmlMessage?.content
                    )
                    return requestAddFriend
                }

            }
            else -> {
                // 其他
                LogUtils.i(originalMessage.toString())
            }
        }

        if (msgContent != null) {

            var message: WxMessage.ChatMessage<*>? = null
            when (sourceType) {
                // 公众号
                SourceType.Subscription -> {
                    //如果微信ID为gh_3dfda90e39d6 说明是收款消息
                    if (originalMessage.wxid == "gh_3dfda90e39d6") {
                        originalMessage.content = "微信收款到账"
                    } else {
                        originalMessage.content = "公众号发来推文,请在手机上查看"
                    }

                }

                // 文件助手
                SourceType.FileHelper -> {
                    message = WxMessage.ChatMessage(null, originalMessage.wxid, sourceType, msgContent)
                }

                // 群聊
                SourceType.GroupChat -> {
                    message = WxMessage.ChatMessage(originalMessage.wxid, originalMessage.msgSender, sourceType, msgContent)
                }

                // 好友
                SourceType.FriendChat -> {
                    message = WxMessage.ChatMessage(null, originalMessage.wxid, sourceType, msgContent)
                }
            }

            return message
        }

        return null
    }

    fun getSourceType(originalMessage: WMMessage.WxReceiveOriginalMessage): SourceType {
        return when {
            // 文件助手
            originalMessage.wxid.toLowerCase() == "filehelper" -> {
                SourceType.FileHelper
            }

            // 系统好友消息（朋友推荐消息）
            originalMessage.wxid.toLowerCase() == "fmessage" -> {
                SourceType.FriendMessage
            }

            // 以gh_开头，则说明是公众号
            originalMessage.wxid.toLowerCase().startsWith("gh_") -> {
                SourceType.Subscription
            }

            // 以wxid_开头，则说明是普通微信用户
            originalMessage.wxid.toLowerCase().startsWith("wxid_") -> {
                SourceType.FriendChat
            }

            // 如果微信ID以@chatroom结尾，则说明是群聊
            originalMessage.wxid.toLowerCase().endsWith("@chatroom") -> {
                SourceType.GroupChat
            }

            // 【可能还有其它未知类型，有待收集】
            else -> {
                SourceType.Unknown
            }
        }
    }

}