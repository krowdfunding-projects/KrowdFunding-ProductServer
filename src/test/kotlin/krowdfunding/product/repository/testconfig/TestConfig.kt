package krowdfunding.product.service.testconfig

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@TestConfiguration
class TestConfig(
        @PersistenceContext val entityManager: EntityManager
        ) {
        @Bean
        fun jpaQueryFactory() = JPAQueryFactory(entityManager)
}