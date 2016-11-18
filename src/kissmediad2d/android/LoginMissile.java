package kissmediad2d.android;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.teleal.cling.android.AndroidUpnpService;
import org.teleal.cling.binding.LocalServiceBindingException;
import org.teleal.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.teleal.cling.model.DefaultServiceManager;
import org.teleal.cling.model.ValidationException;
import org.teleal.cling.model.meta.DeviceDetails;
import org.teleal.cling.model.meta.DeviceIdentity;
import org.teleal.cling.model.meta.LocalDevice;
import org.teleal.cling.model.meta.LocalService;
import org.teleal.cling.model.meta.ManufacturerDetails;
import org.teleal.cling.model.meta.ModelDetails;
import org.teleal.cling.model.types.DeviceType;
import org.teleal.cling.model.types.UDADeviceType;
import org.teleal.cling.model.types.UDAServiceType;
import org.teleal.cling.model.types.UDN;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import database.storage.messageProcess;
import login.submit1.Logout;
import login.submit1.Retrieve;
import login.submit1.User;
import login.submit1.Login;
import login.submit1.locupdate;
import tab.list.AttachParameter;
import tab.list.FileContentProvider;
import tab.list.FileContentProvider.UserSchema;
import upnp.service.BrowserUpnpService;
import upnp.service.SwitchPower;
import upnp.service.UPnPDeviceFinder;

public class LoginMissile extends Activity implements OnClickListener, PropertyChangeListener{
	
	private UDN udn = UDN.uniqueSystemIdentifier("Demo Binary Light");
    private AndroidUpnpService upnpService;
    private EditText et, et2;
    private CheckedTextView ct;
    private String getid, getpw, getip;
    private int loginSuccess = 0,ss=0;
    private static final int UPDATE_LIST_MES = 0,UPDATE_FFMPEG=1,timeout=3,change_word=4,SHOW_NOTIFY=5;
    private boolean finishUpdateList = false;
    public boolean retrieving = true;
    public String sms = null;
    public String loginRequestString = new String();
    public static String login_name;
    public static String Homeip = "140.138.150.26";
    public static String method;
    Boolean idle=true;
    String[] aliveIp;
    String notify_msg=new String();
    private NotificationManager gNotMgr = null;
    public String[] rep, req;
    Button btnReady, btnReg, btnFrie, btnEdit, btnLogin, btnIn;
    locupdate locup =new locupdate();
    ProgressDialog pdialog = null,re_ffmpeg_dialog=null,Dialog_for_login = null;
    int tolen,hasRead;
    String content;
    Logout logout = new Logout();
    Thread aliveThread;
    Thread listThread;
    FileContentProvider KM_DB = new FileContentProvider();
    ContentResolver contentResolver;
    private static final Logger log = Logger.getLogger(LoginMissile.class.getName());
    int i = 0;
    List<String> fileList = new ArrayList<String>();
    String bdtoken =new String();
    String bdsender =new String();
    String bdtype =new String();
    String bdreretrieve = new String();
    Activity appCompatActivity;
    private UPnPDeviceFinder mDevfinder  = null;
   

    TextView name;

    @Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		
        mDevfinder=null;
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        getApplicationContext().bindService(
                new Intent(this, BrowserUpnpService.class),
                serviceConnection,
                Context.BIND_AUTO_CREATE
        );
        File file = new File(AttachParameter.sdcardPath);
        if(!file.exists()){
            file.mkdir();
        }

        // ����sqlite��DB�������A�s�W�@��table�A�s��Ҧ�"�H��̪�²�T"
        KM_DB.new_table(Uri.parse("content://tab.list.d2d/user_data"));
        // ����"�H���"
        KM_DB.new_table(Uri.parse("content://tab.list.d2d/user_group"));
        // �������W���ɮ�
        KM_DB.new_table(Uri.parse("content://tab.list.d2d/user_reply"));
        KM_DB.new_table(Uri.parse("content://tab.list.d2d/user_info"));
        KM_DB.new_table(Uri.parse("content://tab.list.d2d/temp_content"));
        KM_DB.new_table(Uri.parse("content://tab.list.d2d/temp_ffmpeg"));
        KM_DB.new_table(Uri.parse("content://tab.list.d2d/missile_group"));
        KM_DB.new_table(Uri.parse("content://tab.list.d2d/missile_fire"));
        btnReady = (Button) this.findViewById(R.id.no_bn);
        btnReg = (Button) this.findViewById(R.id.reg_bn);
        btnFrie = (Button) this.findViewById(R.id.in_bn);
        btnEdit = (Button) this.findViewById(R.id.ed_bn);
        btnLogin = (Button) this.findViewById(R.id.log_bn);
        btnIn = (Button) this.findViewById(R.id.send_bn);
        name = (TextView)this.findViewById(R.id.username);

        btnReady.setEnabled(false);
        btnFrie.setEnabled(false);
        btnEdit.setEnabled(false);
        btnIn.setEnabled(false);

        btnReady.setOnClickListener(this);
        btnReg.setOnClickListener(this);
        btnFrie.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnIn.setOnClickListener(this);

        String[] TestForm = { UserSchema._ID, UserSchema._SENDER,
                UserSchema._TITTLE, UserSchema._CONTENT,
                UserSchema._MESSAGETOKEN, UserSchema._FILESIZE,
                UserSchema._DATE, UserSchema._FILEPATH,
                UserSchema._RECEIVEID, UserSchema._USESTATUS,
                UserSchema._FILEID };
        updateState("", TestForm, "userstatus!='delete'");
        contentResolver = getContentResolver();
        appCompatActivity = LoginMissile.this;
        Cursor info_cursor = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_info"), null, null, null, null);
        if(info_cursor.getCount()==0){
            ContentValues values = new ContentValues();
            values.put(UserSchema._REMEMBER, "false");
            getContentResolver().insert(Uri.parse("content://tab.list.d2d/user_info"), values);
            values = null;
        }
        info_cursor.close();

        Cursor cursor = getContentResolver().query(Uri.parse("content://tab.list.d2d/temp_file"), new String[] {UserSchema._SELFID, UserSchema._MESSAGETOKEN }, null, null, null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            for(int a=0;a<cursor.getCount();a++){
                cursor.getString(0);
                cursor.getString(1);
                cursor.moveToNext();
            }
        }
        cursor.close();
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		  Intent intent = new Intent();
	      Bundle bundle = new Bundle();
	      ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	      NetworkInfo info = CM.getActiveNetworkInfo();
		
