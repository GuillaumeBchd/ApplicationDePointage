package modele.TimeRelated;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import controleur.DateTimeTools;

/**
* Class sérialisable représentant un pointage.
* Cette classe possède un LocalDateTime normalement arrondi au quart d'heure près et un id d'employé.
*/
public class CheckInOut implements Serializable {

  private static final long serialVersionUID = 1L;
 
  	//Attributes
   private LocalDateTime roundedDateTime;
   private int idEmployee;

  
   //Constructors
   /**
    * Constructeur par défaut de la classe CheckInOut.
    * Le LocalDateTime est généré automatiquement au moment de la création de ce CheckInOut.
    */
   public CheckInOut(){
       roundedDateTime = DateTimeTools.roundHour(LocalDateTime.now());
       idEmployee = -1;
   }
  
   /**
    * Constructeur de confort de la classe CheckInOut prenant un identifiant d'employé en paramètre.
    * Le LocalDateTime est généré automatiquement au moment de la création de ce CheckInOut.
    * @param id Id de l'employé du pointage.
    */
   public CheckInOut(int id){
       roundedDateTime = DateTimeTools.roundHour(LocalDateTime.now());
       idEmployee = id;
   }
  
   /**
    * Constructeur de confort de la classe CheckInOut prenant un LocalDateTime en parametre.
    * @param DateTime
    */
   public CheckInOut(LocalDateTime DateTime){
       roundedDateTime = DateTimeTools.roundHour(DateTime);
       idEmployee = -1;
   }
   /**
    * Constructeur de confort de la classe CheckInOut prenant un LocalDateTime et un id d'employé en parametre.
    * @param DateTime
    */
   public CheckInOut(LocalDateTime DateTime, int id){
       roundedDateTime = DateTimeTools.roundHour(DateTime);
       idEmployee = id;
   }

   /**
    * Constructeur de recopie de la classe CheckInOut.
    * @param DateTime
    */
   public CheckInOut(CheckInOut cio) {
      roundedDateTime = cio.getRoundedDateTime();
       idEmployee = cio.getIdEmployee();
  }

   //Methodes
   /**
    * Accesseur de lecture sur le LocalDateTime du CheckInOut.
    * @return
    */
   public LocalDateTime getRoundedDateTime() {
       return roundedDateTime;
   }
  
   /**
    * Accesseur d'écriture sur le LocalDateTime du CheckInOut.
    * @param localDateTime
    */
   public void setRoundedDateTime(LocalDateTime localDateTime) {
      roundedDateTime = localDateTime.truncatedTo(ChronoUnit.MINUTES);
   }

   /**
    * Accesseur d'écriture sur l'id de l'employé du CheckInOut.
    * @param id
    */
   public void setIdEmployee(int id) {
      idEmployee = id;
   }
  
   /**
    * Accesseur de lecture sur l'id de l'employé du CheckInOut.
    * @return
    */
   public int getIdEmployee() {
      return idEmployee;
   }
  
  
 //Surcharges
   @Override
   /**
    * Surcharge de la méthode toString() pour afficher un CheckInOut sous le format "jour/mois/année-XhXmin"
    */
   public String toString(){
      return roundedDateTime.getDayOfMonth()+"/"+roundedDateTime.getMonthValue()+"/"+roundedDateTime.getYear()
      +" - " + roundedDateTime.getHour()+"h"+roundedDateTime.getMinute()+"min";
   }
  
   @Override
   /**
    * Surcharge de la méthode equals pour comparer de CheckInOut
    */
   public boolean equals(Object obj)
   {
      if(this == obj)
           return true;
        
       if(obj == null || obj.getClass()!= this.getClass())
           return false;
        
       // type casting of the argument. 
       CheckInOut cio = (CheckInOut) obj;
      
       if(cio.roundedDateTime != null) {
          return (cio.idEmployee == this.idEmployee && cio.roundedDateTime==this.roundedDateTime);
       }
       else {
          return false;
       }
   } 
}