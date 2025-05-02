package com.example.routes

import com.example.dao.QuizFlashcardDao
import com.example.models.QuizFlashcard
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.quizFlashcardRoutes(flashcardDao: QuizFlashcardDao) {
    route("/flashcards/quiz") {

        get {
            val flashcards = flashcardDao.getAll()
            call.respond(flashcards)
        }

        post {
            val flashcard = call.receive<QuizFlashcard>()
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
