package nerdcastle.faravy.info;

public class TempSales {

	private String waiterId;
	private String prvCusID;
	private int itemID;
	private String optionsID;
	private String optionsGroupName;
	private String itemName;
	private String tableNo;
	private String isHappyHourPrice;
	private String isPrinted;
	private String customerName;
	private String area;
	private String instruction;
	private String categoryId;
	private double hrPrice;
	private double cpu;
	private String isOptionprice;
	private int item_RegularPrice;
	private String isOptionAvailable;
	private String addonsName;
	private String addonsId;
	private double addonsPrice;
	private String itemSL;
	private String optionName;
	private String optionGroupName;
	private String optionId;
	private String orderNo;
	private double vatAmount;

	
	
	public double getVatAmount() {
		return vatAmount;
	}

	public void setVatAmount(double vatAmount) {
		this.vatAmount = vatAmount;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public String getOptionGroupName() {
		return optionGroupName;
	}

	public void setOptionGroupName(String optionGroupName) {
		this.optionGroupName = optionGroupName;
	}

	public String getOptionId() {
		return optionId;
	}

	public void setOptionId(String optionId) {
		this.optionId = optionId;
	}

	public String getItemSL() {
		return itemSL;
	}

	public void setItemSL(String itemSL) {
		this.itemSL = itemSL;
	}

	public String getAddonsName() {
		return addonsName;
	}

	public void setAddonsName(String addonsName) {
		this.addonsName = addonsName;
	}

	public String getAddonsId() {
		return addonsId;
	}

	public void setAddonsId(String addonsId) {
		this.addonsId = addonsId;
	}


	public double getAddonsPrice() {
		return addonsPrice;
	}

	public void setAddonsPrice(double addonsPrice) {
		this.addonsPrice = addonsPrice;
	}

	public String getIsOptionAvailable() {
		return isOptionAvailable;
	}

	public void setIsOptionAvailable(String isOptionAvailable) {
		this.isOptionAvailable = isOptionAvailable;
	}

	public double getItem_RegularPrice() {
		return item_RegularPrice;
	}

	public void setItem_RegularPrice(int item_RegularPrice) {
		this.item_RegularPrice = item_RegularPrice;
	}

	public double getHrPrice() {
		return hrPrice;
	}

	public void setHrPrice(double hrPrice) {
		this.hrPrice = hrPrice;
	}

	public double getCpu() {
		return cpu;
	}

	public void setCpu(double cpu) {
		this.cpu = cpu;
	}

	public String getIsOptionprice() {
		return isOptionprice;
	}

	public void setIsOptionprice(String isOptionprice) {
		this.isOptionprice = isOptionprice;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	private static TempSales instance = null;

	private TempSales() {
	}

	public static synchronized TempSales getInstance() {
		if (instance == null) {
			instance = new TempSales();
		}

		return instance;
	}

	public String getWaiterId() {
		return waiterId;
	}

	public void setWaiterId(String waiterId) {
		this.waiterId = waiterId;
	}

	public String getPrvCusID() {
		return prvCusID;
	}

	public void setPrvCusID(String prvCusID) {
		this.prvCusID = prvCusID;
	}

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public String getOptionsID() {
		return optionsID;
	}

	public void setOptionsID(String optionsID) {
		this.optionsID = optionsID;
	}


	public String getOptionsGroupName() {
		return optionsGroupName;
	}

	public void setOptionsGroupName(String optionsGroupName) {
		this.optionsGroupName = optionsGroupName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getTableNo() {
		return tableNo;
	}

	public void setTableNo(String tableNo) {
		this.tableNo = tableNo;
	}

	public String getIsHappyHourPrice() {
		return isHappyHourPrice;
	}

	public void setIsHappyHourPrice(String isHappyHourPrice) {
		this.isHappyHourPrice = isHappyHourPrice;
	}

	public String getIsPrinted() {
		return isPrinted;
	}

	public void setIsPrinted(String isPrinted) {
		this.isPrinted = isPrinted;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

}
