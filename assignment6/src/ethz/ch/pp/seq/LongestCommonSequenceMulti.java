package ethz.ch.pp.seq;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class LongestCommonSequenceMulti extends RecursiveTask<Sequence> {
	
	private static final long serialVersionUID = 4179716026313995745L;
	int[] input;
	int start;
	int end;
	int left;
	int right;
	int longest;
	int index;
	
	public LongestCommonSequenceMulti(int[] input, int start, int end) {
		this.input = input;
		this.start = start;
		this.end = end;
		this.longest = longest;
	}
		
	public static Sequence longestCommonSequence(int[] input, int numThreads) {
		ForkJoinPool fipool = new ForkJoinPool(numThreads);
		LongestCommonSequenceMulti honobulu = new LongestCommonSequenceMulti(input,0,input.length-1);
		return fipool.invoke(honobulu);
	}

	@Override
	protected Sequence compute() {
		if (end-start == 1) {
			left = input[start];
			right = input[start];
			longest = 1;
			index = start;
		} else if (end-start == 2) {
			if (input[start+0] == input[start+1]) {
				left = input[start+0];
				right = input[start+1];
				longest = 2;
				index = start;
			} else {
				left = input[start+0];
				right = input[start+1];
				longest = 1;
				index = start+1;
			}
		} else if (end-start>2) {
			int middle = start + (end-start)/2;
			
			LongestCommonSequenceMulti left = new LongestCommonSequenceMulti(input, start, middle);
			LongestCommonSequenceMulti right = new LongestCommonSequenceMulti(input, middle+1, end);
			
			left.fork();
			right.fork();
			
			left.join();
			right.join();
			
			int temp;
			if (left.right == right.left) {
				
			}
		}
		return new Sequence(0, 0);		
	}
}

// Doch. Mach es doch so. Es darf in gewissen Fällen schon langsamer sein als die sequentielle lösung