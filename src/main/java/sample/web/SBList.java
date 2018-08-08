package sample.web;

import java.util.Collection;

import lombok.Data;
import sample.entities.SampleBoard;

/*
 * SampleBoard ¸ñ·Ï¿ë entity
 * 
 * @author Chulhui Park <chulhui72@gmail.com>
 */
@Data
public class SBList {
	public static final String ID = SBList.class.getName();
	
	int totalCount;
	Collection<SampleBoard> items;
}
