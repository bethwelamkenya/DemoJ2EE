package com.example.demoj2ee.database

import com.example.demoj2ee.models.Admin
import com.example.demoj2ee.models.Member
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.sql.*
import java.util.*
import kotlin.collections.ArrayList

class DatabaseAdapter {
    private val databaseConnector: DatabaseConnector = DatabaseConnector()
    private var connection: Connection = databaseConnector.getConnection()
    private lateinit var preparedStatement: PreparedStatement
    private lateinit var resultSet: ResultSet

    private val member = "member"
    private val admin = "admin"
    private val attendance = "attendance"
    private val id = "id"
    private val name = "name"
    private val email = "email"
    private val regNo = "reg_no"
    private val number = "number"
    private val school = "school"
    private val year = "year"
    private val department = "department"
    private val residence = "residence"
    private val date = "date"
    private val status = "status"
    private val attendanceId = "attendance_id"
    private val userName = "username"
    private val password = "password"
    private val security = "security"
    private val answer = "answer"
    fun isDatabaseConnected(): Boolean {
        return connection.isValid(2)
    }

    private fun createTables(query: String) {
        try {
            val statement: Statement = connection.createStatement()
            statement.executeUpdate(query)
        } catch (e: SQLException) {
            println(e)
//            throw RuntimeException(e)
        }
    }

    fun memberTable() {
        val memberTable = ("CREATE TABLE $member ($id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$name TEXT UNIQUE, "
                + "$email TEXT, "
                + "$number INTEGER, "
                + "$regNo TEXT, "
                + "$school TEXT, "
                + "$year INTEGER, "
                + "$department TEXT, "
                + "$residence TEXT)")
        createTables(memberTable)
    }

    fun adminTable() {
        val adminTable = ("CREATE TABLE $admin ($id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$name TEXT, "
                + "$email TEXT, "
                + "$number INTEGER, "
                + "$userName VARCHAR (200) UNIQUE, "
                + "$password TEXT, "
                + "$security TEXT, "
                + "$answer TEXT)")
        createTables(adminTable)
    }

    fun attendaceTable() {
        val attendancesTable = ("CREATE TABLE $attendance ($attendanceId INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$id INTEGER, "
                + "$name TEXT, " /* REFERENCES members (name), "*/
                + "$date TEXT, "
                + "$status INTEGER)")
        createTables(attendancesTable)
    }

    fun dateTable() {
        val dateTable = ("CREATE TABLE $date ($id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$date TEXT)")
        createTables(dateTable)
    }

