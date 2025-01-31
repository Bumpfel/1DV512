import java.util.Random;

public class Philosopher implements Runnable {
	
	private int id;
	
	private final ChopStick leftChopStick;
	private final ChopStick rightChopStick;
	
	private Random randomGenerator = new Random();
	
	private int numberOfEatingTurns = 0;
	private int numberOfThinkingTurns = 0;
	private int numberOfHungryTurns = 0;

	private double thinkingTime = 0;
	private double eatingTime = 0;
	private double hungryTime = 0;

	private boolean debug;
	
	public Philosopher(int id, ChopStick leftChopStick, ChopStick rightChopStick, int seed, boolean debug) {
		this.id = id;
		this.leftChopStick = leftChopStick;
		this.rightChopStick = rightChopStick;
		
		this.debug = debug;
		/*
		 * set the seed for this philosopher. To differentiate the seed from the other philosophers, we add the philosopher id to the seed.
		 * the seed makes sure that the random numbers are the same every time the application is executed
		 * the random number is not the same between multiple calls within the same program execution 
		 
		 * NOTE
		 * In order to get the same average values use the seed 100, and set the id of the philosopher starting from 0 to 4 (0,1,2,3,4). 
		 * Each philosopher sets the seed to the random number generator as seed+id. 
		 * The seed for each philosopher is as follows:
		 * 	 	P0.seed = 100 + P0.id = 100 + 0 = 100
		 * 		P1.seed = 100 + P1.id = 100 + 1 = 101
		 * 		P2.seed = 100 + P2.id = 100 + 2 = 102
		 * 		P3.seed = 100 + P3.id = 100 + 3 = 103
		 * 		P4.seed = 100 + P4.id = 100 + 4 = 104
		 * Therefore, if the ids of the philosophers are not 0,1,2,3,4 then different random numbers will be generated.
		 */
		
		randomGenerator.setSeed(id+seed);
	}
	public int getId() {
		return id;
	}

	public double getAverageThinkingTime() {
		/* TODO
		 * Return the average thinking time
		 * Add comprehensive comments to explain your implementation
		 */
		return thinkingTime / numberOfThinkingTurns;
	}

	public double getAverageEatingTime() {
		/* TODO
		 * Return the average eating time
		 * Add comprehensive comments to explain your implementation
		 */
		return eatingTime / numberOfEatingTurns;
	}

	public double getAverageHungryTime() {
		/* TODO
		 * Return the average hungry time
		 * Add comprehensive comments to explain your implementation
		 */
		return hungryTime / numberOfHungryTurns;
	}
	
	public int getNumberOfThinkingTurns() {
		return numberOfThinkingTurns;
	}
	
	public int getNumberOfEatingTurns() {
		return numberOfEatingTurns;
	}
	
	public int getNumberOfHungryTurns() {
		return numberOfHungryTurns;
	}

	public double getTotalThinkingTime() {
		return thinkingTime;
	}

	public double getTotalEatingTime() {
		return eatingTime;
	}

	public double getTotalHungryTime() {
		return hungryTime;
	}

	@Override
	public void run() {
		/* TODO
		 * Think,
		 * Hungry,
		 * Eat,
		 * Repeat until thread is interrupted
		 * Increment the thinking/eating turns after thinking/eating process has finished.
		 * Add comprehensive comments to explain your implementation, including deadlock prevention/detection
		 */
		
		final int RANDOM_TIME = 1000; //== [0, 1000[              randomGenerator(1001) would be [0-1000]
		
		try {
			while(true) {
				// thinking
				int num = randomGenerator.nextInt(RANDOM_TIME);
				numberOfThinkingTurns ++;
				Thread.sleep(num);
				if(debug)
					System.out.println("Philosopher_" + id + " is THINKING");
				thinkingTime += num;
				
				
				
				//hungry (waiting for chopsticks to be available)
				if(debug)
					System.out.println("Philosopher_" + id + " is HUNGRY");
				long timeStamp = System.currentTimeMillis();
				numberOfHungryTurns ++;
				 // Deadlocks should not happen since a philosopher only picks up the chop sticks when both the left and right are available
				while(!leftChopStick.isAvailable() || !rightChopStick.isAvailable()) {
					Thread.sleep(1);
				}
				long timeTaken = System.currentTimeMillis() - timeStamp;
				leftChopStick.pickUp();
				rightChopStick.pickUp();

				if(debug) {
					System.out.println("Philosopher_" + id + " picked up Chopstick_" + leftChopStick.getId());
					System.out.println("Philosopher_" + id + " picked up Chopstick_" + rightChopStick.getId());
				}
				hungryTime += timeTaken;
				
				
				
				//eating
				num = randomGenerator.nextInt(RANDOM_TIME);
				if(debug)
					System.out.println("Philosopher_" + id + " is EATING");
				numberOfEatingTurns ++;
				Thread.sleep(num);
				rightChopStick.putDown();
				leftChopStick.putDown();
				if(debug) {
					System.out.println("Philosopher_" + id + " released Chopstick_" + rightChopStick.getId());
					System.out.println("Philosopher_" + id + " released Chopstick_" + leftChopStick.getId());
				}
				eatingTime += num;
			}
		}
		catch(InterruptedException e) {
		}
	
	}
	
}
