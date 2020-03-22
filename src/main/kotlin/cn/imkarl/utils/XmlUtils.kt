package cn.imkarl.utils

import cn.imkarl.core.common.encode.EncodeUtils
import cn.imkarl.core.common.log.LogUtils
import java.io.ByteArrayOutputStream
import javax.xml.bind.JAXBContext
import kotlin.reflect.KClass

/**
 * XML文件解析相关
 */
object XmlUtils {

    inline fun <reified T : Any> toXml(obj: Any): String? {
        return try {
            val marshaller = getJaxbContext(T::class).createMarshaller()
            val baos = ByteArrayOutputStream()
            marshaller.marshal(obj, baos)
            baos.toString(EncodeUtils.UTF_8)
        } catch (throwable: Throwable) {
            LogUtils.e(throwable)
            null
        }
    }

    inline fun <reified T : Any> fromXml(xmlContent: String): T? {
        return try {
            val unmarshaller = getJaxbContext(T::class).createUnmarshaller()
            unmarshaller.unmarshal(xmlContent.byteInputStream()) as? T
        } catch (throwable: Throwable) {
            LogUtils.e(throwable)
            null
        }
    }


    private val jaxbContextCache by lazy { HashMap<KClass<*>, JAXBContext>() }
    fun getJaxbContext(clazz: KClass<*>): JAXBContext {
        var jaxbContext = jaxbContextCache[clazz]
        if (jaxbContext == null) {
            jaxbContext = JAXBContext.newInstance(clazz.java)
            jaxbContextCache[clazz] = jaxbContext
        }
        return jaxbContext!!
    }

}