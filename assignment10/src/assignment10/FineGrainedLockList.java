package assignment10;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 	Kind of correct. You are holding to many locks in the hand-over-hand-locking. Please try to name your variables
 * 	logically instead of start which changes position.
 * 	Use try / finally blocks. Try to avoid code duplication as in the contains method where you would have been able to
 * 	write a lot cleaner code instead of an if statement which once returns true and the other time returns false.
 */
public class FineGrainedLockList<T extends Comparable<T>> implements SortedListInterface<T> {

	/** Represents the start of the list. */
	private Node head;

	/** Represents the end of the list. */
	private Node tail;

	public FineGrainedLockList() {
		head = new Node(Integer.MIN_VALUE);
		head.next = new Node(Integer.MAX_VALUE);
		tail = head.next;
	}

	@Override
	public boolean add(T item) {
		int key = item.hashCode();
		head.MyLock.lock();
		head.next.MyLock.lock();
		Node start = head;
		Node next = head.next;
		while(next.key < key) {
			next.next.MyLock.lock();
			Node temp = start;
			next = next.next;
			start = start.next;
			temp.MyLock.unlock();
		}
		if (next.key == key) {
			start.MyLock.unlock();
			next.MyLock.unlock();
			return false;
		} else {
			Node newNode = new Node(item);
			start.next = newNode;
			newNode.next = next;
			start.MyLock.unlock();
			next.MyLock.unlock();
			return true;
		}
	}

	@Override
	public boolean remove(T item) {
		int key = item.hashCode();
		head.MyLock.lock();
		head.next.MyLock.lock();
		Node start = head;
		Node next = head.next;
		while(next.key < key) {
			next.next.MyLock.lock();
			next = next.next;
			Node temp = start;
			start = start.next;
			temp.MyLock.unlock();
		}
		if (next.key == key) {
			start.next = next.next;
			start.MyLock.unlock();
			next.MyLock.unlock();
			return true;
		} else {
			start.MyLock.unlock();
			next.MyLock.unlock();
			return false;
		}
	}

	@Override
	public boolean contains(T item) {
		int key = item.hashCode();
		head.MyLock.lock();
		head.next.MyLock.lock();
		Node start = head;
		Node next = head.next;
		while(next.key < key) {
			next.next.MyLock.lock();
			next = next.next;
			Node temp = start;
			start = start.next;
			temp.MyLock.unlock();
		}
		if (next.key == key) {
			start.MyLock.unlock();
			next.MyLock.unlock();
			return true;
		} else {
			start.MyLock.unlock();
			next.MyLock.unlock();
			return false;
		}
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
