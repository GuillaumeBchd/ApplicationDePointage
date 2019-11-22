package controleur;

import java.io.FileOutputStream;
import java.util.List;

import modele.Parameters;
import modele.Actors.Employee;
import modele.TimeRelated.CheckInOut;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Classe comportant les m�thodes permettant de sauvegarder et load les parametres et donn�es du TimeTracker.
 *
 */
public class ReadWriteTimeTracker {

	/**
     * M�thode de la classe ReadWriteTimeTracker qui vide le fichier comportant les donn�es de notre TimeTracker et reecrit
     * ses donn�es � la place.
     * @param filename Chemin vers le fichier contenant les donn�es du TimeTracker.
     * @return 
     * @throws IOException
     */
    public static int saveTimeTracker(String filename) throws IOException{

        //Vider le contenu
        new FileOutputStream(filename).close();

        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(DataTimeTracker.getEmployees());
        oos.writeObject(DataTimeTracker.getCheckInOut());
        oos.close();
        return 0;
    }
    
    @SuppressWarnings("unchecked")
    /**
     * M�thode de la classe ReadWriteTimeTracker qui lit les donn�es dans le fichier pass�e en parametre.
     * On les envois ensuite � DataTimeTracker.
     * @param filename Chemin vers le fichier contenant les donn�es du TimeTracker.
     * @throws IOException
     */
	public static void loadTimeTrackerFromFile(String nomFichier) throws IOException{
		FileInputStream fis = new FileInputStream(nomFichier);
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(fis);
		} catch (EOFException e)
		{
		   System.out.println("Empty file");
		}
		
		if(ois != null) {
			try {
				DataTimeTracker.addEmployees((List<Employee>) ois.readObject());
				DataTimeTracker.addCheckInOut((List<CheckInOut>) ois.readObject());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			ois.close();
		}
    }
    
    /**
     * M�thode de la classe ReadWriteTimeTracker qui vide le fichier comportant les param�tres de notre TimeTracker et reecrit
     * ses donn�es � la place.
     * @param filename Chemin vers le fichier contenant les param�tres du TimeTracker.
     * @return 
     * @throws IOException
     */
    public static int saveParametersTimeTracker(String filename) throws IOException{

        //Vider le contenu
        new FileOutputStream(filename).close();

        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(DataTimeTracker.getParameters());
        oos.close();
        return 0;
    }
    
    /**
     * M�thode de la classe ReadWriteTimeTracker qui lit les param�tres dans le fichier pass�e en parametre.
     * On les envois ensuite � DataTimeTracker.
     * @param filename Chemin vers le fichier contenant les param�tres du TimeTracker.
     * @throws IOException
     */
    public static void loadParametersTimeTrackerFromFile(String filename) throws IOException{
    	FileInputStream fis = null;
    	ObjectInputStream ois = null;
    	try {
    		fis = new FileInputStream(filename);
    		ois = new ObjectInputStream(fis);
    	}
    	catch(java.io.EOFException eof) {
    		System.out.println("Empty TimeTracker parameters file.");
    		fis.close();
    		return;
    	}
    	catch(java.io.FileNotFoundException fnfe) {
    		System.out.println("TimeTracker parameters file not found.");
    		return;
    	}
		try {
			DataTimeTracker.setParameters((Parameters) ois.readObject());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		ois.close();
    }
}