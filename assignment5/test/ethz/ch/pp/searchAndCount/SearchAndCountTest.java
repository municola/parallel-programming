package ethz.ch.pp.searchAndCount;

import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Test;

import ethz.ch.pp.searchAndCount.SearchAndCountForkJoin;
import ethz.ch.pp.searchAndCount.SearchAndCountSeq;
import ethz.ch.pp.util.DatasetGenerator;
import ethz.ch.pp.util.Workload;

public class SearchAndCountTest {

	@Test
	public void test100HEAVYrandom() {

		DatasetGenerator dg = new DatasetGenerator(100);
		int[] testNumbers = dg.generate();
		int resSingle = SearchAndCountSeq.countNoAppearances(testNumbers, Workload.Type.HEAVY);
		int resMultiple = SearchAndCountForkJoin.countNoAppearances(
				testNumbers, Workload.Type.HEAVY, 2, 2);
		
		Assert.assertEquals(resSingle, resMultiple);
	}
	
	@Test
	public void test100HEAVYrandomSeq() {

		DatasetGenerator dg = new DatasetGenerator(100);
		int[] testNumbers = dg.generate();
		int resSingle = SearchAndCountSeq.countNoAppearances(testNumbers, Workload.Type.HEAVY);
		int resMultiple = SearchAndCountSeqDivideAndConquer.countNoAppearances(
				testNumbers,Workload.Type.HEAVY);
		
		Assert.assertEquals(resSingle, resMultiple);
	}

	@Test
	public void test100HEAVYrandomExecutor() throws InterruptedException, ExecutionException {

		DatasetGenerator dg = new DatasetGenerator(100);
		int[] testNumbers = dg.generate();
		int resSingle = SearchAndCountSeq.countNoAppearances(testNumbers, Workload.Type.HEAVY);
		int resMultiple = SearchAndCountParDivideAndConquer.countNoAppearances(
				testNumbers,Workload.Type.HEAVY, 100, 4);
		
		Assert.assertEquals(resSingle, resMultiple);
	}
	
	@Test
	public void test100Lightrandom() {

		DatasetGenerator dg = new DatasetGenerator(100);
		int[] testNumbers = dg.generate();
		int resSingle = SearchAndCountSeq.countNoAppearances(testNumbers, Workload.Type.LIGHT);
		int resMultiple = SearchAndCountForkJoin.countNoAppearances(
				testNumbers, Workload.Type.LIGHT, 2, 2);

		Assert.assertEquals(resSingle, resMultiple);
	}

	@Test
	public void test1000000HEAVYrandom() {

		DatasetGenerator dg = new DatasetGenerator(100000);
		int[] testNumbers = dg.generate();
		int resSingle = SearchAndCountSeq.countNoAppearances(testNumbers, Workload.Type.HEAVY);
		int resMultiple = SearchAndCountForkJoin.countNoAppearances(
				testNumbers, Workload.Type.HEAVY, 10000, 4);

		Assert.assertEquals(resSingle, resMultiple);
	}

	@Test
	public void test1000000Lightrandom() {

		DatasetGenerator dg = new DatasetGenerator(1000000);
		int[] testNumbers = dg.generate();
		int resSingle = SearchAndCountSeq.countNoAppearances(testNumbers, Workload.Type.LIGHT);
		int resMultiple = SearchAndCountForkJoin.countNoAppearances(
				testNumbers, Workload.Type.LIGHT, 100, 2);
		
		Assert.assertEquals(resSingle, resMultiple);
	}

}
