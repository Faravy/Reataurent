package nerdcastle.faravy.activity;

import java.util.ArrayList;

import nerdcastle.faravy.adapter.ExpListViewAdapter;
import nerdcastle.faravy.info.AppController;
import nerdcastle.faravy.info.ChildOfExpListView;
import nerdcastle.faravy.info.GroupOfExpListView;
import nerdcastle.faravy.info.Order;
import nerdcastle.faravy.info.OrderButton;
import nerdcastle.faravy.info.SessionManager;
import nerdcastle.faravy.info.TempSales;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class FinishingOrder extends Activity {
	
	Button sendkot;
	Button orderReview;
	Button continueOrder;
	Button modifyOrder;
	CheckBox isService;
	
	ArrayList<String> itemList;
	ArrayList<String> itemPriceList;
	ArrayList<Integer> quantityList;
	JSONArray jsonArray;
	TableRow tableRow=null;
	int i;
	View layout;
	String baseUrl;
	String isCheck="N";
	String orderno="";
	
	private ExpListViewAdapter expAdapter;
	private ExpandableListView expandList;
	private  ProgressDialog proDialog;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finisishing);
		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(
				Color.parseColor("#ff9800")); //0080FF
		ab.setBackgroundDrawable(colorDrawable);
		//ab.setDisplayHomeAsUpEnabled(true);

		isService=(CheckBox)findViewById(R.id.isService);
	    expandList = (ExpandableListView) findViewById(R.id.exp_list);

        proDialog = new ProgressDialog(this);
        proDialog.setMessage("Loading.....");
        proDialog.setCancelable(true);
		
		SessionManager session=new SessionManager(this);
		baseUrl=session.getUserData();
		
		/*sendkot=(Button) findViewById(R.id.sendKot);
		orderReview=(Button) findViewById(R.id.orderReview);
		continueOrder=(Button) findViewById(R.id.continueOrder);
		modifyOrder=(Button) findViewById(R.id.modifyOrder);*/
		
		orderno=TempSales.getInstance().getOrderNo();

		showOrderInList(orderno,isCheck);

		isService.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (isService.isChecked()){
					isCheck="Y";
					showOrderInList(orderno,isCheck);
				}
				else {
					isCheck="N";
					showOrderInList(orderno,isCheck);
				}

			}
		});

		
	}
	
	
	private void showOrderInList(String orderno,String isService) {
		proDialog.show();
		
		StringRequest stringRequest=new StringRequest(Method.GET, baseUrl+"/dc/Api/Sales/GetOrderPreviewByCustomers?orderNo="+orderno+"&isService="+isService,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						ArrayList<GroupOfExpListView> group_list = new ArrayList<GroupOfExpListView>();
                        ArrayList<ChildOfExpListView> ch_list;
						try {
							  
                              
                              
							jsonArray = new JSONArray(response);
							
							for (int i = 0; i < jsonArray.length(); i++) {

								GroupOfExpListView gru = new GroupOfExpListView();
								ch_list = new ArrayList<ChildOfExpListView>();
								String customerName = jsonArray.getJSONObject(i)
										.getString("CusName");
								
								
								JSONArray childArray=jsonArray.getJSONObject(i).getJSONArray("tempSales");
								
								for (int j = 0; j < childArray.length(); j++) {
									
									ChildOfExpListView ch = new ChildOfExpListView();
									
									String itemName=childArray.getJSONObject(j).getString("ItemName");
									String itemQuantity=childArray.getJSONObject(j).getString("QTY_Text");
									String unitPrice=childArray.getJSONObject(j).getString("RPU_Text");
							
									ch.setName(itemName);
									ch.setQuantity(itemQuantity);
									ch.setUnitPrice(unitPrice);
									
									ch_list.add(ch);
																									
								}
								
								gru.setName(customerName);
								gru.setItems(ch_list);
																	
								group_list.add(gru);								
							}
							
							 
							  expAdapter = new ExpListViewAdapter(
	                                    getApplicationContext(), group_list);
	                            
							  expandList.setAdapter(expAdapter);

	                            proDialog.dismiss();
							
						} catch (JSONException e) {
							
						}
												
					}
						
		}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						 proDialog.dismiss();
						
						
					}
				});
		AppController.getInstance().addToRequestQueue(stringRequest);
		
						
	}

	public void sendKot(View v) {

		isCheck="N";

		if (isService.isChecked()){
			isCheck="Y";
		}

		String data="UserId="+TempSales.getInstance().getWaiterId()
				+"&"+"IsService="+isCheck;
	    StringRequest stringrequest = new StringRequest(Method.POST,
				baseUrl+"/dc/Api/Sales/SubmitFinalSalesData?"+data.replaceAll(" ","%20"),
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
					//Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();	
						startActivity(new Intent(getApplicationContext(),
								AreaSelectionActivity.class));
						finish();
						Log.i("Send Kot", response.toString());
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();

					}
				});
	    AppController.getInstance().addToRequestQueue(stringrequest);
	   // Log.i("Send Kot", stringrequest.toString());

	}

	public void continueOrder(View v) {
		startActivity(new Intent(getApplicationContext(),
				MemberSelectionActivity.class));//MemberTypeActivity
		Order.getInstance().setCheck(0);
		finish();

	}

	public void modifyOrder(View v) {
		tableRow = new TableRow(getApplicationContext());
		proDialog.show();

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(" Select Member ");
		LayoutInflater inflater = (LayoutInflater) getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		layout = inflater.inflate(R.layout.modify_user, null);
		final LinearLayout addonLayout = (LinearLayout) layout
				.findViewById(R.id.modifyuser);
		String data=
	    		"orderNo="+TempSales.getInstance().getOrderNo();
		StringRequest stringrequest = new StringRequest(Method.GET,
				baseUrl+"/dc/Api/Sales/GetOrderCustomers?"+data.replaceAll(" ","%20"),
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						//Toast.makeText(getApplicationContext(), response.toString(), 1000).show();
						//Log.i("modify", response.toString());
						try {
							
							jsonArray = new JSONArray(response);
							for ( i = 0; i < jsonArray.length(); i++) {

								final String customerName=jsonArray.getJSONObject(i).getString("CustomerName");
								final String cardNo=jsonArray.getJSONObject(i).getString("CardNo");
								//final String cusId=jsonArray.getJSONObject(i).getString("PrvCusID");



                                //Toast.makeText(getApplicationContext(),customerName,Toast.LENGTH_SHORT).show();

								final OrderButton orderBtn = new OrderButton(
										getApplicationContext());
								orderBtn.setId(i);
								
								orderBtn.setText(customerName);
								orderBtn.setTextColor(Color
										.parseColor("#FFFFFF"));
								orderBtn.setBackgroundResource((R.drawable.selector));
								orderBtn.setOnClickListener(new OnClickListener() {
									int check = 1;


									@Override
									public void onClick(View v) {
										v.setSelected(true);

										if (check == 1) {
											v.setBackgroundResource(R.drawable.selector);

											check = 0;
											//Toast.makeText(getApplicationContext(), addonsList.get(addonsList.size()-1), 1000).show();
										} else {
											v.setBackgroundResource(R.drawable.buttontable);

											check = 1;
											// Toast.makeText(getActivity(), String.valueOf(addonsList), 1000).show();
										}


										getMemberInformation(cardNo);
									}
								});
								//addonLayout.removeAllViews();
								//tableRow.addView(orderBtn);
								addonLayout.addView(orderBtn);
								proDialog.dismiss();
							}

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
	
		builder.show();

	}
	
	protected void getMemberInformation(final String strcardNo) {
		proDialog.show();

		StringRequest stringrequest = new StringRequest(Method.GET,
				baseUrl+"/dc/Api/Customer/GetMemberByID?memberID="
						+strcardNo,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
			
						Log.i("MemberInfo", response.toString());
						try {
							

	    						JSONObject jObj = new JSONObject(
										response);
								String cusCategory = jObj
										.getString("CusCategory");
								Order.getInstance().setCardNo(strcardNo);
								Order.getInstance().setCusCategory(cusCategory);
								String cusName = jObj
										.getString("CusName");
								Order.getInstance().setCustomerName(cusName);
								String cusBalance = jObj
										.getString("BalanceAmount");
							    String tempSaleAmount=jObj.getString("TempSaleAmount");

							    String currentNalance=String.valueOf(Double.parseDouble(cusBalance)+Double.parseDouble(tempSaleAmount));
								Order.getInstance().setBalance(currentNalance);

							    String prvCusID = jObj
										.getString("PrvCusID");
								Order.getInstance().setPrvCusID(prvCusID);
								
								String cardNo=jObj
										.getString("CardNo");
								Order.getInstance().setCardNo(cardNo);

							    String creditLimit=jObj.getString("CreditLimit");

							    String todaysSale=jObj.getString("ToDaysSale");

							    Order.getInstance().setCreditLimit(creditLimit);
							    Order.getInstance().setTodaysSale(todaysSale);

								getAreAandTable(prvCusID);

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
		//Log.i("Member Info", stringrequest.toString());
      
		
	}

	private void getAreAandTable(String customerId) {
		String userId=TempSales.getInstance().getWaiterId();
		//String customerId=Order.getInstance().getPrvCusID();

		StringRequest stringrequest = new StringRequest(Method.GET,
				baseUrl+"/dc/Api/Sales/SelectAllByUserIDCustomerID?userID="+userId+"&customerId="+customerId,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						try {
							jsonArray = new JSONArray(response);
							String areaName=jsonArray.getJSONObject(0).getString("Area");
							String tableNo=jsonArray.getJSONObject(0).getString("TblNo");
							TempSales.getInstance().setArea(areaName);
							TempSales.getInstance().setTableNo(tableNo);

							startActivity(new Intent(getApplicationContext(), SalesWindowActivity.class));
							Order.getInstance().setCheck(1);
							finish();

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();

			}
		});
		AppController.getInstance().addToRequestQueue(stringrequest);
		// Log.i("Instruction", stringrequest.toString());

	}

	public void orderReview(View v) {

		String data=
	    		"oderno="+TempSales.getInstance().getOrderNo()	    		
	    		+"&"+"cusid="+Order.getInstance().getPrvCusID();
	    StringRequest stringrequest = new StringRequest(Method.POST,
				baseUrl+"/dc/Api/Sales/SetPrintPreviewCommand?"+data.replaceAll(" ","%20"),
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {

						//Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
						//Log.i("OrderReview", response.toString());

					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();

					}
				});
	    AppController.getInstance().addToRequestQueue(stringrequest);
	   // Log.i("Instruction", stringrequest.toString());
		
	}


	@Override
	public void onBackPressed() {
		
		
	}
	

}
