package cn.imkarl.wechat

import cn.imkarl.wechat.modular.*

/**
 * 微信相关API的入口类
 */
object We {

    val install = WeInstall
    val login = WeLoginModule
    val message = WeMessageModule
    val user = WeUserModule
    val group = WeGroupModule
    val transfer = WeTransferModule

}