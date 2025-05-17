package ru.uoykaii.imgseek.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table("shop")
data class Shop(
    val uid: UUID,
    var name: String,
    var locked: Boolean = false,
    var callback: String
) {
    @Id
    var id: Long? = null
    var s3Url: String? = null
    var s3Key: String? = null
    var s3SecretKey: String? = null
    var s3Bucket: String? = null
}
