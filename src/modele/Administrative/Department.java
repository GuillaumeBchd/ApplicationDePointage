package modele.Administrative;

import java.io.Serializable;

/**
 * Class s�rialisable repr�sentant un d�partement poss�dant un nom, un manager et un identifiant.
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
     * Constructeur de confort de la classe Department prenant un nom en param�tre.
     * @param givenName Nom du d�partement
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

    //M�thodes
    
    /**
     * Accesseur en lecture sur l'identifiant du d�partement
     * @return L'identifiant du d�partement.
     */
	public int getId() {
    	return id;
    }
    
	/**
     * Accesseur en lecture sur le nom du d�partement
     * @return Le nom du d�partement.
     */
    public String getName() {
        return name;
    }
    
    /**
	 * Accesseur en �criture sur le nom du d�partement
	 * @param name Le nom du d�partement
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Accesseur en lecture sur le manager du d�partement
     * @return L'id du manager du d�partement.
     */
	public int getManager() {
		return manager;
	}

	/**
	 * Accesseur en �criture sur le manager du d�partement
	 * @param manager L'id du manager du d�partement
	 */
	public void setManager(int manager) {
		this.manager = manager;
	}
	
	/**
	 * Attention : Ne pas utiliser cette fonction.
	 * Cette fonction est uniquement dans DataCentral pour que les d�partements cr�es aient le m�me id que ceux lus.
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
	 * Surcharge de la m�thode toString() permettant d'afficher le nom du d�partement.
	 */
	public String toString(){
		return name;
	}
	
	@Override
	/**
	 * Surcharge de la m�thode equals() permettant de comparer deux d�partements.
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