package vue;

import java.time.LocalDateTime;

/**
 * Cette classe est utilisée par CentralInterface pour mettre à jour l'heure de l'horloge dynamiquement.
 *
 */
public class CentralClock extends Thread {
	
	Thread thread = null;
	
	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
	/**
	 * Méthode principale qui met à jour l'heure de l'horloge pour l'heure actuelle
	 */
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LocalDateTime localDateTime = LocalDateTime.now();
            int h = localDateTime.getHour();
            int m = localDateTime.getMinute();
            int s = localDateTime.getSecond();
            String hd, md, sd;
            if(h<10) { hd = "0" + Integer.toString(h); }
            else { hd =  Integer.toString(h); }
            if(m<10) { md = "0" + Integer.toString(m); }
            else { md =  Integer.toString(m); }
            if(s<10) { sd = "0" + Integer.toString(s); }
            else { sd =  Integer.toString(s); }
            CentralInterface.sideMenuTimeLabel.setText(hd + " : " + md + " : " + sd);
        }
    }
}