package controleur.Tables;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import controleur.DataCentral;
import modele.Actors.Employee;
import modele.Actors.Manager;
import modele.Administrative.Department;

/**
 * Classe qui étend la classe AbstractTableModel et permet la création d'un modèle de table représentant des employés.
 *
 */
public class EmployeesTable extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	//Attributs :
	
	//Indices des colonnes
    private static final int COLUMN_MANAGER = 0;
    private static final int COLUMN_NAME = 1;
    private static final int COLUMN_SURNAME = 2;
    private static final int COLUMN_DEPARTEMENT = 3;
    private static final int COLUMN_MAIL = 4;
    private static final int COLUMN_START_TIME = 5;
    private static final int COLUMN_END_TIME = 6;
    private static final int COLUMN_BALANCE = 7;
    
	private String[] nomColonnes = { "Manager", "Name", "Surname", "Department", "@Mail", "Start time", "End time", "Time balance" };
	private List<Employee> listEmployees;

	public void setListEmployees(List<Employee> listeEmployes) {
		this.listEmployees = listeEmployes;
	}

	public EmployeesTable(List<Employee> employees) {
		listEmployees = employees;
	}

	@Override
	public int getColumnCount() {
		return nomColonnes.length;
	}

	@Override
	public int getRowCount() {
		if(listEmployees == null){
			return 0;
		}
		return listEmployees.size();
	}
	
	@Override
    public String getColumnName(int columnIndex) {
        return nomColonnes[columnIndex];
    }
	
	@Override
    public Class<?> getColumnClass(int columnIndex) {
		Object value=getValueAt(0,columnIndex);
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
		if(listEmployees==null || listEmployees.size()==0){
			return null;
		}
        Employee employee = listEmployees.get(rowIndex);
        Object returnValue = null;
        switch (columnIndex) {
	        case COLUMN_MANAGER :
	        	returnValue = "✗";
	            for(Manager m : DataCentral.getManagers()) {
	            	if(m.getIdentifiant() == employee.getIdentifiant()) {
	            		returnValue = "✔";
	            	}
	            }
	            break;
	        case COLUMN_NAME :
	            returnValue = employee.getName();
	            break;
	        case COLUMN_SURNAME :
	            returnValue = employee.getSurname();
	            break;
	        case COLUMN_DEPARTEMENT :
	        	returnValue = "no department";
	            for(Department d : DataCentral.getDepartments()){
					if(d.getId() == employee.getIdDepartment()){
						returnValue = d.getName();
					}
				}
	            break;
	        case COLUMN_MAIL :
	            returnValue = employee.getMail();
	            break;
	        case COLUMN_START_TIME :
	        	returnValue = employee.getStartHour().getHour() + "h" + employee.getStartHour().getMinute() + "min";
	            break;
	        case COLUMN_END_TIME :
	        	returnValue = employee.getEndHour().getHour() + "h" + employee.getEndHour().getMinute() + "min";
	            break;
	        case COLUMN_BALANCE :
	        	returnValue = Employee.formatDuration(employee.getTimeBalance());
	        	break;
	        default:
	            throw new IllegalArgumentException("Invalid column index");
        }
        return returnValue;
    }
}