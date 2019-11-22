package controleur;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import modele.Actors.Employee;
import modele.Actors.Manager;
import modele.TimeRelated.WorkingDay;

public class ListFilter {
	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @return Liste de tous les pointages ayant été faits entre ces dates et heure
	 */
	public static List<WorkingDay> checkInOutFilter(LocalDateTime startDate, LocalDateTime endDate){
		
		List<WorkingDay> listToReturn = new ArrayList<WorkingDay>();
		
		if(DataCentral.getWorkingDays() != null) {
			List<WorkingDay> workingDaysCopy = new ArrayList<WorkingDay>(DataCentral.getWorkingDays());
			for(WorkingDay wd : workingDaysCopy) {
				//Pas besoin de comparer la fin ici puisqu'il n'y en pas dans cette liste
				if(	(wd.getStart().getRoundedDateTime().isAfter(startDate) || wd.getStart().getRoundedDateTime().isEqual(startDate)))
				{
					listToReturn.add(wd);
				}
			}
		}

		if(DataCentral.getEmployees() != null) {
			List<Employee> employeeCopy = new ArrayList<Employee>(DataCentral.getEmployees());
			for(Employee e : employeeCopy) {
				for(WorkingDay wd : e.getWorkingDays()) {
					if(wd != null) {
						if(	(wd.getStart().getRoundedDateTime().isAfter(startDate) || wd.getStart().getRoundedDateTime().isEqual(startDate))
								&& (wd.getEnd().getRoundedDateTime().isBefore(endDate) || wd.getEnd().getRoundedDateTime().isEqual(endDate)) )
							{
								listToReturn.add(wd);
							}
					}
				}
			}
		}

		if(DataCentral.getManagers() != null) {
			List<Manager> managerCopy= new ArrayList<Manager>(DataCentral.getManagers());
			for(Manager m : managerCopy) {
				for(WorkingDay wd : m.getWorkingDays()) {
					if(wd != null) {
						if(	(wd.getStart().getRoundedDateTime().isAfter(startDate) || wd.getStart().getRoundedDateTime().isEqual(startDate))
								&& (wd.getEnd().getRoundedDateTime().isBefore(endDate) || wd.getEnd().getRoundedDateTime().isEqual(endDate)) )
							{
								listToReturn.add(wd);
							}
					}
				}
			}
		}
		
		return listToReturn;
	}
	
	/**
	 * 
	 * @param date
	 * @return Liste de tous les pointages ayant une date de début ou de fin correspondant à la date passée en paramètre
	 */
	public static List<WorkingDay> checkInOutFilter(LocalDate date){
		List<WorkingDay> listToReturn = new ArrayList<WorkingDay>();

		if(DataCentral.getWorkingDays() != null) {
			List<WorkingDay> workingDaysCopy = new ArrayList<WorkingDay>(DataCentral.getWorkingDays());
			for(WorkingDay wd : workingDaysCopy) {
				//Pas besoin de comparer la date de fin puisqu'il n'y en pas dans cette liste
				if(	(wd.getStart().getRoundedDateTime().getYear() == date.getYear()
						&& wd.getStart().getRoundedDateTime().getMonthValue() == date.getMonthValue()
						&& wd.getStart().getRoundedDateTime().getDayOfMonth() == date.getDayOfMonth())
						)
					{
						listToReturn.add(wd);
					}
			}
		}

		if(DataCentral.getEmployees() != null) {
			List<Employee> employeeCopy = new ArrayList<Employee>(DataCentral.getEmployees());
			for(Employee e : employeeCopy){
				if(e.getWorkingDays() != null){
					for(WorkingDay wd : e.getWorkingDays()){
						if(	(wd.getStart().getRoundedDateTime().getYear() == date.getYear()
							&& wd.getStart().getRoundedDateTime().getMonthValue() == date.getMonthValue()
							&& wd.getStart().getRoundedDateTime().getDayOfMonth() == date.getDayOfMonth())
							||
							(wd.getEnd().getRoundedDateTime().getYear() == date.getYear()
							&& wd.getEnd().getRoundedDateTime().getMonthValue() == date.getMonthValue()
							&& wd.getEnd().getRoundedDateTime().getDayOfMonth() == date.getDayOfMonth()))
						{
							listToReturn.add(wd);
						}
					}
				}
			}
		}
		
		if(DataCentral.getManagers() != null) {
			List<Manager> managerCopy= new ArrayList<Manager>(DataCentral.getManagers());
			for(Manager m : managerCopy){
				if(m.getWorkingDays() != null){
					for(WorkingDay wd : m.getWorkingDays()){
						if(	(wd.getStart().getRoundedDateTime().getYear() == date.getYear()
							&& wd.getStart().getRoundedDateTime().getMonthValue() == date.getMonthValue()
							&& wd.getStart().getRoundedDateTime().getDayOfMonth() == date.getDayOfMonth())
							||
							(wd.getEnd().getRoundedDateTime().getYear() == date.getYear()
							&& wd.getEnd().getRoundedDateTime().getMonthValue() == date.getMonthValue()
							&& wd.getEnd().getRoundedDateTime().getDayOfMonth() == date.getDayOfMonth()))
						{
							listToReturn.add(wd);
						}
					}
				}
			}
		}
		
		return listToReturn;
	}
	
