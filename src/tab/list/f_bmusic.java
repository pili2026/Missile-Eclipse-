package tab.list;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kissmediad2d.android.R;
import tab.list.FileContentProvider.UserSchema;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class f_bmusic extends Fragment {
	/*
	 * ��ftab�Ӻ޲zf_�t�C��class�Af_bmusic�O�Ψ���ܹ��e������Ҧ������֡A�䥦���Pf_image�ۦP
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";
	AttachParameter music = new AttachParameter();
	ListView Audiolist;
	View v;
	public static f_bmusic newInstance(int sectionNumber) {
		f_bmusic fragment = new f_bmusic();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}


	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.blist, container, false);
		Uri uri_music;
		getActivity().getIntent().setData(Uri.parse("content://tab.list.d2d/file_choice"));
		final Uri uri_test = getActivity().getIntent().getData();
		music.seturi(uri_test);

		final String[] columns = { MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM_ID, };
		final String orderBy = MediaStore.Audio.Media._ID;
		 boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if(sdCardExist){
			uri_music =MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		}else{
			uri_music =MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
		}
		 final Cursor musiccursor = getActivity().getContentResolver().query(uri_music, columns, null, null, orderBy);		

			final int music_column_index = musiccursor.getColumnIndex(MediaStore.Audio.Media._ID);
			music.setcount(musiccursor.getCount());
			music.setarrPath(new String[music.getcount()]);
			music.setPath(new String[music.getcount()]);
			music.setarrName(new String[music.getcount()]);
			music.setthumbnails(new Bitmap[music.getcount()]);
			music.setthumbnailsselection(new boolean[music.getcount()]);
			Thread thread = new Thread() {
				@Override
	            public void run () {
					for (int i = 0; i < music.getcount(); i++) {
						musiccursor.moveToPosition(i);
						int id = musiccursor.getInt(music_column_index);
						int dataColumnIndex = musiccursor.getColumnIndex(MediaStore.Audio.Media.DATA);
						int FileColumnIndex = musiccursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
						music.arrPath[i] = musiccursor.getString(dataColumnIndex);
						music.Path[i] = String.valueOf(i);
						music.arrName[i] = musiccursor.getString(FileColumnIndex);
						
					}
					musiccursor.close();
			}
			};
			thread.run();
		

		Audiolist = (ListView) v.findViewById(R.id.PhoneVideoList);
		Audiolist.setTextFilterEnabled(true);
		Audiolist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		Audiolist.setBackgroundResource(R.drawable.background);
		Audiolist.setCacheColorHint(Color.TRANSPARENT);

		Audiolist.setAdapter(new ImageAdapter());
		// autoComplete = (AutoCompleteTextView)
		// v.findViewById(R.id.autoCompleteTextView1);
		// ArrayAdapter<String> adapter = new
		// ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,arrPath);
		// autoComplete.setAdapter(adapter);
		return v;
	}

	public class ImageAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public ImageAdapter() {
			mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return music.getcount();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.bmusic, null);
				holder.fileNameView = (TextView) convertView.findViewById(R.id.textView1);
				holder.checkbox = (CheckBox) convertView.findViewById(R.id.itemCheckBox1);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.checkbox.setId(position);
			holder.fileNameView.setId(position);
			holder.checkbox.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					String[] Form = { UserSchema._ID };
					CheckBox cb = (CheckBox) v;
					int id = cb.getId();
					Pattern patternphoto = Pattern.compile("file_name");
					Matcher matchorther = patternphoto.matcher(music.arrPath[id]);
					if (music.thumbnailsselection[id]) {
						cb.setChecked(false);
						Cursor c = getActivity().getContentResolver().query(music.geturi(), Form, "path_id='" + music.Path[id] + "'", null, null);
						c.moveToFirst();
						music.setid(Integer.parseInt(c.getString(0)));
						String where = UserSchema._ID + " = " + music.id_this;
						getActivity().getContentResolver().delete(music.geturi(), where, null);
						music.thumbnailsselection[id] = false;
					} else {
						Cursor ch_temp_cursor = getActivity().getContentResolver().query(music.geturi(), Form, null, null, null);
						if (ch_temp_cursor.getCount() > 0) {
							Toast.makeText(getActivity().getApplicationContext(), "�̦h�u���@���ɮ�", Toast.LENGTH_SHORT).show();
							cb.setChecked(false);
							ch_temp_cursor.close();
						} else if (matchorther.find()) {
							Toast.makeText(getActivity().getApplicationContext(), "��p�A����惡�ɮסA���ɻP�t�νĬ�", Toast.LENGTH_SHORT).show();
							cb.setChecked(false);
							ch_temp_cursor.close();
						} else {
							ch_temp_cursor.close();
							cb.setChecked(true);
							ContentValues values = new ContentValues();
							values.put(UserSchema._FILEPATH, music.arrPath[id]);
							values.put(UserSchema._PATHID, music.Path[id]);
							getActivity().getContentResolver().insert(music.geturi(), values);
							music.thumbnailsselection[id] = true;
						}

					}
				}

			});
			holder.fileNameView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub

					int id = v.getId();
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					String tt=music.arrPath[id];
					intent.setDataAndType(Uri.parse("file://" + tt), "audio/*");
					startActivity(intent);
				}
			});
			holder.fileNameView.setText(music.arrName[position]);
			holder.checkbox.setChecked(music.thumbnailsselection[position]);
			holder.id = position;
			return convertView;
		}
	}

	class ViewHolder {
		TextView fileNameView;
		CheckBox checkbox;
		int id;
	}
}