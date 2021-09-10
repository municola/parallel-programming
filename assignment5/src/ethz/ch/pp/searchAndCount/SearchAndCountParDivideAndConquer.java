package ethz.ch.pp.searchAndCount;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ethz.ch.pp.util.Workload;

public class SearchAndCountParDivideAndConquer {
	
	int[] input;
	Workload.Type workloadType;

	private SearchAndCountParDivideAndConquer(int[] input, Workload.Type wt) {
		this.input = input;		
		this.workloadType = wt;
	}
	
	public static int countNoAppearances(int[] input, Workload.Type wt, int cutOff, int numThreads) throws InterruptedException, ExecutionException {
		ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
		
		Callable<Integer> t1 = new CounterTask(input, 0, input.length-1, wt, executorService, cutOff);
		Future<Integer> f1 = executorService.submit(t1);
		
		int result = f1.get();
		executorService.shutdown();
		
		return result;
	}

}