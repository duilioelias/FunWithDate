package br.com.luiszeni.funwithdateoperation;

/**
  * @author zeni
 */
public class Main {

	public static void main(String[] args) {

		String data = "01/03/2010 23:00"; //input data to the function

		char op = '+'; //op of the func
		
		int minute = 4000; //number of minute to add our sub
		int hour = 0;   //number of hour to add our sub
		int days = 0; //number of days to add our sub 
		int year = 0; //number of years to add our sub
	
		DateOperations dOp = new DateOperations();

		try {
					
			long value = minute + hour*DateOperations.NUMBER_OF_MINUTES_IN_HOUR
								+ days*DateOperations.NUMBER_OF_MINUTES_IN_DAY
								+ year*DateOperations.NUMBER_OF_MINUTES_IN_YEAR;
			
			String result = dOp.changeDate(data, op, value);

			System.out.println(" Original date: " + data);
			System.out.println("Resultant date: " + result);
			
		} catch (DateOperationException e) {
			e.printStackTrace();
		} catch (DateFormatException e) {
			e.printStackTrace();
		}
	}
}
