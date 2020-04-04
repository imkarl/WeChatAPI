package cn.imkarl.wechat.modular

import cn.imkarl.core.common.app.AppUtils
import cn.imkarl.core.common.file.FileUtils
import cn.imkarl.core.common.log.LogUtils
import cn.imkarl.core.common.security.EncryptUtils
import cn.imkarl.utils.CmdUtils
import cn.imkarl.wechat.internal.WeListener
import java.io.File
import java.io.FileNotFoundException

object WeInstall {

    private val VERSIONs by lazy {
        return@lazy hashMapOf(
            "a49a3e22519d5c2bf8d7bd0be0045151" to "2.6.8.52"
        )
    }

    private val InjectFileMD5s by lazy {
        return@lazy arrayOf("34dd746f58dbefd822ae5b182f62c9b3")
    }
    private val WeBinderDllFileMD5s by lazy {
        return@lazy arrayOf(
            "c7ebdc203d13692ff2cce98faa0e5552",
            "1fa147c3dbe9b6893e8a0d26b2b3f232"
        )
    }
    private val WeHelperDllFileMD5s by lazy {
        return@lazy arrayOf(
            "bf3dbda8e64f7e252eb0ea1f93446033",
            "cd7e70b9326f3830bd758e5c8a0adaab",
            "98efadf643c243c7c7db266243fb4e1a"
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
        val runDir = if (AppUtils.isJarRun) FileUtils.getResourceRootFile().parentFile else FileUtils.getResourceRootFile()

        // 检查依赖文件是否存在
        val injectExeFile = File(runDir, "Inject.exe")
        LogUtils.d("Inject: ${EncryptUtils.md5(injectExeFile)}")
        if (!injectExeFile.exists()) {
            throw FileNotFoundException("找不到 ${injectExeFile.name} 文件，请复制该文件到当前运行目录【由于hook程序容易被杀软报毒，请先关闭杀软 或 加入白名单，以避免文件被删】")
        } else if (!InjectFileMD5s.contains(EncryptUtils.md5(injectExeFile)?.toLowerCase())) {
            throw IllegalAccessException("文件 ${injectExeFile.name} 可能被损坏【请校验文件的MD5值】")
        }

        val weBinderDllFile = File(runDir, "WeBinder.dll")
        LogUtils.d("WeBinder: ${EncryptUtils.md5(weBinderDllFile)}")
        if (!weBinderDllFile.exists()) {
            throw FileNotFoundException("找不到 ${weBinderDllFile.name} 文件，请复制该文件到当前运行目录【由于hook程序容易被杀软报毒，请先关闭杀软 或 加入白名单，以避免文件被删】")
        } else if (!WeBinderDllFileMD5s.contains(EncryptUtils.md5(weBinderDllFile)?.toLowerCase())) {
            throw IllegalAccessException("文件 ${weBinderDllFile.name} 可能被损坏【请校验文件的MD5值】")
        }

        val weHelperDllFile = File(runDir, "WeHelper.dll")
        LogUtils.d("wehelper: ${EncryptUtils.md5(weHelperDllFile)}")
        if (!weHelperDllFile.exists()) {
            throw FileNotFoundException("找不到 ${weHelperDllFile.name} 文件，请复制该文件到当前运行目录【由于hook程序容易被杀软报毒，请先关闭杀软 或 加入白名单，以避免文件被删】")
        } else if (!WeHelperDllFileMD5s.contains(EncryptUtils.md5(weHelperDllFile)?.toLowerCase())) {
            throw IllegalAccessException("文件 ${weHelperDllFile.name} 可能被损坏【请校验文件的MD5值】")
        }

        // 启动微信
//        WeBinder.INSTANCE.StartWeChat(WString(File(getInstallDir(), "WeChat.exe").absolutePath), WString(File(runDir, "WeHelper.dll").absolutePath))
        CmdUtils.exec("cmd /c \"${injectExeFile.absolutePath}\"", runDir)

        // 启动监听器
        WeListener.start()

        return true
    }

}