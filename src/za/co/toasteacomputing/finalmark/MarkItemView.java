package za.co.toasteacomputing.finalmark;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class MarkItemView 
{
	private Context context;
	private int viewId;
	private EditText etxtName;
	private EditText etxtWeighting;
	private EditText etxtMark;
	private ImageButton ibtnDelete;
	private View view;
	
	public MarkItemView(Context context, int id)
	{
		this.context = context;
		init(id);
	}
	
	private void init(int id)
	{
		LayoutInflater inflator = LayoutInflater.from(context);
		this.view = inflator.inflate(R.layout.mark_item, null);
		
		setViewId(id);
		this.etxtName = (EditText)view.findViewById(R.id.etxt_mark_item_name);
		this.etxtWeighting = (EditText)view.findViewById(R.id.etxt_mark_item_weighting);
		this.etxtMark = (EditText)view.findViewById(R.id.etxt_mark_item_mark);
		this.ibtnDelete = (ImageButton)view.findViewById(R.id.ibtn_mark_item_del);
		
	}
	
	public int getViewId()
	{
		return viewId;
	}
	
	public void setViewId(int Id)
	{
		//linlayThis = (LinearLayout)view.findViewById(R.id.linlay_mark_item);
		//linlayThis.setId(layId);
		this.viewId = Id;
	}
	
	public View getView()
	{
		return view;
	}
	
	public void setEtxtName(EditText name)
	{
		this.etxtName = name;
	}
	
	public EditText getEtxtName()
	{
		return etxtName;
	}
	
	public void setEtxtWeighting(EditText weighting)
	{
		this.etxtWeighting = weighting;
	}
	
	public EditText getEtxtWeighting()
	{
		return etxtWeighting;
	}
	
	public void setEtxtMark(EditText mark)
	{
		this.etxtMark = mark;
	}
	
	public EditText getEtxtMark()
	{
		return etxtMark;
	}
	
	public void setIbtnDelete(ImageButton delete)
	{
		this.ibtnDelete = delete;
	}
	
	public ImageButton getIbtnDelete()
	{
		return ibtnDelete;
	}
}
