package modele.Actors;

import java.io.Serializable;

import modele.Actors.Employee;
import modele.TimeRelated.WorkingDay;


/**
 * Class sérialisable représentant un manager héritant de Employee.
 *  
 */
public class Manager extends Employee implements Serializable {
	
	private static final long serialVersionUID = 1L;
    
	/**
	 * Constructeur de confort de la classe Manager appelant le même constructeur de la classe Employee.
	 * @param name Nom du manager
	 * @param surname Prenom du manager
	 */
	public Manager(String name, String surname) {
		super(name, surname);
	}
	
	/**
	 * Constructeur de confort de la classe Manager permettant de cast un employee en manager
	 */
	public Manager(Employee employee) {
		addToTimeBalance(employee.getTimeBalance());
        for(WorkingDay wd : employee.getWorkingDays()) {
        	addWorkingDay(wd);
        }
        setId(employee.getIdentifiant());
        setIdDepartment(employee.getIdDepartment());
        setStartHour(employee.getStartHour());
        setEndHour(employee.getEndHour());
        setName(employee.getName());
        setSurname(employee.getSurname());
	}

	/**
	 * Constructeur par défaut de la classe Manager appelant le constructeur par défaut de la classe Employee
	 */
	public Manager() {
		super();
	}
}