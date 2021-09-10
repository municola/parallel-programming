package ethz.ch.pp.assignment3.counters;


/**
 * 	Correct.
 */
public class  SynchronizedCounter implements Counter {
	public int value = 0;
	
	public synchronized void increment() {
		value++;
	}
	
	public synchronized int value() {
		return value;
	}
}