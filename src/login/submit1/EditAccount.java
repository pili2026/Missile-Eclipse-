package login.submit1;

import java.io.IOException;

import tab.list.AttachParameter;

import kissmediad2d.android.LoginMissile;

import com.github.kevinsawicki.http.HttpRequest;

import android.util.Log;

public class EditAccount  {
	/*
	 * �o��Oedit.java�I�s���A�Ψ���ܨϥΪ̸�T�A�ϥγo�i�H�s��L���K�X�����SMS���A
	 */

	private String id, requsest, phone;
	private String regReadLine = null;

	private boolean response = false;

	public EditAccount() {

	}

	// �w�]��response�Ofalse
	public boolean getResponse() {
		return response;
	}

	// �w�]��reReadLine�Onull
	public String getRegReadLine() {
		return regReadLine;
	}

	// �]�wpassword
	public void setrequsest(String arg) {
		requsest = arg;
	}

	// �s��password
	public void edituser() {
		try {
			response = false;

			String pathUrl = "http://" + LoginMissile.Homeip + "/wsgi/account/changepassword/";
			// �ϥ�POST���覡�إ߳s�u
			HttpRequest request = HttpRequest.post(pathUrl);
			// �]�wcookie�B�ǰerequest�r��A���e���s�K�X�B�s�q�ܡA�óz�Lbody���oresponse
			String body = request.header("cookie", Login.latest_cookie).send(requsest).body();

			// �p�G���A���O200
			if (request.ok()) {
				regReadLine = body;
				response = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	// �s��q�����A�A�ˬd�ǤJ��method��sms��wlan(wireless lan)
	public void editnotify(String method) {
		try {
			String req = "method=" + method;
			String pathUrl1 = "http://" + LoginMissile.Homeip + "/wsgi/account/setnotification/";
			HttpRequest request1 = HttpRequest.post(pathUrl1);
			String body = request1.header("cookie", Login.latest_cookie).send(req).body();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public String getnotify() {
		String body = new String();
		try {
			String pathUrl1 = "http://" + LoginMissile.Homeip + "/wsgi/account/getnotification/";
			HttpRequest request1 = HttpRequest.get(pathUrl1);
			body = request1.header("cookie", Login.latest_cookie).body();
			boolean result=AttachParameter.chechsuccess(body);
			if(result){
				String[] temp=body.split("&");
				body=temp[1].replace("method=", "");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return body;
	}
}
