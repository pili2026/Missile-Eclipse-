package login.submit1;

import tab.list.AttachParameter;

import com.github.kevinsawicki.http.HttpRequest;
import com.github.kevinsawicki.http.HttpRequest.HttpRequestException;

public class Login {
	/*
	 * �o��O�ΰ�loginactivity�ҥΪ�class�A�ت����n�J
	 */

	public String homeIp;
	// public String requestString = new String();
	public String cookie;
	public static String latest_cookie;
	// static String latest_name;
	// retrieve retrieve = new retrieve();
	private String loginreadLine;
	// 2014/4/2 4$ �s�W readfile_coolie,Alive
	public String login_name = new String();

	public Login() {
		homeIp = new String();
		cookie = new String();
	}

	public void set_alive_arg(String a_cookie, String a_name) {
		cookie = a_cookie;
		login_name = a_name;
	}

	public String getcookie() {

		return cookie;
	}

	public String gethomeIp() {
		return homeIp;
	}

	public String[] login(String aliveIp, String requestString) {

		homeIp = aliveIp;
		String[] loginreturn = new String[3];// �s��^�Ǹ�T

		try {
			String pathUrl = "http://" + aliveIp + "/wsgi/account/login/";
			HttpRequest request = HttpRequest.post(pathUrl);
			// request�r��񪺬O�b�K�Asend�X�h����^cookie�A�������Pserver�s�u�ɡA�T�O���O�P�@��������
			loginreadLine = request.connectTimeout(20000).send(requestString).body();
			// ���response�r��A��0�}�l��A��";"�e������
			if(request.code()==java.net.HttpURLConnection.HTTP_OK){
				// request�r��񪺬O�b�K�Asend�X�h����^cookie�A�������Pserver�s�u�ɡA�T�O���O�P�@��������
				
				boolean result = AttachParameter.chechsuccess(loginreadLine);
				if (result) {
					String response = request.connectTimeout(10000).header("set-cookie");
					cookie = response.substring(0, response.indexOf(";"));
					//����cookie
					latest_cookie = cookie;
					loginreturn[0] = "true"; // control �n�J�ɪ�pialog
					loginreturn[1] = loginreadLine; // put the response into the
					// array[1]
				}else{
					loginreturn[0] = "true"; // control �n�J�ɪ�pialog
					loginreturn[1] = loginreadLine; // put the response into the
					// array[1]
				}
			}

			
		}// try end
		catch (HttpRequestException httpex) {
			loginreturn[0] = "false";
			loginreturn[2] = "false";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return loginreturn;
	}// login end

}
