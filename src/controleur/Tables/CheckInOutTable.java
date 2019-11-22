package controleur.Tables;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import controleur.DataCentral;
import modele.Actors.Employee;
import modele.Actors.Manager;
import modele.TimeRelated.WorkingDay;

/**
 * Classe qui étend la classe AbstractTableModel et permet la création d'un modèle de table représentant des pointages.
 *
 */
public class CheckInOutTable extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	//Attributs :
	
	//Indices des colonnes
	private static final int COLUMN_EMPLOYE = 0;
    private static final int COLUMN_START = 1;
    private static final int COLUMN_END = 2;
    private static final int COLUMN_DURATION = 3;
    
    private String[] columnNames = { "Employé", "Start", "End", "Duration" };
	private List<WorkingDay> workingDays;
	
	public void setWorkingDays(List<WorkingDay> workDays) {
		workingDays = workDays;
	}

	public CheckInOutTable(List<WorkingDay> workDays) {
		if(workDays == null){
			workingDays = new ArrayList<WorkingDay>();
		}
		else{
			workingDays = workDays;
		}
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		if(workingDays == null){
			return 0;
		}
		return workingDays.size();
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
		if(workingDays==null || workingDays.size()==0){
			return null;
		}
        WorkingDay workingDay = workingDays.get(rowIndex);
        Object returnValue = null;
        switch (columnIndex) {
	        case COLUMN_EMPLOYE :
	        	for(Employee e : DataCentral.getEmployees()){
					if(e.getIdentifiant() == workingDay.getIdEmployee()){
						returnValue = e;
					}
				}
	        	for(Manager m : DataCentral.getManagers()){
					if(m.getIdentifiant() == workingDay.getIdEmployee()){
						returnValue = m;
					}
				}
	            break;
	        case COLUMN_START :
	            returnValue = workingDay.getStart();
	            break;
	        case COLUMN_END :
	            returnValue = workingDay.getEnd();
	            break;
	        case COLUMN_DURATION :
	            returnValue = Employee.formatDuration(workingDay.getDuration());
	            break;
	        default:
	            throw new IllegalArgumentException("Invalid column index");
        }
        return returnValue;
    }
}