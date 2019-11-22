package controleur;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import modele.Event;
import modele.Parameters;
import modele.Actors.Employee;
import modele.Actors.Manager;
import modele.Administrative.Company;
import modele.Administrative.Department;
import modele.TimeRelated.WorkingDay;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Classe comportant les m�thodes permettant de sauvegarder et load les parametres et donn�es de la Central.
 *
 */
public class ReadWriteCentral {

    /**
     * M�thode de la classe ReadWriteCentral qui vide le fichier comportant les donn�es de notre Central et reecrit
     * ses donn�es � la place.
     * @param filename Chemin vers le fichier contenant les donn�es de la Central.
     * @return 
     * @throws IOException
     */
    public static int saveDataCentral(String filename) throws IOException{
    	
    	try {
            //Vider le contenu
    		new FileOutputStream(filename).close();
    		
    		FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(DataCentral.getCompany());
            oos.writeObject(DataCentral.getManagers());
            oos.writeObject(DataCentral.getWorkingDays());
            oos.writeObject(DataCentral.getEmployees());
            oos.writeObject(DataCentral.getDepartments());
            oos.writeObject(DataCentral.getEvents());
            oos.close();
    	}
        catch(java.io.FileNotFoundException e) {
        	System.out.println("Central data file not found, read/save function disabled");
        }
        
        return 0;
    }
    
    /**
     * M�thode de la classe ReadWriteCentral qui lit les donn�es dans le fichier pass�e en parametre.
     * On les envois ensuite � DataCentral.
     * @param filename Chemin vers le fichier contenant les donn�es de la Central.
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
	public static void loadDataCentralFromFile(String filename) throws IOException{
    	
    	FileInputStream fis = null;
    	ObjectInputStream ois = null;
    	try {
    		fis = new FileInputStream(filename);
    		ois = new ObjectInputStream(fis);
    	}
    	catch(java.io.EOFException eof) {
    		System.out.println("Empty central data file.");
    		fis.close();
    		return;
    	}
    	catch(java.io.FileNotFoundException fnfe) {
    		System.out.println("Central data file not found.");
    		return;
    	}
    	catch(Exception e) {
			e.printStackTrace();
		}
    	
		try {
			DataCentral.setCompany((Company) ois.readObject());
			DataCentral.setManagers((List<Manager>) ois.readObject());
			DataCentral.setWorkingDays((List<WorkingDay>) ois.readObject());
			DataCentral.setEmployees((List<Employee>) ois.readObject());
			DataCentral.setDepartments((List<Department>) ois.readObject());
			DataCentral.addEvents((List<Event>) ois.readObject());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		ois.close();
    }

    
    /**
     * M�thode de la classe ReadWriteCentral qui vide le fichier comportant les param�tres de notre Central et reecrit
     * ses param�tres � la place.
     * @param filename Chemin vers le fichier contenant les param�tres de la Central.
     * @return
     * @throws IOException
     */
    public static int saveParametersCentral(String filename) throws IOException{

        //Vider le contenu
        new FileOutputStream(filename).close();

        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(DataCentral.getParameters());
        oos.close();
        return 0;
    }
    
    /**
     * M�thode de la classe ReadWriteCentral qui lit les donn�es dans le fichier pass�e en parametre.
     * On les envois ensuite � DataCentral.
     * @param filename Chemin vers le fichier contenant les param�tres de la Central.
     * @throws IOException
     */
    public static void loadParametersCentralFromFile(String filename) throws IOException{

    	FileInputStream fis = null;
    	ObjectInputStream ois = null;
    	try {
    		fis = new FileInputStream(filename);
    		ois = new ObjectInputStream(fis);
    	}
    	catch(java.io.EOFException eof) {
    		System.out.println("Empty central parameters file.");
    		fis.close();
    		return;
    	}
    	catch(java.io.FileNotFoundException fnfe) {
    		System.out.println("Central parameters file not found.");
    		return;
    	}
		try {
			DataCentral.setParameters((Parameters) ois.readObject());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		ois.close();
    }
}