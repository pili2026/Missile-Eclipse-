package kissmediad2d.android;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import login.submit1.EditAccount;
import login.submit1.Logout;
import login.submit1.User;
import net.sbbi.upnp.messages.UPNPResponseException;
import softwareinclude.ro.portforwardandroid.asyncTasks.WebServerPluginInfo;
import softwareinclude.ro.portforwardandroid.asyncTasks.openserver;
import softwareinclude.ro.portforwardandroid.network.UPnPPortMapper;
import softwareinclude.ro.portforwardandroid.util.ApplicationConstants;
import tab.list.AttachParameter;
import tab.list.FileContentProvider;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.iangclifton.android.floatlabel.FloatLabel;

public class edit extends Fragment implements OnClickListener{
	/**
	 * �o��O�Ψӽs��ϥΪ̪���T�A�i�H�ק�K�X��q�ܸ��X
	 */
	private TextView name;
	private FloatLabel newpw, newpwagain, phone;
	private NumberPicker timePicker;
	public static Switch open_server;
    String H="1";
    String M;

	ArrayAdapter<String> adapter;
	String getNotifyMethod = new String(), smsstate = new String(), getNewPw = new String(), getComfirm = new String(), getphone = new String();

	EditAccount editAccount = new EditAccount();

	User userdata = new User();
	ProgressDialog pdialog = null;
	boolean getRes = false;
	
	
	private static final String ARG_SECTION_NUMBER = "section_number";
	public static edit newInstance(int sectionNumber) {
		edit fragment = new edit();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

	        // Inflate the layout for this fragment
	        final View view = inflater.inflate(R.layout.edit, container, false);
	        ((MainActivity) getActivity()).getActionBar().setTitle("Edit");

	        TextView ok = (Button) view.findViewById(R.id.okbutton);
	        name = (TextView) view.findViewById(R.id.username);
	        phone = (FloatLabel) view.findViewById(R.id.user_phone);
	        newpw = (FloatLabel) view.findViewById(R.id.NewPassword);
	        newpwagain = (FloatLabel) view.findViewById(R.id.NewPasswordAgain);
	        open_server = (Switch) view.findViewById(R.id.switch1);
	        open_server.setChecked(AttachParameter.nat);
	        ok.setOnClickListener(this);
	        open_server.setOnCheckedChangeListener(LisServer);
	        new getuser().execute();

	        return view;
	 }//end of onCreateView
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
        switch (id){
        case R.id.logout:
        	FileContentProvider test = new FileContentProvider();
			test.del_table(Uri.parse("content://tab.list.d2d/user_info"));

			ConnectivityManager CM = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = CM.getActiveNetworkInfo();
    		if (info == null || !info.isAvailable()) {
    			
    		}else{
    			Logout logout = new Logout();
    			logout.logout_start();
    		
    		}
			info=null;
			CM=null;
			
			// intent.setClass(writepage.this, browse.class);
			intent.setClass(getActivity(), LoginMissile.class);
			// startActivityForResult(intent,0);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); 
			startActivity(intent);
			getActivity().finish();
			break;
        case android.R.id.home:

        	getActivity().finish();
        	break;
        
        }
        
