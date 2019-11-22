package controleur;

import java.util.ArrayList;
import java.util.List;

import modele.Actors.Employee;
import modele.Event;
import modele.Incoherence;
import modele.Parameters;
import modele.Actors.Manager;
import modele.Administrative.Company;
import modele.Administrative.Department;
import modele.TimeRelated.CheckInOut;
import modele.TimeRelated.WorkingDay;

/**
 * Classe agissant de "mémoire vive" de la central.
 * Toutes les autres classes vont accéder aux listes de DataCentral qui possède les dernières listes à jour.
 * Cette classe comporte également les fonctions nécessaires à la gestion de ces listes.
 *
 */
public class DataCentral {
	
	//Attributs
	public static String adressSaveDataCentral = "ressources"+java.io.File.separator+"data_centrale.txt";
	public static String adressSaveParametersCentral = "ressources"+java.io.File.separator+"parameters_centrale.txt";
	
	private static Parameters parameters;
	private static Company company;
	
	private static List<Manager> managers;
	private static List<WorkingDay> workingDays ;
	private static List<Employee> employees;
	private static List<Department> departments;  
	private static List<Event> events;

	/*============ Parameters ============*/
	
	public static void setParameters(Parameters param) {
		parameters = param;
	}
	
	public static Parameters getParameters() {
		return parameters;
	}
	
	
	/*============ Company ============*/
	public static void setCompany(Company com) {
		company = com;
	}
	
	public static Company getCompany() {
		return company;
	}
	
	
	/*============ Department ============*/
	/**
	 * Fonction qui ajoute un une liste de départements à la liste des départements si ils ne sont pas déjà en mémoire.
	 * @param departms : Une liste de département.
	 */
	public static void addDepartments(List<Department> departms) {
	   if(departments == null){
		   departments = departms;
	   }
	   else{
		   for(Department item : departms){
			   if(!departments.contains(item)) {
				   departments.add(item);
			   }
		   }
	   }
    }
	
	/**
	 * Remplace la liste des départements par une nouvelle.
	 * @param departmentsList : Une liste de départements.
	 */
	public static void setDepartments(List<Department> departmentsList) {
    	departments = new ArrayList<Department>();
    	for(Department d : departmentsList) {
    		departments.add(new Department(d));
    	}
    }
	
	/**
	 * Remplace la liste des départements par une nouvelle.
	 * Les départements doivent forcément avoir le même id qu'avant leur sérialisation car c'est grace à celui ci que les employés repèrent leur département.
	 * @param departmentsList : Une liste de départements.
	 */
	public static void setDepartmentsAfterSerialisation(List<Department> departmentsList) {
		departments = new ArrayList<Department>();
    	for(Department d : departmentsList) {
    		Department newDepartment = new Department(d);
    		newDepartment.setId(d.getId());
    		departments.add(newDepartment);
    	}
	}
	
	/**
	 * Ajoute un département à la liste de départements.
	 * Initialise la liste des départements si il le faut.
	 * @param dep : Un département.
	 */
	public static void addDepartment(Department dep){
	   if(departments == null){
		   departments = new ArrayList<Department>();
	   }
	   if(!departments.contains(dep)){
		   departments.add(dep);
	   }
	}
	   
	/**
	 * Supprime un département de la liste et met à -1 le département des employés qui y étaient.
	 * @param dep : Un département.
	 */
	public static void delDepartment(Department dep){
		if(DataCentral.getManagers() != null) {
			for(Manager m : DataCentral.getManagers()) {
				if(m.getIdDepartment() == dep.getId()) {
					m.setIdDepartment(-1);
				}
			}
		}
		if(DataCentral.getEmployees() != null) {
			for(Employee e : DataCentral.getEmployees()) {
				if(e.getIdDepartment() == dep.getId()) {
					e.setIdDepartment(-1);
				}
			}
		}
		
		if(departments.contains(dep)) {
			departments.remove(dep);
		}
	}
	   
