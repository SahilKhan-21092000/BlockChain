package NewCoinPackage;

public class EmptyQueueException extends Exception{
	
	public EmptyQueueException(){
		System.out.println("Queue is empty!");
	}

}