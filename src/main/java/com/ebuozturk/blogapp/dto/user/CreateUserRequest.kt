package com.ebuozturk.blogapp.dto.user

import java.time.LocalDate
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Past

data class CreateUserRequest @JvmOverloads constructor(
    @field:NotEmpty
    val username:String,
    @field:NotEmpty
    val firstName:String,
    val middleName:String?="",
    @field:NotEmpty
    val lastName:String,
    @field:Email
    @field:NotEmpty
    val email:String,
    @field:Past
    @field:NotNull(message="can not be null")
    val birthDate:LocalDate
){}
