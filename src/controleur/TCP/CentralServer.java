package controleur.TCP;

import java.io.IOException;
import java.net.Socket;

import controleur.DataCentral;

import java.net.ServerSocket;

/**
 * Classe executable de la Central qui permet la synchronisation du TimeTracker vers la Central.
 *
 */
public class CentralServer implements Runnable{
	
	private ServerSocket ss;
    private Socket s;
	
    /**
     * Methode run de la classe CentralServer qui va créer un ServerSocket et qui va donc écouter le portPtoC spécifié
     * dans la classe Parameters. A chaque fois qu'une application se connecte à ce port, on lance la classe
     * CentralServerAccept.
     */
	public void run() {
		
		try {
	        ss = new ServerSocket(DataCentral.getParameters().getPortPtoC());
	        //ss.setSoTimeout(10000);
			
	        while(true) {
                s = ss.accept();
                new Thread(new CentralServerAccept(s)).start();
            }

		}
		catch(java.net.BindException e) {
			System.out.println("java.net.BindException : An instance of Polycentral is already running.");
		}
		catch(java.lang.NullPointerException e) {
        	System.out.println("Central error, please check if ports are specified in settings");
        }
		catch (IOException a) {
			a.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}