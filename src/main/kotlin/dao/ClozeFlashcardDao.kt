package com.example.dao

import com.example.models.ClozeFlashcard
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object ClozeFlashcards : Table() {
    val id = long("id").autoIncrement()
    val subjectId = long("subjectId")
    val fullText = text("fullText")
    val gaps = text("gaps") // Armazenado como JSON
    val lastLocationId = long("lastLocationId").nullable()
    val reviewTime = long("reviewTime")
    override val primaryKey = PrimaryKey(id)
}

class ClozeFlashcardDao(private val db: Database) {

    init {
        transaction(db) {
            SchemaUtils.create(ClozeFlashcards)
        }
    }

    suspend fun getAll(): List<ClozeFlashcard> = dbQuery {
        ClozeFlashcards.selectAll().map {
            ClozeFlashcard(
                id = it[ClozeFlashcards.id],
                subjectId = it[ClozeFlashcards.subjectId],
                fullText = it[ClozeFlashcards.fullText],
                gaps = Json.decodeFromString(it[ClozeFlashcards.gaps]),
                lastLocationId = it[ClozeFlashcards.lastLocationId],
                reviewTime = it[ClozeFlashcards.reviewTime]
            )
        }
    }

    suspend fun create(card: ClozeFlashcard): Long = dbQuery {
        ClozeFlashcards.insert {
            it[subjectId] = card.subjectId
            it[fullText] = card.fullText
            it[gaps] = Json.encodeToString(card.gaps)
            it[lastLocationId] = card.lastLocationId
            it[reviewTime] = card.reviewTime
        }[ClozeFlashcards.id]
    }

    suspend fun delete(id: Long): Boolean = dbQuery {
        ClozeFlashcards.deleteWhere { ClozeFlashcards.id eq id } > 0
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
