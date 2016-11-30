package kissmediad2d.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.VideoView;
import tab.list.AttachParameter;
import tab.list.FileContentProvider;
import tab.list.FileContentProvider.UserSchema;
import tab.list.FileUtils;
import login.submit1.*;

class openFireInfo {
    private String title, content, file, date, token,FileCount;

    openFireInfo() {
    }

    public void setTitle(String arg) {
        title = arg;
    }

    public void setDate(String arg) {
        date = arg;
    }

    public void seContent(String arg) {
        content = arg;
    }

    public void setoken(String arg) {
        token = arg;
    }

    public void setFile(String arg) {
        file = arg;
    }
    public void seFileCount(String arg) {
        FileCount = arg;
    }
    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getFilename() {
        return file;
    }

    public String gettoken() {
        return token;
    }
    public String getFileCount() {
        return FileCount;
    }

}

public class OpenFire extends ListActivity implements OnItemClickListener{
	
	 /*
		 * �o��O��ܨϥΪ̹L�h�����v�T���A�O��conractlist�I�s��class�A �Ϊ̬Onotify
		 * �b�I�s���P�ɷ|�����ǤJ��bundle�Abundle���e�O�H��̩m�W �A�ӭY�O�qnotify�Ӫ��ܡA�z�L�I�����v�T���i�H�q���H��H�W���ɮ�
		 */
	    List<openFireInfo> getinfo;
	    public int mailcount = 0;
	    public String sender,attachment;
	    public String res = null;
	    public String viewmod = "";
	    public boolean finishretrieve;
	    public String[] Again_id;
	    public String tittle,message,bmsg,upmsg,state,postFile, fileName, fileId, attachFile, attachPath;
	    public String Again_token,Again_sender;
//	    public String sdcardPath_2 = Environment.getExternalStorageDirectory().toString() + File.separator + "Download" + "/";
	    private String gettoken = new String();
	    private String thumbnails = "mnt/sdcard/DCIM/.thumbnails/";
	    private int listid, urgent = 0, split_seq = 0;
	    private static final int ITEM1 = Menu.FIRST;
	    private static final int ITEM2 = Menu.FIRST + 1;
	    private static final int ITEM3 = Menu.FIRST + 2;
	    private static final int ITEM4 = Menu.FIRST + 3;
	    private static final int ITEM5 = Menu.FIRST + 4;
	    User user = new User();
	    ProgressDialog pdialog = null;
	    ProgressDialog send_Dialog = null;
	    List<String> fileList = new ArrayList<String>();
	    SimpleAdapter adapter;
	    public Boolean finish =true;
	    private final int SHOW_MSG = 0,UPDATE=1;
	    private final int timeout = 4;
	    private final int ok = 5;
	    private final int error = 6;
	    public List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();;

	    EditText chk_Password, input_receiver;
	    public String check_pass;
	    ProgressDialog sendDialog = null;
	    String receiver;
	    String title = "Command", content = "Missile Fire";
	    public int id = 0, file_amount, duration, file_size;
	    boolean checkFileType = false ;
	    String filetype[];
	    Submit submit ;
	    String selfId, token, file_name;
	    static final int BLACK = -16777216;  // Constant to represent the RGB binary value of black. In binary - 1111111 00000000 00000000 00000000
	    static final int WHITE = -1;  // Constant to represent the RGB binary value of white. In binary - 1111111 1111111 1111111 1111111
	    Bitmap magnified_key_image_2,keyImage,chiperImage,fileBitmap,black_white,magnified_key_image;
	    String[] form = { UserSchema._FILEPATH, UserSchema._DURATION, UserSchema._FILESIZE, UserSchema._FILENAME, UserSchema._ID };
	    ArrayList<String> file_path;
	    int index;
	    ProgressDialog dialog = null;
	    
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        Intent data = getIntent();
	        Bundle bundle = data.getExtras();
	        // ���o�ǤJ��contact,���Ofilename ���Osender(�H���)
	        sender = bundle.getString("contact");
	        setTitle("History Massage");
	        this.registerForContextMenu(getListView()); // register on Item in
	        // listview on context menu

	        finishretrieve = false;
	        super.onCreate(savedInstanceState);
			/*
			 * �ϥ�adapter�A���ư���X, getData()�O��ƨӷ��AR.layout.receive�O�@�Φb�o��layout�W new
			 * String[]���۩w�q�����W�١A�N��Ʃ��int[]���Aint[]�Olayout�W���󪺩Ҧb��m
			 */
	        adapter = new SimpleAdapter(this, getData(), R.layout.receive, new String[] { "title", "info", "img", "date" }, new int[] { R.id.title, R.id.info, R.id.img, R.id.date });

