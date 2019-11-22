package controleur.Tables;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import modele.Incoherence;
import modele.Actors.Employee;
import modele.Actors.Manager;
import modele.Administrative.Department;
import modele.TimeRelated.CheckInOut;

/**
 * Classe qui étend la classe AbstractTableModel et permet la création d'un modèle de table représentant des incohérences.
 *
 */
public class IncoherencesTable extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	//Attributs :
	
	//Indices des colonnes
	private static final int COLUMN_OBJECT = 0;
	private static final int COLUMN_DESCRIPTION = 1;
    
    private String[] columnsName = { "Object" , "Description" };
	private List<Incoherence> incoherencesList;
	
	public IncoherencesTable(List<Incoherence> incoherencesList) {
		this.incoherencesList = incoherencesList;
	}
	
	public List<Incoherence> getIncoherencesList() {
		return incoherencesList;
	}

	public void setIncoherencesList(List<Incoherence> incoherencesList) {
		this.incoherencesList = incoherencesList;
	}
	
	public Incoherence getIncoherence(int index) {
		return incoherencesList.get(index);
	}
	
	@Override
	public int getColumnCount() {
		return columnsName.length;
	}

	@Override
	public int getRowCount() {
		if(incoherencesList == null){
			return 0;
		}
		return incoherencesList.size();
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
		if(incoherencesList == null || incoherencesList.size()==0){
			return null;
		}
		
        Incoherence incoherence = incoherencesList.get(rowIndex);
        String objectType = "";
        if(incoherence.getIncoherentObject() instanceof Employee) { objectType = "Employee"; }
        if(incoherence.getIncoherentObject() instanceof Manager) { objectType = "Manager"; }
        if(incoherence.getIncoherentObject() instanceof CheckInOut) { objectType = "CheckInOut"; }
        if(incoherence.getIncoherentObject() instanceof Department) { objectType = "Department"; }
        Object returnValue = null;
        switch (columnIndex) {
	        case COLUMN_OBJECT :
	        	switch(objectType) {
		        	case "Employee" :
		        		returnValue = ((Employee)incoherence.getIncoherentObject()).getName() + " " + ((Employee)incoherence.getIncoherentObject()).getSurname();
		        		break;
		        	case "Manager":
		        		returnValue = ((Employee)incoherence.getIncoherentObject()).getName() + " " + ((Employee)incoherence.getIncoherentObject()).getSurname();
		        		break;
		        	case "CheckInOut":
		        		returnValue = "CheckInOut " + ((CheckInOut)incoherence.getIncoherentObject()).getRoundedDateTime().toString();
		        		break;
		        	case "Department":
		        		returnValue = "Department  " + ((Department)incoherence.getIncoherentObject()).getName();
		        		break;
	        		default:
	        			break;
	        			
	        	}
	            break;
	        case COLUMN_DESCRIPTION :
	        	returnValue = incoherence; //Utilise la surcharge de toString de la classe Incoherence
	            break;
	        default:
	            throw new IllegalArgumentException("Invalid column index");
        }
        return returnValue;
    }
}
