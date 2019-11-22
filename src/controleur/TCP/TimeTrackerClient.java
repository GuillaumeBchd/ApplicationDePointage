package controleur.TCP;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import controleur.DataTimeTracker;
import controleur.ReadWriteTimeTracker;

import java.io.*;

/**
 * Classe executable du TimeTracker qui permet la synchronisation du TimeTracker vers la Central.
 *
 */
public class TimeTrackerClient implements Runnable {
    
    private static Socket client; 
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    
    /**
     * Constructeur de la classe TimeTrackerClient qui itialise tous les paramètres pour le bon fonctionnement de la classe.
     */
    public TimeTrackerClient(){
    	client = null;
    	out = null;
    	in = null;
    }
    
    /**
     * Fonction executable de la classe TimeTrackerClient qui envoie une liste de CheckInOut à la Central.
     */
    public void run() {

        try{
            client = new Socket(DataTimeTracker.getParameters().getIp(), DataTimeTracker.getParameters().getPortPtoC());
            
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());

            out.writeObject(DataTimeTracker.getCheckInOut());

            out.flush();
            
            out.close();
            in.close();
            
            client.close();

            DataTimeTracker.removeCheckInOut();
            ReadWriteTimeTracker.saveTimeTracker(DataTimeTracker.adressSaveTimeTracker);
        }
        catch (UnknownHostException e){
        	System.out.println("TimeTracker error, change port or check if Central is online (UnknownHostException)");
        }
        catch (ConnectException e){
        	System.out.println("TimeTracker error, change port or check if Central is online (ConnectException)");
        }
        catch(java.lang.NullPointerException e) {
        	System.out.println("TimeTracker error, please check if ports are specified in settings ! (java.lang.NullPointerException)");
	    	e.printStackTrace();
        }
        catch(java.net.SocketException e) {
        	System.out.println("TimeTracker error, incorrect ports/ip or unreachable destination (java.net.SocketException)");
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