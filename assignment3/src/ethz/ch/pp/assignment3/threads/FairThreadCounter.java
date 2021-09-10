package ethz.ch.pp.assignment3.threads;

import ethz.ch.pp.assignment3.counters.Counter;

/**
 * 	Correct. If you use an <code>if</code> and it's not the turn of the thread it will wait as you mentioned. What will
 * 	happen once it gets notified? As if only checks the condition once it will execute no matter if it's the threads turn
 * 	therefor it will violate the round-robin principal. On every execution it will notify all other threads therefor making
 * 	the execution order completely arbitrary, only the current thread can't immediately execute.
 * 	<p>
 * 	As there are 4 threads created imagine that two have finished already.
 * 	Let's call these threads 0 and 1 with thread 0 being on the 8th iteration (i = 8, 0 <= i < 10) and thread 1 on the 9th of 10 .
 * 	Thread 0 is currently waiting and thread 1 just got the lock. What will happen?
 * 	</p>
 * 	<p>
 * 	Thread 1 will iterate the counter and notify thread 0 after which it will have reached i = 10 and release the lock as
 * 	it is finished.
 * 	Thread 0 will acquire the lock, increment the counter, notify nobody as all are finished and check the loop condition.
 * 	As i is currently only equal to 9 it will execute the last iteration of the loop. Therefor it will check the if condition which will
 * 	evaluate to false because the next id after 1 would be 2 not 0. It will wait but nobody will ever notify it
 * 	as it is completely alone in the whole runtime. :(
 * 	</p>
 */
public class FairThreadCounter extends ThreadCounter {

	public FairThreadCounter(Counter counter, int id, int numThreads, int numIterations) {
		super(counter, id, numThreads, numIterations);
	}
	
	/*
	 * Threads take the lock on the counter. 
	 * In each iteration we would like to increment the counter. But we can only if it's this threads turn to do so.
	 * But we can't afford to just not increment this iteration. Otherwise we would be missing incrementations and
	 * we wouldn't reach 400000. Therefore we put the thread in its waiting modus and wait till we are at our turn
	 * and we can increment (still in the same iteration of the for-loop).
	 * It is important to put a while and not an if (had it in the lecture) because:
	 * ????
	 * if i put and if the counter goes up to (between 360000 - 390000), but then the counter wont be incremented again
	 * and the program doesn't terminate. Why??
	 * In my view we check if the thread P can increment, if yes do so, otherwise put the thread P to wait. Then an other thread
	 * increments the counter and notifys all other threads. Then all other threads get notified. If Thread P still can't increment
	 * it will be put to wait again. Otherwise it increments.
	 * I can not explain myself why the program doesn't terminate and its even more weird that the counter actually gets 
	 * incremented at least 360000 times.
	 */
 	@Override
	public void run() {
		synchronized (counter) {
			for (int i = 0; i < numIterations; i++) {
				System.out.println(this.id);
				while (counter.value() % numThreads != id) {
					try {
						counter.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				counter.increment();
				counter.notifyAll();
			}
		}
	}
}

