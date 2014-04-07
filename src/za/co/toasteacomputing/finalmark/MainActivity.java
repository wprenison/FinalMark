package za.co.toasteacomputing.finalmark;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	private ArrayList<MarkItemView> markItemList = new ArrayList<MarkItemView>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		
	}
	
	//Round a value to x decimal places
	public double round(int places, double value)
	{
		double roundedValue = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
		return roundedValue;
	}
	
	public void onClickAddItem(View view)
	{
		LinearLayout layout = (LinearLayout)findViewById(R.id.linlay_main_mark_items);
		MarkItemView ItemView = new MarkItemView(getBaseContext(), markItemList.size());
		markItemList.add(ItemView);
		layout.addView(ItemView.getView());
	}
	
	public void onClickCalculate(View view)
	{
		boolean valid = validateInputs();
		if(valid)
		{
			String name = "";
			double weighting = 0;
			double mark = 0;
			double convertedMark = 0;
			double finalMark = 0;
			String resultSheet = "";
			
			for(int i = 0; i < markItemList.size(); i++)
			{
				//Retrieves all info from objects for easier use
				name = markItemList.get(i).getEtxtName().getText().toString();
				weighting = Double.parseDouble(markItemList.get(i).getEtxtWeighting().getText().toString());
				mark = Double.parseDouble(markItemList.get(i).getEtxtMark().getText().toString());
				
				//calculates and stores converted marks (converted to appropriate weighting)
				convertedMark = ((weighting/100) * (mark/100));
				finalMark = finalMark + convertedMark;
				
				resultSheet = resultSheet + name + " => " + round(2,(convertedMark*100)) + "\n";
			}
				resultSheet = resultSheet + "\nFinal Mark => " + round(2, (finalMark*100));
				
				//Output results
				Intent resultsIntent = new Intent(MainActivity.this, ActivityResults.class);
				resultsIntent.putExtra("resultSheet", resultSheet);
				MainActivity.this.startActivity(resultsIntent);
				
		}
	}
	
	public boolean validateInputs()
	{
		boolean valid = true;
		String errorMsgs = "";
		
		//Variables to capture all details of a specific item for easier use
		String name = "";
		String weighting = "";
		String mark = "";
		double totWeighting = 0;
		
		//One big loop to cycle all items and validate
		for(int i = 0; i < markItemList.size(); i++)
		{
			//Retrieves items details
			name = markItemList.get(i).getEtxtName().getText().toString();
			weighting = markItemList.get(i).getEtxtWeighting().getText().toString();
			mark = markItemList.get(i).getEtxtMark().getText().toString();
			
			//Completeness check
			if(name.isEmpty())
			{
				errorMsgs = errorMsgs + "There was no name specified for Item " + (i+1) + "\n";
				valid = false;
			}
			
			if(weighting.isEmpty())
			{
				//Checks if items has name for use in error msg else use generic name referance
				if(name.isEmpty())
				{
					errorMsgs = errorMsgs + "No weighting specified for Item " + (i+1) + "\n";
				}
				else
				{
					errorMsgs = errorMsgs + "No weighting specified for " + name + "\n";
				}
				
				valid = false;
			}
			else	//checks weighting is not more than 100% or less than 1%
			{
				if(Double.parseDouble(weighting) > 100)
				{
					//Checks if items has name for use in error msg else use generic name referance
					if(name.isEmpty())
					{
						errorMsgs = errorMsgs + "Weighting specified for Item " + (i+1) + " is more than 100%\n";
					}
					else
					{
						errorMsgs = errorMsgs + "Weighting specified for " + name + " is more than 100%\n";
					}
					
					valid = false;
				}
				else if(Double.parseDouble(weighting) < 1)
				{
					//Checks if items has name for use in error msg else use generic name referance
					if(name.isEmpty())
					{
						errorMsgs = errorMsgs + "Weighting specified for Item " + (i+1) + " is less than 1%\n";
					}
					else
					{
						errorMsgs = errorMsgs + "Weighting specified for " + name + " is less than 1%\n";
					}
					
					valid = false;
				}
				else //Add weighting to total weighting to check all weightings together add up too 100%
					totWeighting = totWeighting + Double.parseDouble(weighting);
			}
			
			if(mark.isEmpty())
			{
				//Checks if items has name for use in error msg else use generic name referance
				if(name.isEmpty())
				{
					errorMsgs = errorMsgs + "No mark specified for Item " + (i+1) + "\n";
				}
				else
				{
					errorMsgs = errorMsgs + "No mark specified for " + name + "\n";
				}
				
				valid = false;
			}
			else
			{
				if(Double.parseDouble(mark) > 100)
				{
					//Checks if items has name for use in error msg else use generic name referance
					if(name.isEmpty())
					{
						errorMsgs = errorMsgs + "Mark specified for Item " + (i+1) + " is more than 100%\n";
					}
					else
					{
						errorMsgs = errorMsgs + "Mark specified for " + name + " is more than 100%\n";
					}
					
					valid = false;
				}
				else if(Double.parseDouble(mark) < 1)
				{
					//Checks if items has name for use in error msg else use generic name referance
					if(name.isEmpty())
					{
						errorMsgs = errorMsgs + "Mark specified for Item " + (i+1) + " is less than 1%\n";
					}
					else
					{
						errorMsgs = errorMsgs + "Mark specified for " + name + " is less than 1%\n";
					}
					
					valid = false;
				}
			}
		}
		
		//final validation check, checks weighting adds up to a 100%
		if(totWeighting != 100)
		{
			errorMsgs = errorMsgs + "The Total weighting of all items do not add up to a 100% \n";			
			valid = false;
		}
		
		if(!valid)
			Toast.makeText(this, errorMsgs, Toast.LENGTH_LONG).show();
				
		return valid;
	}
	
	public void onClickHelp(View view)
	{
		Intent helpIntent = new Intent(MainActivity.this, ActivityHelp.class);
		MainActivity.this.startActivity(helpIntent);
	}
	
	public void onClickDelItem(View view)
	{
		//Removes specified item from list
		ViewGroup vg = (ViewGroup)view.getParent().getParent();
		vg.removeAllViews();
		
		//Removes reference from arraylist
		
		//Finds the name of the item to be removed
		LinearLayout parentLay = (LinearLayout)view.getParent();
		EditText etxt_name = (EditText)parentLay.getChildAt(0);
		String searchName = etxt_name.getText().toString();
		
		//searches for name in arraylist then removes it
		int index = 0;
		boolean found = false;
		while(!found)
		{
			if(searchName.equalsIgnoreCase(markItemList.get(index).getEtxtName().getText().toString()))
			{
				found = true;
				markItemList.remove(index);
			}
			else
				index++;
		}
			
	}
}
