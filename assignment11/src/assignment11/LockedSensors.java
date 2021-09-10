package assignment11;

/**
 * 	Correct. Good job for implementing the RWLock (even if this is copied from the solution, just rewriting code helps
 * 	understanding it a lot, if not even more impressive :)). Obviously this lock is writer-biased but one more step and you'd
 * 	have a fair lock (adding waitingReaders and writeWait).
 */
class LockedSensors implements Sensors {
	long time = 0;
	double data[]; 
	final RWLock ReadWrtiteLock = new RWLock();
    
	LockedSensors() {
    	time = 0;
    }
		
    // store data and timestamp
    // if and only if data stored previously is older (lower timestamp)
    public void update(long timestamp, double[] data) { 
    	ReadWrtiteLock.writeLock();
    	
    	try {
			if (timestamp > time) {
				if (this.data == null)
					this.data = new double[data.length];
					time = timestamp;
					for (int i=0; i<data.length;++i)
						this.data[i]= data[i];
			}
    	} finally {
    		ReadWrtiteLock.writeUnlock();
    	}
		
    }

    // pre: val != null
    // pre: val.length matches length of data written via update
    // if no data has been wristten previously, return 0
    // otherwise return current timestamp and fill current data to array passed as val
    public long get(double val[]) {
    	ReadWrtiteLock.readLock();
    	try {
	        if (time == 0) {
	        	return 0;
	    	} else {
	        	for (int i = 0; i<data.length; ++i) {
	        		val[i] = data[i];
	        	}
	        	return time;
	        }
    	} finally {
    		ReadWrtiteLock.readUnlock();
    	}
    }    	
}

class RWLock {
	int writers = 0;
	int readers = 0;
	int writersWaiting = 0;

	synchronized void readLock() {
		while (writers > 0 || writersWaiting > 0)
			try {
				wait();
			} catch (InterruptedException e) {	
		}
		readers++;
	}

	synchronized void readUnlock() {
		readers--;
		notifyAll();
	}
	
	synchronized void writeLock() {
		writersWaiting++;
		while (writers > 0 || readers > 0) {
			try { wait(); }
			catch (InterruptedException e) {}
		}
		writersWaiting--;
		writers++;
	}
	
	synchronized void writeUnlock() {
		writers--;
		notifyAll();
	}
}

/*
Meeting 11
------------------------
Um java readwriteLock fair zu machcen mache: readWriteLokc(true)
 */
/*
Normales ReadWriteLock klappt nicht. Die monitorThreads readen die ganze zeit, womit die 
sensoren nie writen können, den es wird immer einen reader geben, der das lock hält.
*/
