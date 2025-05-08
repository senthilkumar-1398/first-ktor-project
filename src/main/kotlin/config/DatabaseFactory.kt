package com.example.config

import org.jetbrains.exposed.sql.Database

object DatabaseFactory {
    fun init() {
        Database.connect(
            url = "jdbc:mysql://localhost:3306/ktor-first-project",
            driver = "com.mysql.cj.jdbc.Driver",
            user = "root",
            password = "Test@123"
        )
    }
}
