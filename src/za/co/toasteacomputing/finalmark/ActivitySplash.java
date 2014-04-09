package za.co.toasteacomputing.finalmark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class ActivitySplash extends Activity
{
	//Splash lenght
	private final int SPLASH_TIMEOUT = 3000;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		
		//uses delay before moving to main screen
		new Handler().postDelayed(new Runnable()
		{
			
			public void run()
			{
				Intent intent = new Intent(ActivitySplash.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		}, SPLASH_TIMEOUT);
	}
}
