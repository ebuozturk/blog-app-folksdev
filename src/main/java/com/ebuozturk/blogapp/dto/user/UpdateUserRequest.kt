package com.ebuozturk.blogapp.dto.user

import java.time.LocalDate
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Past

data class UpdateUserRequest(
    @field:NotEmpty
    val username:String,
    @field:NotEmpty
    val firstName:String,
    val middleName:String,
    @field:NotEmpty
    val lastName:String,
    @field:NotEmpty
    @field:Email
    val email:String,
    @field:Past
    @field:NotNull
    val birthDate: LocalDate
)
