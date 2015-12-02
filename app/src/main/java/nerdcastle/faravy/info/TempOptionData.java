package nerdcastle.faravy.info;

import java.util.ArrayList;
import java.util.List;

public class TempOptionData {
	
	public static List<String> groupId = new ArrayList<String>();
	public static List<Integer> clickItem = new ArrayList<Integer>();
	public static List<Integer> minValue = new ArrayList<Integer>();
	public static List<Integer> maxValue = new ArrayList<Integer>();
	
	public static List<String> urlList = new ArrayList<String>();
	
	public static List<String> addonsName = new ArrayList<String>();
	public static List<String> addonsId = new ArrayList<String>();
	public static List<Double> addonsPrice = new ArrayList<Double>();
	
	public static List<String> addonsNameList;
	
	
	public static List<Integer> instructionIdList = new ArrayList<Integer>();
	public static List<Integer> insIndexList = new ArrayList<Integer>();
	public static List<String> instructionName = new ArrayList<String>();
	
	public static List<String> getAddonsNameList() {
		return addonsNameList;
	}

	public static void setAddonsNameList(List<String> addonsNameList) {
		TempOptionData.addonsNameList = addonsNameList;
	}

	public static double totalPrice;

	public static double getTotalPrice() {
		return totalPrice;
	}

	public static void setTotalPrice(double totalPrice) {
		TempOptionData.totalPrice = totalPrice;
	}
	

}
