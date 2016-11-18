package login.submit1;

import java.io.File;
import kissmediad2d.android.LoginMissile;
import android.os.Environment;

import com.github.kevinsawicki.http.HttpRequest;

public class Submit {
	  private String filename = new String();
	  public String token = new String();
	  private String loginreadLine, submitreadLine;
	  private static String requestString;
	  public CharSequence[] path;
	  public boolean LoginException;
	  public int  code;
	  public String source;
	  public Submit() {
	      requestString = new String();
	      loginreadLine = new String();
	      submitreadLine = new String();
	      LoginException = false;
	    }

	    public String getFilename() {
	        return filename;
	    }

	    public void setrequestString(String arg) {
	        requestString = arg;
	    }

	    // �W�Ǥ��e����alive���ʧ@�A���P�_�ثe�̪�server�b��
	    public String submit1(String arg_cookie) {
	        HttpRequest request = null;
	        try {

	            String pathUrl = "http://" + LoginMissile.Homeip + "/wsgi/cms/submit/";
	            request = HttpRequest.post(pathUrl);

	            //20160905�Ǫ����Q���
	            //token=request.header("cookie", Login.latest_cookie).send(requestString).body();

	            token=request.header("cookie", Login.latest_cookie).send(requestString).body();
	            // �o��requestString�Osubject�Bcontent�Breceiver�Bfilecnt�Blength�Bfile_name(�ܤ֤@��)
	        }catch (Exception ex) {
	            ex.printStackTrace();
	            token="timeout";
	            File output = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "KM/aa.html");
	            //token=request.body();
	            request.receive(output);
	        }
	        return token;

	    }

}
