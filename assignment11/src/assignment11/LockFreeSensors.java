package assignment11;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 	Correct. Try to think about what the while-condition could be. We want to try setting the value, therefor just use
 * 	the bool from CAS in the condition instead.
 *
 * 	Your report is incorrect as it states the implementation is not wait-free. It actually is as the number of times a thread
 * 	loops is always finite because of the timestamps.
 */
class LockFreeSensors implements Sensors {
	AtomicReference<SensorData> dataAtmoic = new AtomicReference<SensorData>();
	
    LockFreeSensors() {
    	SensorData senData = new SensorData(0, new double[]{0,0});
    	dataAtmoic.set(senData);
    }

    // store data and timestamp
    // if and only if data stored previously is older (lower timestamp)
    public void update(long timestamp, double[] data) {
    	SensorData newData = new SensorData(timestamp, data);
    	
    	while(true) {
        	SensorData expected = dataAtmoic.get();
        
        	// out-dated
        	if (timestamp <= expected.timestamp) {
        		return;
        	}
        	
        	// could update
        	if(dataAtmoic.compareAndSet(expected, newData)) {
        		break;
        	};
    	}
    }


    public long get(double val[]) {
    	SensorData senData = dataAtmoic.get();
    	double[] values = senData.getValues();
    	long timeStamp = senData.getTimestamp();

    	// not new data
    	if (timeStamp == 0) {
    		return 0;
    	}
    	
    	// new data
    	for (int i = 0; i < values.length; i++) {
    		val[i] = values[i];
    	}
    	return timeStamp;
    }

}


/* ORIGINAL
    public long get(double val[]) {
    	
    	long time = dataAtmoic.get().timestamp;
    	if (time == 0) {
        	return 0;
    	} else {
        	return time;
        }
    }
*/


/* TA-CORRECT
    public long get(double val[]) {
    	SensorData currentData = dataAtmoic.get();
    	double[] values = currentData.getValues();
    	
    	if(values == null) {
    		return 0;
    	}
    	
    	for (int i = 0; i < val.length; i++) {
    		val[i] = values[i];
    	}
    	
    	return currentData.getTimestamp();
    }
*/
