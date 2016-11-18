package kissmediad2d.android;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import database.storage.messageProcess;
import login.submit1.Login;
import login.submit1.Logout;
import login.submit1.Retrieve;
import login.submit1.User;
import login.submit1.locupdate;
import net.sbbi.upnp.messages.UPNPResponseException;
import softwareinclude.ro.portforwardandroid.asyncTasks.WebServerPluginInfo;
import softwareinclude.ro.portforwardandroid.asyncTasks.openserver;
import softwareinclude.ro.portforwardandroid.network.UPnPPortMapper;
import softwareinclude.ro.portforwardandroid.util.ApplicationConstants;
import tab.list.AttachParameter;
import tab.list.FileContentProvider;
import tab.list.FileContentProvider.UserSchema;

class FiringRoomInfo {

    public String cookie = new String();
    private String sender, title, token, filesize, date, msg;

    FiringRoomInfo() {
        filesize = new String();
        sender = new String();
        title = new String();
        token = new String();
        date = new String();
        msg = new String();
    }

    void setDate(String arg) {
        date = arg;
    }

    void setFilesize(String arg) {
        filesize = arg;
    }

    void setSender(String arg) {
        sender = arg;
    }

    void setContent(String arg) {
        title = arg;
    }

    void setToken(String arg) {
        token = arg;
    }

    void setmsg(String arg) {
        msg = arg;
    }

    String getDate() {
        return date;
    }

    String getSender() {
        return sender;
    }

    String getContent() {
        return title;
    }

    String getToken() {
        return token;
    }

    String getmsg() {
        return msg;
    }

    String getFilesize() {
        return filesize;
    }
}

/**
 * Notification �O���o��ذT���A�@�O�q�����ɮץi�U���A�G�O�q���бN�ɮפW�� �n�D�W�Ǫ��ɮ׷|�b�k�W���X�{reply���r��
 * �I����|��Ū�����e�A�M��|����recivelist
 */
public class FiringRoom extends Fragment implements OnItemClickListener{
	
	  public String subject = null, content = null, msg = null;
	    public String sms = null;
	    public int listid = -1;
	    String[] Form = { UserSchema._ID, UserSchema._SENDER, UserSchema._CONTENT, UserSchema._MESSAGETOKEN, UserSchema._FILESIZE, UserSchema._DATE };
	    String[] reply = { UserSchema._ID, UserSchema._SENDER, UserSchema._FILENAME, UserSchema._MESSAGETOKEN, UserSchema._DATE, UserSchema._MSG };
	    String H="1";
	    private NumberPicker timePicker;
	    List<FiringRoomInfo> getinfo = new ArrayList<FiringRoomInfo>();
	    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	    SimpleAdapter adapter;
	    ProgressDialog pdialog = null;
	    locupdate locup =new locupdate();
	    ListView Audiolist;
	    private static final int UPDATE_LIST_MES = 0;
	
	
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
	        final View view = inflater.inflate(R.layout.blist, container, false);
	        ((Activity) getActivity()).getActionBar().setTitle("�t�γq��");

	        Audiolist = (ListView) view.findViewById(R.id.PhoneVideoList);
	        adapter = new SimpleAdapter(getActivity(), getData(), R.layout.notification, new String[] { "notititle", "notiinfo", "notiimg", "notidate", "notmsg" }, new int[] { R.id.notititle, R.id.notiinfo, R.id.notiimg, R.id.notidate, R.id.msg });

	        Audiolist.setTextFilterEnabled(true);
	        if(list.isEmpty()){
	            Audiolist.setBackgroundResource(R.drawable.worldbackground3);
	        }else{
	            Audiolist.setBackgroundResource(R.drawable.worldbackground3);
	        }

