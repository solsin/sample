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
@RequestMapping(value = "/sampleBoardJsRender")
@Slf4j
public class SampleBoardJsRenderController {
	@Autowired
	SampleBoardService sbService;

	@RequestMapping("/")
	public String index(Map<String, Object> model) {
		model.put(SBList.ID, sbService.list());	
		
		return "sampleBoardJsRender/index";
	}
}
