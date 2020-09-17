package com.example.library.repositories

import com.example.library.models.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository: JpaRepository<User,UUID>{
    fun findByNationalId(national:String): User?


}