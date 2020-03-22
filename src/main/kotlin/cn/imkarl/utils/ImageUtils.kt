package cn.imkarl.utils

import cn.imkarl.core.common.file.FileUtils
import java.awt.Image
import java.awt.Toolkit
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import javax.imageio.ImageIO
import javax.swing.ImageIcon

/**
 * 图片相关
 * @author imkarl
 */
object ImageUtils {

    fun getBufferedImage(icon: File): BufferedImage = ImageIO.read(icon)

    fun getImage(icon: File): Image = getImage(icon.toURI().toURL())
    fun getImage(icon: URL): Image {
        return Toolkit.getDefaultToolkit().getImage(icon.toURI().toURL())
    }

}

fun Image.toIcon(): ImageIcon {
    return ImageIcon(this)
}

fun BufferedImage.writeTo(format: String = "png", file: File) {
    FileUtils.mkdirs(file.parentFile)
    FileUtils.createNewFile(file)
    ImageIO.write(this, format, file)
}
