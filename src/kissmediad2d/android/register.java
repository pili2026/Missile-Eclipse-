package kissmediad2d.android;

import java.util.Date;

import login.submit1.regFromServer;
import tab.list.AttachParameter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.iangclifton.android.floatlabel.FloatLabel;

public class register extends Activity implements OnClickListener {
	/**
	 * �������O�Ӧ۩�loginActivity��intent,
	 * �o�̬O�ΨӰ����U�b���Ϊ��A���U����ݭn��email���ҡA���Ҧ��\�~��n�J
	 * 	 */
	private Button reg, back;
	private FloatLabel regid, regpw, regpwagain, regphone;
	private String getId, getPw, getRetype, getPhone,getsms="False";
	regFromServer regist;
	ProgressDialog pdialog = null;
	boolean getRes = false;
	private ToggleButton regsb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Put your code here
		setTitle("Register");
		regist = new regFromServer();
		setContentView(R.layout.register);
		reg = (Button) findViewById(R.id.reg);
		back = (Button) findViewById(R.id.back);
		regid = (FloatLabel) findViewById(R.id.RegID);
		regpw = (FloatLabel) findViewById(R.id.RegPassword);
		regpwagain = (FloatLabel) findViewById(R.id.RegPasswordAgain);
		regphone = (FloatLabel) findViewById(R.id.RegPhone);
		//regmail = (FloatLabel) findViewById(R.id.mail);

		reg.setOnClickListener(this);
		back.setOnClickListener(this);
	}
	/*
	 * ����U���U�îɩҭn������
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.reg:
			//���o�e���W�Ҧ�editext����T
			getId = regid.getEditText().getText().toString();
			getPw = regpw.getEditText().getText().toString();
			getRetype = regpwagain.getEditText().getText().toString();
			getPhone = regphone.getEditText().getText().toString();
			//getmail = regmail.getEditText().getText().toString();
			/*
			 * �P�_�U�����O�_����
			 * */
			ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = CM.getActiveNetworkInfo();

			if (getId.equals("") | getPw.equals("") | getRetype.equals("")
					| getPhone.equals("")  ) {
				Toast.makeText(getApplicationContext(), "�|�����i���šA���~���g",
						Toast.LENGTH_SHORT).show();

			} else if (!(Linkify.addLinks(regphone.getEditText().getText(),
					Linkify.PHONE_NUMBERS))) {//�ˬd���X�O�_�ŦX��A�Ϊ�method
				Toast.makeText(getApplicationContext(),
						"�q�ܿ��~�A�����O��0-9�Ʀr�զ���10��ơA�Э��s��g", Toast.LENGTH_SHORT)
						.show();
			}

			else if (!(getPw.equalsIgnoreCase(getRetype))) // �p�G�K�X�P�T�{�K�X�ۦP
			{
				Toast.makeText(getApplicationContext(), "�⦸��J���K�X���P�A�Э��s��g",
						Toast.LENGTH_SHORT).show();
			} 
			else if(info == null || !info.isAvailable()){
				Toast.makeText(register.this, "�L�i�κ���", Toast.LENGTH_LONG).show();
			}else {

				new registThread().execute();
			}
			break;
		case R.id.back:
			finish();
			break;
		}
	}
	
	//���ѥثe��ܬOsms�٬Owlan
	public OnCheckedChangeListener sbcheck = new OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (isChecked) {
				getsms = "True";
			} else {
				getsms = "False";
			}
			
		}
	};
	
	//���U�éҭn���檺thread,�o�|�I�s�۩w�q���h��class,�PServer�����q
	private class registThread extends AsyncTask<Void, Void, String[]> // implement
																		// thread
	{
		String[] regreturn = new String[5];
		String[] conreturn = new String[4];
		String response;
		AlertDialog.Builder Dialog = new AlertDialog.Builder(register.this);
		@Override
		protected void onPreExecute() {
			// �}�Ҹ�ƶǰedialog
			pdialog = ProgressDialog.show(register.this, "�еy��", "���U��", true);
			pdialog.show();
		}
		@Override
		// �@�I�s�N�|���檺�禡�A�b�I�������檺��
		protected String[] doInBackground(Void... params) {

			String requestString = "user=" + getId + "&password=" + getPw
					+ "&phone=" + getPhone+ "&sms=" + getsms;

			// regist�O�۩w�q��regfromserver
			// �|�^��regreturn[0]:verifycode [1]:�����[2]:�S���[3][4]:�o�ͨҥ~
			
			response = regist.regist(requestString);
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
				Intent intent = new Intent();
				Bundle content =new Bundle();
				content.putString("phone",getPhone);
	
				intent.putExtras(content);
				intent.setClass(register.this, sms.class);
				// startActivityForResult(intent,0);
				startActivity(intent);
			}else{
				AlertDialog.Builder Dialog = new AlertDialog.Builder(register.this); // Dialog
				Dialog.setTitle("���U����");
				Dialog.setMessage("��p�A�ӥΤ�W�٤w�Q�ϥΡA�п�J��L�Τ�W��");
				Dialog.setIcon(android.R.drawable.ic_dialog_info);
				Dialog.setNegativeButton("�T�w", new DialogInterface.OnClickListener() { // ���Uabort
							// �Nthread����
							// ����progressbar
							// �]�w���s����U�ɵ����A�����o��Dialog�ä��_�o��thread
							@Override
							public void onClick(DialogInterface dialog, int which) {
								regid.getEditText().setText("");
							}
						});
				Dialog.show();
			}

		}

		// �����ɭn������
		protected void onCancelled() {
			new registThread().cancel(true);
			Dialog.create().dismiss();
		}
	}

}
