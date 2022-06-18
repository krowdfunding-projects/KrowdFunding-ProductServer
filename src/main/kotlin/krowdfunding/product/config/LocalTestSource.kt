package krowdfunding.product.config

import krowdfunding.product.domain.category.CategoryType
import krowdfunding.product.domain.company.Company
import krowdfunding.product.domain.product.Product
import krowdfunding.product.dto.CreateCompanyDto
import krowdfunding.product.dto.CreateProductDto
import krowdfunding.product.repository.company.CompanyRepository
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
@Profile("local || prod")
class LocalTestSource(
    val companyRepository: CompanyRepository
) {

    @EventListener(ApplicationReadyEvent::class)
    fun initData(){
        val company = Company.createCompany(
            CreateCompanyDto(
                companyName = "LG",
                presidentName = "구광모",
                companyAddress = "서울시 마포구",
                industryList = mutableSetOf(CategoryType.TECH)
            )
        )
        val createProduct = Product.createProduct(
            CreateProductDto(
                quantity = 10000,
                price = 500,
                content = "LG 에어컨 휘센 빙판위의 김연아 ",
                type = CategoryType.TECH,
                title = "LG 에어컨 휘센",
                fundingEndDate = LocalDate.of(2024, 12, 31),
                targetAmount = 10000000L
            )
        )
        company.products.add(createProduct)
        companyRepository.save(company)
    }
}