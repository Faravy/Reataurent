package nerdcastle.faravy.info;

import java.util.ArrayList;

public class ModifyOrder {
	private static ModifyOrder instance = null;

	private ModifyOrder() {
	}

	public static synchronized ModifyOrder getInstance() {
		if (instance == null) {
			instance = new ModifyOrder();
		}
		return instance;
	}
	private ArrayList<String> itemIdList;
	private ArrayList<String> addonsIdList;
	private ArrayList<Integer> itemSLList;
	private ArrayList<String> optionsIdList;
	private ArrayList<String> prevInsList;
	private ArrayList<String> insIndexList;

	public ArrayList<String> getPriorityList() {
		return priorityList;
	}

	public void setPriorityList(ArrayList<String> priorityList) {
		this.priorityList = priorityList;
	}

	private ArrayList<String> priorityList;

	public ArrayList<String> getPrevInsList() {
		return prevInsList;
	}

	public void setPrevInsList(ArrayList<String> prevInsList) {
		this.prevInsList = prevInsList;
	}

	public ArrayList<String> getInsIndexList() {
		return insIndexList;
	}

	public void setInsIndexList(ArrayList<String> insIndexList) {
		this.insIndexList = insIndexList;
	}

	public ArrayList<String> getOptionsIdList() {
		return optionsIdList;
	}

	public void setOptionsIdList(ArrayList<String> optionsIdList) {
		this.optionsIdList = optionsIdList;
	}

	public ArrayList<String> getItemIdList() {
		return itemIdList;
	}

	public void setItemIdList(ArrayList<String> itemIdList) {
		this.itemIdList = itemIdList;
	}

	public ArrayList<String> getAddonsIdList() {
		return addonsIdList;
	}

	public void setAddonsIdList(ArrayList<String> addonsIdList) {
		this.addonsIdList = addonsIdList;
	}

	public ArrayList<Integer> getItemSLList() {
		return itemSLList;
	}

	public void setItemSLList(ArrayList<Integer> itemSLList) {
		this.itemSLList = itemSLList;
	}

}