    public static List<Department> getDepartments() {
        return departments;
    }
   
	   
	/*============ Managers ============*/
    /**
     * Ajoute un manager si il n'est pas dans la liste.
     * Initialise la liste des managers si il le faut.
     * @param manag : Un manager.
     */
    public static void addManagers(List<Manager> manag) {
 	   if(managers == null){
 		   managers =  new ArrayList<Manager>();
 	   }
 	   
	   for(Manager item : manag){
		   if(!managers.contains(item)) {
			   managers.add(item);
		   }
	   }
    }
 	
    /**
     * Remplace la liste des managers en mémoire par une nouvelle liste.
     * @param managerList
     */
    public static void setManagers(List<Manager> managerList) {
    	managers = new ArrayList<Manager>();
    	if(managerList != null) {
    		for(Manager m : managerList) {
        		managers.add(new Manager(m));
        	}
    	}
    }
    
    /**
     * Ajoute un manager à la liste des managers.
     * Initialise la liste des managers si il le faut.
     * @param manag
     */
 	public static void addManager(Manager manag){
 	   if(managers == null){
 		   managers = new ArrayList<Manager>();
 	   }
 	   if(!managers.contains(manag)){
 		   managers.add(manag);
 	   }
 	}
 	   
 	/**
 	 * Supprime un manager de la liste des managers.
 	 * Supprime également les workingDays en mémoire et les évènements associés au manager.
 	 * @param manag : Un manager.
 	 */
 	public static void delManager(Manager manag){
 		if(manag!=null && managers.contains(manag)) {
 		   
 		   //On supprime les workingDays concernant l'employe supprime
 		   int indexDay = -1;
 		   int index = 0;
 		   if(workingDays != null) {
 			   for(WorkingDay wd : workingDays) {
 				   if(wd.getIdEmployee() == manag.getIdentifiant()) {
 					   indexDay = index;
 				   }
 				   index++;
 			   }
 		   }
 		   
 		   if(indexDay != -1) {
 			   workingDays.remove(indexDay);
 		   }
 		   
 		   //On supprime les events concernant l'employe
 		   if(events != null) {
 			   for(int i=0; i<=events.size(); i++) {
 				   indexDay = -1;
 				   index = 0;
 				   for(Event e : events) {
 					   if(e.getIdEmployee() == manag.getIdentifiant()) {
 						   indexDay = index;
 					   }
 					   index++;
 				   }
 				   if(indexDay != -1) {
 					   events.remove(indexDay);
 				   }
 			   }
 		   }
 		   
 		   managers.remove(manag);
 	   }
 	}
 	   
	public static List<Manager> getManagers() {
		return managers;
	}
	
	
	/*============ Employee ============*/
	
	/**
	 * Ajoute un employé si il n'y est pas déjà à la liste des employés
	 * Initialise la liste des employés si il le faut.
	 * @param emp : Un employé.
	 */
	public static void addEmployees(List<Employee> emp) {
	   if(employees == null){
		   employees = new ArrayList<Employee>();
	   }
	   
	   for(Employee item : emp){
		   if(!employees.contains(item)) {
			   employees.add(item);
		   }
	   }
   }
	
	/**
	 * Remplace la liste des employés par une autre.
	 * @param employeeList : Une liste d'employés.
	 */
	public static void setEmployees(List<Employee> employeeList) {
    	employees = new ArrayList<Employee>();
    	if(employeeList != null) {
    		for(Employee e : employeeList) {
        		employees.add(new Manager(e));
        	}
    	}
    }
	
	/**
	 * Ajoute un employé si il n'y est pas déjà.
	 * Initialise la liste des employés si il le faut.
	 * @param emp
	 */
	public static void addEmployee(Employee emp){
	   if(employees == null){
		   employees = new ArrayList<Employee>();
	   }
	   if(!employees.contains(emp)){
		   employees.add(emp);
	   }
	}
   