    fun logIn(theuserName: String, thepassword: String): Boolean {
        return try {
            val select = "SELECT * FROM $admin WHERE $userName = ? AND $password = ?"
            preparedStatement = connection.prepareStatement(select)
            preparedStatement.setString(1, theuserName)
            preparedStatement.setString(2, sha256(thepassword))
            resultSet = preparedStatement.executeQuery()
            return resultSet.next()
        } catch (e: SQLException) {
            println(e)
            false
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close()
            }
            if (resultSet != null) {
                resultSet.close()
            }
//            connection.close()
//            connection.endRequest()
        }
    }

    fun insertMember(
        thename: String,
        theemail: String?,
        theregNo: String?,
        thenumber: Long,
        theschool: String?,
        theyear: Int,
        thedepartment: String?,
        theresidence: String?
    ): Boolean {
        return try {
            val membersInsert =
                "INSERT INTO $member ( $name , $email, $regNo, $number, $school, $year, $department, $residence) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)"
            preparedStatement = connection.prepareStatement(membersInsert)
            preparedStatement.setString(1, thename)
            preparedStatement.setString(2, theemail)
            preparedStatement.setString(3, theregNo)
            preparedStatement.setLong(4, thenumber)
            preparedStatement.setString(5, theschool)
            preparedStatement.setInt(6, theyear)
            preparedStatement.setString(7, thedepartment)
            preparedStatement.setString(8, theresidence)
            preparedStatement.execute()
        } catch (e: SQLException) {
            println(e)
            false
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close()
            }
//            if (resultSet !=null){
//                resultSet.close()
//            }
//            connection.close()
//            connection.endRequest()
        }
    }

    fun insertAdmin(
        thename: String,
        theemail: String?,
        thenumber: Long,
        theuserName: String,
        thepassword: String,
        thesecurity: String,
        theanswer: String
    ): Boolean {
        return try {
            val adminInsert =
                "INSERT INTO $admin ( $name, $email, $number, $userName, $password, $security, $answer) VALUES ( ?, ?, ?, ?, ?, ?, ?)"
            preparedStatement = connection.prepareStatement(adminInsert)
            preparedStatement.setString(1, thename)
            preparedStatement.setString(2, theemail)
            preparedStatement.setLong(3, thenumber)
            preparedStatement.setString(4, theuserName)
            preparedStatement.setString(5, sha256(thepassword))
            preparedStatement.setString(6, thesecurity)
            preparedStatement.setString(7, sha256(theanswer))
            preparedStatement.execute()
        } catch (e: SQLException) {
            println(e)
            false
        } finally {
//            connection.close()
            connection.endRequest()
        }
    }


    fun insertAttendance(
        memberID: Long,
        memberName: String,
        theDate: String,
        theStatus: Int
    ): Boolean {
        return try {
            val attendanceInsert = "INSERT INTO $attendance ( $id, $name, $date, $status) VALUES ( ?, ?, ?, ?)"
            preparedStatement = connection.prepareStatement(attendanceInsert)
            preparedStatement.setLong(1, memberID)
            preparedStatement.setString(2, memberName)
            preparedStatement.setString(3, theDate)
            preparedStatement.setInt(4, theStatus)
            preparedStatement.execute()
        } catch (e: SQLException) {
            println(e)
            false
        } finally {
//            connection.close()
            connection.endRequest()
        }
    }

    fun insertDate(thedate: String?): Boolean {
        return try {
            val dateInsert = "INSERT INTO $date ( $date) VALUES ( ?)"
            preparedStatement = connection.prepareStatement(dateInsert)
            preparedStatement.setString(1, thedate)
            preparedStatement.execute()
        } catch (e: SQLException) {
            println(e)
            false
        } finally {
//            connection.close()
            connection.endRequest()
        }
    }

    fun updateMember(
        theid: Long,
        thename: String,
        theemail: String?,
        theregNo: String?,
        thenumber: Long,
        theschool: String?,
        theyear: Int,
        thedepartment: String?,
        theresidence: String?
    ): Boolean {
        return try {
            val update =
                "UPDATE $member SET $name=?, $email=?, $regNo=?, $number=?, $school=?, $year=?, $department=?, $residence=? WHERE $id=? "
            preparedStatement = connection.prepareStatement(update)
            preparedStatement.setString(1, thename)
            preparedStatement.setString(2, theemail)
            preparedStatement.setString(3, theregNo)
            preparedStatement.setLong(4, thenumber)
            preparedStatement.setString(5, theschool)
            preparedStatement.setInt(6, theyear)
            preparedStatement.setString(7, thedepartment)
            preparedStatement.setString(8, theresidence)
            preparedStatement.setLong(9, theid)
            preparedStatement.execute()
        } catch (e: SQLException) {
            println(e)
            false
        } finally {
//            connection.close()
            connection.endRequest()
        }
    }

    fun updateAdmin(
        theid: Long,
        thename: String,
        theemail: String?,
        thenumber: Long,
        theuserName: String,
        thepassword: String,
        thesecurity: String,
        theanswer: String
    ): Boolean {
        return try {
            val adminUpdate =
                "UPDATE $admin SET $name=?, $email=?, $number=?, $userName=?, $password=?, $security=?, $answer=? WHERE $id=? "
            preparedStatement = connection.prepareStatement(adminUpdate)
            preparedStatement.setString(1, thename)
            preparedStatement.setString(2, theemail)
            preparedStatement.setLong(3, thenumber)
            preparedStatement.setString(4, theuserName)
            preparedStatement.setString(5, sha256(thepassword))
            preparedStatement.setString(6, thesecurity)
            preparedStatement.setString(7, sha256(theanswer))
            preparedStatement.setLong(8, theid)
            preparedStatement.execute()
        } catch (e: SQLException) {
            println(e)
            false
        } finally {
//            connection.close()
            connection.endRequest()
        }
    }

    fun updateAttendance(
        theid: Long,
        memberID: Long,
        memberName: String?,
        thedate: String?,
        thestatus: Int
    ): Boolean {
        return try {
            val attendanceUpdate =
                "UPDATE attendance SET $id = ?, $name = ?, $date = ?, $status = ? WHERE $attendanceId = ?"
            preparedStatement = connection.prepareStatement(attendanceUpdate)
            preparedStatement.setLong(1, memberID)
            preparedStatement.setString(2, memberName)
            preparedStatement.setString(3, thedate)
            preparedStatement.setInt(4, thestatus)
            preparedStatement.setLong(5, theid)
            preparedStatement.execute()
        } catch (e: SQLException) {
            println(e)
            false
        } finally {
//            connection.close()
            connection.endRequest()
        }
    }

    fun updateDate(theid: Long, thedate: String?): Boolean {
        return try {
            val dateUpdate = "UPDATE $date SET $date = ? WHERE $id = ?"
            preparedStatement = connection.prepareStatement(dateUpdate)
            preparedStatement.setString(1, thedate)
            preparedStatement.setLong(2, theid)
            preparedStatement.execute()
        } catch (e: SQLException) {
            println(e)
            false
        } finally {
//            connection.close()
            connection.endRequest()
        }
    }

    fun deleteMember(theid: Long): Boolean {
        return try {
            val delete = "DELETE FROM $member WHERE $id = ?"
            preparedStatement = connection.prepareStatement(delete)
            preparedStatement.setLong(1, theid)
            preparedStatement.execute()
        } catch (e: SQLException) {
            println(e)
            false
        } finally {
//            connection.close()
            connection.endRequest()
        }
    }

    fun deleteAdmin(theid: Long): Boolean {
        return try {
            val adminDelete = "DELETE FROM $admin WHERE $id = ?"
            preparedStatement = connection.prepareStatement(adminDelete)
            preparedStatement.setLong(1, theid)
            preparedStatement.execute()
        } catch (e: SQLException) {
            println(e)
            false
        } finally {
//            connection.close()
            connection.endRequest()
        }
    }

    fun deleteAttendance(theid: Long): Boolean {
        return try {
            val attendanceDelete = "DELETE FROM $attendance WHERE $id = ?"
            preparedStatement = connection.prepareStatement(attendanceDelete)
            preparedStatement.setLong(1, theid)
            preparedStatement.execute()
        } catch (e: SQLException) {
            println(e)
            false
        } finally {
//            connection.close()
            connection.endRequest()
        }
    }

    fun deleteDate(theid: Long): Boolean {
        return try {
            val dateDelete = "DELETE FROM $date WHERE $id = ?"
            preparedStatement = connection.prepareStatement(dateDelete)
            preparedStatement.setLong(1, theid)
            preparedStatement.execute()
        } catch (e: SQLException) {
            println(e)
            false
        } finally {
//            connection.close()
            connection.endRequest()
        }
    }

    fun getMembers(): ArrayList<Member> {
        return try {
            val members = ArrayList<Member>()
            val memberQuery = "SELECT * FROM $member"
            resultSet = connection.createStatement().executeQuery(memberQuery)
            while (resultSet.next()) {
                members.add(
                    Member(
                        resultSet.getLong(id),
                        resultSet.getString(name),
                        resultSet.getString(email),
                        resultSet.getString(regNo),
                        resultSet.getLong(number),
                        resultSet.getString(school),
                        resultSet.getInt(year),
                        resultSet.getString(department),
                        resultSet.getString(residence)
                    )
                )
            }
            members
        } catch (e: SQLException) {
            println(e)
            ArrayList()
        }
    }

    fun getAdmins(): ArrayList<Admin> {
        return try {
            val admins = ArrayList<Admin>()
            val adminQuery = "SELECT * FROM $admin"
            resultSet = connection.createStatement().executeQuery(adminQuery)
            while (resultSet.next()) {
                admins.add(
                    Admin(
                        resultSet.getLong(id),
                        resultSet.getString(name),
                        resultSet.getString(email),
                        resultSet.getLong(number),
                        resultSet.getString(userName),
                        resultSet.getString(password),
                        resultSet.getString(security),
                        resultSet.getString(answer)
                    )
                )
            }
            return admins
        } catch (e: SQLException) {
            println(e)
            java.util.ArrayList()
        }
    }

