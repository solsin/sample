package sample.exception;

/**
 * ��Ͽ��� ��� �����͸� Ŭ���ϰ� ��ȸȭ���� ����ؾ� �� ��, ��� �����Ͱ� �������� �ʴ� �Ͱ� ���� �翬�� �־���� �����Ͱ� �������� ���� ��� �߻�
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
