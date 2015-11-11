package jmqt;

import jm.music.data.*;
import jmqt.QTUtil;

public class SimpleQTCycle {
    private Thread myThread;
    private QTUtil qtu;  
    private boolean looping = false;  
    private Score score;
  
    public SimpleQTCycle(Score s) {
        this.score = s;
        qtu = new QTUtil();
        loop();
    }     
    
    public void start() {
        looping = true;
    }

    public void stop() {
        looping = false;
    }

    private void loop() {
        myThread = new Thread(new Runnable( ) {
            public void run() {
                while (true) {
                    if (looping) {
                        qtu.playback(score);
                        try {
                            Thread.sleep((int)(1000 * score.getEndTime() * 60.0/score.getTempo()));
                        } catch (Exception e) {};
                    }
																	   else {
                        try {
                            Thread.sleep(100);
                        } catch (Exception e) {};
                     }
                 }
             }
         });
         myThread.start();
     }
     
     public QTUtil getQTUtil() {
         return qtu;
     }

     public void setScore(Score s) {
        this.score = s;
     }
}