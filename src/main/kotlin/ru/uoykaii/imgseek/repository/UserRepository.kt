package ru.uoykaii.imgseek.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.uoykaii.imgseek.entity.User

@Repository
interface UserRepository : CrudRepository<User, Long> {
    fun findByUsername(username: String): User?
}