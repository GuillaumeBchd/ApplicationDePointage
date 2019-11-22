package modele;

import modele.Actors.Employee;
import modele.Actors.Manager;
import modele.Administrative.Department;
import modele.TimeRelated.CheckInOut;

/**
 * Classe sérialisable représente une incohérence.
 * Cette classe possède un type d'incohérence et un Object.
 *
 */
public class Incoherence {
	Object incoherentObject;
	int typeIncoherence;
	
	/* Type incoherence :
	 * Employee/manager :
	 * 	 1=missing name.
	 * 	 2=missing surname.
	 * 	 3=missing department.
	 * 	 4=missing startTime.
	 * 	 5=missing endTime.
	 * 
	 * Manager :
	 * 	 6=Not managing the same department he is in.
	 * 
	 * WorkingDay :
	 * 	 1=No employee corresponding to id.
	 * 	 2=Not stored in corresponding employee.
	 * 	 3=Start CIO employee != End CIO employee
	 * 
	 * Department :
	 * 	 1=No name.
	 * 	 2=No manager.
	 *
	 */
	
	/**
	 * Constructeur de confort de la classe Incoherence.
	 * @param incoherentObject L'objet qui a provoqué cette incohérence.
	 * @param typeIncoherence Le type de cette incohérence.
	 */
	public Incoherence(Object incoherentObject, int typeIncoherence) {
		super();
		this.incoherentObject = incoherentObject;
		this.typeIncoherence = typeIncoherence;
	}
	
	/**
	 * Accesseur en lecture sur l'objet de la classe Incoherence.
	 * @return L'objet qui a provoqué l'incohérence.
	 */
	public Object getIncoherentObject() {
		return incoherentObject;
	}

	/**
	 * Accesseur en écriture sur l'objet de la classe Incoherence.
	 * @param incoherentObject L'objet qui a provoqué l'incohérence.
	 */
	public void setIncoherentObject(Object incoherentObject) {
		this.incoherentObject = incoherentObject;
	}

	/**
	 * Accesseur en lecture sur le type d'incohérence de la classe Incoherence.
	 * @return Le type de l'incohérence.
	 */
	public int getTypeIncoherence() {
		return typeIncoherence;
	}

	/**
	 * Accesseur en écriture sur le type d'incohérence de la classe Incoherence.
	 * @param typeIncoherence Le type de l'incohérence.
	 */
	public void setTypeIncoherence(int typeIncoherence) {
		this.typeIncoherence = typeIncoherence;
	}
	
	@Override
	/**
	 * Surcharge de la méthode toString() permettant d'afficher quel est le type de l'incohérence.
	 * Cette méthode est notamment utilisée dans la classe IncoherencesTable pour afficher la description d'une incohérence.
	 * @return String : L'incohérence sous forme de String.
	 */
	public String toString() {
		String objectType = "";
        if(incoherentObject instanceof Employee) { objectType = "Employee"; }
        if(incoherentObject instanceof Manager) { objectType = "Manager"; }
        if(incoherentObject instanceof CheckInOut) { objectType = "CheckInOut"; }
        if(incoherentObject instanceof Department) { objectType = "Department"; }
		String returnValue = "";
		switch(objectType) {
	    	case "Employee" :
	    		switch(typeIncoherence) {
	        		case 1 :
	        			returnValue = "Employee has no name";
	        			break;
	        		case 2 :
	        			returnValue = "Employee has no surname";
	        			break;
	        		case 3 :
	        			returnValue = "Employee not affected to a department";
	        			break;
	        		case 4 :
	        			returnValue = "Employee has no start time";
	        			break;
	        		case 5 :
	        			returnValue = "Employee has no end time";
	        			break;
	        		default :
	        			returnValue = "Unknown incoherence type";
	    		}
	    		break;
	    	case "Manager" :
	    		switch(typeIncoherence) {
	        		case 1 :
	        			returnValue = "Manager has no name";
	        			break;
	        		case 2 :
	        			returnValue = "Manager has no surname";
	        			break;
	        		case 3 :
	        			returnValue = "Manager not affected to a department";
	        			break;
	        		case 4 :
	        			returnValue = "Manager has no start time";
	        			break;
	        		case 5 :
	        			returnValue = "Manager has no end time";
	        			break;
	        		case 6 :
	        			returnValue = "Manager is not managing the same department he is affected to";
	        			break;
	        		default :
	        			returnValue = "Unknown incoherence type";
	    		}
	    		break;
	    	case "CheckInOut" :
	    		switch(typeIncoherence) {
	        		case 1 :
	        			returnValue = "No employee corresponding to check in/out id";
	        			break;
	        		case 2 :
	        			returnValue = "CheckInOut not stored in corresponding employee.";
	        			break;
	        		case 3 :
	        			returnValue = "Check in employee different from check out employee.";
	        			break;
	        		default :
	        			returnValue = "Unknown incoherence type";
		        		break;
	    		}
	    		break;
	    	case "Department" :
	    		switch(typeIncoherence) {
	        		case 1 :
	        			returnValue = "Department has no name";
	        			break;
	        		case 2 :
	        			returnValue = "Department has no manager";
	        			break;
	        		default :
	        			returnValue = "Unknown incoherence type";
		        		break;
	    		}
	    		break;
			default :
				returnValue = "Unknown object";
				break;
		}
		return returnValue;
	}
}