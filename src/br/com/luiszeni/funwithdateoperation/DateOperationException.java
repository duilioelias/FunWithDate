package br.com.luiszeni.funwithdateoperation;

/** This class represents an exception that is throw when an operation in date is wrong.
 * @author Luis Zeni
 * @version 1.1
 */
public class DateOperationException extends Exception{
	
	private static final long serialVersionUID = -3760182173431783790L;

	public DateOperationException(char op) {
		super("Invalid value of operation. You informed the value " + op + ". The valid values are: + or -");
	}

}
