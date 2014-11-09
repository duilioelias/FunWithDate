package br.com.luiszeni.funwithdateoperation;

/** This class represents an exception that is throw when an date is in wrong format.
 * @author Luis Zeni
 * @version 1.1
 */
public class DateFormatException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public DateFormatException(String message) {
		super(message);
	}
	
}
