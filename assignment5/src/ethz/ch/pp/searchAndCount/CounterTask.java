package ethz.ch.pp.searchAndCount;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import ethz.ch.pp.util.Workload;

/**
 * 	Correct. No need to call <code>get()</code> on both, instead call <code>count()</code> on one directly else some
 * 	of your threads will do no actual work at all. Actually with ExecutorService this is even more important as there is
 * 	no work stealing and you are creating more tasks than available threads.
 */
public class CounterTask implements Callable<Integer> {

	int[] input;
	int start;
	int end;
	int cutOff;
	Workload.Type workloadType;
	ExecutorService executorService;

	public CounterTask (int[] input, int start, int end, Workload.Type wt, ExecutorService ex, int cutOff) {
		this.input = input;
		this.start = start;
		this.end = end;
		this.workloadType = wt;
		this.executorService = ex;
		this.cutOff = cutOff;
	}
	@Override
	public Integer call() throws InterruptedException, ExecutionException {
		if (end-start < cutOff) {
			return count(start, end);
		}
		int middle = start + (end-start)/2;
		
		Callable<Integer> t1 = new CounterTask(input, 0, middle, workloadType, executorService, cutOff);
		Callable<Integer> t2 = new CounterTask(input, middle+1, end, workloadType, executorService, cutOff);
		
		Future<Integer> f1 = executorService.submit(t1);
		Future<Integer> f2 = executorService.submit(t2);
		
		return f1.get() + f2.get();
	}
	
	
	private int count(int start, int end) {
		int count = 0;
		for (int i = start; i <= end; i++) {
			if (Workload.doWork(input[i], workloadType))
				count++;
		}	
		return count;		
	}
	
	

}
















/*
 * package ethz.ch.pp.searchAndCount;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import ethz.ch.pp.util.Workload;

public class CounterTask implements Callable<Integer> {

	int[] input;
	int start;
	int end;
	int cutOff;
	Workload.Type workloadType;
	ExecutorService executorService;

	public CounterTask (int[] input, int start, int end, Workload.Type wt, ExecutorService ex, int cutOff) {
		this.input = input;
		this.start = start;
		this.end = end;
		this.workloadType = wt;
		this.executorService = ex;
		this.cutOff = cutOff;
	}
	@Override
	public Integer call() throws InterruptedException, ExecutionException {
		if (end - start == 2) {
			int count = 0;
			if (Workload.doWork(input[start], workloadType)) {
				count++;
			}
			if (Workload.doWork(input[start+1], workloadType)) {
				count++;
			}
			if (Workload.doWork(input[end], workloadType)) {
				count++;
			}
			return count;
		}
		if (end - start == 1) {
			int count = 0;
			if (Workload.doWork(input[start], workloadType)) {
				count++;
			}
			if (Workload.doWork(input[start+1], workloadType)) {
				count++;
			}
			return count++;
		}
		int middle = start + (end-start)/2;
		
		Callable<Integer> t1 = new CounterTask(input, 0, middle, workloadType, executorService, cutOff);
		Callable<Integer> t2 = new CounterTask(input, middle+1, end, workloadType, executorService, cutOff);
		
		Future<Integer> f1 = executorService.submit(t1);
		Future<Integer> f2 = executorService.submit(t2);
		
		return f1.get() + f2.get();
	}

}
*/
