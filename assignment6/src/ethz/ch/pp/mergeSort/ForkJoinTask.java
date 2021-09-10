package ethz.ch.pp.mergeSort;

import java.util.concurrent.RecursiveAction;

import ethz.ch.pp.util.ArrayUtils;

/**
 * 	Your ForkJoinTask extends RecursiveAction which extends ForkJoinTask<Void>, interesting.
 * 	Correct. Don't fork twice, instead fork once and the other part directly.
 */
public class ForkJoinTask extends RecursiveAction {
	int start;
	int end;
	int[] input;
	int[] result;

	ForkJoinTask(int start, int end, int[] input) {
		this.start = start;
		this.end = end;
		this.input = input;
		this.result = new int[end - start];
	}

	@Override
	protected void compute() {
		if (end - start == 1) {
			result[0] = input[start];
			return;
		} else if (end - start == 2) {
			if (input[start] > input[start + 1]) {
				result[0] = input[start + 1];
				result[1] = input[start];
			} else {
				result[0] = input[start];
				result[1] = input[start + 1];
			}
			return;
		} else if (end - start > 2) {
			int middle = start + (end - start) / 2;

			ForkJoinTask firstPart = new ForkJoinTask(start, middle, input);
			ForkJoinTask secondPart = new ForkJoinTask(middle, end, input);

			firstPart.fork();
			secondPart.fork();

			firstPart.join();
			secondPart.join();

			ArrayUtils.merge(firstPart.result, secondPart.result, result);
		}
		return;
	}
}
