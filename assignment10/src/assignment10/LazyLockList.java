package assignment10;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

import assignment10.OptimisticLockList.Node;

/**
 * 	Same as before. There is no need to use an if statement in contains just return the boolean directly.
 */
public class LazyLockList<T extends Comparable<T>> implements SortedListInterface<T> {

	/** Represents the start of the list. */
	private Node head;

	/** Represents the end of the list. */
	private Node tail;

	public LazyLockList() {
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
						/* nicht (sonst manchmal nullpointerException:
						pred.next = newAddedNode;
						newAddedNode.next = curr;
						 */
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
						curr.flag = 1;
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
		Node pred = this.head;
		Node curr = pred.next;
		while (curr.key < key) {
			pred = curr;
			curr = curr.next;
		}
		return (curr.key == key && curr.flag == 0);
	}
	
	public boolean validate(Node pred, Node curr) {
		return (pred.flag == 0 && curr.flag == 0 && pred.next == curr);
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
		/** actual item */
		ReentrantLock MyLock;
		int flag = 0;
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
			this.flag = 0;
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
			this.flag = 0;
			this.MyLock = new ReentrantLock();
		}
	}
}