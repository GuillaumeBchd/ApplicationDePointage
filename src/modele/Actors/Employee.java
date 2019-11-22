package modele.Actors;

import java.util.ArrayList;
import java.util.List;

import modele.TimeRelated.WorkingDay;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Class sérialisable représentant un employé héritant de Person.
 * Cette classe possède un identifiant, un timeBalance, une liste de WorkingDay, un id de département et une heure de début et de fin.
 */
public class Employee extends Person implements Serializable {
   
	private static final long serialVersionUID = 1L;
	
	//Attributs
    private static int nbId = 0;
    private int idEmployee;
    private Duration timeBalance;
    private List<WorkingDay> workingDays;
    private int idDepartment;
    private LocalTime startHour = LocalTime.now().withHour(8).withMinute(0).truncatedTo(ChronoUnit.MINUTES);
	private LocalTime endHour = LocalTime.now().withHour(17).withMinute(0).truncatedTo(ChronoUnit.MINUTES);
    
    //Constructeurs
	
	/**
	 * Constructeur par défaut de la classe Employee.
	 * L'employé se voit attribué un id automatiquement. Il n'a aucun département et un TimeBalance à 0.
	 */
    public Employee(){
    	idDepartment = -1;
    	timeBalance = Duration.ZERO;
        workingDays = new ArrayList<WorkingDay>();
        nbId++;
        idEmployee = nbId;
    }
    
    /**
	 * Constructeur de confort de la classe Employee.
	 * L'employé se voit attribué un id automatiquement. Il n'a aucun département et un TimeBalance à 0.
	 * Son nom et son prénom sont récupérés dans paramètres données
	 */
    public Employee(String name, String surname){
    	idDepartment = -1;
    	timeBalance = Duration.ZERO;
        workingDays = new ArrayList<WorkingDay>();
        nbId++;
        idEmployee = nbId;
        setName(name);
        setSurname(surname);
    }
    /**
	 * Constructeur de recopie de la classe Employee.
	 */
    public Employee(Employee emp) {
    	timeBalance = emp.getTimeBalance();
        workingDays = new ArrayList<WorkingDay>(emp.getWorkingDays());
        nbId++;
        idEmployee = nbId;
        idDepartment = emp.getIdDepartment();
        startHour = emp.getStartHour();
        endHour = emp.getEndHour();
        setName(emp.getName());
        setSurname(emp.getSurname());
	}
    
    /**
     * Method to create a mock employee with the same id
     * @param emp
     * @param i
     */
    public Employee(Employee emp, int i) {
    	timeBalance = emp.getTimeBalance();
        workingDays = new ArrayList<WorkingDay>(emp.getWorkingDays());
        idEmployee = emp.getIdentifiant();
        idDepartment = emp.getIdDepartment();
        startHour = emp.getStartHour();
        endHour = emp.getEndHour();
        setName(emp.getName());
        setSurname(emp.getSurname());
	}

    //Méthodes
    
    /**
     * Accesseur en écriture (protected) de la classe Employee sur l'identifiant.
     * Utilisé uniquement par la classe Manager
     * @param newId
     */
    protected void setId(int newId) {
    	idEmployee = newId;
    }
    
    /**
     * Méthode qui enlève tout les workingDays de l'employé
     */
	public void removeWorkingDays() {
    	workingDays = null;
    }
    
	/**
	 * Accesseur en lecture de la classe Employee sur idDepartment
	 * @return L'id du département de l'employé.
	 */
    public int getIdDepartment() {
		return idDepartment;
	}

