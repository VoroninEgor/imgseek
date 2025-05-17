package ru.uoykaii.imgseek.service

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.uoykaii.imgseek.entity.User
import ru.uoykaii.imgseek.repository.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findByUsername(username)?.toUserDetails()
            ?: throw UsernameNotFoundException("User not found")
    }

    fun existsByUsername(username: String): Boolean {
        return userRepository.findByUsername(username) != null
    }

    fun createUser(username: String, password: String, role: String): User {
        val user = User(
            username = username,
            password = password,
            role = role
        )
        userRepository.save(user)
        return user
    }

    // Вспомогательная функция преобразования
    private fun User.toUserDetails(): UserDetails {
        return org.springframework.security.core.userdetails.User(
            username,
            password,
            listOf(SimpleGrantedAuthority("ROLE_$role"))
        )
    }
}