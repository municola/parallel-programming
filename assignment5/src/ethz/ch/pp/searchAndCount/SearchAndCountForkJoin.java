package ethz.ch.pp.searchAndCount;

import java.util.concurrent.ForkJoinPool;

import ethz.ch.pp.util.Workload;

public class SearchAndCountForkJoin {	
	
	//TODO: implement using ForkJoinPool and RecursiveTask
	public static Integer countNoAppearances(int[] input, Workload.Type wt, int cutOff, int noThreads) {
		int ans = count(0,input.length, wt, noThreads, input, cutOff);
		return ans;
	}
	
	public static int count(int start, int end, Workload.Type workloadType,int noThreads, int[] input, int cutOff) {
		ForkJoinPool fipool = new ForkJoinPool(noThreads);
		return fipool.invoke(new CounterTaskFork(0, input.length-1, workloadType, noThreads, input, cutOff));
	}

}