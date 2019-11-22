package modele;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.junit.Test;

import controleur.DataCentral;
import controleur.DateTimeTools;
import modele.Actors.Employee;
import modele.Actors.Person;
import modele.Administrative.Department;
import modele.TimeRelated.CheckInOut;
import modele.TimeRelated.WorkingDay;

public class modeleTest {
	
	@Test
	public void testWorkingDay() {
		//On est obligé de créer un employé pour que workingDay puisse calculer ses jours de travail
		Employee employee1 = new Employee();
		employee1.setStartHour(LocalTime.now().withHour(8).withMinute(0).truncatedTo(ChronoUnit.MINUTES));
		employee1.setEndHour(LocalTime.now().withHour(18).withMinute(0).truncatedTo(ChronoUnit.MINUTES));
		DataCentral.addEmployee(employee1);
		
		//On est également obligé de créer des paramètres pour que WorkingDay puisse trouver l'incidentThreshold
		Parameters parameters = new Parameters();
		parameters.setIncidentThreshold(30);
		DataCentral.setParameters(parameters);
		
		//Arrivé et parti à l'heure => On s'attend à une duration de 0 :
		CheckInOut checkInOut1 = new CheckInOut(LocalDateTime.now().withHour(8).withMinute(0).truncatedTo(ChronoUnit.MINUTES), employee1.getIdentifiant());
		CheckInOut checkInOut2 = new CheckInOut(LocalDateTime.now().withHour(18).withMinute(0).truncatedTo(ChronoUnit.MINUTES), employee1.getIdentifiant());
		WorkingDay workingDay1 = new WorkingDay(employee1.getIdentifiant(), checkInOut1, checkInOut2);
		assertEquals(workingDay1.getIdEmployee(), employee1.getIdentifiant());
		assertEquals(workingDay1.getDuration(), Duration.ZERO);
		
		//1h d'avance à l'arrivee et parti 3h trop tot => on s'attend à une duration de -2 :
		CheckInOut checkInOut3 = new CheckInOut(LocalDateTime.now().withHour(7).withMinute(0).truncatedTo(ChronoUnit.MINUTES), employee1.getIdentifiant());
		CheckInOut checkInOut4 = new CheckInOut(LocalDateTime.now().withHour(15).withMinute(0).truncatedTo(ChronoUnit.MINUTES), employee1.getIdentifiant());
		WorkingDay workingDay2 = new WorkingDay(employee1.getIdentifiant(), checkInOut3, checkInOut4);
		assertEquals(workingDay2.getDuration(), Duration.ofHours(-2));
		
		//On vérifie que le timeBalance de l'employé est bien mis à jour :
		assertEquals(employee1.getTimeBalance(), Duration.ofHours(-2));
		
		DataCentral.removeWorkingDayInEmployee(employee1, workingDay2);
		assertEquals(employee1.getTimeBalance(), Duration.ofHours(0));
		
		//Test de la surcharge de equals de la classe WorkingDay
		assertTrue(workingDay1.equals(workingDay1));
	}
	
