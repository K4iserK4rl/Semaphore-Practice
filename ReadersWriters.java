import java.util.concurrent.Semaphore;

public class ReadersWriters {
    static Semaphore readSem = new Semaphore(1);
    static Semaphore writerSem = new Semaphore(1);
    static int readCount = 0;

    static class Reader implements Runnable {
        @Override
        public void run() {
            try {
                // Readers acquire the resource
                readSem.acquire();
                readCount++;

                if(readCount == 1) {
                    writerSem.acquire();
                } 

                readSem.release();

                // Reading Section
                System.out.println(Thread.currentThread().getName() + " is READING.");
                Thread.sleep(2000);
                System.out.println(Thread.currentThread().getName() + " has FINISHED READING.");

                // Readers release the resource so another reader or a writer can come in
                readSem.acquire();
                readCount--;

                if(readCount == 0) {
                    writerSem.release();
                }

                readSem.release();

            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
    }

    static class Writer implements Runnable {
        @Override
        public void run() {
            try {
                
                writerSem.acquire();
                System.out.println(Thread.currentThread().getName() + " is WRITING.");
                Thread.sleep(3000);
                System.out.println(Thread.currentThread().getName() + " has FINISHED WRITING.");
                writerSem.release();

            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
    }

    public static void main(String args[]) {
        // Creating Reader and Writer objects
        Reader reader = new Reader();
        Writer writer = new Writer();

        // Initializing Threads
        Thread t1 = new Thread(reader);
        t1.setName("Reader 1");

        Thread t2 = new Thread(reader);
        t2.setName("Reader 2");

        Thread t3 = new Thread(writer);
        t3.setName("Writer 1");

        Thread t4 = new Thread(reader);
        t4.setName("Reader 3");

        // Begin Threads
        t1.start();
        t3.start();
        t2.start();
        t4.start();
    } 
}