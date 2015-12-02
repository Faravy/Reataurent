package nerdcastle.faravy.fragment;

import java.util.ArrayList;
import java.util.Collections;

import nerdcastle.faravy.adapter.AddonsListAdapter;
import nerdcastle.faravy.info.AppController;
import nerdcastle.faravy.info.Order;
import nerdcastle.faravy.info.OrderButton;
import nerdcastle.faravy.info.SessionManager;
import nerdcastle.faravy.info.TempOptionData;
import nerdcastle.faravy.info.TempSales;
import nerdcastle.faravy.activity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class Itemfragment extends Fragment {
	TableLayout tableLayout;
	TableRow tableRow = null;
	View rootView;
	String categoryId;
	String getItemByCategory ="/dc/Api/Item/GetItemByCategory?catID=";
	String getAddons="/dc/Api/Addons/GetAddons";
	int enabledButton;
	int enabledaddon;
	JSONArray jsonArray;
	JSONArray addonArray;
	Button addOns;
	Button send;
	Button bckCategory;
	View layout;
	ArrayList<String> itemList;
	ArrayList<Double> itemPriceList;
	ArrayList<String> addonsNameList;
	ArrayList<Double> addonsPriceList;
	ArrayList<String> addonsiDList;
	int i;
	String baseUrl;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		 rootView = inflater.inflate(R.layout.fragment_item, container,
				false);
		 addOns = (Button) rootView.findViewById(R.id.addOn);
		 bckCategory = (Button) rootView.findViewById(R.id.bckCategory);
		 
		 tableLayout = (TableLayout)rootView.findViewById(R.id.categoryItem);
		
		 SessionManager session=new SessionManager(getActivity());
		 baseUrl=session.getUserData();
		
			categoryId=TempSales.getInstance().getCategoryId();
		
			addOns.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					//showAddon();
					showAddonsWithFastscroll();

				}
			});
			
			bckCategory.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					FragmentManager fm=getFragmentManager();
			        FragmentTransaction ft=fm.beginTransaction();
			        CategoryFragment myFragment=new CategoryFragment();
			        ft.replace(R.id.fragment,myFragment);
			        ft.addToBackStack(null);
			        ft.commit();
					
				}
			});
		 
			showItem( categoryId);
	    
		return rootView;

}
	
	public void showAddon() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(" Select Addons ");
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		layout = inflater.inflate(R.layout.addons, null);
		final TableLayout addonLayout = (TableLayout) layout
				.findViewById(R.id.tablelayout);
		
		      
		StringRequest stringrequest = new StringRequest(Method.GET,
				baseUrl+getAddons,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						try {
							addonsNameList=new ArrayList<String>();
							addonsPriceList=new ArrayList<Double>();
							addonArray = new JSONArray(response);
							for ( i = 0; i < addonArray.length(); i++) {

								final String addonsName = addonArray.getJSONObject(i)
										.getString("AddonsName");
								final String addonsId = addonArray.getJSONObject(i)
										.getString("AddonsID");
								final double addonsPrice=addonArray.getJSONObject(i).getDouble("AddonsPrice");

								if (i % 4 == 0) {
									tableRow = new TableRow(
											getActivity());
									addonLayout.addView(tableRow);
								}

								final OrderButton orderBtn = new OrderButton(
										getActivity());
								orderBtn.setId(i);
								orderBtn.setCategoryId(addonsId);
								orderBtn.setText(addonsName);
								orderBtn.setTextColor(Color
										.parseColor("#FFFFFF"));
								orderBtn.setBackgroundResource((R.drawable.selector));
								orderBtn.setOnClickListener(new OnClickListener() {
									int check=1;
									
									

									@Override
									public void onClick(View v) {
										v.setSelected(true);
										enabledaddon = orderBtn.getId();
										
										//deselectButton();
										
										 if(check == 1){
										       v.setBackgroundResource(R.drawable.selector);
											    addonsNameList.add(addonsName);
												addonsPriceList.add(addonsPrice);
												check = 0;
												Toast.makeText(getActivity(), addonsNameList.get(addonsNameList.size()-1), 1000).show();
										    }else{
										       v.setBackgroundResource(R.drawable.buttontable);										       
										       addonsNameList.remove(addonsName);
										       addonsPriceList.remove(addonsPrice);
										       check = 1;
										       Toast.makeText(getActivity(), String.valueOf(addonsNameList), 1000).show();
										    }
										 
										 TempSales.getInstance().setAddonsId(addonsId);
										 TempSales.getInstance().setAddonsName(addonsName);
										 TempSales.getInstance().setAddonsPrice(addonsPrice);
										 
										 sendAddonsInfo();
										 
								    Order.getInstance().setAddonsList(addonsNameList);
									Order.getInstance().setAddonsPriceList(addonsPriceList);
									
									}

								});
								tableRow.addView(orderBtn);
								
								
							}

						} catch (JSONException e) {

							e.printStackTrace();
						}

					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {

					}
				});

		AppController.getInstance().addToRequestQueue(stringrequest);
       
		builder.setView(layout);
		
		builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		});
		
		
		
		builder.show(); 

	}
	private void showAddonsWithFastscroll(){

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(" Select Addons ");
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		layout = inflater.inflate(R.layout.test_addons, null);
		
		final ListView listview=(ListView)layout.findViewById(R.id.testing);
		listview.setFastScrollAlwaysVisible(true);
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); 
		//listview.smoothScrollToPosition(0);
		                   
		StringRequest stringrequest = new StringRequest(Method.GET,
				baseUrl+getAddons,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						try {
							addonsNameList=new ArrayList<String>();
							addonsPriceList=new ArrayList<Double>();
							addonsiDList=new ArrayList<String>();
							addonArray = new JSONArray(response);
							for ( i = 0; i < addonArray.length(); i++) {

								final String addonsName = addonArray.getJSONObject(i)
										.getString("AddonsName");
								final String addonsId = addonArray.getJSONObject(i)
										.getString("AddonsID");
								final double addonsPrice=addonArray.getJSONObject(i).getInt("AddonsPrice");									
								
								addonsNameList.add(addonsName);
								addonsPriceList.add(addonsPrice);
								addonsiDList.add(addonsId);
							}			
							
							Collections.sort(addonsNameList);
							AddonsListAdapter adapter=new AddonsListAdapter(getActivity(), addonsNameList);
							listview.setAdapter(adapter);
							
							
							
							listview.setOnItemClickListener(new OnItemClickListener() {
								
								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									
								

									CheckedTextView ctv=(CheckedTextView)view.findViewById(android.R.id.text1);
								
								if (ctv.isChecked()) {
									
									TempOptionData.addonsId.add(addonsiDList.get(position));
									TempOptionData.addonsName.add(addonsNameList.get(position));
									TempOptionData.addonsPrice.add(addonsPriceList.get(position));
									
									
									
									
								}else{
									
									TempOptionData.addonsId.remove(addonsiDList.get(position));
									TempOptionData.addonsName.remove(addonsNameList.get(position));
									TempOptionData.addonsPrice.remove(addonsPriceList.get(position));
									
									
									
									
								}
										 TempSales.getInstance().setAddonsId(addonsiDList.get(position));
										 TempSales.getInstance().setAddonsName(addonsNameList.get(position));
										 TempSales.getInstance().setAddonsPrice(addonsPriceList.get(position));
										 
										// sendAddonsInfo();									
									
								}
							});
							
							

						} catch (JSONException e) {

							e.printStackTrace();
						}

					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {

					}
				});

		AppController.getInstance().addToRequestQueue(stringrequest);
        
		builder.setView(layout);
		
		builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//dialog.dismiss();
				 sendAddonsInfo();

			}
		});
        
		builder.show();
		
	}
	
	private void sendAddonsInfo() {
						
		 String itemSl=TempSales.getInstance().getItemSL();
		 
		  
		   if (itemSl=="" || itemSl==null) {
			  // Toast.makeText(getActivity(), "Select item first", Toast.LENGTH_SHORT).show();
			   warning("Select item first");
			
		}
		   else{
			 
			
			for (int i = 0; i < TempOptionData.addonsId.size(); i++) {
				
			
		   String data="AddonsID="+ TempOptionData.addonsId.get(i)   //TempSales.getInstance().getAddonsId()
					+"&"+"AddonsPrice="+TempOptionData.addonsPrice.get(i)        //TempSales.getInstance().getAddonsPrice()
					+"&"+"ItemID="+String.valueOf(TempSales.getInstance().getItemID())				
					+"&"+"AddonsName="+TempOptionData.addonsName.get(i)       //TempSales.getInstance().getAddonsName()
			        +"&"+"PrvCusID="+ Order.getInstance().getPrvCusID()
			        +"&"+"CusName="+Order.getInstance().getCustomerName()
			        +"&"+"TableNo="+TempSales.getInstance().getTableNo()
			        +"&"+"UserId="+TempSales.getInstance().getWaiterId()
			        +"&"+"vatPercent="+String.valueOf(TempSales.getInstance().getVatAmount())
			        +"&"+"AreaName="+TempSales.getInstance().getArea()
			        +"&"+"ItemSL="+TempSales.getInstance().getItemSL();
			       
			
			StringRequest stringRequest=new StringRequest(Method.POST, baseUrl+"/dc/Api/Sales/AddAddons?"+data.replaceAll(" ","%20"),
					new Response.Listener<String>() {

				@Override
				public void onResponse(String response) {
					
					
					TempOptionData.addonsId.clear();
					TempOptionData.addonsName.clear();
					TempOptionData.addonsPrice.clear();
					//Toast.makeText(getActivity(),response.toString(), 1000).show();
					//Log.i("Response", response.toString());
					
				}
			        }, 
			 
			        new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
					//Log.i("Error", error.toString());
				
					
				}
			});
			
			AppController.getInstance().addToRequestQueue(stringRequest);
			//Log.i("AddonURL",stringRequest.toString());
			}
		}
		
	}
	
	private void showItem(String categoryId) {
		
		StringRequest stringrequest = new StringRequest(Method.GET,
				baseUrl+getItemByCategory
						+ categoryId, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						try {
							itemList=new ArrayList<String>();
							itemPriceList=new ArrayList<Double>();
							jsonArray = new JSONArray(response);
							for (int i = 0; i < jsonArray.length(); i++) {

								final String itemName = jsonArray.getJSONObject(i)
										.getString("Item_name");
								final int itemId = jsonArray.getJSONObject(i).getInt(
										"Item_id");
								final String isOptionsAvailable = jsonArray
										.getJSONObject(i).getString(
												"IsOptionsAvailable");
								final Integer price=jsonArray.getJSONObject(i).getInt("Item_RegularPrice");
								final double hrPrice=jsonArray.getJSONObject(i).getDouble("Item_HHPrice");
								final double cpu=jsonArray.getJSONObject(i).getDouble("CPU");
								final String hhAppicable=jsonArray.getJSONObject(i).getString("HHApplicable");
								final String isOptionprice=jsonArray.getJSONObject(i).getString("IsOptionsPrice");
								
								

								if (i % 2 == 0) {
									tableRow = new TableRow(
											getActivity());
									tableLayout.addView(tableRow);
								}

							  final OrderButton  orderBtn = new OrderButton(
									  getActivity());
								orderBtn.setId(i);
								orderBtn.setIntId(itemId);
								orderBtn.setText(itemName);
								orderBtn.setTextColor(Color
										.parseColor("#FFFFFF"));
								orderBtn.setBackgroundResource((R.drawable.selector));
								
								orderBtn.setOnClickListener(new OnClickListener() {
									int check = 1;
									

									@Override
									public void onClick(View v) {
										v.setSelected(true);
										enabledButton = orderBtn.getId();
										deselectButtons();										 
										if (isOptionsAvailable.equals("Y")) {
											
											FragmentManager fm=getFragmentManager();
									        FragmentTransaction ft=fm.beginTransaction();
									        OptionSelectionFragment myFragment=new OptionSelectionFragment();
									        ft.replace(R.id.fragment,myFragment);
									        ft.addToBackStack(null);
									        ft.commit();
									        Order.getInstance().setItemId(itemId);
											
										} 
													
											 if(check == 1){
											       v.setBackgroundResource(R.drawable.selector);
												       //itemNameList.add(itemName);
													//itemPriceList.add(price);
													check = 0;
											    }else{
											       v.setBackgroundResource(R.drawable.buttontable);
											       check = 1;
											       //itemNameList.remove(itemName);
											       //itemPriceList.remove(price);
											    }
											 
											 TempSales.getInstance().setItemID(itemId);
											 TempSales.getInstance().setItemName(itemName);
											 TempSales.getInstance().setIsHappyHourPrice(hhAppicable);
											 TempSales.getInstance().setHrPrice(hrPrice);
											 TempSales.getInstance().setCpu(cpu);
											 TempSales.getInstance().setIsOptionprice(isOptionprice);
											 TempSales.getInstance().setItem_RegularPrice(price);
											 TempSales.getInstance().setIsOptionAvailable(isOptionsAvailable);
											 
											 
										
									   // Order.getInstance().setItemList(itemNameList);
										//Order.getInstance().setItemPriceList(itemPriceList);
										
											 sendItemInfo();
											
										}
									
								});

								tableRow.addView(orderBtn);
							}
						} catch (JSONException e) {

							e.printStackTrace();
						}
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {

					}
				});

		AppController.getInstance().addToRequestQueue(stringrequest);
	}
	
	public void deselectButtons() {
		for (int i = 0; i < jsonArray.length(); i++) {
			if (enabledButton != i)
				rootView.findViewById(i).setSelected(false);
		}
	}

	private void sendItemInfo() {
		
		String data="CPU="+String.valueOf(TempSales.getInstance().getCpu())
				+"&"+"HHApplicable="+TempSales.getInstance().getIsHappyHourPrice()
        		+"&"+"IsOptionsPrice="+TempSales.getInstance().getIsOptionprice()
        		+"&"+"Item_HHPrice="+String.valueOf(TempSales.getInstance().getHrPrice())
        		+"&"+"Item_RegularPrice="+String.valueOf(TempSales.getInstance().getItem_RegularPrice())
        		+"&"+"Item_id="+String.valueOf(TempSales.getInstance().getItemID())
        		+"&"+"Item_name="+TempSales.getInstance().getItemName()
        		+"&"+"PrvCusID="+ Order.getInstance().getPrvCusID()
        		+"&"+"CusName="+Order.getInstance().getCustomerName()
        		+"&"+"_TableNo="+TempSales.getInstance().getTableNo()
        		+"&"+"WaiterID="+TempSales.getInstance().getWaiterId()
        		+"&"+"vatPercent="+String.valueOf(TempSales.getInstance().getVatAmount())
        		+"&"+"AreaName="+TempSales.getInstance().getArea()
        		+"&"+"IsItemsHaveOption="+TempSales.getInstance().getIsOptionAvailable();
		
		StringRequest stringRequest=new StringRequest(Method.POST, baseUrl+"/dc/Api/Sales/PostTempItem?"+data.replaceAll(" ","%20"),
			new Response.Listener<String>() {

		@Override
		public void onResponse(String response) {
			//Toast.makeText(getActivity(),response.toString(), 1000).show();
			//Log.i("response",response.toString());
			try {
				JSONObject jobj=new JSONObject(response);
				
				String itemSL=jobj.getString("Data");
				TempSales.getInstance().setItemSL(itemSL);
				
				boolean result=jobj.getBoolean("ResultState");
				if (result) {
					
				//Toast.makeText(getActivity(), TempSales.getInstance().getItemName()+"  Selected",Toast.LENGTH_SHORT).show();
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
				
			}
			
		}
	        }, 
	 
	        new Response.ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
			//Log.i("Error", error.toString());
		
			
		}
	});/*{
        @Override
        protected Map<String,String> getParams()throws AuthFailureError{
            Map<String,String> params = new HashMap<String, String>();
            params.put("itemID",String.valueOf(TempSales.getInstance().getItemID()));
            
           
            params.put("CPU",String.valueOf(TempSales.getInstance().getCpu()));
            params.put("HHApplicable",TempSales.getInstance().getIsHappyHourPrice());
            params.put("IsOptionsPrice",TempSales.getInstance().getIsOptionprice());
            params.put("Item_HHPrice",String.valueOf(TempSales.getInstance().getHrPrice()));
            params.put("IsHappyHourPrice",TempSales.getInstance().getIsHappyHourPrice());
            params.put("Item_RegularPrice",String.valueOf(TempSales.getInstance().getItem_RegularPrice()));
            params.put("Item_id",String.valueOf(TempSales.getInstance().getItemID()));
            params.put("Item_name", TempSales.getInstance().getItemName());
            params.put("PrvCusID", Order.getInstance().getPrvCusID());
            params.put("CusName",Order.getInstance().getCustomerName());
            params.put("_TableNo", TempSales.getInstance().getTableNo());
            params.put("WaiterID", TempSales.getInstance().getWaiterId());
            params.put("vatPercent", "0"); 
            params.put("AreaName",TempSales.getInstance().getArea());
            params.put("IsItemsHaveOption",TempSales.getInstance().getIsOptionAvailable());
            
            
            return params;
            
        }
        
	};*/
	AppController.getInstance().addToRequestQueue(stringRequest);
	//Log.i("URL", stringRequest.toString());
		
	}
	
	private void onBackPressed(){
	   // getActivity().onBackPressed();
	   
	}

	private void warning(String message){

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(message);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();

	}
}
