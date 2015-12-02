package nerdcastle.faravy.info;

import java.util.ArrayList;

public class GroupOfExpListView {
	
	private String name;
	private ArrayList<ChildOfExpListView> items;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<ChildOfExpListView> getItems() {
		return items;
	}

	public void setItems(ArrayList<ChildOfExpListView> Items) {
		this.items = Items;
	}

}
