package modele;

import java.io.Serializable;

/**
 * Classe sérialisable représentant des paramètres.
 * Cette classe possède une ip, deux ports, et un seuil d'accident.
 *
 */
public class Parameters implements Serializable {

    private static final long serialVersionUID = 1L;
    
	//Attributes
    private String ip;		//Check à faire un tableau d'ip pour plusieurs pointeuse
    private int portPtoC;
    private int portCtoP;

    private int incidentThreshold;

    //Methods
    /**
     * Accesseur en lecture sur l'ip de la classe Parameters.
     * @return L'ip stocké dans les paramètres.
     */
    public String getIp() {
        return ip;
    }
    
    /**
     * Accesseur en lecture sur le port du TimeTracker vers la Central de la classe Parameters.
     * @return Le port du TimeTracker vers la Central.
     */
    public int getPortPtoC() {
        return portPtoC;
    }
    
    /**
     * Accesseur en lecture sur le port de la Central vers le TimeTracker de la classe Parameters.
     * @return Le port de la Central vers le TimeTracker.
     */
    public int getPortCtoP() {
        return portCtoP;
    }
    
    /**
     * Accesseur en lecture sur le seuil d'incident de la classe Parameters.
     * @return Le seuil d'incident stocké dans les paramètres.
     */
    public int getIncidentThreshold() {
        return incidentThreshold;
    }

    /**
     * Accesseur en écriture sur l'ip de la classe Parameters.
     * @param ipGiven L'ip a stocké dans les paramètres.
     */
    public void setIp(String ipGiven) {
    	ip = ipGiven;
    }
    
    /**
     * Accesseur en écriture sur le port du TimeTracker vers la Central de la classe Parameters.
     * @param portGiven Le port du TimeTracker vers la Central.
     */
    public void setPortPtoC(int portGiven) {
        portPtoC = portGiven;
    }
    
    /**
     * Accesseur en écriture sur le port de la Central vers le TimeTracker de la classe Parameters.
     * @param portGiven Le port de la Central vers le TimeTracker.
     */
    public void setPortCtoP(int portGiven) {
        portCtoP = portGiven;
    }
    
    /**
     * Accesseur en écriture sur le seuil d'incident de la classe Parameters.
     * @param incidentThresholdGiven Le seuil d'incident a stocké dans les paramètres.
     */
    public void setIncidentThreshold(int incidentThresholdGiven) {
        incidentThreshold = incidentThresholdGiven;
    }
}