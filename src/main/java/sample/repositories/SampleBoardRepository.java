package sample.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import sample.entities.SampleBoard;

public interface SampleBoardRepository extends PagingAndSortingRepository<SampleBoard, Long> {

    @Query("select sb from SampleBoard sb where sb.subject like ?1 order by id desc")
    List<SampleBoard> findBySubject(String country);
}
