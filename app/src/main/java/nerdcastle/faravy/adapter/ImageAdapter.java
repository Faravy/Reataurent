package nerdcastle.faravy.adapter;

import nerdcastle.faravy.activity.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
 
    // Keep all Images in array
    public Integer[] mThumbIds = {
           
    };
   public String[] names = new String[]{"continental breakfast", "traditional english breakfast", "omelette deluxe","yoghurt granola","plain yoghut","fruit salad","dutch pancake"};
    // Constructor
    public ImageAdapter(Context c){
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
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView btn = new TextView(mContext);
        btn.setText(names[position]);
        btn.setTextSize(18f);
        btn.setId( (int) getItemId(position));
       // final int id=btn.getId();
		btn.setTextColor(Color.parseColor("#FFFFFF"));
		btn.setBackgroundResource((R.drawable.selector));
		/*btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.setSelected(true);
				Order.getInstance().setName("Faravy");
				if (id==0) {
					Button btn = new Button(mContext);
			        btn.setText("Faravy");
			        //btn.setId( (int) getItemId(position));
			        final int id=btn.getId();
					btn.setTextColor(Color.parseColor("#FFFFFF"));
					btn.setBackgroundResource((R.drawable.selector));
					//Toast.makeText(mContext, "faravy", 1000).show();
					
				}
				
				
			}
		});*/
        return btn;
    }

	protected void showmenu() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(" Select Addons ");

		// Context mContext = getApplicationContext();
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View layout = inflater.inflate(R.layout.addons, null);
		builder.setView(layout);
		builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		});
		//builder.show().getContext();
		
		
	}
 
}
