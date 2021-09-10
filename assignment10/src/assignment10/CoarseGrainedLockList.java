package assignment10;

import java.util.LinkedList;

import assignment10.SequentialList.Node;

/**
 * 	Correct.
 */
public class CoarseGrainedLockList<T extends Comparable<T>> implements SortedListInterface<T> {

	/** Represents the start of the list. */
	private Node head;

	/** Represents the end of the list. */
	private Node tail;

	public CoarseGrainedLockList() {
		head = new Node(Integer.MIN_VALUE);
		head.next = new Node(Integer.MAX_VALUE);
		tail = head.next;
	}

	@Override
	public synchronized boolean add(T item) {
		int key = item.hashCode();
		Node start = head;
		while (key > start.next.key) {
			start = start.next;
		}
		if (key == start.key) {
			return false;
		} else {
			Node next = start.next;
			Node newNode = new Node(item);
			start.next = newNode;
			newNode.next = next;
			return true;
		}
	}

	@Override
	public synchronized boolean remove(T item) {
		int key = item.hashCode();
		Node start = head;
		Node pre = head;
		while (start.key < key) {
			pre = start;
			start = start.next;
		}
		if (key == start.key) {
			pre.next = start.next;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public synchronized boolean contains(T item) {
		int key = item.hashCode();
		Node start = head;
		while (start.next.key < key) {
			start = start.next;
		}
		return (start.next.key == key);
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
		}

		/**
		 * Constructor for sentinel entry
		 * 
		 * @param key
		 *            should be min or max int value
		 */
		Node(int key) {
			this.key = key;
		}
	}

}


/*
 * 	@Override
	public synchronized boolean add(T item) {
		int key = item.hashCode();
		Node start = head;
		while (key > start.next.key) {
			start = start.next;
		}
		if (key == start.key) {
			return false;
		} else {
			Node next = start.next;
			Node newNode = new Node(item);
			start.next = newNode;
			newNode.next = next;
			return true;
		}
	}

	@Override
	public synchronized boolean remove(T item) {
		int key = item.hashCode();
		Node start = head;
		Node pre = head;
		while (key > start.next.key) {
			pre = start;
			start = start.next;
		}
		if (key == start.key) {
			pre.next = start.next;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public synchronized boolean contains(T item) {
		int key = item.hashCode();
		Node start = head;
		while (key > start.next.key) {
			start = start.next;
		}
		if (key == start.key) {
			return true;
		} else {
			return false;
		}
	}
	*/
