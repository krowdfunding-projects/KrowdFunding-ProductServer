package krowdfunding.product.repository.product

import krowdfunding.product.domain.product.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.QueryHints
import org.springframework.data.repository.query.Param
import javax.persistence.LockModeType
import javax.persistence.QueryHint

interface ProductRepository :JpaRepository<Product,Long> ,ProductQueryDslRepository {

    @Query("select p from Product p where p.id = :productId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(QueryHint(name = "javax.persistence.lock.timeout", value = "3000"))
    fun findByProductForUpdate(@Param("productId") productId: Long): Product?

    @Query("select p from Product p where p.id = :productId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByProductIdForUpdate(productId : Long) : Product?

}