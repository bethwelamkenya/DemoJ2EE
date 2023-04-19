package com.example.demoj2ee.models

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
open class Member(
    @Id
    val id: Long = 0,

    @Column
    val name: String = "",

    @Column
    val email: String = "",

    @Column
    val reg_no: String = "",

    @Column
    val number: Long = 0,

    @Column
    val school: String = "",

    @Column
    val year: Int = 0,

    @Column
    val department: String = "",

    @Column
    val residence: String = ""

)