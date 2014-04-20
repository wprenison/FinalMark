package za.co.toasteacomputing.finalmark;

import java.io.File;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class DialogLoadorDel extends ListActivity
{
	File [] templatePaths = null;
	String operation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.dialog_load_template);
		
		//Gets what operation should be performed del or load
		operation = getIntent().getStringExtra("loadOrDel");
		
		//change title for del operation
		if(operation.equalsIgnoreCase("del"))
			this.setTitle("Which File To Delete?");
		
		//boolean to det if directory exists
		boolean fileLoadStatus = getTemplates();
		
		if(fileLoadStatus)
		{
			populateTemplateList(templatePaths);
		}
		else
		{
			setResult(RESULT_CANCELED);
			finish();
		}
	}
	
	public void onListItemClick(ListView parent, View v, int position, long id)
	{
		File chosenTemplate = templatePaths[position];	//gets the chosen template for loading\del trough parallel arraying
		
		if(operation.equalsIgnoreCase("load"))
		{
			//Return file path of chosen template
			Intent returnIntent = getIntent();
			returnIntent.putExtra("loadPath", chosenTemplate.getAbsolutePath());
			setResult(RESULT_OK, returnIntent);
			finish();	
		}
		else if(operation.equalsIgnoreCase("del"))
		{
			chosenTemplate.delete();
			Toast.makeText(this, "Template has been deleted", Toast.LENGTH_LONG).show();
			finish();
		}
		else
			Toast.makeText(this, "An operation flag has not been passed", Toast.LENGTH_LONG).show();
		
	}
	
	private boolean getTemplates()
	{	
		boolean fileLoadStatus = false;
		
		//Gets file directory for saved templates
		File finalMarkTemplateDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Final Mark Templates");
				
		//Checks if path exists in other word if any templates have been saved before
		if(finalMarkTemplateDir.isDirectory())
		{
			templatePaths = finalMarkTemplateDir.listFiles();
			fileLoadStatus = true;
		}
		else
		{
			Toast.makeText(this, "No previous templates have been saved.", Toast.LENGTH_LONG).show();			
		}
		
		return fileLoadStatus;
		
	}
	
	private void populateTemplateList(File [] templatePaths)
	{
		String [] templateItems = new String[templatePaths.length];
		String fileName = ""; //a temporary string to modify the directory path string to only show file name and hide extension
		int dotPos; //Used to indicate the position index of where the dot before the extension is before the file name
		
		//Creates a list of template files stored in the template directory
		for(int i = 0; i < templatePaths.length; i++)
		{
			fileName = templatePaths[i].getName();
			dotPos = fileName.lastIndexOf(".");
			fileName = fileName.substring(0, dotPos);
			templateItems[i] = fileName;
		}
		
		//Populates actual list view with loadable template items
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, templateItems));
	}
}
