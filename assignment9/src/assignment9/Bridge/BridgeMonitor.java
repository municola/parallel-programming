package assignment9.Bridge;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Dining philosophers problem
 * <ol type = 'a'>
 * 	<li>
 * 		Correct.
 * 	</li>
 * 	<li>
 * 		Correct. We introduce an ordering therefor breaking the symmetry.
 * 	</li>
 * 	<li>
 * 		Correct. Taking away meaning passing to another philosopher which does not have normally have access or
 * 		just fairness?	For the first one you'd need some information passing, I don't think the second one actually changes the outcome.
 * 	</li>
 * </ol>
 *
 * 	Correct. Boolean variables are more readable and the increment method is just confusing especially if you use it for decrementing.
 */
public class BridgeMonitor extends Bridge {
	// TODO use this object as a monitor
	// you might find that you need some additional variables.
	private final Object monitor = new Object();
	volatile int count = 0;

	public void enterCar() throws InterruptedException {
		synchronized (monitor) {
			while (count >= 3) {
				try {
					monitor.wait();
				} catch (InterruptedException e) {
				}
			}
			increment(1);
		}
	}

	public void leaveCar() {
		synchronized (monitor) {
			increment(-1);
			monitor.notifyAll();
		}
	}

	public void enterTruck() throws InterruptedException {
		synchronized (monitor) {
			while (count >= 1) {
				try {
					monitor.wait();
				} catch (InterruptedException e) {
				}
			}
			increment(3);
		}
	}

	public void leaveTruck() {
		synchronized (monitor) {
			increment(-3);
			monitor.notifyAll();
		}
	}

	public void increment(int value) {
		count += value;
	}

	public static void main(String[] args) {
		Random r = new Random();
		BridgeMonitor b = new BridgeMonitor();
		for (int i = 0; i < 100; ++i) {
			if (r.nextBoolean()) {
				(new Car()).driveTo(b);
			} else {
				(new Truck()).driveTo(b);
			}
		}
	}

}
