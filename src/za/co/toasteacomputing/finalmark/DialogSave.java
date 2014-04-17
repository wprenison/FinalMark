package za.co.toasteacomputing.finalmark;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class DialogSave extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_save_template);
	}
	
	public void onClickSave(View view)
	{
		//get the user selected name for the template file to be saved
		EditText etxtFileName = (EditText)findViewById(R.id.etxt_save_file_name);
		String fileName = etxtFileName.getText().toString();
		
		//Any extra validation if required here
		
		Intent intent = this.getIntent();
		intent.putExtra("file_name", fileName);
		setResult(RESULT_OK, intent);
		
		//Hides keyboard when save is clicked
		InputMethodManager inmman = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inmman.hideSoftInputFromWindow(etxtFileName.getWindowToken(), 0);
		
		finish();
	}
	
	public void onClickCancel(View view)
	{
		setResult(RESULT_CANCELED);
		finish();
	}
}
