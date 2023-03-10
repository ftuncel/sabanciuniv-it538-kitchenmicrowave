package edu.sabanciuniv.kitchenmicrowavesimulation;

import java.util.Timer;
import java.util.TimerTask;

public class CookingTimer{

    private int counterSecond;
    private boolean isPause = false;

    public CookingTimer(int second) {
        if (second>0){
            this.counterSecond = second;
        }
    }

    public int getTime() {
        return counterSecond;
    }

    public void start (){
        System.out.println("CookingTimer.start method called. counterSecond= "+counterSecond + " - isPaused: "+ isPause);

        if (counterSecond == 0){
            System.out.println("CookingTimer.start failed. counterSecond= " + counterSecond);
            return;
        }
        Timer timer = new Timer();
        TimerTask tt = new TimerTask() {
            public void run() {
                System.out.println("Start Timer Task. t = "  + counterSecond);
                try {
                    while (counterSecond > 0){
                        if (isPause){
                            System.out.println("CookingTimer paused.");
                            isPause = false;
                            timer.cancel();
                            return;
                        }
                        System.out.println(counterSecond);
                        Thread.sleep(1000);
                        counterSecond--;
                    }
                    System.out.println("Timer timeout");
                    KitchenMicrowave.getInstance().handleEvent(KitchenMicrowave.Event.countdown_end);
                    timer.cancel();

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        timer.schedule(tt, 0, 1000);
    }

    public void pause() {
        System.out.println("CookingTimer pause method called");
        isPause = true;
    }
}