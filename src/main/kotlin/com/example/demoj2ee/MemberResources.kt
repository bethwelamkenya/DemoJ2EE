package com.example.demoj2ee

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Path("/member")
class MemberResources {
    @GET
    @Produces("text/plain")
    fun hello(): String {
        return "Hello, World!"
    }
}