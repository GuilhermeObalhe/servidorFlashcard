package com.example.routes

import com.example.dao.LocationDao
import com.example.models.Location
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.locationRoutes(locationDao: LocationDao) {
    route("/locations") {

        get {
            val locations = locationDao.getAll()
            call.respond(locations)
        }

        post {
            val location = call.receive<Location>()
            val id = locationDao.create(location)
            call.respond(HttpStatusCode.Created, mapOf("id" to id))
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid ID")
                return@delete
            }

            val deleted = locationDao.delete(id)
            if (deleted) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}
