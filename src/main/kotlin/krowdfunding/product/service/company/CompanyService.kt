package krowdfunding.product.service.company

import krowdfunding.product.domain.company.Company
import krowdfunding.product.dto.CreateCompanyDto
import krowdfunding.product.repository.company.CompanyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CompanyService (
    val companyRepository: CompanyRepository)
{

    @Transactional
    fun createNewCompany(createCompanyDto: CreateCompanyDto): Long{
        val createCompany = Company.createCompany(createCompanyDto)
        return createCompany.id?:throw RuntimeException("회사를 등록하는데 실패하였습니다")
    }
}