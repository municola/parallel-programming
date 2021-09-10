package assignment9.Database;

/**
 * 	Correct.
 */
public class MySemaphore {

	private volatile int count;
	private final Object monitor = new Object();

	public MySemaphore(int maxCount) {
		this.count = maxCount;
	}

	public void acquire() throws InterruptedException {
		synchronized(monitor) {
			while (count < 1) {
				monitor.wait();
			}
			count--;
		}
	}

	public void release() {
		synchronized(monitor) {
			count++;
			monitor.notifyAll();
		}
	}

}
