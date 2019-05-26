

import java.util.concurrent.Callable;

/**
 * 
 * @author Noa Efrati and Ilana Sanka.
 * This class implements the interface "callable".
 * 
 *
 */
public  class LineCounterCallable implements Callable<Integer>{

	private String fileName;
	
	
/**
 * A constructor
 * @param fileName
 */
	public LineCounterCallable(String fileName){
		this.fileName=fileName;
	}
	/**
	 * The call method - calling for the function "count()" with the current fileName, and returns an Integer.
	 * If necessary the function throws an exception.
	 */
	@Override
	public Integer call() throws Exception{

		return Ex3B.count(this.fileName);

	}
}

