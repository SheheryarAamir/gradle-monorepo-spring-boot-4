package com.example.appservice.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("users")
data class User(
    @Id
    val id: Long? = null,
    val name: String,
    val email: String,
    @Column("mobile_number")
    val mobileNumber: String,
    @Column("created_at")
    val createdAt: Instant = Instant.now(),
    @Column("created_by")
    val createdBy: String,
    @Column("updated_at")
    val updatedAt: Instant? = null,
    @Column("updated_by")
    val updatedBy: String? = null,
)
