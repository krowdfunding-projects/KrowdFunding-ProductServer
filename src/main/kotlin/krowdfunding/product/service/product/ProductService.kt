package krowdfunding.product.service.product

import krowdfunding.product.domain.product.Product
import krowdfunding.product.dto.CreateProductDto
import krowdfunding.product.dto.ProductInfoDto
import krowdfunding.product.dto.UpdateProductDto
import krowdfunding.product.repository.product.ProductRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.LockModeType

@Service
@Transactional(readOnly = true)
class ProductService(
    val productRepository: ProductRepository
) {

    @Transactional
    fun createNewProduct(createProductDto: CreateProductDto): Long {
        val newProduct = Product.createProduct(createProductDto)
        val save = productRepository.save(newProduct)
        return save.id ?: throw RuntimeException("새로운 상품을 만드는데 실패하였습니다")
    }

    @Transactional
    fun updateProduct(productNumber: String, updateProductDto: UpdateProductDto) :String {
        val product = (productRepository.findByProductNumberForUpdate(productNumber)
            ?: throw RuntimeException("해당 상품은 존재하지 않습니다")).also {
            it.updateProductInfo(updateProductDto)
        }
        // 상품 업데이트 성공시 productNumber를 그대로 반환.
        return product.productNumber
    }

    @Transactional
    fun orderProduct(product_Number: String,number_of_product_orders : Int ,fundingUser_username:String){
        with(productRepository) { findByProductNumberForUpdate(product_Number) }?.also {
            it.fundingSupport(number_of_product_orders, fundingUser_username)
        }
    }
}