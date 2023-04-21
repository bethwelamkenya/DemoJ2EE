package com.example.demoj2ee

import com.example.demoj2ee.database.DatabaseConnector1
import com.example.demoj2ee.models.Admin
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import java.io.IOException
import javax.websocket.server.PathParam
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response


@Path("/admin/{action}")
class AdminResource {
    val databaseConnector1 = DatabaseConnector1()

    @GET
    @Produces("text/plain")
    fun hello(): String {
        return "Hello, World!"
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    fun adminActions(@PathParam("action") act: String, json: String) : Response{
        val mapper = ObjectMapper()
        println(json)
        return try {
            val gson = Gson()
            // convert the JSON string to a Person object
            // add the person to your data source
            if (act == "SignUp"){
                val admin: Admin = mapper.readValue(json, Admin::class.java)
                Response.ok().entity(adminSignUp(admin = admin)).build()
            } else if (act == "LogIn"){
                val admin: Admin = mapper.readValue(json, Admin::class.java)
                Response.ok().entity(adminLogIn(admin = admin)).build()
            }
            //            String respond = gson.toJson(people);
            val respond = "FALSE"
            println(respond)
            // return a response indicating success or failure
            Response.ok().entity(respond).build()
        } catch (e: IOException) {
            e.printStackTrace()
            Response.serverError().build()
        }
    }

    fun adminSignUp(admin: Admin) : String {
        databaseConnector1.insertAdmin(admin)
        return "OK"
    }

    fun adminLogIn(admin: Admin) : String {
        return if (databaseConnector1.findAdmin(admin.user_name) == null){
            "FALSE"
        } else {
            "OK"
        }
    }
}