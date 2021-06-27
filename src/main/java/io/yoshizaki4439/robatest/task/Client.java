package io.yoshizaki4439.robatest.task;

public class Client {
	private Worker worker = new Worker();
	
	public int callAriari(int x) {
		return worker.ariari(x * 2); 
	}
	
	public int callNasinasi(int x) {
		worker.nasinasi();
		return x * 2;
	}
}
