/**
 * Author: Nicolas Muntwyler
 **/

package ethz.ch.pp.assignment2;

import java.util.Random;

/** Corrections
 *	<p>
 *	   Task A:	Correct. Use join to wait for termination. Use <code>Thread.currentThread().getId()</code>
 *	   to check if it's not the main thread.
 *	</p>
 *	<p>
 *	   Task B:	Correct. You should have returned the computed values from the new thread.
 *	</p>
 *	<p>
 *	   Task C:	Correct.
 *	</p>
 *	<p>
 *	   Task D:	Correct.
 *	</p>
 *	<p>
 *	   Task E:	Correct. There is some redundant code and you should name your variables logically (not just t).
 *	   			To your question: (// Why can't I just write : t[i].get()[j] or t[i].result ??)
 *	   			Answer: In line 201: <code>Thread t[] = new MyThread[numThreads];</code> you declare all elements of t
 *	   					as instances of the Thread class. Thread does not have any function named get or field called result.
 *
 *	</p>
 */

public class Main {

	public static void main(String[] args) throws InterruptedException {

		taskA();

		int[] input1 = generateRandomInput(1000);
		int[] input2 = generateRandomInput(10000);
		int[] input3 = generateRandomInput(100000);
		int[] input4 = generateRandomInput(1000000);

		// Sequential version
		taskB(input1);
		taskB(input2);
		taskB(input3);
		taskB(input4);

		// Parallel version
		taskE(input1, 4);
		taskE(input2, 4);
		taskE(input3, 4);
		taskE(input4, 4);

		// Task F
		// Intel i7-4790K (4 cores, 8 threads)
		for (int i = 1; i < 8; i++) {
			int num = (int) Math.pow(2, i);
			System.out.println("Threads: " + num);
			taskE(input1, num);
			taskE(input2, num);
			taskE(input3, num);
			taskE(input4, num);
			System.out.println();
		}
		// => fastest with 8 Threads

		long threadOverhead = taskC();
		System.out.format("Thread overhead on current system is: %d nano-seconds\n", threadOverhead);
	}

	private final static Random rnd = new Random(42);

	public static int[] generateRandomInput() {
		return generateRandomInput(rnd.nextInt(10000) + 1);
	}

	public static int[] generateRandomInput(int length) {
		int[] values = new int[length];
		for (int i = 0; i < values.length; i++) {
			values[i] = rnd.nextInt(99999) + 1;
		}
		return values;
	}

	public static int[] computePrimeFactors(int[] values) {
		int[] factors = new int[values.length];
		for (int i = 0; i < values.length; i++) {
			factors[i] = numPrimeFactors(values[i]);
		}
		return factors;
	}

	public static int numPrimeFactors(int number) {
		int primeFactors = 0;
		int n = number;
		for (int i = 2; i <= n; i++) {
			while (n % i == 0) {
				primeFactors++;
				n /= i;
			}
		}
		return primeFactors;
	}

	public static class ArraySplit {
		public final int startIndex;
		public final int length;

		ArraySplit(int startIndex, int length) {
			this.startIndex = startIndex;
			this.length = length;
		}
	}

	// TaskD

	/** Calculates the partitions: 1002 -> 250, 250, 250, 252 **/
	public static ArraySplit[] PartitionData(int length, int numPartitions) {
		ArraySplit[] arr = new ArraySplit[numPartitions];

		for (int i = 0; i < numPartitions - 1; i++) {
			arr[i] = new ArraySplit((length / numPartitions) * i, length / numPartitions);
		}
		arr[numPartitions - 1] = new ArraySplit((length / numPartitions) * (numPartitions - 1), length - ((length / numPartitions) * (numPartitions - 1)));
		return arr;
	}

	public static void taskA() {
		Thread t = new Thread() {
			public void run() {
				System.out.println("hello world");
			}
		};
		t.start();
	}

	/** own Thread vs main Thread**/
	public static int[] taskB(final int[] values) throws InterruptedException {
		// Own-thread compute
		Thread t = new Thread() {
			public void run() {
				long startTime = System.nanoTime();

				computePrimeFactors(values);

				long endTime = System.nanoTime();
				long elapsedNs = endTime - startTime;
				double elapsedMs = elapsedNs / 1.0e6;

				System.out.println("Thread: " + elapsedMs);
			}
		};

		t.start();
		t.join();

		// Main-thread compute
		long startTime = System.nanoTime();

		computePrimeFactors(values);

		long endTime = System.nanoTime();
		long elapsedNs = endTime - startTime;
		double elapsedMs = elapsedNs / 1.0e6;

		System.out.println("Main: " + elapsedMs);

		return null;
	}

	// Returns overhead of creating thread in nano-seconds
	public static long taskC() throws InterruptedException {
		long startTime = System.nanoTime();

		Thread t = new Thread() {
			public void run() {
			}
		};

		t.start();
		t.join();

		long endTime = System.nanoTime();
		long elapsedNs = endTime - startTime;

		return elapsedNs;
	}

	/** splits the work with multiple threads: 
	 * - Array of threads
	 * - Each thread solves the assigned part of the work and returns the result
	 * - The result from above is copied into a solution array
	 * - return the solution array
	 **/
	public static int[] taskE(final int[] values, final int numThreads) throws InterruptedException {
		ArraySplit[] split = PartitionData(values.length, numThreads);
		int[] solution = new int[values.length];

		long startTime = System.nanoTime();

		Thread t[] = new MyThread[numThreads];
		for (int i = 0; i < numThreads; i++) {
			t[i] = new MyThread(i, split[i].length, split, values);
			t[i].start();
		}

		for (int i = 0; i < numThreads; i++) {
			t[i].join();
		}

		for (int i = 0; i < numThreads; i++) {
			for (int j = 0; j < split[i].length; j++) {
				solution[split[i].startIndex + j] = ((MyThread) t[i]).get()[j];
				// Why can't I just write : t[i].get()[j] or t[i].result ??
			}
		}

		long endTime = System.nanoTime();
		long elapsedNs = endTime - startTime;
		double elapsedMs = elapsedNs / 1.0e6;
		System.out.println(elapsedMs);

		return solution;
	}
}

