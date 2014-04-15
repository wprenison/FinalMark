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

public class DialogLoad extends ListActivity
{
	File [] templatePaths = getTemplates();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.dialog_load_template);
		
		populateTemplateList(templatePaths);
	}
	
	public void onListItemClick(ListView parent, View v, int position, long id)
	{
		File chosenLoadTemplate = templatePaths[position];	//gets the chosen template for loading trough parallel arraying
		
		//Return file path of chosen template
		Intent returnIntent = getIntent();
		returnIntent.putExtra("loadPath", chosenLoadTemplate.getAbsolutePath());
		setResult(RESULT_OK, returnIntent);
		finish();		
	}
	
	private File[] getTemplates()
	{
		File[] templatePaths = null;
		
		//Gets file directory for saved templates
		File finalMarkTemplateDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Final Mark Templates");
		
		//Checks if path exists in other word if any templates have been saved before
		if(finalMarkTemplateDir.exists())
		{
			templatePaths = finalMarkTemplateDir.listFiles();
		}
		else
		{
			Toast.makeText(this, "No previous templates have been saved.", Toast.LENGTH_LONG).show();
			finish();
		}
		
		return templatePaths;
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
		
		//Populates actual list view with loadabel template items
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, templateItems));
	}
}
