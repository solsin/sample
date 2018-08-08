package sample.web;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;
import sample.entities.SampleBoard;

/**
 * �󼼺���/������ ��ȸ ��� entity
 * 
 * @author Chulhui Park <chulhui72@gmail.com>
 *
 */
@Data
public class SBView {
	public static final String ID = StringUtils.replace(SBView.class.getName(), ".", "_");
	
	SampleBoard sb;
}
