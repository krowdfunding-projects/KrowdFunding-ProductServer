package krowdfunding.product.dto

import krowdfunding.product.domain.category.CategoryType
import java.time.LocalDate
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

class CreateProductDto(

    @Min(value = 10)
    var quantity: Int ,

    @NotBlank
    var content: String, //Product Content

    @NotBlank
    var type : CategoryType,

    @NotBlank
    var title : String,

    @NotBlank
    var fundingEndDate : LocalDate,

    @NotBlank
    var targetAmount : Long,

)

class UpdateProductDto(

    var quantity: Int? = null,
    var content: String? = null,
    var type: CategoryType? = null,
    var title: String? = null,
    var fundingEndDate : LocalDate? = null,
    var targetAmount : Long? = null
)

/**
 * 상품 조회시 나타내어줄 정보.
 */
class ProductInfoDto(
    var quantity: Int? = null,
    var content: String? = null,
    var collectedAmount:Long? = null,
    var type: CategoryType? = null,
    var title: String? = null,
    var fundingEndDate : LocalDate? = null,
    var targetAmount : Long? = null,
    var companyName: String? = null,
    var supportersCnt : Int = 0
)