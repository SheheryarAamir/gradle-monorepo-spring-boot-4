package com.example.appservice.entity

import com.example.appservice.service.PostDto
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("posts")
data class Post(
    @Id
    val id: Long? = null,

    @Column("users_id")
    val userId: Long, // References User.id

    val title: String,
    val body: String?,

    @Column("created_at")
    val createdAt: Instant = Instant.now(),

    @Column("created_by")
    val createdBy: String,

    @Column("updated_at")
    val updatedAt: Instant? = null,

    @Column("updated_by")
    val updatedBy: String? = null
)

fun Post.toDto(): PostDto {
    return PostDto(
        userId = this.userId.toString(),
        id = this.id?.toString() ?: "",
        title = this.title,
        body = this.body ?: ""
    )
}