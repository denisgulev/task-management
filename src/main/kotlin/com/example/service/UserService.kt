package com.example.service

import com.example.entities.UserInfo
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.slf4j.LoggerFactory

class UserService(private val client: HttpClient, private val baseUrl: String) {

    companion object {
        private val logger = LoggerFactory.getLogger(UserService::class.java)
    }

    suspend fun getPermissions(token: String, action: String): UserInfo {
        return try {
            logger.info("(getPermissions) - Token being sent: $token")
            val response: HttpResponse = client.get("$baseUrl/users/has-permission/$action") {
                headers {
                    set(HttpHeaders.Authorization, token) // token already contains "Bearer"
                }
            }
            logger.info("Response received: $response")
            logger.info("Response body: ${response.bodyAsText()}")
            UserInfo(
                hasPermission = response.status == HttpStatusCode.OK,
                userId = response.bodyAsText()
            )
        } catch (e: Exception) {
            logger.error("Error occurred while fetching tasks: ${e.message}")
            UserInfo(
                hasPermission = false,
                userId = ""
            )
        }
    }
}