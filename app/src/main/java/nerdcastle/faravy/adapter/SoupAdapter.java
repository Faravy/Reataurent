package nerdcastle.faravy.adapter;

import nerdcastle.faravy.info.Order;
import nerdcastle.faravy.activity.R;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class SoupAdapter extends BaseAdapter {
	private Context mContext;

	// Keep all Images in array
	public Integer[] mThumbIds = {

	};
	public String[] names = new String[] { "grandmother vegetable soup",
			"tom kha gai", "cream of tomato soup" };

	// Constructor
	public SoupAdapter(Context c) {
		mContext = c;
	}

	@Override
	public int getCount() {
		return names.length;
	}

	@Override
	public Object getItem(int position) {
		return names[position];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Button btn = new Button(mContext);
		btn.setText(names[position]);
		btn.setTextColor(Color.parseColor("#FFFFFF"));
		btn.setBackgroundResource((R.drawable.selector));
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setSelected(true);
				Order.getInstance().setCustomerName("Tanvir");

			}
		});
		return btn;
	}

}