//    fun getAttendances(): ArrayList<Attendance> {
//        return try {
//            val attendances = ArrayList<Attendance>()
//            val attendanceQuery = "SELECT * FROM $attendance"
//            resultSet = connection.createStatement().executeQuery(attendanceQuery)
//            while (resultSet.next()){
//                attendances.add(
//                    Attendance(
//                        resultSet.getLong(attendanceId),
//                        resultSet.getLong(id),
//                        resultSet.getString(name),
//                        resultSet.getLong(number),
//                        resultSet.getString(residence),
//                        resultSet.getString(date),
//                        resultSet.getInt(status)
//                    )
//                )
//            }
//            return attendances
//        }catch (e: SQLException){
//            println(e)
//            ArrayList()
//        }
//    }

//    fun getDates(): ArrayList<Date> {
//        return try {
//            val dates = ArrayList<Date>()
//            val dateQuery = "SELECT * FROM $date"
//            resultSet =  connection.createStatement().executeQuery(dateQuery)
//            while (resultSet.next()){
//                dates.add(
//                    Date(
//                        resultSet.getLong(id),
//                        resultSet.getString(date)
//                    )
//                )
//            }
//            dates
//        } catch (e: SQLException){
//            println(e)
//            ArrayList()
//        }
//    }

    fun getMember(theId: Long): Member? {
        return try {
            val selectMember = "SELECT * FROM $member WHERE $id = ?"
            preparedStatement = connection.prepareStatement(selectMember)
            preparedStatement.setLong(1, theId)
            resultSet = preparedStatement.executeQuery()
            val member = Member(
                resultSet.getLong(id),
                resultSet.getString(name),
                resultSet.getString(email),
                resultSet.getString(regNo),
                resultSet.getLong(number),
                resultSet.getString(school),
                resultSet.getInt(year),
                resultSet.getString(department),
                resultSet.getString(residence)
            )
            member
        } catch (e: SQLException) {
            println(e)
            null
        } finally {
//            connection.close()
            connection.endRequest()
        }
    }

    fun getMember(theName: String): Member? {
        return try {
            val selectMember = "SELECT * FROM $member WHERE $name LIKE ?"
            preparedStatement = connection.prepareStatement(selectMember)
            preparedStatement.setString(1, theName.lowercase(Locale.getDefault()))
            resultSet = preparedStatement.executeQuery()
            val member = Member(
                resultSet.getLong(id),
                resultSet.getString(name),
                resultSet.getString(email),
                resultSet.getString(regNo),
                resultSet.getLong(number),
                resultSet.getString(school),
                resultSet.getInt(year),
                resultSet.getString(department),
                resultSet.getString(residence)
            )
            member
        } catch (e: SQLException) {
            println(e)
            null
        } finally {
            connection.endRequest()
//            connection.close()
        }
    }

    fun getAdmin(theid: Long): Admin? {
        return try {
            val selectAdmin = "SELECT * FROM $admin WHERE $id = ?"
            preparedStatement = connection.prepareStatement(selectAdmin)
            preparedStatement.setLong(1, theid)
            resultSet = preparedStatement.executeQuery()
            val admin = Admin(
                resultSet.getLong(id),
                resultSet.getString(name),
                resultSet.getString(email),
                resultSet.getLong(number),
                resultSet.getString(userName),
                resultSet.getString(password),
                resultSet.getString(security),
                resultSet.getString(answer)
            )
            admin
        } catch (e: SQLException) {
            println(e)
            null
        } finally {
//            connection.close()
            connection.endRequest()
        }
    }

    fun getAdmin(theuserName: String): Admin? {
        return try {
            val selectAdmin = "SELECT * FROM $admin WHERE $userName = ?"
            preparedStatement = connection.prepareStatement(selectAdmin)
            preparedStatement.setString(1, theuserName)
            resultSet = preparedStatement.executeQuery()
            val admin = Admin(
                resultSet.getLong(id),
                resultSet.getString(name),
                resultSet.getString(email),
                resultSet.getLong(number),
                resultSet.getString(userName),
                resultSet.getString(password),
                resultSet.getString(security),
                resultSet.getString(answer)
            )
            admin
        } catch (e: SQLException) {
            println(e)
            null
        } finally {
            connection.endRequest()
//            connection.close()
        }
    }

    fun getMembersByYear(theyear: Int): ArrayList<Member> {
        return try {
            val members = ArrayList<Member>()
            val selectMember = "SELECT * FROM $member WHERE $year = ?"
            preparedStatement = connection.prepareStatement(selectMember)
            preparedStatement.setInt(1, theyear)
            resultSet = preparedStatement.executeQuery()
            while (resultSet.next()) {
                members.add(
                    Member(
                        resultSet.getLong(id),
                        resultSet.getString(name),
                        resultSet.getString(email),
                        resultSet.getString(regNo),
                        resultSet.getLong(number),
                        resultSet.getString(school),
                        resultSet.getInt(year),
                        resultSet.getString(department),
                        resultSet.getString(residence)
                    )
                )
            }
            return members
        } catch (e: SQLException) {
            println(e)
            ArrayList()
        } finally {
//            connection.close()
            connection.endRequest()
        }
    }

