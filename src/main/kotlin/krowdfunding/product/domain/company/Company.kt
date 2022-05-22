package krowdfunding.product.domain.company

import krowdfunding.product.domain.category.CategoryType
import krowdfunding.product.domain.product.Product
import krowdfunding.product.dto.CreateCompanyDto
import javax.persistence.*

@Entity
@Table(name = "companys", indexes = [Index(columnList = "companyName")])
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
    val products : MutableList<Product> = mutableListOf(),

    @ElementCollection
    @CollectionTable(
        name = "company_follower",
        joinColumns = [JoinColumn(name = "member_name")])
    @Column(name = "company_followers")
    var followers : MutableSet<String> = mutableSetOf()


){
    companion object{
        fun createCompany(companyDto: CreateCompanyDto) : Company{
            return Company(
                companyName = companyDto.companyName,
                presidentName = companyDto.presidentName,
                companyAddress = companyDto.companyAddress,
                companyIndustry =  companyDto.companyIndustrys.toMutableSet()
            )
        }
    }

    fun addIndustry(categoryType: CategoryType){
        this.companyIndustry.add(categoryType)
    }

    fun getFollowersCount() : Int{
        return followers.size
    }

    fun addCompanyProduct(product:Product){
        this.products.add(product)
        product.company = this
    }
}