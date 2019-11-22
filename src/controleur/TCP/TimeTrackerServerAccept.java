package controleur.TCP;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;

import controleur.DataTimeTracker;
import modele.Actors.Employee;
import vue.TimeTrackerInterface;

/**
 * Classe executable du TimeTracker qui permet la synchronisation de la Central vers le TimeTracker.
 *
 */
public class TimeTrackerServerAccept implements Runnable{
	private Socket s;
	private ObjectInputStream in;
	
	/**
     * Constructeur de la classe TimeTrackerServerAccept qui récupère le socket qu'on lui passe en parametre.
     */
	TimeTrackerServerAccept(Socket s) {
    	this.s = s;
    	in = null;
    }

    @SuppressWarnings("unchecked")
	/**
     * Methode run de la classe TimeTrackerServerAccept qui récupère la liste des Employee envoyés par la Centrals.
     * Elle les envoie ensuite à la classe DataTimeTracker.
     */
	public void run() {
        try {
        	
			in = new ObjectInputStream(s.getInputStream());    // get the input stream of client.

			DataTimeTracker.setEmployees((List<Employee>) in.readObject());
			
			TimeTrackerInterface.UpdateEmployees(DataTimeTracker.getEmployees());
			
			// close resources
			in.close();
            s.close();
        }
        catch(IOException e){ }
        catch(Exception e) {
        	e.printStackTrace();
        }
    }
}