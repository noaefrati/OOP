

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 
 * @author Noa Efrati and Ilana Sanka
 * 
 * class Ex3a:
 * The task was to write Write a class called Ex3A that represents a class to calculate prime numbers,
 * a class with a method that receives a number and duration of natural time,
 * and calculates if the natural number is a head - as long as the time allocated to the calculation has not passed,
 * as soon as the time passed for the function to throw an error.
 */
public class Ex3A {

	/**
	 * 
	 * The class "CallableTask" which is implements the interface - Callable.
	 * There is one field in this class and it is a number- num.
	 */
	static class CallableTask implements Callable<Boolean>{

		long num;

		/**
		 * @param num- the number we are getting from the user (to check if this number is a prime).
		 * It is a constructor that receives a value from the user.
		 */
		public CallableTask(long num){
			this.num=num;
		}

		/**
		 * Overriding the method "call()"- which is receives a Boolean.
		 */
		@Override
		public Boolean call() throws Exception{

			Boolean result = Boolean.valueOf(Ex3A_tester.isPrime(this.num));
			return result;	
		}

	}

	/**
	 * This function was built to be used as a casing function for the function given in the tester.
	 * The above function accepts two parameters:
	 * the maximum runtime value and the number we are checking for is a prime number.
	 * If the function of the function is completed within the time frame -
	 * The casing will immediately return the calculated value,
	 * and if the function is stuck on the casing,
	 * throw an error after no answer has been reached for max seconds.
	 * 
	 * @param n - the number we are checking for is a prime number.
	 * @param maxTime - the maximum runtime value.
	 * @return - Returns the truth if the number is a prime number and the time taken to calculate it is at the maximum run time,
	 * otherwise returns a false.
	 * @throws RuntimeException - If the running time exceeds the maximum running time then an exception will be thrown: " RuntimeException".
	 */
	public boolean isPrime(long n, double maxTime) throws RuntimeException{

		//Creating a thread pool - when we created a repository with a single thread
		ExecutorService ex = Executors.newSingleThreadExecutor();

		//the future result + creating a new CallableTask (which gets the number n)
		Future<Boolean> futureResult = ex.submit(new CallableTask(n));

		//Converts time from seconds to milliseconds
		long maxtime = (long)(maxTime*1000);

		Boolean ans=null;

		try{

			// timeout if the future takes more than maxTime milliseconds to return the result
			ans=futureResult.get(maxtime, TimeUnit.MILLISECONDS);


		} 
		catch (InterruptedException e) {

			// thread was interrupted
			e.printStackTrace();

		} 
		catch (ExecutionException e) {

			// thread threw an exception
			e.printStackTrace();

		}
		catch (TimeoutException e) {

			// timeout before the future task is complete
			e.printStackTrace();

		} finally {

			// shut down the executor manually and the future result.
			futureResult.cancel(true);
			ex.shutdown();
		}

		return ans;


	}


}