	@Test
	public void testRoundHour() {
		LocalDateTime heureArrondie = DateTimeTools.roundHour(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0));
		assertEquals(heureArrondie, DateTimeTools.roundHour(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0)));
		
		heureArrondie = DateTimeTools.roundHour(LocalDateTime.now().withHour(23).withMinute(52).withSecond(29));
		assertEquals(heureArrondie, DateTimeTools.roundHour(LocalDateTime.now().withHour(23).withMinute(45).withSecond(0)));
		
		heureArrondie = DateTimeTools.roundHour(LocalDateTime.now().withHour(23).withMinute(52).withSecond(30));
		assertEquals(heureArrondie, DateTimeTools.roundHour(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0)));
		
		heureArrondie = DateTimeTools.roundHour(LocalDateTime.now().withHour(12).withMinute(0).withSecond(0));
		assertEquals(heureArrondie, DateTimeTools.roundHour(LocalDateTime.now().withHour(12).withMinute(0).withSecond(0)));
		
		heureArrondie = DateTimeTools.roundHour(LocalDateTime.now().withHour(12).withMinute(7).withSecond(29));
		assertEquals(heureArrondie, DateTimeTools.roundHour(LocalDateTime.now().withHour(12).withMinute(0).withSecond(0)));
		
		heureArrondie = DateTimeTools.roundHour(LocalDateTime.now().withHour(12).withMinute(7).withSecond(30));
		assertEquals(heureArrondie, DateTimeTools.roundHour(LocalDateTime.now().withHour(12).withMinute(15).withSecond(0)));
		
		heureArrondie = DateTimeTools.roundHour(LocalDateTime.now().withHour(12).withMinute(30).withSecond(30));
		assertEquals(heureArrondie, DateTimeTools.roundHour(LocalDateTime.now().withHour(12).withMinute(30).withSecond(0)));
	}
	
	@Test
	public void testPerson() {
		Person person = new Person();
		person.setMail("test mail");
		person.setName("test name");
		person.setSurname("test surname");
		assertEquals("test mail", person.getMail());
		assertEquals("test name", person.getName());
		assertEquals("test surname", person.getSurname());
	}
	
	@Test
	public void testEmployee() {
		Employee employee1 = new Employee();
		employee1.setEndHour(LocalTime.now().withHour(1).truncatedTo(ChronoUnit.MINUTES));
		employee1.setIdDepartment(5);
		employee1.setName("test1 name");
		employee1.setSurname("test1 surname");
		employee1.setStartHour(LocalTime.now().withHour(2).truncatedTo(ChronoUnit.MINUTES));
		employee1.addToTimeBalance(Duration.ofHours(10));
		assertEquals("test1 name test1 surname", employee1.toString());
		assertEquals(employee1.getStartHour(), LocalTime.now().withHour(2).truncatedTo(ChronoUnit.MINUTES));
		assertEquals(employee1.getEndHour(), LocalTime.now().withHour(1).truncatedTo(ChronoUnit.MINUTES));
		assertEquals(employee1.getIdDepartment(), 5);
		assertEquals(employee1.getTimeBalance(), Duration.ofHours(10));
		
		Employee employee2 = new Employee("test name", "test surname");
		assertEquals(employee2.getName(), "test name");
		assertEquals(employee2.getSurname(), "test surname");
		
		Employee employee3 = new Employee(employee1);
		assertEquals(employee3.getEndHour(), employee1.getEndHour());
		assertEquals(employee3.getIdDepartment(), employee1.getIdDepartment());
		assertEquals(employee3.getName(), employee1.getName());
		assertEquals(employee3.getStartHour(), employee1.getStartHour());
		assertEquals(employee3.getSurname(), employee1.getSurname());
		assertEquals(employee3.getTimeBalance(), employee1.getTimeBalance());
		
		//Test de la surcharge de equals de la classe Department
		assertTrue(employee1.equals(employee1));
	}
	
	@Test
	public void testManager() {
		//Rien à tester, il utilise uniquement des méthodes de Employee.
	}
	
	@Test
	public void testDepartment() {
		Department department1 = new Department("name department1");
		department1.setManager(5);
		assertEquals(department1.getName(), "name department1");
		assertEquals(department1.getManager(), 5);
		
		Department department2 = new Department(department1);
		assertEquals(department2.getName(), "name department1");
		assertEquals(department2.getManager(), 5);
		
		//Test de la surcharge de equals de la classe Department
		assertTrue(department1.equals(department1));
	}
	
	@Test
	public void testCheckInOut() {
		CheckInOut checkInOut1 = new CheckInOut();
		assertEquals(checkInOut1.getIdEmployee(), -1);
		checkInOut1.setRoundedDateTime(LocalDateTime.now().withHour(1).withMinute(2).truncatedTo(ChronoUnit.MINUTES));
		assertEquals(checkInOut1.getRoundedDateTime(), LocalDateTime.now().withHour(1).withMinute(2).truncatedTo(ChronoUnit.MINUTES));
		
		CheckInOut checkInOut2 = new CheckInOut(LocalDateTime.now().withHour(3).withMinute(4).truncatedTo(ChronoUnit.MINUTES), 1);
		assertEquals(checkInOut2.getIdEmployee(), 1);
		assertEquals(checkInOut2.getRoundedDateTime(), DateTimeTools.roundHour(LocalDateTime.now().withHour(3).withMinute(4).truncatedTo(ChronoUnit.MINUTES)));
		
		//Test de la surcharge de equals de la classe CheckInOut
		assertTrue(checkInOut2.equals(checkInOut2));
	}
}