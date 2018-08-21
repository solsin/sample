package sample.api;

import java.util.Collection;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sample.entities.SampleBoard;
import sample.exception.NoDataFoundException;
import sample.repositories.SampleBoardMapper;
import sample.repositories.SampleBoardRepository;
import sample.web.SBList;
import sample.web.SBRequest;

/**
 * cxf ±â¹Ý api implementation for SampleBoard
 *  
 * @author Chulhui Park <chulhui72@gmail.com>
 */
@Path("/sampleBoard")
@Produces({ "application/json" })
@Consumes({ "application/json" })
@Service
public class SampleBoardApiImpl implements SampleBoardApi {
	@Autowired
	SampleBoardRepository sbRepository;
	
	@Autowired
	SampleBoardMapper sbMapper;
	
	@Override
	public Response list() throws Exception {
		int count = sbMapper.countOfList();
		Collection<SampleBoard> items = sbMapper.list();
		SBList sbList = new SBList();
		sbList.setTotalCount(count);
		sbList.setItems(items);
		
		return Response.ok().entity(sbList).build();
	}
	
	@Override
    @GET
    @Path("/view/{id}")
	public Response view(@PathParam("id") Long id) throws Exception {
		Optional<SampleBoard> sb = sbRepository.findById(id);
		
		if (sb.isPresent()) {
			return Response.ok().entity(sb.get()).build();
		} else {
			throw new NoDataFoundException("no data");
		}
	}

	@Override
    @POST
    @Path("/save")
	public Response save(SBRequest sbRequest) throws Exception {
		SampleBoard sb = sbRequest.copyTo(SampleBoard.class);
		SampleBoard saved = sbRepository.save(sb);
		
		return Response.ok().entity(saved).build();
	}

	@Override
    @POST
    @Path("/remove")
	public Response remove(SBRequest sbRequest) throws Exception {
		sbRepository.deleteById(sbRequest.getId());
		
		return Response.ok().entity(1).build();
	}

}
