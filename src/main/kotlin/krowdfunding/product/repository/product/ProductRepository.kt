package krowdfunding.product.repository.product

import krowdfunding.product.domain.product.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository :JpaRepository<Product,Long> {

}