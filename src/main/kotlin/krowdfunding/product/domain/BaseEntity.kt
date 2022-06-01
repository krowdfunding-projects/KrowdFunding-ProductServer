package krowdfunding.product.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
class BaseEntity {
    @CreatedDate
    @Column(name = "created_date")
    var createdDate:LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    @Column(name = "modified_date")
    var modifiedDate : LocalDateTime = LocalDateTime.now()
}