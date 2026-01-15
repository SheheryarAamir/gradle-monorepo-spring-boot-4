package com.example.appservice.controller

import com.example.appservice.error.ErrorMessage
import com.example.appservice.model.CreateProductRestModel
import com.example.appservice.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.time.Clock

@RestController
@RequestMapping("/products")
class ProductController(private val productService: ProductService) {

    @PostMapping
    suspend fun createProduct(@RequestBody createProductRestModel: CreateProductRestModel): ResponseEntity<Any> {
        try {
            val productId = productService.createProduct(createProductRestModel)
            return ResponseEntity.status(HttpStatus.CREATED).body(productId)
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(
                ErrorMessage(
                    Clock.System.now(),
                    "Product creation failed",
                    "/products",
                ),
            )
        }
    }
}
