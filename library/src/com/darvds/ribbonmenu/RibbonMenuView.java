package com.darvds.ribbonmenu;


import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.darvds.ribbonmenu.RibbonMenuView.SavedState;


public class RibbonMenuView extends LinearLayout {

	public static final String DEBUG_TAG = "Hello";
	private final int LEFT_ANIM = 1;
	private final int RIGHT_ANIM = 2;
	
	private ListView rbmListView;
	private ListView rbmListViewRight;
	private View rbmOutsideView;
	
	private iRibbonMenuCallback callback;
	
	private static ArrayList<RibbonMenuItem> menuItems;
	private static ArrayList<RibbonMenuItem> menuItemsRight;
	
	
	public RibbonMenuView(Context context) {
		super(context);
		
		load();
	}
	
	public RibbonMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);

		load();
	}


	
	
	public void load(){
		
		if(isInEditMode()) return;
		
		inflateLayout();		
		
		initUi();
		
		
	}
	
	
	private void inflateLayout(){
		
		
		
		
		try{
			LayoutInflater.from(getContext()).inflate(R.layout.rbm_menu, this, true);
			} catch(Exception e){
				
			}	
		
		
	}
	
	private void initUi(){
		
		rbmListView = (ListView) findViewById(R.id.rbm_listview);
		rbmListViewRight = (ListView) findViewById(R.id.rbm_list_view_right_2);
		rbmOutsideView = (View) findViewById(R.id.rbm_outside_view);
				
		rbmOutsideView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				hideMenu(0);
				if((event.getY() < 450) && (event.getY() > 280)){
					if((event.getX() < 70)){
						toggleMenu(LEFT_ANIM);
					}
				}
				return false;
			}
		});
		
		
		
		rbmListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				if(callback != null)					
					callback.RibbonMenuItemClick(menuItems.get(position).id);
		//Comment this out if you want the menu to stay open after a button is clicked	
				//hideMenu();
			}
			
		});
			
		rbmListViewRight.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				if(callback != null)					
					callback.RibbonMenuItemClick(menuItemsRight.get(position).id);
		//Comment this out if you want the menu to stay open after a button is clicked	
				//hideMenu();
			}
			
		});
	}
	
	
	public void setMenuClickCallback(iRibbonMenuCallback callback){
		this.callback = callback;
	}
	
	public void setMenuItems(int menu, int direction){
		
		parseXml(menu, direction);
		
		switch (direction)
		{
		case LEFT_ANIM:
			if(menuItems != null && menuItems.size() > 0)
			{
				rbmListView.setAdapter(new Adapter());
				
			}
			break;
		case RIGHT_ANIM:
			if(menuItemsRight != null && menuItemsRight.size() > 0)
			{
				rbmListViewRight.setAdapter(new RightAdapter());
				
			}
			break;
	}
	}
	
	
	public void setBackgroundResource(int resource){
		rbmListView.setBackgroundResource(resource);
		rbmListViewRight.setBackgroundResource(resource);
		
	}
	
	public void showMenu(int direction){
		rbmOutsideView.setVisibility(View.VISIBLE);	
		switch (direction)
		{
		case LEFT_ANIM:
			rbmListView.setVisibility(View.VISIBLE);	
			rbmListView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rbm_in_from_left));
			break;
		case RIGHT_ANIM:
			rbmListViewRight.setVisibility(View.VISIBLE);	
			rbmListViewRight.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rbm_in_from_right));
			break;
		default:
			rbmListView.setVisibility(View.VISIBLE);	
			rbmListView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rbm_in_from_left));
			rbmListViewRight.setVisibility(View.VISIBLE);	
			rbmListViewRight.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rbm_in_from_right));
			break;
	}

	}
	
	
	public void hideMenu(int direction){
		
		rbmOutsideView.setVisibility(View.GONE);
		switch (direction)
		{
		case LEFT_ANIM:
			if(rbmListView.getVisibility() == View.VISIBLE){
			rbmListView.setVisibility(View.GONE);				
			rbmListView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rbm_out_to_left));
			}
			break;
		case RIGHT_ANIM:
			if(rbmListViewRight.getVisibility() == View.VISIBLE){
			rbmListViewRight.setVisibility(View.GONE);	
			rbmListViewRight.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rbm_out_to_right));
			}
			break;
		default:
			if(rbmListView.getVisibility() == View.VISIBLE){
			rbmListView.setVisibility(View.GONE);				
			rbmListView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rbm_out_to_left));
			}
			if(rbmListViewRight.getVisibility() == View.VISIBLE){
			rbmListViewRight.setVisibility(View.GONE);	
			rbmListViewRight.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rbm_out_to_right));
			}
			break;
	}

	}
	
	
	public void toggleMenu(int direction){
	
	switch(direction){	
	case LEFT_ANIM:
		if(rbmListView.getVisibility() == View.GONE){
			showMenu(LEFT_ANIM);
		} else {
			hideMenu(LEFT_ANIM);
		}
		break;
	case RIGHT_ANIM:
		if(rbmListViewRight.getVisibility() == View.GONE){
			showMenu(RIGHT_ANIM);
		} else {
			hideMenu(RIGHT_ANIM);
		}
		break;
	default:
		if(rbmListView.getVisibility() == View.GONE){
			showMenu(LEFT_ANIM);
		} else {
			hideMenu(LEFT_ANIM);
		}
		if(rbmListViewRight.getVisibility() == View.GONE){
			showMenu(RIGHT_ANIM);
		} else {
			hideMenu(RIGHT_ANIM);
		}
		break;
}

	}
	
	
	private void parseXml(int menu, int direction){
		
		switch (direction)
		{
		case LEFT_ANIM:
			menuItems = new ArrayList<RibbonMenuView.RibbonMenuItem>();
			
			
			try{
				XmlResourceParser xpp = getResources().getXml(menu);
				
				xpp.next();
				int eventType = xpp.getEventType();
				
				
				while(eventType != XmlPullParser.END_DOCUMENT){
					
					if(eventType == XmlPullParser.START_TAG){
						
						String elemName = xpp.getName();
							
						
						
						if(elemName.equals("item")){
												
							
							String textId = xpp.getAttributeValue("http://schemas.android.com/apk/res/android", "title");
							String iconId = xpp.getAttributeValue("http://schemas.android.com/apk/res/android", "icon");
							String resId = xpp.getAttributeValue("http://schemas.android.com/apk/res/android", "id");
							String colorId = xpp.getAttributeValue("http://schemas.android.com/apk/res/android", "onClick");
							
							RibbonMenuItem item = new RibbonMenuItem();
							item.id = Integer.valueOf(resId.replace("@", ""));
							item.text = resourceIdToString(textId);
							item.icon = Integer.valueOf(iconId.replace("@", ""));
							item.color = Integer.valueOf(colorId);
							
							menuItems.add(item);
							
						}
						
						
						
					}
					
					eventType = xpp.next();
					
					
				}
				
				
			} catch(Exception e){
				e.printStackTrace();
			}
			break;
		case RIGHT_ANIM:
			menuItemsRight = new ArrayList<RibbonMenuView.RibbonMenuItem>();
			
			
			try{
				XmlResourceParser xpp = getResources().getXml(menu);
				
				xpp.next();
				int eventType = xpp.getEventType();
				
				
				while(eventType != XmlPullParser.END_DOCUMENT){
					
					if(eventType == XmlPullParser.START_TAG){
						
						String elemName = xpp.getName();
							
						
						
						if(elemName.equals("item")){
												
							
							String textId = xpp.getAttributeValue("http://schemas.android.com/apk/res/android", "title");
							String iconId = xpp.getAttributeValue("http://schemas.android.com/apk/res/android", "icon");
							String resId = xpp.getAttributeValue("http://schemas.android.com/apk/res/android", "id");
							String colorId = xpp.getAttributeValue("http://schemas.android.com/apk/res/android", "onClick");
							
							
							RibbonMenuItem item = new RibbonMenuItem();
							item.id = Integer.valueOf(resId.replace("@", ""));
							item.text = resourceIdToString(textId);
							item.icon = Integer.valueOf(iconId.replace("@", ""));
							item.color = Integer.valueOf(colorId);
							
							menuItemsRight.add(item);
							
						}
						
						
						
					}
					
					eventType = xpp.next();
					
					
				}
				
				
			} catch(Exception e){
				e.printStackTrace();
			}
			break;
	}
		
		
	}
	
	
	
	
	
	private String resourceIdToString(String text){
		
		if(!text.contains("@")){
			return text;
		} else {
									
			String id = text.replace("@", "");
									
			return getResources().getString(Integer.valueOf(id));
			
		}
		
	}
	
	
	public boolean isMenuVisible(){		
		return rbmOutsideView.getVisibility() == View.VISIBLE;		
	}
	
		
	
	
	@Override 
	protected void onRestoreInstanceState(Parcelable state)	{
	    SavedState ss = (SavedState)state;
	    super.onRestoreInstanceState(ss.getSuperState());

	    if (ss.bShowMenu) {
	        showMenu(0);
	    } else {
	        hideMenu(0);
	    }
	}
	
	

	@Override 
	protected Parcelable onSaveInstanceState()	{
	    Parcelable superState = super.onSaveInstanceState();
	    SavedState ss = new SavedState(superState);

	    ss.bShowMenu = isMenuVisible();

	    return ss;
	}

	static class SavedState extends BaseSavedState {
	    boolean bShowMenu;

	    SavedState(Parcelable superState) {
	        super(superState);
	    }

	    private SavedState(Parcel in) {
	        super(in);
	        bShowMenu = (in.readInt() == 1);
	    }

	    @Override
	    public void writeToParcel(Parcel out, int flags) {
	        super.writeToParcel(out, flags);
	        out.writeInt(bShowMenu ? 1 : 0);
	    }

	    public static final Parcelable.Creator<SavedState> CREATOR
	            = new Parcelable.Creator<SavedState>() {
	        public SavedState createFromParcel(Parcel in) {
	            return new SavedState(in);
	        }

	        public SavedState[] newArray(int size) {
	            return new SavedState[size];
	        }
	    };
	}
	
	
	
	class RibbonMenuItem{
		
		int id;
		String text;
		int icon;
		int color;
		
	}
	
	
	
	private class Adapter extends BaseAdapter {

		private LayoutInflater inflater;
		
		public Adapter(){
			inflater = LayoutInflater.from(getContext());
		}
		
		
		
		@Override
		public int getCount() {
			
			return menuItems.size();
		}

		@Override
		public Object getItem(int position) {
			
			return null;
		}

		@Override
		public long getItemId(int position) {
			
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			final ViewHolder holder;
			
			
			
			if(convertView == null || convertView instanceof TextView){
				convertView = inflater.inflate(R.layout.rbm_item, null);

				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.rbm_item_icon);
				holder.text = (TextView) convertView.findViewById(R.id.rbm_item_text);
				
				convertView.setTag(holder);
			
			} else {
			
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.image.setImageResource(menuItems.get(position).icon);
			holder.text.setText(menuItems.get(position).text);
			holder.text.setTextColor((menuItems.get(position).color)+0xFF000000);
			
			return convertView;
		}
		
		
		class ViewHolder {
			TextView text;
			ImageView image;
		
		}
			
		
		
		
	}

	private class RightAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		
		public RightAdapter(){
			inflater = LayoutInflater.from(getContext());
		}
		
		
		
		@Override
		public int getCount() {
			
			return menuItemsRight.size();
		}

		@Override
		public Object getItem(int position) {
			
			return null;
		}

		@Override
		public long getItemId(int position) {
			
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			final ViewHolder holder;
			
			
			
			if(convertView == null || convertView instanceof TextView){
				convertView = inflater.inflate(R.layout.rbm_item, null);

				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.rbm_item_icon);
				holder.text = (TextView) convertView.findViewById(R.id.rbm_item_text);
				
				
				convertView.setTag(holder);
			
			} else {
			
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.image.setImageResource(menuItemsRight.get(position).icon);
			holder.text.setText(menuItemsRight.get(position).text);
			holder.text.setTextColor((menuItemsRight.get(position).color)+0xFF000000);
			Log.d("Test", "Color:" + menuItemsRight.get(position).color + "Set Color" + holder.text.getTextColors());
			
			return convertView;
		}
		
		
		class ViewHolder {
			TextView text;
			ImageView image;
		
		}
			
		
		
		
	}


}
