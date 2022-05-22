package krowdfunding.product.config

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Configuration
class QueryConfig(
    @PersistenceContext
    val entityManager: EntityManager
) {

    @Bean
    fun jpqQueryFactory() = JPAQueryFactory(entityManager)

}