	/**
	 * Supprime un employé de la liste des employés.
 	 * Supprime également les workingDays en mémoire et les évènements associés à l'employé.
	 * @param emp
	 */
	public static void delEmployee(Employee emp){
	   if(emp!=null && employees.contains(emp)) {
		   
		   //On supprime les workingDays concernant l'employe supprime
		   int indexDay = -1;
		   int index = 0;
		   if(workingDays != null) {
			   for(WorkingDay wd : workingDays) {
				   if(wd.getIdEmployee() == emp.getIdentifiant()) {
					   indexDay = index;
				   }
				   index++;
			   }
		   }
		   
		   if(indexDay != -1) {
			   workingDays.remove(indexDay);
		   }
		   
		   //On supprime les events concernant l'employe
		   if(events != null) {
			   for(int i=0; i<=events.size(); i++) {
				   indexDay = -1;
				   index = 0;
				   for(Event e : events) {
					   if(e.getIdEmployee() == emp.getIdentifiant()) {
						   indexDay = index;
					   }
					   index++;
				   }
				   if(indexDay != -1) {
					   events.remove(indexDay);
				   }
			   }
		   }
		   
		   employees.remove(emp);
	   }
   }
   
   public static List<Employee> getEmployees() {
       return employees;
   }
   
   /**
    * Renvoie l'employé correspondant à l'id passé en paramètre.
    * @param id : Un id d'employé.
    * @return L'employé correspondant, null si il n'existe pas.
    */
   public static Employee searchEmployeeById(int id) {
	   if(employees != null) {
		   for(Employee emp : employees) {
			   if(emp.getIdentifiant() == id) {
				   return emp;
			   }
		   }
	   }
	   
	   if(managers != null) {
		   for(Manager man : managers) {
			   if(man.getIdentifiant() == id) {
				   return (Employee)man;
			   }
		   }
	   }
	   
	   return null;
   }
   
   /**
    * Crée une nouvelle liste avec tous les employés et les managers sans leurs workingDays qui sera envoyée à la pointeuse.
    * @return une nouvelle liste de tous les employés (employés + managers).
    */
   public static List<Employee> listeEmployeesToSend(){
	   	//On cree la liste des employees en enlevant les working days
   		List<Employee> listeToSend = new ArrayList<Employee>();
   		
   		if(DataCentral.getEmployees() != null) {
   			for(Employee emp : DataCentral.getEmployees()) {
   				Employee e = new Employee(emp, 1); //Method to create mock employee with the same id
   				listeToSend.add(e);
   	   		}
   		}
   		
   		if(DataCentral.getManagers() != null) {
   			for(Manager man : DataCentral.getManagers()) {
   				Employee e = new Employee(((Employee) man), 1); //Method to create mock employee with the same id
   				listeToSend.add(e);
   			}
   		}
   		
   		if(listeToSend != null) {
   			for(Employee emp : listeToSend) {
   				emp.removeWorkingDays();
   			}
   		}
		
		return listeToSend;
   	}
   
	/*============ Working Days ============*/
	/**
	 * Remplace la liste des workingDays par une nouvelle.
	 * @param listWorkingDays : Une liste de jours de travail.
	 */
   public static void setWorkingDays(List<WorkingDay> listWorkingDays) {
	   if(listWorkingDays != null) {
		   if(workingDays == null){
			   workingDays = listWorkingDays;
		   }
		   else{

			   for(WorkingDay item : listWorkingDays){
				   boolean test = false;
				   for(WorkingDay w : workingDays){
					   if(item.getIdEmployee() == w.getIdEmployee()){
						   test=true;
					   }
				   }
				   if(test == false){
					   workingDays.add(item);
				   }
			   }
		   }
	   }
   }
   
