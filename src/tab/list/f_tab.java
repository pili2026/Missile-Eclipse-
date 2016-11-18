package tab.list;

import android.R;
import android.net.Uri;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;

import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class f_tab extends Activity {
	/*
	 * �qwritepage�I�s���A�o��O�ΨӺ޲zfragment�Ϊ�(f_�t�C��class)
	 */
	Uri uri;
	FileContentProvider test = new FileContentProvider();
	int a = 0, b = 0, c = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getIntent().setData(Uri.parse("content://tab.list.file.cloud/file_choice"));
		final Uri uri_test = getIntent().getData();
		uri = uri_test;

		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		// �ϥ�ActionBar
		final ActionBar actbar = getActionBar();
		// �]�wActionBar���Ҧ�
		actbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // set
		// fullscreen

		actbar.setDisplayShowHomeEnabled(false);
		actbar.setDisplayShowTitleEnabled(false);
		setContentView(kissmediad2d.android.R.layout.f_tab);
		actbar.setDisplayShowTitleEnabled(false);
		// �[�Jf_bmusic��actbar,�ó]�w��ť��
		f_bmusic af = new f_bmusic();
		actbar.addTab(actbar.newTab().setText("MUSIC").setTabListener(new la(af)));
		// �[�Jf_bvideo��actbar,�ó]�w��ť��
		f_bvideo bf = new f_bvideo();
		actbar.addTab(actbar.newTab().setText("VIDEO").setTabListener(new lb(bf)));
		// �[�Jf_bbimage��actbar,�ó]�w��ť��
		f_bimage cf = new f_bimage();
		actbar.addTab(actbar.newTab().setText("IMAGE").setTabListener(new lc(cf)));
		// ��l��temp_file�Pfile_choice
		test.del_table(uri);
	}

	// �o��O��ť���������ɩҭn�����ơA
	public class la implements ActionBar.TabListener {
		private f_bmusic frag;

		public la(f_bmusic fragment) {
			frag = fragment;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub

		}

		// �����Q��ܮɫh��ܭ���
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			if (a == 0) {
				a=1;
				ft.add(android.R.id.content, frag, null);
			}
			ft.show(frag);
		}

		// �����Q�����ɫh���í����A�ت��O���F�O���e�@���������A
		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			ft.hide(frag);
		}

	}

	public class lb implements ActionBar.TabListener {
		private f_bvideo frag;

		public lb(f_bvideo fragment) {
			frag = fragment;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			if (b == 0) {
				b=1;
				ft.add(android.R.id.content, frag, null);
			}

			ft.show(frag);
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			ft.hide(frag);
		}

	}

	public class lc implements ActionBar.TabListener {
		private f_bimage frag;

		public lc(f_bimage fragment) {
			frag = fragment;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			if (c == 0) {
				c=1;
				ft.add(android.R.id.content, frag, null);
			}
			ft.show(frag);
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			ft.hide(frag);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		return true;
	}

}
