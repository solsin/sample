package sample.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;

import lombok.extern.slf4j.Slf4j;
import sample.config.TestJPAConfig;
import sample.entities.Sample;

/**
 * @DataJpaTest annotation에 의해 각 test method 실행 후 rollback 된다.
 * 하지만, memory 상에 존재하는 h2 db까지 초기화가 되는 것은 아니여서 id는 초기화되지 않고 증가한다.
 * 
 * @author Chulhui Park <chulhui72@gmail.com>
 *
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestJPAConfig.class)
@DataJpaTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class SampleRepositoriesTest {
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    SampleRepository sampleRepository;
    
    @Autowired
    DataSource ds;
    
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	private void insertTestData() {
		Sample sample = new Sample();
		sample.setName("sample-1");
		sampleRepository.save(sample);
		log.info("[{}] sample inserted", sample);
		
		
		sample = new Sample();
		sample.setName("sample-2");
		sampleRepository.save(sample);
		log.info("[{}] sample inserted", sample);		
		
		entityManager.flush();
	}

	@Test
	public void _00_testSave() {
		Sample sample = new Sample();
		sample.setName("sample-1");
		entityManager.persistAndGetId(sample);
		entityManager.flush();
		log.info("[{}] sample inserted", sample);
		
		sample = new Sample();
		sample.setName("sample-2");
		sampleRepository.save(sample);
		entityManager.flush();
		log.info("[{}] sample inserted", sample);
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
		int res = JdbcTestUtils.countRowsInTable(jdbcTemplate, "SAMPLE");
		assertEquals(2, res);
	}

	@Test
	public void _01_testFindAllById() {
		insertTestData();
		
		Optional<Sample> data = sampleRepository.findById(3L);
		assertTrue(data.isPresent());
		assertEquals("sample-1", data.get().getName());
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
		int res = JdbcTestUtils.countRowsInTable(jdbcTemplate, "SAMPLE");
		assertEquals(2, res);
	}
	
	@Test
	public void _02_testFindByName() {
		insertTestData();
		
		List<Sample> data = sampleRepository.findByName("sample-1");
		assertEquals(1, data.size());
		
		data = sampleRepository.findAllByName("sample%");
		assertEquals(2, data.size());
	}

	@Test
	public void _03_testDelete() {
		insertTestData();
		
		sampleRepository.deleteById(sampleRepository.findByName("sample-1").get(0).getId());
		entityManager.flush();
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
		int res = JdbcTestUtils.countRowsInTable(jdbcTemplate, "SAMPLE");
		assertEquals(1, res);
		
		sampleRepository.delete(sampleRepository.findByName("sample-2").get(0));
		entityManager.flush();
		
		res = JdbcTestUtils.countRowsInTable(jdbcTemplate, "SAMPLE");
		assertEquals(0, res);
	}
}
