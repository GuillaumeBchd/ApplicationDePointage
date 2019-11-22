package modele.TimeRelated;

import java.io.Serializable;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import controleur.DataCentral;
import modele.Event;
import modele.Actors.Employee;
import modele.Event.TypeEvent;

/**
 * Classe sérialisable qui représente une journée de travail. 
 * Cette classe est utilisée pour gérer les CheckInOut dans la Central.
 * Elle est composée de deux CheckInOut, d'une durée et d'un id d'employé.
 *
 */
public class WorkingDay implements Serializable {

    private static final long serialVersionUID = 1L;
    
	//Attributes
    private CheckInOut Start;
    private CheckInOut End;
    private Duration timeBalanceDuration = Duration.ZERO;
	private int idEmployee;

    //Constructors
	/**
	 * Constructeur de confort de la classe WorkingDay prenant en parametre l'id de l'employé et qu'un seul CheckInOut.
	 * Ce constructeur permet de construire des WorkingDay non complétés.
	 * @param idEmp
	 * @param startP
	 */
    public WorkingDay(int idEmp, CheckInOut startP){
    	timeBalanceDuration = Duration.ZERO;
    	
    	Employee employee = DataCentral.searchEmployeeById(idEmp);
    	
    	if(employee != null) {
    		Duration duration = Duration.between(startP.getRoundedDateTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES), employee.getStartHour().truncatedTo(ChronoUnit.MINUTES));
    		if(duration.toMinutes() < -DataCentral.getParameters().getIncidentThreshold() || duration.toMinutes() > 0) {
    			timeBalanceDuration = timeBalanceDuration.plus(duration);
    		}
    		//On check si l'employe pointe apres son heure d'arrivee plus le seuil d'incident
			if(startP.getRoundedDateTime().toLocalTime().isAfter(employee.getStartHour().plusMinutes(DataCentral.getParameters().getIncidentThreshold()))) {
				DataCentral.addEvent(new Event(idEmp, TypeEvent.start, startP.getRoundedDateTime(), startP));
			}
			
    		employee.addToTimeBalance(timeBalanceDuration);
    	}
    	
