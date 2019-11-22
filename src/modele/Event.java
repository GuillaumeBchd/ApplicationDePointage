package modele;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import modele.TimeRelated.CheckInOut;

/**
 * Classe sérialisable représentant des évenemments.
 * Cette classe possède un type d'évenement, un id d'employé, un LocalTime, un LocalDate et un CheckInOut.
 *
 */
public class Event implements Serializable {
	
	public static enum TypeEvent { start, end, day };
	
	private static final long serialVersionUID = 1L;
	
	private TypeEvent typeEvent;
	private int idEmployee;
	private LocalTime time;
	private LocalDate date;
	private CheckInOut checkInOut;
	//Le checkInOut correspond au pointage qui a généré l'event.
	//Cela permet notament de supprimer/modifier l'event lorsque l'on supprime/modifie le checkInOut.

	@Override
	/**
	 * Surcharge de la méthode toString permettant d'afficher un évènement selon son type.
	 * Cette méthode est notamment utilisée dans la classe eventsTable pour afficher la description d'un event.
	 * @return String : L'event sous forme de String.
	 */
    public String toString(){
		String returnValue = "Unknown event";
		switch(typeEvent) {
			case start:
				returnValue = "Late start";
	    		break;
	    		
	    	case end:
				returnValue = "Early finish";
	    		break;
	    		
	    	case day:
	    		returnValue = "Day missed";
	    		break;
	    		
			default:
				returnValue = "Unknown event";
				break;
		}
    	return returnValue;
    }
	
	//Constructors 
	/**
	 * Constructeur de recopie de la classe Event.
	 * @param e L'évènement que l'on souhaite recopier.
	 */
	public Event(Event e) {
		typeEvent = e.typeEvent;
		idEmployee = e.idEmployee;
		time = e.time;
		date = e.date;
		checkInOut = e.checkInOut;
	}
	
	/**
	 * Constructeur de confort de la classe Event prenant en paramètre un id d'employé.
	 * @param id L'id d'employé associé à cet évènement.
	 */
	public Event(int id){
		typeEvent = null;
		idEmployee = id;
		time = null;
		date = null;
		checkInOut = null;
	}
	
	//Evènement de retard ou de depart trop tot
	/**
	 * Constructeur de confort de la classe Event.
	 * Ce constructeur permet de créer un évènement de retard ou de départ trop tôt.
	 * @param idEmp Id d'employé
	 * @param typeEv Type de l'évènement
	 * @param t LocalTime de l'évènement
	 * @param d LocalDate de l'évènement
	 * @param cio CheckInOut qui a provoqué cet évènement
	 */
	public Event(int idEmp, TypeEvent typeEv, LocalTime t, LocalDate d, CheckInOut cio){
		typeEvent = typeEv;
		idEmployee = idEmp;
		time = t;
		date = d;
		checkInOut = cio;
	}
	
	/**
	 * Constructeur de confort de la classe Event.
	 * Ce constructeur permet de créer un évènement de retard ou de départ trop tôt.
	 * @param idEmp Id d'employé
	 * @param typeEv Type de l'évènement
	 * @param dateTime LocalDateTime de l'évènement
	 * @param cio CheckInOut qui a provoqué cet évènement
	 */
	public Event(int idEmp, TypeEvent typeEv, LocalDateTime dateTime, CheckInOut cio){
		typeEvent = typeEv;
		idEmployee = idEmp;
		time = dateTime.toLocalTime();
		date = dateTime.toLocalDate();
		checkInOut = cio;
	}
	
	/**
	 * Constructeur de confort de la classe Event.
	 * Ce constructeur permet de créer un évènement d'absence.
	 * @param idEmp Id d'employé
	 * @param typeEv Type de l'évènement
	 * @param d LocalDate de l'évènement
	 * @param cio CheckInOut qui a provoqué cet évènement
	 */
	public Event(int idEmp, TypeEvent typeEv, LocalDate d, CheckInOut cio){
		typeEvent = typeEv;
		idEmployee = idEmp;
		date = d;
		checkInOut = cio;
	}
	
	
	//Methodes
	/**
	 * Accesseur en lecture sur le type de l'évènement
	 * @return Le type de l'évènement
	 */
	public TypeEvent getEventType() { return typeEvent; }
	
	/**
	 * Accesseur en lecture sur l'id de l'employé de l'évènement
	 * @return Id de l'employé associé à l'évènement.
	 */
	public int getIdEmployee() { return idEmployee; }
	
	/**
	 * Accesseur en lecture sur le LocalTime de l'évènement
	 * @return Le LocalTime de l'évènement
	 */
	public LocalTime getTime() { return time; }
	
	/**
	 * Accesseur en lecture sur le LocalDate de l'évènement
	 * @return Le LocalDate de l'évènement
	 */
	public LocalDate getDate() { return date; }
	
	/**
	 * Accesseur en écriture sur le LocalTime de l'évènement
	 * @param t LocalTime que l'on assigne à l'évènement
	 */
	public void setHour(LocalTime t) { time = t; }
	
	/**
	 * Accesseur en écriture sur le LocalDate de l'évènement
	 * @param d LocalTime que l'on assigne à l'évènement
	 */
	public void setDate(LocalDate d) { date = d; }
	
	/**
	 * Accesseur en lecture sur le CheckInOut associé à l'évènement
	 * @return Le CheckInOut associé à l'évènement
	 */
	public CheckInOut getCheckInOut() { return checkInOut; }

	/**
	 * Accesseur en écriture sur le CheckInOut associé à l'évènement
	 * @param cio Le CheckInOut associé à l'évènement
	 */
	public void setCheckInOut(CheckInOut cio) { checkInOut = cio; }
}