		return true;
	}
		
	
	@Override
	public void onClick(View v) {
		getNewPw = newpw.getEditText().getText().toString();
		getComfirm = newpwagain.getEditText().getText().toString();
		getphone = phone.getEditText().getText().toString();
		//�P�_���O�_����
		if (getNewPw.equals("") | (getNewPw.equals("") & getComfirm.equals(""))) {
			Toast.makeText(getActivity().getApplicationContext(), "�K�X�B���i���šA�Э��s��g", Toast.LENGTH_SHORT).show();
		} else if (getComfirm.equals("")) {
			Toast.makeText(getActivity().getApplicationContext(), "���ƱK�X�B���i���šA�Э��s��g", Toast.LENGTH_SHORT).show();

		} else {
			switch (v.getId()) {
			case R.id.okbutton:
				//�ˬd�⦸�K�X�O�_�ۦP
				if (getNewPw.equalsIgnoreCase(getComfirm)) {
					pdialog = ProgressDialog.show(getActivity(), "�еy��", "�ק襤", true);
					new Thread() {
						public void run() {
							try {
								while (!getRes) {
									Thread.sleep(1000);
								}

							} catch (Exception e) {
								Log.i("Thread", "dialog error");
							} finally {
								pdialog.dismiss();
							}
						}
					}.start();

					new edit_profile().execute();
					new WaitForResponse().execute();
				} else {
					Toast.makeText(getActivity().getApplicationContext(), "�⦸�K�X��J���P�A�Э��s��g", Toast.LENGTH_SHORT).show();

				}

				break;

			}
		}

	}
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
    	menu.clear();
    	inflater.inflate(R.menu.main, menu);
        }
	
	//���ѥثe��ܬOsms�٬Owlan
	public OnCheckedChangeListener LisServer = new OnCheckedChangeListener() {

		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			
			if (isChecked) {
			    
				LayoutInflater factory = LayoutInflater.from(getActivity());
				final View v1 = factory.inflate(R.layout.servertime, null);
				timePicker = (NumberPicker) v1.findViewById(R.id.timePicker);
				timePicker.setMaxValue(24);  
				timePicker.setMinValue(1);    
				timePicker.setValue(1);AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
				dialog.setTitle("�п�ܤ����~�}��ɶ�");
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
							open_server.setChecked(false);
							Toast.makeText(getActivity().getApplicationContext(), "�|���}��wifi�A�L�k�}��d2d�\��", Toast.LENGTH_LONG).show();					
						}
//						else if(AttachParameter.in_ip.equals(AttachParameter.out_ip)&& AttachParameter.out_ip != "0.0.0.0"){
//							open_server.setChecked(false);
//							Toast.makeText(getApplicationContext(), "��p�A�ثe���Ҥ��A�X�}��d2d�\��", Toast.LENGTH_LONG).show();					
//						
//						}
						else {
				    		ConnectivityManager mConnectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
				    		final NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
							mNetworkInfo.getTypeName();
							if(mNetworkInfo.getTypeName().equalsIgnoreCase("MOBILE")){
								Toast.makeText(getActivity().getApplicationContext(), "��p�A��ʺ������Ҥ��A�X�}��D2D�\��", Toast.LENGTH_LONG).show();
								open_server.setChecked(false);
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
						open_server.setChecked(false);
					}
				});
				dialog.show();

			} else {
				if(AttachParameter.nat){
					AttachParameter.nat=false;
					new posserver(AttachParameter.out_ip,AttachParameter.port,"").execute();
					AttachParameter.server.stop();
					
					 System.out.println("Server stopped.\n");
//				     Intent intent = new Intent(edit.this,CopyOfAddPortAsync.class);
//				     stopService(intent);
				     }
				else{
					AttachParameter.nat=false;
				//�o����P���q

				}

			}

		}
	};
    static String TimeFix(int c){
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
	 public class posserver extends AsyncTask<Void, Void, String> {
		 String state=new String();
		    private UPnPPortMapper uPnPPortMapper;
		    private String externalIP;
		    private int externalPort;
		    private String message;
			ProgressDialog senddialog;
		    public posserver(String externalIP,int externalPort,String msg) {
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
		            	User user = new User();
		            	user.setservicetime("H=-"+H+"&M=0");
		            	user=null;

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
				open_server.setChecked(AttachParameter.nat);
				if(state.equalsIgnoreCase("no")){
					Toast.makeText(getActivity(), "server��������", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getActivity(), "server�������\", Toast.LENGTH_SHORT).show();
				}
			}
		}

	class preserver extends AsyncTask<Void, Void, String> // implement thread
	{	 
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
    			// ���response�r��A��0�}�l��A��";"�e������
                                   try {
									uPnPPortMapper.openRouterPort(AttachParameter.out_ip, AttachParameter.port,AttachParameter.in_ip,AttachParameter.port, ApplicationConstants.ADD_PORT_DESCRIPTION);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (UPNPResponseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
//            PortMapping desiredMapping =
//                    new PortMapping(
//                    		AttachParameter.port,
//                            AttachParameter.in_ip,
//                            PortMapping.Protocol.TCP,
//                            "My Port Mapping"
//                    );
//            final WifiManager wifiManager =(WifiManager) getSystemService(Context.WIFI_SERVICE);
//            UpnpService upnpService =
//                    new UpnpServiceImpl(
//                            new PortMappingListener(desiredMapping)
//                    );
//
//            upnpService.getControlPoint().search();
                
			// Defaults
	        int port = AttachParameter.port;
//	        int port=33102;
//	        AttachParameter.port=port;
	        String host = AttachParameter.in_ip; // bind to all interfaces by default
	        //String host = AttachParameter.out_ip;
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
	    	 user=null;
	    	 if(!AttachParameter.chechsuccess(res)){
	    		 state="time_error";
	    	 }

		}
			return state;
		}
		protected void onPostExecute(String state) {
			senddialog.dismiss();
			if(state.equalsIgnoreCase("no")){
				AttachParameter.nat=false;
				Toast.makeText(getActivity(), "server�}�ҥ���", Toast.LENGTH_SHORT).show();
			}else if(state.equalsIgnoreCase("time_error")){
				AttachParameter.nat=false;
				new posserver(AttachParameter.out_ip,AttachParameter.port,"�}��ɶ��]�w����,server�N�۰�����").execute();
				AttachParameter.server.stop();
			}
			else{
				Toast.makeText(getActivity(), "server�}�Ҧ��\", Toast.LENGTH_SHORT).show();
			}
				
			
			
		}
	}
	private class getuser extends AsyncTask<Void, Void, String[]> {

		@Override
		protected void onPreExecute() {
			// �}�Ҹ�ƶǰedialog
			pdialog = ProgressDialog.show(getActivity(), "�еy��", "���Ū����", true);
			pdialog.show();
		}

		@Override
		protected String[] doInBackground(Void... params) {
			String[] userinfo = new String[4];
			userinfo = userdata.getuser();
			return userinfo;
		}

		protected void onPostExecute(String[] userinfo) {
			pdialog.dismiss();
			//userinfo�q1�}�l���O�O user_name user_phone user_sms
			name.setText(userinfo[1]);
			phone.getEditText().setText(userinfo[2]);
			//toLowerCase()�ק�r�����g�p


		}
	}

	private class edit_profile extends AsyncTask<Void, Void, Void> // implement thread
	{
		@Override
		protected Void doInBackground(Void... params) { // �@�I�s�N�|���檺�禡
			String requsest = "new_password=" + getNewPw + "&new_phone=" + getphone + "&new_sms=" + smsstate;
			editAccount.setrequsest(requsest);
			editAccount.edituser();
			getRes = editAccount.getResponse();

			return null;
		}

	}

	private class editsms extends AsyncTask<Void, Void, Void> // implement
																// thread
	{
		@Override
		protected Void doInBackground(Void... params) { // �@�I�s�N�|���檺�禡
			editAccount.editnotify(smsstate);
			return null;
		}

	}

	private class WaitForResponse extends AsyncTask<Void, Void, Void> // implement
																		// thread
	{

		@Override
		protected Void doInBackground(Void... params) { // �@�I�s�N�|���檺�禡
			while (!getRes)
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					e.printStackTrace();
				}

			return null;
		}

		protected void onPostExecute(Void noUse) // ��u�@������ �|�I�s���禡
		{
			String getLine = editAccount.getRegReadLine();
			Pattern pattern = Pattern.compile("ret=0"); // check correct
			Matcher matcher = pattern.matcher(getLine);

			if (matcher.find()) {
				AlertDialog.Builder Dialog = new AlertDialog.Builder(getActivity()); // Dialog
				Dialog.setTitle("");
				Dialog.setMessage("�ק令�\");
				Dialog.setIcon(android.R.drawable.ic_dialog_info);
				Dialog.setNeutralButton("�T�w", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						newpw.getEditText().setText("");
						newpwagain.getEditText().setText("");

					}
				});
				Dialog.show();
			} else {
				AlertDialog.Builder Dialog = new AlertDialog.Builder(getActivity()); // Dialog
				Dialog.setTitle("");
				Dialog.setMessage("�ק異��");
				Dialog.setIcon(android.R.drawable.ic_dialog_info);
				Dialog.setNeutralButton("�T�w", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
				Dialog.show();
			}

		}

	}
}
