package krowdfunding.product.dto

import krowdfunding.product.domain.category.CategoryType
import javax.validation.constraints.NotBlank

class CreateCompanyDto(

    @NotBlank
    val companyName : String,

    @NotBlank
    val presidentName : String,

    @NotBlank
    val companyAddress : String,

    val industryList : Set<CategoryType>
)

class CompanyInfoDto(

    val companyName: String,

    //회사 사장 이름
    val presidentName:String,

    val companyAddress: String,

    val companyIndustry :MutableSet<CategoryType>,

    val followerCnt : Int
)

