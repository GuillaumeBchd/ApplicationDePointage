package controleur;

import java.time.LocalDateTime;

/**
 * Classe comportant les fonctions relatives aux calculs sur les dates et les heures.
 *
 */
public class DateTimeTools {

	/**
	 * Fonction qui arrondi au quart d'heure le plus proche (sans les secondes, nanosecondes, ...).
	 * @param date : Un localDateTime.
	 * @return : LocalDateTime passée en paramètre et arrondie.
	 */
    public static LocalDateTime roundHour(LocalDateTime date){

        LocalDateTime roundedDateTime = date;

        int dateH = roundedDateTime.getHour();
        int dateM = roundedDateTime.getMinute();
        int dateS = roundedDateTime.getSecond();

        int roundedHours = dateH;
        int roundedMinutes = dateM;
        int hourQuarters = 0;

        while(roundedMinutes-15 > 0){
            roundedMinutes -= 15;
            hourQuarters++;
        }
        if(roundedMinutes * 60 + dateS < 450){
            roundedMinutes = 0;
        }
        else{
            roundedMinutes = 0;
            hourQuarters++;
        }

        if(hourQuarters == 4){
            hourQuarters = 0;
            roundedHours++;
        }
        roundedMinutes = hourQuarters * 15;

        if(roundedHours == 24){
            roundedHours = 0;
        }

        roundedDateTime = date.withMinute(roundedMinutes).withHour(roundedHours).withSecond(0).withNano(0);

        return roundedDateTime;
    }
}