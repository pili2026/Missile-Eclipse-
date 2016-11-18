package login.submit1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kissmediad2d.android.LoginMissile;
import tab.list.AttachParameter;
import android.widget.Toast;

import com.github.kevinsawicki.http.HttpRequest;

public class regFromServer {
    /*
     * �o��Oregister.java�ҩI�s���A�o�|�N�ϥΪ̩Ҷ�n�����U��ưe��server�إ� �إߧ���ݭn�z�Lemail���覡���Ҥ~�ॿ���ҥαb��
     */

    public regFromServer() {
    }

    public String regist(String requestString) {

        String regreturn = null;
        try {

            String pathUrl = "http://" +  AttachParameter.Homeip + "/wsgi/account/register/";
            HttpRequest request = HttpRequest.post(pathUrl);
            String response = request.connectTimeout(10000).send(requestString).body();

            if (request.ok()) {
                regreturn = response;
                // �]�w���W�Хܪk

            }else{
                regreturn="ret=1";
            }
        } catch (Exception ex) {

            ex.printStackTrace();
            regreturn="ret=1";

        }
        // regreturn[0]verifycode [1]�����[2]�S���[3][4]�o�ͨҥ~
        return regreturn;
    }

    // �o���O�ΨӰeemail����k
    public String[] sendmail(String one) { // ��ǤJ��ONE�r��i����ΡA�̷�&�Ӱ����Ψé�J�x�}��
        final String[] validate = one.split("&");
        final String[] conreturn = new String[4];

        int Conntimeout = 15000;
        try {
            // �]�wmail�D��
            String subject = "welcome to kissmedia";
            // �]�wmail���e
            String message = "http://"+ AttachParameter.Homeip+"/wsgi/account/validate/?" + validate[1];
            // �]�wmail�������
            String to_email = validate[3];
            String requestString = "subject=" + subject + "&message=" + message + "&to_email=" + to_email;
            String pathUrl = "http://"+ AttachParameter.Homeip+":8000/cms/send_email/";
            HttpRequest request = HttpRequest.post(pathUrl);
            String response = request.send(requestString).body();
            if (request.ok()) {
                Pattern pattern = Pattern.compile("ret=0.*"); // check file type
                Matcher matcher = pattern.matcher(response);
                if (matcher.find()) {// �p�G�����ret=0
                    conreturn[0] = "true";

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return conreturn;
    }

    public String validate(String requestString) {

        String regreturn = null;
        try {

            String pathUrl = "http://" +  AttachParameter.Homeip + "/wsgi/account/validate/";
            String req="validate="+requestString+"&";
            HttpRequest request = HttpRequest.get(pathUrl);
            String response = request.connectTimeout(10000).send(req).body();

            if (request.ok()) {
                regreturn = response;
                // �]�w���W�Хܪk

            }else{
                regreturn="ret=1";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // regreturn[0]verifycode [1]�����[2]�S���[3][4]�o�ͨҥ~
        return regreturn;
    }

}
