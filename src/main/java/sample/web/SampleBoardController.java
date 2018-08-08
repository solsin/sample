package sample.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import sample.entities.SampleBoard;
import sample.exception.NoDataFoundException;
import sample.service.SampleBoardService;

/**
 * JPA ±â¹ÝÀÇ sample board
 * 
 * @author Chulhui Park <chulhui72@gmail.com>
 *
 */
@Controller
@RequestMapping(value = "/sampleBoard")
@Slf4j
public class SampleBoardController {
	@Autowired
	SampleBoardService sbService;

	@RequestMapping("/")
	public String index(Map<String, Object> model) {
		model.put(SBList.ID, sbService.list());	
		
		return "sampleBoard/index";
	}
	
	@RequestMapping("/insert")
	@ResponseBody
	public String insert(@RequestBody SBRequest sbRequest) {
		sbService.save(sbRequest);
		
		return "OK";
	}
	
	@RequestMapping("/insertAndRedirect")
	public String insertAndRedirect(@RequestBody SBRequest sbRequest) {
		sbService.save(sbRequest);
		
		log.info("insertAndRedirect");
		return "redirect:./";
	}
	
	@RequestMapping("/view")
	public String insertAndRedirect(@RequestParam("id") Long id, Map<String, Object> model) throws NoDataFoundException {
		SampleBoard sb = sbService.view(id);
		SBView sbView = new SBView();
		sbView.setSb(sb);
		
		model.put(SBView.ID, sbView);
		
		return "sampleBoard/view";
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public SampleBoard update(@RequestBody SBRequest sbRequest) throws NoDataFoundException {
		SampleBoard saved = sbService.save(sbRequest);
				
		return saved;
	}
	
	@RequestMapping("/remove")
	@ResponseBody
	public String remove(@RequestBody SBRequest sbRequest) throws NoDataFoundException {
		sbService.remove(sbRequest.getId());
				
		return "OK";
	}
}
