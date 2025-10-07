package com.creatorvault.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
    exclude = [UserDetailsServiceAutoConfiguration::class]
)
class CreatorVaultApiApplication

fun main(args: Array<String>) {
    @Suppress("SpreadOperator")
    runApplication<CreatorVaultApiApplication>(*args)
}