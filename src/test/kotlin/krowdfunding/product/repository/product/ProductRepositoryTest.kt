package krowdfunding.product.repository.product

import krowdfunding.product.domain.category.CategoryType
import krowdfunding.product.domain.company.Company
import krowdfunding.product.domain.product.Product
import krowdfunding.product.dto.CreateCompanyDto
import krowdfunding.product.dto.CreateProductDto
import krowdfunding.product.repository.company.CompanyRepository
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
@ExtendWith(SpringExtension::class)
@Transactional
internal class ProductRepositoryTest @Autowired constructor(
    val productRepository: ProductRepository,
    val companyRepository: CompanyRepository
){


    @Test
    fun `새로운 상품 생성 후 조회`() {

        //given
        val initData = initData()

        //when
        productRepository.run { findByIdOrNull(initData.productId) }
            .also {
                it?.let {

                    //then
                    assertThat(it.type).isNotNull
                    assertThat(it.type).isEqualTo(CategoryType.TECH)
                    assertThat(it.title).contains("Mac", "m2")
                    assertThat(it.fundingEndDate).isAfter(LocalDate.now())
                } ?: kotlin.run {
                    fail("없는 ID를 조회하여 테스트 케이스 실패입니다.")
                }
            }
    }

    @Test
    fun `새로운 상품 생성 후 없는 상품 ID 조회`() {

        //given
        val initData = initData()

        //when with then
        assertThrows<RuntimeException> {
            productRepository.run { findByIdOrNull(initData.productId + 1) }
                .also {
                    it?.let {
                        assertThat(it.type).isNotNull
                        assertThat(it.type).isEqualTo(CategoryType.TECH)
                        assertThat(it.title).contains("Mac", "m2")
                        assertThat(it.fundingEndDate).isAfter(LocalDate.now())
                    } ?: kotlin.run {
                        throw RuntimeException("존재하지 않는 상품 레포지토리 테스트 대역.")
                    }
                }
        }
    }

    @Test
    fun `Query_DSL 조회한 ProductInfo 조회 테스트`(){

        //given
        val initData = initData()

        //when
        //현재 설계를 product_Id가 아닌 uuid로 조회하였기 때문에 한번 조회를 함.
        val product = productRepository.findByIdOrNull(initData.productId)
            ?: throw RuntimeException("없는 상품 Id입니다")

        val number = product.productNumber
        //then
        productRepository.run { getProductInfo(number) }?.also {
            assertThat(it).isNotNull
            assertThat(it.companyName).contains("saechimdaeki","Inc")
            assertThat(it.content).isNotEmpty()
        }?: kotlin.run {
            fail("테스트 케이스 실패입니다")
        }

    }


    fun initData() : productAndCompanyId {
        val newCompany = Company.createCompany(
            CreateCompanyDto(
                companyName = "saechimdaeki Inc",
                presidentName = "saechimdaeki",
                companyAddress = "Seoul city",
                companyIndustrys = setOf(CategoryType.FASHION, CategoryType.TECH)
            )
        )

        val newProduct = Product.createProduct(
            CreateProductDto(
                quantity = 100,
                content = "Mac m1 Apple",
                type = CategoryType.TECH,
                title = "Mac m2 investment recruit",
                fundingEndDate = LocalDate.of(2024, 12, 31),
                targetAmount = 24000000L
            )
        )

        newCompany.addCompanyProduct(newProduct)
        val company = companyRepository.save(newCompany)
        val productId = company.products[0].id

        return productAndCompanyId(productId!!,company.id!!)
    }

    data class productAndCompanyId(val productId : Long, val CompanyId : Long)

}