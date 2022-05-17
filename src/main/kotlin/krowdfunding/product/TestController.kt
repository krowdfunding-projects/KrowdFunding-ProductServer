package krowdfunding.product

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * RewirePath Gateway Test용 controller
 */
@RestController("/product")
class TestController {

    @GetMapping("/abcd")
    fun abcd():String{
        return "abcd"
    }
}