package processor;

import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.sql.DataSource;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;

import lombok.extern.slf4j.Slf4j;
import sample.config.TestConfig;
import sample.entities.SampleBoard;

/**
 * Test : test data ����� ���� �ʿ��� *.csv format ����� ���� �����͸� jpa ����� entity�� �����Ͽ� �ϰ� ����ϴ� CSVFormatProcessor
 *  
 * @author Chulhui Park
 *
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@DataJpaTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
class CSVFormatProcessorTest {

	@Autowired
	ApplicationContext ctx;
	
	@Autowired
	CSVFormatProcessor<SampleBoard> processor;
	
	@Autowired
	DataSource ds;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	/**
	 * test : id�� ���� csv data import  
	 * @throws Exception
	 */
	@Test
	void testProcess() throws Exception {
		log.info("start test: process");
		processor.process(SampleBoard.class, "classpath:data/sample/entities/SampleBoard.csv");
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
		int res = JdbcTestUtils.countRowsInTable(jdbcTemplate, "SAMPLE_BOARD");
		assertEquals(21, res);
		
		log.info("finish test: process");
	}

	/**
	 * test : id�� ������ csv data import  
	 * @throws Exception
	 */
	@Test
	void testProcess2() throws Exception {
		log.info("start test: process2");
		processor.process(SampleBoard.class, "classpath:data/sample/entities/SampleBoard2.csv");
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
		int res = JdbcTestUtils.countRowsInTable(jdbcTemplate, "SAMPLE_BOARD");
		assertEquals(21, res);
		
		jdbcTemplate = new JdbcTemplate(ds);
		res = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "SAMPLE_BOARD", "id=158");
		assertEquals(1, res);
		
		jdbcTemplate = new JdbcTemplate(ds);
		res = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "SAMPLE_BOARD", "id=1");
		assertEquals(0, res);
		
		log.info("finish test: process2");
	}
	
	/**
	 * test : root direcotry ���� �����ϴ� ��� csv �ϰ� import
	 * @throws Exception
	 */
	@Test
	void testProcess3() throws Exception {
		log.info("start test: process3");
		
		File file = new File("./src/test/resources/data");
		processor.process(file);
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
		int res = JdbcTestUtils.countRowsInTable(jdbcTemplate, "SAMPLE_BOARD");
		assertEquals(42, res);
		
		jdbcTemplate = new JdbcTemplate(ds);
		res = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "SAMPLE_BOARD", "id=158");
		assertEquals(1, res);
		
		jdbcTemplate = new JdbcTemplate(ds);
		res = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "SAMPLE_BOARD", "id=1");
		assertEquals(0, res);
		
		log.info("start test: process3");
	}
}
