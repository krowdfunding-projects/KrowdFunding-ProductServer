package krowdfunding.product

import krowdfunding.product.domain.category.CategoryType
import krowdfunding.product.domain.product.Product
import krowdfunding.product.dto.CreateProductDto
import krowdfunding.product.repository.product.ProductRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
@ExtendWith(SpringExtension::class)
@Transactional
class ProductTest @Autowired constructor(
    val productRepository: ProductRepository
){
    @Test
    @Rollback(false)
    fun test(){
        val createProduct = Product.createProduct(
            createProductDto = CreateProductDto(
                quantity = 123,
                content = "abcd",
                type = CategoryType.ANIMAL,
                title = "123",
                fundingEndDate = LocalDate.of(2022, 12, 13),
                targetAmount = 400000L
            )
        )
        productRepository.save(createProduct)

        val count = productRepository.findByIdOrNull(1L)?.let {
            it.addSupport("하하")
            it.getSupportersCount()
        }?: kotlin.run {
            0
        }

        Assertions.assertThat(count).isEqualTo(1)
        
    }
}