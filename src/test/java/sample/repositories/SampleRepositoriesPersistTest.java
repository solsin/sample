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
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import lombok.extern.slf4j.Slf4j;
import sample.config.TestConfig;
import sample.entities.Sample;

/**
 * @DataJpaTest annotation을 제거함으로써 각 test가 완료되면 commit 된다.
 * application.test.properties에 정의된 'spring.jpa.hibernate.ddl-auto=create-drop' 에 의해 hibernate의 sessionFactory가 
 * close될 때 모든 생성된 스키마는 제거된다. 
 * 
 * @author solsi
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class SampleRepositoriesPersistTest {
	static int id = 1;
	
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
//		JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
//		JdbcTestUtils.deleteFromTables(jdbcTemplate, "SAMPLE");
	}

	@After
	public void tearDown() throws Exception {
	}
	
	private void insertTestData() {
		Sample sample = new Sample();
		sample.setName("sample-"+id++);
		sampleRepository.save(sample);
		log.info("[{}] sample inserted", sample);
		
		
		sample = new Sample();
		sample.setName("sample-2"+id++);
		sampleRepository.save(sample);
		log.info("[{}] sample inserted", sample);		
		
		sampleRepository.flush();
	}

	@Test
	public void _00_testSave() {
		Sample sample = new Sample();
		sample.setName("sample-"+id++);
		sampleRepository.persist(sample);
		sampleRepository.flush();
		log.info("[{}] sample inserted", sample);
		
		sample = new Sample();
		sample.setName("sample-"+id++);
		sampleRepository.save(sample);
		sampleRepository.flush();
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
		assertEquals("sample-3", data.get().getName());
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
		int res = JdbcTestUtils.countRowsInTable(jdbcTemplate, "SAMPLE");
		assertEquals(4, res);
	}
	
	@Test
	public void _02_testFindByName() {
		List<Sample> data = sampleRepository.findByName("sample-1");
		assertEquals(1, data.size());
		
		data = sampleRepository.findAllByName("sample%");
		assertEquals(4, data.size());
	}

	@Test
	public void _03_testDelete() {
		insertTestData();
		
		sampleRepository.deleteById(sampleRepository.findByName("sample-1").get(0).getId());
		sampleRepository.flush();
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
		int res = JdbcTestUtils.countRowsInTable(jdbcTemplate, "SAMPLE");
		assertEquals(5, res);
		
		sampleRepository.delete(sampleRepository.findByName("sample-2").get(0));
		sampleRepository.flush();
		
		res = JdbcTestUtils.countRowsInTable(jdbcTemplate, "SAMPLE");
		assertEquals(4, res);
	}
}
