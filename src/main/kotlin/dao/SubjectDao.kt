package com.example.dao

import com.example.models.Subject
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object Subjects : Table() {
    val id = long("id").autoIncrement()
    val name = varchar("name", 255)
    override val primaryKey = PrimaryKey(id)
}

class SubjectDao(private val db: Database) {

    init {
        transaction(db) {
            SchemaUtils.create(Subjects)
        }
    }

    suspend fun getAll(): List<Subject> = dbQuery {
        Subjects.selectAll().map {
            Subject(
                id = it[Subjects.id],
                name = it[Subjects.name]
            )
        }
    }

    suspend fun create(subject: Subject): Long = dbQuery {
        Subjects.insert {
            it[name] = subject.name
        }[Subjects.id]
    }

    suspend fun delete(id: Long): Boolean = dbQuery {
        Subjects.deleteWhere { Subjects.id eq id } > 0
    }

    suspend fun update(id: Long, subject: Subject): Boolean = dbQuery {
        Subjects.update({ Subjects.id eq id }) {
            it[name] = subject.name
        } > 0
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
