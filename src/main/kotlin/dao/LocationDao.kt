package com.example.dao

import com.example.models.Location
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object Locations : Table() {
    val id = long("id").autoIncrement()
    val name = varchar("name", 255)
    override val primaryKey = PrimaryKey(id)
}

class LocationDao(private val db: Database) {

    init {
        transaction(db) {
            SchemaUtils.create(Locations)
        }
    }

    suspend fun getAll(): List<Location> = dbQuery {
        Locations.selectAll().map {
            Location(
                id = it[Locations.id],
                name = it[Locations.name]
            )
        }
    }

    suspend fun create(location: Location): Long = dbQuery {
        Locations.insert {
            it[name] = location.name
        }[Locations.id]
    }

    suspend fun delete(id: Long): Boolean = dbQuery {
        Locations.deleteWhere { Locations.id eq id } > 0
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
