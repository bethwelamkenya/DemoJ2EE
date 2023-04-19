package com.example.demoj2ee.models

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
open class Admin(
    @Id
    val id: Long = 0,

    @Column
    val name: String = "",

    @Column
    val email: String = "",

    @Column
    val number: Long = 0,

    @Column
    val user_name: String = "",

    @Column
    val password: String = "",

    @Column
    val security: String = "",

    @Column
    val answer: String = ""
)