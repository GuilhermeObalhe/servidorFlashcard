package com.example.dao

import com.example.models.QuizFlashcard
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object QuizFlashcards : Table() {
    val id = long("id").autoIncrement()
    val subjectId = long("subjectId")
    val question = text("question")
    val options = text("options") // Armazenado como JSON
    val correctIndex = integer("correctIndex")
    val lastLocationId = long("lastLocationId").nullable()
    val reviewTime = long("reviewTime")

    override val primaryKey = PrimaryKey(id)
}

class QuizFlashcardDao(private val db: Database) {

    init {
        transaction(db) {
            SchemaUtils.create(QuizFlashcards)
        }
    }

    suspend fun getAll(): List<QuizFlashcard> = dbQuery {
        QuizFlashcards.selectAll().map {
            QuizFlashcard(
                id = it[QuizFlashcards.id],
                subjectId = it[QuizFlashcards.subjectId],
                question = it[QuizFlashcards.question],
                options = Json.decodeFromString(it[QuizFlashcards.options]),
                correctIndex = it[QuizFlashcards.correctIndex],
                lastLocationId = it[QuizFlashcards.lastLocationId],
                reviewTime = it[QuizFlashcards.reviewTime]
            )
        }
    }

    suspend fun create(flashcard: QuizFlashcard): Long = dbQuery {
        QuizFlashcards.insert {
            it[subjectId] = flashcard.subjectId
            it[question] = flashcard.question
            it[options] = Json.encodeToString(flashcard.options)
            it[correctIndex] = flashcard.correctIndex
            it[lastLocationId] = flashcard.lastLocationId
            it[reviewTime] = flashcard.reviewTime
        }[QuizFlashcards.id]
    }

    suspend fun delete(id: Long): Boolean = dbQuery {
        QuizFlashcards.deleteWhere { QuizFlashcards.id eq id } > 0
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
