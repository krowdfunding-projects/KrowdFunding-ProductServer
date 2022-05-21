package krowdfunding.product.domain.company

import krowdfunding.product.domain.category.CategoryType
import krowdfunding.product.domain.product.Product
import javax.persistence.*

@Entity
@Table(name = "companys")
class Company (
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "company_id")
    var id:Long? = null,

    @Column(unique = true)
    val companyName: String,

    //회사 사장 이름
    var presidentName:String,

    var companyAddress: String,

    @ElementCollection
    var companyIndustry : MutableSet<CategoryType> = mutableSetOf(),

    @OneToMany(mappedBy = "company", cascade = [CascadeType.ALL])
    val products : MutableList<Product> = mutableListOf()
){
    companion object{
        fun createCompany(companyName: String, presidentName: String, companyAddress: String ,companyIndustry: MutableSet<CategoryType>) : Company{
            return Company(
                companyName = companyName,
                presidentName = presidentName,
                companyAddress = companyAddress,
                companyIndustry = companyIndustry
            )
        }
    }

    fun addIndustry(categoryType: CategoryType){
        this.companyIndustry.add(categoryType)
    }


}