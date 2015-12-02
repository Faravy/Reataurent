package nerdcastle.faravy.info;

import android.content.Context;
import android.widget.Button;

public class OrderButton extends Button {

	private String categoryId;
	private int intId;
	
	public int getIntId() {
		return intId;
	}
	public void setIntId(int intId) {
		this.intId = intId;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public OrderButton(Context context) {
		super(context);
	}

}
