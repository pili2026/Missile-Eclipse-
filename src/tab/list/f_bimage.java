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
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class f_bimage extends Fragment {
	/*
	 * ��ftab�Ӻ޲zf_�t�C��class�Af_bimage�O�Ψ���ܹ��e������Ҧ����Ϥ��A
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";
	AttachParameter image = new AttachParameter();
	View v;
	public static f_bimage newInstance(int sectionNumber) {
		f_bimage fragment = new f_bimage();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public f_bimage() {
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.bgallery, container, false);
		getActivity().getIntent().setData(Uri.parse("content://tab.list.d2d/file_choice"));
		final Uri uri_test = getActivity().getIntent().getData();
		image.seturi(uri_test);

		final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME };
		final String orderBy = MediaStore.Images.Media._ID;
		// �o�䪺DB�����o������ت��Ϥ�table�Aquery�Ĥ@�ӬOtabel���s���B�ĤG�ӬO�n��X�������X�ӨϥΡB�ĤT�]�wnull��ܨS���]�w����A�ĥ|�ӬO���ƧǪ��覡
		Cursor imagecursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
		/*
		 * getColumnIndex����k�O�A�Чi�D��MediaStore.Images.Media._ID�b���h�����̡A ������m�O�ĴX��
		 */
		int image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Media._ID);
		image.setcount(imagecursor.getCount());
		// �o��O�����ɮת��`�ƶq
		image.setthumbnails(new Bitmap[image.getcount()]);
		image.setarrPath(new String[image.getcount()]);
		image.setarrName(new String[image.getcount()]);
		image.setthumbnailsselection(new boolean[image.getcount()]);
		// �o�̬O�w��C���ɮ��x�s�U�۪���T
		for (int i = 0; i < image.getcount(); i++) {
			// �����N����(���ƹ�)����ҭn���檺��ơA�p�S���ϥ�,�h�|�o�ͧ䤣���ƪ����~
			imagecursor.moveToPosition(i);
			int id = imagecursor.getInt(image_column_index);
			// ��XMediaStore.Images.Media.DATA�����b���@�Ӧ�m
			int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
			// ��XMediaStore.Images.Media.DISPLAY_NAME�����b���@�Ӧ�m
			int FileColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
			// ���o�Ϥ����Y��
			image.thumbnails[i] = MediaStore.Images.Thumbnails.getThumbnail(getActivity().getApplicationContext().getContentResolver(), id, MediaStore.Images.Thumbnails.MICRO_KIND, null);
			/*
			 * ��ڭ��X�Ӫ���m�A�ϥ�getString�N�i�H�o��Ӧ�m����
			 * �ҦpFileColumnIndex��X�ӬO1�A�z�LgetString�i�H�o�� �ɮצW��
			 */
			image.arrPath[i] = imagecursor.getString(dataColumnIndex);
			image.arrName[i] = imagecursor.getString(FileColumnIndex);
			// .println("arrName[id]="+arrName[id]);
		}
		imagecursor.close();
		GridView imagegrid = (GridView) v.findViewById(R.id.PhoneImageGrid);
		// �ϥΦۭq�q��adapter
		imagegrid.setAdapter(new ImageAdapter());

		return v;
	}

	public class ImageAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public ImageAdapter() {
			mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return image.count;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			/*
			 * �ϥ�holder���ت��O�޲z�C�@��checkbox�����A�C
			 * �]�����ެOlistview+checkbox�A�Ϊ̬Ogridview+checkbox
			 * ���Ƶ��Ƥj�󭶭��i����ܪ��j�p�ɡA�|���ҿת��^������A���]�����[�Wcheckbox�ҥH�ݤ��X��
			 * �Y��[�Wcheckbox�ɨöi���I���ɡA�|�y��checkbox�������D���͡A�ҥHholder���ѨMcheckbox�ø������D���@
			 */
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.bimage, null);
				holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
				holder.checkbox = (CheckBox) convertView.findViewById(R.id.itemCheckBox);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.checkbox.setId(position);
			holder.imageview.setId(position);
			holder.checkbox.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					String[] Form = { UserSchema._ID };
					CheckBox cb = (CheckBox) v;
					int id = cb.getId();

					Pattern patternphoto = Pattern.compile("file_name");
					Matcher matchorther = patternphoto.matcher(image.arrPath[id]);

					if (image.thumbnailsselection[id]) {
						cb.setChecked(false);
						// �o�̪�DB�O��ϥΪ̨����ҿ�ܪ��ɮ׮ɡA�n�qtable���R�����ҿ襤���ɮ�
						Cursor c = getActivity().getContentResolver().query(image.geturi(), Form, "filepath='" + image.arrPath[id] + "'", null, null);
						c.moveToFirst();
						image.setid(Integer.parseInt(c.getString(0)));
						String where = UserSchema._ID + " = " + image.getid();
						getActivity().getContentResolver().delete(image.geturi(), where, null);
						c.close();
						image.thumbnailsselection[id] = false;
					} else {
						// ���ˬd�O�_�w�g��ܹL�ɮפF
						Cursor ch_temp_cursor = getActivity().getContentResolver().query(image.geturi(), Form, null, null, null);
						if (ch_temp_cursor.getCount() > 0) {
							Toast.makeText(getActivity().getApplicationContext(), "�̦h�u���@���ɮ�", Toast.LENGTH_SHORT).show();
							cb.setChecked(false);
							ch_temp_cursor.close();
						} else if (matchorther.find()) {
							Toast.makeText(getActivity().getApplicationContext(), "��p�A����惡�ɮסA���ɻP�t�νĬ�", Toast.LENGTH_SHORT).show();
							cb.setChecked(false);
							ch_temp_cursor.close();
						} else {
							// ���ɮת����|�[�J��TABLE��
							cb.setChecked(true);
							ContentValues values = new ContentValues();
							values.put(UserSchema._FILEPATH, image.arrPath[id]);
							getActivity().getContentResolver().insert(image.geturi(), values);
							image.thumbnailsselection[id] = true;
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
					intent.setDataAndType(Uri.parse("file://" + image.arrPath[id]), "image/*");
					startActivity(intent);
				}
			});
			holder.imageview.setImageBitmap(image.thumbnails[position]);
			holder.checkbox.setChecked(image.thumbnailsselection[position]);
			holder.id = position;
			return convertView;
		}
	}

	class ViewHolder {
		ImageView imageview;
		CheckBox checkbox;
		int id;
	}
}