//    fun getAttendancesByDate(thedate: String): ArrayList<Attendance> {
//        return try {
//            val attendances = ArrayList<Attendance>()
//            val selectAdmin = "SELECT * FROM $attendance WHERE $date = ?"
//            preparedStatement = connection.prepareStatement(selectAdmin)
//            preparedStatement.setString(1, thedate)
//            resultSet = preparedStatement.executeQuery()
//            while (resultSet.next()){
//                attendances.add(
//                    Attendance(
//                        resultSet.getLong(attendanceId),
//                        resultSet.getLong(id),
//                        resultSet.getString(name),
//                        resultSet.getLong(number),
//                        resultSet.getString(residence),
//                        resultSet.getString(date),
//                        resultSet.getInt(status)
//                    )
//                )
//            }
//            attendances
//        } catch (e: SQLException) {
//            println(e)
//            ArrayList()
//        } finally {
////            connection.close()
//            connection.endRequest()
//        }
//    }

//    fun getAttendancesByName(theName: String): ArrayList<MemberAttendance> {
//        return try {
//            val attendances = ArrayList<MemberAttendance>()
//            val selectAdmin = "SELECT * FROM $attendance WHERE $name LIKE ?"
//            preparedStatement = connection.prepareStatement(selectAdmin)
//            preparedStatement.setString(1, theName)
//            resultSet = preparedStatement.executeQuery()
//            while (resultSet.next()){
//                attendances.add(
//                    MemberAttendance(
//                        resultSet.getLong(attendanceId),
//                        resultSet.getString(date),
//                        resultSet.getInt(status)
//                    )
//                )
//            }
//            attendances
//        } catch (e: SQLException) {
//            println(e)
//            ArrayList()
//        } finally {
////            connection.close()
//            connection.endRequest()
//        }
//    }

    fun memberExists(theName: String): Boolean {
        return try {
            val select = "SELECT * FROM $member WHERE $name LIKE ?"
            preparedStatement = connection.prepareStatement(select)
            preparedStatement.setString(1, theName.lowercase(Locale.getDefault()))
            resultSet = preparedStatement.executeQuery()
            resultSet.next()
        } catch (e: SQLException) {
            println(e)
            false
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close()
            }
            if (resultSet != null) {
                resultSet.close()
            }
//            connection.close()
//            connection.endRequest()
        }
    }

    fun adminExists(theUserName: String): Boolean {
        return try {
            val select = "SELECT * FROM $admin WHERE $userName = ?"
            preparedStatement = connection.prepareStatement(select)
            preparedStatement.setString(1, theUserName)
            resultSet = preparedStatement.executeQuery()
            resultSet.next()
        } catch (e: SQLException) {
            println(e)
            false
        } finally {
//            connection.close()
            connection.endRequest()
        }
    }

    fun dateExists(theDate: String): Boolean {
        val select = "SELECT * FROM $date WHERE $date = ?"
        return try {
            preparedStatement = connection.prepareStatement(select)
            preparedStatement.setString(1, theDate)
            resultSet = preparedStatement.executeQuery()
            resultSet.next()
        } catch (e: SQLException) {
            println(e)
            false
        } finally {
//            connection.close()
            connection.endRequest()
        }
    }

    fun resetAdminPassword(theUserName: String, thePassword: String): Boolean {
        return try {
            val resetPassword = "UPDATE $admin SET $password = ? WHERE $userName = ?"
            preparedStatement = connection.prepareStatement(resetPassword)
            preparedStatement.setString(1, sha256(thePassword))
            preparedStatement.setString(2, theUserName)
            preparedStatement.execute()
        } catch (e: SQLException) {
            println(e)
            false
        } finally {
//            connection.close()
            connection.endRequest()
        }
    }

    private fun sha256(input: String): String? {
        return try {
            val bytes = input.toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            digest.joinToString("") { String.format("%02x", it) }
        } catch (e: NoSuchAlgorithmException) {
            println(e)
            null
        }
    }
}