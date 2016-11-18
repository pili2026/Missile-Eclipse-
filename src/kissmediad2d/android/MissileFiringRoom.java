package kissmediad2d.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
import login.submit1.Login;
import login.submit1.Submit;
import tab.list.AttachParameter;
import tab.list.FileContentProvider;
import tab.list.FileContentProvider.UserSchema;
import tab.list.FileUtils;

public class MissileFiringRoom extends Fragment{
	
	 static final int BLACK = -16777216;  // Constant to represent the RGB binary value of black. In binary - 1111111 00000000 00000000 00000000
	    static final int WHITE = -1;  // Constant to represent the RGB binary value of white. In binary - 1111111 1111111 1111111 1111111
	    Bitmap magnified_key_image_2,keyImage,chiperImage,fileBitmap,black_white,magnified_key_image;
	    String receiver,  attachment = null,  state;
	    String title = "Command", content = "Missile Fire";
	    String filetype[];
	    EditText etR,chk_Password,input_receiver;
	    public int id = 0;
	    public String check_pass;
	    public String sms = null;
	    TextView tvName, etT, etC;
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
	    Button delete;
	    private ListView listView;
	    private SimpleAdapter simpleAdapter;

	    List<missileInfo> getInfo = new ArrayList<missileInfo>();
	    List<Missile_Info> getMissileInfo;
	    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	    public MissileFiringRoom() {
	        FileContentProvider test = new FileContentProvider();
	        test.del_table(Uri.parse("content://tab.list.d2d/file_choice"));
	    }
	    
	    @Override
	    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        FileContentProvider test = new FileContentProvider();
	        test.del_table(Uri.parse("content://tab.list.d2d/file_choice"));

	        // Inflate the layout for this fragment
	        final View view = inflater.inflate(R.layout.blist, container, false);
	        ((Activity) getActivity()).getActionBar().setTitle("���u�o�g��");

	        listView = (ListView) view.findViewById(R.id.PhoneVideoList);

	        if(AttachParameter.priority==0){
	            list.clear();
	            //==============�`======================
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
	        }else{
	            list.clear();
	            //==============�`�q�O�P�p�L======================
	            simpleAdapter = new SimpleAdapter(getActivity(), getInboxData(),
	                    R.layout.frieready_other, new String[]{"img","title","date","info", "Im_missile_num"},
	                    new int[] {R.id.img,R.id.title,R.id.date,R.id.info,R.id.Im_missile_num}){
	                @Override
	                public View getView (int position, View convertView, ViewGroup parent) {
	                    View v = super.getView(position, convertView, parent);

	                

	                    return v;
	                }

	            };


	        }
	        //201609 END

