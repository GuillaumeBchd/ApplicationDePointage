package controleur.Tables;

import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import controleur.DataCentral;
import modele.Event;
import modele.Actors.Employee;
import modele.Actors.Manager;

/**
 * Classe qui étend la classe AbstractTableModel et permet la création d'un modèle de table représentant des évènements.
 *
 */
public class EventTable extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	
	//Attributs :
	
	//Indices des colonnes
	private static final int COLUMN_EMPLOYE = 0;
    private static final int COLUMN_EVENT_TYPE = 1;
    private static final int COLUMN_DATE = 2;
    private static final int COLUMN_DESCRIPTION = 3;
	
	private String[] columnNames = { "Employee", "Event type", "Date", "Description" };
	private List<Event> events;

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public EventTable(List<Event> eventsList) {
		events = eventsList;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		if(events == null){
			return 0;
		}
		return events.size();
	}
	
	@Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }
	
	@Override
    public Class<?> getColumnClass(int columnIndex) {
		Object value=this.getValueAt(0,columnIndex);
		return (value==null?Object.class:value.getClass());
    }
	
	/**
	 * Renvoie la description de la case en fonction de la colonne et de l'objet.
	 * @param rowIndex : Le numéro de la ligne.
	 * @param columnIndex : Le numéro de la colonne.
	 * @return Object : L'objet correspondant à la case.
	 */
	@Override
    public Object getValueAt(int rowIndex, int columnIndex) {
		if(events==null || events.size()==0){
			return null;
		}
        Event event = events.get(rowIndex);
        //finding employee corresponding to event :
        Employee emp = null;
        for(Employee e : DataCentral.getEmployees()){
			if(e.getIdentifiant() == event.getIdEmployee()){
				emp = e;
			}
		}
    	for(Manager m : DataCentral.getManagers()){
			if(m.getIdentifiant() == event.getIdEmployee()){
				emp = m;
			}
		}
        Object returnValue = null;
        switch (columnIndex) {
	        case COLUMN_EMPLOYE :
	        	returnValue = emp;
	            break;
	        case COLUMN_EVENT_TYPE :
	        	returnValue = event;
	            break;
	        case COLUMN_DATE :
	            returnValue = event.getCheckInOut();
	            break;
	        case COLUMN_DESCRIPTION :
	        	//Si c'est un evenement de retard
	        	switch(event.getEventType()) {
	        		case start:
        				int startHoursLate = (int) emp.getStartHour().until(event.getTime(), ChronoUnit.HOURS);
	        			int startMinutesLate = (((int) emp.getStartHour().until(event.getTime(), ChronoUnit.MINUTES))%60);
	        			returnValue = "Started " + startHoursLate + "h and " + startMinutesLate + "min too late";
		        		break;
		        		
		        	case end:
		        		int endHoursLate = (int) event.getTime().until(emp.getEndHour(), ChronoUnit.HOURS);
	        			int endMinutesLate = (((int) event.getTime().until(emp.getEndHour(), ChronoUnit.MINUTES))%60);
	        			returnValue = "Left " + endHoursLate + "h and " + endMinutesLate + "min too early";
		        		break;
		        		
		        	case day:
		        		returnValue = "Whole day missed : " + event.getDate();
		        		break;
		        		
	        		default:
	        			returnValue = "No description found";
	        			break;
	        	}
	            break;
	        default:
	            throw new IllegalArgumentException("Invalid column index");
        }
        return returnValue;
    }
}