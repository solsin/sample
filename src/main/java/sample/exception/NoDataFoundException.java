package sample.exception;

/**
 * 목록에서 대상 데이터를 클릭하고 조회화면을 출력해야 할 때, 대상 데이터가 존재하지 않는 것과 같은 당연히 있어야할 데이터가 존재하지 않을 경우 발생
 * 
 * @author Chulhui Park <chulhui72@gmail.com>
 *
 */
public class NoDataFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoDataFoundException(String msg) {
		super(msg);
	}
}
