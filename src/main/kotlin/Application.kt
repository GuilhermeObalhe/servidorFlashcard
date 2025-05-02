package com.example

import com.example.dao.LocationDao
import com.example.routes.locationRoutes
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.Database

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    // Conexão com banco (SQLite ou H2 persistente)
    val db = Database.connect("jdbc:sqlite:flashcards.db", "org.sqlite.JDBC")

    // Inicializa DAO
    val locationDao = LocationDao(db)

    // Plugins
    install(ContentNegotiation) {
        json()
    }

    // Rotas
    routing {
        locationRoutes(locationDao)
    }
}
