package assignment7;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 	Nearly correct.
 * 	<p>
 * 	Firstly if you check <code>from.getBalance() < amount</code> non-synchronized the balance can change before you make
 * 	the transaction. Secondly synchronizing separately for both parts of the transaction is incorrect at least for the
 * 	summing part. It exposes intermediate states to the system therefor not being atomic even though it should work.
 * 	</p>
 * 	<p>
 * 	To your questions:
 * 		1. No, certain locks should be faster than synchronized as it is the simplest of implementations of a reentrant lock.
 * 		2. Have you heard of wait / notify? lk.lock()? Synchronized? :)
 * 	</p>
 */
public class BankingSystem {

	protected List<Account> accountList;
	private Lock lk = new ReentrantLock();

	/**
	 * Initializes the BankingSystem:
	 * accountList is empty and totalMoneyInBank() should return 0.
	 */
	public BankingSystem() {
		setAccountList(new ArrayList<Account>());
	}

	/**
	 * Transfers Money from one account to another.
	 * 
	 * @param from Account to transfer money from.
	 * @param to Account to transfer money to.
	 * @param amount Amount to transfer.
	 * @return True if Money was transferred successfully.
	 *         False if there was not enough balance in from.
	 */
	public boolean transferMoney(Account from, Account to, int amount) {
		if (from.getBalance() < amount) {
			return false;
		} else {
			from.loseMoney(amount);
			to.getMoney(amount);
		}
		//System.out.println(from.getId());
		return true;
	}

	/**
	 * Returns the sum of a given list of accounts.
	 * 
	 * @fixme Tends to return wrong results :-(
	 */
	public int sumAccounts(List<Account> accounts) {
		int sum = 0;
		// lock all transactions		
		for (Account a : accounts) {
			synchronized(a) {
				sum += a.getBalance();
			}
		}
		// unlock
		return sum;
			
	}

	/**
	 * Calculates the total amount of money in the bank at any point in time.
	 * @return The total amount of money.
	 * 
	 * @fixme Tends to return wrong results :-(
	 */
	public int totalMoneyInBank() {
		return sumAccounts(getAccountList());
	}

	/**
	 * Adds a new account to the bank.
	 * The account needs to have a positive balance to be added to the system.
	 * 
	 * @param a New account
	 * @return True if account was added successfully.
	 *         False if account could not be added to the system 
	 *         (ie., account did not have enough balance).
	 */
	public boolean addAccount(Account a) {
		if (a.getBalance() >= 0) {
			getAccountList().add(a);
			return true;
		}
		else {
			return false;
		}
	}

	protected List<Account> getAccountList() {
		return accountList;
	}

	protected void setAccountList(List<Account> accountList) {
		this.accountList = accountList;
	}

}


/*
Use locks. Lock both accounts. And in order. (Ansonsten deadlock). Account gets a attribute lock.
And for the task 4. Copy the whole list and sort (ansosten deadlock mit accounts die locked werden von der transfermoney method
vielleicht. z.b zuerst der zweite account gelockt von sumIt, der transfermoney lockt den ersten und will den zweiten während sumit
den ersten will. 
*/


/*
For task 2/3
technically not wrong, but you want the whole transaction to be atomic.
-!- <you should also not sort the accounts in transaction but you the id, since a new account could be created. 
*/
