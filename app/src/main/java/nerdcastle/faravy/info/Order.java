package nerdcastle.faravy.info;

import java.util.ArrayList;

public class Order {
	private static Order instance = null;

	private Order() {
	}

	public static synchronized Order getInstance() {
		if (instance == null) {
			instance = new Order();
		}

		return instance;
	}
    private int memberTypeId;
	public int getMemberTypeId() {
		return memberTypeId;
	}

	public void setMemberTypeId(int memberTypeId) {
		this.memberTypeId = memberTypeId;
	}
	private String customerName;
	private String balance;
	private String CusCategory;
	private String prvCusID;
	private String creditLimit;
	private String todaysSale;

	private ArrayList<String> itemList;
	private ArrayList<String> itemPriceList;
	private ArrayList<Integer> quantityList;
	private ArrayList<String> priorityList;

	public ArrayList<String> getPriorityList() {
		return priorityList;
	}

	public void setPriorityList(ArrayList<String> priorityList) {
		this.priorityList = priorityList;
	}

	private int itemId;
	private String cardNo;
	private int check;
	private String areaName;

	public String getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
	}

	public String getTodaysSale() {
		return todaysSale;
	}

	public void setTodaysSale(String todaysSale) {
		this.todaysSale = todaysSale;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public int getCheck() {
		return check;
	}

	public void setCheck(int check) {
		this.check = check;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getPrvCusID() {
		return prvCusID;
	}

	public void setPrvCusID(String prvCusID) {
		this.prvCusID = prvCusID;
	}

	public ArrayList<Integer> getQuantityList() {
		return quantityList;
	}

	public void setQuantityList(ArrayList<Integer> quantityList) {
		this.quantityList = quantityList;
	}

	private ArrayList<String> addonsList;
	private ArrayList<Double> addonsPriceList;

	public ArrayList<String> getAddonsList() {
		return addonsList;
	}

	public void setAddonsList(ArrayList<String> addonsList) {
		this.addonsList = addonsList;
	}

	public ArrayList<Double> getAddonsPriceList() {
		return addonsPriceList;
	}

	public void setAddonsPriceList(ArrayList<Double> addonsPriceList) {
		this.addonsPriceList = addonsPriceList;
	}

	public ArrayList<String> getItemPriceList() {
		return itemPriceList;
	}

	public void setItemPriceList(ArrayList<String> priceList) {
		this.itemPriceList = priceList;
	}

	public ArrayList<String> getItemList() {
		return itemList;
	}

	public void setItemList(ArrayList<String> itemList) {
		this.itemList = itemList;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getCusCategory() {
		return CusCategory;
	}

	public void setCusCategory(String cusCategory) {
		CusCategory = cusCategory;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerName() {
		return customerName;
	}
}
