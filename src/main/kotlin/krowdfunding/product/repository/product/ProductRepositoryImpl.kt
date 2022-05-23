package krowdfunding.product.repository.product

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import krowdfunding.product.domain.company.QCompany.*
import krowdfunding.product.domain.product.QProduct.product
import krowdfunding.product.dto.ProductInfoDto

class ProductRepositoryImpl(val jpaQueryFactory: JPAQueryFactory)
    : ProductQueryDslRepository {

    override fun getProductInfo(productNumber: String): ProductInfoDto? {
        return jpaQueryFactory.select(
            Projections.constructor(ProductInfoDto::class.java,
                product.quantity,
                product.content,
                product.collectedAmount,
                product.type,
                product.title,
                product.fundingEndDate,
                product.targetAmount,
                product.company.companyName,
                product.supporters.size()
            )
        ).from(product)
            .leftJoin(product.company, company)
            .distinct()
            .where(product.productNumber.eq(productNumber))
            .fetchFirst()

    }
}