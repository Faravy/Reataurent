package nerdcastle.faravy.activity;

import nerdcastle.faravy.info.AppController;
import nerdcastle.faravy.info.Order;
import nerdcastle.faravy.info.OrderButton;
import nerdcastle.faravy.info.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class MemberSelectionActivity extends Activity {
	EditText etCardNo;
	Button btnSubmit;
	boolean isTrue;
	String cardNo;
	int id;
	String getIsMemberTypeValid = "/dc/Api/Customer/GetIsMemberTypeValid?memberID=";
	String baseUrl;
	View layout;
	protected JSONArray jsonArray;
	TableRow tableRow=null;
	private ProgressDialog proDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_selection);
		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#ff9800"));
		 ab.setBackgroundDrawable(colorDrawable);
		 ab.setDisplayHomeAsUpEnabled(true);
		// showInputDialog();
		proDialog = new ProgressDialog(this);
		proDialog.setMessage("Loading.....");
		proDialog.setCancelable(true);
		SessionManager session=new SessionManager(this);
		baseUrl=session.getUserData();
		doNothing();
		etCardNo = (EditText) findViewById(R.id.cardNo);
		etCardNo.setTransformationMethod(PasswordTransformationMethod.getInstance());
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//id = getIntent().getStringExtra("id");
				//id=Order.getInstance().getMemberTypeId();
				cardNo = etCardNo.getText().toString().trim();
				if (cardNo.length() > 0) {
					validateMemberWithType(cardNo);

				} else
					Toast.makeText(getApplicationContext(),
							"Please enter card no", Toast.LENGTH_LONG).show();
			}
		});

	}

	protected void validateMemberWithType( final String cardNo) {
		proDialog.show();
		StringRequest stringRequest = new StringRequest(Method.GET,
				baseUrl+getIsMemberTypeValid
						+ cardNo ,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						try {
							//Log.i("modify", response.toString());
							JSONObject jObj = new JSONObject(response);
							String error = jObj.getString("Error");
							boolean result = jObj.getBoolean("ResultState");
							
							String count=jObj.getString("Counts");
							
							int memberCount=Integer.parseInt(count);
						
							if (memberCount==1) {
								
								getMemberInformation(cardNo);
							}
								
							else if (memberCount>1) {
								showMemberDetails(cardNo);
									
							} 
							 else {
								warning(error);
								//Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
							}
							proDialog.dismiss();
						} 
						catch (JSONException e) {

							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						proDialog.dismiss();
					}
				});

		AppController.getInstance().addToRequestQueue(stringRequest);
		//Log.i("Validity", stringRequest.toString());
		
		
	}

	private void warning(String message){

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
	
	

	protected void showMemberDetails(String cardNo) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(" Select Member ");
		LayoutInflater inflater = (LayoutInflater) getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		layout = inflater.inflate(R.layout.addons, null);
		final TableLayout addonLayout = (TableLayout) layout
				.findViewById(R.id.tablelayout);
		proDialog.show();
		StringRequest stringrequest = new StringRequest(Method.GET,
				baseUrl+"/dc/Api/Customer/GetALLMemberByID?memberID="+cardNo,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						//Toast.makeText(getApplicationContext(), response.toString(), 5000).show();
						//Log.i("modify", response.toString());
						try {
							
							jsonArray = new JSONArray(response);
							for (int i = 0; i < jsonArray.length(); i++) {

								final String cusCategory = jsonArray.getJSONObject(i)
										.getString("CusCategory");
								
								
								final String cusName = jsonArray.getJSONObject(i)
										.getString("CusName");
								
								
							    final	String cusBalance = jsonArray.getJSONObject(i)
										.getString("BalanceAmount");
								
								
								final String prvCusID = jsonArray.getJSONObject(i)
										.getString("PrvCusID");
								
								
								final String cardNo=jsonArray.getJSONObject(i)
										.getString("CardNo");

								final String creditLimit=jsonArray.getJSONObject(i)
										.getString("CreditLimit");

								final String todaysSale=jsonArray.getJSONObject(i)
										.getString("ToDaysSale");
																
								if (i % 2 == 0) {
									tableRow = new TableRow(
											getApplicationContext());
									addonLayout.addView(tableRow);
								}

								final OrderButton orderBtn = new OrderButton(
										getApplicationContext());
								
								orderBtn.setText(cusName);
								orderBtn.setTextColor(Color
										.parseColor("#FFFFFF"));
								orderBtn.setBackgroundResource((R.drawable.selector));
								orderBtn.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										v.setSelected(true);
									
										Order.getInstance().setCusCategory(cusCategory);
										Order.getInstance().setCustomerName(cusName);
										Order.getInstance().setBalance(cusBalance);
										Order.getInstance().setPrvCusID(prvCusID);
										Order.getInstance().setCardNo(cardNo);
										Order.getInstance().setCreditLimit(creditLimit);
										Order.getInstance().setTodaysSale(todaysSale);
																				
										Intent intent = (new Intent(
												getApplicationContext(),
												SalesWindowActivity.class));
										//Toast.makeText(getApplicationContext(),Order.getInstance().getCardNo(),Toast.LENGTH_SHORT).show();
										startActivity(intent); 	
										finish();
										 									
									}
								});
								tableRow.addView(orderBtn);
							}
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
	
		builder.show();
		
	}

	protected void getMemberInformation(final String cardNo) {
		proDialog.show();
		StringRequest stringrequest = new StringRequest(Method.GET,
				baseUrl+"/dc/Api/Customer/GetMemberByID?memberID="
						+cardNo,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						try {
							if (response.toString() == null
									|| response.toString() == "") {
								Toast.makeText(getApplicationContext(), "Member card is not found", Toast.LENGTH_LONG).show();

							} else {

	    							JSONObject jObj = new JSONObject(response);
								String cusCategory = jObj.getString("CusCategory");

								Order.getInstance().setCusCategory(cusCategory);
								
								String cusName = jObj
										.getString("CusName");
								Order.getInstance().setCustomerName(cusName);
								
								String cusBalance = jObj
										.getString("BalanceAmount");
								Order.getInstance().setBalance(cusBalance);
								
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

								Intent intent = (new Intent(getApplicationContext(), SalesWindowActivity.class));
								
								startActivity(intent);
								finish();
							}
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
	public  void doNothing(){
		//hfgfgfgfjnh
	}


	
}