	        setListAdapter(adapter);
	        getListView().setOnItemClickListener(this);
	        ListView listView = getListView();
	        listView.setBackgroundResource(R.drawable.worldbackground);
	        listView.setCacheColorHint(Color.TRANSPARENT);
	    }
	    
	    /*
		 * ����U���v�T���ɡA��ܭn�����Ʊ��A���T���ˬd�B�J �A �@�O���ˬd�O�_�o�w�g�q���i�D���A�й��W���ɮ� �A �G�O�ˬd���O�_���N�ɮפW�ǡA
		 * �T�N�^�k���e�����A�ˬd�ɮת��A�O�_ready�A�Yready���ܥ��ˬd�O�_���g�Q���_�U���A�Y���N�~��U���F �_�h �N�����߰ݬO�_�n�s���B�����U��
		 */
	    //notification :0= �i����,1=�ϥ�wlan ,2=�ϥ�sms,3=�i�U��
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long position) {
		// TODO Auto-generated method stub
		 final String[] itemForm = { UserSchema._RECEIVEID, UserSchema._MESSAGETOKEN, UserSchema._FILEPATH, UserSchema._FILEID };
	        final String[] check = {UserSchema._TYPE};
	        listid = mailcount - (int) position - 1;
	        // String[] Form = { UserSchema._RECEIVEID, UserSchema._MESSAGETOKEN,
	        // UserSchema._FILEPATH, UserSchema._FILEID };

	        // �ˬd�L�h�ɮצ��S���U���L�A�Y���I���᪽������ 0=�U���L�A
	        Cursor data_cursor = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), itemForm, "notification = '0' and messagetoken='" + getinfo.get(listid).gettoken() + "' and receive_id ='' and file_id is not null", null, null);
	        if (data_cursor.getCount() > 0) {
	            data_cursor.moveToFirst();
	            String chd2d=data_cursor.getString(3);
	            if(chd2d.equalsIgnoreCase("d2d")){
	                viewmod ="preview";
	                viewfile(AttachParameter.sdcardPath + data_cursor.getString(2));
	            }else{
	                viewfile(AttachParameter.sdcardPath + data_cursor.getString(2));


	            }
	            data_cursor.close();
	        } else {
	            //�w�o�e�L�q���A�ˬd�ण��U��
	            Cursor ch0_cursor = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), check, "notification is not null and messagetoken='" + getinfo.get(listid).gettoken() + "'", null, null);
	            int dlownload_num=0;
	            dlownload_num=ch0_cursor.getCount();
	            ch0_cursor.close();
	            if(dlownload_num>0){

	                Cursor CH_state = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), null, "userstatus='download'", null, null);
	                if (CH_state.getCount() > 0) {
	                    Toast.makeText(getApplicationContext(), "�ثe�|���ɮפU�����A�еy��A��", Toast.LENGTH_LONG).show();
	                    CH_state.close();
	                }else{
	                    //new chfile().execute();
	                    new chfile_for_m2m().execute();
	                }

	            }
	            else{//�ɮפ��i���� �]����U�� �ˬd�o�{�S�q��

	                // �߰ݬO�_�w���B�����U��
	                AlertDialog.Builder DialogPreDl = new AlertDialog.Builder(this); // Dialog
	                DialogPreDl.setTitle("");
	                DialogPreDl.setMessage("�����ԳƩR�O");
	                DialogPreDl.setIcon(android.R.drawable.ic_dialog_info);
	                if(Integer.valueOf(getinfo.get(listid).getFileCount())>1){
	                    DialogPreDl.setMessage("�п�ܹw���ɮשΤU�������ɮ�");
	                    DialogPreDl.setNegativeButton("�w��", new DialogInterface.OnClickListener() { // �������ɮ�
	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {

	                            try {
	                                Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
	                                field.setAccessible(true);
	                                field.set(dialog, false);
	                            } catch (Exception e) {

	                            }
	                            Cursor CH_state = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), null, "userstatus='download'", null, null);
	                            if (CH_state.getCount() > 0) {
	                                Toast.makeText(getApplicationContext(), "�ثe�|���ɮפU�����A�y��A��", Toast.LENGTH_LONG).show();

	                            } else {

	                                Log.i("token=", getinfo.get(listid).gettoken());
	                                // �]�wtoken�A�ت��O�i���ݷ|��retrive�ɭn������ɮ�

	                                // ����ۭq���D�P�B�禡
	                                viewmod = "preview";
	                                new startretrieve().execute();

	                            }
	                            CH_state.close();

	                        }
	                    });
	                }

	                DialogPreDl.setPositiveButton("�i�J�Գƪ��A", new DialogInterface.OnClickListener() { // �������ɮ�
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                        try {
	                            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
	                            field.setAccessible(true);
	                            field.set(dialog, true);
	                        } catch (Exception e) {

	                        }
	                        Cursor CH_state = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), null, "userstatus='download'", null, null);
	                        //=======20160903���Ϊ�========//
	                        int count=CH_state.getCount();
	                        CH_state.close();

	                        if (count > 0){
	                            Toast.makeText(getApplicationContext(), "�ثe�|���ɮפU�����A�y��A��", Toast.LENGTH_LONG).show();
	                        }else {
	                            int filecount=0;
	                            String check_exist = null;
	                            Cursor CH_first_is_photo = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"),
	                                    new String[]{UserSchema._FIRST, UserSchema._FILECOUNT},
	                                    "messagetoken='" + getinfo.get(listid).gettoken() + "'", null, null);
	                            if(CH_first_is_photo.getCount()>0){
	                                CH_first_is_photo.moveToFirst();
	                                filecount= Integer.valueOf(CH_first_is_photo.getString(1));
	                                check_exist=CH_first_is_photo.getString(0);
	                            }
	                            CH_first_is_photo.close();

	                            if (filecount > 1){
	                                if (check_exist != null){
	                                    new chfile_for_m2m().execute();
	                                }else {
	                                    Toast.makeText(getApplicationContext(), "�|���ŦX�U�����", Toast.LENGTH_LONG).show();
	                                }
	                            }else {
	                                new chfile_for_m2m().execute();
	                            }
	                        }
	                        //=======20160903���Ϊ�========//
	                    }//���ݤU�� end
	                });

	                DialogPreDl.setNeutralButton("�T�{�R�O", new DialogInterface.OnClickListener() { // �w���ɮ�
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                        try {
	                            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
	                            field.setAccessible(true);
	                            field.set(dialog, true);
	                        } catch (Exception e) {

	                        }
	                    }
	                });

	                DialogPreDl.show();

	            }//else end �ɮפ��i���� �]����U�� �ˬd�o�{�S�q��
	        }//else �ɮ׬O�_�i�H���� end
	        System.out.println("fffff");
	        // �߰ݬO�_�w���B�����U��end		
		
	}
	
	private class chfile_for_m2m extends AsyncTask<String, Void, String> {
        String token;
        String resp;
        @Override
        protected void onPreExecute() {
            // �}�Ҹ�ƶǰedialog
            pdialog = ProgressDialog.show(OpenFire.this, "�еy��", "�T�{���O�_���\�s�u", true);
            pdialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String[] res =user.getservicetime(sender);

            if(res[1].equalsIgnoreCase("true")){
                token = getinfo.get(listid).gettoken();
                String[] socketport = res[2].replace("socket=", "").split(":");
                //���Xip ��port
                //�ˬd�ؼЬO�_�����~��
                if(AttachParameter.out_ip.equals(socketport[0])){
                    //�~��ip�ۦP�A������ip
                	AttachParameter.connect_ip=socketport[1];
                    System.out.println("�o��O d2d socketport[1]="+socketport[1]);
                }else{
                    //�~��ip���P�A���~��
                    AttachParameter.connect_ip=socketport[0];
                    System.out.println("�o��O d2d socketport[0]="+socketport[0]);
                }
                AttachParameter.connect_port=socketport[2];
                Retrieve retrieve=new Retrieve();
                System.out.println("�o��O d2d socketport[0]="+socketport[0]);
                resp = retrieve.check_file_for_m2m(AttachParameter.connect_ip+":"+ AttachParameter.connect_port, token);

            }else{
                resp="server not ready";
            }


            return resp;
        }
        protected void onPostExecute(String reretrieve) {
            pdialog.dismiss();
            if(reretrieve.equalsIgnoreCase("server not ready")){				// �Yer���ߡA�h��ܿ�J������̤��s�b

                Cursor ch0_cursor = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), null, "notification = '2' and messagetoken='" + getinfo.get(listid).gettoken() + "'", null, null);
                if(ch0_cursor.getCount()>0){

                    AlertDialog.Builder T_Dialog = new AlertDialog.Builder(OpenFire.this); // Dialog
                    T_Dialog.setTitle("����ɮש|���ǳƦn");
                    T_Dialog.setMessage("�аݭn�O�d�T����?");
                    T_Dialog.setIcon(android.R.drawable.ic_dialog_info);

                    T_Dialog.setNegativeButton("�O�d", new DialogInterface.OnClickListener() { // �������ɮ�
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    T_Dialog.setPositiveButton("�R��", new DialogInterface.OnClickListener() { // �������ɮ�
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String[] tokenID = { UserSchema._ID, UserSchema._FILEID, UserSchema._RECEIVEID };
                            Cursor sender_data_group_cursor = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), tokenID, "messagetoken='" + getinfo.get(listid).gettoken() + "'", null, null);
                            if (sender_data_group_cursor.getCount() > 0) {
                                sender_data_group_cursor.moveToFirst();
                                int id_this = 0;
                                id_this = Integer.valueOf(sender_data_group_cursor.getString(0));
                                ContentValues values = new ContentValues();
                                String delete = "delete";
                                values.put(UserSchema._USESTATUS, delete);
                                String where = UserSchema._ID + " = " + id_this;
                                getContentResolver().update(Uri.parse("content://tab.list.d2d/user_data"), values, where, null);

                            }
                            sender_data_group_cursor.close();
                            Toast.makeText(getApplicationContext(), "�R�����\", Toast.LENGTH_SHORT).show();
                            onResume();
                        }
                    });
                    T_Dialog.show();


                }else{
                    AlertDialog.Builder Dialog = new AlertDialog.Builder(OpenFire.this); // Dialog
                    Dialog.setTitle("���Server�|���}��");
                    Dialog.setMessage("�аݻݭn�q�����}�Ҷ�?");
                    Dialog.setIcon(android.R.drawable.ic_dialog_info);
                    Dialog.setNeutralButton("�O", new DialogInterface.OnClickListener() { // ���Uretry
                        // �Nthread����
                        // �A�]�@�ӷs��thread
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Thread.currentThread().interrupt();

                            AlertDialog.Builder T_Dialog = new AlertDialog.Builder(OpenFire.this); // Dialog
                            T_Dialog.setTitle("");
                            T_Dialog.setMessage("�п�ܳq������");
                            T_Dialog.setIcon(android.R.drawable.ic_dialog_info);

                            T_Dialog.setNeutralButton("����", new DialogInterface.OnClickListener() { // �������ɮ�
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            Cursor chnu_cursor = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), null, "notification is null and messagetoken='" + getinfo.get(listid).gettoken() + "'", null, null);
                            int num=0;
                            num=chnu_cursor.getCount();
                            chnu_cursor.close();
                            if(num>0){

                                T_Dialog.setPositiveButton("wlan", new DialogInterface.OnClickListener() { // �������ɮ�
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        urgent = 0;
                                        new request_for_d2d().execute();
                                    }
                                });
                                T_Dialog.setNegativeButton("sms", new DialogInterface.OnClickListener() { // �w���ɮ�
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        urgent = 1;
                                        new request_for_d2d().execute();
                                    }
                                });
                            }else{
                                Cursor ch1_cursor = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), null, "notification = '1' and messagetoken='" + getinfo.get(listid).gettoken() + "'", null, null);
                                num=ch1_cursor.getCount();
                                ch1_cursor.close();
                                if(num>0){
                                    T_Dialog.setNegativeButton("sms", new DialogInterface.OnClickListener() { // �w���ɮ�
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            urgent = 1;
                                            new request_for_d2d().execute();
                                        }
                                    });
                                }else{
                                    T_Dialog.setNegativeButton("wlan", new DialogInterface.OnClickListener() { // �w���ɮ�
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            urgent = 0;
                                            new request_for_d2d().execute();
                                        }
                                    });
                                }
                            }
                            T_Dialog.show();
                        }
                    });
                    Dialog.setNegativeButton("�_", new DialogInterface.OnClickListener() { // ���Uabort

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder T_Dialog = new AlertDialog.Builder(OpenFire.this); // Dialog
                            T_Dialog.setTitle("�ɮש|������W��");
                            T_Dialog.setMessage("�аݭn�O�d�T����?");
                            T_Dialog.setIcon(android.R.drawable.ic_dialog_info);

                            T_Dialog.setNegativeButton("�O�d", new DialogInterface.OnClickListener() { // �������ɮ�
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            T_Dialog.setPositiveButton("�R��", new DialogInterface.OnClickListener() { // �������ɮ�
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String[] tokenID = { UserSchema._ID, UserSchema._FILEID, UserSchema._RECEIVEID };
                                    Cursor sender_data_group_cursor = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), tokenID, "messagetoken='" + getinfo.get(listid).gettoken() + "'", null, null);
                                    if (sender_data_group_cursor.getCount() > 0) {
                                        sender_data_group_cursor.moveToFirst();
                                        int id_this = 0;
                                        id_this = Integer.valueOf(sender_data_group_cursor.getString(0));
                                        ContentValues values = new ContentValues();
                                        String delete = "delete";
                                        values.put(UserSchema._USESTATUS, delete);
                                        String where = UserSchema._ID + " = " + id_this;
                                        getContentResolver().update(Uri.parse("content://tab.list.d2d/user_data"), values, where, null);

                                    }
                                    sender_data_group_cursor.close();
                                    Toast.makeText(getApplicationContext(), "�R�����\", Toast.LENGTH_SHORT).show();
                                    onResume();
                                }
                            });
                            T_Dialog.show();

                        }
                    });
                    Dialog.show();
                }
                ch0_cursor.close();
            }else{
                Boolean res= AttachParameter.chechsuccess(reretrieve);
                if (res) {
                    //�u���@�����ɭ� NOTIFY�]�|�j��]��3
                    updateNotification("3", "d2d");
                    final String[] itemForm = { UserSchema._RECEIVEID, UserSchema._MESSAGETOKEN, UserSchema._FILEPATH, UserSchema._FILEID };
                    Cursor ch1_cursor = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), itemForm, "notification = '3' and messagetoken='" + getinfo.get(listid).gettoken() + "'", null, null);
                    if (ch1_cursor.getCount() > 0) {
                        ch1_cursor.moveToFirst();
                        String r_id = ch1_cursor.getString(0);
                        if (r_id == null) {
                            r_id = "";
                        }
                        String f_id = ch1_cursor.getString(3);
                        if (f_id == null) {
                            f_id = "";
                        }
                        if (f_id.equals("")) {// f_id���ťN��S���U���L
                            // �����U��


                            // �}�l�U��,�����۩w�q���禡
                            viewmod = "startview";
                            new startretrieve().execute();


                        }else{
                            viewmod = "startview";

                            updateNotification("2", "sms");
                            String[] tokenID = { UserSchema._ID, UserSchema._FILEID, UserSchema._RECEIVEID, UserSchema._SENDER };
                            Cursor copy_fileid_cursor = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), tokenID, "messagetoken='" + token + "'", null, null);
                            if (copy_fileid_cursor.getCount() > 0) {
                                copy_fileid_cursor.moveToFirst();

                                Again_id = copy_fileid_cursor.getString(1).substring(1,copy_fileid_cursor.getString(1).length()).split("&");
                                Again_token = token;
                                Again_sender = copy_fileid_cursor.getString(3);
                                int id_this = Integer.valueOf(copy_fileid_cursor.getString(0));
                                ContentValues values = new ContentValues();
                                values.put(UserSchema._RECEIVEID, copy_fileid_cursor.getString(1));
                                values.put(UserSchema._FILEPATH, "");

                                String where = UserSchema._ID + " = " + id_this;
                                getContentResolver().update(Uri.parse("content://tab.list.d2d/user_data"), values, where, null);
                            }
                            copy_fileid_cursor.close();
                            new retrieveAgain().execute();
                        }

                    }else{
                        Toast.makeText(getApplicationContext(), "�e�@���U�����ѡA�иյۤ[���T���A��ܥR�s�U��", Toast.LENGTH_SHORT).show();
                    }
                    ch1_cursor.close();
                }
                else{
                    //�o�̭n��s�L�����server���A
                    Toast.makeText(getApplicationContext(), "�ɮ��٨S�ǳƦn", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }
	
	/*
	 * �۩w�q���D�P�B�禡�A�o�̷|�~��U�����������ɮסA�ʧ@��retrive�@�ˡA�ߤ@���P���O �ɮ�id�O��db����X�Ӫ�
	 */
    private class retrieveAgain extends AsyncTask<Void, Void, String[]> {
        String[] reretrieve_string=new String[1];
        Retrieve retrieve = new Retrieve();
        String playpath =new String();
        String[] reretrieve_file = new String[7];
        boolean has_error=false;
        protected void onPreExecute() {
            // �}�Ҹ�ƶǰedialog
            pdialog = ProgressDialog.show(OpenFire.this, "�еy��", "��ƤU����", true);
            pdialog.show();
        }
        @Override
        protected String[] doInBackground(Void... arg0) {
            User user = new User();
            String[] res =user.getservicetime(Again_sender);
            if(res[1].equalsIgnoreCase("true")){
                updateState("download", "messagetoken='" + Again_token + "'");
                String[] socketport = res[2].replace("socket=", "").split(":");
                //���Xip ��port
                //�ˬd�ؼЬO�_�����~��
                if(AttachParameter.out_ip.equals(socketport[0])){
                    //�~��ip�ۦP�A������ip
                    AttachParameter.connect_ip=socketport[1];
                    System.out.println("�o��O d2d socketport[1]="+socketport[1]);
                }else{
                    //�~��ip���P�A���~��
                	AttachParameter.connect_ip=socketport[0];
                    System.out.println("�o��O d2d socketport[0]="+socketport[0]);
                }
                AttachParameter.connect_port=socketport[2];
                updateNotification("3", "d2d");

                retrieve.fileid = Again_id;
                for (int j = 0; j < retrieve.fileid.length; j++) {
                    try {
                        retrieve.settoken(Again_token);
                        //20160905�Ǫ��[�^(getContentResolver())
                        reretrieve_file=retrieve.saveBinaryFile_for_d2d(Again_token, j, getContentResolver());
                        upmsg="�Фť��N���ʦ�m�A�קK�_�u�A�ثe���b�U����"+(j+1)+"/"+retrieve.fileid.length;
                        mHandler.obtainMessage(UPDATE).sendToTarget();
                        if(reretrieve_file[0].equalsIgnoreCase("true")){
                            // �۩w�q��
                            replaceSeq("messagetoken='" + Again_token + "'", "again",reretrieve_file[1]);
                            playpath = playpath + "&" + Again_token+"_"+retrieve.filename.replace(".", "-_" + String.valueOf(j) + ".");
                        }else{
                            has_error=true;
                            break;
                        }
                        // �۩w�q��

                    } catch (IOException ex) {
                        reretrieve_file[0]="server error";
                        reretrieve_file[5]="true";
                    }catch(Exception ex){
                        reretrieve_file[0]="server error";
                        reretrieve_file[4]="true";
                    }
                }
                if(!has_error){
                    String[] tokenID = { UserSchema._ID};
                    Cursor update_play_path = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), tokenID, "messagetoken='" + Again_token + "'", null, null);
                    if (update_play_path.getCount() > 0) {
                        update_play_path.moveToFirst();

                        int id_this = Integer.valueOf(update_play_path.getString(0));
                        ContentValues values = new ContentValues();
                        values.put(UserSchema._FILEPATH, playpath);
                        String where = UserSchema._ID + " = " + id_this;
                        getContentResolver().update(Uri.parse("content://tab.list.d2d/user_data"), values, where, null);
                    }
                    update_play_path.close();


                    updateNotification("0","wlan");
                    updateState("", "messagetoken='" + Again_token + "'");
                }
                reretrieve_string[0]="true";

            }else{
                reretrieve_file[6]="true";
                reretrieve_file[0]="server error";
                Again_token="";
                updateNotification("2","sms");
                updateState("", "messagetoken='" + getinfo.get(listid).gettoken() + "'");
                //Toast.makeText(getApplicationContext(), "���|���}��server", Toast.LENGTH_LONG).show();
            }


            return reretrieve_string;
        }

        protected void onPostExecute(String[] notuse) {
            pdialog.dismiss();
            updateState("", "messagetoken='" + Again_token + "'");
            if (reretrieve_file[0].equalsIgnoreCase("true")) {
                tittle ="����";
                message ="�ɮפU�����\!";
                bmsg="�T�w";
                onResume();
                mHandler.obtainMessage(SHOW_MSG).sendToTarget();

            }else if(reretrieve_file[0]=="false"){
                //retreive file ���� (�o��200�H�~��code)
                //�nŪbody
                Pattern pattern_msg = Pattern.compile("message does not exist"); // check file type
                Matcher matcher_msg = pattern_msg.matcher(reretrieve_file[1]);
                Pattern pattern_file = Pattern.compile("file is not ready"); // check file type
                Matcher matcher_file = pattern_file.matcher(reretrieve_file[1]);
                Pattern pattern_file_II = Pattern.compile("file not found"); // check file type
                Matcher matcher_file_II = pattern_file_II.matcher(reretrieve_file[1]);

                if(matcher_msg.find()){
                    tittle ="���~";
                    message ="�T���ä��s�bserver�W�A�ЧR�����T��";
                    bmsg="�T�w";
                }else if(matcher_file.find()){
                    tittle ="���~";
                    message ="���b�ɮ׮ɵo�Ϳ��~�A�еy��A��";
                    bmsg="�T�w";
                }else if(matcher_file_II.find()){
                    tittle ="���~";
                    message ="���ɮקǸ����s�b�A�ЧR�����T��";
                    bmsg="�T�w";
                }
                mHandler.obtainMessage(SHOW_MSG).sendToTarget();
            }else {
                //server error
                if(reretrieve_file[2]!=null && reretrieve_file[2].equalsIgnoreCase("true")){
                    tittle ="ĵ�i";
                    message ="���A�������o�Ϳ��~�A�мȰ��ϥ�McaaS";
                    bmsg="�T�w";
                }else if(reretrieve_file[3]!=null && reretrieve_file[3].equalsIgnoreCase("true")){
                    tittle ="ĵ�i";
                    message ="���server���}�A�мȰ��ϥ�McaaS";
                    bmsg="�T�w";
                }else if (reretrieve_file[4]!=null && reretrieve_file[4].equalsIgnoreCase("true")){
                    tittle ="ĵ�i";
                    message ="���������~�A�мȰ��ϥ�McaaS";
                    bmsg="�T�w";
                }else if (reretrieve_file[5]!=null && reretrieve_file[5].equalsIgnoreCase("true")){
                    tittle ="ĵ�i";
                    message ="�����x�s�Ŷ��o�Ϳ��~�A�мȰ��ϥ�McaaS";
                    bmsg="�T�w";
                }
                else if (reretrieve_file[6]!=null && reretrieve_file[6].equalsIgnoreCase("true")){
                    tittle ="�U������";
                    message ="���|���}��Server�A�еy��A��";
                    bmsg="�T�w";
                }
                mHandler.obtainMessage(SHOW_MSG).sendToTarget();
            }

        }

    }
	
	private class startretrieve extends AsyncTask<Void, Void, String[]> {// implement
        public String getFilename = null;
    public String[] getSplit;
    String[] reretrieve = new String[5];
    String[] reretrieve_file = new String[5];
    String[] reretrieve_arg = new String[11];
    public String token;
    public int id_this;
    String[] notuse;
    public String where;
    String[] Form = { UserSchema._ID, UserSchema._RECEIVEID };
    Retrieve retrieve = new Retrieve();
    @Override
    protected void onPreExecute() {
        // �}�Ҹ�ƶǰedialog
        pdialog = ProgressDialog.show(OpenFire.this, "�еy��", "��ƤU����", true);
        pdialog.show();
    }
    @Override
    protected String[] doInBackground(Void... params) {
        // String[] refile = new String[4];

        // ���o�̪�IP
        token = getinfo.get(listid).gettoken();
        if (viewmod.equals("preview")) {
            retrieve.viewmod = viewmod;
            updateState("download", "messagetoken='" + token + "'");
            reretrieve = retrieve.retrieve_req(token, retrieve.viewmod);
            reretrieve_arg[0]=reretrieve[0];
            reretrieve_arg[1]=reretrieve[1];
            reretrieve_arg[2]=reretrieve[2];
            reretrieve_arg[3]=reretrieve[3];
            reretrieve_arg[4]=reretrieve[4];

            if (reretrieve[0] == "true"){ // retrieve_req=ret=0

                try {

                    reretrieve_file = retrieve.saveBinaryFile(token, 0);
                    updateState("", "messagetoken='" + token + "'");
                    reretrieve_arg[5]=reretrieve_file[0];
                    reretrieve_arg[6]=reretrieve_file[1];
                    reretrieve_arg[7]=reretrieve_file[2];
                    reretrieve_arg[8]=reretrieve_file[3];
                    reretrieve_arg[9]=reretrieve_file[4];
                } catch (IOException ex) {
                    reretrieve_arg[5]="server error";
                    reretrieve_arg[10]="true";
                }catch(Exception ex){
                    reretrieve_arg[5]="server error";
                    reretrieve_arg[9]="true";
                }
            }
        }  else if (viewmod.equals("startview")) {
            retrieve.viewmod = viewmod;
            // �o�䪺DB�A�O�n��s�ثe�buser_data����²�T�����A�A�令�U���������A
            // cursor(��ƪ�ӷ�,����������W��,����)
            // ��������W�١Guser_data��ƪ�ID�ȡA����Gitemclick�I���ɩұo��token

            updateState("download", "messagetoken='" + token + "'");
            // ����request���ʧ@�A�ǤJtoken�hserver�A���o��token�ҫ������ɮ�id
            upmsg="�ثe���b�P"+ AttachParameter.connect_ip+"�s�u��";
            mHandler.obtainMessage(UPDATE).sendToTarget();

            reretrieve = retrieve.retrieve_req_for_d2d(AttachParameter.connect_ip, AttachParameter.connect_port,getinfo.get(listid).gettoken());
            reretrieve_arg[0]=reretrieve[0];
            reretrieve_arg[1]=reretrieve[1];
            reretrieve_arg[2]=reretrieve[2];
            reretrieve_arg[3]=reretrieve[3];
            reretrieve_arg[4]=reretrieve[4];
            //reretrieve = retrieve.retrieve_req(getinfo.get(listid).gettoken(), mod);
            System.out.println("�^�_�����D���B "+reretrieve);
            if (reretrieve[0] == "true"){ // retrieve_req=ret=0

                fileList.clear(); // ���NfileList�M�� �H�KŪ��W������
                getSplit = reretrieve[1].split("&");
                //reretrieve[0]=/storage/emulated/0/KM/file_name0_0-MOV_0259.mp4&/storage/emulated/0/KM/file_name0_1-MOV_0259.mp4
                /*
				 * �]���ɮץi��|�U�����ѡA���O���@�ӥ��Ѥ����D�A�ҥH�o��DB���ʧ@���A�������n�U�����ɮ�ID�A
				 * �ҥHRECEIVEID�O���ɮפU�����\�ɡA�ݭ��@�����\�N�hRECEIVEID���R��
				 * ��FILEID�O�Ψӭ��s�U�����γ~
				 */
                Cursor up_fileid = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), Form, "messagetoken='" + token + "'", null, null);
                if (up_fileid.getCount() > 0) {
                    up_fileid.moveToFirst();
                    ContentValues values = new ContentValues();
                    values.put(UserSchema._RECEIVEID, "&"+reretrieve[1]);
                    values.put(UserSchema._FILEID, "&"+reretrieve[1]);
                    id_this = Integer.parseInt(up_fileid.getString(0));
                    where = UserSchema._ID + " = " + id_this;
                    getContentResolver().update(Uri.parse("content://tab.list.d2d/user_data"), values, where, null);
                    values = null;

                }
                up_fileid.close();
					/*
					 * �o��O���ɮת�ID�A����N��������۩w�q��saveBinaryFile�U���ɮסA�����i��
					 * saveBinaryFile�ثe�̪�ip,�H�β{�b�n�U�����@���ɮ�
					 */
                for (int j = 0; j < retrieve.fileid.length; j++) { // ���ɮ�ID
                    try {

                        String DLfilename=new String();

                        upmsg="�Фť��N���ʦ�m�A�קK�_�u�A�ثe���b�P"+ AttachParameter.connect_ip+"�s�u���A�äU����"+(j+1)+"/"+retrieve.fileid.length;
                        mHandler.obtainMessage(UPDATE).sendToTarget();
                        //20160905�Ǫ��[�^(getContentResolver())
                        reretrieve_file =retrieve.saveBinaryFile_for_d2d(token, j,getContentResolver()); // �����ln���ɮ�
                        reretrieve_arg[5]=reretrieve_file[0];
                        reretrieve_arg[6]=reretrieve_file[1];
                        reretrieve_arg[7]=reretrieve_file[2];
                        reretrieve_arg[8]=reretrieve_file[3];
                        reretrieve_arg[9]=reretrieve_file[4];
                        if(reretrieve_file[0].equalsIgnoreCase("true")){
                            DLfilename = retrieve.filename;
                            // �z��^�Ǫ�filename�APattern�O�A�n�z�諸���e�AMatcher�O�A�n�d���@�Ӧr�갵�z��
                            boolean[] checktype = new boolean[AttachParameter.filetype];
                            checktype = AttachParameter.checktype(DLfilename);
                            System.out.println("�^�_�����D���B3 "+DLfilename);
                            // �o��DB���ʧ@���A�����w���쪺file id�A�|���_�a�⦨�\��id�A�qDB���R��
                            replaceSeq("messagetoken='" + token + "'", "retrive",reretrieve_file[1]);
                            // �p�G�o���U�����O�v��
                            if (checktype[AttachParameter.video]) {
                                // �N�ɦW�[�J��arraylist
                                fileList.add(DLfilename);
                                if(retrieve.fileid.length>3){
                                    if (j == 3 ) { // �u�b�Ĥ@��������Ұʼ���
                                        Intent intent = new Intent();
                                        intent.setClass(OpenFire.this, VideoView.class);
                                        Bundle bundle = new Bundle();
                                        // changeToSeries�O�۩w�q����ơA�ت��O�إߤ@�Ө㦳���Ǫ��M��
                                        bundle.putString("playList", changeToSeries(token,DLfilename, retrieve.fileid.length)); // �N�Ѽƶǻ���videoView
                                        intent.putExtras(bundle);

                                        startActivity(intent);

                                    }
                                }else{
                                    if (j == 0 ) { // �u�b�Ĥ@��������Ұʼ���
                                        Intent intent = new Intent();
                                        intent.setClass(OpenFire.this, VideoView.class);
                                        Bundle bundle = new Bundle();
                                        // changeToSeries�O�۩w�q����ơA�ت��O�إߤ@�Ө㦳���Ǫ��M��
                                        bundle.putString("playList", changeToSeries(token,DLfilename, retrieve.fileid.length)); // �N�Ѽƶǻ���videoView
                                        intent.putExtras(bundle);

                                        startActivity(intent);

                                    }
                                }


                            }
                            //=====20160903���Ϊ�=====//
                            else if(checktype[AttachParameter.photo]){
                                fileList.add(DLfilename);
                            }
                            else {// �p�G���O�v���A�h��������
                                viewfile(AttachParameter.sdcardPath + DLfilename);
                            }
                        }else{
                            break;
                        }


                    } catch (IOException ex) {
                        reretrieve_arg[5]="server error";
                        reretrieve_arg[10]="true";
                    }catch(Exception ex){
                        reretrieve_arg[5]="server error";
                        reretrieve_arg[9]="true";
                    }
                }
            }

        }
        return notuse;
    }

    protected void onPostExecute(String[] notuse) {
        pdialog.dismiss();
        updateState("", "messagetoken='" + token + "'");
        if(reretrieve_arg[0]=="true"){
            //retreive���\
            if(reretrieve_arg[5]=="true"){
                //retreive file���\
                if (viewmod.equals("preview")) {
                    boolean[] checktype = new boolean[AttachParameter.filetype];
                    checktype = AttachParameter.checktype(retrieve.filename);
                    if(checktype[AttachParameter.photo]){
                        //=======20160903���Ϊ�========//
                        Cursor first_ready = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), Form, "messagetoken='" + token + "'", null, null);
                        ContentValues values = new ContentValues();

                        if (first_ready.getCount() > 0){
                            first_ready.moveToFirst();
                            values.put(UserSchema._FIRST, retrieve.filename);
                            //=======20160905�[�^============//
                            values.put(UserSchema._LENGTH_RECORD, "file0_0=0&");
                            //=======20160905�[�^============//
                            int id_this = Integer.parseInt(first_ready.getString(0));
                            String where = UserSchema._ID + " = " + id_this;
                            getContentResolver().update(Uri.parse("content://tab.list.d2d/user_data"), values, where, null);
                        }
                        first_ready.close();
                        //=======20160903���Ϊ�========//
                        viewfile(AttachParameter.sdcardPath + retrieve.filename);
                    }else{
                        viewfile(retrieve.filename);
                    }
                }else if (viewmod.equals("startview")) {

					/*
					 * cursor(��ƪ�ӷ�,����������W��,����)//��������W�١Guser_data��ƪ�ID�ȡA����
					 * �Gitemclick�I���ɩұo��token
					 *
					 * �]���w�g�U�����F�A�ҥHuser_state��download����
					 */
                    updateState("", "messagetoken='" + token + "'");
                    if (fileList.size() == 0){ // �Y�ɮפ��Ovideo fileList=0
                        getFilename = retrieve.filename;
                    } else{ // �Y�ɮ׬Ovideo �h��&���ꦨ�@��A�ت��O�n���v���M��(�o�̪��M�椺�e�O�O�ɦW��_�Ӫ�)
                        getFilename = "";
                        for (int i = 0; i < fileList.size(); i++)
                            getFilename = getFilename + "&" + token+"_"+fileList.get(i).replace(".", "-_" + String.valueOf(i) + ".");
                    }
                    // �]�����U�����F�A�ҥH��sfilepath
                    Cursor up_filepath = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), Form, "messagetoken='" + token + "'", null, null);
                    ContentValues values = new ContentValues();
                    if (up_filepath.getCount() > 0) {
                        up_filepath.moveToFirst();
                        values.put(UserSchema._FILEPATH, getFilename);
                        //=====20160903���Ϊ�============//
                        values.put(UserSchema._FILENAME, retrieve.filename);
                        //=====20160903���Ϊ�============//
                        int id_this = Integer.parseInt(up_filepath.getString(0));
                        String where = UserSchema._ID + " = " + id_this;
                        getContentResolver().update(Uri.parse("content://tab.list.d2d/user_data"), values, where, null);
                    }
                    up_filepath.close();

                    //=========20161031�@�s�W========//
                    values = new ContentValues();
                    values.put(UserSchema._FILEPATH, attachment);
                    getContentResolver().insert(Uri.parse("content://tab.list.d2d/file_choice"), values);
                    //=========20161031�@�s�W========//

                    updateNotification("0","wlan");
                    onResume();
                }
            }else if(reretrieve_arg[5]=="false"){
                //retreive file ���� (�o��200�H�~��code)
                //�nŪbody
                Pattern pattern_msg = Pattern.compile("message does not exist"); // check file type
                Matcher matcher_msg = pattern_msg.matcher(reretrieve_arg[6]);
                Pattern pattern_file = Pattern.compile("file is not ready"); // check file type
                Matcher matcher_file = pattern_file.matcher(reretrieve_arg[6]);
                Pattern pattern_file_II = Pattern.compile("file not found"); // check file type
                Matcher matcher_file_II = pattern_file_II.matcher(reretrieve_arg[6]);
                if(matcher_msg.find()){
                    tittle ="���~";
                    message ="�T���ä��s�bserver�W�A�ЧR�����T��";
                    bmsg="�T�w";
                }else if(matcher_file.find()){
                    tittle ="���~";
                    message ="���b�ɮ׮ɵo�Ϳ��~�A�еy��A��";
                    bmsg="�T�w";
                }else if(matcher_file_II.find()){
                    tittle ="���~";
                    message ="���ɮקǸ����s�b�A�ЧR�����T��";
                    bmsg="�T�w";
                }
                mHandler.obtainMessage(SHOW_MSG).sendToTarget();
            }else {
                //server error
                if(reretrieve_arg[7]!=null && reretrieve_arg[7].equalsIgnoreCase("true")){
                    tittle ="ĵ�i";
                    message ="���A�������o�Ϳ��~�A�мȰ��ϥ�McaaS";
                    bmsg="�T�w";
                }else if(reretrieve_arg[8]!=null && reretrieve_arg[8].equalsIgnoreCase("true")){
                    tittle ="ĵ�i";
                    message ="�s�����ѡA�мȰ��ϥ�McaaS";
                    bmsg="�T�w";
                }else if (reretrieve_arg[9]!=null && reretrieve_arg[9].equalsIgnoreCase("true")){
                    tittle ="ĵ�i";
                    message ="���������~�A�мȰ��ϥ�McaaS";
                    bmsg="�T�w";
                }else if (reretrieve_arg[10]!=null && reretrieve_arg[10].equalsIgnoreCase("true")){
                    tittle ="ĵ�i";
                    message ="�����x�s�Ŷ��o�Ϳ��~�A�мȰ��ϥ�McaaS";
                    bmsg="�T�w";
                }
                mHandler.obtainMessage(SHOW_MSG).sendToTarget();
            }
        }else if(reretrieve_arg[0]=="false"){
            //retreive���� (�o��200�H�~��code)
            //�nŪbody
            Pattern pattern_msg = Pattern.compile("message does not exist"); // check file type
            Matcher matcher_msg = pattern_msg.matcher(reretrieve_arg[1]);
            Pattern pattern_msg_II = Pattern.compile("file is not ready"); // check file type
            Matcher matcher_msg_II = pattern_msg_II.matcher(reretrieve_arg[1]);
            if(matcher_msg.find()){
                tittle ="���~";
                message ="�T���ä��s�bserver�W�A�ЧR�����T��";
                bmsg="�T�w";
            }else if(matcher_msg_II.find()){
                tittle ="���~";
                message ="���~���T���s���A�ЧR�����T��";
                bmsg="�T�w";
            }
            mHandler.obtainMessage(SHOW_MSG).sendToTarget();
        }else{
            //server error
            if(reretrieve_arg[2]!=null && reretrieve_arg[2].equalsIgnoreCase("true")){
                tittle ="ĵ�i";
                message ="���A�������o�Ϳ��~�A�мȰ��ϥ�McaaS";
                bmsg="�T�w";
            }else if(reretrieve_arg[3]!=null && reretrieve_arg[3].equalsIgnoreCase("true")){
                tittle ="ĵ�i";
                message ="�s�����ѡA�мȰ��ϥ�McaaS";
                bmsg="�T�w";
            }else if (reretrieve_arg[4]!=null && reretrieve_arg[4].equalsIgnoreCase("true")){
                tittle ="ĵ�i";
                message ="���������~�A�мȰ��ϥ�McaaS";
                bmsg="�T�w";
            }
            mHandler.obtainMessage(SHOW_MSG).sendToTarget();
        }
    }
}
	
	private class request_for_d2d extends AsyncTask<Void, Void, String> {
	        @Override
	        protected void onPreExecute() {
	            // �}�Ҹ�ƶǰedialog
	            pdialog = ProgressDialog.show(OpenFire.this, "�еy��", "�q����", true);
	            pdialog.show();
	        }

	        @Override
	        protected String doInBackground(Void... params) {
	            String reretrieve = new String();
	            reretrieve=user.setd2d(getinfo.get(listid).gettoken(),urgent);
	            return reretrieve;
	        }

	        protected void onPostExecute(String reretrieve) {
	            pdialog.dismiss();
	            if (AttachParameter.chechsuccess(reretrieve)) {
	                Toast.makeText(getApplicationContext(), "�o�e���\�A���}��server��N�۰ʤU��", Toast.LENGTH_LONG).show();
	                if(urgent==0){
	                    updateNotification("1","d2d");
	                }else if(urgent==1){
	                    updateNotification("2","sms");
	                }
	                //��l��urgent
	                urgent = 0;

	            } else {
	                Toast.makeText(getApplicationContext(), "�o�e���ѡA�Э��s�A��", Toast.LENGTH_LONG).show();
	            }
	        }
	    }
	
	 private class del_msg extends AsyncTask<Void, Void, String> {
	        protected void onPreExecute() {
	            // �}�Ҹ�ƶǰedialog
	            pdialog = ProgressDialog.show(OpenFire.this, "�еy��", "��ƧR����", true);
	            pdialog.show();
	        }
	        @Override
	        protected String doInBackground(Void... arg0) {
	            user = new User();
	            String res=user.delete_msg(gettoken);
	            user=null;
	            return res;
	        }
	        protected void onPostExecute(String reretrieve) {
	            pdialog.dismiss();
	            Boolean res= AttachParameter.chechsuccess(reretrieve);
	            if (res) {
	                AlertDialog.Builder Dialog = new AlertDialog.Builder(OpenFire.this); // Dialog
	                Dialog.setTitle("");
	                Dialog.setMessage("�ɮקR�����\");
	                Dialog.setIcon(android.R.drawable.ic_dialog_info);
	                Dialog.setNeutralButton("�T�w", new DialogInterface.OnClickListener() { // ���Uretry
	                    // �Nthread����
	                    // �A�]�@�ӷs��thread
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                        onResume();
	                    }
	                });
	                Dialog.show();

	            } // reretrieve[1]=="true"
	            else {
	                AlertDialog.Builder Dialog = new AlertDialog.Builder(OpenFire.this); // Dialog
	                Dialog.setTitle("");
	                Dialog.setMessage("�ɮקR������");
	                Dialog.setIcon(android.R.drawable.ic_dialog_info);
	                Dialog.setNeutralButton("�T�w", new DialogInterface.OnClickListener() { // ���Uretry
	                    // �Nthread����
	                    // �A�]�@�ӷs��thread
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {

	                    }
	                });
	                Dialog.show();

	            }

	        }
	    }

	  private class check_password extends AsyncTask<Void, Void, String> { // implement
	        // Asyntask �e�m�@�~
	        @Override
	        protected void onPreExecute() {
	            sendDialog = ProgressDialog.show(OpenFire.this, "�еy��", "�K�X�T�{��", true);
	            sendDialog.show();
	        }

	        @Override
	        protected String doInBackground(Void... params) {
	            User check_state=new User();
	            String res=check_state.check_password("password="+check_pass);

	            return res;
	        }

	        protected void onPostExecute(String res) {
	            sendDialog.dismiss();
	            Boolean reslut=AttachParameter.chechsuccess(res);
	            if(reslut){
	                LayoutInflater factory = LayoutInflater.from(OpenFire.this);
	                final View v1 = factory.inflate(R.layout.input_reciever, null);
	                input_receiver = (EditText) v1.findViewById(R.id.input_receiver);

	                AlertDialog.Builder dialog = new AlertDialog.Builder(OpenFire.this);
	                dialog.setTitle("��J������");
	                dialog.setView(v1);
	                dialog.setPositiveButton("�T�w", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                        try {
	                            receiver=input_receiver.getText().toString();
	                            if (receiver.equals("")) {
	                                Toast.makeText(OpenFire.this, "�����̤��i���ť�", Toast.LENGTH_LONG).show();
	                            }else{
	                                title="S00"+(id+1);
	                                content="�i�J�Գ�";
	                                fileUploadToSend("sned");
	                                Toast.makeText(OpenFire.this,"OK",Toast.LENGTH_SHORT).show();
	                            }
	                        } catch (Exception e) {
	                            Toast.makeText(OpenFire.this, "���~!", Toast.LENGTH_LONG).show();
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
	                Toast.makeText(OpenFire.this,"�K�X���~�A�бq�s��J",Toast.LENGTH_SHORT).show();
	            }


	        }
	    }


	   public void fileUploadToSend(String arg) {
        // �ˬd�ϥΪ̦��S����ܭn�W�Ǫ��ɮ�,��ܦn���ɮ׷|�g�Jfile_choice��table��
        file_path = new ArrayList<String>();
        attachFile = attachment.replace("&", "");
        attachPath = AttachParameter.sdcardPath + attachFile;
        Cursor up_file_cursor = getContentResolver().query(Uri.parse("content://tab.list.d2d/file_choice"), form, null, null, null);
//        Cursor up_file_cursor = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), form, null, null, null);
        if (up_file_cursor.getCount() > 0) {
            System.out.println("Show up_file_cursor: " + up_file_cursor.getCount());
            dialog = ProgressDialog.show(OpenFire.this, "�еy��", "��ƳB�z��", true);
            dialog.show();
            up_file_cursor.moveToFirst();
            file_path.add(up_file_cursor.getString(0));
            selfId = randomString(20);
            postFile = new String();
            System.out.println("Show up_file_cursor detail: " + up_file_cursor.getCount() + " selfId: " + selfId);

            for (index = 0; index < up_file_cursor.getCount(); index++) {
                System.out.println("for loop entry, index: " + index);
                boolean[] checktype = new boolean[AttachParameter.filetype];
                checktype = AttachParameter.checktype(attachPath);
                System.out.println("for loop entry, checktype: " + checktype);
                // �p�G��ܪ��O�v���A�h�p��L���v�����סB�j�p�A�ت��O�n�ǵ�ffmpeg�ϥ�
                if (checktype[AttachParameter.video]||checktype[AttachParameter.music]||checktype[AttachParameter.photo]) {
                    System.out.println("if loop entry");
                    File file = new File(file_path.get(index));
                    // �}�ҭp���ɮת��רæs�JattachSize
                    file_size = (int) file.length();
                    file_name = file.getName();
                    postFile = postFile + "&file_name" + index + "_0=" + file_name;
                    duration = 0;
                    file_amount = 1;

                    System.out.println("file_size: " + file_size + " file_name: " + file_name + " postFile: " + postFile);

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
                    getContentResolver().insert(Uri.parse("content://tab.list.d2d/temp_file"), values);
                    //dialog.dismiss();
                    state = "write";
                    dialog.dismiss();
                    new sendFile().execute();
                }
                else{
                    //2016/07/04�s�W�ק�
                    Toast.makeText(OpenFire.this, "�A�ҿ諸�ɮ��������䴩�A�Э��s���", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
                up_file_cursor.moveToNext();
            }
        } else {
            Toast.makeText(OpenFire.this, "�|����ܪ��[�ɮסA�Э��s���", Toast.LENGTH_LONG).show();
        }

        state = "write";
        up_file_cursor.close();
        index = 0;
        // �I�s�۩w�q���禡

    }
	    private class sendFile extends AsyncTask<Void, Void, String> {
	        // Asyntask �e�m�@�~
	        @Override
	        protected void onPreExecute() {
	            sendDialog = ProgressDialog.show(OpenFire.this, "�еy��", "��ƤW�Ǥ�", true);
	            sendDialog.show();
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

	                /// ex : storage/emulated/0/DCIM/100ANDRO/MOV_0259.mp4
//	                File file = new File("/storage/emulated/0/KM/0R2emEfBL_lbe-_0.png");
	                File file = new File(attachment);
	                String filename = file.getName();
	                filetype = filename.split("\\.");
	                Cursor check_finish_cursor = getContentResolver().query(Uri.parse("content://tab.list.d2d/temp_file"), new String[] { UserSchema._FILEPATH, UserSchema._FILENAME}, "filecheck='0' and filerecord='file0_0' and selfid='" + selfId + "'", null, null);
	                if(check_finish_cursor.getCount()>0){
	                    check_finish_cursor.moveToFirst();
	                    file0_0=check_finish_cursor.getString(0);
	                    fileName = check_finish_cursor.getString(1);
	                    firstfile =new File(file0_0 + filename);

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
	                        "&filepath=" + attachPath + "&length=" + file_size +
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
	                    Cursor change_token = getContentResolver().query(Uri.parse("content://tab.list.d2d/temp_content"), Form, "selfid='" + selfId + "'", null, null);
	                    if (change_token.getCount() > 0) {
	                        change_token.moveToFirst();
	                        int id_this = 0;
	                        id_this = Integer.valueOf(change_token.getString(0));
	                        ContentValues values = new ContentValues();
	                        values.put(UserSchema._MESSAGETOKEN, resp.replace("ret=0&token=", ""));
	                        String where = UserSchema._ID + " = " + id_this;
	                        getContentResolver().update(Uri.parse("content://tab.list.d2d/temp_content"), values, where, null);

	                    }
	                    change_token.close();

	                    //�Ĥ@���W�ǧ�  ��sFIRST��TRUE
	                    Cursor change_first = getContentResolver().query(Uri.parse("content://tab.list.d2d/temp_content"), Form, "selfid='" + selfId + "'", null, null);
	                    if (change_first.getCount() > 0) {
	                        change_first.moveToFirst();
	                        int id_this = 0;
	                        id_this = Integer.valueOf(change_first.getString(0));
	                        ContentValues values = new ContentValues();
	                        values.put(UserSchema._FIRST, "true");
	                        String where = UserSchema._ID + " = " + id_this;
	                        getContentResolver().update(Uri.parse("content://tab.list.d2d/temp_content"), values, where, null);

	                    }
	                    change_first.close();

	                    //20160903 �����Ϊ�
	                    token=resp.replace("ret=0&token=", "");

	                    Cursor up_tempfile = getContentResolver().query(Uri.parse("content://tab.list.d2d/temp_file"), Form, "selfid='" + selfId + "' and filecheck='0'", null, null);
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
	                        getContentResolver().update(Uri.parse("content://tab.list.d2d/temp_file"), values, where, null);
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
	                mHandler.obtainMessage(ok).sendToTarget();

	            }

	        }
	    }

	    private class decrypt extends AsyncTask<Void, Void, String> {
	        String source = null,filename = null;
	        String[] two_source;
	        Bitmap source1,source2,final_image;
	        OutputStream f = null;

	        protected void onPreExecute() {
	            // �}�Ҹ�ƶǰedialog
	            pdialog = ProgressDialog.show(OpenFire.this, "�еy��", "�v���ѱK��", true);
	            pdialog.show();
	        }

	        @Override
	        protected String doInBackground(Void... arg0) {
	            String res = null;
	            //Cursor get_source = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), new String[]{UserSchema._FILEPATH,UserSchema._FILENAME}, "messagetoken='" + gettoken + "'", null, null);

	            Cursor get_source = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), new String[]{UserSchema._FILEPATH, UserSchema._FILENAME}, "messagetoken='" + gettoken + "'", null, null);
	            int count=get_source.getCount();
	            if(count>0){
	                get_source.moveToFirst();
	                source=get_source.getString(0);
	                filename = get_source.getString(1);
	                //���⦸token�W�٪��z��(�z��"_"),�p�G�ɦW�O��token + "_"�զ�����
	            }
	            get_source.close();

	            getContentResolver().query(Uri.parse("content://tab.list.d2d/missile_fire"), new String[]{UserSchema._FILE_1, UserSchema._FILE_2}, "file_1'" + source + "'", null, null);

	            AttachParameter.image_decrypt_file = AttachParameter.sdcardPath + UserSchema._FILENAME + "_reslut.png";
	            two_source=source.split("&");
	            source1 = BitmapFactory.decodeFile(AttachParameter.sdcardPath + two_source[1]);
	            source2 = BitmapFactory.decodeFile(AttachParameter.sdcardPath + two_source[2]);
//	            source1 = BitmapFactory.decodeFile(AttachParameter.sdcardPath+"file_name0_0-lbe.png");
//	            source2 = BitmapFactory.decodeFile(AttachParameter.sdcardPath+"file_name0_1-lbe.png");
	            //�ѽX
	            final_image = Bitmap.createBitmap(source1.getWidth(),source1.getHeight(), Bitmap.Config.ARGB_4444);
	            for (int i = 0; i < source1.getHeight(); i += 2) {
	                for (int j = 0; j < source1.getWidth(); j += 2) {
	                    if (source1.getPixel(j, i) == -16777216 && source2.getPixel(j+1,  i) == -16777216){
	                        final_image.setPixel(j, i, -16777216);
	                        final_image.setPixel(j+1, i, -16777216);
	                        final_image.setPixel(j, i+1, -16777216);
	                        final_image.setPixel(j+1, i+1, -16777216);
	                    }
	                    else if (source1.getPixel(j, i) == -1 && source2.getPixel(j+1,  i) == -1){
	                        final_image.setPixel(j, i, -16777216);
	                        final_image.setPixel(j+1, i, -16777216);
	                        final_image.setPixel(j, i+1, -16777216);
	                        final_image.setPixel(j+1, i+1, -16777216);
	                    }
	                    else {
	                        final_image.setPixel(j, i, -1);
	                        final_image.setPixel(j+1, i, -1);
	                        final_image.setPixel(j, i+1, -1);
	                        final_image.setPixel(j+1, i+1, -1);
	                    }

	                }
	                System.out.println("��X�`Height�O"+source1.getHeight()+" �ثeHeight�O"+i);

	            }

	            try{

	                f = new FileOutputStream(new File( AttachParameter.image_decrypt_file));
	                final_image.compress(Bitmap.CompressFormat.PNG, 100, f);

	                f.flush();
	                f.close();
	            }catch(Exception e){
	                e.printStackTrace();
	            }finally{
	                try{
	                    if(f!=null){
	                        f.close();
	                    }
	                }catch(IOException e){
	                    e.printStackTrace();
	                }
	            }

	            return res;
	        }
	        protected void onPostExecute(String reretrieve) {

	            //��ϸ��|�i�J�n��ifile_choice

	            pdialog.dismiss();
	            AlertDialog.Builder Dialog = new AlertDialog.Builder(OpenFire.this); // Dialog
	            Dialog.setTitle("");
	            Dialog.setMessage("�ѱK���\");
	            Dialog.setIcon(android.R.drawable.ic_dialog_info);
	            Dialog.setNeutralButton("�T�w", new DialogInterface.OnClickListener() { // ���Uretry
	                // �Nthread����
	                // �A�]�@�ӷs��thread
	                @Override
	                public void onClick(DialogInterface dialog, int which) {
	                    onResume();
	                }
	            });
	            Dialog.show();

	        }
	    }
	 
	    /*
	     * �o��O��ť�A�����v�T���[���ɡA�X�{��menu�M��
	     */
	    @Override
	    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
	        // TODO Auto-generated method stub
	        super.onCreateContextMenu(menu, v, menuInfo);
	        menu.setHeaderTitle("");
	        menu.setHeaderIcon(R.drawable.m2m_ver01);
	        // �T�ؿ��

	        menu.add(0, ITEM1, 0, "���s�U��");
	        menu.add(0, ITEM2, 0, "�R���T��");
	        menu.add(0, ITEM3, 0, "����");

	        if (AttachParameter.priority == 0){
	            menu.add(0, ITEM4, 0, "�ѱK");
	        }else {
	            menu.add(0,ITEM5, 0, "�i�J�Գ�");
	        }

	    }

	    /*
	     * �o��O��ť�O���Umenu�����@��item�A�ھڤ��P��item�����P����
	     */
	    @Override
	    public boolean onContextItemSelected(MenuItem item) {
	        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
	        int pos = getinfo.size() - (int) getListAdapter().getItemId(menuInfo.position) - 1;
	        listid = pos;
	        gettoken = getinfo.get(pos).gettoken();
	        String[] tokenID = { UserSchema._ID, UserSchema._FILEID, UserSchema._RECEIVEID, UserSchema._SENDER };
	        switch (item.getItemId()) {
			/*
			 * item1���@���s�U�����ʧ@�A�u�n�hdb����X�bfileid�Y�i�A
			 * fileid�O����notify�q���ɡA�w��������id�A���۰�retrive�ۦP���ʧ@
			 */
	            case ITEM1:
	                final String[] itemForm = { UserSchema._RECEIVEID, UserSchema._MESSAGETOKEN, UserSchema._FILEPATH, UserSchema._FILEID };

	                // �T�ؿ��
	                //Cursor data_cursor = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), itemForm, "notification = '0' and messagetoken='" + getinfo.get(listid).gettoken() + "' and receive_id ='' and file_id is not null", null, null);

	                Cursor data_cursor = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), itemForm, "(notification = '0' or notification = '3')and messagetoken='" + getinfo.get(listid).gettoken() + "'", null, null);
	                if(data_cursor.getCount()>0){

	                    Cursor CH_state = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), null, "userstatus='download'", null, null);
	                    if (CH_state.getCount() > 0) {
	                        Toast.makeText(getApplicationContext(), "�ثe�|���ɮפU�����A�y��A��", Toast.LENGTH_LONG).show();

	                    } else {
	                        updateNotification("2", "sms");
	                        Cursor copy_fileid_cursor = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), tokenID, "messagetoken='" + gettoken + "'", null, null);
	                        if (copy_fileid_cursor.getCount() > 0) {
	                            copy_fileid_cursor.moveToFirst();

	                            Again_id = copy_fileid_cursor.getString(1).substring(1,copy_fileid_cursor.getString(1).length()).split("&");
	                            Again_token = gettoken;
	                            Again_sender = copy_fileid_cursor.getString(3);
	                            int id_this = Integer.valueOf(copy_fileid_cursor.getString(0));
	                            ContentValues values = new ContentValues();
	                            values.put(UserSchema._RECEIVEID, copy_fileid_cursor.getString(1));
	                            values.put(UserSchema._FILEPATH, "");

	                            String where = UserSchema._ID + " = " + id_this;
	                            getContentResolver().update(Uri.parse("content://tab.list.d2d/user_data"), values, where, null);
	                        }
	                        copy_fileid_cursor.close();
	                        // ���Ffileid��A�Y�i���s�U��
	                        new retrieveAgain().execute();
	                        //viewfile(sdcardPath + getinfo.get(listid).getFilename());

	                    }
	                    CH_state.close();
	                }
	                else{
	                    Toast.makeText(getApplicationContext(), "��p�A�ɮױq���U���L�A�L�k���s�U��", Toast.LENGTH_SHORT).show();
	                }
	                data_cursor.close();
	                break;
	            case ITEM2:

	                // --------------------Delete------------
	                // cursor(��ƪ�ӷ�,����������W��,����)//��������W�١Guser_data��ƪ�ID�ȡA����Gitemclick�I���ɩұo��token
	                Cursor sender_data_group_cursor = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), tokenID, "messagetoken='" + gettoken + "'", null, null);
	                if (sender_data_group_cursor.getCount() > 0) {
	                    sender_data_group_cursor.moveToFirst();
	                    int id_this = 0;
	                    id_this = Integer.valueOf(sender_data_group_cursor.getString(0));
	                    String where = UserSchema._ID + " = " + id_this;
	                    getContentResolver().delete(Uri.parse("content://tab.list.d2d/user_data"),where, null);

	                }
	                sender_data_group_cursor.close();
	                new del_msg().execute();
	                break;
	            case ITEM3:
	            	Cursor fileAttach = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"),
	                        new String[] {UserSchema._FILEPATH, UserSchema._FILENAME, UserSchema._ID}, null, null, null);
	                if (fileAttach.getCount() > 0){
	                    fileAttach.moveToFirst();
	                    attachment = fileAttach.getString(0);
	                    fileName = fileAttach.getString(1);
	                    fileId = fileAttach.getString(2);

//	                    String[] pathArray = attachment.split("&");
	//
//	                    for (String attachmentPath : pathArray){
//	                        System.out.println("AttachmentPath: " + sdcardPath + attachmentPath);
//	                    }
	                    System.out.println("AttachmentPath: " + AttachParameter.sdcardPath + attachment.replace("&", ""));
	                    System.out.println("file detail = " + attachment + " " + fileName + " " + fileId + " ");

	                    LayoutInflater factory = LayoutInflater.from(OpenFire.this);
	                    final View v1 = factory.inflate(R.layout.check_password, null);

	                    chk_Password = (EditText) v1.findViewById(R.id.chk_Password);
	                    AlertDialog.Builder dialog = new AlertDialog.Builder(OpenFire.this);
	                    dialog.setTitle("�����T�{");
	                    dialog.setView(v1);
	                    dialog.setPositiveButton("�T�w", new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog, int whichButton) {
	                            try {
	                                check_pass=chk_Password.getText().toString();
	                                new check_password().execute();
	                            } catch (Exception e) {
	                                Toast.makeText(OpenFire.this, "���~!", Toast.LENGTH_LONG).show();
	                                System.out.println("�����T�{��: " + e.getMessage());
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
	                    Toast.makeText(OpenFire.this,"!!!!",Toast.LENGTH_SHORT).show();
	                }else {
	                    Toast.makeText(OpenFire.this,"file is nothing",Toast.LENGTH_SHORT).show();
	                }
	                fileAttach.close();
	                break;
	            case ITEM4:
	                new decrypt().execute();
	                break;
	            case ITEM5:
	            	 Cursor file_Attach = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"),
	                         new String[] {UserSchema._FILEPATH, UserSchema._FILENAME, UserSchema._ID}, null, null, null);
	                 if (file_Attach.getCount() > 0){
	                	 file_Attach.moveToFirst();
	                     String filePath = file_Attach.getString(0);
	                     String fileName = file_Attach.getString(1);
	                     String fileId = file_Attach.getString(2);

	                     System.out.println("file detail = " + filePath + " " + fileName + " " + fileId + " ");

	                     LayoutInflater factory = LayoutInflater.from(OpenFire.this);
	                     final View v1 = factory.inflate(R.layout.check_password, null);

	                     chk_Password = (EditText) v1.findViewById(R.id.chk_Password);
	                     AlertDialog.Builder dialog = new AlertDialog.Builder(OpenFire.this);
	                     dialog.setTitle("�����T�{");
	                     dialog.setView(v1);
	                     dialog.setPositiveButton("�T�w", new DialogInterface.OnClickListener() {
	                         public void onClick(DialogInterface dialog, int whichButton) {
	                             try {
	                                 check_pass=chk_Password.getText().toString();
	                                 new check_password().execute();
	                             } catch (Exception e) {
	                                 Toast.makeText(OpenFire.this, "���~!", Toast.LENGTH_LONG).show();
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
	                     Toast.makeText(OpenFire.this,"!!!!",Toast.LENGTH_SHORT).show();
	                 }else {
	                     Toast.makeText(OpenFire.this,"file is nothing",Toast.LENGTH_SHORT).show();
	                 }
	                 file_Attach.close();
	                break;

	        }
	        return super.onContextItemSelected(item);

	    }
	
	 @Override
	    protected void onResume() {// ���B��,�C������������,�ߧY������s
	        super.onResume();
	        if(finish){
	            mailcount = 0;
	            list.clear();
	            list = getData();
	            adapter.notifyDataSetChanged();
	        }
	    }

	    @Override
	    protected void onDestroy() {
	        super.onDestroy();
	    }
	
	 private void updateState(String state, String conditional) {
	        Cursor change_state = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), new String[] { UserSchema._ID, UserSchema._RECEIVEID }, conditional, null, null);
	        if (change_state.getCount() > 0) {
	            change_state.moveToFirst();
	            for (int i = 0; i < change_state.getCount(); i++) {
	                int id_this = 0;
	                id_this = Integer.valueOf(change_state.getString(0));
	                ContentValues values = new ContentValues();
	                values.put(UserSchema._USESTATUS, state);
	                String where = UserSchema._ID + " = " + id_this;
	                getContentResolver().update(Uri.parse("content://tab.list.d2d/user_data"), values, where, null);
	                change_state.moveToNext();
	            }

	        }
	        change_state.close();
	    }

	 private void updateNotification(String state, String type) {
	        Cursor noti_cursor = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), new String[] { UserSchema._ID, UserSchema._NOTIFICATION }, "messagetoken='" + getinfo.get(listid).gettoken() + "'", null, null);
	        if (noti_cursor.getCount() > 0) {
	            noti_cursor.moveToFirst();
	            ContentValues values = new ContentValues();
	            values.put(UserSchema._NOTIFICATION, state);
	            values.put(UserSchema._TYPE, type);
	            int id_this = Integer.parseInt(noti_cursor.getString(0));
	            String where = UserSchema._ID + " = " + id_this;
	            getContentResolver().update(Uri.parse("content://tab.list.d2d/user_data"), values, where, null);

	        }
	        noti_cursor.close();
	    }
	
	  public static String changeToSeries(String token, String arg, int no) {//
	        String series = new String();

	        for (int i = 0; i < no; i++) {
	            series = series + "&" + token+"_"+arg.replace(".", "-_" + String.valueOf(i) + ".");
	        }
	        return series;

	    }
	 
	 public void replaceSeq(String conditional, String mod, String name) {
	        int id_this;
	        String where;
	        Cursor check_fileid_cursor = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), new String[] { UserSchema._ID, UserSchema._RECEIVEID, UserSchema._FILEPATH }, conditional, null, null);

	        if (check_fileid_cursor.getCount() > 0) {
	            check_fileid_cursor.moveToFirst();
	            ContentValues values = new ContentValues();
	            // ���]split �����e�O &10
	            String[] split = (name).split("_-");
	            // ���]check_id �����e�O&10&11&12&13
	            String check_id = check_fileid_cursor.getString(1);
	            // split_id[0~4]�����e���O�O"",10,11,12,13
	            String[] split_id = check_id.split("&");
	            // ���]split_id�j��2
	            if (split_id.length != 2) {
	                // &10& ������ &
	                // �ҥH&10&11&12&13 �|�ܦ� &11&12&13
	                check_id = check_id.replace("&" + split[0] + "&", "&");
	            } else {// ��split_id���׵���2
	                // split_id[0~1]�����e�� "",13
	                // &13 ������ ""
	                // �ҥH&13 �|����
	                check_id = check_id.replace("&" + split[0], "");
	            }

	            System.out.println(check_id);
	            values.put(UserSchema._RECEIVEID, check_id);
	            id_this = Integer.parseInt(check_fileid_cursor.getString(0));
	            where = UserSchema._ID + " = " + id_this;
	            getContentResolver().update(Uri.parse("content://tab.list.d2d/user_data"), values, where, null);
	        }
	        check_fileid_cursor.close();
	    }
	 
	private List<Map<String, Object>> getData() {

	        getinfo = new ArrayList<openFireInfo>();
	        String[] Form = { UserSchema._TITTLE, UserSchema._CONTENT, UserSchema._FILEPATH, UserSchema._DATE, UserSchema._MESSAGETOKEN, UserSchema._FILECOUNT };
	        String[] typefrom = {UserSchema._NOTIFICATION, UserSchema._TYPE};
	        openFireInfo tempinfo = new openFireInfo();
	        // �ϥαH��̥h���X �L�h���w�gŪ�L��²�T�H�Υ��Q�R�������,�� tittle��userstatus�h�ˬd
	        Cursor sender_data_group_cursor = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), Form, "sender='" + sender + "' and read is not null and userstatus IN ('', 'download')", null, null);
	        if (sender_data_group_cursor.getCount() > 0) {
	            sender_data_group_cursor.moveToFirst();
	            for (int i = 0; i < sender_data_group_cursor.getCount(); i++) {
	                tempinfo.setTitle(sender_data_group_cursor.getString(0));
	                tempinfo.seContent(sender_data_group_cursor.getString(1));
	                tempinfo.setFile(sender_data_group_cursor.getString(2));
	                tempinfo.setDate(sender_data_group_cursor.getString(3));
	                tempinfo.setoken(sender_data_group_cursor.getString(4));
	                tempinfo.seFileCount(sender_data_group_cursor.getString(5));
	                getinfo.add(tempinfo);
	                mailcount++;
	                tempinfo = new openFireInfo();
	                sender_data_group_cursor.moveToNext();
	            }
	        }
	        sender_data_group_cursor.close();

	        Map<String, Object> map = new HashMap<String, Object>();
	        for (int i = mailcount - 1; i >= 0; i--) {
	            map = new HashMap<String, Object>();
	            // �o��DB�O�ˬd�L�h�O�_�����������ɮסA�p�G�����������ɮסA�h����ĸ����Ϯ׳q���ϥΪ�
	            Cursor ch_finish_cursor = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), null, "messagetoken='" + getinfo.get(i).gettoken() + "' and receive_id!=''", null, null);
	            if (ch_finish_cursor.getCount() > 0) {
	                map.put("fin", R.drawable.not_fihish);
	            } else {
	                map.put("fin", R.drawable.fihish);
	            }
	            ch_finish_cursor.close();
	            Cursor type_cursor = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), typefrom, "messagetoken='" + getinfo.get(i).gettoken()+"'", null, null);
	            if (type_cursor.getCount() > 0) {
	                type_cursor.moveToFirst();
	                String not = type_cursor.getString(0);
	                String type = type_cursor.getString(1);
	                if(not!= null &&type != null){
	                    if(not.equalsIgnoreCase("1")){
	                        if(type.equalsIgnoreCase("wlan")){
	                            ////map.put("type", R.drawable.wlan_g);
	                        }
	                        else{
	                            //map.put("type", R.drawable.d2d_g);
	                        }
	                    }
	                    else{
	                        if(type.equalsIgnoreCase("wlan")){
	                            //map.put("type", R.drawable.wlan_r);
	                        }
	                        else{
	                            //map.put("type", R.drawable.d2d_r);
	                        }
	                    }
	                } else {
	                    //map.put("type", R.drawable.fihish);
	                }

	            } else {
	                //map.put("type", R.drawable.fihish);
	            }
	            type_cursor.close();

	            // put info array into list
	            map.put("title", getinfo.get(i).getTitle());
	            map.put("info", getinfo.get(i).getContent());
	            map.put("date", getinfo.get(i).getDate());
	            map.put("filecount", "�@�� 1/"+getinfo.get(i).getFileCount()+" ���ɮ�");
	            // �ˬd�ɮ������A��m�۹������ɮ��������Ϥ�
	            boolean[] checktype = new boolean[AttachParameter.filetype];
	            checktype = AttachParameter.checktype(getinfo.get(i).getFilename());
	            System.out.println("�ݲM���1  "+getinfo.get(i).getFilename());
	            Pattern patternmovie = Pattern.compile(".*.movie");
	            Matcher matchermovie = patternmovie.matcher(getinfo.get(i).getFilename());

	            if (checktype[AttachParameter.music]) { // music set a note
	                map.put("img", R.drawable.notes);
	                map.put("filecount", "");

	            } else if (checktype[AttachParameter.video]) { // video set a camera
	                String videoSegment[] = getinfo.get(i).getFilename().split("&");
	                Bitmap filebitmap;

	                //�]��d2d�O��@�ɮ� �S������ �ҥH�o��n�ˬd
	                if(videoSegment.length==1){
	                    filebitmap = ThumbnailUtils.createVideoThumbnail(AttachParameter.sdcardPath + getinfo.get(i).getFilename(), MediaStore.Images.Thumbnails.MICRO_KIND);
	                }else{
	                    filebitmap = ThumbnailUtils.createVideoThumbnail(AttachParameter.sdcardPath + videoSegment[1], MediaStore.Images.Thumbnails.MICRO_KIND);
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
	                    map.put("img", R.drawable.notify1);

	                } catch (IOException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                    map.put("img", R.drawable.notify1);
	                }catch (Exception e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                    map.put("img", R.drawable.notify1);
	                }
	                map.put("img", thumbnails + "temp_" + i + ".jpg");
	                map.put("filecount", "");
	            } else if (checktype[AttachParameter.photo]){ // photo set photo
	                map.put("img", AttachParameter.sdcardPath + getinfo.get(i).getFilename().replace("&", ""));
	                map.put("filecount", "");
	            } else if (matchermovie.find()) {
	                map.put("img", R.drawable.iconvideo);
	                map.put("filecount", "");
	            } else
	                map.put("img", R.drawable.notify1);

	            list.add(map);
	        }

	        return list;
	    }
	 
	// �۩w�q���s���ɮסA�o�̿z�粒�ɮ׫�A�|�ۦ�j�Mandroid�t�Τ��w�]���{���}��
	    public void viewfile(String attachment) {
	        boolean[] checktype = new boolean[AttachParameter.filetype];
	        checktype = AttachParameter.checktype(attachment);

	        if (checktype[AttachParameter.music]) {
	            Intent it = new Intent(Intent.ACTION_VIEW);
	            File file = new File(attachment);
	            it.setDataAndType(Uri.fromFile(file), "audio/*");

	            startActivity(it);
	        } else if (checktype[AttachParameter.video]) {
	            if (viewmod.equals("preview")) {
	                Intent intent = new Intent();
	                intent.setClass(OpenFire.this, VideoView.class);
	                Bundle bundle = new Bundle();
	                bundle.putString("playList", attachment); // �N�Ѽƶǻ���videoView
	                intent.putExtras(bundle);
	                startActivity(intent);
	            } else {
	                Intent intent = new Intent();
	                intent.setClass(OpenFire.this, VideoView.class);
	                //intent.setClass(receivelist.this, VideoPlayActivity.class);
	                Bundle bundle = new Bundle();
	                bundle.putString("playList", attachment); // �N�Ѽƶǻ���videoView
	                intent.putExtras(bundle);
	                startActivity(intent);
	            }

	        } else if (checktype[AttachParameter.photo]) {
	            Intent it = new Intent(Intent.ACTION_VIEW);
	            File file = new File(attachment);
	            it.setDataAndType(Uri.fromFile(file), "image/*");

	            startActivity(it);
	        } else {
	            System.out.println("�o�����"+attachment);
	            AlertDialog.Builder Dialog = new AlertDialog.Builder(this); // Dialog
	            Dialog.setTitle("��p...�ɮצ�m���s�b");
	            Dialog.setMessage("�иյۭ��s��z");
	            Dialog.setIcon(android.R.drawable.ic_dialog_info);
	            Dialog.setNegativeButton("�T�w", new DialogInterface.OnClickListener() { // �������ɮ�
	                @Override
	                public void onClick(DialogInterface dialog, int which) {
	                }
	            });
	            Dialog.show();
	        }

	    }
	
	 private Handler mHandler = new Handler() {

	        // ����k�bui�u�{�B��
	        public void handleMessage(Message msg) {
	            //what�N���J���Ѽ�,����switch�P�_��J���Ѽ�,case�A�i��ʧ@,75�榳�Ѽ�
	            switch (msg.what) {
	                case SHOW_MSG:
	                    AlertDialog.Builder Dialog = new AlertDialog.Builder(OpenFire.this); // Dialog
	                    Dialog.setTitle(tittle);
	                    Dialog.setMessage(message);
	                    Dialog.setIcon(android.R.drawable.ic_dialog_info);
	                    Dialog.setPositiveButton(bmsg, new DialogInterface.OnClickListener() { // ���Uabort
	                        // �Nthread����
	                        // ����progressbar
	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {

	                        }
	                    });
	                    Dialog.show();
	                    tittle="";
	                    message="";
	                    bmsg="";
	                    onResume();
	                    break;
	                case UPDATE:

	                    pdialog.setMessage(upmsg);
	                    upmsg="";

	            }
	        }
	    };

	    public String randomString(int len) {
	        String str = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < len; i++) {
	            int idx = (int)(Math.random() * str.length());
	            sb.append(str.charAt(idx));
	        }
	        return sb.toString();
	    }
}
