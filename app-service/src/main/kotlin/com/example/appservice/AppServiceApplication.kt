package com.example.appservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories

@SpringBootApplication
@EnableJdbcRepositories
class AppServiceApplication

fun main(args: Array<String>) {
    runApplication<AppServiceApplication>(*args)
}
