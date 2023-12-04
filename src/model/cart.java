package model;

public class cart {

	private String objectname;
	private Double objectprice;
	private String objectdescription;
	private  Integer objectquantity;
	public cart(String objectname, Double objectprice, String objectdescription, Integer objectquantity) {
		super();
		this.objectname = objectname;
		this.objectprice = objectprice;
		this.objectdescription = objectdescription;
		this.objectquantity = objectquantity;
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

}
