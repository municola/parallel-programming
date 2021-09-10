package assignment8.livelock;

public class Diner {
	final String name;
	volatile int hungry;

	public Diner(String n) {
		name = n;
		hungry = 10;
	}

	public String getName() {
		return name;
	}

	public boolean isHungry() {
		if (hungry > 0) {
			return true;
		} return false;
	}

	public void eatWith(Spoon spoon, Diner spouse) {
		while (hungry>0) {
			// Don't have the spoon, so wait patiently for other one.
			if (spoon.owner != this) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					continue;
				}
				continue;
			}

			// If loved one is still hungry, insist upon passing the spoon.
			if (spouse.isHungry() && spoon.turn == spouse) {
				System.out.println();
				System.out.printf("%s: You go first dear %s!%n", name, spouse.getName());
				spoon.setOwner(spouse);
				continue;
			}

			// Spouse wasn't hungry, so finally eat
			//// Critical region start
			spoon.use();
			//// Critical region end
			this.hungry--;
			spoon.turn = spouse;
			spoon.setOwner(spouse);
			
			if(!this.isHungry()) {
				System.out.printf("%s: I am full, dear %s!%n", name, spouse.getName());
			}
		}
	}
}