	      switch (v.getId()){

          case R.id.no_bn:
              bundle.putString("location","2");
              intent.putExtras(bundle);
              FileContentProvider test = new FileContentProvider();
              test.del_table(Uri.parse("content://tab.list.d2d/file_choice"));
              intent.setClass(LoginMissile.this, MainActivity.class);
              startActivity(intent);
              break;

          case R.id.send_bn:
              bundle.putString("location","0");
              intent.putExtras(bundle);
              intent.setClass(LoginMissile.this,MainActivity.class);
              if (info == null || !info.isAvailable()) {
                  Toast.makeText(LoginMissile.this, "�ثe�S��������!�ҥH�L�k�i�J�q��", Toast.LENGTH_LONG).show();
              }else{
                  new getcontent().execute();
              }
              break;

          case R.id.ed_bn:
              bundle.putString("location","3" );
              intent.putExtras(bundle);
              intent.setClass(LoginMissile.this, MainActivity.class);
              startActivity(intent);
              break;

          case R.id.in_bn:
              bundle.putString("location","1" );
              intent.putExtras(bundle);
              intent.setClass(LoginMissile.this, MainActivity.class);
              startActivity(intent);
              break;

          case R.id.log_bn:
              if (loginSuccess == 0){
                  Login();
              }else {
                  AttachParameter.selfid = "";
                  listThread.interrupt();
                  aliveThread.interrupt();
                  aliveThread=null;
                  listThread=null;
                  mDevfinder=null;

                  loginSuccess = 0;
                  retrieving = true;
                  finishUpdateList = true;

                  btnReady.setEnabled(false);
                  btnFrie.setEnabled(false);
                  btnEdit.setEnabled(false);
                  btnIn.setEnabled(false);

                  btnReg.setVisibility(View.VISIBLE);
                  btnLogin.setBackgroundResource(R.drawable.login_btn);


                  if (info == null || !info.isAvailable()) {
                      System.out.println("�ثe�S��������");
                  }else{
                      logout.logout_start();
                      name.setText("");

                  }
              }
              break;

          case R.id.reg_bn:
              if (loginSuccess == 0){
                  intent.setClass(LoginMissile.this, register.class);
                  startActivity(intent);
              }else {
                  bundle.putString("location","4" );
                  intent.putExtras(bundle);
                  intent.setClass(LoginMissile.this, MainActivity.class);
                  startActivity(intent);
              }
              break;
      }
      info=null;
      CM=null;
		
	}
	
	  public void Login() {
	        LayoutInflater factory = LayoutInflater.from(LoginMissile.this);
	        final View v1 = factory.inflate(R.layout.logininput, null);

	        et = (EditText) v1.findViewById(R.id.usr);
	        et2 = (EditText) v1.findViewById(R.id.pwd);
	        ct = (CheckedTextView) v1.findViewById(R.id.remember);
	        ct.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v)
	            {
	                ((CheckedTextView) v).toggle();
	            }
	        });
	        Cursor info_cursor = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_info"), new String[] { UserSchema._ACCOUNT,UserSchema._PASSWORD,UserSchema._REMEMBER }, null, null, null);
	        if(info_cursor.getCount()>0){
	            info_cursor.moveToFirst();
	            String remember = info_cursor.getString(2);
	            if(remember.equalsIgnoreCase("false")){
	                ct.setChecked(false);
	            }
	            else{
	                et.setText(info_cursor.getString(0));
	                et2.setText(info_cursor.getString(1));
	                ct.setChecked(true);
	            }
	        }
	        info_cursor.close();
	        AlertDialog.Builder dialog = new AlertDialog.Builder(LoginMissile.this);
	        dialog.setTitle("�Τ�n�J");
	        dialog.setView(v1);
	        dialog.setPositiveButton("�n�J", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	                try {
	                    // ���o�����A�Ȫ�����
	                    ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	                    NetworkInfo info = CM.getActiveNetworkInfo();

	                    getid = et.getText().toString();
	                    getpw = et2.getText().toString();
	                    getip = AttachParameter.Homeip;
	                    // �P�_��J���b�K�O�_���j��20
	                    if (getid.length() > 20 || getpw.length() > 20) {
	                        Toast.makeText(LoginMissile.this, "ID or Password can not over 20 characters", Toast.LENGTH_LONG).show();

	                    }else if(getid.length() == 0 || getpw.length() == 0){
	                        Toast.makeText(LoginMissile.this, "�b���αK�X���šA�Э��s��J", Toast.LENGTH_LONG).show();
	                    }
	                    // �P�_�O�_������
	                    else if (info == null || !info.isAvailable()) {
	                        Toast.makeText(LoginMissile.this, "�L�i�κ���", Toast.LENGTH_LONG).show();
	                    } else {
	                        // ����n�J���ʧ@
	                        new ConnectHttp().execute();

	                    }
	                } catch (Exception e) {
	                    Toast.makeText(LoginMissile.this, "���~!���ˬd����", Toast.LENGTH_LONG).show();
	                }
	            }

	            private void showToast() {
	                // TODO Auto-generated method stub
	                Toast.makeText(LoginMissile.this, "ID or Password can not over 20 characters", Toast.LENGTH_LONG).show();

	            }
	        });
	        dialog.setNegativeButton("����", new DialogInterface.OnClickListener() {

	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                // TODO Auto-generated method stub

	            }
	        });
	        dialog.show();
	    }

	    @Override
	    public void propertyChange(PropertyChangeEvent event) {
	        if (event.getPropertyName().equals("status")) {
	            log.info("Turning light: " + event.getNewValue());
	            setLightbulb((Boolean) event.getNewValue());
	        }
	    }


	    private class ConnectHttp extends AsyncTask<Void, Void, String[]> {

	        AlertDialog.Builder Dialog = new AlertDialog.Builder(LoginMissile.this);

	        protected void onPreExecute() {
	            Dialog_for_login = ProgressDialog.show(LoginMissile.this, "�еy��", "�n�J��", true);
	            Dialog_for_login.show();
	        }

	        // �b�I�����榹method�A���槹�|����onPostExecute
	        @Override
	        public String[] doInBackground(Void... params) {

	            String[] loginreturn = new String[3];
	            // �Ngetid�Pgetpw�s�@���n�J��T�A�������s�u�ǤJserver���Ѽ�
	            loginRequestString = "username=" + getid + "&password=" + getpw;
	            login_name = loginRequestString;

				/*
				 * �ϥ�login����login method�A�^�ǭȬO�@�Ӱ}�C loginreturn[0]�O�Ψ��ˬd���L���\
				 * loginreturn[1]�Oserver�^�Ǫ��T��
				 */
	            loginreturn = locup.login(getip, loginRequestString);
	            if(loginreturn[0] == "true"&& AttachParameter.chechsuccess(loginreturn[1])){
	                //�ϥ�ssdp�M��port
	                mHandler.obtainMessage(change_word).sendToTarget();
	                if(mDevfinder == null){
	                    mDevfinder = new UPnPDeviceFinder(true);
	                }
	                mHandler.obtainMessage(change_word).sendToTarget();
	                content="�ˬd���ҬO�_�i��UPNP";
	                ArrayList<String> devList = mDevfinder.getUPnPDevicesList();
	                //�^��IP
	                Map<String, String> map = new HashMap<String, String>();
	                for(int i = 0;i<devList.size();i++){
	                    String ss = devList.get(i);
	                    String index ="";
	                    String[]socket;
	                    if (ss.indexOf("Location: http://")!= -1){
	                        index = ss.substring(ss.indexOf("Location: http://")+17,ss.indexOf("Location: http://")+40);
	                        index = index.substring(0,index.indexOf("/"));
	                        socket = index.split(":");
	                        map.put(socket[0],socket[1]);

	                    }
	                    else if(ss.indexOf("Location:http://")!= -1){
	                        index = ss.substring(ss.indexOf("Location:http://")+16,ss.indexOf("Location:http://")+40);
	                        index =index.substring(0,index.indexOf("/"));
	                        socket=index.split(":");
	                        map.put(socket[0],socket[1]);
	                    }

	                    //String index = ss.substring(ss.indexOf("Location: http://"),ss.indexOf(ss.indexOf('/'),ss.indexOf("Location: http://")));
	                    System.out.println(index);
	                }
	                String ipp = AttachParameter.getIPAddress(getApplicationContext());

	                System.out.println(ipp);
	                content="���ݱ���IP�γs����";
	                mHandler.obtainMessage(change_word).sendToTarget();
	                //�����D,3G�L�k�ϥ�
	                try{
	                    AttachParameter.port= Integer.valueOf(map.get(ipp.substring(ipp.indexOf("in_ip=")+6,ipp.indexOf("&"))))+1;
	                    //att_parameter.port=5555;

	                }catch(Exception e){
	                    AttachParameter.port=9527;
	                }
	                String[] aliveIp = locup.locationupdate(Login.latest_cookie, ipp , AttachParameter.port);
	                messageProcess MsgSave = new messageProcess();
	                MsgSave.checkwlan(getContentResolver(), aliveIp[4]);
//					updateContent(Uri.parse("content://tab.list.d2d/user_data"), "retrievable","content is null");
	                MsgSave = null;
	                //��l�Ʈɶ�
	                User user = new User();
	                user.setservicetime("H=0&M=0");
	                user=null;
	            }

	            return loginreturn;
	        }//end doInBackground

	        // onPostExecute �|���� doInBackground ��return
	        protected void onPostExecute(String loginreturn[]) {
	            Dialog_for_login.dismiss();
	            mDevfinder = null;
	            if (loginreturn[0] == "true"){ // �p�G��server���^��
	                Boolean result =AttachParameter.chechsuccess(loginreturn[1]);
	                if (result){ // �Y�b���K�X���T ��U�@��

	                	listThread = new Thread(runnable);
	                    aliveThread = new Thread(alive_run);
	                    listThread.start();
	                    aliveThread.start();

	                    finishUpdateList=false;
	                    loginSuccess = 1;
	                    // ���n�X���Ϥ�
	                    btnLogin.setBackgroundResource(R.drawable.logout_btn);
	                    // �]�w���s���i�ϥ�
	                    btnReady.setEnabled(true);
	                    btnIn.setEnabled(true);
	                    btnFrie.setEnabled(true);
	                    btnEdit.setEnabled(true);
	                    // ���õ��U��
	                    //reg_bn.setBackgroundResource(R.drawable.dra_custom_btn);
	                    btnReg.setVisibility(View.INVISIBLE);
	                    retrieving = false;

	                    if (AttachParameter.priority == 0){
	                        name.setText("�`��");
	                    }else if (AttachParameter.priority == 1){
	                        name.setText("�q�O");
	                    }else if (AttachParameter.priority == 2){
	                        name.setText("�L���x");
	                    }


	                    Cursor change_remember = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_info"), new String[] { UserSchema._ID }, null, null, null);
	                    if (change_remember.getCount() > 0) {
	                        change_remember.moveToFirst();
	                        int id_this = 0;
	                        id_this = Integer.valueOf(change_remember.getString(0));
	                        ContentValues values = new ContentValues();
	                        if(ct.isChecked()){
	                            values.put(UserSchema._REMEMBER, "true");
	                            values.put(UserSchema._ACCOUNT, et.getText().toString());
	                            values.put(UserSchema._PASSWORD, et2.getText().toString());
	                        }else{
	                            values.put(UserSchema._REMEMBER, "false");
	                            values.put(UserSchema._ACCOUNT, "");
	                            values.put(UserSchema._PASSWORD, "");
	                        }
	                        String where = UserSchema._ID + " = " + id_this;
	                        getContentResolver().update(Uri.parse("content://tab.list.d2d/user_info"), values, where, null);

	                    }
	                    change_remember.close();
//						Cursor check_ffmpeg = getContentResolver().query(Uri.parse("content://tab.list.d2d/temp_ffmpeg"), null, null, null, null);
//						if(check_ffmpeg.getCount()>0){
//							tolen=check_ffmpeg.getCount();
	//
//						}
//						check_ffmpeg.close();
	                    String []prority=loginreturn[1].split("&");
	                    AttachParameter.priority=Integer.valueOf(prority[1]);//20160923�w�]�u���v�̰�
	                    if(AttachParameter.priority==0){
	                        Cursor check_missile =getContentResolver().query(Uri.parse("content://tab.list.d2d/missile_group"), null, null, null, null);
	                        int count=check_missile.getCount();
	                        check_missile.close();
	                        if(count==0){
	                            for(int i=0;i<4;i++){
	                                ContentValues values = new ContentValues();
	                                values.put(UserSchema._TITTLE, "S00"+(i+1));
	                                values.put(UserSchema._CONTENT, "���u"+(i+1)+"��");
	                                values.put(UserSchema._STATE, "live");
	                                getContentResolver().insert(Uri.parse("content://tab.list.d2d/missile_group"), values);
	                                values = null;
	                            }
	                        }
	                        else{
	                            String[] Form = { UserSchema._ID };
	                            Cursor change_path = getContentResolver().query(Uri.parse("content://tab.list.d2d/missile_group"), Form, "filepath is not null", null, null);
	                            if (change_path.getCount() > 0) {
	                                change_path.moveToFirst();
	                                for(int ia =0;ia<change_path.getCount();ia++){
	                                    int id_this = 0;
	                                    id_this = Integer.valueOf(change_path.getString(0));
	                                    ContentValues values = new ContentValues();
	                                    values.put(UserSchema._FILEPATH, "");
	                                    String where = UserSchema._ID + " = " + id_this;
	                                    getContentResolver().update(Uri.parse("content://tab.list.d2d/missile_group"), values, where, null);
	                                    change_path.moveToNext();
	                                }


	                            }
	                            change_path.close();
	                        }

	                    }
	                }else{
	                    //server���^���A���Oret=1;
	                    Pattern pattern_user = Pattern.compile("User name or Password is not correct");
	                    Matcher error_user = pattern_user.matcher(loginreturn[1]);
	                    Pattern pattern_activity = Pattern.compile("No Permission");
	                    Matcher error_activity = pattern_activity.matcher(loginreturn[1]);
	                    if(error_user.find()){
	                        final AlertDialog.Builder Dialog = new AlertDialog.Builder(LoginMissile.this); // Dialog
	                        Dialog.setTitle("��p!!");
	                        Dialog.setMessage("�z��J���b���αK�X���~�A�Э��s�T�{��A�i��n�J");
	                        Dialog.setIcon(android.R.drawable.ic_dialog_info);
	                        Dialog.setNegativeButton("�T�w", new DialogInterface.OnClickListener() { // ���Uabort
	                            // �Nthread����
	                            // ����progressbar
	                            // �]�w���s����U�ɵ����A�����o��Dialog�ä��_�o��thread
	                            @Override
	                            public void onClick(DialogInterface dialog, int which) {
	                                // ConnectHttp.cancel(ConnectCancel);

	                                // dialog.cancel();
	                            }
	                        });
	                        Dialog.show();
	                    }else if(error_activity.find()){
	                        final AlertDialog.Builder Dialog = new AlertDialog.Builder(LoginMissile.this); // Dialog
	                        Dialog.setTitle("��p!!");
	                        Dialog.setMessage("�z���b���|���i�����ҡA�Э��s����");
	                        Dialog.setIcon(android.R.drawable.ic_dialog_info);
	                        Dialog.setNegativeButton("�T�w", new DialogInterface.OnClickListener() { // ���Uabort
	                            // �Nthread����
	                            // ����progressbar
	                            // �]�w���s����U�ɵ����A�����o��Dialog�ä��_�o��thread
	                            @Override
	                            public void onClick(DialogInterface dialog, int which) {
	                                // ConnectHttp.cancel(ConnectCancel);

	                                // dialog.cancel();
	                            }
	                        });
	                        Dialog.show();
	                    }

	                }
	            } else {

	                if (loginreturn[2] == "false"){
	                // no response from http,ask user to wait or retry

	                    AlertDialog.Builder Dialog = new AlertDialog.Builder(LoginMissile.this); // Dialog
	                    Dialog.setTitle("ĵ�i");
	                    Dialog.setMessage("�������`,�Э��s�A��");
	                    Dialog.setIcon(android.R.drawable.ic_dialog_info);
	                    Dialog.setNeutralButton("����", new DialogInterface.OnClickListener() { // ���Uretry
	                        // �Nthread����
	                        // �A�]�@�ӷs��thread
	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {
	                            Thread.currentThread().interrupt();
	                            // ���s����
	                            new ConnectHttp().execute();
	                        }
	                    });
	                    Dialog.setNegativeButton("����", new DialogInterface.OnClickListener() { // ���Uabort
	                        // �Nthread����
	                        // ����progressbar
	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {
	                        }
	                    });
	                    Dialog.show();
	                }

	                else {
	                    AlertDialog.Builder Dialog = new AlertDialog.Builder(LoginMissile.this); // Dialog
	                    Dialog.setTitle("ĵ�i");
	                    Dialog.setMessage("�n�J����");
	                    Dialog.setIcon(android.R.drawable.ic_dialog_info);
	                    Dialog.setNeutralButton("����", new DialogInterface.OnClickListener() { // ���Uretry
	                        // �Nthread����
	                        // �A�]�@�ӷs��thread
	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {
	                            Thread.currentThread().interrupt();
	                            new ConnectHttp().execute();
	                        }
	                    });
	                }
	            }
	        }


	        @Override
	        public void onCancelled() {
	            // ����ϥ�task
	            new LoginMissile.ConnectHttp().cancel(true);
	            Dialog.create().dismiss();
	        }//end onCancelled
	    }//end ConnectHttp

	    private class getcontent extends AsyncTask<Void, Void, Void> {

	        @Override
	        protected void onPreExecute() {
	            // �}�Ҹ�ƶǰedialog
	            pdialog = ProgressDialog.show(LoginMissile.this, "�еy��", "���Ū����", true);
	            pdialog.show();
	        }

	        @Override
	        protected Void doInBackground(Void... params) {
	            aliveIp = locup.locationupdate(Login.latest_cookie,AttachParameter.getIPAddress(getApplicationContext()),AttachParameter.port);

	            if(aliveIp[0]!=null){

	                messageProcess MsgSave = new messageProcess();
	                MsgSave.checkwlan(getContentResolver(), aliveIp[4]);
	                updateContent(Uri.parse("content://tab.list.d2d/user_data"), "retrievable","content is null");
	                updateContent(Uri.parse("content://tab.list.d2d/user_reply"), "reply","filename is null");
	                MsgSave=null;
	                pdialog.dismiss();
	                // �n�[�W���~���P�_
	                Bundle bundle=new Bundle();
	                Intent intent = new Intent();
	                bundle.putString("location","0" );
	                // intent.setClass(writepage.this, browse.class);
	                intent.putExtras(bundle);
	                intent.setClass(LoginMissile.this, MainActivity.class);
	                // startActivityForResult(intent,0);
	                startActivity(intent);

	            }else{
	                pdialog.dismiss();
	                mHandler.obtainMessage(timeout).sendToTarget(); // �ǰe�n�D��slist���T����handler
	            }

	            return null;
	        }
	    }

	    private class startretrieve extends AsyncTask<Void, Void, String[]> {// implement
	        // thread
	        public String[] aliveIp;
	        public String checkfile = new String();
	        public String getFilename = null;
	        public String[] getSplit;
	        public String token;
	        public int id_this;
	        public String where;
	        String[] reretrieve = new String[5];
	        String[] reretrieve_file = new String[5];
	        String[] reretrieve_arg = new String[11];
	        String[] Form = { UserSchema._ID, UserSchema._RECEIVEID, UserSchema._SENDER};
	        Retrieve retrieve = new Retrieve();

	        protected void onPreExecute() {
	            final int notifyID = 0;
	            //final Notification notification = new Notification.Builder(getApplicationContext()).setSmallIcon(R.drawable.icon).setContentTitle("���e���D").setContentText("���e��r").setContentIntent(contentIntent).setAutoCancel(true).build(); // �إ߳q��
	            gNotMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	            final Notification notification = new Notification.Builder(getApplicationContext()).setSmallIcon(android.R.drawable.stat_sys_download).setContentTitle("�ɮפU����").setAutoCancel(false).setProgress(0, 50, false).build(); // �إ߳q��
	            gNotMgr.notify(notifyID, notification); // �o�e�q��
	        }
	        @Override
	        protected String[] doInBackground(Void... params) {
	            idle=false;
	            // String[] refile = new String[4];

	            aliveIp = locup.locationupdate(Login.latest_cookie, AttachParameter.getIPAddress(getApplicationContext()), AttachParameter.port);
	            messageProcess MsgSave = new messageProcess();
	            MsgSave.checkwlan(getContentResolver(), aliveIp[4]);
	            MsgSave = null;
	            // ���o�̪�IP
	            token = bdtoken;

	            retrieve.viewmod = "startview";
	            // �o�䪺DB�A�O�n��s�ثe�buser_data����²�T�����A�A�令�U���������A
	            // cursor(��ƪ�ӷ�,����������W��,����)
	            // ��������W�١Guser_data��ƪ�ID�ȡA����Gitemclick�I���ɩұo��token
	            updateState("download", "messagetoken='" + token + "'");
	            // ����request���ʧ@�A�ǤJtoken�hserver�A���o��token�ҫ������ɮ�id
	            reretrieve = retrieve.retrieve_req_for_d2d(AttachParameter.connect_ip, AttachParameter.connect_port,bdtoken);
	            reretrieve_arg[0]=reretrieve[0];
	            reretrieve_arg[1]=reretrieve[1];
	            reretrieve_arg[2]=reretrieve[2];
	            reretrieve_arg[3]=reretrieve[3];
	            reretrieve_arg[4]=reretrieve[4];

	            //reretrieve = retrieve.retrieve_req(bdtoken, "");
	            System.out.println("�^�_�����D���B "+reretrieve);
	            if (reretrieve[0] == "true"){ // retrieve_req=ret=0

	                //========20160905�[�J===========//
	                notify_msg="���b�����Ӧ۩�: "+ AttachParameter.connect_ip+"��"+retrieve.fileid[i].substring(retrieve.fileid[i].indexOf("/KM/")+4,retrieve.fileid[i].length());
	                ss=10;
	                mHandler.obtainMessage(SHOW_NOTIFY).sendToTarget();
	                //========20160905�[�J===========//

	                fileList.clear(); // ���NfileList�M�� �H�KŪ��W������
	                getSplit = reretrieve[1].split("&");
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
	                        //20160905�Ǫ��[�^(getContentResolver())
	                        reretrieve_file =retrieve.saveBinaryFile_for_d2d(token, j, getContentResolver()); // �����ln���ɮ�

	                        reretrieve_arg[5]=reretrieve_file[0];
	                        reretrieve_arg[6]=reretrieve_file[1];
	                        reretrieve_arg[7]=reretrieve_file[2];
	                        reretrieve_arg[8]=reretrieve_file[3];
	                        reretrieve_arg[9]=reretrieve_file[4];
	                        if(reretrieve_file[0].equalsIgnoreCase("true")){
//	                            notify_msg="�ثe�w�U������"+(j+1)+"/"+retrieve.fileid.length;
	                            notify_msg="�R�O���o";
	                            ss=3;
	                            mHandler.obtainMessage(SHOW_NOTIFY).sendToTarget();
	                            DLfilename = retrieve.filename;
	                            // �z��^�Ǫ�filename�APattern�O�A�n�z�諸���e�AMatcher�O�A�n�d���@�Ӧr�갵�z��
	                            boolean[] checktype = new boolean[AttachParameter.filetype];
	                            checktype = AttachParameter.checktype(DLfilename);
	                            System.out.println("�^�_�����D���B3 "+DLfilename);
	                            // �o��DB���ʧ@���A�����w���쪺file id�A�|���_�a�⦨�\��id�A�qDB���R��
	                            replaceSeq("messagetoken='" + token + "'", "retrive",reretrieve_file[1]);
	                            // �p�G�o���U�����O�v��
	                            if (checktype[AttachParameter.video]||checktype[AttachParameter.photo]||checktype[AttachParameter.music]) {
	                                // �N�ɦW�[�J��arraylist
	                                fileList.add(DLfilename);
	                            }
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


	            return reretrieve;
	        }

	        protected void onPostExecute(String[] notuse) {
	            String[]type;
	            String name=new String(),filename=new String();
	            updateState("", "messagetoken='" + token + "'");
	            if (reretrieve_arg[0] == "true") {
	                //retreive���\
	                if(reretrieve_arg[5]=="true"){
	                    /*
						 * cursor(��ƪ�ӷ�,����������W��,����)//��������W�١Guser_data��ƪ�ID�ȡA����
						 * �Gitemclick�I���ɩұo��token
						 *
						 * �]���w�g�U�����F�A�ҥHuser_state��download����
						 */
	                    if (fileList.size() == 0) { // �Y�ɮפ��Ovideo fileList=0

	                        getFilename = retrieve.filename;
	                        type =getFilename.split("\\.");//2016/06/30�s�W�ק�
	                        filename=getFilename;//2016/07/04�s�W�ק�
	                    } else { // �Y�ɮ׬Ovideo �h��&���ꦨ�@��A�ت��O�n���v���M��(�o�̪��M�椺�e�O�O�ɦW��_�Ӫ�)
	                        getFilename = "";
	                        filename=fileList.get(0);//2016/06/30�s�W�ק�
	                        type=filename.split("\\.");
	                        for (int i = 0; i < fileList.size(); i++)
	                            getFilename = getFilename + "&" + token+"_"+fileList.get(i).replace(".", "-_" + String.valueOf(i) + ".");
	                    }
	                    // �]�����U�����F�A�ҥH��sfilepath
	                    Cursor up_filepath = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), Form, "messagetoken='" + token + "'", null, null);
	                    ContentValues values = new ContentValues();
	                    if (up_filepath.getCount() > 0) {
	                        up_filepath.moveToFirst();
	                        values.put(UserSchema._FILEPATH, getFilename);
	                        int id_this = Integer.parseInt(up_filepath.getString(0));
	                        name = up_filepath.getString(2);
	                        //filename=up_filepath.getString(3);
	                        String where = UserSchema._ID + " = " + id_this;
	                        getContentResolver().update(Uri.parse("content://tab.list.d2d/user_data"), values, where, null);
	                    }
	                    up_filepath.close();
	                    
	                    //=========20161117�@�s�W========//
	                    values = new ContentValues();
	                    values.put(UserSchema._FILEPATH, getFilename);
	                    getContentResolver().insert(Uri.parse("content://tab.list.d2d/file_choice"), values);
	                    //=========20161117�@�s�W========//
	                    
	                    updateNotification("0", "wlan",bdtoken);
	                    bdtoken="";
	                    idle=true;

	                    //����Notification����A�ó]�w���ݩ�
	                    final int notifyID = 0;
	                    final int requestCode = notifyID;
	                    //final int flags = PendingIntent.FLAG_CANCEL_CURRENT;
	                    Intent notificationIntent = new Intent(getApplicationContext(), OpenFire.class);
	                    Bundle bundle = new Bundle();
	                    bundle.putString("contact", name);
	                    notificationIntent.putExtras(bundle);
	                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	                    PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), requestCode, notificationIntent, 0);
	                    //final Notification notification = new Notification.Builder(getApplicationContext()).setSmallIcon(R.drawable.icon).setContentTitle("���e���D").setContentText("���e��r").setContentIntent(contentIntent).setAutoCancel(true).build(); // �إ߳q��
	                    gNotMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	                    final Notification notification = new Notification.Builder(getApplicationContext()).setSmallIcon(android.R.drawable.stat_sys_download_done).setContentTitle( filename+"."+type[1]).setContentText("���\�����Ӧ�"+name+"���ɮ�").setDefaults(Notification.DEFAULT_SOUND).setContentIntent(contentIntent).setAutoCancel(true).build(); // �إ߳q��
	                    gNotMgr.notify(notifyID, notification); // �o�e�q��

	                    notify_msg="���\�U���Ӧ�"+name+"��"+filename+"."+type[1];
	                    ss=5;
	                    mHandler.obtainMessage(SHOW_NOTIFY).sendToTarget();
	                }
	                else if(reretrieve_arg[5]=="false"){
	                    //retreive file ���� (�o��200�H�~��code)
	                    //�nŪbody
	                    Pattern pattern_msg = Pattern.compile("message does not exist"); // check file type
	                    Matcher matcher_msg = pattern_msg.matcher(reretrieve_arg[6]);
	                    Pattern pattern_file = Pattern.compile("file is not ready"); // check file type
	                    Matcher matcher_file = pattern_file.matcher(reretrieve_arg[6]);
	                    Pattern pattern_file_II = Pattern.compile("file not found"); // check file type
	                    Matcher matcher_file_II = pattern_file_II.matcher(reretrieve_arg[6]);
	                    if(matcher_msg.find()){
	                        notify_msg="�T���ä��s�bserver�W�A�ЧR�����T��";
	                    }else if(matcher_file.find()){
	                        notify_msg="����ɮת��o�Ϳ��~�A�ЧR�����T��";
	                    }else if(matcher_file_II.find()){
	                        notify_msg="���ɮקǸ����s�b�A�ЧR�����T��";
	                    }
	                    final int notifyID = 0;
	                    gNotMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	                    final Notification notification = new Notification.Builder(getApplicationContext()).setSmallIcon(android.R.drawable.stat_sys_download).setContentTitle("���~").setContentText(notify_msg).setAutoCancel(false).setProgress(0, 50, false).build(); // �إ߳q��
	                    gNotMgr.notify(notifyID, notification); // �o�e�q��

	                    ss=3;
	                    mHandler.obtainMessage(SHOW_NOTIFY).sendToTarget();
	                }else{

	                    //server error
	                    if(reretrieve_arg[7]!=null && reretrieve_arg[7].equalsIgnoreCase("true")){
	                        notify_msg="ĵ�i�A���A�������o�Ϳ��~�A�мȰ��ϥ�MD2MD";
	                    }else if(reretrieve_arg[8]!=null && reretrieve_arg[8].equalsIgnoreCase("true")){
	                        notify_msg="ĵ�i�A�s�����ѡA�мȰ��ϥ�MD2MD";
	                    }else if (reretrieve_arg[9]!=null && reretrieve_arg[9].equalsIgnoreCase("true")){
	                        notify_msg="ĵ�i�A���������~�A�мȰ��ϥ�MD2MD";
	                    }else if (reretrieve_arg[10]!=null && reretrieve_arg[10].equalsIgnoreCase("true")){
	                        notify_msg="ĵ�i�A�����x�s�Ŷ��o�Ϳ��~�A�мȰ��ϥ�MD2MD";
	                    }
	                    final int notifyID = 0;
	                    gNotMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	                    final Notification notification = new Notification.Builder(getApplicationContext()).setSmallIcon(android.R.drawable.stat_notify_error).setContentTitle("ĵ�i").setContentText(notify_msg).setAutoCancel(false).setProgress(0, 50, false).build(); // �إ߳q��
	                    gNotMgr.notify(notifyID, notification); // �o�e�q��
	                    ss=3;
	                    mHandler.obtainMessage(SHOW_NOTIFY).sendToTarget();
	                }
	            }else if(reretrieve_arg[0]=="false"){
	                //retreive���� (�o��200�H�~��code)
	                //�nŪbody
	                Pattern pattern_msg = Pattern.compile("message does not exist"); // check file type
	                Matcher matcher_msg = pattern_msg.matcher(reretrieve_arg[1]);
	                Pattern pattern_msg_II = Pattern.compile("file is not ready"); // check file type
	                Matcher matcher_msg_II = pattern_msg_II.matcher(reretrieve_arg[1]);
	                if(matcher_msg.find()){
	                    notify_msg="�T���ä��s�bserver�W�A�ЧR�����T��";

	                }else if(matcher_msg_II.find()){
	                    notify_msg="���~���T���s���A�ЧR�����T��";
	                }
	                final int notifyID = 0;
	                gNotMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	                final Notification notification = new Notification.Builder(getApplicationContext()).setSmallIcon(android.R.drawable.stat_sys_download).setContentTitle("���~").setContentText(notify_msg).setAutoCancel(false).setProgress(0, 50, false).build(); // �إ߳q��
	                gNotMgr.notify(notifyID, notification); // �o�e�q��
	                ss=3;

	                mHandler.obtainMessage(SHOW_NOTIFY).sendToTarget();
	            }else{
	                //server error
	                if(reretrieve_arg[2]!=null && reretrieve_arg[2].equalsIgnoreCase("true")){
	                    notify_msg="ĵ�i�A���A�������o�Ϳ��~�A�мȰ��ϥ�MD2MD";

	                }else if(reretrieve_arg[3]!=null && reretrieve_arg[3].equalsIgnoreCase("true")){
	                    notify_msg="ĵ�i�A�s�����ѡA�мȰ��ϥ�MD2MD";

	                }else if (reretrieve_arg[4]!=null && reretrieve_arg[4].equalsIgnoreCase("true")){
	                    notify_msg="ĵ�i�A���������~�A�мȰ��ϥ�MD2MD";

	                }
	                final int notifyID = 0;
	                gNotMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	                final Notification notification = new Notification.Builder(getApplicationContext()).setSmallIcon(android.R.drawable.stat_sys_download).setContentTitle("ĵ�i").setContentText(notify_msg).setAutoCancel(false).setProgress(0, 50, false).build(); // �إ߳q��
	                gNotMgr.notify(notifyID, notification); // �o�e�q��

	                ss=3;
	                mHandler.obtainMessage(SHOW_NOTIFY).sendToTarget();
	            }

	        }
	    } // reretrieve[1]=="true"

	    // ��s��Ʈw���A
	    private void updateState(String state, String[] Form, String conditional) {
	        Cursor change_state = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), Form, conditional, null, null);
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
	    }//end updateState

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
	    }//end of updateState

	    // ��s��Ʈw���A
	    public void updateContent(Uri location, String mod, String condition) {
	        int id_this;
	        String where, content, token, tittle;
	        String[] Form = { UserSchema._ID, UserSchema._MESSAGETOKEN };
	        String[] reretrieve = new String[5];

	        Cursor up_content = getContentResolver().query(location, Form, condition, null, null);
	        if (up_content.getCount() > 0) {
	            up_content.moveToFirst();
	            Retrieve retreive =new Retrieve();
	            for (int i = 0; i < up_content.getCount(); i++) {
	                token = up_content.getString(1);
	                reretrieve = retreive.retrieve_req(token, mod);
	                if (reretrieve[0].equals("true")) {
	                    if(retreive.retrieveFileCount.length > 3){
	                        content = reretrieve[1].substring(reretrieve[1].indexOf("content=") + 8, reretrieve[1].indexOf("&file"));
	                    }else{
	                        content = reretrieve[1].substring(reretrieve[1].indexOf("content=") + 8, reretrieve[1].length()-1);
	                    }
	                    id_this = Integer.valueOf(up_content.getString(0));
	                    ContentValues values = new ContentValues();
	                    if (mod.equals("retrievable")) {
	                        tittle = reretrieve[1].substring(reretrieve[1].indexOf("subject=") + 8, reretrieve[1].indexOf("&content="));
	                        values.put(UserSchema._CONTENT, content);
	                        values.put(UserSchema._TITTLE, tittle);
	                        values.put(UserSchema._USESTATUS, "");
	                        values.put(UserSchema._FILEPATH, "");
	                    }else{
	                        values.put(UserSchema._FILENAME, content);
	                    }

	                    where = UserSchema._ID + " = " + id_this;
	                    getContentResolver().update(location, values, where, null);

	                }
	                up_content.moveToNext();
	            }

	        }
	        up_content.close();
	    }

	    private void updateNotification(String state, String type, String token) {
	        Cursor noti_cursor = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), new String[] { UserSchema._ID, UserSchema._NOTIFICATION }, "messagetoken='" + token + "'", null, null);
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
	    }//end of updateNotification

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
	            if (mod.equals("again")) {
	                String check_path = check_fileid_cursor.getString(2) + "&" + name;
	                values.put(UserSchema._FILEPATH, check_path);
	            }
	            System.out.println(check_id);
	            values.put(UserSchema._RECEIVEID, check_id);
	            id_this = Integer.parseInt(check_fileid_cursor.getString(0));
	            where = UserSchema._ID + " = " + id_this;
	            getContentResolver().update(Uri.parse("content://tab.list.d2d/user_data"), values, where, null);
	        }
	        check_fileid_cursor.close();
	    }//end of replaceSeq

	    private ServiceConnection serviceConnection = new ServiceConnection() {

	        public void onServiceConnected(ComponentName className, IBinder service) {
	            upnpService = (AndroidUpnpService) service;

	            LocalService<SwitchPower> switchPowerService = getSwitchPowerService();

	            // Register the device when this activity binds to the service for the first time
	            if (switchPowerService == null) {
	                try {
	                    LocalDevice binaryLightDevice = createDevice();

	                    Toast.makeText(LoginMissile.this, R.string.registering_demo_device, Toast.LENGTH_SHORT).show();
	                    upnpService.getRegistry().addDevice(binaryLightDevice);

	                    switchPowerService = getSwitchPowerService();

	                } catch (Exception ex) {
	                    log.log(Level.SEVERE, "Creating demo device failed", ex);
	                    Toast.makeText(LoginMissile.this, R.string.create_demo_failed, Toast.LENGTH_SHORT).show();
	                    return;
	                }
	            }

	            // Obtain the state of the power switch and update the UI
	            setLightbulb(switchPowerService.getManager().getImplementation().getStatus());

	            // Start monitoring the power switch
	            switchPowerService.getManager().getImplementation().getPropertyChangeSupport()
	                    .addPropertyChangeListener(LoginMissile.this);

	        }

	        public void onServiceDisconnected(ComponentName className) {
	            upnpService = null;
	        }
	    };//end ServiceConnection

	    protected LocalService<SwitchPower> getSwitchPowerService() {
	        if (upnpService == null)
	            return null;

	        LocalDevice binaryLightDevice;
	        if ((binaryLightDevice = upnpService.getRegistry().getLocalDevice(udn, true)) == null)
	            return null;

	        return (LocalService<SwitchPower>)
	                binaryLightDevice.findService(new UDAServiceType("MASP", 1));
	    }

	    protected LocalDevice createDevice()
	            throws ValidationException, LocalServiceBindingException {

	        DeviceType type =
	                new UDADeviceType("BinaryLight", 1);

	        DeviceDetails details =
	                new DeviceDetails(
	                        "MASP",
	                        new ManufacturerDetails("ACME"),
	                        new ModelDetails("1705A_MASP", "TEST UPNP", "v1")
	                );

	        LocalService service =
	                new AnnotationLocalServiceBinder().read(SwitchPower.class);

	        service.setManager(
	                new DefaultServiceManager<SwitchPower>(service, SwitchPower.class)
	        );

	        return new LocalDevice(
	                new DeviceIdentity(udn),
	                type,
	                details,
	                service
	        );
	    }//end LocalDevice

	    protected void setLightbulb(final boolean on) {
	        runOnUiThread(new Runnable() {
	            public void run() {
	            }
	        });
	    }//end setLightbulb

	    public void checkSMS() {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); // �Ndate
	        String tempdate;
	        // uri�O�s������DB���@�Ӥ覡�A�o�̭n�h��²�T��Ʈw��������
	        Uri SMS_INBOX = Uri.parse("content://sms/inbox");
	        Cursor new_sms_cursor = getContentResolver().query(SMS_INBOX, new String[] { "address", "date", "body" }, "address='0903410141' or address='+886903410141'", null, null);

	        // �ˬd�O�_���s��sms
	        if (new_sms_cursor.getCount() > 0) {
	            new_sms_cursor.moveToFirst();
	            for (int i = 0; i < new_sms_cursor.getCount(); i++) {
	                // decode��@��H�ݱo���o����
	                Date d = new Date(Long.parseLong(new_sms_cursor.getString(new_sms_cursor.getColumnIndex("date"))));
	                tempdate = dateFormat.format(d);

	                sms = new_sms_cursor.getString(new_sms_cursor.getColumnIndex("body"));
	                messageProcess MsgSave = new messageProcess();
	                MsgSave.insertdata(getContentResolver(), sms, tempdate);
	                MsgSave = null;
	                // �۩w�q���禡
	                new_sms_cursor.moveToNext();
	            }// end for
	        }// end if
	        new_sms_cursor.close();
	    }//end of checkSMS

	    private Handler mHandler = new Handler(){
	        public void handleMessage(Message msg){
	            switch(msg.what){

	                case UPDATE_LIST_MES:
	                    checkSMS();
	                    break;

	                case UPDATE_FFMPEG:
	                    if(hasRead<=tolen){
	                        re_ffmpeg_dialog.setProgress(hasRead);
	                    }else{
	                        re_ffmpeg_dialog.dismiss();
	                    }
	                    break;

	                case change_word:
	                    Dialog_for_login.setMessage(content);
	                    break;

	                case timeout:
	                    AlertDialog.Builder Dialog0 = new AlertDialog.Builder(LoginMissile.this); // Dialog
	                    Dialog0.setTitle("�s�u�O��");
	                    Dialog0.setMessage("�еy�Ԧb��");
	                    Dialog0.setIcon(android.R.drawable.ic_dialog_info);
	                    Dialog0.setNeutralButton("�T�w", new DialogInterface.OnClickListener() {
	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {

	                        }
	                    });
	                    Dialog0.show();
	                    break;
	                case SHOW_NOTIFY:
	                    Toast.makeText(getApplicationContext(),notify_msg, ss).show();
	                    notify_msg="";
	                    ss=0;
	                    break;
	            }
	        }
	    };

	    public boolean onKeyDown(int keyCode, KeyEvent event) {//������^��
	        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	            ConfirmExit();//����^��A�h����h�X�T�{
	            return true;
	        }
	        return super.onKeyDown(keyCode, event);
	    }

	    @Override
	    protected void onDestroy() {
	        super.onDestroy();

	        AttachParameter.selfid="";
	        finishUpdateList = true;

	        LocalService<SwitchPower> switchPowerService = getSwitchPowerService();
	        if (switchPowerService != null)
	            switchPowerService.getManager().getImplementation().getPropertyChangeSupport()
	                    .removePropertyChangeListener(this);
	        getApplicationContext().unbindService(serviceConnection);

	    }

	    public void ConfirmExit(){//�h�X�T�{
	        AlertDialog.Builder DialogPreDl = new AlertDialog.Builder(this); // Dialog
	        DialogPreDl.setTitle("");
	        DialogPreDl.setMessage("�T�w�n�h�X?");
	        DialogPreDl.setIcon(android.R.drawable.ic_dialog_info);

	        DialogPreDl.setPositiveButton("�O", new DialogInterface.OnClickListener() { // �������ɮ�
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                idle=true;
	                ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	                NetworkInfo info = CM.getActiveNetworkInfo();
	                if (info == null || !info.isAvailable()) {
	                    System.out.println("�ثe�S��������");
	                }else{
	                    Logout logout = new Logout();
	                    logout.logout_start();
	                }

	                LoginMissile.this.finish();
	            }//���ݤU�� end
	        });

	        DialogPreDl.setNeutralButton("�_", new DialogInterface.OnClickListener() { // �w���ɮ�
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	            }
	        });

	        DialogPreDl.show();
	    }

	    // �إߤu�@�M��A�C30���ˬd
	    Runnable runnable = new Runnable() {
	        @Override
	        public void run() {
	            while (!finishUpdateList) {
	                try {
	                    Thread.sleep(5000);
	                } catch (InterruptedException e) {
	                    //Thread.currentThread().interrupt();
	                    Log.i("update list thread", "Thread.currentThread().isInterrupted()");
	                }
//						if (!retrieving){
//							mHandler.obtainMessage(UPDATE_LIST_MES).sendToTarget(); // �ǰe�n�D��slist���T����handler
//						}
	                ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	                NetworkInfo info = CM.getActiveNetworkInfo();
	                if (info == null || !info.isAvailable()) {
	                    System.out.println("�ثe�S��������");
	                }else{
	                    mHandler.obtainMessage(UPDATE_LIST_MES).sendToTarget(); // �ǰe�n�D��slist���T����handler
	                    String[] aliveIp = locup.locationupdate(Login.latest_cookie, AttachParameter.getIPAddress(getApplicationContext()), AttachParameter.port);
	                    if (aliveIp.length>0&&aliveIp[1].equalsIgnoreCase("true")){
	                        messageProcess MsgSave = new messageProcess();
	                        MsgSave.checkwlan(getContentResolver(), aliveIp[4]);
	                        updateContent(Uri.parse("content://tab.list.d2d/user_data"), "retrievable","content is null");
	                        updateContent(Uri.parse("content://tab.list.d2d/user_reply"), "reply","filename is null");
	                        MsgSave=null;
	                        if( aliveIp[4].length()>20){
	                            Pattern pattern_content = Pattern.compile("content");
	                            Matcher matcher_content = pattern_content.matcher(aliveIp[4]);
	                            Pattern pattern_d2d = Pattern.compile("d2d");
	                            Matcher matcher_d2d = pattern_d2d.matcher(aliveIp[4]);
	                            if(matcher_content.find()){
	                                final int notifyID = 2;
	                                final int requestCode = notifyID;

	                                //final int flags = PendingIntent.FLAG_CANCEL_CURRENT;
	                                Bundle bundle=new Bundle();
	                                bundle.putString("location","0" );
	                                // intent.setClass(writepage.this, browse.class);

	                                Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
	                                notificationIntent.putExtras(bundle);
	                                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	                                PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), requestCode, notificationIntent, 0);
	                                //final Notification notification = new Notification.Builder(getApplicationContext()).setSmallIcon(R.drawable.icon).setContentTitle("���e���D").setContentText("���e��r").setContentIntent(contentIntent).setAutoCancel(true).build(); // �إ߳q��
	                                gNotMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	                                final Notification notification = new Notification.Builder(getApplicationContext()).setSmallIcon(R.drawable.m2m_ver01).setContentTitle("�A���@�h�s�T��").setContentText("���H�H�H���A�F!�Ч֨�Ū��!").setDefaults(Notification.DEFAULT_SOUND).setContentIntent(contentIntent).setAutoCancel(true).build(); // �إ߳q��
	                                gNotMgr.notify(notifyID, notification); // �o�e�q��
	                                notify_msg="�A���@�h�s�T���A�֬ݬO��";
	                                ss=1;
	                                mHandler.obtainMessage(SHOW_NOTIFY).sendToTarget();
	                            }
	                            else if(matcher_d2d.find()){
	                                final int notifyID = 2;
	                                final int requestCode = notifyID;
	                                //final int flags = PendingIntent.FLAG_CANCEL_CURRENT;
	                                Bundle bundle=new Bundle();
	                                bundle.putString("location","0" );
	                                // intent.setClass(writepage.this, browse.class);

	                                Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
	                                notificationIntent.putExtras(bundle);
	                                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	                                PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), requestCode, notificationIntent, 0);
	                                //final Notification notification = new Notification.Builder(getApplicationContext()).setSmallIcon(R.drawable.icon).setContentTitle("���e���D").setContentText("���e��r").setContentIntent(contentIntent).setAutoCancel(true).build(); // �إ߳q��
	                                gNotMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	                                final Notification notification = new Notification.Builder(getApplicationContext()).setSmallIcon(R.drawable.m2m_ver01).setContentTitle("�T�{�R�O�����n�D").setContentText("�}��server").setDefaults(Notification.DEFAULT_SOUND).setContentIntent(contentIntent).setAutoCancel(true).build(); // �إ߳q��
	                                gNotMgr.notify(notifyID, notification); // �o�e�q��
	                                notify_msg="���H�n�D�A�}��server�A���֫e���s��";
	                                ss=2;
	                                mHandler.obtainMessage(SHOW_NOTIFY).sendToTarget();

	                            }

	                        }
	                        aliveIp=null;
	                    }
	                }
	                info=null;
	                CM=null;
	            }
	        }
	    };//end of runnable
	    // �إߤu�@�M��A�C10���ˬd
	    Runnable alive_run = new Runnable() {
	        @Override
	        public void run() {
	            String self_id,file_where,token=new String(),d2d_id;
	            final String[] check_down = {UserSchema._MESSAGETOKEN, UserSchema._SENDER, UserSchema._TYPE};
	            final String[] check_up = {UserSchema._MESSAGETOKEN, UserSchema._SENDER, UserSchema._ID, UserSchema._SENDER, UserSchema._SELFID};
	            final String[] id_up = {UserSchema._MESSAGETOKEN, UserSchema._SELFID};
	            final String[] temp_up = {UserSchema._ID, UserSchema._SELFID};
	            while (!finishUpdateList) {
	                try {
	                    Thread.sleep(5000);
	                } catch (InterruptedException e) {
	                    //Thread.currentThread().interrupt();

	                }
	                ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	                NetworkInfo info = CM.getActiveNetworkInfo();
	                if (info == null || !info.isAvailable()) {
	                    System.out.println("�ثe�S��������");
	                }else{
	                    //================20160905�[�^�Ǫ�����==========//
	                    Cursor down = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), new String[]{UserSchema._NOTIFICATION, UserSchema._MESSAGETOKEN, UserSchema._USESTATUS}, null, null, null);
	                    if(down.getCount()>0){
	                        down.moveToFirst();
	                        for(int i=0;i<down.getCount();i++){
	                            System.out.println("TEST_1 = "+down.getString(0));
	                            System.out.println("TEST_12 = "+down.getString(1));
	                            System.out.println("TEST_13 =" +down.getString(2));
	                            down.moveToNext();
	                        }
	                    }down.close();
	                    //================20160905�[�^�Ǫ�����==========//

	                    //20160905�[�^userstatus=''"
	                    Cursor down_cursor = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), check_down, "(notification = '1' or notification = '2') and userstatus=''", null, null);
	                    if(down_cursor.getCount()>0){
	                        down_cursor.moveToFirst();
	                        for (int i = 0; i < down_cursor.getCount(); i++) {
	                            while(!idle){
	                                try {
	                                    Thread.sleep(2000);
	                                } catch (InterruptedException e) {
	                                    //Thread.currentThread().interrupt();
	                                }
	                            }
	                            if(bdtoken.equalsIgnoreCase(down_cursor.getString(0))){
	                                //do noting
	                            }
	                            else{
	                                idle=false;
	                                bdtoken =down_cursor.getString(0);
	                                bdsender =down_cursor.getString(1);
	                                bdtype =down_cursor.getString(2);
	                                if(bdtype.equalsIgnoreCase("wlan")||bdtype.equalsIgnoreCase("sms")){
	                                    Retrieve retrieve = new Retrieve();
	                                    bdreretrieve = retrieve.check_file(bdtoken);
	                                    Boolean res= AttachParameter.chechsuccess(bdreretrieve);
	                                    if (res) {
	                                        updateNotification("3", bdtype,bdtoken);
	                                        new startretrieve().execute();
	                                    }
	                                    else{
	                                        idle=true;
	                                        bdtoken="";
	                                    }
	                                    retrieve =null;
	                                }else if(bdtype.equalsIgnoreCase("d2d")){
	                                    User user = new User();
	                                    String[] res =user.getservicetime(bdsender);

	                                    if(res[1].equalsIgnoreCase("true")){
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
	                                        updateNotification("3", "d2d",bdtoken);
	                                        new startretrieve().execute();

	                                    }else{
	                                        idle=true;
	                                        bdtoken="";
	                                    }
	                                    user = null;
	                                }
	                                down_cursor.moveToNext();

	                            }

	                        }

	                    }
	                    down_cursor.close();

	                    //d2d�M��
	                    Cursor get_d2d_id = getContentResolver().query(Uri.parse("content://tab.list.d2d/temp_content"), id_up,"finish ='yes'", null, null);
	                    //�ˬd���ɮ׬O�_���Χ���
	                    if(get_d2d_id.getCount()>0){
	                        get_d2d_id.moveToFirst();
	                        for(int i=0;i<get_d2d_id.getCount();i++){
	                            token=get_d2d_id.getString(0);
	                            d2d_id=get_d2d_id.getString(1);
	                            //��覬�T�T���n�D�ɮסA���o�{�o�S��token�A�N��token�S����A���s�o�e�n�D���^token
	                            if(token == null){
	                                //����republish�A�n�Dtoken,����ϥΫD�P�B
	                            }else{
	                                //token�s�b�A�]���Χ���
	                                Cursor file_token = getContentResolver().query(Uri.parse("content://tab.list.d2d/temp_file"), temp_up, "selfid='"+d2d_id +"' and messagetoken is null", null, null);
	                                //�ˬdtemp_file�����ɮ׬O�_�]��s�Wtoken
	                                if(file_token.getCount()>0){
	                                    //�i��token��s
	                                    ContentValues values = new ContentValues();
	                                    file_token.moveToFirst();
	                                    values.put(UserSchema._MESSAGETOKEN, token);
	                                    for (int a = 0; a < file_token.getCount(); a++) {
	                                        int id_this = Integer.parseInt(file_token.getString(0));
	                                        file_where = UserSchema._ID + " = " + id_this;
	                                        getContentResolver().update(Uri.parse("content://tab.list.d2d/temp_file"), values, file_where, null);
	                                        file_token.moveToNext();
	                                    }

	                                }else{
	                                    //temp_file����token�w�g������s
	                                }
	                                file_token.close();
	                                //��sreply��ready

	                            }
	                            get_d2d_id.moveToNext();
	                        }

	                    }else{
	                        //�ɮ��٨S���Φn�A���ݭI���۰ʤ���
	                    }
	                    get_d2d_id.close();

	                    Cursor ch_id = getContentResolver().query(Uri.parse("content://tab.list.d2d/user_reply"), check_up, "ready is null", null, null);
	                    //�o�{���٨SREADY����ơA�N��ӵ��٤���W�ǡA�}�l�v�@�ˬd��]
	                    if(ch_id.getCount()>0){
	                        Log.i("LOGININPUT", "�}�l�P�_");
	                        ch_id.moveToFirst();
	                        for(int i =0;i<ch_id.getCount();i++){
	                            self_id=ch_id.getString(4);
	                            Cursor get_id = getContentResolver().query(Uri.parse("content://tab.list.d2d/temp_content"), id_up, "selfid='"+self_id +"' and finish ='yes'", null, null);
	                            //�ˬd���ɮ׬O�_���Χ���
	                            if(get_id.getCount()>0){
	                                get_id.moveToFirst();
	                                token=get_id.getString(0);
	                                //��覬�T�T���n�D�ɮסA���o�{�o�S��token�A�N��token�S����A���s�o�e�n�D���^token
	                                if(token.equals("")){
	                                    //����republish�A�n�Dtoken,����ϥΫD�P�B
	                                }else{
	                                    //token�s�b�A�]���Χ���
	                                    Cursor file_token = getContentResolver().query(Uri.parse("content://tab.list.d2d/temp_file"), temp_up, "selfid='"+self_id +"' and messagetoken is null", null, null);
	                                    //�ˬdtemp_file�����ɮ׬O�_�]��s�Wtoken
	                                    if(file_token.getCount()>0){
	                                        //�i��token��s
	                                        ContentValues values = new ContentValues();
	                                        file_token.moveToFirst();
	                                        values.put(UserSchema._MESSAGETOKEN, token);
	                                        for (int a = 0; a < file_token.getCount(); a++) {
	                                            int id_this = Integer.parseInt(file_token.getString(0));
	                                            file_where = UserSchema._ID + " = " + id_this;
	                                            getContentResolver().update(Uri.parse("content://tab.list.d2d/temp_file"), values, file_where, null);
	                                            file_token.moveToNext();
	                                        }

	                                    }else{
	                                        //temp_file����token�w�g������s
	                                    }
	                                    file_token.close();
	                                    //��sreply��ready
	                                    ContentValues values = new ContentValues();
	                                    values.put(UserSchema._READY, "yes");
	                                    int id_this = Integer.parseInt(ch_id.getString(2));
	                                    file_where = UserSchema._ID + " = " + id_this;
	                                    getContentResolver().update(Uri.parse("content://tab.list.d2d/user_reply"), values, file_where, null);
	                                }

	                            }else{
	                                //�ɮ��٨S���Φn�A���ݭI���۰ʤ���
	                            }
	                            get_id.close();

	                            ch_id.moveToNext();
	                        }
	                        Log.i("LOGININPUT", "�����P�_");
	                    }else{
	                        //�S���o�{ready���Ū��A�N��reply���ˬd�L�F
	                    }
	                    ch_id.close();
	                }
	                info=null;
	                CM=null;

	            }
	        }
	    };//end of alive_run
	
}
