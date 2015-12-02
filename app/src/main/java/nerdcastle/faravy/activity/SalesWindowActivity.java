package nerdcastle.faravy.activity;

import nerdcastle.faravy.adapter.ImageAdapter;
import nerdcastle.faravy.fragment.CategoryFragment;
import nerdcastle.faravy.fragment.ConfirmOrderFragment;
import nerdcastle.faravy.info.AppController;
import nerdcastle.faravy.info.Order;
import nerdcastle.faravy.info.SessionManager;
import nerdcastle.faravy.info.TempSales;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.StringRequest;

public class SalesWindowActivity extends Activity  {
	TextView name;
	TextView balance;
	TextView cusCatagory;
	TextView ratio;
	TextView area_table;
	ImageView cusImage;

	GridView gridView;
	ImageAdapter adapter;
	int enabledButton;
	String getCusImage = "/dc/Api/Customer/GetMemberImage?memberID=";
	LinearLayout linearLayout;
	String baseUrl;
	private ProgressDialog proDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sales_window);
		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(
				Color.parseColor("#ff9800"));//0080FF
		ab.setBackgroundDrawable(colorDrawable);
		ab.setDisplayHomeAsUpEnabled(true);

		SessionManager session = new SessionManager(this);
		baseUrl = session.getUserData();
		
		proDialog = new ProgressDialog(this);
        proDialog.setMessage("Loading.....");
        proDialog.setCancelable(true);

		cusImage = (ImageView) findViewById(R.id.customerImage);
		name = (TextView) findViewById(R.id.name);
		balance = (TextView) findViewById(R.id.balance);
		cusCatagory = (TextView) findViewById(R.id.memberType);
		ratio = (TextView) findViewById(R.id.ratio);
		area_table = (TextView) findViewById(R.id.area_table);

		name.setText(Order.getInstance().getCustomerName());
		balance.setText(Order.getInstance().getBalance());
		cusCatagory.setText(Order.getInstance().getCusCategory());
		ratio.setText(Order.getInstance().getCreditLimit()+"/"+Order.getInstance().getTodaysSale());
		area_table.setText(TempSales.getInstance().getArea() + "/" + TempSales.getInstance().getTableNo());

		linearLayout = (LinearLayout) findViewById(R.id.fragment);

		int check = Order.getInstance().getCheck();
		if (check == 1) {
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ConfirmOrderFragment myFragment = new ConfirmOrderFragment();
			ft.add(R.id.fragment, myFragment);
			ft.commit();
			Order.getInstance().setCheck(0);

		} else {

			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			CategoryFragment myFragment = new CategoryFragment();
			ft.add(R.id.fragment, myFragment);
			ft.commit();
		}

		showImage();

	}

	@Override
	protected void onResume() {
		//linearLayout.removeAllViews();
		getVatAmount();

		super.onResume();
	}

	private void getVatAmount() {
		StringRequest stringRequest = new StringRequest(Method.GET, baseUrl
				+ "/dc/Api/Global/GetGlobalSetup",
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {

						try {
							JSONObject jsonObj = new JSONObject(response);
							double vatAmount = jsonObj.getDouble("VATPrcnt");
							TempSales.getInstance().setVatAmount(vatAmount);

						} catch (JSONException e) {

							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {

					}
				});
		AppController.getInstance().addToRequestQueue(stringRequest);

	}

	// for itemselection fragment onclick
	public void confirm(View v) {

		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ConfirmOrderFragment myFragment = new ConfirmOrderFragment();
		ft.replace(R.id.fragment, myFragment);
		ft.addToBackStack(null);
		ft.commit();
	}

	private void showImage() {

		ImageLoader imageLoader = AppController.getInstance().getImageLoader();

		String customerId = Order.getInstance().getCardNo();

		imageLoader.get(baseUrl + getCusImage + customerId,
				new ImageListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
					}

					@Override
					public void onResponse(ImageContainer response, boolean arg1) {
						if (response.getBitmap() != null) {
							// load image into imageview
							cusImage.setImageBitmap(response.getBitmap());
						}
					}
				});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		if (id == R.id.logout) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Do you want to log out?");
			builder.setIcon(R.drawable.ic_launcher);
			builder.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						

						public void onClick(DialogInterface dialog, int id) {
							
							String data="UserId="+TempSales.getInstance().getWaiterId();
						    proDialog.show();
							StringRequest stringrequest = new StringRequest(Method.POST,
									baseUrl+"/dc/Api/Sales/RemoveAllData?"+data.replaceAll(" ","%20"),
									new Response.Listener<String>() {

										@Override
										public void onResponse(String response) {
											proDialog.dismiss();
											startActivity(new Intent(getApplicationContext(),
													LoginActivity.class));
											finish();
										
										}

									}, new Response.ErrorListener() {

										@Override
										public void onErrorResponse(VolleyError error) {
											Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
											proDialog.dismiss();

										}
									});
						    AppController.getInstance().addToRequestQueue(stringrequest);
							
						}
					});
			builder.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});
			builder.create().show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onBackPressed() {

	}

}
