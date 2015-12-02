package nerdcastle.faravy.adapter;

import java.util.ArrayList;

import nerdcastle.faravy.info.ChildOfExpListView;
import nerdcastle.faravy.info.GroupOfExpListView;
import nerdcastle.faravy.activity.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpListViewAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<GroupOfExpListView> groupList;

	public ExpListViewAdapter(Context context, ArrayList<GroupOfExpListView> groups) {
		this.context = context;
		this.groupList = groups;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ArrayList<ChildOfExpListView> chList = groupList.get(groupPosition).getItems();
		return chList.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		ChildOfExpListView child = (ChildOfExpListView) getChild(groupPosition, childPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.child_items, null);
		}

		TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
		TextView itemQuantity = (TextView) convertView
				.findViewById(R.id.quantity);
		TextView itemUnitPrice = (TextView) convertView
				.findViewById(R.id.unitPrice);
		TextView itemTotalPrice = (TextView) convertView
				.findViewById(R.id.totalprice);

		itemName.setText(child.getName().toString());
		itemQuantity.setText(child.getQuantity().toString());
		itemUnitPrice.setText(child.getUnitPrice().toString());
		
		double qun=Double.parseDouble(itemQuantity.getText().toString());
		double price=Double.parseDouble(itemUnitPrice.getText().toString());
		
		itemTotalPrice.setText(String.valueOf(qun*price));

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		ArrayList<ChildOfExpListView> chList = groupList.get(groupPosition).getItems();
		return chList.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groupList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groupList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupOfExpListView group = (GroupOfExpListView) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater inf = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inf.inflate(R.layout.group_items, null);
		}
		TextView groupName = (TextView) convertView
				.findViewById(R.id.group_name);
		groupName.setText(group.getName());
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
