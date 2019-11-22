package controleur.Tables;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import controleur.DataCentral;
import modele.Actors.Manager;
import modele.Administrative.Department;

/**
 * Classe qui étend la classe AbstractTableModel et permet la création d'un modèle de table représentant des départements.
 *
 */
public class DepartmentsTable extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	
	//Attributs :
	
	//Indices des colonnes
	private static final int COLUMN_NAME = 0;
	private static final int COLUMN_MANAGER = 1;
    
    private String[] columnsName = { "Name" , "Manager" };
	private List<Department> departments;

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	public DepartmentsTable(List<Department> dptmts) {
		departments = dptmts;
	}

	@Override
	public int getColumnCount() {
		return columnsName.length;
	}

	@Override
	public int getRowCount() {
		if(departments == null){
			return 0;
		}
		return departments.size();
	}
	
	@Override
    public String getColumnName(int columnIndex) {
        return columnsName[columnIndex];
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
		if(departments == null || departments.size()==0){
			return null;
		}
		
        Department department = departments.get(rowIndex);
        Object returnValue = null;
        switch (columnIndex) {
	        case COLUMN_NAME :
	        	returnValue = department.getName();
	            break;
	        case COLUMN_MANAGER :
	        	if(department.getManager() == -1) {
	        		returnValue = "No manager";
	        	}
	        	else {
	        		for(Manager m : DataCentral.getManagers()){
						if(m.getIdentifiant() == department.getManager()){
							returnValue = m.getName() + " " + m.getSurname();
						}
					}
	        	}
	            break;
	        default:
	            throw new IllegalArgumentException("Invalid column index");
        }
        return returnValue;
    }
}