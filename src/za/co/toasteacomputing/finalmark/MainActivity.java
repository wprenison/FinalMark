package za.co.toasteacomputing.finalmark;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;


public class MainActivity extends Activity {
	
	private ArrayList<MarkItemView> markItemList = new ArrayList<MarkItemView>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		//Add menu to action bar
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//Round a value to x decimal places
	public double round(int places, double value)
	{
		double roundedValue = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
		return roundedValue;
	}
	
	public void onClickAddItem(View view)
	{
		//Finds the appropriate layout for the item to be added too, creates an object of the item
		//Stores that obj in and arrayList for later referance and adds the view item to the layout
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
	
	public void onClickSaveTemplate(MenuItem item)
	{
		//Validates that items exist and appropriate validation for a save template. eg all validation except for the mark of an item
		if(validateItemsExist())
		{
			//Prompt for save file name
			Intent saveIntent = new Intent(MainActivity.this, DialogSave.class);
			MainActivity.this.startActivityForResult(saveIntent, 1);
		}
		else
			Toast.makeText(this, "You have not added any items to save into the template.", Toast.LENGTH_LONG).show();
	}
	
	public void onClickLoadTemplate(MenuItem item)
	{
		//Call load dialog
		Intent loadIntent = new Intent(MainActivity.this, DialogLoad.class);
		MainActivity.this.startActivityForResult(loadIntent, 2);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == 1)
		{
			if(resultCode == RESULT_OK)
			{
				String fileName = data.getStringExtra("file_name");
				
				
					//Gets appropriate directory for writing of file
					File pubDirFile = new File(Environment.getExternalStorageDirectory(), "/Final Mark Templates");

					//Checks if the base directory already exists if not it creates it
					if(!pubDirFile.isDirectory())
					{
						if(!pubDirFile.mkdir())
							Toast.makeText(this, "Directory could not be created!", Toast.LENGTH_LONG).show();
					}
					
					//Creates actual file to write too
					final File oFile = new File(pubDirFile, fileName + ".txt");
					
					//Checks if template already exists
					if(oFile.exists())
					{
						//Display dialog to find out if user would like to overwrite the existing template
						AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
						builder.setTitle("Overwrite Confirm");
						builder.setMessage("A Template with that name already exists. Would you like to replace it?");
						
						builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) 
							{
								// TODO Auto-generated method stub
								try
								{
									//Writes template to file
									FileOutputStream fOut = new FileOutputStream(oFile);
									OutputStreamWriter writer = new OutputStreamWriter(fOut);
									
									writeTemplate(writer);
									dialog.dismiss();
								}
								catch(IOException ioe)
								{
									ioe.printStackTrace();
								}
							}
						});
						
						builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) 
							{
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
						
						//Call created dialog
						AlertDialog alert = builder.create();
						alert.show();
					}
					else
					{
						try
						{
							//Writes template to file
							FileOutputStream fOut = new FileOutputStream(oFile);
							OutputStreamWriter writer = new OutputStreamWriter(fOut);
							
							writeTemplate(writer);
						}
						catch(IOException ioe)
						{
							ioe.printStackTrace();
						}
					}
					
					
			}
			else if(resultCode == RESULT_CANCELED)
				Toast.makeText(this, "Template Save was canceled", Toast.LENGTH_LONG).show();
			
		}
		else if(requestCode == 2)
		{
			if(resultCode == RESULT_OK)
			{
				//Get load template path string
				String loadPath = data.getStringExtra("loadPath");
				Toast.makeText(this, "Awe nigga the load path is: " + loadPath, Toast.LENGTH_LONG).show();
			}
			else if(resultCode == RESULT_CANCELED)
			{
				Toast.makeText(this, "Template Load was canceled", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public void loadTemplate(String loadPath)
	{
		//flushes markItemsList array list of any old values that it may have contained
		markItemList.clear();
		
		
	}
	
	public boolean validateItemsExist()
	{
		boolean itemsExist = false;
		
		if(!markItemList.isEmpty())
		{
			itemsExist = true;
		}
		
		return itemsExist;
	}
	
	//Used to create and write a template file
	public void writeTemplate(OutputStreamWriter writer)
	{
		
		try
		{
			for(int i = 0; i < markItemList.size(); i++)
			{
				writer.write(markItemList.get(i).getName() + "," + markItemList.get(i).getWeighting() + ",");
				writer.flush();
			}
			
			writer.close();
			
			Toast.makeText(this, "Template Saved!", Toast.LENGTH_LONG).show();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	
	public boolean validateInputs()
	{
		//Bool param is only to modify this validate methode to only validate requirements to save a tamplate
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
