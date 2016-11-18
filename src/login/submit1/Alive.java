package login.submit1;

import java.net.URLDecoder;

import kissmediad2d.android.LoginMissile;
import tab.list.AttachParameter;

import com.github.kevinsawicki.http.HttpRequest;

public class Alive {
    /*
     * ���h�@�ߩI�s�ĤT���Ʈw�AHttpRequest�C
     * ��class�Ψ��ˬd�ثe��client�̪�server�A�Y�����server�h�|�o���server��ip
     */

    // login�w�֦�homeIp & cookie��T
    String aliveIp = LoginMissile.Homeip;// �w�]aliveIp�OHomeServer��IP
    // 2013/3/10,4$ �ק� private String aliveRes
    private String aliveRes;

    public Alive() {

    }

    public String[] alive(String cookie,String ip,int portnumber) {
	String[] aliveres = new String[5];
	try {
		String requeststring= ip+"&port="+portnumber;
		
	    String pathUrl = "http://" + aliveIp + "/wsgi/loc/alive/";
	    // �ϥ�get����k
	    HttpRequest request = HttpRequest.post(pathUrl);
	    // �]�wcookie�è��^body����T
	    String alive = request.connectTimeout(10000).header("cookie", cookie).send(requeststring).body();
	    System.out.println(alive);
	    boolean result = AttachParameter.chechsuccess(alive);
	    if (result) { // ���Tret=0
		aliveres[1] = "true";
		// �ѽXutf8���榡
		alive = URLDecoder.decode(alive, "utf-8");
		alive = URLDecoder.decode(alive, "utf-8");
		// �ѪR�X�ӬOip
		aliveRes = alive.substring(alive.indexOf("ip=") + 3, alive.length() - 1);
		boolean[] checktype = new boolean[AttachParameter.msgtype];
		checktype = AttachParameter.checkCR(aliveRes);
		if(checktype[AttachParameter.content]){
		    ip=aliveRes.substring(0, aliveRes.indexOf("&content"));
		}
		else if(checktype[AttachParameter.reply]){
		    ip=aliveRes.substring(0, aliveRes.indexOf("&reply"));
		}
		else if(checktype[AttachParameter.d2d]){
		    ip=aliveRes.substring(0, aliveRes.indexOf("&d2d"));
		}
		else{
		    ip=aliveRes;
		}
		// �p�G����Server IP�M�쥻��aliveIp���@�˪��ܷ|�Q�񪺨��N�A�ӥB�|���s�n�J
		if (!ip.equalsIgnoreCase(aliveIp)) {

		    aliveIp = ip;
		    // �x�s�^�Ъ�ip
		    aliveres[0] = aliveIp;
		    aliveres[3] = "false";
		    // 2013/4/2 4$�ק�
		    // �I�s�W�h,���j�骺���D,�Y�O�nlogin>alive>lgoin
		    // ���orequestString
		} else// 2013/4/2 4$�ק� else�N��ip���Χ󴫡A�J�M���Χ󴫴N���ΦA���n�J
		{
		    aliveres[0] = aliveIp;
		    aliveres[3] = "true";
		}
		aliveres[4]=aliveRes;
	    } else{
			aliveres[1] = "false";
	    }
		// �����Tret=1

	} catch (Exception ex) {
		aliveres[1] = "false";
	    aliveres[2] = "false";
	    ex.printStackTrace();
	}

	return aliveres;
    }
}
