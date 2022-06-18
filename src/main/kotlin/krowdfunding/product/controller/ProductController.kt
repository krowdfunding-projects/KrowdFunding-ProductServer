package krowdfunding.product.controller

import krowdfunding.product.dto.CreateProductDto
import krowdfunding.product.dto.ResponseDto
import krowdfunding.product.dto.UpdateProductDto
import krowdfunding.product.service.product.ProductService
import org.apache.logging.log4j.Logger
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/product")
class ProductController(
    val productService: ProductService,
    val logger: Logger ) {


    @PostMapping("/{companyId}")
    fun createProduct(@PathVariable companyId: Long , @RequestBody createProductDto: CreateProductDto) :ResponseEntity<Any>{
        val createNewProduct = productService.createNewProduct(createProductDto, companyId)
        return ResponseEntity(
            EntityModel.of(
                ResponseDto(createNewProduct, HttpStatus.OK),
                linkTo<ProductController> { WebMvcLinkBuilder.methodOn(ProductController::class.java).
                createProduct(companyId,createProductDto) }
                    .withSelfRel(),
                linkTo<ProductController> { WebMvcLinkBuilder.methodOn(ProductController::class.java).getProductInfo(createNewProduct)
                    }.withRel("productInfo"),
                linkTo<ProductController> { WebMvcLinkBuilder.methodOn(ProductController::class.java).updateProduct(createNewProduct,UpdateProductDto(
                    title = "테스트 데이터"
                )) }.withRel("modifyProduct")
            ), HttpStatus.OK)
    }

    @GetMapping("/detail/{productId}")
    fun getProductInfo(@PathVariable productId : Long) : ResponseEntity<Any>{
        return ResponseEntity(
            EntityModel.of(
                ResponseDto(productService.getProductInfo(productId), HttpStatus.OK),
                linkTo<ProductController> { WebMvcLinkBuilder.methodOn(ProductController::class.java)
                    .getProductInfo(productId)
                }.withSelfRel()
            ),HttpStatus.OK
        )
    }

    @PatchMapping("/{productId}")
    fun updateProduct(@PathVariable productId: Long , @RequestBody updateProductDto: UpdateProductDto) : ResponseEntity<Any>{
        return ResponseEntity(
            EntityModel.of(
                ResponseDto(productService.updateProduct(productId,updateProductDto),HttpStatus.OK),
                linkTo<ProductController> {WebMvcLinkBuilder.methodOn(ProductController::class.java)
                    .updateProduct(productId,updateProductDto)  }
                    .withSelfRel(),
                linkTo<ProductController> { WebMvcLinkBuilder.methodOn(ProductController::class.java)
                    .getProductInfo(productId)
                }.withRel("productInfo")
            ),HttpStatus.OK
        )
    }




}