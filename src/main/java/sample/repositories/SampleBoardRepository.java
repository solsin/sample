package sample.repositories;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import sample.entities.SampleBoard;

@Mapper
public interface SampleBoardRepository extends PagingAndSortingRepository<SampleBoard, Long> {

    @Query("select sb from SampleBoard sb where sb.subject like ?1 order by id desc")
    List<SampleBoard> findBySubject(String subject);
    
    @Query(value="	SELECT id, subject, content, userName, useYn " + 
    		"	  FROM SAMPLE_BOARD " + 
    		"	 WHERE subject like :subject " + 
    		"	   AND content like :content " + 
    		"     ORDER BY id desc", nativeQuery=true)
    Collection<SampleBoard> findWithNativeQeury(@Param("subject") String subject, @Param("content") String content);
    
    @Query(value="	SELECT id, subject, content, userName " + 
    		"	  FROM SAMPLE_BOARD " + 
    		"	 WHERE subject like :subject " + 
    		"	   AND content like :content " + 
    		"     ORDER BY id desc", nativeQuery=true)
    Collection<Object[]> findWithNativeQeury2(@Param("subject") String subject, @Param("content") String content);
    
    @Query(value="	SELECT id, subject, content, userName " + 
    		"	  FROM SAMPLE_BOARD " + 
    		"	 WHERE subject like :subject " + 
    		"	   AND content like :content " + 
    		"     ORDER BY id desc", nativeQuery=true)
    Collection<SampleBoard.SB> findWithNativeQeury3(@Param("subject") String subject, @Param("content") String content);
}
