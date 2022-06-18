package krowdfunding.product.repository.product

import krowdfunding.product.dto.ProductInfoDto

interface ProductQueryDslRepository {

    fun getProductInfo(productId : Long) : ProductInfoDto?
}