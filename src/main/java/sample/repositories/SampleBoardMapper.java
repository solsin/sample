package sample.repositories;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import sample.entities.SampleBoard;

@Mapper
public interface SampleBoardMapper {
    @Select(value="	SELECT id, subject, content, userName " + 
    		"	  FROM SAMPLE_BOARD " + 
    		"	 WHERE subject like #{subject} " + 
    		"	   AND content like #{content} " + 
    		"     ORDER BY id desc")
    List<SampleBoard> findWithMybatis(@Param("subject") String subject, @Param("content") String content);
}