        idEmployee = idEmp;
        Start = startP;
    }
    
    /**
     * Constructeur de confort de la classe WorkingDay prenant en parametre l'id de l'employé et deux CheckInOut.
	 * Ce constructeur permet de construire des WorkingDay complétés.
     * @param idEmp
     * @param startP
     * @param endP
     */
    public WorkingDay(int idEmp, CheckInOut startP, CheckInOut endP){
    	timeBalanceDuration = Duration.ZERO;
    	
    	Employee employee = DataCentral.searchEmployeeById(idEmp);
    	
    	if(employee != null) {
    		Duration startDuration = Duration.between(startP.getRoundedDateTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES), employee.getStartHour().truncatedTo(ChronoUnit.MINUTES));
    		if(startDuration.toMinutes() < -DataCentral.getParameters().getIncidentThreshold() || startDuration.toMinutes() > 0) {
    			timeBalanceDuration = timeBalanceDuration.plus(startDuration);
    		}
			
    		Duration endDuration = Duration.between(employee.getEndHour().truncatedTo(ChronoUnit.MINUTES), endP.getRoundedDateTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES));
    		if(endDuration.toMinutes() < -DataCentral.getParameters().getIncidentThreshold() || endDuration.toMinutes() > 0) {
    			timeBalanceDuration = timeBalanceDuration.plus(endDuration);
    		}
    		
			if(timeBalanceDuration.toMinutes() < -DataCentral.getParameters().getIncidentThreshold() ) {
				//On check si l'employe pointe apres son heure d'arrivee plus le seuil d'incident
				if(startP.getRoundedDateTime().toLocalTime().isAfter(employee.getStartHour().plusMinutes(DataCentral.getParameters().getIncidentThreshold()))) {
					DataCentral.addEvent(new Event(idEmp, TypeEvent.start, startP.getRoundedDateTime(), startP));
				}
				//On check si l'employe pointe avant son heure de depart moins le seuil d'incident
				if(endP.getRoundedDateTime().toLocalTime().isBefore(employee.getEndHour().minusMinutes(DataCentral.getParameters().getIncidentThreshold()))) {
					DataCentral.addEvent(new Event(idEmp, TypeEvent.end, endP.getRoundedDateTime(), endP));
				}
			}
			
    		employee.addToTimeBalance(timeBalanceDuration);
    	}
    	
        idEmployee = idEmp;
        Start = startP;
        End = endP;
    }
    
    /**
     * Méthode statique de la classe WorkingDay permettant de supprimer un WorkingDay.
     * @param workingDay
     */
    public static void deleteWorkingDay(WorkingDay workingDay) {
    	if(workingDay != null) {
    		
 		   CheckInOut endCIO = workingDay.getEnd();
 		   
 		   
 		   int index=0, indexToRemove=-1;
 		   
 		   if(workingDay.getStart() != null) {
 			//On cheche s'il y a un event associ� au CheckInOut de d�but
 			  CheckInOut startCIO = workingDay.getStart();
 	 		   if(DataCentral.getEvents() != null) {
 	 			   for(Event ev : DataCentral.getEvents()) {
 	 				   if(ev.getCheckInOut().equals(startCIO)) {
 	 					   indexToRemove = index;
 	 				   }
 	 				   index++;
 	 			   }
 	 			   //Et si il y en a un, on le supprime
 	 			   if(indexToRemove != -1) {
 	 				  DataCentral.getEvents().remove(indexToRemove);
 	 			   }
 	 		   }
 		   }
 		   
 		  if(workingDay.getEnd() != null) {
 			//On cheche s'il y a un event associ� au CheckInOut de fin
 	 		   index=0; indexToRemove=-1;

 	 		   if(DataCentral.getEvents() != null) {
 	 			   for(Event ev : DataCentral.getEvents()) {
 	 				   if(ev.getCheckInOut().equals(endCIO)) {
 	 					   indexToRemove = index;
 	 				   }
 	 				   index++;
 	 			   }
 	 			   //Et si il y en a un, on le supprime
 	 			   if(indexToRemove != -1) {
 	 				  DataCentral.getEvents().remove(indexToRemove);
 	 			   }
 	 		   }
 		  }
 		   
 		   //We remove the duration of the workingDay
 		   DataCentral.searchEmployeeById(workingDay.getIdEmployee()).substractToTimeBalance(workingDay.getDuration());
    	}
    }
    
    
    //Methods
    
    /**
     * Accesseur de lecture sur la durée de la classe WorkingDay.
     * @return La durée associé à notre WorkingDay
     */
    public Duration getDuration() {
		return timeBalanceDuration;
	}
    
    /**
     * Accesseur d'écriture sur la durée de la classe WorkingDay.
     * @param duration La durée associé à notre WorkingDay
     */
	public void setDuration(Duration duration) {
		this.timeBalanceDuration = duration;
	}
	
	/**
	 * Accesseur d'écriture sur le CheckInOut de début de notre WorkingDay.
	 * @param start Le CheckInOut de début de notre WorkingDay.
	 */
    public void setStart(CheckInOut start) {
        this.Start = start;
    }
    
    /**
     * Accesseur de lecture sur le CheckInOut de début de notre WorkingDay.
     * @return Le CheckInOut de début de notre WorkingDay.
     */
    public CheckInOut getStart() {
        return Start;
    }

    /**
     * Accesseur d'écriture sur le CheckInOut de fin de notre WorkingDay.
     * @param end CheckInOut de fin de notre WorkingDay.
     */
    public void setEnd(CheckInOut end) {
        this.End = end;
    }
    
    /**
     * Accesseur de lecture sur le CheckInOut de fin de notre WorkingDay.
     * @return Le CheckInOut de fin de notre WorkingDay.
     */
    public CheckInOut getEnd() {
        return End;
    }

    /**
     * Accesseur d'écriture sur l'id de l'employé de notre WorkingDay.
     * @param idEmployee L'id d'employé que l'on assigne à notre WorkingDay
     */
    public void setIdEmployee(int idEmployee) {
        this.idEmployee = idEmployee;
    }
    
    /**
     * Accesseur de lecture sur l'id de l'employé de notre WorkingDay.
     * @return L'id d'employé assigné à notre WorkingDay
     */
    public int getIdEmployee() {
        return idEmployee;
    }
    
    @Override
    /**
     * Surcharge de la méthode equals permettant de comparer deux WorkingDay
     */
    public boolean equals(Object obj) 
    { 
    	if(this == obj) 
            return true; 
          
        if(obj == null || obj.getClass()!= this.getClass()) 
            return false; 
          
        // type casting of the argument.  
        WorkingDay wd = (WorkingDay) obj; 
        
        if(wd.getEnd() != null) {
        	return (wd.idEmployee == this.idEmployee && wd.getStart()==this.getStart() && wd.getEnd()==this.getEnd());
        }
        else {
        	return (wd.idEmployee == this.idEmployee && wd.getStart()==this.getStart());
        }
    }
}