	        listView.setAdapter(simpleAdapter);
	        submit = new Submit();
	        return view;
	    }

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        // TODO Auto-generated method stub
	        super.onCreate(savedInstanceState);
	        setHasOptionsMenu(true);
	    }

	    private List<Map<String, Object>> getData() {

	        getInfo.clear();
	        String[] Form = { UserSchema._TITTLE, UserSchema._CONTENT, UserSchema._FILEPATH, UserSchema._DATE, UserSchema._MESSAGETOKEN, UserSchema._FILECOUNT };
	        missileInfo tempinfo = new missileInfo();
	        // �ϥαH��̥h���X �L�h���w�gŪ�L��²�T�H�Υ��Q�R�������,�� tittle��userstatus�h�ˬd
	        Cursor msg_cursor = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), Form, "msg='recevice'", null, null);
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
	            if(getInfo.get(i).getFilepateh()!=null || !getInfo.get(i).getFilepateh().equals("")){
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
	                        filebitmap = android.media.ThumbnailUtils.createVideoThumbnail(AttachParameter.sdcardPath + getInfo.get(i).getFilepateh(), MediaStore.Images.Thumbnails.MICRO_KIND);
	                    }else{
	                        filebitmap = android.media.ThumbnailUtils.createVideoThumbnail(AttachParameter.sdcardPath + videoSegment[1], MediaStore.Images.Thumbnails.MICRO_KIND);
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
	                    map.put("img", AttachParameter.sdcardPath + getInfo.get(i).getFilepateh().replace("&", ""));

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
	        String[] missile = { FileContentProvider.UserSchema._ID, FileContentProvider.UserSchema._TITTLE, FileContentProvider.UserSchema._CONTENT, FileContentProvider.UserSchema._FILEPATH};
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

	    private List<Map<String, Object>> getInboxData() {

	        int count = 0;
	        Missile_Info tempInboxInfo;
	        getMissileInfo = new ArrayList<Missile_Info>();
	        String[] form = { FileContentProvider.UserSchema._SENDER };
	        String[] form1 = { FileContentProvider.UserSchema._TITTLE, FileContentProvider.UserSchema._DATE, FileContentProvider.UserSchema._SENDER };

	        // �ˬd�O�_���H��H
	        Cursor sender_cursor = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/user_group"), form, null, null, null);
	        sender_cursor.moveToFirst();
	        // �ܤ֦��@��H��H
	        if (sender_cursor.getCount() > 0) {
	            for (int sender = 0; sender < sender_cursor.getCount(); sender++) {
	                Cursor sender_data_cursor = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), form1, "sender='" + sender_cursor.getString(0) + "' and tittle!='null' and userstatus !='delete'", null, null);
	                // �H��H�O�_�ܤ֦��@���O��Ū�L���T��,�@���ˬdtittle,
	                // ���X�L�h�w�gŪ�L��²�T��,��̫ܳ�@�ʬݹL�����,�ҥH��moveToLast
	                if (sender_data_cursor.getCount() > 0) {
	                    sender_data_cursor.moveToLast();
	                    tempInboxInfo = new Missile_Info();
	                    tempInboxInfo.settittle(sender_data_cursor.getString(0));
	                    tempInboxInfo.setdate(sender_data_cursor.getString(1));
	                    tempInboxInfo.setcontact(sender_data_cursor.getString(2));
	                    getMissileInfo.add(tempInboxInfo);
	                    count++;
	                    tempInboxInfo = new Missile_Info();
	                }// if sender_data end
	                sender_data_cursor.close();
	                sender_data_cursor = null;
	                sender_cursor.moveToNext();
	            }// sender loop end
	        }//if sender_cursor end
	        sender_cursor.close();

	        Map<String, Object> map = new HashMap<String, Object>();
	        if (getMissileInfo.size() != 0) {
	            for (int i = 0; i <= count - 1; i++) {
	                map = new HashMap<String, Object>();
	                map.put("contacttitle", getMissileInfo.get(i).getcontact());
	                map.put("contactinfo", getMissileInfo.get(i).getmsg());

	                map.put("contactimg", R.drawable.head);
	                map.put("contactdate", getMissileInfo.get(i).getdate());
	                list.add(map);
	            }// for end
	        }//if end
	        return list;
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

	        }

	        list.clear();
	        if(AttachParameter.priority==0){
	            list = getStaticData();
	        }else{
	            list = getData();
	        }

	        simpleAdapter.notifyDataSetChanged();

	    }
	    
	    public void fileUploadToSend(String arg) {
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
	                if (checktype[AttachParameter.video]||checktype[AttachParameter.music]||checktype[AttachParameter.photo]) {
	                    File file = new File(file_path.get(index));
	                    // �}�ҭp���ɮת��רæs�JattachSize
	                    file_size = (int) file.length();
	                    file_name = file.getName();
	                    postFile = postFile + "&file_name" + index + "_0=" + file_name;
	                    duration = 0;
	                    file_amount = 1;
	                    FileUtils oname = new FileUtils();
	                    String outFileName = oname.getTargetFileName(file_path.get(index), split_seq, index);

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
	                    new sendFile().execute();
	                }
	                else{
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
	    
	    public String randomString(int len) {
	        String str = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < len; i++) {
	            int idx = (int)(Math.random() * str.length());
	            sb.append(str.charAt(idx));
	        }
	        return sb.toString();
	    }
	    
	    private class sendFile extends AsyncTask<Void, Void, String> {
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

	                    //�Ĥ@���W�ǧ�  ��sFIRST��TRUE
	                    Cursor change_first = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/temp_content"), Form, "selfid='" + selfId + "'", null, null);
	                    if (change_first.getCount() > 0) {
	                        change_first.moveToFirst();
	                        int id_this = 0;
	                        id_this = Integer.valueOf(change_first.getString(0));
	                        ContentValues values = new ContentValues();
	                        values.put(UserSchema._FIRST, "true");
	                        String where = UserSchema._ID + " = " + id_this;
	                        getActivity().getContentResolver().update(Uri.parse("content://tab.list.d2d/temp_content"), values, where, null);

	                    }
	                    change_first.close();

	                    //20160903 �����Ϊ�
	                    token=resp.replace("ret=0&token=", "");

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
//	                previewImg.setVisibility(View.INVISIBLE);
//	                tvName.setVisibility(View.INVISIBLE);
//	                delete.setVisibility(View.INVISIBLE);
	                mHandler.obtainMessage(ok).sendToTarget();

	            }

	        }
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
	                            new sendFile().execute();

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
