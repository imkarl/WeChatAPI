package cn.imkarl.wechat.modular

import cn.imkarl.core.common.app.AppUtils
import cn.imkarl.core.common.file.FileUtils
import cn.imkarl.core.common.security.EncryptUtils
import cn.imkarl.utils.CmdUtils
import cn.imkarl.wechat.internal.WeListener
import java.io.File

object WeInstall {

    private val VERSIONs by lazy {
        return@lazy hashMapOf(
            "a49a3e22519d5c2bf8d7bd0be0045151" to "2.6.8.52"
        )
    }

    /**
     * 获取微信安装目录
     */
    fun getInstallDir(): File? {
        val regQuery = "reg query HKEY_CURRENT_USER\\Software\\Tencent\\bugReport\\WechatWindows"
        val result = CmdUtils.exec(regQuery).split("\n")
        val installDirStr = result.firstOrNull { it.trim().startsWith("InstallDir") }
        val installDir = installDirStr?.trim()?.removePrefix("InstallDir")?.trim()?.removePrefix("REG_SZ")?.trim()
        return installDir?.let { File(it) }
    }

    /**
     * 获取安装的微信版本号
     */
    fun getVersion(): String? {
        val wechatExeFile = File(getInstallDir(), "WeChat.exe")
        val md5 = EncryptUtils.md5(wechatExeFile)
        return md5?.let { VERSIONs[it] }
    }

    /**
     * 启动微信
     */
    fun launch(): Boolean {
        // 启动微信
//        WeBinder.INSTANCE.StartWeChat(WString(File(getInstallDir(), "WeChat.exe").absolutePath), WString(File(runDir, "WeHelper.dll").absolutePath))
        val runDir = if (AppUtils.isJarRun) FileUtils.getResourceRootFile().parentFile else FileUtils.getResourceRootFile()
        val openExeFile = File(runDir,"Inject.exe")
        CmdUtils.exec("cmd /c \"${openExeFile.absolutePath}\"", runDir)

        // 启动监听器
        WeListener.start()

        return true
    }

}