package sample.service;

import java.util.Collection;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import sample.entities.SampleBoard;
import sample.exception.NoDataFoundException;
import sample.repositories.SampleBoardMapper;
import sample.repositories.SampleBoardRepository;
import sample.web.SBList;
import sample.web.SBRequest;

@Service
public class SampleBoardServiceImpl implements SampleBoardService {
	@Autowired
	SampleBoardRepository sbRepository;
	
	@Autowired
	SampleBoardMapper sbMapper;
	
	@Autowired
    private MessageSource message;
	
	@Autowired
	private LocaleResolver locale;
	
	@Override
	public SBList list() {
		int count = sbMapper.countOfList();
		Collection<SampleBoard> items = sbMapper.list();
		SBList sbList = new SBList();
		sbList.setTotalCount(count);
		sbList.setItems(items);
		
		return sbList;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class, readOnly=false)
	public SampleBoard save(SBRequest sbRequest) {
		SampleBoard sb = sbRequest.copyTo(SampleBoard.class);
		return sbRepository.save(sb);
	}
	
	@Override
	public SampleBoard view(Long id) throws NoDataFoundException {
		Optional<SampleBoard> sb = sbRepository.findById(id);
		if (!sb.isPresent()) {
			String msg = getMessage("error.no_data_exception", new Object[]{id});
			throw new NoDataFoundException(msg);
		}
		return sb.get();
	}
	
	private String getMessage(String code, Object[] args) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return message.getMessage(code, args, locale.resolveLocale(request));
	}

	@Override
	public void remove(long id) {
		sbRepository.deleteById(id);
	}
	
}
