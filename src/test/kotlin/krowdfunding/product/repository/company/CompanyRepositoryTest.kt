package krowdfunding.product.repository.company

import com.querydsl.jpa.impl.JPAQueryFactory
import krowdfunding.product.domain.category.CategoryType
import krowdfunding.product.domain.company.Company
import krowdfunding.product.domain.product.Product
import krowdfunding.product.dto.CreateCompanyDto
import krowdfunding.product.dto.CreateProductDto
import krowdfunding.product.repository.product.ProductRepository
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@ExtendWith(SpringExtension::class)
@DataJpaTest
internal class CompanyRepositoryTest @Autowired constructor(
    val companyRepository: CompanyRepository,
    val productRepository: ProductRepository
){
    @TestConfiguration
    internal class TestConfig(@PersistenceContext val entityManager: EntityManager) {
        @Bean
        fun jpaQueryFactory() = JPAQueryFactory(entityManager)
    }
    @Test
    fun `새로운 회사 등록 테스트`(){
        //given
        val initData = initData()

        //when
        companyRepository.run { findByIdOrNull(initData.CompanyId) }.also {
            it?.let {
                //then
                assertThat(it.companyName).contains("saechimdaeki")
                assertThat(it).isNotNull
                assertThat(it.companyIndustry).contains(CategoryType.FASHION)
                assertThat(it.companyAddress).contains("Seoul", "city")
                assertThat(it.products).isNotEmpty
                assertThat(it.getFollowersCount()).isEqualTo(0)

            } ?: kotlin.run {
                fail("해당 테스트 케이스는 실패입니다")
            }
        }
    }

    @Test
    fun `등록하지 않은 회사 조회는 실패해야한다`(){
        //given
        val initData = initData()

        //when with then
        assertThrows<RuntimeException> {
            companyRepository.run { findByIdOrNull(initData.CompanyId+1) }
                .also {
                    it?.let {
                    assertThat(it.companyName).contains("saechimdaeki")
                    assertThat(it).isNotNull
                    assertThat(it.companyIndustry).contains(CategoryType.FASHION)
                    assertThat(it.companyAddress).contains("Seoul", "city")
                    assertThat(it.products).isNotEmpty
                    assertThat(it.getFollowersCount()).isEqualTo(0)

                } ?: kotlin.run {
                    throw RuntimeException("등록하지 않은 회사를 조회하므로 익셉션을 발생시킵니다")
                }
                }
        }
    }

    @Test
    fun `회사를 등록했을때 상품도 cascade로 인해 같이 등록되어야 한다`(){

        //given
        val initData = initData()
        val product = productRepository.findByIdOrNull(initData.productId)!!

        //when with then
        companyRepository.run { findByIdOrNull(initData.CompanyId) }.also {
            it?.also {
                val companyProduct = it.products[0]

                assertThat(companyProduct).isNotNull
                assertThat(companyProduct.id).isEqualTo(product.id)
                assertThat(companyProduct.title).isEqualTo(product.title)
                assertThat(companyProduct.content).isEqualTo(product.content)
            }?: kotlin.run{
                fail("테스트 케이스 실패입니다 다시 작성합시다")
            }
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