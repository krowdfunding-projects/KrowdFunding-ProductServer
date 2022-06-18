package krowdfunding.product.service.product

import krowdfunding.product.domain.product.Product
import krowdfunding.product.dto.CreateProductDto
import krowdfunding.product.dto.ProductInfoDto
import krowdfunding.product.dto.UpdateProductDto
import krowdfunding.product.repository.company.CompanyRepository
import krowdfunding.product.repository.product.ProductRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductService(
    val productRepository: ProductRepository,
    val companyRepository: CompanyRepository
) {

    @Transactional
    fun createNewProduct(createProductDto: CreateProductDto , companyId:Long): Long {

        companyRepository.findByIdOrNull(companyId)?.let {
            val save = productRepository.save(Product.createProduct(createProductDto))
            it.products.add(save)
            return save.id ?: throw RuntimeException("새로운 상품을 만드는데 실패하였습니다")
        }
        throw RuntimeException("새로운 상품을 만드는데 실패하였습니다")
    }

    @Transactional
    fun updateProduct(productId: Long, updateProductDto: UpdateProductDto) :String {
        val product = (productRepository.findByProductForUpdate(productId)
            ?: throw RuntimeException("해당 상품은 존재하지 않습니다")).also {
            it.updateProductInfo(updateProductDto)
        }
        // 상품 업데이트 성공시 productNumber를 그대로 반환.
        return product.productNumber
    }

    @Transactional
    fun orderProduct(productId: Long,number_of_product_orders : Int ,fundingUser_username:String){
        with(productRepository) { findByProductForUpdate(productId) }?.also {
            it.fundingSupport(number_of_product_orders, fundingUser_username)
        }
    }

    fun getProductInfo(productId: Long) :ProductInfoDto{
        productRepository.getProductInfo(productId = productId)?.let {
            return it
        }
        throw RuntimeException("해당 상품을 찾을 수 없습니다")
    }
}