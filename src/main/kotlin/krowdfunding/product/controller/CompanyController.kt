package krowdfunding.product.controller

import krowdfunding.product.dto.CreateCompanyDto
import krowdfunding.product.dto.ResponseDto
import krowdfunding.product.service.company.CompanyService
import org.apache.logging.log4j.Logger
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


/**
 * url example localhost:8081/merchant/company/{}
 */
@RestController
@RequestMapping("/company")
class CompanyController(
    val companyService: CompanyService,
    val logger: Logger
) {

    @PostMapping
    fun createCompany(@RequestBody createCompanyDto :CreateCompanyDto) :ResponseEntity<Any>{
        val companyId = companyService.createNewCompany(createCompanyDto)
        logger.info("생성된 회사 id {}",companyId)
        return ResponseEntity(EntityModel.of(
            ResponseDto(companyId,HttpStatus.CREATED),
            linkTo<CompanyController> {
                WebMvcLinkBuilder.methodOn(CompanyController::class.java).createCompany(createCompanyDto)
            }.withSelfRel(),
            linkTo<CompanyController> {
                WebMvcLinkBuilder.methodOn(CompanyController::class.java).getCompanyInfo(companyId)
            }.withRel("detail")),
            HttpStatus.CREATED)

    }

    @GetMapping("/{companyId}")
    fun getCompanyInfo(@PathVariable companyId : Long) : ResponseEntity<Any> {
        return ResponseEntity(
            EntityModel.of(
                ResponseDto(companyService.getCompanyInfo(companyId),HttpStatus.OK),
                linkTo<CompanyController> { WebMvcLinkBuilder.methodOn(CompanyController::class.java).
                getCompanyInfo(companyId)  }
                    .withSelfRel()
            ),
            HttpStatus.OK
        )
    }

}