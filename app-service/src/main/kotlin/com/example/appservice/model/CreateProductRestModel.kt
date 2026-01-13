package com.example.appservice.model

import java.math.BigDecimal

data class CreateProductRestModel(val title: String, val price: BigDecimal, val quantity: Int)
