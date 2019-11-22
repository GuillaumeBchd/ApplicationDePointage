package controleur;

import modele.Actors.Employee;

import java.util.ArrayList;
import java.util.List;

import modele.Parameters;
import modele.TimeRelated.CheckInOut;

/**
 * Classe agissant de "mémoire vive" de la pointeuse.
 * Toutes les autres classes vont accéder aux listes de DataTimeTracker qui possède les dernières listes à jour.
 * Cette classe comporte également les fonctions nécessaires à la gestion de ces listes.
 *
 */
public class DataTimeTracker {
	
	public static String adressSaveTimeTracker = "ressources"+java.io.File.separator+"data_timetracker.txt";
	public static String adressSaveParametersTimeTracker = "ressources"+java.io.File.separator+"parameters_timetracker.txt";
	
	private static Parameters parameters;

	private static List<CheckInOut> checkInOut ;
	private static List<Employee> employees;
   
	
	/*============ Parameters ============*/
	
	public static void setParameters(Parameters par) {
		parameters = par;
	}
	
	public static Parameters getParameters() {
		return parameters;
	}
	
	/*============ Employee ============*/
	public static void setEmployees(List<Employee> emp) {
		employees = emp;
	}
   
	/**
	 * Ajoute tous les employés d'une liste d'employés si il ne sont pas déjà présents dans DataTimeTracker.
	 * @param employeeList : Une liste d'employés.
	 */
	public static void addEmployees(List<Employee> employeeList) {
		if(employees == null){
			employees = employeeList;
		}
		else{
			for(Employee item : employeeList){
			   boolean test = false;
			   for(Employee e : employees){
				   if(item.getIdentifiant() == e.getIdentifiant()){
					   test=true;
				   }
			   }
			   if(test == false){
				   employees.add(item);
			   }
		   }
	   }
	}
   
	/**
	 * Ajoute un employé dans la liste si il n'est pas déjà présent.
	 * Initialise la liste des employés si il le faut.
	 * @param emp : Un employé.
	 */
	public static void addEmployee(Employee emp){
		if(employees == null){
			employees = new ArrayList<Employee>();
		}
		if(!employees.contains(emp)){
			employees.add(emp);
	   }
   }
   
   public static void delEmployee(Employee emp){
	   if(employees.contains(emp)){
		   employees.remove(emp);
	   }
   }
   
   public static List<Employee> getEmployees() {
       return employees;
   }

   
   /*============ Working Days ============*/
   
   /**
    * Supprime tous les jours de travail (workingDays) de DataTimeTracker.
    * Cette méthode est notamment appelée lorsque l'on a réussi à envoyer les pointages à la centrale.
    */
   public static void removeWorkingDays() {
	   for(Employee emp : employees) {
		   emp.removeWorkingDays();
	   }
   }
   
   
   /*============ CheckInOut ============*/
   
   /**
    * Ajoute tous les CheckInOut à DataTimeTracker si ils ne sont pas déjà présents.
    * @param checkInOuts : Une liste de checkInOut.
    */
   public static void addCheckInOut(List<CheckInOut> checkInOuts) {
	   if(checkInOut == null){
		   checkInOut = checkInOuts;
	   }
	   else{
		   for(CheckInOut item : checkInOuts){
			   if(!checkInOut.contains(item)) {
				   checkInOut.add(item);
			   }
		   }
	   }
   }
   
   /**
    * Ajoute un nouveau pointage avec l'id de l'employé et une date null.
    * Initialise la liste des checkInOut si il le faut.
    * @param id : Un id d'employé.
    */
   public static void addCheckInOut(int id){
	   if(checkInOut == null){
		   checkInOut = new ArrayList<CheckInOut>();
	   }
	   
	   checkInOut.add(new CheckInOut(id));

   }
   
   /**
    * Ajoute un pointage à la liste des pointages de DataTimeTracker.
    * Initialise la liste des pointages si il le faut.
    * @param checkIO : Un pointage (checkInOut).
    */
   public static void addCheckInOut(CheckInOut checkIO){
	   if(checkInOut == null){
		   checkInOut = new ArrayList<CheckInOut>();
	   }
	   
	   checkInOut.add(checkIO);

   }
   

   public static List<CheckInOut> getCheckInOut() {
       return checkInOut;
   }
   
   /**
    * Vide la liste des pointages de DataTimeTracker.
    * Cette méthode est notamment appelée lorsque l'on a réussi à envoyer les pointages à la centrale.
    */
   public static void removeCheckInOut() {
	   checkInOut = new ArrayList<CheckInOut>();
   }
}