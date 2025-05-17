package ru.uoykaii.imgseek.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.uoykaii.imgseek.utils.JwtUtils

@Component
class JwtFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // Пропускаем публичные эндпоинты без проверки
        if (request.servletPath.startsWith("/auth/") ||
            request.servletPath.startsWith("/v3/api-docs") ||
            request.servletPath.startsWith("/swagger-ui")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = extractToken(request)

        try {
            if (token == null) {
                throw JwtAuthenticationException("Missing authorization token")
            }

            if (!JwtUtils.validateToken(token)) {
                throw JwtAuthenticationException("Invalid token")
            }

            val authentication = JwtUtils.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = authentication
            filterChain.doFilter(request, response)

        } catch (e: JwtAuthenticationException) {
            SecurityContextHolder.clearContext()
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.message)
        }
    }

    private fun extractToken(request: HttpServletRequest): String? {
        return request.getHeader("Authorization")
            ?.takeIf { it.startsWith("Bearer ") }
            ?.substring(7)
    }
}

class JwtAuthenticationException(message: String) : RuntimeException(message)