/** 
 * @author Noa Efrati and Ilana Sanka
 * This class (LineCounter) implements the interface Runnable for use of Threads.
 *
 */

public class LineCounter implements Runnable{

	private  String fileName;
	private  int countLines=0;

	/**
	 * Constructor
	 * @param fileName- The class constructor should get the filename.
	 */
	public LineCounter(String fileName){
	
		this.fileName=fileName;
	}
	
	/**
	 * @return the numbers of lines.
	 */
	public int getNumberOfLines(){

		return countLines;

	}
	
	/**
	 *run method: calling to "count()" function.
	 */
	@Override
	public void run()
	{ 
		countLines=Ex3B.count(fileName);

	}
	
}








