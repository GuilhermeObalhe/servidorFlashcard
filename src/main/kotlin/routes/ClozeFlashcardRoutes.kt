package com.example.routes

import com.example.dao.ClozeFlashcardDao
import com.example.models.ClozeFlashcard
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.clozeFlashcardRoutes(flashcardDao: ClozeFlashcardDao) {
    route("/flashcards/cloze") {

        get {
            val flashcards = flashcardDao.getAll()
            call.respond(flashcards)
        }

        post {
            val flashcard = call.receive<ClozeFlashcard>()
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
