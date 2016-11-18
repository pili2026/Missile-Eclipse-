package kissmediad2d.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import tab.list.AttachParameter;
import tab.list.FileContentProvider;
import tab.list.FileContentProvider.UserSchema;
import tab.list.f_tab;
import login.submit1.*;

class missileInfo{
    private String title,content, token, state, date,filepateh;

    missileInfo() {
        content = new String();
        title = new String();
        token = new String();
        date = new String();
        filepateh=new String();
    }

    void setDate(String arg) {
        date = arg;
    }
    void setContent(String arg) {
        content = arg;
    }

    void setToken(String arg) {
        token = arg;
    }
    void setTitle(String arg) {
        title = arg;
    }
    void setFilepateh(String arg) {
        filepateh = arg;
    }
    String getDate() {
        return date;
    }
    String getContent() {
        return title;
    }

    String getToken() {
        return token;
    }

    String getTitle() {
        return title;
    }
    String getFilepateh() {
        return filepateh;
    }
}

public class FiringReadyRoom extends Fragment{
	
	  static final int BLACK = -16777216;  // Constant to represent the RGB binary value of black. In binary - 1111111 00000000 00000000 00000000
	  static final int WHITE = -1;  // Constant to represent the RGB binary value of white. In binary - 1111111 1111111 1111111 1111111
	  Bitmap magnified_key_image_2,keyImage,chiperImage,fileBitmap,black_white,magnified_key_image, finalimage;
	  String receiver,  attachment = null,  state;
	  String title = "Command", content = "Missile Fire";
	  String filetype[];
	  public String sdcardPath = Environment.getExternalStorageDirectory().toString() + File.separator + "KM" + "/";
	  EditText etR,chk_Password,input_receiver;
	  public int id = 0;
	  public String check_pass;
	  public String sms = null;
	  TextView tvName, etT, etC;
	  Button delete;
	  ImageView previewImg;
	  int file_amount, split_seq = 0;
	  private final int closedialog = 0;
	  private final int timeout = 1;
	  private final int ok = 2;
	  private final int error = 3;
	  private final int password=4;//201609
	  private final int SHOW_MSG = 5;
	  private final int UPDATE = 6;

	  public String tittle,message,bmsg,upmsg;

	  int index, listId, urgent = 0;
	  String file_name = "", postFile;
	  private String thumbnails = "mnt/sdcard/DCIM/.thumbnails/";
	  ArrayList<String> file_path;
	  int duration, file_size;
	  ProgressDialog sendDialog = null;
	  String selfId;
	  public int mailcount = 0;
	  Submit submit;
	  ProgressDialog dialog = null;
	  public String token ,fileName;
	  String[] form = { UserSchema._FILEPATH, UserSchema._DURATION, UserSchema._FILESIZE, UserSchema._FILENAME, UserSchema._ID };
	  boolean checkFileType = false ;

	  private ListView listView;
	  private SimpleAdapter simpleAdapter;
	  public String sender;

	  List<missileInfo> getInfo = new ArrayList<missileInfo>();
	  List<Missile_Info> getMissileInfo;
	  List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	  public FiringReadyRoom() {
	      FileContentProvider test = new FileContentProvider();
	      test.del_table(Uri.parse("content://tab.list.d2d/file_choice"));
	    }
	    
	    @Override
	    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
	    	FileContentProvider test = new FileContentProvider();
	         test.del_table(Uri.parse("content://tab.list.d2d/file_choice"));
	         
	         final View view = inflater.inflate(R.layout.blist, container, false);
	         ((Activity) getActivity()).getActionBar().setTitle("���u�ǳƫ�");

	         listView = (ListView) view.findViewById(R.id.PhoneVideoList);
	         
