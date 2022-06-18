package krowdfunding.product.config

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class GlobalConfig {

    @Bean
    fun logger() :Logger {
        return LogManager.getLogger()
    }
}