package ethz.ch.pp.mergeSort;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

/**
 * 	What exactly is a honobulu?
 * 	<code>invoke()</code> already calls <code>join()</code>.
*/
public class MergeSortMulti {

	private static final long serialVersionUID = 1531647254971804196L;

	public static int[] sort(int[] input, int numThreads) {
		ForkJoinPool fipool = new ForkJoinPool(numThreads);
		ForkJoinTask honobulu = new ForkJoinTask(0,input.length, input);
		fipool.invoke(honobulu);
		honobulu.join();
		return honobulu.result;
	}
}
