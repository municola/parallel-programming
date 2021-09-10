package assignment10;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

import assignment10.CoarseGrainedLockList.Node;

/**
 * 	Kind of correct. The test should only be after you have validated therefor you will sometimes return false even if
 * 	you shouldn't. (Creating the node before validating is wasting resources.)
 *
 *	Try it out, maybe they will fix the behavior you described.
 */
public class OptimisticLockList<T extends Comparable<T>> implements SortedListInterface<T> {

	/** Represents the start of the list. */
	private Node head;

	/** Represents the end of the list. */
	private Node tail;

	public OptimisticLockList() {
		head = new Node(Integer.MIN_VALUE);
		head.next = new Node(Integer.MAX_VALUE);
		tail = head.next;
	}

	@Override
	public boolean add(T item) {
		int key = item.hashCode();
		while (true) {
			Node pred = this.head;
			Node curr = pred.next;
			
			while(curr.key < key) {
				pred = curr;
				curr = curr.next;
			}
			
			try {
				pred.MyLock.lock();
				curr.MyLock.lock();
			
				if (validate(pred, curr)) {
					if (curr.key == key) {
						return false;
					} else {
						Node newAddedNode = new Node(item);
						newAddedNode.next = curr;
						pred.next = newAddedNode;
						return true;
					}
				}
				
			} finally {
				pred.MyLock.unlock();
				curr.MyLock.unlock();
			}
		}
	}

	@Override
	public boolean remove(T item) {
		int key = item.hashCode();
		while(true) {
			Node pred = this.head;
			Node curr = pred.next;
			
			while(curr.key < key) {
				pred = curr;
				curr = curr.next;
			}
			
			try {
				pred.MyLock.lock();
				curr.MyLock.lock();
				
				if (validate(pred, curr)) {
					if (curr.key != key) {
						pred.next = curr.next;
						return true;
					} else {
						return false;
					}
				}
			} finally {
				pred.MyLock.unlock();
				curr.MyLock.unlock();
			}
		}
	}

	@Override
	public boolean contains(T item) {
		int key = item.hashCode();
		while (true) {
			Node pred = head;
			Node curr = pred.next;
			
			while (curr.key < key) {
				pred = curr;
				curr = curr.next;
			}
			
			// braucht locks remove zuesrt dann contains, contains schneller als remove und gibt true zurück.
			try {
				pred.MyLock.lock();
				curr.MyLock.lock();
				
				if (validate(pred, curr)) {
					return curr.key == key;
				}
			} finally {
				pred.MyLock.unlock();
				curr.MyLock.unlock();
			}
		}
	}
	
	public  boolean validate(Node pred, Node curr) {
		Node entry = head;
		while (entry.key < pred.key) {
			entry = entry.next;
		}
		return (entry == pred && entry.next == curr);
	}

	@Override
	public LinkedList<T> toLinkedList() {
		LinkedList<T> list = new LinkedList<T>();
		Node curr = this.head.next;
		while (curr != tail) {
			list.add(curr.item);
			curr = curr.next;
		}
		return list;
	}

	/** list node */
	class Node {
		ReentrantLock MyLock;
		/** actual item */
		T item;

		/** item's hash code */
		int key;

		/** next entry in list */
		Node next;

		/**
		 * Constructor for usual entry
		 * 
		 * @param item
		 *            element in list
		 */
		Node(T item) {
			this.item = item;
			this.key = item.hashCode();
			this.MyLock = new ReentrantLock();
		}

		/**
		 * Constructor for sentinel entry
		 * 
		 * @param key
		 *            should be min or max int value
		 */
		Node(int key) {
			this.key = key;
			this.MyLock = new ReentrantLock();
		}
	}

}
