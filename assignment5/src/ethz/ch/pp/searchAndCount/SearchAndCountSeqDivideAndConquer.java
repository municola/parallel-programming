package ethz.ch.pp.searchAndCount;

import ethz.ch.pp.util.Workload;


/**
 * 	Have you heard of the rare and elusive for loop? :)
 */
public class SearchAndCountSeqDivideAndConquer {
	
	int[] input;
	Workload.Type workloadType;

	private SearchAndCountSeqDivideAndConquer(int[] input, Workload.Type wt) {
		this.input = input;		
		this.workloadType = wt;
	}

	public static int countNoAppearances(int[] input, Workload.Type wt) {
		SearchAndCountSeqDivideAndConquer counter = new SearchAndCountSeqDivideAndConquer(input, wt);
		int result = counter.count(0, input.length-1);
		return result;
	}
	
	public int count(int start, int end) {
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
		int num1 = this.count(start, middle);
		int num2 = this.count(middle + 1, end);
		return num1 + num2;
	}

}











/*
package ethz.ch.pp.searchAndCount;

import ethz.ch.pp.util.Workload;

public class SearchAndCountSeqDivideAndConquer {
	
	int[] input;
	int start;
	int end;
	int result;
	Workload.Type workloadType;

	private SearchAndCountSeqDivideAndConquer(int[] input, int start, int end, Workload.Type wt) {
		this.start = start;
		this.end = end;
		this.input = input;		
		this.workloadType = wt;
	}

	public static int countNoAppearances(int[] input, Workload.Type wt) {
		SearchAndCountSeqDivideAndConquer counter = new SearchAndCountSeqDivideAndConquer(input, 0, input.length, wt);
		int num = counter.count();
		System.out.println("myne" + counter.result);
		return num;
	}
	
	public int count() {
		if (this.end - this.start == 2) {
			int result  = input[this.start] + input[this.start + 1] + input[this.end];
			this.result = result;
			return result;
		}
		if (this.end - this.start == 1) {
			int result =  input[this.start] + input[this.end];
			this.result = result;
			return result;
		}
		int middle = (this.start - this.end)/2;
		SearchAndCountSeqDivideAndConquer counter1 = new SearchAndCountSeqDivideAndConquer(input, this.start, middle, this.workloadType);
		SearchAndCountSeqDivideAndConquer counter2 = new SearchAndCountSeqDivideAndConquer(input, middle+1, this.end, this.workloadType);
		this.result = counter1.result + counter2.result;
		return counter1.result + counter2.result;
	}

}
*/



















/*
package ethz.ch.pp.searchAndCount;

import ethz.ch.pp.util.Workload;

public class SearchAndCountSeqDivideAndConquer {
	
	int[] input;
	Workload.Type workloadType;

	private SearchAndCountSeqDivideAndConquer(int[] input, Workload.Type wt) {
		this.input = input;		
		this.workloadType = wt;
	}

	public static int countNoAppearances(int[] input, Workload.Type wt) {
		SearchAndCountSeqDivideAndConquer counter = new SearchAndCountSeqDivideAndConquer(input, wt);
		int result = counter.count(0, input.length-1);
		return result;
	}
	
	public int count(int start, int end) {
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
		int num1 = this.count(start, middle);
		int num2 = this.count(middle + 1, end);
		return num1 + num2;
	}

}
*/