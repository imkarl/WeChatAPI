package cn.imkarl.wechat.internal

import cn.imkarl.core.common.log.LogLevel
import cn.imkarl.core.common.log.LogUtils
import com.sun.jna.*

internal interface WeBinder : Library {

    companion object {
        val INSTANCE by lazy {
            val weChatOpenApi = Native.load("WeBinder", WeBinder::class.java)
            LogUtils.println(LogLevel.INFO, "load 'WeBinder.dll' success.")
            weChatOpenApi
        }
    }

    fun ExecCommand(command: Int): Int
    fun ExecCommand2(command: Int, msg1: WString, msg2: WString): Int

}
