package assignment9.Database;

/**
 * 	Correct. Normally a barrier counts upwards. Set the count to zero at the end to get a reusable barrier.
 */
public class MyBarrier {
	
	volatile int count;
	private final Object monitor = new Object();
	
	MyBarrier(int n){
		this.count = n;
    }

	public void await() throws InterruptedException {
		synchronized(monitor) {
			count--;
			if (count <= 0) {
				monitor.notifyAll();
			} else {
				while (count > 0) {
					try { monitor.wait(); }
					catch (InterruptedException e) {}
				}
			}
		}
	}
}