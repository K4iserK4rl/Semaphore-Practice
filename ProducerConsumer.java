import java.util.concurrent.Semaphore;
import java.util.LinkedList;

public class ProducerConsumer {

    static LinkedList<Integer> list = new LinkedList<Integer>();
    static Semaphore sem = new Semaphore(0);
    static Semaphore mutex = new Semaphore(1);

    static class Consumer implements Runnable {
        public void run() {
            try {
                sem.acquire(1);
                mutex.acquire();
                System.out.println(Thread.currentThread().getName() + " consumed " + list.removeFirst());
                mutex.release();
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
    }

    static class Producer implements Runnable {
        public void run() {
            try {
                int N = 0;

                mutex.acquire();
                list.add(N++);
                System.out.println(Thread.currentThread().getName() + " produced " + N);
                mutex.release();
                sem.release(1);
                Thread.sleep(1000);

            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
    }

    public static void main(String args[]) {
        // Creating Consumer and Producer objects
        Consumer consumer = new Consumer();
        Producer producer = new Producer();

        // Initializing Threads
        Thread t1 = new Thread(consumer);
        t1.setName("Consumer 1");

        Thread t2 = new Thread(consumer);
        t2.setName("Consumer 2");

        Thread t3 = new Thread(producer);
        t3.setName("Producer 1");

        Thread t4 = new Thread(producer);
        t4.setName("Producer 2");

        // Begin Threads
        t3.start();
        t1.start();
        t4.start();
        t2.start();
    }
}