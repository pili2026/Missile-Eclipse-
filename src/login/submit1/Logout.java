package login.submit1;

import java.io.IOException;

import tab.list.AttachParameter;

import kissmediad2d.android.LoginMissile;

import com.github.kevinsawicki.http.HttpRequest;
import com.github.kevinsawicki.http.HttpRequest.HttpRequestException;

public class Logout {
    /*
     * �o��tab.java�ҩI�s�A�Ψӵn�X��
     */

    String[] tab;
    // public String homeIp ;
    // public String requestString = new String();
    public String logout_cookie;
    String homeIp, cookie;

    // retrieve retrieve = new retrieve();
    // 2014/4/2 4$ �s�W readfile_coolie,Alive

    public Logout() {
	cookie = new String();
    }

    public void logout_start() {

	try {
	    Thread thread = new Thread() {
		public void run() {

		    //�����o�Ӧ�login�ɩҬ����U��cookie
		    //�����ǤJcookie�A��server��ϥΪ̵n�X
		    String pathUrl = "http://" +  LoginMissile.Homeip + "/wsgi/account/logout/";
		    HttpRequest request = HttpRequest.get(pathUrl);
		    String body = request.header("cookie", Login.latest_cookie).body();

		}
	    };
	    thread.start();

	} catch (HttpRequestException httpex) {
			System.out.println("�A�S������");
	}catch (Exception ex) {
	    ex.printStackTrace();
	}

    }
}
