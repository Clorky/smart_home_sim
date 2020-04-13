package application;

public class DataReceiver implements Runnable {

    public static boolean running = true;

    @Override
    public void run() {
        while(running){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("working");
        }
    }
}
