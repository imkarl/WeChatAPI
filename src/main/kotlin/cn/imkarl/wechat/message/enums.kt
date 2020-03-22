package cn.imkarl.wechat.message

import com.google.gson.annotations.SerializedName

/**
 * 消息来源类型
 */
enum class SourceType {
    @SerializedName("1")
    FileHelper,
    @SerializedName("2")
    GroupChat,
    @SerializedName("3")
    FriendChat,
    @SerializedName("4")
    Subscription,
    @SerializedName("0")
    Unknown;
}

/**
 * 转账支付类型
 */
enum class TransferPayType(val code: Int) {
    @SerializedName("1")
    Create(1),  // 发起转账
    @SerializedName("3")
    Complete(3),  // 确认收款
    @SerializedName("0")
    Unknown(0);

    companion object {
        fun parse(code: Int?): TransferPayType {
            if (code == null) {
                return Unknown
            }
            return values().firstOrNull { it.code == code } ?: Unknown
        }
    }
}

/**
 * 性别
 */
enum class Gender(val code: Int) {
    @SerializedName("1")
    Main(1),  // 男
    @SerializedName("2")
    Woman(2),  // nv
    @SerializedName("0")
    Unknown(0);

    companion object {
        fun parse(code: Int?): Gender {
            if (code == null) {
                return Unknown
            }
            return values().firstOrNull { it.code == code } ?: Unknown
        }
    }
}
