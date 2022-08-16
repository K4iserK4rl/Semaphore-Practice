import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class DiningPhilosophers {
    // Number of Philosophers 
    static int philosopher = 5;

    // Initializing an array of philosophers
    static Philosopher philosophers[] = new Philosopher[philosopher];

    // Initializing an array of chopsticks equal to number of philosophers
    static Chopstick chopsticks[] = new Chopstick[philosopher];

    static class Chopstick {
        // Constructing a Semaphore with 1 resource
        public Semaphore mutex = new Semaphore(1);

        void grab() {
            try {
                // Grabs the resource
                mutex.acquire();
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }

        void release() {
            // Releases the resource
            mutex.release();
        }

        boolean isFree() {
            // Returns the current number of resources available
            // If greater than 0 returns TRUE since there are resources available and FALSE if otherwise
            return mutex.availablePermits() > 0;
        }
    }

    static class Philosopher extends Thread {
        public int number;
        public Chopstick left;
        public Chopstick right;

        Philosopher(int num, Chopstick l, Chopstick r) {
            number = num;
            left = l;
            right = r;
        }

        public void run() {
            while(true) {
                // If both chopsticks are free the philosophers will grab them
                left.grab();
                System.out.println("Philosopher " + (number + 1) + " grabs left chopstick.");
                right.grab();
                System.out.println("Philosopher " + (number + 1) + " grabs right chopstick.");

                // Once a Philosopher has both chopsticks they eat
                eat();

                // After eating the Philosopher will release both chopsticks 
                left.release();
                System.out.println("Philosopher " + (number + 1) + " releases left chopstick.");
                right.release();
                System.out.println("Philosopher " + (number + 1) + " releases right chopstick.");
            }
        }

        void eat() {
            try {
                // Generates a random number between 0 and 1000 for sleep time in milliseconds
                int sleepTime = ThreadLocalRandom.current().nextInt(0, 1000);

                System.out.println("Philosopher " + (number + 1) + " eats for " + sleepTime +"ms"); 
                
                //sleeps the thread for a specified time  
                Thread.sleep(sleepTime);
                
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
    }

    public static void main(String args[]) {
        for(int i = 0; i < philosopher; i++) {
            chopsticks[i] = new Chopstick();
        }

        for(int i = 0; i < philosopher; i++) {
            if(i % 2 == 0) {
                philosophers[i] = new Philosopher(i, chopsticks[(i + 1) % philosopher], chopsticks[i]);
            }
            else {
                philosophers[i] = new Philosopher(i, chopsticks[i], chopsticks[(i + 1) % philosopher]);
            }
            philosophers[i].start();
        }

        while(true) {
            try {
                Thread.sleep(1000);
                boolean deadlock = true;

                for(Chopstick cs : chopsticks) {
                    if(cs.isFree() == true) {
                        deadlock = false;
                        break;
                    }
                }

                if(deadlock) {
                    Thread.sleep(1000);
                    System.out.println("All Philosophers Eat!");
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }

        System.exit(0);
    }
}