package assignment8.livelock;

public class Spoon {
	Diner owner;
	Diner turn;

	public Spoon(Diner d) {
		owner = d;
		turn = d;
	}

	public synchronized Diner getOwner() {
		return owner;
	}

	public synchronized void setOwner(Diner d) {
		owner = d;
	}

	public synchronized void use() {
		System.out.printf("%s had dinner!%n", owner.name);
	}
}
