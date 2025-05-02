package com.example.routes

import com.example.dao.BasicFlashcardDao
import com.example.models.BasicFlashcard
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.basicFlashcardRoutes(flashcardDao: BasicFlashcardDao) {
    route("/flashcards/basic") {

        get {
            val flashcards = flashcardDao.getAll()
            call.respond(flashcards)
        }

        post {
            val flashcard = call.receive<BasicFlashcard>()
            val id = flashcardDao.create(flashcard)
            call.respond(HttpStatusCode.Created, mapOf("id" to id))
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@delete
            }

            val deleted = flashcardDao.delete(id)
            if (deleted) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}
