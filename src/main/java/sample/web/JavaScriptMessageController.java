package sample.web;

import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import config.CustomReloadableResourceBundleMessageSource;
import lombok.extern.slf4j.Slf4j;

/**
 * locale message를 javascript에서 사용하게 하기 위한 *.js 제공 controller sample application
 * 에서는 꼭 필요한 기능이 아니므로, 정의만 해둔다.
 * 
 * @author Chulhui Park <charles@plgrim.com>
 *
 */
@Controller
@RequestMapping(value = "/message")
@Slf4j
public class JavaScriptMessageController {
	private static final String SUFFIX_JS = ".js";
	
	@Autowired
	CustomReloadableResourceBundleMessageSource messageSource;

	@RequestMapping(method = RequestMethod.GET, value = { "/{messageFile:.+}" })
	public void messageCommon(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("messageFile") String messageFile) throws Exception {
    	if (!messageFile.endsWith(SUFFIX_JS)) {
    		log.warn("invalid messageFile suffix[{}]: only support .js", messageFile);
    		response.setStatus(HttpStatus.SC_NOT_FOUND);
    		return;
    	}
    	
    	response.setStatus(HttpStatus.SC_OK);
    	response.setContentType("text/javascript;charset=UTF-8");
    	
    	// sample 이므로 캐시 설정은 하지 않는다.
		
    	String locale = messageFile.substring(0, messageFile.length()-SUFFIX_JS.length());
    	Map<String, String> messages = messageSource.getAllMessages("", new Locale(locale));
    	
    	try {
    		PrintWriter out = response.getWriter();
    		
    		out.println("var MESSAGES={};");
    		
    		for(Map.Entry<String, String> entry : messages.entrySet()) {
        		String key = entry.getKey();
        		String value = entry.getValue();
        		
        		out.println("MESSAGES['"+key+"'] = \""+value+"\"");
        	}
    	} finally {
    		response.flushBuffer();
    	}
	}
}
