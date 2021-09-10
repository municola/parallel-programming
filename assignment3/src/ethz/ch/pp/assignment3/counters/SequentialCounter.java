package ethz.ch.pp.assignment3.counters;

/**
 * 	Correct.
 */
public class SequentialCounter implements Counter {
	
	int value = 0;
	
	public void increment() {
		value++;
	}
	
	public int value() {
		return value;
		
	}
	
}