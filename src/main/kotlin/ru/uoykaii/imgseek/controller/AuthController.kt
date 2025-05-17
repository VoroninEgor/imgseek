package ru.uoykaii.imgseek.controller

import org.springframework.http.HttpStatus
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import ru.uoykaii.imgseek.service.UserService
import ru.uoykaii.imgseek.utils.JwtUtils

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): AuthResponse {
        val user = userService.loadUserByUsername(request.username)

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw BadCredentialsException("Invalid credentials")
        }

        // Получаем роли из UserDetails
        val roles = user.authorities.map { it.authority }
        val token = JwtUtils.generateToken(user.username, roles)

        return AuthResponse(
            token = token,
            username = user.username,
            roles = roles
        )
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody request: RegisterRequest): AuthResponse {
        if (userService.existsByUsername(request.username)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists")
        }

        val encodedPassword = passwordEncoder.encode(request.password)
        val user = userService.createUser(
            username = request.username,
            password = encodedPassword,
            role = "USER"
        )

        val roles = listOf(user.role)
        val token = JwtUtils.generateToken(user.username, roles)

        return AuthResponse(
            token = token,
            username = user.username,
            roles = roles
        )
    }
}

// DTOs
data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val username: String,
    val roles: Collection<String>
)