package tab.list;

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
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class f_bvideo extends Fragment {
	/*
	 * ��ftab�Ӻ޲zf_�t�C��class�Af_bvideo�O�Ψ���ܹ��e������Ҧ������֡A�䥦���Pf_image�ۦP
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";
	AttachParameter video =new AttachParameter();
	ListView Videolist;
	View v;
	public static f_bvideo newInstance(int sectionNumber) {
		f_bvideo fragment = new f_bvideo();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public f_bvideo() {
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.blist, container, false);

		getActivity().getIntent().setData(Uri.parse("content://tab.list.d2d/file_choice"));
		final Uri uri_test = getActivity().getIntent().getData();
		video.seturi(uri_test);

		final String[] columns = { MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION, MediaStore.Video.Media.DATE_ADDED, };
		final String orderBy = MediaStore.Video.Media._ID;

		Cursor videocursor = getActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);

		int music_column_index = videocursor.getColumnIndex(MediaStore.Video.Media._ID);
		video.setcount(videocursor.getCount());
		video.setthumbnails(new Bitmap[video.getcount()]);
		video.setarrPath(new String[video.getcount()]);
		video.setarrName(new String[video.getcount()]);
		video.setarrDuration(new int[video.getcount()]);
		video.setarrSize(new int[video.getcount()]);
		video.setthumbnailsselection(new boolean[video.getcount()]);

		for (int i = 0; i < video.getcount(); i++) {
			videocursor.moveToPosition(i);
			int id = videocursor.getInt(music_column_index);
			int dataColumnIndex = videocursor.getColumnIndex(MediaStore.Video.Media.DATA);
			int FileColumnIndex = videocursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME);
			int sizeColumnIndex = videocursor.getColumnIndex(MediaStore.Video.Media.SIZE);
			int DurationColumnIndex = videocursor.getColumnIndex(MediaStore.Video.Media.DURATION);
			video.thumbnails[i] = MediaStore.Video.Thumbnails.getThumbnail(getActivity().getApplicationContext().getContentResolver(), id, MediaStore.Images.Thumbnails.MICRO_KIND, null);
			video.arrPath[i] = videocursor.getString(dataColumnIndex);
			video.arrName[i] = videocursor.getString(FileColumnIndex);
			video.arrSize[i] = Integer.valueOf(videocursor.getString(sizeColumnIndex));

			video.arrDuration[i] = videocursor.getInt(DurationColumnIndex);

			
		}
		videocursor.close();
		Videolist = (ListView) v.findViewById(R.id.PhoneVideoList);
		Videolist.setTextFilterEnabled(true);
		Videolist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		Videolist.setBackgroundResource(R.drawable.background);
		Videolist.setCacheColorHint(Color.TRANSPARENT);
		Videolist.setAdapter(new VideoAdapter());

		return v;
	}

	public class VideoAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public VideoAdapter() {
			mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return video.count;
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
				convertView = mInflater.inflate(R.layout.bvideo, null);
				holder.duration = (TextView) convertView.findViewById(R.id.duration);
				holder.file_size = (TextView) convertView.findViewById(R.id.file_size);
				holder.imageview = (ImageView) convertView.findViewById(R.id.image_preview);
				holder.fileNameView = (TextView) convertView.findViewById(R.id.Videoname);
				holder.checkbox = (CheckBox) convertView.findViewById(R.id.Vcheck);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.checkbox.setId(position);
			holder.imageview.setId(position);
			holder.fileNameView.setId(position);
			holder.checkbox.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					String[] Form = { UserSchema._ID };
					CheckBox cb = (CheckBox) v;
					int id = cb.getId();
					Pattern patternphoto = Pattern.compile("file_name");
					Matcher matchorther = patternphoto.matcher(video.arrPath[id]);
					if (video.thumbnailsselection[id]) {

						cb.setChecked(false);

						Cursor c = getActivity().getContentResolver().query(video.geturi(), Form, "filepath='" + video.arrPath[id] + "'", null, null);
						c.moveToFirst();
						video.setid(Integer.parseInt(c.getString(0)));

						String where = UserSchema._ID + " = " + video.getid();
						getActivity().getContentResolver().delete(video.geturi(), where, null);

						video.thumbnailsselection[id] = false;
					} else {
						Cursor ch_temp_cursor = getActivity().getContentResolver().query(video.geturi(), Form, null, null, null);
						if (ch_temp_cursor.getCount() > 0) {
							Toast.makeText(getActivity().getApplicationContext(), "�̦h�u���@���ɮ�", Toast.LENGTH_SHORT).show();
							cb.setChecked(false);
							ch_temp_cursor.close();
						} else if (matchorther.find()) {
							Toast.makeText(getActivity().getApplicationContext(), "��p�A����惡�ɮסA���ɻP�t�νĬ�", Toast.LENGTH_SHORT).show();
							cb.setChecked(false);
							ch_temp_cursor.close();
						} else {
							cb.setChecked(true);
							ContentValues values = new ContentValues();
							values.put(UserSchema._FILEPATH, video.arrPath[id]);
							values.put(UserSchema._DURATION, video.arrDuration[id]);
							values.put(UserSchema._FILENAME, video.arrName[id]);
							values.put(UserSchema._FILESIZE, video.arrSize[id]);

							getActivity().getContentResolver().insert(video.geturi(), values);
							video.thumbnailsselection[id] = true;
						}

					}
				}
			});
			holder.imageview.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub

					int id = v.getId();
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);

					intent.setDataAndType(Uri.parse("file://" + video.arrPath[id]), "video/*");
					startActivity(intent);
				}
			});
			holder.imageview.setImageBitmap(video.thumbnails[position]);
			holder.fileNameView.setText(video.arrName[position]);

			holder.duration.setText(String.valueOf(TimeUtils.toFormattedTime(video.arrDuration[position])));
			holder.file_size.setText(String.valueOf(video.arrSize[position] / (1024 * 1024)) + "MB | ");
			holder.checkbox.setChecked(video.thumbnailsselection[position]);
			holder.id = position;
			return convertView;
		}
	}

	class ViewHolder {
		ImageView imageview;
		TextView fileNameView, duration, file_size;
		CheckBox checkbox;
		int id;
	}

}