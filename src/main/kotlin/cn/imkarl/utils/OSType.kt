package cn.imkarl.utils

/**
 * 系统类型
 * @author imkarl
 */
enum class OSType {
    WINDOWS, LINUX, MACOSX, UNKNOWN;

    companion object {

        /**
         * 获取当前系统类型
         */
        fun getOSType(): OSType {
            val osName = System.getProperty("os.name")?.toLowerCase() ?: return UNKNOWN
            return when {
                osName.contains("windows") -> WINDOWS
                osName.contains("linux") -> LINUX
                osName.contains("os x") -> MACOSX
                else -> UNKNOWN
            }
        }

    }

}
