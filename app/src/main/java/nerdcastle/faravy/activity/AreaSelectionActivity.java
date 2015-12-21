package nerdcastle.faravy.activity;

import nerdcastle.faravy.info.AppController;
import nerdcastle.faravy.info.Order;
import nerdcastle.faravy.info.SessionManager;
import nerdcastle.faravy.info.TempSales;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class AreaSelectionActivity extends Activity implements OnClickListener {
	Button btnPool;
	Button btnGarden;
	Button btnBar;
	String getTableByAreaId = "/dc/Api/Table/GetTableByArea?areaName=";
	String baseUrl;
	private ProgressDialog proDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(
				Color.parseColor("#ff9800")); //0080FF
		ab.setBackgroundDrawable(colorDrawable);
		btnPool = (Button) findViewById(R.id.btnPool);
		btnGarden = (Button) findViewById(R.id.btnGarden);
		btnBar = (Button) findViewById(R.id.btnBar);

		proDialog = new ProgressDialog(this);
		proDialog.setMessage("Loading.....");
		proDialog.setCancelable(true);
		
		SessionManager session=new SessionManager(this);
		baseUrl=session.getUserData();
		
		btnPool.setOnClickListener(this);
		btnGarden.setOnClickListener(this);
		btnBar.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btnBar:
			String bar = "main bar";
			//selectArea(bar);
			areaSelection(bar);

			break;
		case R.id.btnGarden:
			String garden = "garden cafe";
			//selectArea(garden);
			areaSelection(garden);

			break;

		case R.id.btnPool:
			String pool = "pool";
			//selectArea(pool);
			areaSelection(pool);

			break;

		default:
			break;
		}
	}
	public void areaSelection(String area){
		Intent intent = (new Intent(
				getApplicationContext(),
				TableSelectionActivity.class));
		//intent.putExtra("area", area);
		Order.getInstance().setAreaName(area);
		TempSales.getInstance().setArea(area);
		startActivity(intent);
		
	}

	/*public void selectArea(String area) {
		StringRequest stringrequest = new StringRequest(Method.GET,
				baseUrl + getTableByAreaId + area.replaceAll(" ", "%20"),
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						try {
							JSONObject jObj = new JSONObject(response);
							String areaName = jObj.getString("AreaName");
							int tableNo = jObj.getInt("TableCount");
							
							TempSales.getInstance().setArea(areaName);
							
							
							Intent intent = (new Intent(
									getApplicationContext(),
									TableSelectionActivity.class));
							intent.putExtra("TableNo", tableNo);
							startActivity(intent);
							//finish();
							
							Toast.makeText(
									getApplicationContext(),
									areaName + " No of Table- "
											+ String.valueOf(tableNo),
									Toast.LENGTH_SHORT).show();

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
	}*/
	
	@Override
	public void onBackPressed() {
		//startActivity(new Intent(getApplicationContext(),LoginActivity.class));
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Do you want to log out?");
		builder.setIcon(R.drawable.ic_launcher);
		builder.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {


					public void onClick(DialogInterface dialog, int id) {

						String data = "UserId=" + TempSales.getInstance().getWaiterId();
						proDialog.show();
						StringRequest stringrequest = new StringRequest(Method.POST,
								baseUrl + "/dc/Api/Sales/RemoveAllData?" + data.replaceAll(" ", "%20"),
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

		
	}


}

