package sample.web;

import lombok.Data;
import sample.entities.AbstractEntity;

@Data
public class SBRequest extends AbstractEntity {
	long id;
	String subject;
	String userName;
	String content;
}
