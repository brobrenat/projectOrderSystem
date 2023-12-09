package model;

public class cart {

	private String objectname;
	private Double objectprice;
	private String objectdescription;
	private  Integer objectquantity;
	private String objectID;
	
	public cart(String objectname, Double objectprice, Integer objectquantity, String objectdescription) {
	    super();
	    this.objectname = objectname;
	    this.objectprice = objectprice;
	    this.objectquantity = objectquantity;
	    this.objectdescription = objectdescription;
	}
	
	public cart(String objectname, Double objectprice) {
	    this(objectname, objectprice, 1, ""); 
	}

	public String getObjectname() {
		return objectname;
	}
	public void setObjectname(String objectname) {
		this.objectname = objectname;
	}
	public Double getObjectprice() {
		return objectprice;
	}
	public void setObjectprice(Double objectprice) {
		this.objectprice = objectprice;
	}
	public String getObjectdescription() {
		return objectdescription;
	}
	public void setObjectdescription(String objectdescription) {
		this.objectdescription = objectdescription;
	}
	public Integer getObjectquantity() {
		return objectquantity;
	}
	public void setObjectquantity(Integer objectquantity) {
		this.objectquantity = objectquantity;
	}
	
	public String getObjectID() { 
        return objectID;
    }

    public void setObjectID(String objectID) { 
        this.objectID = objectID;
    }

}
