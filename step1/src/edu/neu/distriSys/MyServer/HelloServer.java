package edu.neu.distriSys.MyServer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by wangy on 9/26/2017.
 */
@Path("/hello")
public class HelloServer {

  //This method is called if TEXT_PLAIN is requested.
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String sayTextHello() {
    return "Hello";
  }

//  //This method is called if XML is requested
//  @GET
//  @Produces(MediaType.TEXT_XML)
//  public String sayXMLHello() {
//    return "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>";
//  }
//  // This method is called if HTML is request
//  @GET
//  @Produces(MediaType.TEXT_HTML)
//  public String sayHtmlHello() {
//    return "<html> " + "<title>" + "Hello Jersey" + "</title>"
//        + "<body><h1>" + "Hello Jersey" + "</body></h1>" + "</html> ";
//  }

  @POST
  @Consumes(MediaType.TEXT_PLAIN)
  public Response postText(String content) {
    return Response
        .status(200)
        .entity(content)
        .build();
  }

}
