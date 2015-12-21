package nerdcastle.faravy.fragment;

import java.util.ArrayList;
import java.util.Collections;

import nerdcastle.faravy.adapter.ListViewAdapter;
import nerdcastle.faravy.adapter.AddonsListAdapter;
import nerdcastle.faravy.info.AppController;
import nerdcastle.faravy.info.TempOptionData;
import nerdcastle.faravy.info.ModifyOrder;
import nerdcastle.faravy.info.Order;
import nerdcastle.faravy.info.SessionManager;
import nerdcastle.faravy.info.TempSales;
import nerdcastle.faravy.activity.AreaSelectionActivity;
import nerdcastle.faravy.activity.FinishingOrder;
import nerdcastle.faravy.activity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class ConfirmOrderFragment extends Fragment implements OnClickListener{

	View rootView;
	JSONArray jsonArray;

	private ArrayList<String> itemNameList;
	private ArrayList<String> itemPriceList;
	private ArrayList<Integer> quantityList;
	private ArrayList<String> itemIdList;
	private ArrayList<String> addonsIdList;
	private ArrayList<Integer> itemSLList;
	private ArrayList<String> optionsIdList;
	private ArrayList<String> preInsList;
	private ArrayList<String> insIndexList;
	private ArrayList<String> priorityList;

	ListView listView;
	ListAdapter listAdapter;

	int mPosition;
	AlertDialog dialog;
	
	TextView totalPtice;
	Button instruction;
	Button submit;
	Button submit_print;
	Button cancel;
	Button exit;
	Button bckAddons;
	Button bckCategory;
	
	CheckBox checkbox;
	EditText pinNo;
	String checkCommand;
	
	String baseUrl;
	
	View layout;
	ArrayList<String> instructionList;
	ArrayList<Integer> instructionIdList;

	String getAddons="/dc/Api/Addons/GetAddons";
	JSONArray addonArray;
	ArrayList<String> addonsList;
	ArrayList<Double> addonsPriceList;
	ArrayList<String> addonsiDList;
	private ProgressDialog proDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_confirmorder,container,false);
		
		init();		
		showData();
		return rootView;

	}
	

	private void init() {
		listView = (ListView)rootView.findViewById(R.id.listOrder);
		totalPtice=(TextView) rootView.findViewById(R.id.subTotal);
		//instruction=(Button) rootView.findViewById(R.id.instruction);
		submit=(Button) rootView.findViewById(R.id.submit);
		//submit_print=(Button) rootView.findViewById(R.id.submitPrint);
		cancel=(Button) rootView.findViewById(R.id.cancel);
		exit=(Button) rootView.findViewById(R.id.exit);
		bckAddons=(Button) rootView.findViewById(R.id.bckAddon);
		bckCategory=(Button) rootView.findViewById(R.id.bckCategory);
		
		SessionManager session=new SessionManager(getActivity());
		baseUrl=session.getUserData();
		
		proDialog = new ProgressDialog(getActivity());
        proDialog.setMessage("Loading.....");
        proDialog.setCancelable(true);
        
		
		//instruction.setOnClickListener(this);
		submit.setOnClickListener(this);
		//submit_print.setOnClickListener(this);
		cancel.setOnClickListener(this);
		exit.setOnClickListener(this);
		bckAddons.setOnClickListener(this);
		bckCategory.setOnClickListener(this);
		
	}

	private void showData() {
		
		final double total=0;
		String userId=TempSales.getInstance().getWaiterId();
		String customerId=Order.getInstance().getPrvCusID();
		
		proDialog.show();
		
		StringRequest stringRequest = new StringRequest(
				Method.GET,
				baseUrl+"/dc/Api/Sales/SelectAllByUserIDCustomerID?userID="+userId+"&customerId="+customerId,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.i("Listview", response.toString());
						
						try {
							itemNameList =new ArrayList<String>();
							itemPriceList=new ArrayList<String>();
							quantityList=new ArrayList<Integer>();
							
							itemIdList=new ArrayList<String>();
							addonsIdList=new ArrayList<String>();
							itemSLList=new ArrayList<Integer>();
							optionsIdList=new ArrayList<String>();
							preInsList=new ArrayList<String>();
							insIndexList=new ArrayList<String>();
							priorityList=new ArrayList<String>();
							
							jsonArray = new JSONArray(response);

							for (int i = 0; i < jsonArray.length(); i++) {

								String itemName = jsonArray.getJSONObject(i)
										.getString("ItemName");
								String rpu=jsonArray.getJSONObject(i).getString("RPU_Text");
								int quantity=jsonArray.getJSONObject(i).getInt("QTY");	
								
								   
								itemNameList.add(itemName);
								itemPriceList.add(rpu);
								quantityList.add(quantity);
								
								String itemId=jsonArray.getJSONObject(i).getString("Item_id");
								String addonsId=jsonArray.getJSONObject(i).getString("AddonsID");
								int itemSL=jsonArray.getJSONObject(i).getInt("ItemSL");
								String optionsId=jsonArray.getJSONObject(i).getString("OptionsGroupName");
								
								itemIdList.add(itemId);
								itemSLList.add(itemSL);
								addonsIdList.add(addonsId);
								optionsIdList.add(optionsId);

								String orderNo=jsonArray.getJSONObject(i).getString("OrderNo");
								TempSales.getInstance().setOrderNo(orderNo);

								String instruction=jsonArray.getJSONObject(i).getString("Instruction");
								preInsList.add(instruction);
								String insIndex=jsonArray.getJSONObject(i).getString("InstructionIndex");
								insIndexList.add(insIndex);
								String priority=jsonArray.getJSONObject(i).getString("Priority");
								priorityList.add(priority);

								/*String areaName=jsonArray.getJSONObject(i).getString("Area");
								String tableNo=jsonArray.getJSONObject(i).getString("TblNo");
								TempSales.getInstance().setArea(areaName);
								TempSales.getInstance().setTableNo(tableNo);*/




							}
							
							ModifyOrder.getInstance().setItemIdList(itemIdList);
							ModifyOrder.getInstance().setAddonsIdList(addonsIdList);
							ModifyOrder.getInstance().setItemSLList(itemSLList);
							ModifyOrder.getInstance().setOptionsIdList(optionsIdList);
							ModifyOrder.getInstance().setPrevInsList(preInsList);
							ModifyOrder.getInstance().setInsIndexList(insIndexList);
							ModifyOrder.getInstance().setPriorityList(priorityList);


							Order.getInstance().setItemList(itemNameList);
							Order.getInstance().setItemPriceList(itemPriceList);
							Order.getInstance().setQuantityList(quantityList);
							//Order.getInstance().setPriorityList(priorityList);


							listAdapter = new ListViewAdapter(getActivity(), itemNameList);
							listView.setAdapter(listAdapter);


							calculateSubtotalValue(total);

							listView.setOnItemLongClickListener(new OnItemLongClickListener() {

								@Override
								public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

									editOption(position);
									return false;
								}
							});
									
							listView.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									
									view.setSelected(true);
									
									String itemID=ModifyOrder.getInstance().getItemIdList().get(position);      		
							        String itemSL=String.valueOf(ModifyOrder.getInstance().getItemSLList().get(position));
							       
							        TempSales.getInstance().setItemID(Integer.parseInt(itemID));
							        TempSales.getInstance().setItemSL(itemSL);
									
									
								}
							});	
							proDialog.dismiss();

						} 
						catch (JSONException e) {
							e.printStackTrace();
							
						}

					}
				},

				new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
						proDialog.dismiss();

					}
				});
		
		AppController.getInstance().addToRequestQueue(stringRequest);

	}

	public void deleteItem(int position){
    
		String option=ModifyOrder.getInstance().getOptionsIdList().get(position);
		
		if (option.length()==0){
		String data=        		
        		"UserId="+TempSales.getInstance().getWaiterId()
        		+"&"+"cusID="+Order.getInstance().getPrvCusID()
        		+"&"+"ItemID="+ModifyOrder.getInstance().getItemIdList().get(position)       		
        		+"&"+"ItemSL="+String.valueOf(ModifyOrder.getInstance().getItemSLList().get(position))
		        +"&"+"AddonsID="+ModifyOrder.getInstance().getAddonsIdList().get(position);
		
		
		StringRequest stringRequest=new StringRequest(Method.POST, baseUrl+"/dc/Api/Sales/RemoveItemAddons?"+data.replaceAll(" ","%20"),
			new Response.Listener<String>() {

		@Override
		public void onResponse(String response) {
			//Toast.makeText(getActivity(),response.toString(), 1000).show();
			try {
				JSONObject jsonObj= new JSONObject(response);
				boolean result= jsonObj.getBoolean("ResultState");
				if(result==true){
					showData();
					TempSales.getInstance().setItemSL("");
					
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
	});
		AppController.getInstance().addToRequestQueue(stringRequest);
		
		//Log.i("Delete",stringRequest.toString());
		}
		else
		
		Toast.makeText(getActivity(), "You can't modify options", Toast.LENGTH_SHORT).show();

		}

	public void editOption( int position){
		mPosition=position;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(" Select Option ");
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View layout = inflater.inflate(R.layout.modify_order, null);
		Button add=(Button)layout.findViewById(R.id.add);
		Button remove=(Button)layout.findViewById(R.id.remove);
		Button delete=(Button)layout.findViewById(R.id.delete);
		Button instruction=(Button)layout.findViewById(R.id.instruction);
		Button priority=(Button)layout.findViewById(R.id.text_priority);
		builder.setView(layout);
	       // builder.show();
	        dialog=builder.create();
	        dialog.show();

		priority.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				putPriority(mPosition);
				dialog.dismiss();
			}
		});

		instruction.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(getActivity(),String.valueOf(mPosition),Toast.LENGTH_SHORT).show();
				//Toast.makeText(getActivity(),ModifyOrder.getInstance().getPrevInsList().toString(),Toast.LENGTH_SHORT).show();
				//showData();
				putInstruction(mPosition);
				dialog.dismiss();

			}
		});
		
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addItem(mPosition);
				dialog.dismiss();


			}
		});
		
        remove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				removeItem(mPosition);
				dialog.dismiss();

			}
		});
        
        delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				deleteItem(mPosition);
				dialog.dismiss();


			}
		});
        
		
	}

	private void putPriority(final int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Put Priority");
		builder.setIcon(R.drawable.ic_launcher);
		final EditText input = new EditText(getActivity());
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		String previousPriority=ModifyOrder.getInstance().getPriorityList().get(position);
		input.setText(previousPriority);

		builder.setView(input);


		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				String priority = input.getText().toString().trim();

				if (priority.equals("0")|| priority.equals("")){
					warning("Invalid Number");
					return;
				}
				sendPriority(position, priority);
			}
		});


		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//sendPriority(position, "");
				dialog.dismiss();
			}
		});

		builder.show();
	}

	protected void removeItem(int position) {
    
		String option=ModifyOrder.getInstance().getOptionsIdList().get(position);		
		
		if (option.length()==0){ 
		
		String data=
				"AddonsID="+ModifyOrder.getInstance().getAddonsIdList().get(position)
				+"&"+"ItemID="+ModifyOrder.getInstance().getItemIdList().get(position)
				+"&"+"cusID="+Order.getInstance().getPrvCusID()
				+"&"+"qty="+"-1"
        		+"&"+"UserId="+TempSales.getInstance().getWaiterId()      		
        		+"&"+"ItemSL="+String.valueOf(ModifyOrder.getInstance().getItemSLList().get(position));

		StringRequest stringRequest=new StringRequest(Method.POST, baseUrl+"/dc/Api/Sales/UpdateTempItemAddons?"+data.replaceAll(" ","%20"),
			new Response.Listener<String>() {

		@Override
		public void onResponse(String response) {
			//Toast.makeText(getActivity(),response.toString(), 1000).show();
			try {
				JSONObject jsonObj= new JSONObject(response);
				boolean result= jsonObj.getBoolean("ResultState");
				if(result==true){
					showData();
					
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
	});
		AppController.getInstance().addToRequestQueue(stringRequest);
		
		}
		else
		
		//Toast.makeText(getActivity(), "You can't modify options", Toast.LENGTH_SHORT).show();
		warning("You can't modify options");

		}

	protected void addItem(final int position) {
	
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle("Put Amount");
	    final EditText input = new EditText(getActivity());
	    input.setInputType(InputType.TYPE_CLASS_NUMBER);
	    builder.setView(input);
	    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				String quantity = input.getText().toString().trim();

				if (quantity.equals("0") || quantity.equals("")) {
					warning("Invalid Number");
					return;
				}
				additemNo(position, quantity);
			}
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
	    builder.show();
	}

	protected void additemNo(int position,String quantity) {
		
		String option=ModifyOrder.getInstance().getOptionsIdList().get(position);

		Log.i("option", option);
		
		if (option.length()==0){	
				
		String data= "AddonsID="+ModifyOrder.getInstance().getAddonsIdList().get(position)		
				      +"&"+"ItemID="+ModifyOrder.getInstance().getItemIdList().get(position)
				      +"&"+"cusID="+Order.getInstance().getPrvCusID()
				      +"&"+"qty="+quantity
        		      +"&"+"UserId="+TempSales.getInstance().getWaiterId()      		
        		      +"&"+"ItemSL="+String.valueOf(ModifyOrder.getInstance().getItemSLList().get(position));

		StringRequest stringRequest=new StringRequest(Method.POST, baseUrl+"/dc/Api/Sales/UpdateTempItemAddons?"+data.replaceAll(" ","%20"),
			new Response.Listener<String>() {

		@Override
		public void onResponse(String response) {

			try {
				JSONObject jsonObj= new JSONObject(response);
				boolean result= jsonObj.getBoolean("ResultState");
				if(result==true){
					showData();
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
	});
		AppController.getInstance().addToRequestQueue(stringRequest);
		//Log.i("Delete",stringRequest.toString());
		}
		else
		
		//Toast.makeText(getActivity(), "You can't modify options", Toast.LENGTH_SHORT).show();
		warning("You can't modify options");

		}

	public void calculateSubtotalValue(double total){
		
		for (int i = 0; i < listView.getCount(); i++) {
			View v=listView.getAdapter().getView(i,null,null);
			TextView price=(TextView)v.findViewById(R.id.totalprice);
			double value=Double.parseDouble(price.getText().toString());
			total+=value;
			TempOptionData.setTotalPrice(total);
			totalPtice.setText("Total : "+String.valueOf(total));
		}
	}

	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch (id) {

		/*case R.id.instruction:
			putInstruction();
			break;*/
        case R.id.submit:
        	submit();

			break;
       /* case R.id.submitPrint:
        	submitAndPrint();

			break;*/
        case R.id.cancel:
        	cancel();

	        break;
        case R.id.exit:
        	exit();

        	break;

        case R.id.bckAddon:

        	showAddons();

	        break;
        case R.id.bckCategory:

        	FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			CategoryFragment myFragment = new CategoryFragment();
			ft.replace(R.id.fragment, myFragment);
			ft.commit();

        	break;

		default:
			break;
		}

	}

	private void showAddons() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(" Select Addons ");
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		layout = inflater.inflate(R.layout.test_addons, null);
		
		final ListView listview=(ListView)layout.findViewById(R.id.testing);
		listview.setFastScrollAlwaysVisible(true);
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); 
		
		  proDialog.show();                 
		
		  StringRequest stringrequest = new StringRequest(Method.GET,
				baseUrl+getAddons,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						
						try {
							addonsList=new ArrayList<String>();
							addonsPriceList=new ArrayList<Double>();
							addonsiDList=new ArrayList<String>();
							addonArray = new JSONArray(response);
							
							for (int i = 0; i < addonArray.length(); i++) {

								final String addonsName = addonArray.getJSONObject(i)
										.getString("AddonsName");
								final String addonsId = addonArray.getJSONObject(i)
										.getString("AddonsID");
								final double addonsPrice=addonArray.getJSONObject(i).getInt("AddonsPrice");									
								
								addonsList.add(addonsName);
								addonsPriceList.add(addonsPrice);
								addonsiDList.add(addonsId);
							}				
							
							TempOptionData.addonsId.clear();
							TempOptionData.addonsName.clear();
							TempOptionData.addonsPrice.clear();
							
							Collections.sort(addonsList);
							AddonsListAdapter adapter=new AddonsListAdapter(getActivity(), addonsList);
							listview.setAdapter(adapter);
							TempOptionData.setAddonsNameList(addonsList);
							
							
							listview.setOnItemClickListener(new OnItemClickListener() {
								
								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									
									
								
									CheckedTextView ctv=(CheckedTextView)view.findViewById(android.R.id.text1);
								
								if (ctv.isChecked()) {
									
									TempOptionData.addonsId.add(addonsiDList.get(position));
									TempOptionData.addonsName.add(addonsList.get(position));
									TempOptionData.addonsPrice.add(addonsPriceList.get(position));
									//ctv.setChecked(true);
									
									
									
								}else{
									
									TempOptionData.addonsId.remove(addonsiDList.get(position));
									TempOptionData.addonsName.remove(addonsList.get(position));
									TempOptionData.addonsPrice.remove(addonsPriceList.get(position));
									//ctv.setChecked(false);
									
									
									
								}
									
										 /*TempSales.getInstance().setAddonsId(addonsiDList.get(position));
										 TempSales.getInstance().setAddonsName(addonsList.get(position));
										 TempSales.getInstance().setAddonsPrice(addonsPriceList.get(position));*/
										 
																			
									
								}

								
							});
							
							proDialog.dismiss();
							

						} catch (JSONException e) {

							e.printStackTrace();
						}

					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						proDialog.dismiss();

					}
				});

		AppController.getInstance().addToRequestQueue(stringrequest);
        
		builder.setView(layout);
		
		builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
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
		
	}else{
		 
		
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
				
				showData();
				
				
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

	private void exit() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setTitle("Do you want to exit?");
		builder.setIcon(R.drawable.ic_launcher);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				String data = "UserId=" + TempSales.getInstance().getWaiterId();
				proDialog.show();
				StringRequest stringrequest = new StringRequest(Method.POST,
						baseUrl + "/dc/Api/Sales/RemoveAllData?" + data.replaceAll(" ", "%20"),
						new Response.Listener<String>() {

							@Override
							public void onResponse(String response) {
								//Toast.makeText(getActivity(), response.toString(), 1000).show();
								//getActivity().moveTaskToBack(true); 
								getActivity().finishAffinity();
							}

						}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
						proDialog.dismiss();

					}
				});
				AppController.getInstance().addToRequestQueue(stringrequest);
				//Log.i("Instruction", stringrequest.toString());


			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	
	}

	private void cancel() {
		proDialog.show();
		
		String data="UserId="+TempSales.getInstance().getWaiterId();
	    
		StringRequest stringrequest = new StringRequest(Method.POST,
        baseUrl+"/dc/Api/Sales/RemoveAllData?"+data.replaceAll(" ","%20"),
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
					//Toast.makeText(getActivity(), response.toString(), 1000).show();	
                    startActivity(new Intent(getActivity(),AreaSelectionActivity.class));
                    getActivity().finish();
                    proDialog.dismiss();
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
						proDialog.dismiss();

					}
				});
	    AppController.getInstance().addToRequestQueue(stringrequest);
	    //Log.i("Instruction", stringrequest.toString());
	    
		
		
	}

	private void submitAndPrint() {
		sendPrintCommand();
		submit();
		
	}

	public void sendPrintCommand() {
		String data=
	    		"oderno="+TempSales.getInstance().getOrderNo()	    		
	    		+"&"+"cusid="+Order.getInstance().getPrvCusID();
	    StringRequest stringrequest = new StringRequest(Method.POST,
				baseUrl+"/dc/Api/Sales/SetPrintPreviewCommand?"+data.replaceAll(" ","%20"),
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
					//Toast.makeText(getActivity(), response.toString(), 1000).show();	

					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();

					}
				});
	    AppController.getInstance().addToRequestQueue(stringrequest);
	   // Log.i("Instruction", stringrequest.toString());
		
	}

	private void submit() {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Pin Verification");

		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.submit, null);
		
		pinNo=(EditText)view.findViewById(R.id.verifyPin);
		pinNo.setTransformationMethod(PasswordTransformationMethod.getInstance());
	    checkbox=(CheckBox)view.findViewById(R.id.checkPrint);
		
	    builder.setView(view);
		builder.setPositiveButton("Submit",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						String todaySale = Order.getInstance().getTodaysSale();

						double balance = Double.parseDouble(Order.getInstance().getBalance());
						double dailyLimit = Double.parseDouble(Order.getInstance().getCreditLimit());
						double todaysSale = 0;
						double totalPrice = TempOptionData.getTotalPrice();
						double childLimit = 0;

						if (todaySale.length() > 0) {
							todaysSale = Double.parseDouble(todaySale);
							childLimit = totalPrice + todaysSale;
							//Toast.makeText(getActivity(),String.valueOf(childLimit),Toast.LENGTH_SHORT).show();
						}

						if (balance < totalPrice) {
							warning("Insufficient Balance");

						} else if (dailyLimit > 0) {

							if (childLimit > dailyLimit) {
								warning("You have reached your limit");
							} else if (totalPrice > dailyLimit) {
								warning("You have reached your limit");
							} else {
								loadData();
							}
						}

						else{
							loadData();
						}
				}
	});
		builder.show();
		
		
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

	private void putInstruction(final int position) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle("Put instruction");
	    LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		layout = inflater.inflate(R.layout.instruction, null);
		
		final ListView listview=(ListView)layout.findViewById(R.id.instructionList);
		final EditText editText=(EditText)layout.findViewById(R.id.instructionText);

		String prevInstruction=ModifyOrder.getInstance().getPrevInsList().get(position);

		if (!prevInstruction.equals("")) {

			String[] parts = prevInstruction.split("~");

			if (parts.length>1) {
				String part1 = parts[1];
				editText.setText(part1);
			}
		}


	    proDialog.show();

		StringRequest stringRequest=new StringRequest(Method.GET, baseUrl+"/dc/Api/Sales/GetInstructionList",
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
					try {

						instructionList=new ArrayList<String>();
						instructionIdList=new ArrayList<Integer>();
						
						jsonArray=new JSONArray(response);
						
						for (int i = 0; i < jsonArray.length(); i++) {

							final String instruction=jsonArray.getJSONObject(i).getString("Instruction");
							final int instructionId=jsonArray.getJSONObject(i).getInt("ID");
							
							instructionList.add(instruction);
							instructionIdList.add(instructionId);
							
						}
						
						TempOptionData.instructionIdList.clear();
						TempOptionData.instructionName.clear();
						TempOptionData.insIndexList.clear();

						ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(), R.layout.addons_text,instructionList);
						listview.setAdapter(adapter);

						listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

					/*	ArrayList<String>indexList=ModifyOrder.getInstance().getInsIndexList();
						String index=ModifyOrder.getInstance().getInsIndexList().get(position);
						//Toast.makeText(getActivity(),"Tanvir "+String.valueOf(indexList),Toast.LENGTH_SHORT).show();


						if (!index.equals("") && !index.equals("null")){

							String[] parts = index.split(", ");

							for (String item : parts) {

								int indexpos=Integer.parseInt(item);
								//Toast.makeText(getActivity(), String.valueOf(indexpos), Toast.LENGTH_SHORT).show();

								listview.setItemChecked(indexpos, true);
							}

						}*/



						listview.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								

								CheckedTextView ctv=(CheckedTextView)view.findViewById(android.R.id.text1);

							
							if (ctv.isChecked()) {
								
								TempOptionData.instructionIdList.add(instructionIdList.get(position));
								TempOptionData.instructionName.add(instructionList.get(position));
								TempOptionData.insIndexList.add(position);
								//Toast.makeText(getActivity(),String.valueOf(position),Toast.LENGTH_SHORT).show();
								
							}else{
								
								TempOptionData.instructionIdList.remove(instructionIdList.get(position));
								TempOptionData.instructionName.remove(instructionList.get(position));
								TempOptionData.insIndexList.remove(position);
							}


							}
							
						});
						proDialog.dismiss();
					} catch (Exception e)
					{
						Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
					}
						
					}		
		
		}, 
				
				new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						proDialog.dismiss();
						
						
					}
				});
		
		AppController.getInstance().addToRequestQueue(stringRequest);

		builder.setView(layout);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				sendInstruction(position);
			}

			private void sendInstruction(int position) {

				String instruction = "";

				for (int i = 0; i < TempOptionData.instructionName.size(); i++) {

					instruction += TempOptionData.instructionName.get(i) + " ! ";
				}

				String data = "UserId=" + TempSales.getInstance().getWaiterId()
						+ "&" + "orderno=" + TempSales.getInstance().getOrderNo()
						+ "&" + "instruction=" + instruction + "~" + editText.getText().toString().trim()
						+ "&" + "instructionIDs=" + TempOptionData.instructionIdList
						+ "&" + "cusid=" + Order.getInstance().getPrvCusID()
						+ "&" + "itemId=" + ModifyOrder.getInstance().getItemIdList().get(position)
						+ "&" + "itemSl=" + String.valueOf(ModifyOrder.getInstance().getItemSLList().get(position))
						+ "&" + "InsIndex=" + TempOptionData.insIndexList;

				StringRequest stringrequest = new StringRequest(Method.POST,
						baseUrl + "/dc/Api/Sales/UpdateKitchenInstructions?" + data.replaceAll(" ", "%20"),

						new Response.Listener<String>() {

							@Override
							public void onResponse(String response) {
								//Temp.instructionIdList.clear();
								//Temp.instructionName.clear();
								//Toast.makeText(getActivity(), response.toString(), 1000).show();
								showData();

							}

						}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
						//Log.i("Error", error.toString());

					}
				});
				AppController.getInstance().addToRequestQueue(stringrequest);
				// Log.i("Instruction", stringrequest.toString());


			}
		});
	    builder.show();
	   
	   
		
	}
	
	private void sendInvoicePrint(String check) {
		
		String data=
				"UserId="+TempSales.getInstance().getWaiterId()
	    		+"&"+"orderno="+TempSales.getInstance().getOrderNo()
	    		+"&"+"isInvoice="+check
	    		+"&"+"cusid="+Order.getInstance().getPrvCusID();
	    StringRequest stringrequest = new StringRequest(Method.POST,
				baseUrl+"/dc/Api/Sales/UpdateInvoicePrintInstruction?"+data.replaceAll(" ","%20"),
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {

					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();

					}
				});
	    AppController.getInstance().addToRequestQueue(stringrequest);
	   // Log.i("Invoice Print", stringrequest.toString());
	
		
		
	}
	private void loadData(){
		proDialog.show();

		String data =
				"cusID=" + Order.getInstance().getPrvCusID()
						+ "&" + "pin=" + pinNo.getText().toString().trim();
		StringRequest stringrequest = new StringRequest(Method.GET,
				baseUrl + "/dc/Api/Customer/MemberVerification?" + data.replaceAll(" ", "%20"),
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						//Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG).show();

						try {
							JSONObject jsnObj = new JSONObject(response);
							boolean result = jsnObj.getBoolean("ResultState");
							String error = jsnObj.getString("Error");
							//Toast.makeText(getActivity(), "Error " + error, Toast.LENGTH_SHORT).show();

							if (result) {
								if (checkbox.isChecked()) {
									checkCommand = "N";

								} else {
									checkCommand = "Y";
								}

								sendInvoicePrint(checkCommand);
								Intent intent = new Intent(getActivity(), FinishingOrder.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
								startActivity(intent);
								getActivity().finish();

							} else {
								warning(error);

								//Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();
							}
							proDialog.dismiss();
						} catch (JSONException e) {
							Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
						}

					}


				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
				proDialog.dismiss();

			}
		});
		AppController.getInstance().addToRequestQueue(stringrequest);
		// Log.i("Pin Varification", stringrequest.toString());
		//Toast.makeText(getActivity(), stringrequest.toString(), Toast.LENGTH_SHORT).show();

	}
	private void sendPriority( int position,String priority){
		String data = "UserId=" + TempSales.getInstance().getWaiterId()
				+ "&" + "orderno=" + TempSales.getInstance().getOrderNo()
				+ "&" + "priority=" + priority
				+ "&" + "cusid=" + Order.getInstance().getPrvCusID()
				+ "&" + "itemId=" + ModifyOrder.getInstance().getItemIdList().get(position)
				+ "&" + "itemSl=" + String.valueOf(ModifyOrder.getInstance().getItemSLList().get(position));


		//Toast.makeText(getActivity(),ModifyOrder.getInstance().getItemIdList().get(position)+"and"+String.valueOf(ModifyOrder.getInstance().getItemSLList().get(position)),Toast.LENGTH_LONG).show();

		StringRequest stringrequest = new StringRequest(Method.POST,
				baseUrl + "/dc/Api/Sales/UpdatePriority?" + data.replaceAll(" ", "%20"),

				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {

						showData();


					}

				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
				//Log.i("Error", error.toString());

			}
		});
		AppController.getInstance().addToRequestQueue(stringrequest);
		// Log.i("Instruction", stringrequest.toString());
	}
}