	/**
	 * 
	 * @param idDepartment
	 * @return Liste de tous les pointages du département
	 */
	public static List<WorkingDay> checkInOutDepartmentFilter(int idDepartment){
		List<Employee> employeeCopy = null;
		if(DataCentral.getEmployees() != null) {
			employeeCopy = new ArrayList<Employee>(DataCentral.getEmployees());
		}
		List<Manager> managerCopy = null;
		if(DataCentral.getManagers() != null) {
			managerCopy = new ArrayList<Manager>(DataCentral.getManagers());
		}
		
		List<WorkingDay> listToReturn = new ArrayList<WorkingDay>();

		if(DataCentral.getWorkingDays() != null) {
			List<WorkingDay> workingDaysCopy = new ArrayList<WorkingDay>(DataCentral.getWorkingDays());
			for(WorkingDay wd : workingDaysCopy) {
				if(employeeCopy != null) {
					for(Employee e : employeeCopy) {
						if(e.getIdDepartment() == idDepartment && wd.getIdEmployee() == e.getIdentifiant()) {
							listToReturn.add(wd);
						}
					}
				}
				if(managerCopy != null) {
					for(Manager m : managerCopy) {
						if(m.getIdDepartment() == idDepartment && wd.getIdEmployee() == m.getIdentifiant()) {
							listToReturn.add(wd);
						}
					}
				}
			}
		}
			
		if(employeeCopy != null) {
			for(Employee e : employeeCopy){
				if(e.getIdDepartment() == idDepartment) {
					if(e.getWorkingDays() != null){
						for(WorkingDay wd : e.getWorkingDays()){
							listToReturn.add(wd);
						}
					}
				}
			}
		}
		
		if(managerCopy != null) {
			for(Manager m : managerCopy) {
				if(m.getIdDepartment() == idDepartment) {
					if(m.getWorkingDays() != null) {
						for(WorkingDay wd : m.getWorkingDays()) {
							listToReturn.add(wd);
						}
					}
				}
			}
		}
		
		return listToReturn;
	}
	
	/**
	 * 
	 * @param idPerson
	 * @return Liste de tous les pointages de cette personne (employé ou manager)
	 */
	public static List<WorkingDay> checkInOutPersonFilter(int idPerson){
		List<WorkingDay> listToReturn = new ArrayList<WorkingDay>();

		if(DataCentral.getWorkingDays() != null) {
			List<WorkingDay> workingDaysCopy = new ArrayList<WorkingDay>(DataCentral.getWorkingDays());
			for(WorkingDay wd : workingDaysCopy) {
				if(wd.getIdEmployee() == idPerson) {
					listToReturn.add(wd);
				}
			}
		}
		
		if(DataCentral.getEmployees() != null) {
			List<Employee> employeeCopy = new ArrayList<Employee>(DataCentral.getEmployees());
			for(Employee e : employeeCopy){
				if(e.getIdentifiant() == idPerson) {
					if(e.getWorkingDays() != null){
						for(WorkingDay wd : e.getWorkingDays()){
							listToReturn.add(wd);
						}
					}
				}
			}
		}
		
		if(DataCentral.getManagers() != null) {
			List<Manager> managerCopy= new ArrayList<Manager>(DataCentral.getManagers());
			for(Manager m : managerCopy){
				if(m.getIdentifiant() == idPerson) {
					if(m.getWorkingDays() != null){
						for(WorkingDay wd : m.getWorkingDays()){
							listToReturn.add(wd);
						}
					}
				}
			}
		}
		
		return listToReturn;
	}
	
	/**
	 * 
	 * @return Liste de tous les pointages
	 */
	public static List<WorkingDay> checkInOutNoFilter(){
		List<WorkingDay>listToReturn = new ArrayList<WorkingDay>();

		if(DataCentral.getWorkingDays() != null) {
			for(WorkingDay wd : DataCentral.getWorkingDays()) {
				listToReturn.add(wd);
			}
		}
		
		if(DataCentral.getEmployees() != null) {
			List<Employee> employeeCopy = new ArrayList<Employee>(DataCentral.getEmployees());
			for(Employee e : employeeCopy){
				if(e.getWorkingDays() != null){
					for(WorkingDay wd : e.getWorkingDays()){
						listToReturn.add(wd);
					}
				}
			}
		}

		if(DataCentral.getManagers() != null) {
			List<Manager> managerCopy= new ArrayList<Manager>(DataCentral.getManagers());
			for(Manager m : managerCopy){
				if(m.getWorkingDays() != null){
					for(WorkingDay wd : m.getWorkingDays()){
						listToReturn.add(wd);
					}
				}
			}
		}
		
		return listToReturn;
	}

	public static List<WorkingDay> checkInOutCurrentDay() {
		List<WorkingDay>listToReturn = new ArrayList<WorkingDay>();

		if(DataCentral.getWorkingDays() != null) {
			for(WorkingDay wd :DataCentral.getWorkingDays()) {
				if(wd.getEnd() == null) {
					listToReturn.add(wd);
				}
			}
		}
		
		if(DataCentral.getEmployees() != null) {
			List<Employee> employeeCopy = new ArrayList<Employee>(DataCentral.getEmployees());
			for(Employee e : employeeCopy){
				if(e.getWorkingDays() != null){
					for(WorkingDay wd : e.getWorkingDays()){
						if(wd.getEnd() == null) {
							listToReturn.add(wd);
						}
					}
				}
			}
		}

		if(DataCentral.getManagers() != null) {
			List<Manager> managerCopy= new ArrayList<Manager>(DataCentral.getManagers());
			for(Manager m : managerCopy){
				if(m.getWorkingDays() != null){
					for(WorkingDay wd : m.getWorkingDays()){
						if(wd.getEnd() == null) {
							listToReturn.add(wd);
						}
					}
				}
			}
		}
		
		return listToReturn;
	}
}