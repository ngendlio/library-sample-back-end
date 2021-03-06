package com.example.library.models

import java.util.*
import javax.persistence.Entity
import javax.validation.constraints.NotBlank

@Entity
data class User(
        @NotBlank val firstName: String,
        @NotBlank val lastName: String,
        @NotBlank val dateOfBirth: Date,
        @NotBlank val nationalId: String
) : BaseModel()

@Entity
class Book(
        @NotBlank val title: String,
        @NotBlank val isbn: String,
        @NotBlank val price: String,
        @NotBlank val author: String
) : BaseModel()


@Entity
class RegistreBook(
        @NotBlank val user: User,
        @NotBlank val book: Book,
        @NotBlank val dueDate: Date,
        @NotBlank val price: Double
) : BaseModel()

