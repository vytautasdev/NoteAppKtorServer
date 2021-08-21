package com.noteapp.routes

import com.noteapp.data.checkPasswordForEmail
import com.noteapp.data.requests.AccountRequest
import com.noteapp.data.responses.SimpleResponse
import io.ktor.application.*
import io.ktor.features.ContentTransformationException
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.loginRoute() {
    route("/login") {
        post {
            val request = try {
                call.receive<AccountRequest>()
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val isPasswordCorrect = checkPasswordForEmail(request.email, request.password)
            if (isPasswordCorrect) {
                call.respond(OK, SimpleResponse(true, "Your are now logged in as: ${request.email}"))
            } else {
                call.respond(OK, SimpleResponse(false, "No user found with this email/password. Please try again."))
            }
        }
    }
}