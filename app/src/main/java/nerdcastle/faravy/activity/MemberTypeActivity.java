package nerdcastle.faravy.activity;

import nerdcastle.faravy.info.AppController;
import nerdcastle.faravy.info.Order;
import nerdcastle.faravy.info.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class MemberTypeActivity extends Activity implements OnClickListener {
	Button btnMember;
	Button btnGuest;
	Button btnHotelGuest;
	String getMemberTypeById="/dc/Api/CusCategory/GetMemberTypeByID?meberTypeID=";
	String baseUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_type);
		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(
				Color.parseColor("#000000"));//#347C17
		ab.setBackgroundDrawable(colorDrawable);
		ab.setDisplayHomeAsUpEnabled(true);
		
		SessionManager session=new SessionManager(this);
		baseUrl=session.getUserData();
		
		btnMember = (Button) findViewById(R.id.btnMember);
		btnGuest = (Button) findViewById(R.id.btnGuest);
		btnHotelGuest = (Button) findViewById(R.id.btnHotelGuest);
		btnMember.setOnClickListener(this);
		btnGuest.setOnClickListener(this);
		btnHotelGuest.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnMember:
			String member = "0";
			selectMember(member);

			break;
		case R.id.btnGuest:
			String guest = "1";
			selectMember(guest);

			break;
		case R.id.btnHotelGuest:
			String hotelGuest = "2";
			selectMember(hotelGuest);

			break;

		default:
			break;
		}
	}

	public void selectMember(String memberType) {
		StringRequest stringrequest = new StringRequest(Method.GET,
				baseUrl+getMemberTypeById
						+ memberType + "", new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						try {
							JSONObject jObj = new JSONObject(response);
							String cusCategory = jObj.getString("CusCategory");
							int id = jObj.getInt("ID");
							
							Order.getInstance().setMemberTypeId(id);

							Intent intent = (new Intent(
									getApplicationContext(),
									MemberSelectionActivity.class));
							//intent.putExtra("id", id);

							startActivity(intent);
							Toast.makeText(getApplicationContext(),
									cusCategory + " Selected", Toast.LENGTH_SHORT)
									.show();
							

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
	public void onBackPressed() {
	  
	  startActivity(new Intent(getApplicationContext(),TableSelectionActivity.class));
	  
	}

}
