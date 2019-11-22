package controleur.TCP;

import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import controleur.DataCentral;



/**
 * Classe executable de la Central qui permet la synchronisation de la Central vers le TimeTracker.
 *
 */
public class CentralLaunchSynchro implements Runnable {

 	private static Socket client;
 	private boolean dispo;
    
    /**
     * Constructeur de la classe CentralLaunchSynchro qui itialise tous les paramètres pour le bon fonctionnement de la classe.
     */
    public CentralLaunchSynchro(){
    	client = null;
    	dispo = false;
    }
    
    
    /**
     * Methode run de la classe CentralLaunchSynchro qui va tenter d'effectuer une connexion jusqu'à ce qu'elle réussisse.
     * Une fois qu'elle a réussi, elle lance CentralClient pour l'envoie des donnés vers le TimeTracker.
     */
    public void run() {
    	while(dispo == false) {
    		try {
    			client = new Socket(DataCentral.getParameters().getIp(), DataCentral.getParameters().getPortCtoP());
    			client.close();
    			dispo = true;
    		} catch (UnknownHostException e){
	            e.printStackTrace();
	        } catch (ConnectException e) {
	        	try {
	        		Thread.sleep(5000);
	        	} catch (Exception a) {
	        		
	        	}
	        	//System.out.println("Error, change port or check if TimeTracker is online");
	        } catch (Exception e){
	            e.printStackTrace();
	        }
    	}
    	
    	new Thread(new CentralClient()).start();
    }
}