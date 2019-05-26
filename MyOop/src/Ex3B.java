import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 
 * @author Noa Efrati and Ilana Sanka.
 * In this section we will write a program that receives a list of text files and prints a few lines per file.
 * The plan should build thread for each file that should calculate the number of rows of each file at the same time.
 *This class has 4 static functions and a main function.
 */
public class Ex3B {
	/////.......................................b1......................................................//////
	/**
	 * CreateFiles(int n): A static function that creates a given number of text files.
	 * Each file contains a random number of lines,
	 * each line is written in one sentence: "World Hello."
	 * For a random number we used the random class of java
	 * (Random.util.java import) that allows you to get the same series of random numbers in different runs of the program.
	 * @param n- the number of the files the user wants to create.
	 * @return- a string array of all the files's names.
	 */
	public static String[] createFiles(int n){

		String filesName[] = new String[n];
		Random r = new Random(123);
		String text = "Hello World!";


		for(int i=0;i<filesName.length;i++){

			filesName[i]="File_"+i+".txt";
			int lines = r.nextInt(1000);

			FileWriter fw = null;

			try{

				fw=new FileWriter(filesName[i]);

				for(int j=0;j<lines;j++){
					fw.write(text);
					fw.write(System.getProperty( "line.separator" ));
				}
				fw.close();

			}catch(IOException e){
				e.printStackTrace();
			}
		}
		return filesName;
	}

	/**
	 * deleteFiles- this static function deletes all the files, by giving array of strings (files names).
	 * @param fileNames
	 */
	public static void deleteFiles(String[] fileNames){

		for(int i=0;i<fileNames.length;i++){
			File f=new File(fileNames[i]);
			f.delete();
		}

	}


	/**
	 * countLinesThreads: The (static) function accepts multiple files as an argument.
	 * The function creates files, triggers for each file the "thread" and prints the total number of rows in all the files.
	 * The function also needs to print the runtime of the threads (not including creating and deleting the files)
	 * at the end of the function should delete all the files.
	 * 
	 * @param numFiles- the numbers of the files the user wants to create.
	 * 
	 */
	public static void countLinesThreads(int numFiles){

		String[] fileNames=createFiles(numFiles);

		Thread[] threads=new Thread[numFiles]; //creating an array of Threads- so that we can activate THREADS one by one.

		LineCounter[] lc=new LineCounter[numFiles];	//creating an array of LineCounter- the threads will activate the run method (in the LineCounter class)-
		//by giving any thread in the array a new variable of LineCounter 
		int rows_result=0;

		long start= System.currentTimeMillis();



		for(int i=0;i<fileNames.length;i++){

			lc[i]=new LineCounter(fileNames[i]);//creating a new variable of LineCounter,getting every file name.

			threads[i]=new Thread(lc[i]);//creating a new thread,that gets the value of the new LineCounter variable.

			threads[i].start();//starts running the threads
		}


			for(int j=0;j<fileNames.length;j++){

				try{
					threads[j].join();//Waiting for threads to finish theirs run

				}catch(InterruptedException e ){
					e.printStackTrace();
				}

				rows_result=rows_result+lc[j].getNumberOfLines();//sum up the sum of all the lines.
			}
		

			long end=System.currentTimeMillis()-start;

			System.out.println("countLinesThreads - total runtime: "+(end)+" milli seconds");
			System.out.println("countLinesThreads - total lines: " +rows_result); 

			deleteFiles(fileNames);

		}

		/////...............................................b2.....................................................//////

		/**
		 * countLinesThreadPool-The function accepts multiple files as an argument.
		 * The function creates files,
		 * triggers for each file the thread generated in TreadPool and prints the total number of rows in all the files.
		 * The function also needs to print the total run time (not including creating and deleting files) at the end of the function should delete all files.
		 * @param num-The function accepts multiple files as an argument.
		 */
		public static void countLinesThreadPool(int num){

			// Source: https://www.javatpoint.com/java-executors-callable-method //

			String files[]=createFiles(num);
			ExecutorService executor= Executors.newFixedThreadPool(5); //create a threadPool 
			List<Future<Integer>> list = new ArrayList<Future<Integer>>();  //create new list of Future
			int rows_result=0;

			long start=System.currentTimeMillis();

			for( int i = 0 ;i<files.length ; i++ ){

				Callable<Integer> callable = new LineCounterCallable(files[i]); //Create a callable variable that works with threadpool 

				Future<Integer> ftr = executor.submit(callable);//creating new future variable, and insert into the pool

				list.add(ftr);   //adding the new future variable into the list of futures variables.
			}


			for(Future<Integer> fut : list){    
				try {
					rows_result=fut.get()+rows_result;//getting from the list the number of lines from every file (the return value).
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   
			}
			long end=System.currentTimeMillis();

			System.out.println("countLinesThreadPool - total runtime : "+(end-start)+" milli seconds");
			System.out.println("countLinesThreadPool - total lines: "+rows_result);

			executor.shutdown();
			deleteFiles(files);
		}

		/////..................................................b3......................................................//////
		/**
		 *  This function calculates the total number of rows without using threads.
		 * The function creates files, reads files one by one,
		 * prints the total number of rows and the runtime (not including creating and deleting files).
     * At the end of the function should delete all files.
		 * @param numFiles-This function accepts the number of files the user wants to create
		 */

		public static void countLinesOneProcess(int numFiles){

			String files[]=createFiles(numFiles);

			int rows_result=0;
			long start=System.currentTimeMillis();

			for(int i=0;i<files.length;i++){

				rows_result=count(files[i])+rows_result;// We've summed up the number of rows in all the files we've created
			}

			long end=System.currentTimeMillis();
			System.out.println("countLinesOneProcess - total runtime: "+(end-start)+" milli seconds");
			System.out.println("countLinesOneProcess - total lines: "+rows_result);
			deleteFiles(files);

		}

		/**
		 *This function is intended to calculate the number of rows in the same file.
		 * @param filename
		 * @return- the number of lines in specific file.
		 */
		@SuppressWarnings("resource")
		public static int count(String filename) {

			//source : https://stackoverflow.com/questions/8898590/short-form-for-java-if-statement//

			try {

				InputStream is = new BufferedInputStream(new FileInputStream(filename));
				byte[] c = new byte[1024];

				int readChars = is.read(c);
				if (readChars == -1) {

					return 0;//If there is nothing to read from the file
				}

				int count = 0;

				// Count the remaining characters
				while (readChars != -1) {

					for (int i=0; i<readChars; ++i) {

						if (c[i] == '\n') {

							++count;
						}
					}
					readChars = is.read(c);
				}

				is.close();

				return count == 0 ? 1 : count; 

			} catch(IOException e) {
				e.printStackTrace();	
			}
			return 0;
		}


		public static void main(String[]args){

			countLinesThreads(1000);
			countLinesThreadPool(1000);
			countLinesOneProcess(1000);


		}
	}

