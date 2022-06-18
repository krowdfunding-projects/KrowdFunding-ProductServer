package krowdfunding.product.service.product

import krowdfunding.product.domain.category.CategoryType
import krowdfunding.product.domain.product.Product
import krowdfunding.product.dto.CreateProductDto
import krowdfunding.product.repository.product.ProductRepository
import org.apache.logging.log4j.LogManager
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger


@SpringBootTest
internal class ProductServiceTest @Autowired constructor(
    val productRepository: ProductRepository,
    val productService: ProductService,
) {


    @Test
    fun `상품 주문을 200명이 동시 주문 했을때 재고량 처리 테스트`(){
        val log =LogManager.getLogger()
        // given
        val product = productRepository.save(
            Product.createProduct(
                CreateProductDto(
                    quantity = 100,
                    content = "Mac m1 Apple",
                    type = CategoryType.TECH,
                    title = "Mac m2 investment recruit",
                    fundingEndDate = LocalDate.of(2024, 12, 31),
                    targetAmount = 24000000L,
                    price = 10000
                )
            )
        )
        val threadCnt = 100
        val latch = CountDownLatch(threadCnt)
        val service = Executors.newFixedThreadPool(10)
        val (successCnt,failCnt) = arrayOf(AtomicInteger(), AtomicInteger())

        //when
        for (i in 0 until threadCnt) {
            service.execute {
                try {
                    productService.orderProduct(product.id!!,10,"SAECHIM")
                        .apply { successCnt.getAndIncrement() }.run { log.info("비관락 로맨틱 성공적") }
                } catch (e: Exception) {
                    failCnt.getAndIncrement()
                    log.info(e.message)
                }
                latch.countDown()
            }
        }
        latch.await()

        val findByProduct = productRepository.findByIdOrNull(product.id)!!

        //then

        assertThat(successCnt).isNotNull
        assertThat(successCnt.get()).isEqualTo(10)
        assertThat(successCnt.get()).isLessThan(failCnt.get())
        assertThat(failCnt).isNotNull
        assertThat(successCnt).isNotEqualTo(failCnt)
        assertThat(findByProduct.quantity).isEqualTo(0)
        assertThat(findByProduct.collectedAmount).isEqualTo(1000000)
        assertThat(findByProduct.percentage).isEqualTo(4)
    }

}