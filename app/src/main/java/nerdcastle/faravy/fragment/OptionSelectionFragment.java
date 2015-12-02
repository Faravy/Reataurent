package nerdcastle.faravy.fragment;

import java.util.ArrayList;
import java.util.List;

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
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class OptionSelectionFragment extends Fragment implements
		OnClickListener {
	View rootView;
	TableLayout tableLayout;
	TableRow tableRow = null;
	JSONArray jsonArray;
	OrderButton orderBtn;
	int enabledButton;
	int itemId;
	String getGroupByItemId = "/dc/Api/ItemsOptions/GetGroupByItemID?itemID=";
	String getAllOptions = "/dc/Api/ItemsOptions/GetAllOptionByItemGroupID?itemID=";
	private ProgressDialog pDialog;
	int i;
	int optionCount = 1;
	String baseUrl;

	ArrayList<String> optionsNameList;
	ArrayList<String> optionsIdList;
	ArrayList<String> optionsGroupList;
	ArrayList<String> selectedOptionList;

	Button submitOption;
	Button cancelOption;

	List<String> minValueList = new ArrayList<String>();

	List<String> maxValueList = new ArrayList<String>();
	int btnClickCount = 0;
	int quantity = 0;
	int inputQuantity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_option_selection,
				container, false);
		tableLayout = (TableLayout) rootView.findViewById(R.id.options);
		tableRow = (TableRow) rootView.findViewById(R.id.tableRow);
		SessionManager session = new SessionManager(getActivity());
		baseUrl = session.getUserData();

		submitOption = (Button) rootView.findViewById(R.id.submitOption);
		cancelOption = (Button) rootView.findViewById(R.id.cancelOption);
		submitOption.setOnClickListener(this);
		cancelOption.setOnClickListener(this);

		itemId = Order.getInstance().getItemId();
		pDialog = new ProgressDialog(getActivity());
		pDialog.setCancelable(false);

		optionsNameList = new ArrayList<String>();
		optionsIdList = new ArrayList<String>();
		optionsGroupList = new ArrayList<String>();
		selectedOptionList = new ArrayList<String>();
		getGroupName(itemId);
		// showOptionNew(itemId);
		return rootView;

	}

	private void getGroupName(final int itemId) {

		pDialog.setMessage("Loading.....");

		showDialog();

		StringRequest stringrequest = new StringRequest(Method.GET, baseUrl
				+ getGroupByItemId + itemId, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				hideDialog();
				try {
					jsonArray = new JSONArray(response);

					TempOptionData.groupId.clear();
					TempOptionData.clickItem.clear();
					TempOptionData.minValue.clear();
					TempOptionData.maxValue.clear();
					TempOptionData.urlList.clear();
					for (i = 0; i < jsonArray.length(); i++) {
						final String groupName = jsonArray.getJSONObject(i)
								.getString("GroupName");

						TempOptionData.groupId.add(groupName);
						TempOptionData.clickItem.add(0);

						int maxOption = jsonArray.getJSONObject(i).getInt(
								"MaxOption");
						int minOption = jsonArray.getJSONObject(i).getInt(
								"MinOption");
						TempOptionData.minValue.add(minOption);
						TempOptionData.maxValue.add(maxOption);

						showOption(itemId, groupName);

					}

				} catch (JSONException e) {

					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				hideDialog();

			}
		});

		AppController.getInstance().addToRequestQueue(stringrequest);
	}


	protected void showOption(int itemId, final String groupName) {

		pDialog.setMessage("Loading.....");
		showDialog();
		final StringRequest stringrequest = new StringRequest(Method.GET,
				baseUrl + getAllOptions + itemId + "&groupID=" + groupName,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						hideDialog();
						try {
							jsonArray = new JSONArray(response);
							if (jsonArray.length() > 0) {
								String min = "";
								String max = "";
								int maxOption = jsonArray.getJSONObject(0)
										.getInt("MaxOption");
								int minOption = jsonArray.getJSONObject(0)
										.getInt("MinOption");
								min = String.valueOf(minOption);
								max = String.valueOf(maxOption);

								TableRow titleRow = new TableRow(getActivity());
								TextView tv = new TextView(getActivity());
								tv.setTextSize(20);
								tv.setText("Option " + optionCount + ": Min: "
										+ min + ", Max: " + max);
								titleRow.addView(tv);

								tableLayout.addView(titleRow);

								optionCount++;
							}

							for (i = 0; i < jsonArray.length(); i++) {
								final String optionName = jsonArray
										.getJSONObject(i).getString(
												"OptionsName");

								final String optionsId = jsonArray
										.getJSONObject(i)
										.getString("OptionsID");

								if (i % 2 == 0) {
									tableRow = new TableRow(getActivity());
									tableLayout.addView(tableRow);
								}

								orderBtn = new OrderButton(getActivity());

								orderBtn.setText(optionName);
								orderBtn.setTextColor(Color
										.parseColor("#FFFFFF"));
								orderBtn.setBackgroundResource((R.drawable.selector));
								tableRow.addView(orderBtn);
								
								
								orderBtn.setOnClickListener(new OnClickListener() {

									

									@Override
									public void onClick(View v) {
										
										if (!v.isSelected()) {
											
											TempSales.getInstance()
													.setOptionName(optionName);
											TempSales.getInstance()
													.setOptionsID(optionsId);
											TempSales.getInstance()
													.setOptionGroupName(
															groupName);

											Toast.makeText(getActivity(),
													optionName + "  Selected",
													Toast.LENGTH_SHORT).show();

											int pos = 0;
											for (int i = 0; i < TempOptionData.groupId
													.size(); i++) {
												if (TempOptionData.groupId
														.get(i).equals(
																groupName)) {
													pos = i;
													break;
												}
											}

											int tempClick = TempOptionData.clickItem
													.get(pos);
											TempOptionData.clickItem.set(pos,
													tempClick + 1);
											quantity = 1;
											sendOptionInfo();
											
											v.setSelected(true);
											v.setBackgroundResource(R.drawable.selector);
											
										} else {
											TempSales.getInstance()
													.setOptionName(optionName);
											TempSales.getInstance()
													.setOptionsID(optionsId);
											TempSales.getInstance()
													.setOptionGroupName(
															groupName);

											
											((OrderButton) v).setText(optionName);

											Toast.makeText(
													getActivity(),
													optionName + "  Deselected",
													Toast.LENGTH_SHORT).show();

											

																						

												sendDeselectOptionInfo();

											v.setSelected(false);
											v.setBackgroundResource(R.drawable.buttontable);
											
										}
									}
								});

								orderBtn.setOnLongClickListener(new OnLongClickListener() {
									

									@Override
									public boolean onLongClick(final View v) {

										TempSales.getInstance().setOptionName(
												optionName);
										TempSales.getInstance().setOptionsID(
												optionsId);
										TempSales.getInstance()
												.setOptionGroupName(groupName);

										AlertDialog.Builder builder = new AlertDialog.Builder(
												getActivity());

										builder.setTitle("Put your quantity");

										final EditText editText = new EditText(
												getActivity());

										editText.setInputType(InputType.TYPE_CLASS_NUMBER);

										builder.setView(editText);

										builder.setPositiveButton(
												"Ok",
												new DialogInterface.OnClickListener() {
													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {

														int pos = 0;
														for (int i = 0; i < TempOptionData.groupId
																.size(); i++) {
															if (TempOptionData.groupId
																	.get(i)
																	.equals(groupName)) {
																pos = i;
																break;
															}
														}

														
														String data=editText.getText().toString().trim();
					
														if (data.length() ==0) {
															Toast.makeText(
																	getActivity(),
																	"Invalid quantity",
																	Toast.LENGTH_SHORT)
																	.show();

														} else {
															int inputQuantity = editText
																	.getText()
																	.toString() == "" ? 0
																	: Integer
																			.parseInt(editText
																					.getText()
																					.toString());
													
															quantity = inputQuantity;
															// remove previous data with same option id
															if(quantity>0)
															sendDeselectOptionInfo();
										
															((OrderButton) v).setText(optionName
																	+ " - "
																	+ String.valueOf(inputQuantity));
															//((OrderButton) v).setTextColor(Color.parseColor("#000000"));
															

															Toast.makeText(
																	getActivity(),
																	String.valueOf(inputQuantity)
																			+ " "
																			+ optionName
																			+ " Selected",
																	Toast.LENGTH_SHORT)
																	.show();
															
															int tempClick = TempOptionData.clickItem
																	.get(pos);

															TempOptionData.clickItem
																	.set(pos,tempClick+inputQuantity);

															
															for (int i = 0; i < inputQuantity; i++) {

																sendOptionInfo();

															}
															v.setSelected(true);															
															v.setBackgroundResource(R.drawable.selector);
															

														}
													}
												});

										builder.show();
										return false;
									}

								});

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


	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.submitOption:

			boolean validation = false;

			for (int i = 0; i < TempOptionData.groupId.size(); i++) {

				if ((TempOptionData.clickItem.get(i) >= TempOptionData.minValue
						.get(i))
						&& (TempOptionData.clickItem.get(i) <= TempOptionData.maxValue
								.get(i))) {
					validation = true;
				} else {
					validation = false;
					break;
				}
			}

			if (validation) {
				for (int i = 0; i < TempOptionData.urlList.size(); i++) {
					showDialog();
					StringRequest stringRequest = new StringRequest(
							Method.POST, baseUrl
									+ "/dc/Api/Sales/AddOptions?"
									+ TempOptionData.urlList.get(i)
											.replaceAll(" ", "%20"),
							new Response.Listener<String>() {

								@Override
								public void onResponse(String response) {

									// Log.i("response",response.toString());
									hideDialog();

									try {

										JSONObject jsonObj = new JSONObject(
												response);

										boolean result = jsonObj
												.getBoolean("ResultState");

										if (result) {

											FragmentManager fm = getFragmentManager();
											FragmentTransaction ft = fm
													.beginTransaction();
											ConfirmOrderFragment myFragment = new ConfirmOrderFragment();
											ft.replace(R.id.fragment,
													myFragment);
											ft.addToBackStack(null);
											ft.commit();
										}

									} catch (JSONException e) {
										e.printStackTrace();

									}

								}
							},

							new Response.ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError error) {
									Toast.makeText(getActivity(),
											error.toString(), Toast.LENGTH_LONG)
											.show();
									hideDialog();
									// Log.i("Error", error.toString());

								}
							});
					AppController.getInstance()
							.addToRequestQueue(stringRequest);
					Log.i("OPtion URL", stringRequest.toString());

				}
			}

			else {

				Toast.makeText(getActivity(), "Your Selection is Wrong",
						Toast.LENGTH_LONG).show();
				// GroupCollection.urlList.clear();
			}

			break;

		case R.id.cancelOption:
			cancel();
			break;
		default:
			break;
		}

	}

	private void cancel() {

		String data = "UserId=" + TempSales.getInstance().getWaiterId() + "&"
				+ "cusID=" + Order.getInstance().getPrvCusID() + "&"
				+ "ItemID="
				+ String.valueOf(TempSales.getInstance().getItemID()) + "&"
				+ "ItemSL=" + TempSales.getInstance().getItemSL() + "&"
				+ "AddonsID=" + "";

		showDialog();
		StringRequest stringRequest = new StringRequest(Method.POST, baseUrl
				+ "/dc/Api/Sales/RemoveItemAddons?"
				+ data.replaceAll(" ", "%20"), new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				// Toast.makeText(getActivity(),response.toString(),
				// 1000).show();
				hideDialog();
				try {
					JSONObject jsonObj = new JSONObject(response);
					boolean result = jsonObj.getBoolean("ResultState");
					if (result == true) {
						FragmentManager fm = getFragmentManager();
						FragmentTransaction ft = fm.beginTransaction();
						Itemfragment myFragment = new Itemfragment();
						ft.replace(R.id.fragment, myFragment);
						ft.addToBackStack(null);
						ft.commit();
					}

				} catch (JSONException e) {

					e.printStackTrace();
				}

			}
		},

		new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(getActivity(), error.toString(),
						Toast.LENGTH_SHORT).show();
				hideDialog();
				// Log.i("Error", error.toString());

			}
		});
		AppController.getInstance().addToRequestQueue(stringRequest);
		Log.i("Cancel", stringRequest.toString());

	}

	private void sendOptionInfo() {
		// Toast.makeText(getActivity(),String.valueOf(quantity) , 1000).show();
		String data = "CPU="
				+ String.valueOf(TempSales.getInstance().getCpu())
				+ "&"
				+ "HHApplicable="
				+ TempSales.getInstance().getIsHappyHourPrice()
				+ "&"
				+ "IsOptionsPrice="
				+ TempSales.getInstance().getIsOptionprice()
				+ "&"
				+ "Item_HHPrice="
				+ "0" // String.valueOf(TempSales.getInstance().getHrPrice())
				+ "&"
				+ "Item_RegularPrice="
				+ "0" // String.valueOf(TempSales.getInstance().getItem_RegularPrice())
				+ "&" + "Item_id=" + String.valueOf(itemId) + "&"
				+ "OptionName=" + TempSales.getInstance().getOptionName() + "&"
				+ "OptionGroupName="
				+ TempSales.getInstance().getOptionGroupName() + "&"
				+ "OptionsID=" + TempSales.getInstance().getOptionsID() + "&"
				+ "PrvCusID=" + Order.getInstance().getPrvCusID() + "&"
				+ "CusName=" + Order.getInstance().getCustomerName() + "&"
				+ "_TableNo=" + TempSales.getInstance().getTableNo() + "&"
				+ "WaiterID=" + TempSales.getInstance().getWaiterId() + "&"
				+ "vatPercent=" + TempSales.getInstance().getVatAmount() + "&"
				+ "AreaName=" + TempSales.getInstance().getArea() + "&"
				+ "ItemSL=" + TempSales.getInstance().getItemSL() + "&"
				+ "QTY=" + String.valueOf(quantity);

		TempOptionData.urlList.add(data);
		//Log.i("URL",TempOptionData.urlList.toString());
	}

	private void showDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hideDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}

	private void sendDeselectOptionInfo() {
		
		String optionParticular ="OptionsID=" + TempSales.getInstance().getOptionsID() ;
		
		int removeCount = 0;
		
		for (int count = 0; count < TempOptionData.urlList.size(); count++)
		{			
			
			if (TempOptionData.urlList.get(count).contains(optionParticular)) {
				
				TempOptionData.urlList.remove(count);
				removeCount++;
				count--;				
				}	
		}
		
		
		int pos = 0;
		for (int i = 0; i < TempOptionData.groupId
				.size(); i++) {
			if (TempOptionData.groupId
					.get(i).equals(TempSales.getInstance().getOptionGroupName())) {
				pos = i;
				break;
			}
		}

		int tempClick = TempOptionData.clickItem
				.get(pos);
		
		Log.i("Temp Click" ,String.valueOf(tempClick));
		
		int finalCount = tempClick - removeCount;
		
		if(finalCount<0)
			finalCount = 0;
		
		Log.i("Group name : ", TempSales.getInstance().getOptionGroupName());
		Log.i("Removable Count : ", String.valueOf(finalCount));

		TempOptionData.clickItem.set(pos,finalCount);
		
	
	}

}
