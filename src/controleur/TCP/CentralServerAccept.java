package controleur.TCP;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;

import controleur.DataCentral;
import modele.TimeRelated.CheckInOut;
import vue.CentralInterface;

/**
 * Classe executable de la Central qui permet la synchronisation du TimeTracker vers la Central.
 *
 */
public class CentralServerAccept implements Runnable{
	
	private Socket s;
	private ObjectInputStream in = null;
	
	/**
     * Constructeur de la classe CentralServerAccept qui récupère le socket qu'on lui passe en parametre.
     */
    CentralServerAccept(Socket s) {
    	this.s = s;
    }
    
    /**
     * Methode run de la classe CentralServerAccept qui récupère la liste des CheckInOut envoyés par le TimeTracker.
     * Elle les envoie ensuite à la classe DataCentral.
     */
    public void run() {
        try {
			in = new ObjectInputStream(s.getInputStream());    // get the input stream of client.

			@SuppressWarnings("unchecked")
			List<CheckInOut> listePointage = (List<CheckInOut>) in.readObject();
			
			if(listePointage == null) {
				in.close();
	            s.close();
	            
				new Thread(new CentralClient()).start();
				
			}
			else {

				for(CheckInOut elem : listePointage) {
					DataCentral.addCheckInOut(elem);
				}
				
				//Actualiser les tables :
				CentralInterface.refreshTables();
				
				// close resources
				in.close();
	            s.close();
			}
        }
        catch(IOException e){ 
        	//Exception normale si le fichier est vide.
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
    }
}