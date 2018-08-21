package sample.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import sample.web.SBRequest;

/**
 * cxf ±â¹Ý api interface for SampleBoard
 *  
 * @author Chulhui Park <chulhui72@gmail.com>
 */
@Path("/sampleBoard")
@Produces({ "application/json" })
@Consumes({ "application/json" })
public interface SampleBoardApi {
    @GET
    @Path("/list")
	Response list() throws Exception;
    
    @GET
    @Path("/view/{id}")
	Response view(@PathParam("id") Long id) throws Exception;

    @POST
    @Path("/save")
	Response save(SBRequest sbRequest) throws Exception;
    
    @POST
    @Path("/remove")
	Response remove(SBRequest sbRequest) throws Exception;    
}
