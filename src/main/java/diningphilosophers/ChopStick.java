package diningphilosophers;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;

public class ChopStick {
    private static int stickCount = 0;
    private boolean iAmFree = true;
    // private int delay = 0 ;
    private final int myNumber;

    public ChopStick() {
        myNumber = ++stickCount;

    }

    private final Lock verrou = new ReentrantLock();

    private final Condition libre = verrou.newCondition();

    public boolean tryTake() throws InterruptedException {
        verrou.lock();
        try {
            while (!iAmFree) {
                libre.await();
            }
            iAmFree = false;
            System.out.println("Stick " + myNumber + " c'est déjà pris !");
            return true;
        } finally {
            verrou.unlock();
        }
    }

    synchronized public void release() {
        // assert !iAmFree;
        System.out.println("baguette " + myNumber + " relâchée");
        iAmFree = true;
        notifyAll(); // On prévient ceux qui attendent que la baguette soit libre
    }

    @Override
    public String toString() {
        return "baguette #" + myNumber;
    }

}

/* 

    synchronized public boolean take() throws InterruptedException {
        if (!iAmFree) {
            try {
                wait(delay);
            } catch (InterruptedException miam) {
            }
            if (!iAmFree) {
                System.out.println("Stick " + myNumber + " Prend donc mais relâche après !");
                return false;
            }
        } else
            iAmFree = false;
        System.out.println("Stick " + myNumber + " C'est pris !");
        return true;
    }

    public void release() throws InterruptedException{
                verrou.lock();
                try {
                    iAmFree = true;
                    System.out.println("Stick " + myNumber + " Released");
                    libre.signalAll();
                } finally {
                    verrou.unlock();
                }
            }
 */
