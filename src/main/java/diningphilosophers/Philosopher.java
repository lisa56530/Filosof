package diningphilosophers;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Philosopher extends Thread {
    private static int seed = 1;
    private final Random myRandom = new Random(System.currentTimeMillis() + seed++);
    private final static int timeout = 1000;
    private final ChopStick myLeftStick;
    private final ChopStick myRightStick;
    private boolean running = true;

    public Philosopher(String name, ChopStick left, ChopStick right) {
        super(name);
        myLeftStick = left;
        myRightStick = right;
    }

    private void think() throws InterruptedException {
        int delay = myRandom.nextInt(500 + timeout);
        System.out.println(this.getName() + " pense... " + delay + " ms");
        sleep(delay); // Le thread peut être interrompu ici
        System.out.println(this.getName() + " arrête de penser");
    }
    private void eat() throws InterruptedException {
        int delay = myRandom.nextInt(100 + timeout);
        System.out.println(this.getName() +  " mange..." + delay + " ms");
        sleep(delay); // Le thread peut être interrompu ici
        System.out.println(this.getName() + " arrête de manger");
    }



    @Override
    public void run() {
        while (running) {
            try {
                think();
                // Aléatoirement prendre la baguette de gauche puis de droite ou l'inverse
                switch (new Random().nextInt(2)) {
                    case 0:
                        myLeftStick.tryTake();
                        think(); // pour augmenter la probabilité d'interblocage
                        myRightStick.tryTake();
                        break;
                    case 1:
                        myRightStick.tryTake();
                        think(); // pour augmenter la probabilité d'interblocage
                        myLeftStick.tryTake();
                }
                // Si on arrive ici, on a pu "take" les 2 baguettes
                eat();
                // On libère les baguettes :
                myLeftStick.release();
                myRightStick.release();
                // try again
            } catch (InterruptedException miam) {
                Logger.getLogger("Table").log(Level.SEVERE, "{0} Interrupted", this.getName());
            }
        }
    }

    // Permet d'interrompre le philosophe "proprement" :
    // Il relachera ses baguettes avant de s'arrêter
    public void leaveTable() {
        running = false;
    }


}
