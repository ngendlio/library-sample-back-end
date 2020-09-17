package com.example.library.controllers

import com.example.library.models.User
import com.example.library.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import rw.bk.loanenginecore.models.exceptions.ForbiddenException

@RestController
@RequestMapping("/v1/user")
class UserController {

    @Autowired
    lateinit var userRepository: UserRepository

    @GetMapping
    fun getListUsers(): List<User> = userRepository.findAll()

    @PostMapping
    fun createUser(@RequestBody user: User): User {

        val userExists = userRepository.findByNationalId(user.nationalId)

        if (userExists != null) throw ForbiddenException("NID_ALREADY_EXISTS")

        return userRepository.save(User(nationalId = user.nationalId, firstName = user.firstName, lastName = user.lastName, dateOfBirth = user.dateOfBirth))
    }


}