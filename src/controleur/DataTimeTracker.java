package controleur;

import modele.Actors.Employee;

import java.util.ArrayList;
import java.util.List;

import modele.Parameters;
import modele.TimeRelated.CheckInOut;

/**
 * Classe agissant de "m�moire vive" de la pointeuse.
 * Toutes les autres classes vont acc�der aux listes de DataTimeTracker qui poss�de les derni�res listes � jour.
 * Cette classe comporte �galement les fonctions n�cessaires � la gestion de ces listes.
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
	 * Ajoute tous les employ�s d'une liste d'employ�s si il ne sont pas d�j� pr�sents dans DataTimeTracker.
	 * @param employeeList : Une liste d'employ�s.
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
	 * Ajoute un employ� dans la liste si il n'est pas d�j� pr�sent.
	 * Initialise la liste des employ�s si il le faut.
	 * @param emp : Un employ�.
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
    * Cette m�thode est notamment appel�e lorsque l'on a r�ussi � envoyer les pointages � la centrale.
    */
   public static void removeWorkingDays() {
	   for(Employee emp : employees) {
		   emp.removeWorkingDays();
	   }
   }
   
   
   /*============ CheckInOut ============*/
   
   /**
    * Ajoute tous les CheckInOut � DataTimeTracker si ils ne sont pas d�j� pr�sents.
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
    * Ajoute un nouveau pointage avec l'id de l'employ� et une date null.
    * Initialise la liste des checkInOut si il le faut.
    * @param id : Un id d'employ�.
    */
   public static void addCheckInOut(int id){
	   if(checkInOut == null){
		   checkInOut = new ArrayList<CheckInOut>();
	   }
	   
	   checkInOut.add(new CheckInOut(id));

   }
   
   /**
    * Ajoute un pointage � la liste des pointages de DataTimeTracker.
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
    * Cette m�thode est notamment appel�e lorsque l'on a r�ussi � envoyer les pointages � la centrale.
    */
   public static void removeCheckInOut() {
	   checkInOut = new ArrayList<CheckInOut>();
   }
}