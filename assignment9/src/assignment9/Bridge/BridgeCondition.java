package assignment9.Bridge;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BridgeCondition extends Bridge {

	//TODO use this given lock and create conditions form it
	//you might find that you need some additional variables
	final Lock bridgeLock = new ReentrantLock();
	final Condition notFull = bridgeLock.newCondition();
	volatile int count = 0;
	

	public void enterCar() throws InterruptedException {
		bridgeLock.lock();
			while(count >= 3) {
				try { notFull.await(); }
				catch(InterruptedException e){}
			}
			count++;
		bridgeLock.unlock();
	}

	public void leaveCar() {
		bridgeLock.lock();
			count--;
			notFull.signalAll();
		bridgeLock.unlock();
	}

	public void enterTruck() throws InterruptedException {
		bridgeLock.lock();
			while(count >= 1) {
				try { notFull.await(); }
				catch(InterruptedException e){}
			}
			count += 3;
		bridgeLock.unlock();
	}

	public void leaveTruck() {
		bridgeLock.lock();
			count -= 3;
			notFull.signalAll();
		bridgeLock.unlock();
	}

	public static void main(String[] args) {
		Random r = new Random();
		BridgeCondition b = new BridgeCondition();
		for (int i = 0; i < 100; ++i) {
			if (r.nextBoolean()) {
				(new Car()).driveTo(b);
			} else {
				(new Truck()).driveTo(b);
			}
		}
	}

}
