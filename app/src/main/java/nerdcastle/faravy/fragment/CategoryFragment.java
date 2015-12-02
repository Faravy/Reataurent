package nerdcastle.faravy.fragment;
import nerdcastle.faravy.info.AppController;
import nerdcastle.faravy.info.OrderButton;
import nerdcastle.faravy.info.SessionManager;
import nerdcastle.faravy.info.TempSales;
import nerdcastle.faravy.activity.R;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class CategoryFragment extends Fragment {
	TableLayout tableLayout;
	TableRow tableRow = null;
	int enabledButton;
	JSONArray jsonArray;
	String getAllCategory = "/dc/Api/Category/GetAllCateogry";
	View rootView;
	String baseUrl;
	private ProgressDialog proDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_category, container,
				false);		
		tableLayout = (TableLayout)rootView.findViewById(R.id.categoryItem);
		tableLayout.removeAllViews();
		SessionManager session=new SessionManager(getActivity());
		
		proDialog = new ProgressDialog(getActivity());
        proDialog.setMessage("Loading.....");
        proDialog.setCancelable(true);
		
        baseUrl=session.getUserData();
		showCatagory();
		return rootView;

}


	private void showCatagory() {
		proDialog.show();
		
		StringRequest stringrequest = new StringRequest(Method.GET,
				baseUrl + getAllCategory,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							jsonArray = new JSONArray(response);
							for (int i = 0; i < jsonArray.length(); i++) {

								String name = jsonArray.getJSONObject(i)
										.getString("Category_Name");
								final String categoryid = jsonArray.getJSONObject(i)
										.getString("Category_Id");
								

								if (i % 3 == 0) {
									tableRow = new TableRow(
											getActivity());
									tableLayout.addView(tableRow);
								}
								final OrderButton orderBtn = new OrderButton(
										getActivity());
								orderBtn.setCategoryId(categoryid);
								orderBtn.setText(name);
								orderBtn.setId(i);
								orderBtn.setTextColor(Color
										.parseColor("#000000"));
								orderBtn.setBackgroundResource((R.drawable.deselect));
								orderBtn.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										v.setSelected(true);
										enabledButton = v.getId();
										deselectButtons();
										//Toast.makeText(getActivity(),orderBtn.getCategoryId(),Toast.LENGTH_SHORT).show();
										/*Intent intent = new Intent(
												getActivity(),
												ItemSelectionActivity.class);
										intent.putExtra("CategoryId",
												orderBtn.getCategoryId());
										startActivity(intent);*/
										TempSales.getInstance().setCategoryId(categoryid);
										FragmentManager fm=getFragmentManager();
								        FragmentTransaction ft=fm.beginTransaction();
								        Itemfragment myFragment=new Itemfragment();
								        ft.replace(R.id.fragment,myFragment);
								        ft.addToBackStack(null);
								        ft.commit();
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
	}

	public void deselectButtons() {
		for (int i = 0; i < jsonArray.length(); i++) {
			if (enabledButton != i)
				rootView.findViewById(i).setSelected(false);
		}
	}
		
	}

