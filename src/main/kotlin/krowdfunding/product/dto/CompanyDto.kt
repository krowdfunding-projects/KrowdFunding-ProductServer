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

    val companyIndustrys : Set<CategoryType>
)

