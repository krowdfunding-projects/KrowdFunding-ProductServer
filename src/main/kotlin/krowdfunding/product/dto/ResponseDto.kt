package krowdfunding.product.dto

import org.springframework.http.HttpStatus

data class ResponseDto<T>(
        val content : T,
        val status : HttpStatus
    )