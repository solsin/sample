package sample.service;

import sample.entities.SampleBoard;
import sample.exception.NoDataFoundException;
import sample.web.SBList;
import sample.web.SBRequest;

public interface SampleBoardService {

	SBList list();

	SampleBoard save(SBRequest sbRequest);

	SampleBoard view(Long id) throws NoDataFoundException;

	void remove(long id);

}
