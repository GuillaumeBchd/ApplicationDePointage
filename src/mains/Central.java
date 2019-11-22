package mains;

import java.awt.EventQueue;
import java.io.IOException;

import controleur.DataCentral;
import controleur.ReadWriteCentral;
import controleur.TCP.CentralLaunchSynchro;
import controleur.TCP.CentralServer;
import vue.CentralInterface;

import java.util.ArrayList;
import java.util.List;

import modele.Parameters;
import modele.Actors.Employee;
import modele.Actors.Manager;
import modele.Administrative.Company;
import modele.Administrative.Department;
import modele.TimeRelated.WorkingDay;

/**
 * Méthode principale de Centrale.
 * Cette méthode peut permettre de créer un fichier centrale de test.
 * Elle initialise également la central en lisant les donées dans les fichiers de sauvegarde et essaye de se synchroniser avec le timeTracker.
 *
 */
public class Central {

	/**
	 * Méthode main appelée au lancement.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//Creation d'un fichier centrale de test :
		
		
		ReadWriteCentral.loadDataCentralFromFile(DataCentral.adressSaveDataCentral);
		ReadWriteCentral.loadParametersCentralFromFile(DataCentral.adressSaveParametersCentral);

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CentralInterface window = new CentralInterface();
					window.mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		new Thread(new CentralServer()).start();
		new Thread(new CentralLaunchSynchro()).start();
	}
	
	public static void mockCompany() {
		Parameters parameters = new Parameters();
		parameters.setIncidentThreshold(30);
		parameters.setIp("localhost");
		parameters.setPortCtoP(8080);
		parameters.setPortPtoC(8081);
		DataCentral.setParameters(parameters);
		
		Company company = new Company("Polycorp");
		List<Employee> employees = new ArrayList<Employee>();
		Employee paul = new Employee("Paulo", "Le Tonneau"); employees.add(paul);
		Employee flo = new Employee("Floflo", "Fait du velo"); employees.add(flo);
		List<Manager> managers = new ArrayList<Manager>();
		Manager Colin = new Manager("Colin", "Le meilleur"); managers.add(Colin); 
		List<WorkingDay> workingDays = new ArrayList<WorkingDay>();
		List<Department> departments = new ArrayList<Department>();
		Department DI = new Department("DI");
		Department DMS = new Department("DMS");
		Department DEE = new Department("DEE");
		DI.setManager(Colin.getIdentifiant());
		departments.add(DI);
		departments.add(DMS);
		departments.add(DEE);
		paul.setIdDepartment(DEE.getId());
		
		DataCentral.setCompany(company);
		DataCentral.setManagers(managers);
		DataCentral.setWorkingDays(workingDays);
		DataCentral.setEmployees(employees);
		DataCentral.setDepartments(departments);
		try {
			ReadWriteCentral.saveDataCentral(DataCentral.adressSaveDataCentral);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}