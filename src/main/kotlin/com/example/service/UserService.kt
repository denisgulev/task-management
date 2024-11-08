package com.example.service

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.slf4j.LoggerFactory

class UserService(private val client: HttpClient, private val baseUrl: String) {

    companion object {
        private val logger = LoggerFactory.getLogger(UserService::class.java)
    }

    suspend fun getPermissions(token: String, action: String): Boolean {
        return try {
            logger.info("Token being sent: $token")
            val response: HttpResponse = client.get("$baseUrl/users/has-permission/$action") {
                headers {
                    set(HttpHeaders.Authorization, token) // token already contains "Bearer"
                }
            }
            response.status == HttpStatusCode.OK
        } catch (e: Exception) {
            logger.error("Error occurred while fetching tasks: ${e.message}")
            false
        }
    }
}