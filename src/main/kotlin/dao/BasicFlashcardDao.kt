package com.example.dao

import com.example.models.BasicFlashcard
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object BasicFlashcards : Table() {
    val id = long("id").autoIncrement()
    val subjectId = long("subjectId")
    val front = text("front")
    val back = text("back")
    val lastLocationId = long("lastLocationId").nullable()
    val reviewTime = long("reviewTime")
    override val primaryKey = PrimaryKey(id)
}

class BasicFlashcardDao(private val db: Database) {

    init {
        transaction(db) {
            SchemaUtils.create(BasicFlashcards)
        }
    }

    suspend fun getAll(): List<BasicFlashcard> = dbQuery {
        BasicFlashcards.selectAll().map {
            BasicFlashcard(
                id = it[BasicFlashcards.id],
                subjectId = it[BasicFlashcards.subjectId],
                front = it[BasicFlashcards.front],
                back = it[BasicFlashcards.back],
                lastLocationId = it[BasicFlashcards.lastLocationId],
                reviewTime = it[BasicFlashcards.reviewTime]
            )
        }
    }

    suspend fun create(flashcard: BasicFlashcard): Long = dbQuery {
        BasicFlashcards.insert {
            it[subjectId] = flashcard.subjectId
            it[front] = flashcard.front
            it[back] = flashcard.back
            it[lastLocationId] = flashcard.lastLocationId
            it[reviewTime] = flashcard.reviewTime
        }[BasicFlashcards.id]
    }

    suspend fun delete(id: Long): Boolean = dbQuery {
        BasicFlashcards.deleteWhere { BasicFlashcards.id eq id } > 0
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
