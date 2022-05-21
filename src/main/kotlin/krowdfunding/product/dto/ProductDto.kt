package krowdfunding.product.dto

import krowdfunding.product.domain.category.CategoryType
import krowdfunding.product.domain.product.Product
import java.time.LocalDate
import javax.persistence.Column

class CreateProductDto(
    var quantity: Int ,
    var content: String, //Product Content
    var type : CategoryType,
    var title : String,
    var fundingEndDate : LocalDate,
    var targetAmount : Long,

)