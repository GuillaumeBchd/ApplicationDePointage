package modele.Administrative;

import java.io.Serializable;

/**
 * Class sérialisable représentant une entreprise possédant un nom.
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

    //Méthodes
    /**
     * Accesseur en lecture sur le nom de l'entreprise.
     * @return Le nom de l'entreprise
     */
    public String getName() {
        return name;
    }

    /**
     * Accesseur en écriture sur le nom de l'entreprise.
     * @param name Le nom de l'entreprise
     */
    public void setName(String name) {
        this.name = name;
    }
}