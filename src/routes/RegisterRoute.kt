package com.noteapp.routes

import com.noteapp.data.checkIfUserExists
import com.noteapp.data.collections.User
import com.noteapp.data.registerUser
import com.noteapp.data.requests.AccountRequest
import com.noteapp.data.responses.SimpleResponse
import com.noteapp.security.getHashWithSalt
import io.ktor.application.*
import io.ktor.features.ContentTransformationException
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.registerRoute() {
    route("/register") {
        post {
            val request = try {
                call.receive<AccountRequest>()
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val userExists = checkIfUserExists(request.email)
            if (!userExists) {
                if (registerUser(User(request.email, getHashWithSalt(request.password)))) {
                    call.respond(
                        HttpStatusCode.OK,
                        SimpleResponse(true, "Account: ${request.email} successfully created.")
                    )
                } else {
                    call.respond(
                        HttpStatusCode.OK,
                        SimpleResponse(false, "An unknown error occurred. Please try again.")
                    )
                }
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    SimpleResponse(false, "A user with that e-mail already exists.")
                )
            }
        }
    }
}

