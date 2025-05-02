package com.example

import com.example.dao.BasicFlashcardDao
import com.example.dao.ClozeFlashcardDao
import com.example.dao.LocationDao
import com.example.dao.QuizFlashcardDao
import com.example.dao.SubjectDao
import com.example.routes.basicFlashcardRoutes
import com.example.routes.clozeFlashcardRoutes
import com.example.routes.locationRoutes
import com.example.routes.quizFlashcardRoutes
import com.example.routes.subjectRoutes
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.route
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
    val basicFlashcardDao = BasicFlashcardDao(db)
    val subjectDao = SubjectDao(db)
    val quizFlashcardDao = QuizFlashcardDao(db)
    val clozeFlashcardDao = ClozeFlashcardDao(db)

    // Plugins
    install(ContentNegotiation) {
        json()
    }

    // Rotas
    routing {
        get("/"){
            call.respondText("API de flashcards \n Rotas: /locations \n/subjects \n/flashcards/basic \n/flashcards/quiz \n/flashcards/cloze")
        }
        locationRoutes(locationDao)
        basicFlashcardRoutes(basicFlashcardDao)
        subjectRoutes(subjectDao)
        quizFlashcardRoutes(quizFlashcardDao)
        clozeFlashcardRoutes(clozeFlashcardDao)
    }
}
