package sample.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import sample.entities.Sample;

public interface SampleRepository extends CrudRepository<Sample, Long>, CommonRepository {
	
	List<Sample> findByName(String name);
	List<Sample> findAllByName(String name);
}
