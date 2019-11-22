package controleur.TCP;

import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import controleur.DataTimeTracker;

/**
* Classe executable du TimeTracker qui permet la synchronisation du TimeTracker vers la Central.
*
*/
public class TimeTrackerLaunchSynchro implements Runnable {

 	private static Socket client;
 	private boolean dispo;
 	private ObjectOutputStream out;
    
 	/**
     * Constructeur de la classe TimeTrackerLaunchSynchro qui itialise tous les param�tres pour le bon fonctionnement de la classe.
     */
    public TimeTrackerLaunchSynchro(){
    	client = null;
    	dispo = false;
    	out = null;
    }
    
    /**
     * Methode run de la classe TimeTrackerLaunchSynchro qui va tenter d'effectuer une connexion jusqu'� ce qu'elle r�ussisse.
     * Une fois qu'elle a r�ussi, elle lance TimeTrackerClient pour l'envoie des donn�s vers la Central.
     */
    public void run() {
    	while(dispo == false) {
    		try {
    			client = new Socket(DataTimeTracker.getParameters().getIp(), DataTimeTracker.getParameters().getPortPtoC());
    	           
    			out = new ObjectOutputStream(client.getOutputStream()); // get the output stream of client.
    			
    			out.writeObject(null);
    			
    			out.flush();
    			out.close();
    			
    			client.close();
    			dispo = true;
    		} catch (UnknownHostException e){
	            e.printStackTrace();
	        } catch (ConnectException e) {
	        	try {
	        		Thread.sleep(5000);
	        	} catch (Exception a) {}
	        	/*
	        	System.out.println("Error, change port or check if Centrale is online");
	        	*/
	        } catch (Exception e){
	            e.printStackTrace();
	        }
    	}
    	
    	new Thread(new TimeTrackerClient()).start();
    }
}