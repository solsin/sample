package processor.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;

/**
 * opencsv의 CSVReader가 comment를 추가하지 않아 확장
 * 
 * @author Chulhui Park <chulhui72@gmail.com>
 *
 */
public class CommentSkipCsvReader extends CSVReader {
	private String commentStart = "#";

	public void setCommentStart(String commentStart) {
		this.commentStart = commentStart;
	}

	public CommentSkipCsvReader(Reader reader, char separator, char quotechar, boolean strictQuotes) {
		super(reader, separator, quotechar, strictQuotes);
	}

	public CommentSkipCsvReader(Reader reader, char separator, char quotechar, char escape, int line,
			boolean strictQuotes) {
		super(reader, separator, quotechar, escape, line, strictQuotes);
	}

	public CommentSkipCsvReader(Reader reader, char separator, char quotechar, char escape, int line) {
		super(reader, separator, quotechar, escape, line);
	}

	public CommentSkipCsvReader(Reader reader, char separator, char quotechar, char escape) {
		super(reader, separator, quotechar, escape);
	}

	public CommentSkipCsvReader(Reader reader, char separator, char quotechar, int line) {
		super(reader, separator, quotechar, line);
	}

	public CommentSkipCsvReader(Reader reader, char separator, char quotechar) {
		super(reader, separator, quotechar);
	}

	public CommentSkipCsvReader(Reader reader, char separator) {
		super(reader, separator);
	}

	public CommentSkipCsvReader(Reader reader) {
		super(new BufferedReader(reader));
	}

	@Override
	public List<String[]> readAll() throws IOException {

		List<String[]> result = new ArrayList<String[]>();

		String[] nextLine;
		while ((nextLine = readNext()) != null) {
			result.add(nextLine);
		}

		return result;
	}

	@Override
	public String[] readNext() throws IOException {

		String[] nextLine = super.readNext();

		if (nextLine != null && nextLine.length > 0 && nextLine[0].startsWith(commentStart)) {
			return readNext();
		}

		return nextLine;
	}

}
