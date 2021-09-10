package assignment8.random;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  Correct. Use try / finally block for locks.
 * 
 *  Analyzing locks
 *  a)  Correct. This is a very reduced state space diagram. :)
 * 
 *  b)  Correct but no implementation details. For example a total ordering could be used.
 * 
 *  Atomic operations
 *  a)  Correct.
 * 
 *  b)  We will certainly not use TES at least not until you explain how this would work.
 * 
 *  c)  Correct. For low resource contention.   
 */
public class LockedRandom implements RandomInterface {
    private static final long a = 25214903917L;
    private static final long c = 11;
    private long state;
    public Lock lk = new ReentrantLock();

    public LockedRandom(long seed) {
        state = seed;
    }

    @Override
    public int nextInt() {
    	lk.lock();
	        // TODO locking here
	        // get the current seed value
	        long orig = state;
	        // using recurrence equation to generate next seed
	        long next = (a * orig + c) & (~0L >>> 16);
	        // store the updated seed
	        state = next;
        lk.unlock();
        return (int) (next >>> 16);
    }
}
