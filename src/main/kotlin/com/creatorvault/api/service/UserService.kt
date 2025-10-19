package com.creatorvault.api.service

import com.creatorvault.api.domain.entity.User
import com.creatorvault.api.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository
) {

    @Transactional
    fun createOrGetUser(email: String): User {
        return userRepository.findByEmail(email)
            ?: userRepository.save(User(email = email))
    }

    fun getAllUsers(): List<User> = userRepository.findAll()

    fun getUserById(id: String): User? =
        userRepository.findById(java.util.UUID.fromString(id)).orElse(null)
}
