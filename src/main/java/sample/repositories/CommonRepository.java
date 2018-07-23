package sample.repositories;

public interface CommonRepository {
	
	void flush();

	void persist(Object entity);

}
