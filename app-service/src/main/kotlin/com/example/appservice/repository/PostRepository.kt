package com.example.appservice.repository

import com.example.appservice.entity.Post
import org.springframework.data.repository.ListCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : ListCrudRepository<Post, Long>
