package krowdfunding.product.service.product

import krowdfunding.product.domain.product.Product
import krowdfunding.product.dto.CreateProductDto
import krowdfunding.product.dto.ProductInfoDto
import krowdfunding.product.dto.UpdateProductDto
import krowdfunding.product.repository.product.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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
        val product = (productRepository.findByProductNumber(productNumber)
            ?: throw RuntimeException("해당 상품은 존재하지 않습니다")).also {
            it.updateProductInfo(updateProductDto)
        }
        // 상품 업데이트 성공시 productNumber를 그대로 반환.
        return product.productNumber
    }

    fun getProductInfo(productNumber : String) : ProductInfoDto{
        return productRepository.getProductInfo(productNumber)
            ?: throw RuntimeException("해당 상품은 존재하지 않습니다")
    }
}