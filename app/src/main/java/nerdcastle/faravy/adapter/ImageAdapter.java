package nerdcastle.faravy.adapter;

import nerdcastle.faravy.activity.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
	Context context;
	ArrayList<String> itemList = null;


    public ImageAdapter(Context context, ArrayList<String> itemList){
		this.context = context;
		this.itemList = itemList;
    }
 
    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView btn = new TextView(context);
        btn.setText(itemList.get(position));
        btn.setTextSize(18f);
        btn.setGravity(Gravity.CENTER);
		btn.setTextColor(Color.parseColor("#000000"));
		btn.setBackgroundResource((R.drawable.deselector));

        return btn;
    }
 
}
