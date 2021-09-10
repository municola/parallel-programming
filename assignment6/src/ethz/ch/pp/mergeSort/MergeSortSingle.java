package ethz.ch.pp.mergeSort;

import ethz.ch.pp.util.ArrayUtils;

public class MergeSortSingle {

	private MergeSortSingle() {}

	public static int[] sort(int[] input) {
		return new MergeSortSingle().sort(input, 0, input.length);
	}

	public int[] sort(int[] input, int start, int length) {

		int[] result = new int[length];

		if (length == 1) {
			result[0] = input[start];

		} else if (length == 2) {
			if (input[start] > input[start + 1]) {
				result[0] = input[start + 1];
				result[1] = input[start];
			} else {
				result[0] = input[start];
				result[1] = input[start + 1];
			}
		} else if (length > 2) {
			int halfSize = (length) / 2;
			
			int[] firstHalf = this.sort(input, start, halfSize);
			int[] secondHalf = this.sort(input, start + halfSize, length - halfSize);

			ArrayUtils.merge(firstHalf, secondHalf, result);
		}
		return result;

	}
}
