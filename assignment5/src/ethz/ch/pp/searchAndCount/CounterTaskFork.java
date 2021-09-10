package ethz.ch.pp.searchAndCount;

import java.util.concurrent.RecursiveTask;

import ethz.ch.pp.util.Workload;

/**
 * 	Correct. Bonus points for using only one fork and rediscovering the for loop. :)
 */
public class CounterTaskFork extends RecursiveTask<Integer> {
	int start;
	int end;
	Workload.Type workloadType;
	int noThreads;
	int[] input;
	int cutOff;

	CounterTaskFork(int start, int end, Workload.Type workloadType, int noThreads, int[] input, int cutOff) {
		this.start = start;
		this.end = end;
		this.workloadType = workloadType;
		this.noThreads = noThreads;
		this.input = input;
		this.cutOff = cutOff;
	}

	@Override
	protected Integer compute() {
		if (end - start > cutOff) {
			return count(start, end);
		}
		int middle = start + (end - start) / 2;
		CounterTaskFork right = new CounterTaskFork(start, middle, workloadType, noThreads, input, cutOff);
		CounterTaskFork left = new CounterTaskFork(middle + 1, end, workloadType, noThreads, input, cutOff);

		left.fork();
		int rightAns = right.compute();
		int leftAns = left.join();

		return rightAns + leftAns;
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
