package krowdfunding.product.service.company

import krowdfunding.product.domain.company.Company
import krowdfunding.product.dto.CompanyInfoDto
import krowdfunding.product.dto.CreateCompanyDto
import krowdfunding.product.repository.company.CompanyRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CompanyService (
    val companyRepository: CompanyRepository)
{

    @Transactional
    fun createNewCompany(createCompanyDto: CreateCompanyDto): Long{
        val createCompany = companyRepository.save(Company.createCompany(createCompanyDto))
        return createCompany.id?:throw RuntimeException("회사를 등록하는데 실패하였습니다")
    }

    fun getCompanyInfo(companyId: Long): CompanyInfoDto {
        companyRepository.findByIdOrNull(companyId)?.let {
            return CompanyInfoDto(
                companyName = it.companyName,
                presidentName = it.presidentName,
                companyAddress = it.companyAddress,
                companyIndustry = it.companyIndustry,
                followerCnt = it.getFollowersCount()
            )
        }
        throw RuntimeException("예외 정의 필요 (없는 id 조회입니다)")
    }


}