package krowdfunding.product

import java.time.LocalDateTime
import javax.persistence.*


/**
 * Querydsl 테스트용 엔티티
 */
@Entity(name = "products")
class Product(
    @Column(name = "name")
    val name: String,
 
    @Column(name = "quantity")
    var quantity: Long,
 
    @Column(name = "registeredAt")
    val registeredAt: LocalDateTime,
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
)