   /**
    * Ajoute un pointage dans la structure appropriée.
    * @param cio : Un CheckInOut (pointage).
    */
   public static void addCheckInOut(CheckInOut cio){
	   if(cio != null) {
		   if(workingDays == null){
			   workingDays = new ArrayList<WorkingDay>();
		   }

		   int id = cio.getIdEmployee();
		   Employee emp = searchEmployeeById(id);
		   
		   if(emp != null) {
			   for(WorkingDay wday : workingDays) {
				   if(wday.getIdEmployee() == id) {
					   //On ajoute a l'employe le working day complet
					   emp.addWorkingDay(new WorkingDay(id, wday.getStart(), cio));
					   
					   //Et on supprime l'ancien de data
					   removeWorkingDayInData(emp.getIdentifiant());
		  
					   return;
				   }
			   }
			   
			   //Si on a pas trouve de workingDay entamme, on en cree un
			   workingDays.add(new WorkingDay(id, cio));
		   }
		   else {
			   System.out.println("CheckInOut with an id non existing in central database received was ignored.");
		   }
	   }
   }
   
   public static List<WorkingDay> getWorkingDays() {
       return workingDays;
   }
   
   /**
    * Supprime un workingDay correspondant à un employé de la liste de workingDays non complétés de la centrale
    * Supprime également les évènements associés au workingDay supprimé.
    * @param idEmployee : Un id d'employé.
    */
   public static void removeWorkingDayInData(int idEmployee) {
	   if(workingDays != null){
		   for(WorkingDay wd : workingDays) {
			   if(wd.getIdEmployee() == idEmployee) {
				   
				   //On change le time balance de l'employee
				   Employee emp = searchEmployeeById(idEmployee);
				   
				   //We remove the duration of the workingDay
				   emp.substractToTimeBalance(wd.getDuration());
				   
				   //On cheche s'il y a un event associé à ce CheckInOut
				   if(events != null) {
					   int index=0, indexToRemove=-1;
					   for(Event ev : events) {
						   if(ev.getCheckInOut().equals(wd.getStart())) {
							   indexToRemove = index;
						   }
						   index++;
					   }
					   //Et si il y en a un, on le supprime
					   if(indexToRemove != -1) {
						   events.remove(indexToRemove);
					   }
				   }
				   
				   //Et dans tous les cas on supprime le WorkingDay
				   workingDays.remove(wd);
				   return;
			   }
		   }  
	   }
   }
   
   /**
    * Supprime un workingDay correspondant à un employé de la liste de workingDays complétés d'un employé
    * Supprime également les évènements associés au workingDay supprimé.
    * @param emp : un employé.
    * @param workingDay : Le jour de travail à supprimer.
    */
   public static void removeWorkingDayInEmployee(Employee emp, WorkingDay workingDay) {
	   //Since it is a workingDay in employee, it must have a start and an end
	   if(workingDay.getStart() != null && workingDay.getEnd() != null) {
		   
		   CheckInOut startCIO = workingDay.getStart();
		   CheckInOut endCIO = workingDay.getEnd();
		   
		   //On cheche s'il y a un event associé au CheckInOut de début
		   int index=0, indexToRemove=-1;
		   if(events != null) {
			   for(Event ev : events) {
				   if(ev.getCheckInOut().equals(startCIO)) {
					   indexToRemove = index;
				   }
				   index++;
			   }
			   //Et si il y en a un, on le supprime
			   if(indexToRemove != -1) {
				   events.remove(indexToRemove);
			   }
		   }
		   
		   //On cheche s'il y a un event associé au CheckInOut de fin
		   index=0; indexToRemove=-1;

		   if(events != null) {
			   for(Event ev : events) {
				   if(ev.getCheckInOut().equals(endCIO)) {
					   indexToRemove = index;
				   }
				   index++;
			   }
			   //Et si il y en a un, on le supprime
			   if(indexToRemove != -1) {
				   events.remove(indexToRemove);
			   }
		   }
		   //We remove the duration of the workingDay
		   emp.substractToTimeBalance(workingDay.getDuration());
		   
		   //Et dans tous les cas on supprime le WorkingDay
		   emp.getWorkingDays().remove(workingDay);
	   } 
   }
   
