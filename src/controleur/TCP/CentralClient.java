package controleur.TCP;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import controleur.DataCentral;

/**
 * Classe executable de la Central qui permet la synchronisation de la Central vers le TimeTracker.
 *
 */
public class CentralClient implements Runnable {

 	private static Socket client; 
 	private ObjectOutputStream out;
    
 	/**
     * Constructeur de la classe CentralClient qui itialise tous les paramètres pour le bon fonctionnement de la classe.
     */
    public CentralClient(){
    	client = null;
    	out = null;
    }
    
    /**
     * Fonction executable de la classe CentralClient qui envoie une liste d'employée sans WorkingDay au TimeTracker.
     */
    public void run() {

        try{
        	client = new Socket(DataCentral.getParameters().getIp(), DataCentral.getParameters().getPortCtoP());
            out = new ObjectOutputStream(client.getOutputStream()); // get the output stream of client.
            
            //On envoie la liste des employes sans workingDays
            out.writeObject(DataCentral.listeEmployeesToSend()); 
            
            out.flush();
            
            out.close();
            client.close();
        }
        catch (UnknownHostException e){
            e.printStackTrace();
        }
        catch (ConnectException e){
        	System.out.println("Error, change port or check if Pointeuse is online");
        }
        catch (java.lang.NullPointerException e) {
        	
        }
        catch (Exception e){
        	e.printStackTrace();
        }
        
        finally{
            if(client != null){
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    client = null;
                }
            }
        }
    }
}