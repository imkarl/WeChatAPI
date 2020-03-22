package cn.imkarl.utils.qrcode

import cn.imkarl.core.common.log.LogUtils
import cn.imkarl.utils.ImageUtils
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.google.zxing.common.HybridBinarizer
import java.awt.image.BufferedImage
import java.io.File

/**
 * 二维码相关工具类
 * @author imkarl
 */
object QrcodeUtils {

    /**
     * 解码得到二维码内容
     */
    fun decode(imageFile: File): String? {
        return decode(
            ImageUtils.getBufferedImage(
                imageFile
            )
        )
    }
    /**
     * 解码得到二维码内容
     */
    fun decode(image: BufferedImage): String? {
        return try {
            val source = BufferedImageLuminanceSource(image)
            val bitmap = BinaryBitmap(HybridBinarizer(source))
            val result = MultiFormatReader().decode(bitmap)
            result.text
        } catch (e: Throwable) {
            LogUtils.e(e)
            null
        }
    }

}
