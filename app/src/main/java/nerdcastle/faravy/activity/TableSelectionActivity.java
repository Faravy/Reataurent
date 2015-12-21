package nerdcastle.faravy.activity;

import nerdcastle.faravy.info.AppController;
import nerdcastle.faravy.info.Order;
import nerdcastle.faravy.info.OrderButton;
import nerdcastle.faravy.info.SessionManager;
import nerdcastle.faravy.info.TempSales;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class TableSelectionActivity extends Activity {
	TableLayout layout;
	TableRow tableRow = null;
	int enabledButton;
	int tableNo;
	String area;
	String getTableByAreaId = "/dc/Api/Table/GetTableByArea?areaName=";
	JSONArray jsonArray;
	String baseUrl;
	private ProgressDialog proDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_table_selection);
		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(
				Color.parseColor("#ff9800"));  //347C17
		ab.setBackgroundDrawable(colorDrawable);
		ab.setDisplayHomeAsUpEnabled(true);

		proDialog = new ProgressDialog(this);
		proDialog.setMessage("Loading.....");
		proDialog.setCancelable(true);
		
		SessionManager session=new SessionManager(this);
		baseUrl=session.getUserData();
		
		layout = (TableLayout) findViewById(R.id.categoryItem);
		//tableNo = getIntent().getIntExtra("TableNo", 0);
		area= Order.getInstance().getAreaName(); //getIntent().getStringExtra("area");
		showTable(area);

		/*for (int i = 0; i < tableNo; i++) {
			if (i % 3 == 0) {
				tableRow = new TableRow(this);
				layout.addView(tableRow);
			}
			final Button btnTable = new Button(this);
			btnTable.setId((i));
			btnTable.setText("Table - " + (i + 1));
			btnTable.setTextColor(Color.parseColor("#FFFFFF"));
			btnTable.setBackgroundResource((R.drawable.selector));
			tableRow.addView(btnTable);
			btnTable.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					view.setSelected(true);
					enabledButton = view.getId();
					deselectButtons();
					
					TempSales.getInstance().setTableNo(btnTable.getText().toString());
					startActivity(new Intent(getApplicationContext(),
							MemberTypeActivity.class));
					Toast.makeText(getApplicationContext(),
							btnTable.getText() + " Selected",
							Toast.LENGTH_SHORT).show();
				}
			});
		}*/
	}

	
	
	
	
	private void showTable(String area) {
		proDialog.show();
		StringRequest stringRequest=new StringRequest(Method.GET, baseUrl + getTableByAreaId + area.replaceAll(" ", "%20"), 
				new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				
				try {
					jsonArray=new JSONArray(response);
					for (int i = 0; i < jsonArray.length(); i++) {
						String tableName=jsonArray.getJSONObject(i).getString("TableName");
						int tableId=jsonArray.getJSONObject(i).getInt("TableID");
						
						if (i % 3 == 0) {
							tableRow = new TableRow(getApplicationContext());
							layout.addView(tableRow);
						}
						TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT,1);

						final OrderButton btnTable = new OrderButton(getApplicationContext());
						btnTable.setId((i));
						btnTable.setIntId(tableId);
						btnTable.setText(tableName);
						btnTable.setLayoutParams(params);
						btnTable.setTextColor(Color.parseColor("#FFFFFF"));
						btnTable.setBackgroundResource((R.drawable.selector));						
						btnTable.setOnClickListener(new View.OnClickListener() {
							public void onClick(View view) {
								view.setSelected(true);
								enabledButton = view.getId();
								deselectButtons();
								
								TempSales.getInstance().setTableNo(btnTable.getText().toString());
								startActivity(new Intent(getApplicationContext(),
										MemberSelectionActivity.class)); //MemberTypeActivity
								Toast.makeText(getApplicationContext(),
										btnTable.getText() + " Selected",
										Toast.LENGTH_SHORT).show();
							}
						});
						tableRow.addView(btnTable);
					}
					proDialog.dismiss();
					
				} catch (JSONException e) {
					
				}
			
				
			}
		}, new Response.ErrorListener() {
			

			@Override
			public void onErrorResponse(VolleyError error) {
				proDialog.dismiss();
				Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
								
			}
		});
		AppController.getInstance().addToRequestQueue(stringRequest);		
	}

	public void deselectButtons() {
		for (int i = 0; i < jsonArray.length(); i++) {
			if (enabledButton != i)
				this.findViewById(i).setSelected(false);
		}
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.logout) {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Do you want to log out?");
			builder.setIcon(R.drawable.ic_launcher);
			builder.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							startActivity(new Intent(getApplicationContext(),
									LoginActivity.class));
							//finish();
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
	  
	  startActivity(new Intent(getApplicationContext(),AreaSelectionActivity.class));
	  
	}*/
}
