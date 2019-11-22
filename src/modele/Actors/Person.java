package modele.Actors;

import java.io.Serializable;

/**
 * Class s�rialisable repr�sentant une personne poss�dant un nom, pr�nom et un mail.
 *
 */
public class Person implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    //Attributs
    private String name;
    private String surname;
    private String mail;

    //M�thodes
    
    /**
     * Accesseur en lecture de la class Person sur name.
     * @return Le nom de la personne
     */
    public String getName(){
        return name;
    }
    /**
     * Accesseur en �criture de la class Person sur name.
     * @param name Nom de la personne
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Accesseur en lecture de la class Person sur surname.
     * @return Le pr�nom de la personne
     */
    public String getSurname(){
        return surname;
    }
    /**
     * Accesseur en �criture de la class Person sur surname.
     * @param surname Prenom de la personne
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Accesseur en lecture de la class Person sur mail.
     * @return Le mail de la personne
     */
    public String getMail(){
        return mail;
    }
    /**
     * Accesseur en �criture de la class Person sur mail.
     * @param mail Mail de la personne
     */
    public void setMail(String mail) {
        this.mail = mail;
    }
}