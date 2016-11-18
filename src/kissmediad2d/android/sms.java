package kissmediad2d.android;

import java.text.SimpleDateFormat;
import java.util.Date;

import login.submit1.regFromServer;
import tab.list.AttachParameter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import database.storage.messageProcess;

public class sms extends Activity implements OnClickListener{
	private static final int UPDATE_LIST_MES = 0;
	EditText recsms;
	String smscode;
	Boolean keeprun=true;
	Button Bvalidate;
	Thread aliveThread;
	public String sms = null;
	long time;
	String phone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Validate");
		Intent data = getIntent();
		Bundle bundle = data.getExtras();
		// ���o�ǤJ��contact,���Ofilename ���Osender(�H���)
		phone = bundle.getString("phone");
		// TODO Put your code here
		setContentView(R.layout.sms);
		recsms = (EditText) this.findViewById(R.id.recsms);
		Bvalidate=(Button) this.findViewById(R.id.Bvalidate);
		Bvalidate.setOnClickListener(this);
		aliveThread = new Thread(runnable);
		aliveThread.start();
	}
	
	private Handler mHandler = new Handler() {
		// ����k�bui�u�{�B��
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_LIST_MES:
				recsms.setText(smscode);
				
				break;
			}
		}
	};
	// �إߤu�@�M��A�C30���ˬd
	Runnable runnable = new Runnable() {
		String smsbody;
		String []splitbody;
		long smstime,timenow,min;
		@Override
		public void run() {
			while (keeprun) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						Log.i("update list thread", "Thread.currentThread().isInterrupted()");
					}
					
					Uri SMS_INBOX = Uri.parse("content://sms/inbox");
					Cursor new_sms_cursor = getContentResolver().query(SMS_INBOX, new String[] { "address", "date", "body" }, "address='0903410141' or address='+886903410141'", null, null);
					if(new_sms_cursor.getCount()>0){
						new_sms_cursor.moveToFirst();
						for (int i = 0; i < new_sms_cursor.getCount(); i++) {
							smsbody=sms = new_sms_cursor.getString(new_sms_cursor.getColumnIndex("body"));
							splitbody=smsbody.split("&");
							if(splitbody.length==1){
								//���o²�T�ɶ�
								smstime=Long.parseLong(new_sms_cursor.getString(new_sms_cursor.getColumnIndex("date")));
								//Date mDate = new Date();
								//���o�{�b�ɶ�
								//timenow=mDate.getTime();
								//�ˬd�ɶ��O�_�b�W�w��
								
								Date mDate = new Date();
								time=mDate.getTime();
						
								min=(smstime-time)/(1000*60);
								if(min>=0){
									keeprun=false;
									//splitbody=smsbody.split(" ");
									smscode=smsbody.substring(smsbody.indexOf(':')+1,smsbody.indexOf(','));
									mHandler.obtainMessage(UPDATE_LIST_MES).sendToTarget(); // �ǰe�n�D��slist���T����handler
									break;
								}else{
									splitbody=null;
									new_sms_cursor.moveToNext();
								}
							}else{
								splitbody=null;
								new_sms_cursor.moveToNext();
							}
							
						}	//for end
				}//if end
				new_sms_cursor.close();
			}//while end
		}
	};
	/*
	 * ����U���U�îɩҭn������
	 */
	private class validate extends AsyncTask<Void, Void, String> {
		AlertDialog.Builder Dialog = new AlertDialog.Builder(sms.this);
		ProgressDialog progressDialog = null;
		
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(sms.this, "�еy��", "���Ҥ�", true);
		}
		@Override
		protected String doInBackground(Void... arg0) {
			
			regFromServer validte=new regFromServer();
			String resp=validte.validate(smscode);

			// TODO Auto-generated method stub
			return resp;
		}
		@Override
		protected void onPostExecute(String resp) {
			progressDialog.dismiss();
			String res=new String();
			Boolean result =AttachParameter.chechsuccess(resp);
			if(result){
				res="true";
			}
			else{
				res="false";
			}
			if(res.equalsIgnoreCase("true")){
				AlertDialog.Builder Dialog = new AlertDialog.Builder(sms.this); // Dialog
				Dialog.setTitle("����");
				Dialog.setMessage("���U���\�A�Ы��T�w���^�D�e��");
				Dialog.setIcon(android.R.drawable.ic_dialog_info);
				Dialog.setNegativeButton("�T�w", new DialogInterface.OnClickListener() { // ���Uabort
							// �Nthread����
							// ����progressbar
							// �]�w���s����U�ɵ����A�����o��Dialog�ä��_�o��thread
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent intent = new Intent();
								intent.setClass(sms.this, LoginMissile.class);
								// startActivityForResult(intent,0);
								startActivity(intent);
							}
						});
				Dialog.show();

			}else{
				String [] errmsg=resp.split("&msg=");
				if(errmsg[1].equalsIgnoreCase("verify code Error")){
					Dialog.setTitle("��p�A�ϥΨ���~��²�T����");
					Dialog.setMessage("�е��ݱ����s������²�T");
					Dialog.setIcon(android.R.drawable.ic_dialog_info);
					Dialog.setNegativeButton("�T�w", new DialogInterface.OnClickListener() { // ���Uabort
								@Override
								public void onClick(DialogInterface dialog, int which) {
									keeprun=true;
								}
							});
					Dialog.show();
				}else if(errmsg[1].equalsIgnoreCase("Verify Code time out")){
					Dialog.setTitle("��p�A���ҽX�O��");
					Dialog.setMessage("�Ы��T�w���o���ҽX²�T");
					Dialog.setIcon(android.R.drawable.ic_dialog_info);
					Dialog.setNegativeButton("�T�w", new DialogInterface.OnClickListener() { // ���Uabort
								@Override
								public void onClick(DialogInterface dialog, int which) {
									keeprun=true;
								}
							});
					Dialog.show();
				}

			}
		}
			
		
	}
	//���U�éҭn���檺thread,�o�|�I�s�۩w�q���h��class,�PServer�����q
	private class registAgain extends AsyncTask<Void, Void, String[]> // implement
																		// thread
	{
		String[] regreturn = new String[5];
		String[] conreturn = new String[4];
		String response;
		ProgressDialog pdialog;
		AlertDialog.Builder Dialog = new AlertDialog.Builder(sms.this);
		@Override
		protected void onPreExecute() {
			// �}�Ҹ�ƶǰedialog
			pdialog = ProgressDialog.show(sms.this, "�еy��", "���s�n�D���U�X��", true);
			pdialog.show();
		}
		@Override
		// �@�I�s�N�|���檺�禡�A�b�I�������檺��
		protected String[] doInBackground(Void... params) {
			regFromServer regist = new regFromServer();
			String requestString = "user=" + "" + "&password=" + ""
					+ "&phone=" + phone+ "&sms=" + "";

			// regist�O�۩w�q��regfromserver
			// �|�^��regreturn[0]:verifycode [1]:�����[2]:�S���[3][4]:�o�ͨҥ~
			
			response = regist.regist(requestString);
			regist=null;
			boolean result =AttachParameter.chechsuccess(response);
			if(result){
				regreturn[0]="true";
			}else{
				
				regreturn[0]="false";
			}
			return regreturn;
		}

		@Override
		// �I���u�@������n�����Ʊ��A�ǤJ���Overifycode
		protected void onPostExecute(String[] regreturn) {
			pdialog.dismiss();
			if (regreturn[0] == "true") {
				AlertDialog.Builder Dialog = new AlertDialog.Builder(sms.this); // Dialog
				Dialog.setTitle("�n�D���\!");
				Dialog.setMessage("�е��ݷs�����ҽX");
				Dialog.setIcon(android.R.drawable.ic_dialog_info);
				Dialog.setNegativeButton("�T�w", new DialogInterface.OnClickListener() { // ���Uabort
							// �Nthread����
							// ����progressbar
							// �]�w���s����U�ɵ����A�����o��Dialog�ä��_�o��thread
							@Override
							public void onClick(DialogInterface dialog, int which) {
							
							}
						});
				Dialog.show();
			}else{

			}

		}

		// �����ɭn������
		protected void onCancelled() {
		}
	}
	
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
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.Bvalidate:
			new validate().execute();
			break;
		}
		
	}
}
