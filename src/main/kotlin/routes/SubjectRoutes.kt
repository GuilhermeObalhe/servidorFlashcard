package com.example.routes

import com.example.dao.SubjectDao
import com.example.models.Subject
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.subjectRoutes(subjectDao: SubjectDao) {
    route("/subjects") {

        get {
            val subjects = subjectDao.getAll()
            call.respond(subjects)
        }

        post {
            val subject = call.receive<Subject>()
            val id = subjectDao.create(subject)
            call.respond(HttpStatusCode.Created, mapOf("id" to id))
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@delete
            }

            val deleted = subjectDao.delete(id)
            if (deleted) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@put
            }

            val updatedSubject = call.receive<Subject>()
            val updated = subjectDao.update(id, updatedSubject)
            if (updated) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}