   /**
    * Supprime un workingDay correspondant à un employé de la liste de workingDays complétés d'un employé
    * Supprime également les évènements associés au workingDay supprimé.
    * @param emp : un employé.
    * @param posWd : La position du jour de travail à supprimer.
    */
   public static void removeWorkingDayInEmployee(Employee emp, int posWd) {
	   CheckInOut startCIO = emp.getWorkingDays().get(posWd).getStart();
	   CheckInOut endCIO = emp.getWorkingDays().get(posWd).getEnd();
	   
	   //On cheche s'il y a un event associé au CheckInOut de début
	   int index=0, indexEventToRemove=-1;
	   for(Event ev : events) {
		   if(ev.getCheckInOut().equals(startCIO)) {
			   indexEventToRemove = index;
		   }
		   index++;
	   }
	   //Et si il y en a un, on le supprime
	   if(indexEventToRemove != -1) {
		   events.remove(indexEventToRemove);
	   }
	   
	   //On cheche s'il y a un event associé au CheckInOut de fin
	   index=0; indexEventToRemove=-1;
	   for(Event ev : events) {
		   if(ev.getCheckInOut().equals(endCIO)) {
			   indexEventToRemove = index;
		   }
		   index++;
	   }
	   //Et si il y en a un, on le supprime
	   if(indexEventToRemove != -1) {
		   events.remove(indexEventToRemove);
	   }
	   
		//We remove the duration of the workingDay
		emp.substractToTimeBalance(emp.getWorkingDays().get(posWd).getDuration());
		
		//Et dans tous les cas on supprime le WorkingDay
		emp.getWorkingDays().remove(emp.getWorkingDays().get(posWd));
   }
   
   /**
    * Supprime un workingDay correspondant à un employé de la liste de workingDays non complétés de la centrale
    * Supprime également les évènements associés au workingDay supprimé.
    * @param emp : Un employé.
    */
   public static void removeWorkingDayInData(Employee emp) {
	   WorkingDay workingDay = null;
	   
	   //On cherche le WorkingDay correspondant
	   for(WorkingDay wd : workingDays) {
		   if(wd.getIdEmployee() == emp.getIdentifiant()) {
			   workingDay = wd;
		   }
	   }
	   
	   //On cheche s'il y a un event associé à ce CheckInOut
	   int indexEvent=0, indexEventToRemove=-1;
	   if(events != null) {
		   for(Event ev : events) {
			   if(ev.getCheckInOut().equals(workingDay.getStart())) {
				   indexEventToRemove = indexEvent;
			   }
			   indexEvent++;
		   }
	   }
	   //Et si il y en a un, on le supprime
	   if(indexEventToRemove != -1) {
		   events.remove(indexEventToRemove);
	   }
	   
		//We remove the duration of the workingDay
		emp.substractToTimeBalance(workingDay.getDuration());
		   
		//Et dans tous les cas on supprime le WorkingDay
		workingDays.remove(workingDay);
   }
   
   /*============ Events ============*/
   /**
    * Ajoute tous les éléments d'une liste d'évènements à la liste d'évènements de la centrale.
    * @param listEvents : Une liste d'évènements.
    */
   public static void addEvents(List<Event> listEvents) {
	   if(events == null){
		   events = listEvents;
	   }
	   else{
		   for(Event item : listEvents){
			   if(!events.contains(item)) {
				   events.add(item);
			   }
		   }
	   }
   }
   
   /**
    * Remplace la liste des évènements de la centrale par une autre.
    * @param eventList : Une liste d'évènements.
    */
   public static void setEvents(List<Event> eventList) {
	   events = new ArrayList<Event>();
	   for(Event e : eventList) {
		   events.add(new Event(e));
	   }
	}
	   
   	/**
   	 * Ajoute un évènement à la liste des évènements de la centrale si il n'y est pas déjà. 
   	 * Initialise la liste des évènements si il le faut.
   	 * @param event : Un évènement.
   	 */
	public static void addEvent(Event event){
	   if(events == null){
		   events = new ArrayList<Event>();
	   }
	   if(!events.contains(event)){
		   events.add(event);
	   }
	}
	  
	/**
	 * Supprime un évènement des évènements de la centrale si il existe dedans.
	 * @param event : Un évènement.
	 */
	public static void delEvent(Event event){
	   if(events.contains(event)) {
		   events.remove(event);
	   }
	}
	