	        Audiolist.setCacheColorHint(Color.TRANSPARENT);
	        Audiolist.setAdapter(adapter);
	        Audiolist.setOnItemClickListener(this);
	        return view;
	    }//end of onCreateView
	   
	    public boolean onOptionsItemSelected(MenuItem item) {
	        Intent intent = new Intent();
	        // Handle action bar item clicks here. The action bar will
	        // automatically handle clicks on the Home/Up button, so long
	        // as you specify a parent activity in AndroidManifest.xml.
	        int id = item.getItemId();
	        ConnectivityManager CM = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo info = CM.getActiveNetworkInfo();
	        switch (id){
	            case R.id.action_settings:
	                // intent.setClass(writepage.this, browse.class);
	                intent.setClass(getActivity(), edit.class);
	                // startActivityForResult(intent,0);
	                startActivity(intent);
	                break;
	            case R.id.logout:
	                FileContentProvider test = new FileContentProvider();
	                test.del_table(Uri.parse("content://tab.list.d2d/user_info"));
	                if (info == null || !info.isAvailable()) {

	                }else{
	                    Logout logout = new Logout();
	                    logout.logout_start();
	                }
	                // intent.setClass(writepage.this, browse.class);
	                intent.setClass(getActivity(), LoginMissile.class);
	                // startActivityForResult(intent,0);
	                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	                startActivity(intent);
	                getActivity().finish();
	                break;
	            case R.id.reload:
	                if (info == null || !info.isAvailable()) {
	                    Toast.makeText(getActivity(), "�ثe�S��������!�ҥH�L�k��s", Toast.LENGTH_LONG).show();
	                }else{
	                    new getContent().execute();
	                }

	                break;
	           
	        }
	        info=null;
	        CM=null;
	        return true;
	    }

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        // TODO Auto-generated method stub
	        super.onCreate(savedInstanceState);
	        setHasOptionsMenu(true);
	    }//end of onCreate

	    private Handler mHandler = new Handler() {
	        // ����k�bui�u�{�B��
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	                case UPDATE_LIST_MES:
	                    onResume();
	                    break;
	            }
	        }
	    };//end of mHandler

	    /*
	    * �o�̬Oadapter�һݭn�Ψ쪺��ƨӷ��AArrayList�ϥΪ��O key valuse���覡�x�s
	    */
	    private List<Map<String, Object>> getData() {
	        getinfo.clear();
	        FiringRoomInfo tempinfo = new FiringRoomInfo();

	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); // �Ndate
	        String tempdate;
	        // uri�O�s������DB���@�Ӥ覡�A�o�̭n�h��²�T��Ʈw��������
	        Uri SMS_INBOX = Uri.parse("content://sms/inbox");
	        Cursor new_sms_cursor = getActivity().getContentResolver().query(SMS_INBOX, new String[] { "address", "date", "body" }, "address='0903410141' or address='+886903410141'", null, null);

	        // �ˬd�O�_���s��sms
	        if (new_sms_cursor.getCount() > 0) {
	            new_sms_cursor.moveToFirst();
	            for (int i = 0; i < new_sms_cursor.getCount(); i++) {

	                // decode��@��H�ݱo���o����
	                Date d = new Date(Long.parseLong(new_sms_cursor.getString(new_sms_cursor.getColumnIndex("date"))));
	                tempdate = dateFormat.format(d);

	                sms = new_sms_cursor.getString(new_sms_cursor.getColumnIndex("body"));

	                messageProcess MsgSave = new messageProcess();
	                MsgSave.insertdata(getActivity().getContentResolver(), sms, tempdate);
	                MsgSave=null;
	                // �۩w�q���禡


	                new_sms_cursor.moveToNext();
	            }// end for
	        }// end if
	        new_sms_cursor.close();

	        // �]���Q�n�D�}��d2d,�ҥH���ˬdd2d�O�_��1
	        Cursor d2d_cursor = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/user_reply"), reply, "msg = 'd2d' and d2d='1'", null, null);
	        if (d2d_cursor.getCount() > 0) {
	            d2d_cursor.moveToFirst();
		    /*
		     * �ݦ��X��²�T��Ū,�N�إߴX���q����getinfo�̭��A�Ҧp���|����Ū���q���A�N�إߥ|��²�T��������T��getinfo�̭�
		     */
	            for (int x = 0; x < d2d_cursor.getCount(); x++) {
	                // �N²�T�s�J�۩w�q������(tempinfo)
//	                tempinfo.setSender(d2d_cursor.getString(1)+" �Q�n"+d2d_cursor.getString(2));
	                tempinfo.setSender(d2d_cursor.getString(1)+" �T�{�����R�O");
	                tempinfo.setContent("�n�D�z�}��M2M�s�u");
	                tempinfo.setToken(d2d_cursor.getString(3));
	                tempinfo.setDate(d2d_cursor.getString(4));
	                tempinfo.setmsg(d2d_cursor.getString(5));
	                // �N�ۭq����s�J��getinfo
	                getinfo.add(tempinfo);
	                tempinfo = new FiringRoomInfo();
	                d2d_cursor.moveToNext();
	            }

	        }
	        d2d_cursor.close();

	        // �ѩ�w�gŪ�Lretrievable���H��,�ҥH������tittle���W��,�G��tittle ��null��data,�Y��ܼ���Ū�H��
	        Cursor user_cursor = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), Form, "read is null and content is not null", null, null);
	        if (user_cursor.getCount() > 0) {
	            user_cursor.moveToFirst();
		    /*
		     * �ݦ��X��²�T��Ū,�N�إߴX���q����getinfo�̭��A�Ҧp���|����Ū���q���A�N�إߥ|��²�T��������T��getinfo�̭�
		     */
	            for (int x = 0; x < user_cursor.getCount(); x++) {
	                // �N²�T�s�J�۩w�q������(tempinfo)
	                tempinfo.setSender(user_cursor.getString(1));
	                tempinfo.setContent(user_cursor.getString(2));
	                tempinfo.setToken(user_cursor.getString(3));
	                tempinfo.setFilesize(user_cursor.getString(4));
	                tempinfo.setDate(user_cursor.getString(5));
	                // �N�ۭq����s�J��getinfo
	                getinfo.add(tempinfo);
	                tempinfo = new FiringRoomInfo();
	                user_cursor.moveToNext();
	            }
	        }
	        user_cursor.close();

		/*
		 * �p�P�W�z���@�k�A���O�o��~�O�u���n�� key value���Ʊ��Avalue�N�O�Ӧ۩�W�z��getinfo
		 * �Ҧp²�TA��notititle���O����?�qgetinfo�����X�� ²�TA��notiinfo�O����?�@�ˤ]�O�qgetinfo�����X��
		 */
	        Map<String, Object> map = new HashMap<String, Object>();
	        for (int i = 0; i < getinfo.size(); i++) { // put info array into
	            // list
	            map = new HashMap<String, Object>();
	            // �N�C��²�T����T�s�J��۩w�q��adapter��������
	            map.put("notititle", getinfo.get(i).getSender());
	            map.put("notiinfo", getinfo.get(i).getContent());
	            map.put("notidate", getinfo.get(i).getDate());
	            map.put("notiimg", R.drawable.message);
	            if (getinfo.get(i).getmsg().equals("reply")) {
	                map.put("notmsg", getinfo.get(i).getmsg());
	            }
	            list.add(map);
	        }

	        return list;
	    }
	    

	    // ����U�q���ɡA�n�����Ʊ�
	    public void onItemClick(AdapterView<?> parent, View view, int position,
	                            long position1) {
	        // TODO Auto-generated method stub

	        String aa;
	        Cursor change_state = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), null, "userstatus='download'", null, null);
	        // �ˬd�ثe���S���ɮץ��b�U��
	        if (change_state.getCount() > 0) {
	            Toast.makeText(getActivity().getApplicationContext(), "�ثe�|���ɮפU�����A�y��A��", Toast.LENGTH_LONG).show();

	        } else {
	            listid = (int) position1;
	            // �ˬd�����ɮ׬O�i�U�� �٬O �n�D�W���ɮ�
	            if (getinfo.get(listid).getmsg().equals("reply")) {

	            }else if (getinfo.get(listid).getmsg().equals("d2d")) {
	                if(AttachParameter.nat){
	                    AlertDialog.Builder dialog_d2d = new AlertDialog.Builder(getActivity());
	                    dialog_d2d.setTitle("�Ц�edit����Server�b���s�}��");
	                    dialog_d2d.setMessage("��]:�]����m�ܰʩ|����s�ζ}������w�L�A�y���A�׳Q�ϥΪ̭n�D");
	                    dialog_d2d.setNegativeButton("�T�w", new DialogInterface.OnClickListener() {

	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {
	                            // TODO Auto-generated method stub
	                            Cursor change_read = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/user_reply"), reply, "msg = 'd2d'", null, null);
	                            if (change_read.getCount() > 0) {
	                                change_read.moveToFirst();
	                                for (int i = 0; i < change_read.getCount(); i++) {
	                                    int id_this = 0;
	                                    id_this = Integer.valueOf(change_read.getString(0));
	                                    String where = UserSchema._ID + " = " + id_this;
	                                    getActivity().getContentResolver().delete(Uri.parse("content://tab.list.d2d/user_reply"), where, null);
	                                    change_read.moveToNext();
	                                }
	                            }
	                            change_read.close();
	                            mHandler.obtainMessage(UPDATE_LIST_MES).sendToTarget();
	                        }
	                    });

	                    dialog_d2d.show();
	                }else{
	                    ConnectivityManager mConnectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	                    final NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

	                    LayoutInflater factory = LayoutInflater.from(getActivity().getApplicationContext());
	                    final View v1 = factory.inflate(R.layout.servertime, null);
	                    timePicker = (NumberPicker) v1.findViewById(R.id.timePicker);
	                    timePicker.setMaxValue(24);
	                    timePicker.setMinValue(1);
	                    timePicker.setValue(1);
	                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
	                    dialog.setTitle("�п��M2M�A�Ȯɶ�");
	                    dialog.setView(v1);
	                    timePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener (){
	                        public void onValueChange(NumberPicker view, int oldValue, int newValue) {
	                            H = String.valueOf(newValue);
	                            System.out.println("�o�̬OH="+H);

	                        }
	                    });

	                    dialog.setPositiveButton("�T�w", new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog, int whichButton) {


	                            if(AttachParameter.out_ip.equals("0.0.0.0")){
	                                Toast.makeText(getActivity().getApplicationContext(), "�|���}��wifi�A�L�k�}��d2d�\��", Toast.LENGTH_LONG).show();
	                            }
	                            else {
	                                ConnectivityManager mConnectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	                                final NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
	                                if(mNetworkInfo.getTypeName().equalsIgnoreCase("MOBILE")){
	                                    Toast.makeText(getActivity(), "��p�A��ʺ������Ҥ��A�X�}��D2D�\��", Toast.LENGTH_LONG).show();
	                                }else{
	                                    AttachParameter.nat = true;

	                                    new preserver().execute();
	                                }
	                                //������ �n��nat

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
	                }

	            }
	            else {

	                Cursor change_read = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/user_data"), Form, "messagetoken='" + getinfo.get(listid).getToken() + "'", null, null);
	                if (change_read.getCount() > 0) {
	                    change_read.moveToFirst();
	                    for (int i = 0; i < change_read.getCount(); i++) {
	                        int id_this = 0;
	                        id_this = Integer.valueOf(change_read.getString(0));
	                        ContentValues values = new ContentValues();
	                        values.put(UserSchema._READ, 1);
	                        String where = UserSchema._ID + " = " + id_this;
	                        getActivity().getContentResolver().update(Uri.parse("content://tab.list.d2d/user_data"), values, where, null);
	                        change_read.moveToNext();
	                    }
	                }
	                change_read.close();
	                // �o�̬Oretrievable
	                Intent intent = new Intent();
	                Bundle bundle = new Bundle();
	                // �ǤJ�H��H
	                bundle.putString("contact", getinfo.get(listid).getSender());
	                intent.putExtras(bundle);

	                intent.setClass(getActivity(), OpenFire.class);
	                getActivity().startActivity(intent);
	            }

	        }// else end
	        change_state.close();
	    }


	    class preserver extends AsyncTask<Void, Void, String> { // implement thread

	        private UPnPPortMapper uPnPPortMapper;
	        String state=new String();
	        ProgressDialog senddialog;
	        @Override
	        protected void onPreExecute() {

	            uPnPPortMapper = new UPnPPortMapper();
	            senddialog = ProgressDialog.show(getActivity(), "�еy��", "server�}�Ҥ�", true);

	            senddialog.show();
	            // �}�Ҹ�ƶǰedialog

	        }
	        @Override
	        protected String doInBackground(Void... params)  { // �@�I�s�N�|���檺�禡
	            if(uPnPPortMapper != null){
	                try {
	                    uPnPPortMapper.openRouterPort(AttachParameter.out_ip, AttachParameter.port, AttachParameter.in_ip, AttachParameter.port, ApplicationConstants.ADD_PORT_DESCRIPTION);
	                    // Defaults
	                    int port = AttachParameter.port;

	                    String host = AttachParameter.in_ip; // bind to all interfaces by default
	                    List<File> rootDirs = new ArrayList<File>();
	                    boolean quiet = false;
	                    Map<String, String> options = new HashMap<String, String>();

	                    if (rootDirs.isEmpty()) {
	                        rootDirs.add(new File(Environment.getExternalStorageDirectory().getAbsolutePath()));
	                    }

	                    options.put("host", host);
	                    options.put("port", "" + port);
	                    options.put("quiet", String.valueOf(quiet));
	                    StringBuilder sb = new StringBuilder();
	                    for (File dir : rootDirs) {
	                        if (sb.length() > 0) {
	                            sb.append(":");
	                        }
	                        try {
	                            sb.append(dir.getCanonicalPath());
	                        } catch (IOException ignored) {
	                        }
	                    }
	                    options.put("home", sb.toString());
	                    ServiceLoader<WebServerPluginInfo> serviceLoader = ServiceLoader.load(WebServerPluginInfo.class);

	                    for (WebServerPluginInfo info : serviceLoader) {
	                        String[] mimeTypes = info.getMimeTypes();
	                        for (String mime : mimeTypes) {
	                            String[] indexFiles = info.getIndexFilesForMimeType(mime);
	                            if (!quiet) {
	                                System.out.print("# Found plugin for Mime type: \"" + mime + "\"");
	                                if (indexFiles != null) {
	                                    System.out.print(" (serving index files: ");
	                                    for (String indexFile : indexFiles) {
	                                        System.out.print(indexFile + " ");
	                                    }
	                                }
	                                System.out.println(").");
	                            }
	                            //registerPluginForMimeType(indexFiles, mime, info.getWebServerPlugin(mime), options);
	                        }
	                    }
	                    AttachParameter.server =new openserver(host, port, rootDirs, quiet);
	                    AttachParameter.server.setContent(getActivity().getContentResolver());
	                    try {
	                    	AttachParameter.server.start();

	                    } catch (IOException ioe) {
	                        System.err.println("Couldn't start server:\n" + ioe);
	                        System.exit(-1);
	                        state="no";
	                    }
	                    state="ok";
	                    System.out.println("Server started, Hit Enter to stop.\n");
	                    User user = new User();
	                    Calendar c = Calendar.getInstance();

	                    String res =user.setservicetime("H="+H+"&M="+c.get(Calendar.MINUTE));
	                    if(!AttachParameter.chechsuccess(res)){
	                        state="time_error";
	                    }
	                }catch (SocketException e){
	                    e.printStackTrace();
	                    state="no";
	                } catch (IOException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                    state="no";
	                } catch (UPNPResponseException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                    state="no";
	                }
	            }
	            return state;
	        }
	        protected void onPostExecute(String state) {
	            senddialog.dismiss();
	            if(state.equalsIgnoreCase("no")){
	                Toast.makeText(getActivity().getApplicationContext(), "server�}�ҥ���", Toast.LENGTH_SHORT).show();
	            }else if(state.equalsIgnoreCase("time_error")){
	                AttachParameter.nat=false;
	                new posserver(AttachParameter.out_ip, AttachParameter.port,"�}��ɶ��]�w����,server�N�۰�����").execute();
	                AttachParameter.server.stop();
	            }
	            else{
	                Cursor change_read = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/user_reply"), reply, "msg = 'd2d' and d2d='1'", null, null);
	                int req_server=0;
	                req_server=change_read.getCount();
	                change_read.close();
	                if (req_server > 1) {
	                    AlertDialog.Builder Dialog = new AlertDialog.Builder(getActivity());
	                    Dialog.setTitle("server�}�Ҧ��\");
	                    Dialog.setMessage("�ѩ󦳦h��ϥΪ̭n�D�}��server�A�Ы��T�{��s�ۦP�n�D");
	                    Dialog.setIcon(android.R.drawable.ic_dialog_info);
	                    Dialog.setNeutralButton("�T�w", new DialogInterface.OnClickListener() { // �������ɮ�
	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {
	                            Cursor change_read = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/user_reply"), reply, "msg = 'd2d' and d2d='1'", null, null);
	                            if (change_read.getCount() > 0) {
	                                change_read.moveToFirst();
	                                for (int i = 0; i < change_read.getCount(); i++) {
	                                    int id_this = 0;
	                                    id_this = Integer.valueOf(change_read.getString(0));
	                                    String where = UserSchema._ID + " = " + id_this;
	                                    ContentValues values = new ContentValues();
	                                    values.put(UserSchema._D2D, 0);
	                                    getActivity().getContentResolver().update(Uri.parse("content://tab.list.d2d/user_reply"), values,where, null);
	                                    change_read.moveToNext();
	                                }
	                            }
	                            change_read.close();

	                        }
	                    });

	                    Dialog.show();
	                }else if(req_server==1){

	                    Cursor del_d2d_req = getActivity().getContentResolver().query(Uri.parse("content://tab.list.d2d/user_reply"), reply, "messagetoken='" + getinfo.get(listid).getToken() + "'", null, null);
	                    if (del_d2d_req.getCount() > 0) {
	                        del_d2d_req.moveToFirst();
	                        int id_this = 0;
	                        id_this = Integer.valueOf(del_d2d_req.getString(0));
	                        String where = UserSchema._ID + " = " + id_this;
	                        ContentValues values = new ContentValues();
	                        values.put(UserSchema._D2D, 0);
	                        getActivity().getContentResolver().update(Uri.parse("content://tab.list.d2d/user_reply"), values,where, null);
	                        del_d2d_req.moveToNext();

	                    }
	                    change_read.close();

	                    mHandler.obtainMessage(UPDATE_LIST_MES).sendToTarget();
	                    Toast.makeText(getActivity().getApplicationContext(), "server�}�Ҧ��\", Toast.LENGTH_SHORT).show();
	                }
	            }
	        }
	    }

	    public class posserver extends AsyncTask<Void, Void, String> {
	        String state=new String();
	        private UPnPPortMapper uPnPPortMapper;
	        private String externalIP;
	        private int externalPort;
	        private String message;
	        ProgressDialog senddialog;
	        public posserver(String externalIP, int externalPort, String msg) {
	            this.message = msg;
	            this.externalIP = externalIP;
	            this.externalPort = externalPort;
	        }
	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            uPnPPortMapper = new UPnPPortMapper();
	            if(message.equalsIgnoreCase("")){
	                senddialog = ProgressDialog.show(getActivity(), "�еy��", "server������", true);

	            }else{
	                senddialog = ProgressDialog.show(getActivity(), "�еy��", message, true);
	            }


	            senddialog.show();
	        }
	        @Override
	        protected String doInBackground(Void... params) {
	            if(uPnPPortMapper != null){
	                try {

	                    uPnPPortMapper.removePort(externalIP, externalPort);
	                    AttachParameter.server.stop();
	                }
	                catch (IOException e) {
	                    state="no";
	                    e.printStackTrace();
	                } catch (UPNPResponseException e) {
	                    state="no";
	                    e.printStackTrace();
	                }
	                state="yes";
	            }
	            return state;
	        }
	        protected void onPostExecute(String state) {
	            senddialog.dismiss();
	            if(state.equalsIgnoreCase("no")){
	                Toast.makeText(getActivity().getApplicationContext(), "server��������", Toast.LENGTH_SHORT).show();
	            }else{
	                Toast.makeText(getActivity().getApplicationContext(), "server�������\", Toast.LENGTH_SHORT).show();
	            }
	        }
	    }//end of posserver

	    private class getContent extends AsyncTask<Void, Void, Void> {

	        @Override
	        protected void onPreExecute() {

	            // �}�Ҹ�ƶǰedialog
	            pdialog = ProgressDialog.show(getActivity(), "�еy��", "���Ū����", true);
	            pdialog.show();
	        }

	        @Override
	        protected Void doInBackground(Void... params) {
	            String[] aliveIp;
	            aliveIp = locup.locationupdate(Login.latest_cookie, AttachParameter.getIPAddress(getActivity().getApplicationContext()), AttachParameter.port);
	            String ip =aliveIp[0];
	            messageProcess MsgSave = new messageProcess();
	            MsgSave.checkwlan(getActivity().getContentResolver(), aliveIp[4]);
	            updateContent(Uri.parse("content://tab.list.d2d/user_data"), "retrievable","content is null",ip);
	            MsgSave=null;
	            pdialog.dismiss();
	            // �n�[�W���~���P�_
	            mHandler.obtainMessage(UPDATE_LIST_MES).sendToTarget();
	            return null;
	        }
	    }

	    // ��s��Ʈw���A
	    public void updateContent(Uri location, String mod, String condition, String ip) {
	        int id_this;
	        String where, content, token, tittle;
	        String[] Form = { UserSchema._ID, UserSchema._MESSAGETOKEN };
	        String[] reretrieve = new String[7];

	        Cursor up_content = getActivity().getContentResolver().query(location, Form, condition, null, null);
	        if (up_content.getCount() > 0) {
	            up_content.moveToFirst();
	            Retrieve retreive =new Retrieve();
	            for (int i = 0; i < up_content.getCount(); i++) {
	                token = up_content.getString(1);
	                reretrieve = retreive.retrieve_req(token, mod);
	                if (reretrieve[1].equals("true")) {
	                    if(retreive.retrieveFileCount.length > 3){
	                        content = reretrieve[0].substring(reretrieve[0].indexOf("content=") + 8, reretrieve[0].indexOf("&file"));
	                    }else{
	                        content = reretrieve[0].substring(reretrieve[0].indexOf("content=") + 8, reretrieve[0].length()-1);
	                    }
	                    id_this = Integer.valueOf(up_content.getString(0));
	                    ContentValues values = new ContentValues();
	                    if (mod.equals("retrievable")) {
	                        tittle = reretrieve[0].substring(reretrieve[0].indexOf("subject=") + 8, reretrieve[0].indexOf("&content="));
	                        values.put(UserSchema._CONTENT, content);
	                        values.put(UserSchema._TITTLE, tittle);
	                        values.put(UserSchema._USESTATUS, "");
	                        values.put(UserSchema._FILEPATH, "");
	                    }else{
	                        values.put(UserSchema._FILENAME, content);
	                    }

	                    where = UserSchema._ID + " = " + id_this;
	                    getActivity().getContentResolver().update(location, values, where, null);

	                }
	                up_content.moveToNext();
	            }

	        }
	        up_content.close();
	    }

	    @Override
	    public void onResume() {// ���B��,�I���U���� �R���T��
	        super.onResume();
	        list.clear();
	        list = getData();
	        if(list.isEmpty()){
	            Audiolist.setBackgroundResource(R.drawable.worldbackground);
	        }else{
	            Audiolist.setBackgroundResource(R.drawable.worldbackground);
	        }
	        adapter.notifyDataSetChanged();
	    }

}
