package com.example.demoj2ee.database

import com.example.demoj2ee.models.Admin
import com.example.demoj2ee.models.Member
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ejb.Stateless

@Stateless
class DatabaseConnector1 {
    @PersistenceContext
    private lateinit var entityManager: EntityManager;

    fun insertMember(member: Member) {
        entityManager.persist(member)
    }

    fun updateMember(member: Member){
        entityManager.merge(member)
    }

    fun deleteMember(member: Member){
        entityManager.remove(entityManager.merge(member))
    }

    fun findMember(id: Long) : Member{
        return entityManager.find(Member::class.java, id)
    }

    fun findMember(name: String) : Member{
        return entityManager.find(Member::class.java, name)
    }

    fun findAllMembers() : ArrayList<Member> {
        return ArrayList(entityManager.createQuery("SELECT e from Member e", Member::class.java).resultList)
    }

    fun insertAdmin(admin: Admin) {
        entityManager.persist(admin)
    }

    fun updateAdmin(admin: Admin){
        entityManager.merge(admin)
    }

    fun deleteAdmin(admin: Admin){
        entityManager.remove(entityManager.merge(admin))
    }

    fun findAdmin(id: Long) : Admin{
        return entityManager.find(Admin::class.java, id)
    }

    fun findAdmin(name: String) : Admin?{
        return entityManager.find(Admin::class.java, name)
    }

    fun findAdmin(name: String, password: String) : Admin {
        return entityManager.find(Admin::class.java, name)
    }

    fun findAllAdmins() : ArrayList<Admin> {
        return ArrayList(entityManager.createQuery("SELECT e from Admin e", Admin::class.java).resultList)
    }
}