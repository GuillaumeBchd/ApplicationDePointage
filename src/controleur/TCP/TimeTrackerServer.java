package controleur.TCP;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import controleur.DataTimeTracker;

/**
 * Classe executable du TimeTracker qui permet la synchronisation de la Central vers le TimeTracker.
 *
 */
public class TimeTrackerServer implements Runnable{
	
	private ServerSocket ss;
    private Socket s;
	
    /**
     * Methode run de la classe TimeTrackerServer qui va créer un ServerSocket et qui va donc écouter le portCtoP spécifié
     * dans la classe Parameters. A chaque fois qu'une application se connecte à ce port, on lance la classe
     * TimeTrackerServerAccept.
     */
	public void run() {
		
		try {
	        ss = new ServerSocket(DataTimeTracker.getParameters().getPortCtoP());
	        //ss.setSoTimeout(10000);
			
	        while(true) {
                s = ss.accept();
                new Thread(new TimeTrackerServerAccept(s)).start();
            }

		}
		catch(java.net.BindException e) {
			System.out.println("java.net.BindException : An instance of Polypointeuse is already running.");
		}
		catch (IOException a) {
			a.printStackTrace();
		}
		catch(java.lang.NullPointerException e) {
	    	System.out.println("TimeTracker error, no ports or ip specified in settings.");
	    }
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}