	         //201609
	         if(AttachParameter.priority==0){
	             list.clear();
	             //==================�`�νu==============================
	             simpleAdapter = new SimpleAdapter(getActivity(), getStaticData(),
	                             R.layout.fire_ready, new String[]{"Im_att_missile", "Im_missile_num"},
	                             new int[] {R.id.Im_att_missile, R.id.Im_missile_num}){
	                 @Override
	                 public View getView (int position, View convertView, ViewGroup parent){

	                     View v = super.getView(position, convertView, parent);
	                     final int pos=position;
	                     //id=pos;
	                     Button b_att=(Button)v.findViewById(R.id.Bn_att);
	                     Button b_pub=(Button)v.findViewById(R.id.Bn_publish);
	                     ImageView I_prevew=(ImageView)v.findViewById(R.id.Im_att_missile);

	                     b_att.setOnClickListener(new View.OnClickListener() {
	                         @Override
	                         public void onClick(View v) {
	                             Cursor ch_tmepfile = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/file_choice"), form, null, null, null);
	                             int i =ch_tmepfile.getCount();
	                             ch_tmepfile.close();
	                             if (i > 0) {
	                                // Toast.makeText(getActivity(),"��p�A�@���u��o�e�@�����u",Toast.LENGTH_SHORT).show();
	                            	 FileContentProvider test = new FileContentProvider();
	                                 test.del_table(Uri.parse("content://tab.list.d2d/file_choice"));

	                                 String[] Form = { UserSchema._ID };
	                                 Cursor change_path = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/missile_group"), Form, "filepath is not null", null, null);
	                                 if (change_path.getCount() > 0) {
	                                     change_path.moveToFirst();
	                                     for(int ia =0;ia<change_path.getCount();ia++){
	                                         int id_this = 0;
	                                         id_this = Integer.valueOf(change_path.getString(0));
	                                         ContentValues values = new ContentValues();
	                                         values.put(UserSchema._FILEPATH, "");
	                                         String where = UserSchema._ID + " = " + id_this;
	                                         getActivity().getContentResolver().update(Uri.parse("content://tab.list.d2d/missile_group"), values, where, null);
	                                         change_path.moveToNext();
	                                     }

	                                 }
	                                 change_path.close();
	                             }
	                                 Intent intent = new Intent();
	                                 id = pos;
	                                 Toast.makeText(getActivity(),"�o�̬O��"+pos+"���s",Toast.LENGTH_SHORT).show();
	                                 intent.setClass(getActivity(), f_tab.class);
	                                 // startActivityForResult(intent,0);
	                                 startActivity(intent);
	                         }
	                     });

	                     b_pub.setOnClickListener(new View.OnClickListener() {
	                         @Override
	                         public void onClick(View v) {
	                             id=pos;
	                             Cursor change_path = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/missile_group"), null, "tittle='S00"+ (pos+1) + "'and (filepath is null or filepath ='')", null, null);
	                             int i=change_path.getCount();
	                             change_path.close();
	                             if(i>0){

	                                 Toast.makeText(getActivity(),"�Х���ܶǻ������e",Toast.LENGTH_SHORT).show();
	                             }else{
	                                 LayoutInflater factory = LayoutInflater.from(getActivity());
	                                 final View v1 = factory.inflate(R.layout.check_password, null);

	                                 chk_Password = (EditText) v1.findViewById(R.id.chk_Password);
	                                 AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
	                                     dialog.setTitle("�����T�{");
	                                     dialog.setView(v1);
	                                     dialog.setPositiveButton("�T�w", new DialogInterface.OnClickListener() {
	                                         public void onClick(DialogInterface dialog, int whichButton) {
	                                             try {
	                                                 check_pass=chk_Password.getText().toString();
	                                                 new check_password().execute();
	                                             } catch (Exception e) {
	                                                 Toast.makeText(getActivity(), "���~!", Toast.LENGTH_LONG).show();
	                                             }
	                                         }

	                                 });
	                                 dialog.setNegativeButton("����", new DialogInterface.OnClickListener() {

	                                     @Override
	                                     public void onClick(DialogInterface dialog, int which) {
	                                         // TODO Auto-generated method stub

	                                     }
	                                 });
	                                 dialog.show();
	                                 //�o�G
	                                 Toast.makeText(getActivity(),"!!!!",Toast.LENGTH_SHORT).show();

	                             }

	                         }
	                     });

	                     I_prevew.setOnClickListener(new View.OnClickListener() {
	                         @Override
	                         public void onClick(View v) {
	                             String path=new String();
	                             String[] Form = { UserSchema._FILEPATH };
	                             Cursor change_path = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/missile_group"), Form, "tittle='S00"+ (pos+1) + "'and filepath is NOT null and filepath !=''", null, null);
	                             if (change_path.getCount() > 0) {
	                                 change_path.moveToFirst();
	                                 path=change_path.getString(0);
	                                 viewfile(path);
	                             }else{
	                                 //donoting
	                             }
	                             change_path.close();

	                             Toast.makeText(getActivity(),"�o�̬O��"+pos+"���s",Toast.LENGTH_SHORT).show();
	                         }
	                     });
	                     return v;
	                 }
	             };
	         }else{
	             list.clear();
	             //==============�`�q�O�P�p�L======================
	             simpleAdapter = new SimpleAdapter(getActivity(), getData(),
	                             R.layout.frieready_other, new String[]{"img","title","date","info", "Im_missile_num"},
	                             new int[] {R.id.img,R.id.title,R.id.date,R.id.info,R.id.Im_missile_num}){
	                 @Override
	                 public View getView (int position, View convertView, ViewGroup parent) {
	                     int pos=position;
	                     View v = super.getView(position, convertView, parent);

	                     AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
	                         @Override
	                         public void onItemClick(AdapterView<?> parent, View view, int position, long listId) {


	                             Toast.makeText(getActivity().getApplicationContext(), "�I��", Toast.LENGTH_LONG).show();

	                         }
	                     };

	                     listView.setOnItemClickListener(onItemClickListener);
	                     return v;
	                 }

	             };

	         }
	         //201609 END

	         listView.setAdapter(simpleAdapter);
	         submit = new Submit();
	         return view;
	    }
	    
	    private List<Map<String, Object>> getData() {

	        getInfo.clear();
	        String[] Form = { UserSchema._TITTLE, UserSchema._CONTENT, UserSchema._FILEPATH, UserSchema._DATE, UserSchema._MESSAGETOKEN,UserSchema._FILECOUNT };
	        missileInfo tempinfo = new missileInfo();
	        // �ϥαH��̥h���X �L�h���w�gŪ�L��²�T�H�Υ��Q�R�������,�� tittle��userstatus�h�ˬd
	        Cursor msg_cursor = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), Form, "msg='retrievable'", null, null);
	        if (msg_cursor.getCount() > 0) {
	            msg_cursor.moveToFirst();
	            for (int i = 0; i < msg_cursor.getCount(); i++) {
	                tempinfo.setTitle(msg_cursor.getString(0));
	                tempinfo.setContent(msg_cursor.getString(1));
	                tempinfo.setFilepateh(msg_cursor.getString(2));
	                tempinfo.setDate(msg_cursor.getString(3));
	                tempinfo.setToken(msg_cursor.getString(4));

	                getInfo.add(tempinfo);
	                mailcount++;
	                tempinfo = new missileInfo();
	                msg_cursor.moveToNext();
	            }
	        }
	        msg_cursor.close();

	        Map<String, Object> map = new HashMap<String, Object>();
	        for (int i = mailcount - 1; i >= 0; i--) {
	            map = new HashMap<String, Object>();
	            // �o��DB�O�ˬd�L�h�O�_�����������ɮסA�p�G�����������ɮסA�h����ĸ����Ϯ׳q���ϥΪ�
	            // put info array into list
	            map.put("title", getInfo.get(i).getTitle());
	            map.put("info", getInfo.get(i).getContent());
	            map.put("date", getInfo.get(i).getDate());
	            // �ˬd�ɮ������A��m�۹������ɮ��������Ϥ�
	             if(getInfo.get(i).getFilepateh()!=null || !(getInfo.get(i).getFilepateh().equals(""))){
	                boolean[] checktype = new boolean[AttachParameter.filetype];
	                checktype = AttachParameter.checktype(getInfo.get(i).getFilepateh());
	                Pattern patternmovie = Pattern.compile(".*.movie");
	                Matcher matchermovie = patternmovie.matcher(getInfo.get(i).getFilepateh());

	                if (checktype[AttachParameter.music]) // music set a note
	                {
	                    map.put("img", R.drawable.notes);
	                } else if (checktype[AttachParameter.video]) // video set a camera
	                {

	                    String videoSegment[] = getInfo.get(i).getFilepateh().split("&");
	                    Bitmap filebitmap;
	                    //�]��d2d�O��@�ɮ� �S������ �ҥH�o��n�ˬd
	                    if(videoSegment.length==1){
	                        filebitmap = android.media.ThumbnailUtils.createVideoThumbnail(sdcardPath + getInfo.get(i).getFilepateh(), MediaStore.Images.Thumbnails.MICRO_KIND);
	                    }else{
	                        filebitmap = android.media.ThumbnailUtils.createVideoThumbnail(sdcardPath + videoSegment[1], MediaStore.Images.Thumbnails.MICRO_KIND);
	                    }

	                    filebitmap = ThumbnailUtils.extractThumbnail(filebitmap, 60, 60);
	                    File file = new File(thumbnails, "temp_" + i + ".jpg");
	                    try {
	                        OutputStream outStream = new FileOutputStream(file);
	                        filebitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);

	                        outStream.flush();
	                        outStream.close();
	                    } catch (FileNotFoundException e) {
	                        // TODO Auto-generated catch block
	                        e.printStackTrace();
	                        map.put("img", R.drawable.missile0);

	                    } catch (IOException e) {
	                        // TODO Auto-generated catch block
	                        e.printStackTrace();
	                        map.put("img", R.drawable.missile0);
	                    }catch (Exception e) {
	                        // TODO Auto-generated catch block
	                        e.printStackTrace();
	                        map.put("img", R.drawable.missile0);
	                    }
	                    map.put("img", thumbnails + "temp_" + i + ".jpg");

	                } else if (checktype[AttachParameter.photo]) // photo set photo
	                {
	                    map.put("img", sdcardPath + getInfo.get(i).getFilepateh().replace("&", ""));

	                } else if (matchermovie.find()) {
	                    map.put("img", R.drawable.iconvideo);

	                } else
	                    map.put("img", R.drawable.missile0);

	            }
	            list.add(map);
	        }

	        return list;
	    }

	    private List<Map<String, Object>> getStaticData() {
	        getInfo.clear();
	        missileInfo tempinfo = new missileInfo();


	        // �]���O�`�ΡA�ҥH�������w���]�w�����
	        String[] missile = { UserSchema._ID, UserSchema._TITTLE, UserSchema._CONTENT,UserSchema._FILEPATH};
	        Cursor missile_cursor = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/missile_group"), missile, "state = 'live'", null, null);
	        if (missile_cursor.getCount() > 0) {
	            missile_cursor.moveToFirst();
	            for (int x = 0; x < missile_cursor.getCount(); x++) {
	                // �N²�T�s�J�۩w�q������(tempinfo)
	                tempinfo.setTitle(missile_cursor.getString(1));
	                tempinfo.setContent(missile_cursor.getString(2));
	                tempinfo.setFilepateh(missile_cursor.getString(3));
	                // �N�ۭq����s�J��getinfo
	                getInfo.add(tempinfo);
	                tempinfo = new missileInfo();
	                missile_cursor.moveToNext();
	            }

	        }
	        missile_cursor.close();
		/*
		 * �p�P�W�z���@�k�A���O�o��~�O�u���n�� key value���Ʊ��Avalue�N�O�Ӧ۩�W�z��getinfo
		 * �Ҧp²�TA��notititle���O����?�qgetinfo�����X�� ²�TA��notiinfo�O����?�@�ˤ]�O�qgetinfo�����X��
		 */
	        Map<String, Object> map = new HashMap<String, Object>();
	        for (int i = 0; i < getInfo.size(); i++) { // put info array into
	            // list

	            map = new HashMap<String, Object>();
	            // �N�C��²�T����T�s�J��۩w�q��adapter��������
	            if(getInfo.get(i).getFilepateh()!=null){
	                map.put("Im_att_missile", getInfo.get(i).getFilepateh());
	            }
	            if((getInfo.get(i).getTitle()).equals("S001")){
	                map.put("Im_missile_num", R.drawable.missile01);
	            }
	            else if(getInfo.get(i).getTitle().equals("S002")){
	                map.put("Im_missile_num", R.drawable.newmissile);
	            }
	            else if(getInfo.get(i).getTitle().equals("S003")){
	                map.put("Im_missile_num", R.drawable.newmissile2);
	            }
	            if(getInfo.get(i).getTitle().equals("S004")){
	                map.put("Im_missile_num", R.drawable.newmissile3);
	            }

	            list.add(map);
	        }

	        return list;
	    }
	    
	    public void viewfile(String path) {
	        boolean[] checktype = new boolean[AttachParameter.filetype];
	        checktype = AttachParameter.checktype(path);
	        if (checktype[AttachParameter.music]) {
	            Intent it = new Intent(Intent.ACTION_VIEW);
	            File file = new File(path);
	            it.setDataAndType(Uri.fromFile(file), "audio/*");
	            startActivity(it);
	        } else if (checktype[AttachParameter.video]) {
	            Intent it = new Intent(Intent.ACTION_VIEW);
	            File file = new File(path);
	            it.setDataAndType(Uri.fromFile(file), "video/*");
	            startActivity(it);
	        } else if (checktype[AttachParameter.photo]) {
	            Intent it = new Intent(Intent.ACTION_VIEW);
	            File file = new File(path);
	            it.setDataAndType(Uri.fromFile(file), "image/*");
	            startActivity(it);
	        } else
	            Toast.makeText(getActivity(), "not match file", 4000).show();
	    }
	    
	    private class check_password extends AsyncTask<Void, Void, String> { // implement
	        // thread
	        public String where;
	        public String path;

	        // Asyntask �e�m�@�~
	        @Override
	        protected void onPreExecute() {
	            mHandler.obtainMessage(password).sendToTarget();
	        }

	        @Override
	        protected String doInBackground(Void... params) {
	            String er = "no&";
	            User check_state=new User();
	            String res=check_state.check_password("password="+check_pass);

	            return res;
	        }

	        protected void onPostExecute(String res) {
	            sendDialog.dismiss();
	            Boolean reslut=AttachParameter.chechsuccess(res);
	            if(reslut){
	                LayoutInflater factory = LayoutInflater.from(getActivity());
	                final View v1 = factory.inflate(R.layout.input_reciever, null);
	                input_receiver = (EditText) v1.findViewById(R.id.input_receiver);

	                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
	                dialog.setTitle("��J������");
	                dialog.setView(v1);
	                dialog.setPositiveButton("�T�w", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                        try {
	                            receiver=input_receiver.getText().toString();
	                            if (receiver.equals("")) {
	                                Toast.makeText(getActivity(), "�����̤��i���ť�", Toast.LENGTH_LONG).show();
	                            }else{
	                                title="S00"+(id+1);
	                                content = "�ǳƤ@�žԳ�";
	                                fileUpload("write");
	                            }
	                        } catch (Exception e) {
	                            Toast.makeText(getActivity(), "���~!", Toast.LENGTH_LONG).show();
	                        }
	                    }

	                });
	                dialog.setNegativeButton("����", new DialogInterface.OnClickListener() {

	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                        // TODO Auto-generated method stub

	                    }
	                });
	                dialog.show();
	            }else{
	                Toast.makeText(getActivity(),"�K�X���~�A�бq�s��J",Toast.LENGTH_SHORT).show();
	            }


	        }
	    }
	    
	    public void fileUpload(String arg) {
	        // �ˬd�ϥΪ̦��S����ܭn�W�Ǫ��ɮ�,��ܦn���ɮ׷|�g�Jfile_choice��table��
	        file_path = new ArrayList<String>();
	        Cursor up_file_cursor = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/file_choice"), form, null, null, null);
	        if (up_file_cursor.getCount() > 0) {
	            dialog = ProgressDialog.show(getActivity(), "�еy��", "��ƳB�z��", true);
	            dialog.show();
	            up_file_cursor.moveToFirst();
	            file_path.add(up_file_cursor.getString(0));
	            selfId = randomString(20);
	            postFile = new String();
	            for (index = 0; index < up_file_cursor.getCount(); index++) {
	                boolean[] checktype = new boolean[AttachParameter.filetype];
	                checktype = AttachParameter.checktype(attachment);
	                // �p�G��ܪ��O�v���A�h�p��L���v�����סB�j�p�A�ت��O�n�ǵ�ffmpeg�ϥ�
	                if (checktype[AttachParameter.video]||checktype[AttachParameter.music]) {
	                    File file = new File(file_path.get(index));
	                    // �}�ҭp���ɮת��רæs�JattachSize
	                    file_size = (int) file.length();
	                    file_name = file.getName();
	                    postFile = postFile + "&file_name" + index + "_0=" + file_name;
	                    duration = 0;
	                    file_amount = 1;

	                    //2016/06/30�s�W�ק�
	                    try {
	                        FileInputStream inputStream = new FileInputStream(new File(file_path.get(index)));
	                        byte[] data = new byte[1024];
	                        FileOutputStream outputStream =new FileOutputStream(new File(Environment.getExternalStorageDirectory().toString() + File.separator + "KM" + "/"+file_name));
	                        while (inputStream.read(data) != -1) {
	                            outputStream.write(data);
	                        }
	                        inputStream.close();
	                        outputStream.close();
	                    } catch (FileNotFoundException e) {
	                        // TODO Auto-generated catch block
	                        e.printStackTrace();
	                    }  catch (IOException e) {
	                        // TODO Auto-generated catch block
	                        e.printStackTrace();
	                    }

	                    ContentValues values = new ContentValues();
	                    values.put(UserSchema._FILEPATH, Environment.getExternalStorageDirectory().toString() + File.separator + "KM" + "/"+file_name);//2016/06/30�s�W�ק�
	                    values.put(UserSchema._FILERECORD, "file"+index + "_0");
	                    values.put(UserSchema._FILECHECK, 0);
	                    values.put(UserSchema._FILENAME, file_name);
	                    values.put(UserSchema._SELFID, selfId);
	                    getActivity().getContentResolver().insert(Uri.parse("content://tab.list.d2d/temp_file"), values);
	                    //dialog.dismiss();
	                    state = "write";
	                    dialog.dismiss();
	                    new SendandAttach().execute();
	                }
	                //2016/07/20�s�W�ק�//
	                else if(checktype[AttachParameter.photo]){
	                    //20160905���Q�\��
	                    File file = new File(file_path.get(index));
	                    // �}�ҭp���ɮת��רæs�JattachSize
	                    file_size = (int) file.length();
	                    file_name = file.getName();
	                    file_amount = 1;
	                    String[] tempfilename = new String[file_amount];
	                    for (int i = 0; i < file_amount; i++) {
	                        tempfilename[i] = "file_name" + index + "_" + String.valueOf(i) + "=" + file_name;
	                        postFile = postFile + "&" + tempfilename[i];
	                    }
	                    duration = 0;

	                    checkFileType=true;

	                    //2016/06/30�s�W�ק�(�ɮת�copy)
	                    try {
	                        FileInputStream inputStream = new FileInputStream(new File(file_path.get(index)));
	                        byte[] data = new byte[1024];
	                        FileOutputStream outputStream =new FileOutputStream(new File(Environment.getExternalStorageDirectory().toString() + File.separator + "KM" + "/"+file_name));
	                        while (inputStream.read(data) != -1) {
	                            outputStream.write(data);
	                        }
	                        inputStream.close();
	                        outputStream.close();
	                    } catch (FileNotFoundException e) {
	                        // TODO Auto-generated catch block
	                        e.printStackTrace();
	                    }  catch (IOException e) {
	                        // TODO Auto-generated catch block
	                        e.printStackTrace();
	                    }
	                    state = "write";
	                    dialog.dismiss();
	                    new SendandAttach().execute();

	                }else{
	                    //2016/07/04�s�W�ק�
	                    Toast.makeText(getActivity(), "�A�ҿ諸�ɮ��������䴩�A�Э��s���", Toast.LENGTH_LONG).show();
	                    dialog.dismiss();
	                }
	                up_file_cursor.moveToNext();
	            }
	        } else {
	            Toast.makeText(getActivity(), "�|����ܪ��[�ɮסA�Э��s���", Toast.LENGTH_LONG).show();
	        }

	        state = "write";
	        up_file_cursor.close();
	        index = 0;
	        // �I�s�۩w�q���禡

	    }
	    
	    private class SendandAttach extends AsyncTask<Void, Void, String> { // implement
	        // thread
	        public String where;
	        public String path;

	        // Asyntask �e�m�@�~
	        @Override
	        protected void onPreExecute() {

	            mHandler.obtainMessage(closedialog).sendToTarget(); // �ǰe�n�D��slist���T����handler
	            // �}�Ҹ�ƶǰedialog
	        }

	        @Override
	        protected String doInBackground(Void... params) {
	            String er = "no&";
	            String file0_0;
	            File firstfile = null;
	            boolean[] checktype = null;//2016/06/30�s�W
	            System.out.println("state�|��"+state);
	            state="write";
	            if (state.equals("write")) {
	                //20160905�Ǫ����Q���ɧP�_�b��
	                if(AttachParameter.priority==0){
	                    attachment = visualEncrypt();
	                }
	                /// ex : storage/emulated/0/DCIM/100ANDRO/MOV_0259.mp4
	                File file = new File(attachment);
	                String filename = file.getName();
	                filetype = filename.split("\\.");
	                Cursor check_finish_cursor = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/temp_file"), new String[] { UserSchema._FILEPATH }, "filecheck='0' and filerecord='file0_0' and selfid='" + selfId + "'", null, null);
	                if(check_finish_cursor.getCount()>0){
	                    check_finish_cursor.moveToFirst();
	                    file0_0=check_finish_cursor.getString(0);
	                    firstfile =new File(file0_0);

	                    //2016/06/30�s�W
	                    checktype = new boolean[AttachParameter.filetype];
	                    //2016/06/30�s�W
	                    checktype = AttachParameter.checktype(file.getName());
	                }
	                check_finish_cursor.close();
	                // 2013/4/14 4$�R��filecount�Ppost
	                // 2013/8/10/ �ɤWfilecnt�ç�e��;�令&
	                System.out.println("postFile==" + postFile);
	                // ���]�wrequest�r��

	                // 2013/11/08 ���� �ק�W�Ǹ�Tmetadata
	                submit.setrequestString("subject=" + title + "&content=" + content +
	                        "&selfid="+selfId+"&receiver=" + receiver +
	                        "&filecnt=" + file_amount + "&duration=" + duration +
	                        "&filename=" + filename + "&filetype=" + filetype[1] +
	                        "&filepath=" + attachment + "&length=" + file_size +
	                        "&firstlength=" + (int)firstfile.length()+"&urgent=" + urgent  + postFile);

	                String resp = submit.submit1(Login.latest_cookie);

	                // �o��O�Ψ��ˬd����̬O�_�s�b��server���A�Y���s�b�h�����o�����W��
	                if (!(AttachParameter.chechsuccess(resp))) {
	                    if(resp.equalsIgnoreCase("timeout")){
	                        er = "yes&timeout";
	                    }else{
	                        er = "yes&";
	                    }
	                } else {
	                    //UPDATE TOKEN�ܯ�Z��
	                    String[] Form = { UserSchema._ID };
	                    Cursor change_token = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/temp_content"), Form, "selfid='" + selfId + "'", null, null);
	                    if (change_token.getCount() > 0) {
	                        change_token.moveToFirst();
	                        int id_this = 0;
	                    id_this = Integer.valueOf(change_token.getString(0));
	                    ContentValues values = new ContentValues();
	                    values.put(UserSchema._MESSAGETOKEN, resp.replace("ret=0&token=", ""));
	                    String where = UserSchema._ID + " = " + id_this;
	                    getActivity().getContentResolver().update(Uri.parse("content://tab.list.d2d/temp_content"), values, where, null);

	                }
	                change_token.close();

//	                    //�Ĥ@���W�ǧ�  ��sFIRST��TRUE
//	                    Cursor change_first = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/temp_content"), Form, "selfid='" + selfId + "'", null, null);
//	                    if (change_first.getCount() > 0) {
//	                        change_first.moveToFirst();
//	                        int id_this = 0;
//	                        id_this = Integer.valueOf(change_first.getString(0));
//	                        ContentValues values = new ContentValues();
//	                        values.put(UserSchema._FIRST, "true");
//	                        String where = UserSchema._ID + " = " + id_this;
//	                        getActivity().getContentResolver().update(Uri.parse("content://tab.list.d2d/temp_content"), values, where, null);
	//
//	                    }
//	                    change_first.close();

	                    //20160903 �����Ϊ�
	                    token = resp.replace("ret=0&token=", "");

	                    Cursor up_tempfile = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/temp_file"), Form, "selfid='" + selfId + "' and filecheck='0'", null, null);
	                    if (up_tempfile.getCount() > 0) {
	                        up_tempfile.moveToFirst();
	                        ContentValues values = new ContentValues();
	                        values.put(UserSchema._FILECHECK, 1);

	                        //2016/06/30�s�W�ק�
	                        if (checktype[AttachParameter.music] || checktype[AttachParameter.photo]||checktype[AttachParameter.video]){
	                            values.put(UserSchema._MESSAGETOKEN, token);
	                        }

	                        int id_this = Integer.parseInt(up_tempfile.getString(0));
	                        String where = UserSchema._ID + " = " + id_this;
	                        getActivity().getContentResolver().update(Uri.parse("content://tab.list.d2d/temp_file"), values, where, null);
	                    }
	                    up_tempfile.close();

	                    //20160903 �����Ϊ�
	                    if (checktype[AttachParameter.photo]){
	                        //20160801 ��s�Ϥ��Ҧ���token
	                        Cursor uptoken_cursor = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/temp_file"), Form, "selfid='"+selfId+"'", null, null);
	                        if (uptoken_cursor.getCount() > 0) {
	                            ContentValues values = new ContentValues();
	                            values = new ContentValues();
	                            uptoken_cursor.moveToFirst();
	                            values.put(UserSchema._MESSAGETOKEN, token);
	                            for (int i = 0; i < uptoken_cursor.getCount(); i++) {
	                                int id_this = Integer.parseInt(uptoken_cursor.getString(0));
	                                String file_where = UserSchema._ID + " = " + id_this;
	                                getActivity().getContentResolver().update(Uri.parse("content://tab.list.d2d/temp_file"), values, file_where, null);
	                                uptoken_cursor.moveToNext();
	                            }
	                        }
	                        uptoken_cursor.close();
	                    }
	                    // �Ĥ@���ǧ�
	                }
	            }
	            return er;
	        }

	        protected void onPostExecute(String er) {
	            String[]resp = er.split("&");
	            sendDialog.dismiss();
	            if (resp[0].equals("yes")) {

	                if(resp.length>1 &&resp[1].equalsIgnoreCase("timeout")){
	                    mHandler.obtainMessage(timeout).sendToTarget(); // �ǰe�n�D��slist���T����handler
	                }
	                else{
	                    mHandler.obtainMessage(error).sendToTarget();
	                }

	            } else {
	                // �ǰe�n�D��slist���T����handler
	                FileContentProvider test = new FileContentProvider();
	                test.del_table(Uri.parse("content://tab.list.d2d/file_choice"));
	                mHandler.obtainMessage(ok).sendToTarget();

	            }
	        }
	    }
	    
	    public String visualEncrypt() {
	        // TODO Auto-generated method stub
	        String in_file = "";
	        Cursor up_file_cursor = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/file_choice"), form, null, null, null);
	        String filetype[]  = null ;

	        if(up_file_cursor.getCount()>0){
	            up_file_cursor.moveToFirst();
	            in_file = up_file_cursor.getString(0);
	        }up_file_cursor.close();
	        OutputStream fileKeyMagnifiedOut = null;
	        OutputStream fileKeyChiperOut = null;
	        File file = new File(in_file);
	        fileName = file.getName();
	        filetype = fileName.split("\\.");
	        //File names and paths for the magnified images
	        AttachParameter.save_key_magnified_path = AttachParameter.sdcardPath + "key_" + fileName ;//2016/07/26�ק�[�J key_magnified
	        AttachParameter.save_cipher_magnified_path = AttachParameter.sdcardPath + "cipher_" + fileName ;//cipher_magnified
	        AttachParameter.image_decrypt_file= AttachParameter.sdcardPath + "reslut.png";
	        AttachParameter.key_magnified_file = new File(AttachParameter.save_key_magnified_path);
	        AttachParameter.cipher_magnified_file = new File(AttachParameter.save_cipher_magnified_path);

	        AttachParameter.bw_file = new File(AttachParameter.sdcardPath + "Black_White.png");
	        AttachParameter.key_file = new File(AttachParameter.sdcardPath + "key.png");
	        fileBitmap = BitmapFactory.decodeFile(in_file);

	        if (!AttachParameter.key_magnified_file.exists()){
	            //====================================================================
	            black_white= gray2Binary(fileBitmap);
	            System.out.println(fileBitmap.getWidth());
	            System.out.println(fileBitmap.getHeight());
	            //====================================================================

	            //====================================================================
	            keyImage = Bitmap.createBitmap(fileBitmap.getWidth(),fileBitmap.getHeight(), Bitmap.Config.ARGB_4444);
	            try {
	                SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG");

	                for(int i = 0; i < keyImage.getHeight(); i++){
	                    for(int j = 0; j < keyImage.getWidth(); j++){

	                        int result = secureRandomGenerator.nextInt(100);
	                        if(result < 50){
	                            keyImage.setPixel(j, i, WHITE);
	                        }
	                        else{
	                            keyImage.setPixel(j, i, BLACK);
	                        }
	                    }
	                }
	            } catch (NoSuchAlgorithmException e1) {
	                // TODO Auto-generated catch block
	                e1.printStackTrace();
	            }

	            //====================================================================

	            //====================================================================
	            magnified_key_image = Bitmap.createBitmap(keyImage.getWidth()*2,keyImage.getHeight()*2, Bitmap.Config.ARGB_4444);

	            for(int i = 0; i < keyImage.getHeight(); i++){
	                for(int j = 0; j < keyImage.getWidth(); j++){
	                    if(keyImage.getPixel(j, i) == BLACK){
	                        //
	                        //                     |X| |
	                        //                     -----
	                        //                     | |X|
	                        //
	                        magnified_key_image.setPixel(j*2, i*2, BLACK);
	                        magnified_key_image.setPixel(j*2+1, i*2, WHITE);
	                        magnified_key_image.setPixel(j*2, i*2+1, WHITE);
	                        magnified_key_image.setPixel(j*2+1, i*2+1, BLACK);

	                    }
	                    else{
	                        //
	                        //                     | |X|
	                        //                     -----
	                        //                     |X| |
	                        //
	                        magnified_key_image.setPixel(j*2, i*2, WHITE);
	                        magnified_key_image.setPixel(j*2+1, i*2, BLACK);
	                        magnified_key_image.setPixel(j*2, i*2+1, BLACK);
	                        magnified_key_image.setPixel(j*2+1, i*2+1, WHITE);
	                    }
	                }
	                System.out.println("magnified_key�`width�O"+keyImage.getHeight()+" �ثewidth�O"+i);
	            }
	            System.out.println("magnified_key�����o");
	            try{

	                fileKeyMagnifiedOut = new FileOutputStream(AttachParameter.key_magnified_file);
	                magnified_key_image.compress(Bitmap.CompressFormat.PNG, 100, fileKeyMagnifiedOut);

	                fileKeyMagnifiedOut.flush();
	                fileKeyMagnifiedOut.close();
	            }catch(Exception e){
	                e.printStackTrace();
	            }finally{
	                try{
	                    if(fileKeyMagnifiedOut !=null){
	                        fileKeyMagnifiedOut.close();
	                    }
	                }catch(IOException e){
	                    e.printStackTrace();
	                }
	            }
	        }
	        Cursor magnified_key = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/temp_file"), null, "selfid='"+selfId +"' and filerecord = 'file0_0'", null, null);
	        if(magnified_key.getCount()>0){
	            //do nothing
	        }else{
	            ContentValues values = new ContentValues();
	            values = new ContentValues();
	            values.put(UserSchema._FILEPATH,  AttachParameter.save_key_magnified_path);
	            values.put(UserSchema._FILERECORD, "file0_0");
	            values.put(UserSchema._FILECHECK, 0);
	            values.put(UserSchema._SELFID, selfId);
	            values.put(UserSchema._FILENAME, fileName);//2016/07/26
	            //�٭n�[�ۭqID(��SERVER)
	            getActivity().getContentResolver().insert(Uri.parse("content://tab.list.d2d/temp_file"), values);

	        }
	        magnified_key.close();
	        //magnified ok
	        //====================================================================

	        //====================================================================
	        if(!AttachParameter.cipher_magnified_file.exists()){
	            chiperImage = Bitmap.createBitmap(black_white.getWidth(),black_white.getHeight(), Bitmap.Config.ARGB_4444);

	            for( int i = 0; i<chiperImage.getHeight(); i++){
	                for(int j = 0; j<chiperImage.getWidth(); j++){
	                    if(keyImage.getPixel(j, i) == BLACK){
	                        int temp = Get_and_Flip(black_white, i, j);
	                        chiperImage.setPixel(j, i, temp);
	                    }
	                    else{
	                        chiperImage.setPixel(j, i, black_white.getPixel(j, i));
	                    }
	                }
	                System.out.println("chiperImage�`Height�O"+chiperImage.getHeight()+" �ثeHeight�O"+i);
	            }
	            System.out.println("chiperImage�����o");

	            magnified_key_image_2 = Bitmap.createBitmap(chiperImage.getWidth()*2,chiperImage.getHeight()*2, Bitmap.Config.ARGB_4444);
	            for(int i = 0; i < chiperImage.getHeight(); i++){
	                for(int j = 0; j < chiperImage.getWidth(); j++){
	                    if(chiperImage.getPixel(j, i) == BLACK){
	                        //
	                        //                     |X| |
	                        //                     -----
	                        //                     | |X|
	                        //
	                        magnified_key_image_2.setPixel(j*2, i*2, BLACK);
	                        magnified_key_image_2.setPixel(j*2+1, i*2, WHITE);
	                        magnified_key_image_2.setPixel(j*2, i*2+1, WHITE);
	                        magnified_key_image_2.setPixel(j*2+1, i*2+1, BLACK);

	                    }
	                    else{
	                        //
	                        //                     | |X|
	                        //                     -----
	                        //                     |X| |
	                        //
	                        magnified_key_image_2.setPixel(j*2, i*2, WHITE);
	                        magnified_key_image_2.setPixel(j*2+1, i*2, BLACK);
	                        magnified_key_image_2.setPixel(j*2, i*2+1, BLACK);
	                        magnified_key_image_2.setPixel(j*2+1, i*2+1, WHITE);
	                    }
	                }
	                System.out.println("magnified_key_image_2�`Height�O"+magnified_key_image_2.getHeight()+" �ثeHeight�O"+i);
	            }
	            System.out.println("magnified_key_image_2�����o");
	            try{

	                fileKeyChiperOut = new FileOutputStream(AttachParameter.cipher_magnified_file);
	                magnified_key_image_2.compress(Bitmap.CompressFormat.PNG, 100, fileKeyChiperOut);

	                fileKeyChiperOut.flush();
	                fileKeyChiperOut.close();
	            }catch(Exception e){
	                e.printStackTrace();
	            }finally{
	                try{
	                    if(fileKeyChiperOut !=null){
	                        fileKeyChiperOut.close();
	                    }
	                }catch(IOException e){
	                    e.printStackTrace();
	                }
	            }
	        }

	        Cursor chiperImage = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/temp_file"), null, "selfid='"+selfId +"' and filerecord = 'file0_1'", null, null);
	        if(chiperImage.getCount()>0){
	            //do nothing
	        }else{
	            ContentValues values = new ContentValues();
	            values = new ContentValues();
	            values.put(UserSchema._FILEPATH, AttachParameter.save_cipher_magnified_path);
	            values.put(UserSchema._FILERECORD, "file0_1");
	            values.put(UserSchema._FILECHECK, 0);
	            values.put(UserSchema._SELFID, selfId);
	            values.put(UserSchema._FILENAME, fileName);//2016/07/26
	            //�٭n�[�ۭqID(��SERVER)
	            getActivity().getContentResolver().insert(Uri.parse("content://tab.list.d2d/temp_file"), values);
	        }
	        chiperImage.close();
	        //====================================================================
	        return  AttachParameter.save_key_magnified_path;
	    }

	    public Bitmap gray2Binary(Bitmap graymap) {

	        int width = graymap.getWidth();
	        int height = graymap.getHeight();

	        Bitmap binarymap = null;
	        binarymap = graymap.copy(Bitmap.Config.ARGB_8888, true);

	        for (int i = 0; i < width; i++) {
	            for (int j = 0; j < height; j++) {

	                int col = binarymap.getPixel(i, j);

	                int alpha = col & 0xFF000000;

	                int red = (col & 0x00FF0000) >> 16;
	                int green = (col & 0x0000FF00) >> 8;
	                int blue = (col & 0x000000FF);

	                int gray = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);

	                if (gray <= 95) {
	                    gray = 0;
	                } else {
	                    gray = 255;
	                }

	                int newColor = alpha | (gray << 16) | (gray << 8) | gray;

	                binarymap.setPixel(i, j, newColor);
	                //System.out.println("�`height�O"+height+" �ثeheight�O"+j);
	            }
	            System.out.println("�`width�O"+width+" �ثewidth�O"+i);
	        }
	        System.out.println("�����o");
	        return binarymap;
	    }

	    public static int Get_and_Flip(Bitmap img, int i, int j){

	        int initial = img.getPixel(j, i);

	        if(initial == BLACK){
	            return WHITE;
	        }
	        else{
	            return BLACK;
	        }
	    }
	    
	    public String randomString(int len) {
	        String str = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < len; i++) {
	            int idx = (int)(Math.random() * str.length());
	            sb.append(str.charAt(idx));
	        }
	        return sb.toString();
	    }
	    
	    // ��qattachment�^�ӮɡA��ܭ��ҿ�ܪ��ɮצbwritepage�W�C
	    public void onResume() {
	        super.onResume();
	        mailcount = 0;
	        File file;
	        if(AttachParameter.priority==0){
	            Cursor ch_tmepfile = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/file_choice"), form, null, null, null);
	            if (ch_tmepfile.getCount() > 0) {
	                ch_tmepfile.moveToFirst();
	                attachment = ch_tmepfile.getString(0);
	                file = new File(attachment);

	                String[] Form = { UserSchema._ID };
	                Cursor change_path = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/missile_group"), Form, "tittle='S00" + (id+1) + "'", null, null);
	                if (change_path.getCount() > 0) {
	                    change_path.moveToFirst();
	                    int id_this = 0;
	                    id_this = Integer.valueOf(change_path.getString(0));
	                    ContentValues values = new ContentValues();
	                    values.put(UserSchema._FILEPATH, attachment);
	                    String where = UserSchema._ID + " = " + id_this;
	                    getActivity().getContentResolver().update(Uri.parse("content://tab.list.d2d/missile_group"), values, where, null);

	                }
	                change_path.close();
	            }
	            ch_tmepfile.close();

	        }else {
	            Cursor ch_tmepfile = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/file_choice"), form, null, null, null);
	            if (ch_tmepfile.getCount() > 0) {
	                ch_tmepfile.moveToFirst();
	                attachment = ch_tmepfile.getString(0);
	                file = new File(attachment);
	            }
	            ch_tmepfile.close();

	        }

	        list.clear();
	        if(AttachParameter.priority==0){
	            list = getStaticData();
	        }else{
	            list = getData();
	        }

	        simpleAdapter.notifyDataSetChanged();

	    }

	    @Override
	    public void onDestroy() {
	        super.onDestroy();
	        // un-register BroadcastReceiver
	    }
	    
	    private Handler mHandler = new Handler() {

	        // ����k�bui�u�{�B��
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	                case closedialog:
	                    sendDialog = ProgressDialog.show(getActivity(), "�еy��", "��ƳB�z��", true);
	                    sendDialog.show();
	                    break;
	                case timeout:
	                    AlertDialog.Builder Dialog0 = new AlertDialog.Builder(getActivity()); // Dialog
	                    Dialog0.setTitle("�s�u�O��");
	                    Dialog0.setMessage("�аݬO�_�n���e?");
	                    Dialog0.setIcon(android.R.drawable.ic_dialog_info);
	                    Dialog0.setNeutralButton("�T�w", new DialogInterface.OnClickListener() {
	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {
	                            new SendandAttach().execute();

	                        }
	                    });
	                    Dialog0.setNegativeButton("����", new DialogInterface.OnClickListener() { // ���Uabort
	                        // �Nthread����
	                        // ����progressbar
	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {
	                            FileContentProvider test = new FileContentProvider();
	                            test.del_table(Uri.parse("content://tab.list.d2d/file_choice"));
	                            previewImg.setVisibility(View.INVISIBLE);
	                            tvName.setVisibility(View.INVISIBLE);
	                            delete.setVisibility(View.INVISIBLE);
	                            etR.setText("");
	                            etT.setText("");
	                            etC.setText("");
	                        }
	                    });
	                    Dialog0.show();
	                    break;
	                case error:
	                    AlertDialog.Builder Dialog = new AlertDialog.Builder(getActivity()); // Dialog
	                    Dialog.setTitle("");
	                    Dialog.setMessage("�ǰe���ѡA�d�L�������");
	                    Dialog.setIcon(android.R.drawable.ic_dialog_info);
	                    Dialog.setNeutralButton("�T�w", new DialogInterface.OnClickListener() {
	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {
	                            etR.setText("");

	                        }
	                    });
	                    Dialog.show();
	                    break;
	                case ok:
	                    // �ǰe���\����ܦ��\����
	                    AlertDialog.Builder Dialog1 = new AlertDialog.Builder(getActivity()); // Dialog
	                    Dialog1.setTitle("");
	                    Dialog1.setMessage("�ǰe���\");
	                    Dialog1.setIcon(android.R.drawable.ic_dialog_info);
	                    Dialog1.setNeutralButton("�T�w", new DialogInterface.OnClickListener() {
	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {

	                        }
	                    });
	                    Dialog1.show();
	                    break;
	                case password:
	                    sendDialog = ProgressDialog.show(getActivity(), "�еy��", "�K�X�T�{��", true);
	                    sendDialog.show();
	                    break;

	                case SHOW_MSG:
	                    AlertDialog.Builder Dialog2 = new AlertDialog.Builder(getActivity()); // Dialog
	                    Dialog2.setTitle(tittle);
	                    Dialog2.setMessage(message);
	                    Dialog2.setIcon(android.R.drawable.ic_dialog_info);
	                    Dialog2.setPositiveButton(bmsg, new DialogInterface.OnClickListener() { // ���Uabort
	                        // �Nthread����
	                        // ����progressbar
	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {

	                        }
	                    });
	                    Dialog2.show();
	                    tittle="";
	                    message="";
	                    bmsg="";
	                    onResume();
	                    break;


	                case UPDATE:
	                    dialog.setMessage(upmsg);
	                    upmsg = "";
	            }
	        }
	    };
}
