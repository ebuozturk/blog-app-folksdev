package com.ebuozturk.blogapp.entity

import com.fasterxml.jackson.annotation.JsonFormat
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Entry @JvmOverloads constructor(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
    val id:String? ="",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",referencedColumnName = "id")
    val user:User,
    val title:String,
    val content:String,

    @OneToMany(mappedBy="entry",fetch = FetchType.LAZY,cascade = [CascadeType.ALL])
    val comments: Set<Comment>,

    @CreatedDate
    val createdDate: LocalDateTime? = LocalDateTime.now(),

    val updatedDate: LocalDateTime? = LocalDateTime.now()
) {
    constructor(user: User,content: String,title:String,createdDate: LocalDateTime?,updatedDate: LocalDateTime?):this("",user,content,title,HashSet())
    constructor(id:String,user: User,content: String,title:String):this(id,user,content,title,HashSet())
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Entry

        if (id !=null && id != other.id) return false
        if (user != other.user) return false
        if (comments != other.comments) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + user.id.hashCode()
        result = 31 * result + comments.hashCode()
        return result
    }
}
