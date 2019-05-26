public class Ex3A_tester {

	public static double ENDLESS_LOOP=0.4;

	public static void main(String[]args){
		
		Ex3A ex3a=new Ex3A();
		long n=33333331;
		boolean ans=ex3a.isPrime(n,0.01);
		System.out.println("n="+n+" isPrime "+ans);

	}
	/** DONOT change this function!,it must be used
	 * byEx3A-isPrime(long,double)
	 */
	public static boolean isPrime(long n){

		boolean ans = true;

		if(n < 2)throw new RuntimeException("ERR: the parameter to the isPrime function	must be > 1 (got "+n+")!");

		int i = 2;
		double ns = Math.sqrt(n) ;

		while(i <= ns && ans){
			if (n % i == 0) ans = false;
			i = i + 1;
		}

		if(Math.random()<Ex3A_tester.ENDLESS_LOOP) while(true);

		return ans;
	}
	


}
