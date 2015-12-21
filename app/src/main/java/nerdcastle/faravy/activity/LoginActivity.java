package nerdcastle.faravy.activity;

import nerdcastle.faravy.info.AppController;
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
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class LoginActivity extends Activity {
	private Button btnLogin;
	private EditText inputId;
	private EditText inputPassword;
	private ProgressDialog pDialog;
	String login = "/dc/Api/Login/CheckWaiter?waiterid=";
	String baseUrl;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(
				Color.parseColor("#ff9800")); //0080FF
		ab.setBackgroundDrawable(colorDrawable);
		// showInputDialog();
		inputId = (EditText) findViewById(R.id.waiterId);
		inputPassword = (EditText) findViewById(R.id.waiterPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		SessionManager session=new SessionManager(this);
		baseUrl=session.getUserData();

		// Progress dialog
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(true);


		// Login button Click Event
		btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				String id = inputId.getText().toString().trim();
				String password = inputPassword.getText().toString().trim();

				// Check for empty data in the form
				if (id.length() > 0 && password.length() > 0) {
					// login user
					checkLogin(id, password);
				} 
				else if (id.length() == 0) {
				Toast.makeText(getApplicationContext(),
								"Please enter Id ", Toast.LENGTH_LONG).show();
					}
				else if (password.length() == 0) {
						Toast.makeText(getApplicationContext(),
								"Please enter Password", Toast.LENGTH_LONG)
								.show();
					}				
			}
		});
	}

	private void checkLogin(final String id, final String password) {
		
		//Toast.makeText(getApplicationContext(), baseUrl, 1000).show();

		pDialog.setMessage("Logging in ...");
		showDialog();
		StringRequest strReq = new StringRequest(Method.GET,
		baseUrl + login + id + "&pass=" + password
						, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						
						hideDialog();

						try {
							JSONObject jObj = new JSONObject(response);
							boolean result = jObj.getBoolean("ResultState");
							String error=jObj.getString("Error");
							if (result) {
								TempSales.getInstance().setWaiterId(id);
								
								Intent intent = new Intent(LoginActivity.this,
										AreaSelectionActivity.class);
								startActivity(intent);
								//finish();

								Toast.makeText(getApplicationContext(),
										"Successfully Login", Toast.LENGTH_SHORT).show();
							} else {
								//Toast.makeText(getApplicationContext(),error, Toast.LENGTH_SHORT).show();
								warning(error);
							}

						} catch (JSONException e) {
							// JSON error
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						//Log.i("Error", error.getMessage());
						Toast.makeText(getApplicationContext(),
								"Check your Internet Connection ",Toast.LENGTH_LONG).show();
						hideDialog();
					}
				});
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq);
		//Log.i("Url", strReq.toString());
	}

	private void showDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hideDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final EditText input;
		int id = item.getItemId();
		if (id == R.id.addIp) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder.setTitle("Put ip address");
		    input = new EditText(this);
		    input.setInputType(InputType.TYPE_CLASS_TEXT);
		    builder.setView(input);
		    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		          SessionManager session= new SessionManager(getApplicationContext());
		          session.setUserData("http://"+input.getText().toString().trim());
		        }
		    });
		    builder.show();
			
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		finishAffinity();
		
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


}