    /**
     * Fonction pour chercher dans la liste les employés correspondants aux noms et prénoms
     * @param employeesToSearch
     * @param name
     * @param surname
     * @return Liste d'employés correspondant à la recherche
     */
    public static List<Employee> searchEmployees(List<Employee> employeesToSearch, String name, String surname){
 		List<Employee> listToReturn = new ArrayList<Employee>();
 		
 		if(name.equals("Name") || name.equals("")){
 			if(surname.equals("Surname") || surname.equals("")){
 				for(Employee e : employeesToSearch){
 					listToReturn.add(e);
 				}
 			}
 			else{
 				for(Employee e : employeesToSearch){
 					if(e.getSurname().toLowerCase().equals(surname.toLowerCase())){
 						listToReturn.add(e);
 					}
 				}
 			}
 		}
 		else{
 			if(surname.equals("Surname") || surname.equals("")){
 				for(Employee e : employeesToSearch){
 					if(e.getName().toLowerCase().equals(name.toLowerCase())){
 						listToReturn.add(e);
 					}
 				}
 			}
 			else{
 				for(Employee e : employeesToSearch){
 					if(e.getName().toLowerCase().equals(name) && e.getSurname().toLowerCase().equals(surname.toLowerCase())){
 						listToReturn.add(e);
 					}
 				}
 			}
 		}
 		
 		return listToReturn;
 	}
    
    /**
     * Précondition : Il faut que le département existe, sinon risque de génération d'exception
     * @param idDep
     */
	public void setIdDepartment(int idDep) {
		idDepartment = idDep;
	}
    
	/**
	 * Méthode pour ajouter un working day à un employé.
	 * @param day
	 */
    public void addWorkingDay(WorkingDay day){
    	if(workingDays == null){
    		workingDays = new ArrayList<WorkingDay>();
    	}
    	workingDays.add(day);
    }
    
    /**
     * Accesseur en lecture sur idEmployee
     */
    public int getIdentifiant() {
        return idEmployee;
    }
	/**
	 * Accesseur en lecture sur timeBalance
	 * @return Le timeBalance
	 */
    public Duration getTimeBalance() {
        return timeBalance;
    }
    
    /**
     * Méthode pour enlever une durée au timeBalance
     * @param time
     */
    public void substractToTimeBalance(Duration time) {
        timeBalance = timeBalance.minus(time);
    }
    
    /**
     * Méthode pour ajouter une durée au timeBalance
     * @param time
     */
    public void addToTimeBalance(Duration time) {
        timeBalance = timeBalance.plus(time);
    }
    
    /**
     * Accesseur en lecture sur la liste des WorkingDays des employés
     * @return La liste des workingDay complété de l'employé.
     */
    public List<WorkingDay> getWorkingDays(){
    	return workingDays;
    }
    
    /**
     * Accesseur en lecture sur startHour
     * @return heure de début de travail de l'employé
     */
    public LocalTime getStartHour() {
		return startHour;
	}
    
    /**
     * Accesseur en écriture sur startHour
     * @param startHourGiven
     */
	public void setStartHour(LocalTime startHourGiven) {
		this.startHour = startHourGiven;
	}

	/**
     * Accesseur en lecture sur endHour
     * @return heure de fin de travail de l'employé
     */
	public LocalTime getEndHour() {
		return endHour;
	}
	
	/**
     * Accesseur en écriture sur endHour
     * @param endHourGiven
     */
	public void setEndHour(LocalTime endHourGiven) {
		this.endHour = endHourGiven;
	}
    
    @Override
	/**
	 * Surcharge de toString pour afficher dans la JComboBox 
	 * de TimeTracker non pas un objet Employee mais le nom et le prenom de l'employe :
	 */
    public String toString(){
    	return (getName() + " " + getSurname());
    }
    
    @Override
    /**
     * Surcharge de equals pour comparer des Employee
     */
    public boolean equals(Object obj) 
    { 
    if(this == obj) 
            return true; 
          
        if(obj == null || obj.getClass()!= this.getClass()) 
            return false; 
          
        // type casting of the argument.  
        Employee emp = (Employee) obj; 
          
        return (emp.idEmployee == this.idEmployee); 
    }
    
    /**
     * Méthode pour transformer une durée en string de la forme "XhXmin"
     * @param duration
     * @return La durée transformée
     */
    public static String formatDuration(Duration duration) {
    	long minute = duration.getSeconds()/60;
    	long minuteAbs = Math.abs(minute);
    	
    	String absResult = String.format(
    			"%dh%02dmin",
    			minuteAbs / 60,
    			(minuteAbs % 60)
    			);
    	
    	if(minute < 0) {
    		return "-" + absResult;
    	}
    	else {
    		return absResult;
    	}
    }
}