package mains;

import java.io.IOException;

import controleur.DataTimeTracker;
import controleur.ReadWriteTimeTracker;
import controleur.TCP.TimeTrackerLaunchSynchro;
import controleur.TCP.TimeTrackerServer;
import modele.Parameters;
import vue.TimeTrackerInterface;

/**
 * Méthode principale de TimeTracker.
 * Cette méthode lance l'affichage du timeTracker, définit ses options (ports et ip centrale) et lance la synchronisation avec la centrale.
 *
 */
public class TimeTracker {
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws IOException {
		Parameters par = new Parameters();
		par.setIp("localhost");
		par.setPortCtoP(8080);
		par.setPortPtoC(8081);
		DataTimeTracker.setParameters(par);
		ReadWriteTimeTracker.saveParametersTimeTracker(DataTimeTracker.adressSaveParametersTimeTracker);
		
		//Lire la liste de tous les employes et des workingDays du fichier
		ReadWriteTimeTracker.loadParametersTimeTrackerFromFile(DataTimeTracker.adressSaveParametersTimeTracker);
		ReadWriteTimeTracker.loadTimeTrackerFromFile(DataTimeTracker.adressSaveTimeTracker);
		
		TimeTrackerInterface timeTracker1 = new TimeTrackerInterface();
		timeTracker1.UpdateEmployees(DataTimeTracker.getEmployees());
		timeTracker1.start();
		
		new Thread(new TimeTrackerServer()).start();
		new Thread(new TimeTrackerLaunchSynchro()).start();
	}
}