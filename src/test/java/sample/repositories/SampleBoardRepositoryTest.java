package sample.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.extern.slf4j.Slf4j;
import processor.CSVFormatProcessor;
import sample.config.TestConfig;
import sample.entities.SampleBoard;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class SampleBoardRepositoryTest {
	private static boolean dataLoaded = false;
	
	@Autowired
	CSVFormatProcessor processor;
	
	@Autowired
	SampleBoardRepository sbRepository;
	
	@Autowired
	SampleBoardMapper sbMapper;

	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterAll
	public static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	public void setUp() throws Exception {
		if (dataLoaded) {
			return;
		}
		processor.process(SampleBoard.class, "classpath:data/sample/entities/SampleBoard.csv");
		dataLoaded = true;
	}

	@AfterEach
	public void tearDown() throws Exception {
	}

	@Test
	public void testFindBySubject() {
		List<SampleBoard> list = sbRepository.findBySubject("subject-1%");
		
		assertEquals(11, list.size());
		assertEquals("subject-19", list.get(0).getSubject());
	}

	@Test
	public void testFindAllSort() {
		Iterable<SampleBoard> list = sbRepository.findAll();
		int num = 0;
		for(SampleBoard sampleBoard : list) {
			log.info("SampleBoard:{}", sampleBoard);
			num++;
		}
		assertEquals(21, num);
	}

	@Test
	public void testFindAllPageable() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<SampleBoard> paged = sbRepository.findAll(pageable);
		int num=0;
		for (SampleBoard sampleBoard : paged) {
			log.info("SampleBoard:{}", sampleBoard);
			num++;
		}
		assertEquals(10, num);
	}

	@Test
	public void testFindWithNativeQeury() {
		Collection<SampleBoard> list = sbRepository.findWithNativeQeury("subject-2%", "<<<<content-2>>>>");
		int num=0;
		for (SampleBoard sampleBoard : list) {
			log.info("SampleBoard:{}", sampleBoard);
			num++;
		}
		assertEquals(1, num);
		
		list = sbRepository.findWithNativeQeury("subject-2%", "<<<<content%");
		num=0;
		for (SampleBoard sampleBoard : list) {
			log.info("SampleBoard:{}", sampleBoard);
			num++;
		}
		assertEquals(3, num);
	}
	
	@Test
	public void testFindWithNativeQeury2() {
		Collection<Object[]> list = sbRepository.findWithNativeQeury2("subject-2%", "<<<<content-2>>>>");
		assertEquals(1, list.size());
		Object[] objs = list.iterator().next();
		assertEquals("subject-2", objs[1]);
	}
	
	@Test
	public void testFindWithNativeQeury3() {
		Collection<SampleBoard.SB> list = sbRepository.findWithNativeQeury3("subject-2%", "<<<<content-2>>>>");
		assertEquals(1, list.size());
		SampleBoard.SB sb = list.iterator().next();
		assertEquals("subject-2", sb.getSubject());
	}
	
	@Test
	public void testFindWithMybatis() {
		Collection<SampleBoard> list = sbMapper.findWithMybatis("subject-2%", "<<<<content-2>>>>");
		assertEquals(1, list.size());
		SampleBoard sb = list.iterator().next();
		assertEquals("subject-2", sb.getSubject());
	}
}
