package za.co.toasteacomputing.finalmark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ActivityResults extends Activity {
	
	protected void onCreate(Bundle savedInstaceState)
	{
		super.onCreate(savedInstaceState);
		setContentView(R.layout.activity_result_sheet);
		
		Intent intent = getIntent();
		String resultSheet = intent.getStringExtra("resultSheet");
		TextView txtvResultSheet = (TextView)findViewById(R.id.txtv_result_sheet_results);
		txtvResultSheet.setText(resultSheet);
	}
}
