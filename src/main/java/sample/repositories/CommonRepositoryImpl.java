package sample.repositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

@Component
public class CommonRepositoryImpl implements CommonRepository {
    @PersistenceContext
    private EntityManager em;
    
	@Override
	@Transactional
	public void flush() {
		em.flush();
	}

	@Override
	@Transactional
	public void persist(Object entity) {
		em.persist(entity);
	}

}
