package com.ebuozturk.blogapp.dto.user

import org.springframework.hateoas.RepresentationModel
import java.time.LocalDate

data class UserDto @JvmOverloads constructor(
    val id:String,
    val username:String,
    val firstName:String,
    val middleName:String?="",
    val lastName:String,
    val email:String,
    val birthDate: LocalDate
):RepresentationModel<UserDto>()
