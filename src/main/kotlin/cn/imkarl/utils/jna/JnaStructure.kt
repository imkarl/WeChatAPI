package cn.imkarl.utils.jna

import cn.imkarl.wechat.message.WMMessage
import com.sun.jna.Pointer

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class JnaField(
    val order: Int,
    val type: JnaType,
    val size: Int = -1
)

enum class JnaType {
    INT,
    STRING,
    BOOLEAN
}

object JnaStructure {

    inline fun <reified T : WMMessage> newInstance(pointer: Pointer): T {
        return newInstance(T::class.java, pointer)
    }

    fun <T> newInstance(clazz: Class<T>, pointer: Pointer): T {
        val instance = clazz.newInstance()

        val fields = clazz.fields
        val declaredFields = clazz.declaredFields
            .map {
                it.isAccessible = true
                it
            }

        var offset = 0L
        fields.plus(declaredFields)
            .filter { it.getAnnotation(JnaField::class.java) != null }
            .sortedBy { it.getAnnotation(JnaField::class.java)?.order }
            .forEach {
                val jnaField = it.getAnnotation(JnaField::class.java)

                when (jnaField.type) {
                    JnaType.INT -> {
                        it.set(instance, pointer.getInt(offset))
                        offset += 4
                    }
                    JnaType.STRING -> {
                        it.set(instance, pointer.getWideString(offset))
                        offset += jnaField.size * 2
                    }
                    JnaType.BOOLEAN -> {
                        it.set(instance, pointer.getInt(offset) == 1)
                        offset += 4
                    }
                }
            }

        return instance
    }

}
