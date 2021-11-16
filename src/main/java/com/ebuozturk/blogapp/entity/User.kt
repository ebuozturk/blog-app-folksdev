package com.ebuozturk.blogapp.entity

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name="app_user")
data class User @JvmOverloads constructor(
        @Id
        @GeneratedValue(generator = "UUID")
        @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
        val id:String?="",

        @Column(unique = true)
        val username:String,

        val firstName:String,

        val middleName:String ="",

        val lastName:String,

        @Column(unique = true)
        val email:String,

        val birthDate:LocalDate,

        @CreatedDate
        val createdDate: LocalDateTime? = LocalDateTime.now(),

        val updatedDate: LocalDateTime? = LocalDateTime.now(),

        @OneToMany(fetch = FetchType.LAZY,mappedBy = "user",cascade = [CascadeType.ALL])
        val entries:Set<Entry>? = HashSet(),

        @OneToMany(fetch = FetchType.LAZY,mappedBy = "user",cascade = [CascadeType.ALL])
        val comments:Set<Comment>?= HashSet()
){
        constructor(
                    username: String,
                    firstName: String,
                    middleName: String,
                    lastName: String,
                    email: String,
                    birthDate: LocalDate,
                    updatedDate: LocalDateTime
                    )
                :this("",
                username,
                firstName,
                middleName,
                lastName,
                email,
                birthDate,
                LocalDateTime.now(),
                updatedDate,
                HashSet(),
                HashSet())


        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as User

                if (id != null && id != other.id) return false
                if (username != other.username) return false
                if (entries != other.entries) return false
                if (comments != other.comments) return false

                return true
        }

        override fun hashCode(): Int {
                var result = id?.hashCode() ?: 0
                result = 31 * result + username.hashCode()
                result = 31 * result + entries.hashCode()
                result = 31 * result + comments.hashCode()
                return result
        }
}
