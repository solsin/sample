package processor.csv;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;

import com.opencsv.CSVReader;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import lombok.extern.slf4j.Slf4j;

/**
 * opencsv의 csv to bean mapping시 setId(Serializable id)을 PropertyEditor의 writable method로 처리하질 못한다.
 * 이를 fix하기 위해, HeaderColumnNameMappingStrategy의 caputerHeader 메서드 수행 후 csv에 ID column이 존재하면 
 * setId(Long id)를 writable method로 등록한다.
 * 
 * @author Chulhui Park <chulhui72@gmail.com>
 */
@Slf4j
public class CustomHeaderColumnNameMappingStrategy<T> extends HeaderColumnNameMappingStrategy<T> {

	@Override
	public void captureHeader(CSVReader reader) throws IOException, CsvRequiredFieldEmptyException {
		super.captureHeader(reader);
		log.info("finish capture header");

		// spring data의 AbstracctPersistable을 상속받아, setId가 unwritable method일 경우 강제 세팅
		PropertyDescriptor descriptior = descriptorMap.get("ID");
		if (descriptior == null) {
			return;
		}
		if (descriptior.getWriteMethod() == null) {
			log.info("force PropertyDescriptor.writerMehtod of ID");
			Method method;
			try {
				method = getType().getMethod("setId", Long.class);
				descriptior.setWriteMethod(method);
			} catch (Exception e) {
				log.warn("error occured during writerMehtod of PropertyDescriptior for ID:{}", e.getMessage(), e);
			}
		}
	} 

}
