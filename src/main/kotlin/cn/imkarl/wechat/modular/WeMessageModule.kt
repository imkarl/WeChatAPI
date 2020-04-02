package cn.imkarl.wechat.modular

import cn.imkarl.core.common.log.LogUtils
import cn.imkarl.wechat.internal.OriginalMesageProcessor
import cn.imkarl.wechat.internal.WeBinder
import cn.imkarl.wechat.internal.WeListener
import cn.imkarl.wechat.message.WMCopyData
import cn.imkarl.wechat.message.WxMessage
import com.sun.jna.WString
import java.io.File

/**
 * 微信消息 模块
 */
object WeMessageModule {

    /**
     * 设置接收消息的监听器
     */
    fun setOnReceiveMessageListener(listener: (WxMessage)->Unit) {
        WeListener.onReceiveMessage = { originalMesage ->
            //LogUtils.d("onReceiveMessage: $originalMesage")
            val message = OriginalMesageProcessor.process(originalMesage)
            if (message != null) {
                listener.invoke(message)
            }
        }
    }

    /**
     * 发送文本消息
     */
    fun sendTextMessage(wxid: String, message: String): Boolean {
        val result = WeBinder.INSTANCE.ExecCommand2(WMCopyData.WM_SendTextMessage.code, WString(wxid), WString(message))
        return result == 0
    }

    /**
     * 发送图片消息
     */
    fun sendImageMessage(wxid: String, imageFile: File): Boolean {
        val result = WeBinder.INSTANCE.ExecCommand2(WMCopyData.WM_SendImageMessage.code, WString(wxid), WString(imageFile.absolutePath))
        return result == 0
    }

    /**
     * 发送文件消息
     */
    fun sendFileMessage(wxid: String, file: File): Boolean {
        val result = WeBinder.INSTANCE.ExecCommand2(WMCopyData.WM_SendFileMessage.code, WString(wxid), WString(file.absolutePath))
        return result == 0
    }

}
