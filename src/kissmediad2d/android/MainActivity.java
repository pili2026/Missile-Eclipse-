package kissmediad2d.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import login.submit1.Logout;
import navigation.CustomDrawerAdapter;
import navigation.DrawerItem;

public class MainActivity extends Activity{
	
	  private DrawerLayout mDrawerLayout;
      private ListView mDrawerList;
      private ActionBarDrawerToggle mDrawerToggle;
 
      private CharSequence mDrawerTitle;
      private CharSequence mTitle;
      
      public String selectItem = null;
      
      CustomDrawerAdapter adapter;
      List<DrawerItem> dataList;

    @Override
    	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        
        Bundle bundle = getIntent().getExtras();
        selectItem = bundle.getString("location");
        
     // Initializing
        dataList = new ArrayList<DrawerItem>();
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                    GravityCompat.START);
        
        dataList.add(new DrawerItem("Notify",R.drawable.ic_notify_icon));
        dataList.add(new DrawerItem("Inbox", R.drawable.ic_action_content_drafts));
        dataList.add(new DrawerItem("Send", R.drawable.ic_action_content_mail));
        dataList.add(new DrawerItem("Edit", R.drawable.ic_edit_icon));
        
        adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
                dataList);
        
        mDrawerList.setAdapter(adapter);
        
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
          public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to
                                                          // onPrepareOptionsMenu()
          }
     
          public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to
                                                          // onPrepareOptionsMenu()
          }
    };
     
    	mDrawerLayout.setDrawerListener(mDrawerToggle);
    	
    	 //set fragment location
        setFragment(Integer.valueOf(selectItem));
     
//    	if (savedInstanceState == null) {
//    		SelectItem(0);
//    	}
    }
		public void SelectItem(int possition) {
		
			Fragment fragment = null;
			Intent intent = new Intent();
			switch (possition) {
			case 0:
				fragment = new FiringRoom();
				setFragment(0);
				mDrawerLayout.closeDrawer(GravityCompat.START);
				break;
			case 1:
				fragment = new MissileFiringRoom();
				setFragment(1);
				mDrawerLayout.closeDrawer(GravityCompat.START);
              break;
            case 2:
            	fragment = new FiringReadyRoom();
            	setFragment(2);
            	mDrawerLayout.closeDrawer(GravityCompat.START);
              break;
            case 3 :
            	fragment = new edit();
            	setFragment(3);
            	mDrawerLayout.closeDrawer(GravityCompat.START);
            	break;

        	default:
        			break;
			}

			FragmentManager frgManager = getFragmentManager();
			frgManager.beginTransaction().replace(R.id.content_frame, fragment)
                    	.commit();

			mDrawerList.setItemChecked(possition, true);
			setTitle(dataList.get(possition).getItemName());
			mDrawerLayout.closeDrawer(mDrawerList);

		}
		@Override
		public void setTitle(CharSequence title) {
			mTitle = title;
			getActionBar().setTitle(mTitle);
		}
	 
		@Override
		protected void onPostCreate(Bundle savedInstanceState) {
			super.onPostCreate(savedInstanceState);
			// Sync the toggle state after onRestoreInstanceState has occurred.
			mDrawerToggle.syncState();
		}
	 
		@Override
		public void onConfigurationChanged(Configuration newConfig) {
			super.onConfigurationChanged(newConfig);
			// Pass any configuration change to the drawer toggles
			mDrawerToggle.onConfigurationChanged(newConfig);
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// Handle action bar item clicks here. The action bar will
			// automatically handle clicks on the Home/Up button, so long
			// as you specify a parent activity in AndroidManifest.xml.
			int id = item.getItemId();
			if (id == R.id.action_settings) {
				return true;
			}
			return super.onOptionsItemSelected(item);
		}
		private class DrawerItemClickListener implements  ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			SelectItem(position);

			}
		}
		public void setFragment(int position) {
			FragmentManager frgManager = getFragmentManager();
	        
	        switch (position) {
	            case 0:
	            	FiringRoom firingRoom = new FiringRoom();
	            	frgManager.beginTransaction().replace(R.id.content_frame, firingRoom)
                	.commit();
	                break;
	            case 1:
	            	MissileFiringRoom missileFiringRoom =new  MissileFiringRoom();
	            	frgManager.beginTransaction().replace(R.id.content_frame, missileFiringRoom)
                	.commit();
	                break;
	            case 2:
	            	FiringReadyRoom firingReadyRoom =new  FiringReadyRoom();
	            	frgManager.beginTransaction().replace(R.id.content_frame, firingReadyRoom)
                	.commit();
	                break;     
	            case 3:
	            	edit edit =new  edit();
	            	frgManager.beginTransaction().replace(R.id.content_frame, edit)
                	.commit();
	                break;    
	         
	        }
	    }
}
