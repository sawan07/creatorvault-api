package com.creatorvault.api.controller

import com.creatorvault.api.model.UserRequest
import com.creatorvault.api.model.UserResponse
import com.creatorvault.api.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {

    @GetMapping
    fun listUsers(): List<UserResponse> =
        userService.getAllUsers().map {
            UserResponse(
                id = it.id!!,
                email = it.email,
                createdAt = it.createdAt
            )
        }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: String): UserResponse? {
        val user = userService.getUserById(id) ?: return null
        return UserResponse(
            id = user.id!!,
            email = user.email,
            createdAt = user.createdAt
        )
    }

    @PostMapping
    fun createUser(@RequestBody req: UserRequest): UserResponse {
        val user = userService.createOrGetUser(req.email)
        return UserResponse(
            id = user.id!!,
            email = user.email,
            createdAt = user.createdAt
        )
    }

}
