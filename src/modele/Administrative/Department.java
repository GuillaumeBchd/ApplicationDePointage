package modele.Administrative;

import java.io.Serializable;

/**
 * Class sérialisable représentant un département possédant un nom, un manager et un identifiant.
 *
 */
public class Department implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    //Attributes
	private static int nbId = 0;
    private int id;
    private int manager;
    private String name;
    

    //Constructors
    /**
     * Constructeur de confort de la classe Department prenant un nom en paramètre.
     * @param givenName Nom du département
     */
    public Department(String givenName){
        name = givenName;
        nbId++;
        id = nbId;
    	manager = -1;
    }

    /**
     * Constructeur de recopie de la classe Department.
     * @param d Department que l'on veut recopier.
     */
    public Department(Department d) {
        nbId++;
        id = nbId;
        setName(d.getName());
        setManager(d.getManager());
	}

    //Méthodes
    
    /**
     * Accesseur en lecture sur l'identifiant du département
     * @return L'identifiant du département.
     */
	public int getId() {
    	return id;
    }
    
	/**
     * Accesseur en lecture sur le nom du département
     * @return Le nom du département.
     */
    public String getName() {
        return name;
    }
    
    /**
	 * Accesseur en écriture sur le nom du département
	 * @param name Le nom du département
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Accesseur en lecture sur le manager du département
     * @return L'id du manager du département.
     */
	public int getManager() {
		return manager;
	}

	/**
	 * Accesseur en écriture sur le manager du département
	 * @param manager L'id du manager du département
	 */
	public void setManager(int manager) {
		this.manager = manager;
	}
	
	/**
	 * Attention : Ne pas utiliser cette fonction.
	 * Cette fonction est uniquement dans DataCentral pour que les départements crées aient le même id que ceux lus.
	 * @param newId
	 */
	public void setId(int newId) {
		id = newId;
		if(newId > nbId) {
			nbId = newId + 1;
		}
	}

	//Surcharges
	@Override
	/**
	 * Surcharge de la méthode toString() permettant d'afficher le nom du département.
	 */
	public String toString(){
		return name;
	}
	
	@Override
	/**
	 * Surcharge de la méthode equals() permettant de comparer deux départements.
	 */
    public boolean equals(Object obj) 
    { 
    if(this == obj) 
            return true; 
          
        if(obj == null || obj.getClass()!= this.getClass()) 
            return false; 
          
        // type casting of the argument.  
        Department d = (Department) obj; 
          
        return (d.id == this.id); 
    } 
}