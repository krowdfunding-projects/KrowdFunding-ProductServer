package krowdfunding.product.domain.product

import krowdfunding.product.domain.BaseEntity
import krowdfunding.product.domain.category.CategoryType
import krowdfunding.product.domain.company.Company
import krowdfunding.product.dto.CreateProductDto
import krowdfunding.product.dto.UpdateProductDto
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.UUID
import javax.persistence.*


/**
 * Querydsl 테스트용 엔티티
 */
@Entity
@Table(name = "products" , indexes = [Index(columnList = "productNumber")])
class Product(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    var id: Long? = null,

    @Column(unique = true)
    val productNumber: String,

    @Column(name = "product_quantity")
    var quantity: Int , // 남은 상품의 수

    @Column(name = "product_content")
    var content: String, //Product Content

    @Column(name = "product_target_amount")
    var targetAmount:Long, // 펀딩 목표 금액

    @Column(name = "product_collected_amount")
    var collectedAmount:Long? = null, // 모인 금액

    @Column(name = "product_type")
    @Enumerated(EnumType.STRING)
    var type : CategoryType, //카테고리 유형

    @Column(name = "product_title")
    var title:String, // product 제목

    @Column(name = "product_end_date")
    var fundingEndDate : LocalDate, //funding을 언제까지 하는지

    @Column(name = "funding_activate")
    var isActivated:Boolean? = null, //펀딩이 종료되었는지 유지되는지

    @ElementCollection
    @CollectionTable(
        name = "product_supporter",
        joinColumns = [JoinColumn(name = "member_name")])
    @Column(name = "product_supporter")
    var supporters : MutableSet<String> = mutableSetOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    var company : Company? = null

) : BaseEntity() {
    companion object{
        fun createProduct(createProductDto: CreateProductDto): Product{
            return Product(
                productNumber = UUID.randomUUID().toString(),
                quantity = createProductDto.quantity,
                content = createProductDto.content,
                type = createProductDto.type,
                title = createProductDto.title,
                fundingEndDate = createProductDto.fundingEndDate,
                isActivated = true,
                targetAmount = createProductDto.targetAmount,
            )
        }
    }

    fun updateProductInfo(updateProductDto: UpdateProductDto){
        updateProductDto.quantity?.let {
            this.quantity = it
        }
        updateProductDto.content ?.let {
            this.content = it
        }
        updateProductDto.type ?.let {
            this.type = it
        }
        updateProductDto.title ?.let {
            this.title=it
        }
        updateProductDto.fundingEndDate?.let {
            this.fundingEndDate = it
        }
        updateProductDto.targetAmount?.let {
            this.targetAmount = it
        }
    }

    //TODO GCP 배포 후 이미지 처리 user 도메인 assigner 담당자와 검토후 수정.
    @Column(name = "product_Image_url")
    var productUrl : String? = null
        protected set

    /**
     * 수량 변경
     */
    fun changeQuantity(quantity: Int){
        this.quantity+=quantity
    }

    /**
     * 이미지 부착
     */
    fun attachImage(productUrl : String){
        this.productUrl = productUrl
    }

    /**
     * 이미지 제거
     */
    fun detachImage(){
        this.productUrl = null
    }

    /**
     * 후원하는 서포터가 총 몇명인지.
     */
    fun getSupportersCount():Int{
        return this.supporters.size
    }

    /**
     * 펀딩 종료까지 남은 날짜 계산.
     */
    fun remainDate() : Int{
        val between = ChronoUnit.DAYS.between(this.fundingEndDate, LocalDate.now())
        if(between<0) this.isActivated = false
        return between.toInt()
    }

    fun addSupport(username:String){
        this.supporters.add(username)
    }

    fun settingCompany(company: Company?){

        this.company?.also {
            it.products.remove(this)
        }
        this.company=company
        company?.also { it ->
            it.products.also {
                it.add(this)
            }
        }
        
    }
}