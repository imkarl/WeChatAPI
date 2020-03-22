package cn.imkarl.utils

import cn.imkarl.core.common.log.LogUtils
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object CmdUtils {

    fun exec(cmd: String, runDir: File? = null): String {
        //LogUtils.d("cmd: $cmd")

        val run = Runtime.getRuntime()
        val process: Process = run.exec(cmd, null, runDir)
        val stream = process.inputStream
        val reader = InputStreamReader(stream)
        val br = BufferedReader(reader)
        val sb = StringBuffer()
        var message: String?
        while (br.readLine().also { message = it } != null) {
            sb.append(message).append('\n')
        }
        return sb.toString()
    }

    fun execQuietly(cmd: String, runDir: File? = null): Boolean {
        //LogUtils.d("cmd: $cmd")

        try {
            val run = Runtime.getRuntime()
            val process: Process = run.exec(cmd, null, runDir)
            process.waitFor()
        } catch (e: Throwable) {
            LogUtils.e(e)
        }
        return true
    }

}