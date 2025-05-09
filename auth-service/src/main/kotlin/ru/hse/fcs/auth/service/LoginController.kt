package ru.hse.fcs.auth.service

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("login")
class LoginController {
    @GetMapping
    fun login(): String {
        return "login"
    }
}
