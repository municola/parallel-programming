package ethz.ch.pp.assignment2;

import ethz.ch.pp.assignment2.Main.ArraySplit;

public class MyThread extends Thread {
	int start;
	int length;
	ArraySplit[] split;
	int[] values;
	public int[] result;


	public MyThread(int start, int length, ArraySplit[] split, int[] values) {
		this.start = start;
		this.length = length;
		this.split = split;
		this.values = values;
	}

	public void run() {
		int[] arr = new int[split[start].length];

		for (int j = split[start].startIndex; j < (split[start].startIndex + split[start].length); j++) {
			arr[j - split[start].startIndex] = values[j];
		}

		result = Main.computePrimeFactors(arr);
	}

	public int[] get() {
		return result;
	}
}
