I will try to solve the given problem:

You should create the following function:
public String changeDate(String date, char op, long value);
Where,
       date: An date as String in the format dd/MM/yyyy HH24:mi;

       op: Can be only + | -;

       value: the value that should be incremented/decremented. It will be expressed in minutes;

 

Restrictions:
       You shall not work with non-native classes / libraries;

       You shall not make use of neither Date nor Calendar classes;

       If the op is not valid an exception must be thrown;

       If the value is smaller than zero, you should ignore its signal;

       If the result sum is bigger than max value to the field, you should increment its immediate bigger field;

       Ignore the fact that February have 28/29 days and always consider only 28 days;

       Ignore the daylight save time rules.

Example:
changeDate("01/03/2010 23:00", '+', 4000) = "04/03/2010 17:40"