	public static List<Event> getEvents() {
		return events;
	}

	/**
	 * Renvoie les départements avec un nom correspondant au String passé en paramètre.
	 * @param departmentName : Un nom de département.
	 * @return Une liste de départments.
	 */
	public static List<Department> searchDepartments(String departmentName) {
		List<Department> listToReturn = new ArrayList<Department>();
		
		for(Department d : departments) {
			if(d.getName().toLowerCase().equals(departmentName.toLowerCase()) || departmentName.equals("Name") || departmentName.equals("")) {
				listToReturn.add(d);
			}
		}
		return listToReturn;
	}
	 /*============ Incoherences ============*/
	/**
	 * Cette méthode détecte toutes les incohérences des listes de DataCentral.
	 * @return Une liste d'incohérences.
	 */
	public static List<Incoherence> getIncoherences(){
		List<Incoherence> incoherencesList = new ArrayList<Incoherence>();
		if(employees != null) {
			for(Employee e : employees) {
				if(e.getName()==null || e.getName().equals("")) {
					incoherencesList.add(new Incoherence(e, 1));
				}
				if(e.getSurname()==null || e.getSurname().equals("")) {
					incoherencesList.add(new Incoherence(e, 2));
				}
				if(e.getIdDepartment()==-1) {
					incoherencesList.add(new Incoherence(e, 3));
				}
				if(e.getStartHour()==null) {
					incoherencesList.add(new Incoherence(e, 4));
				}
				if(e.getEndHour()==null) {
					incoherencesList.add(new Incoherence(e, 5));
				}
				
				for(WorkingDay wd : e.getWorkingDays()) {
					if(wd.getIdEmployee() != e.getIdentifiant()) {
						incoherencesList.add(new Incoherence(wd, 2));
					}
				}
			}
		}
		if(managers !=null) {
			for(Manager m : managers) {
				if(m.getName()==null || m.getName().equals("")) {
					incoherencesList.add(new Incoherence(m, 1));
				}
				if(m.getSurname()==null || m.getSurname().equals("")) {
					incoherencesList.add(new Incoherence(m, 2));
				}
				if(m.getIdDepartment()==-1) {
					incoherencesList.add(new Incoherence(m, 3));
				}
				if(m.getStartHour()==null) {
					incoherencesList.add(new Incoherence(m, 4));
				}
				if(m.getEndHour()==null) {
					incoherencesList.add(new Incoherence(m, 5));
				}
				
				for(WorkingDay wd : m.getWorkingDays()) {
					if(wd.getIdEmployee() != m.getIdentifiant()) {
						incoherencesList.add(new Incoherence(wd, 2));
					}
				}
				
				for(Department d : departments) {
					if((d.getManager() == m.getIdentifiant()) && (d.getId() != m.getIdDepartment())) {
						incoherencesList.add(new Incoherence(m, 6));
					}
				}
			}
		}
		if(workingDays != null) {
			for(WorkingDay wd : workingDays) {
				boolean found = false;
				for(Employee e : employees) {
					if(e.getIdentifiant() == wd.getStart().getIdEmployee()) {
						found = true;
					}
				}
				for(Manager m : managers) {
					if(m.getIdentifiant() == wd.getStart().getIdEmployee()) {
						found = true;
					}
				}
				if(found == false) {
					incoherencesList.add(new Incoherence(wd, 1));
				}
				
				if(wd.getEnd() != null) {
					if(wd.getStart().getIdEmployee() != wd.getEnd().getIdEmployee()) {
						incoherencesList.add(new Incoherence(wd, 3));
					}
				}
			}
		}
		if(departments != null) {
			for(Department d : departments) {
				if(d.getName() == null || d.getName().equals("")) {
					incoherencesList.add(new Incoherence(d, 1));
				}
				if(d.getManager() == -1) {
					incoherencesList.add(new Incoherence(d, 2));
				}
			}
		}
		
		return incoherencesList;
	}
}