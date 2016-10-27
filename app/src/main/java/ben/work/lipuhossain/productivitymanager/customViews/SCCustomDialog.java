package ben.work.lipuhossain.productivitymanager.customViews;

import android.app.Dialog;
import android.content.Context;

/**
 * Custom loading progress dialog will provide closing dialog when click back button
 * 
 * @author lipu hossain
 *
 */
public class SCCustomDialog extends Dialog {

	public SCCustomDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setCancelable(false);
	}
	
	public SCCustomDialog(Context context, int style) {
		super(context, style);
		// TODO Auto-generated constructor stub
		this.setCancelable(false);
	}

	@Override
	public void onBackPressed() {
		this.dismiss();
	}


}
