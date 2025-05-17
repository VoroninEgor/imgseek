package ru.uoykaii.imgseek.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("users")
data class User(
    val username: String,
    val password: String,
    val role: String
) {
    @Id
    var id: Long? = null
}
