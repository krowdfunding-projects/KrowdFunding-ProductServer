package krowdfunding.product.repository.company

import krowdfunding.product.domain.company.Company
import org.springframework.data.jpa.repository.JpaRepository

interface CompanyRepository : JpaRepository<Company, Long> {
}