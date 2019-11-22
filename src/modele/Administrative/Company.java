package modele.Administrative;

import java.io.Serializable;

/**
 * Class s�rialisable repr�sentant une entreprise poss�dant un nom.
 *
 */
public class Company implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    //Attributs
    private String name;

    //Constructeurs
    /**
     * Constructeur de confort de la classe Company.
     * @param givenName Nom de l'entreprise.
     */
    public Company(String givenName){
        this.name = givenName;
    }

    //M�thodes
    /**
     * Accesseur en lecture sur le nom de l'entreprise.
     * @return Le nom de l'entreprise
     */
    public String getName() {
        return name;
    }

    /**
     * Accesseur en �criture sur le nom de l'entreprise.
     * @param name Le nom de l'entreprise
     */
    public void setName(String name) {
        this.name = name;
    }
}