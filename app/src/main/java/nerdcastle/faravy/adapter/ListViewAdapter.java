package nerdcastle.faravy.adapter;

import java.util.ArrayList;

import nerdcastle.faravy.info.ModifyOrder;
import nerdcastle.faravy.info.Order;
import nerdcastle.faravy.activity.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListViewAdapter extends ArrayAdapter<String> {

	Activity mContext = null;
	Context context;
	ArrayList<String> itemList = null;
	LinearLayout layout;

	public ListViewAdapter(Context context, ArrayList<String> itemList) {
		super(context, R.layout.adapter_listview, itemList);
		this.context = context;
		this.itemList = itemList;
	}

	// holder Class to contain inflated xml file elements
	static class ViewHolder {

		public TextView itemName;
		public TextView quantity;
		public TextView amount;
		public TextView price;
		public TextView priority;
		
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		
		ViewHolder mVHolder = null;

		
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.adapter_listview, parent, false);

			
			mVHolder = new ViewHolder();
			mVHolder.itemName = (TextView) convertView
					.findViewById(R.id.itemName);
			mVHolder.quantity = (TextView) convertView
					.findViewById(R.id.quantity);
			mVHolder.amount = (TextView) convertView
					.findViewById(R.id.unitPrice);
			mVHolder.price = (TextView) convertView
					.findViewById(R.id.totalprice);
			mVHolder.priority = (TextView) convertView
					.findViewById(R.id.text_priority);
			

			convertView.setTag(mVHolder);

		} else
			mVHolder = (ViewHolder) convertView.getTag();

		int quantity = Order.getInstance().getQuantityList().get(position);
		double amount = Double.parseDouble(Order.getInstance()
				.getItemPriceList().get(position));
		double total = quantity * amount;
		String priority=ModifyOrder.getInstance().getPriorityList().get(position);

		//mVHolder.itemName.setTextColor(Color.parseColor("#4C6600"));
		mVHolder.itemName.setText(Order.getInstance().getItemList()
				.get(position));
		mVHolder.quantity.setText(String.valueOf(Order.getInstance()
				.getQuantityList().get(position)));
		mVHolder.amount.setText(Order.getInstance().getItemPriceList()
				.get(position));
		mVHolder.price.setText(String.valueOf(total));
		mVHolder.priority.setText(priority);

		if (!priority.equals("")){
			//mVHolder.priority.setText("*"+priority);
		}

		return convertView;
	}
}
