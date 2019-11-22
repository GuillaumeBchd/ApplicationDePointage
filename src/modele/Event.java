package modele;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import modele.TimeRelated.CheckInOut;

/**
 * Classe s�rialisable repr�sentant des �venemments.
 * Cette classe poss�de un type d'�venement, un id d'employ�, un LocalTime, un LocalDate et un CheckInOut.
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
	//Le checkInOut correspond au pointage qui a g�n�r� l'event.
	//Cela permet notament de supprimer/modifier l'event lorsque l'on supprime/modifie le checkInOut.

	@Override
	/**
	 * Surcharge de la m�thode toString permettant d'afficher un �v�nement selon son type.
	 * Cette m�thode est notamment utilis�e dans la classe eventsTable pour afficher la description d'un event.
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
	 * @param e L'�v�nement que l'on souhaite recopier.
	 */
	public Event(Event e) {
		typeEvent = e.typeEvent;
		idEmployee = e.idEmployee;
		time = e.time;
		date = e.date;
		checkInOut = e.checkInOut;
	}
	
	/**
	 * Constructeur de confort de la classe Event prenant en param�tre un id d'employ�.
	 * @param id L'id d'employ� associ� � cet �v�nement.
	 */
	public Event(int id){
		typeEvent = null;
		idEmployee = id;
		time = null;
		date = null;
		checkInOut = null;
	}
	
	//Ev�nement de retard ou de depart trop tot
	/**
	 * Constructeur de confort de la classe Event.
	 * Ce constructeur permet de cr�er un �v�nement de retard ou de d�part trop t�t.
	 * @param idEmp Id d'employ�
	 * @param typeEv Type de l'�v�nement
	 * @param t LocalTime de l'�v�nement
	 * @param d LocalDate de l'�v�nement
	 * @param cio CheckInOut qui a provoqu� cet �v�nement
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
	 * Ce constructeur permet de cr�er un �v�nement de retard ou de d�part trop t�t.
	 * @param idEmp Id d'employ�
	 * @param typeEv Type de l'�v�nement
	 * @param dateTime LocalDateTime de l'�v�nement
	 * @param cio CheckInOut qui a provoqu� cet �v�nement
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
	 * Ce constructeur permet de cr�er un �v�nement d'absence.
	 * @param idEmp Id d'employ�
	 * @param typeEv Type de l'�v�nement
	 * @param d LocalDate de l'�v�nement
	 * @param cio CheckInOut qui a provoqu� cet �v�nement
	 */
	public Event(int idEmp, TypeEvent typeEv, LocalDate d, CheckInOut cio){
		typeEvent = typeEv;
		idEmployee = idEmp;
		date = d;
		checkInOut = cio;
	}
	
	
	//Methodes
	/**
	 * Accesseur en lecture sur le type de l'�v�nement
	 * @return Le type de l'�v�nement
	 */
	public TypeEvent getEventType() { return typeEvent; }
	
	/**
	 * Accesseur en lecture sur l'id de l'employ� de l'�v�nement
	 * @return Id de l'employ� associ� � l'�v�nement.
	 */
	public int getIdEmployee() { return idEmployee; }
	
	/**
	 * Accesseur en lecture sur le LocalTime de l'�v�nement
	 * @return Le LocalTime de l'�v�nement
	 */
	public LocalTime getTime() { return time; }
	
	/**
	 * Accesseur en lecture sur le LocalDate de l'�v�nement
	 * @return Le LocalDate de l'�v�nement
	 */
	public LocalDate getDate() { return date; }
	
	/**
	 * Accesseur en �criture sur le LocalTime de l'�v�nement
	 * @param t LocalTime que l'on assigne � l'�v�nement
	 */
	public void setHour(LocalTime t) { time = t; }
	
	/**
	 * Accesseur en �criture sur le LocalDate de l'�v�nement
	 * @param d LocalTime que l'on assigne � l'�v�nement
	 */
	public void setDate(LocalDate d) { date = d; }
	
	/**
	 * Accesseur en lecture sur le CheckInOut associ� � l'�v�nement
	 * @return Le CheckInOut associ� � l'�v�nement
	 */
	public CheckInOut getCheckInOut() { return checkInOut; }

	/**
	 * Accesseur en �criture sur le CheckInOut associ� � l'�v�nement
	 * @param cio Le CheckInOut associ� � l'�v�nement
	 */
	public void setCheckInOut(CheckInOut cio) { checkInOut = cio; }
}