package login.submit1;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import tab.list.AttachParameter;
import tab.list.FileContentProvider.UserSchema;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import com.github.kevinsawicki.http.HttpRequest;

public class Retrieve {
	/*
	 * ���h���D�n�֤ߤ��@�A�o�̬O��notify�ҩI�s���A �o��ʧ@�����o�ɮ�id�B�U���ɮסA�s���ɮ�
	 * �ɮ�id�����O�bserver�������ǡA�Y�n�U���ɮ׫h�����i�D���ɮצbserver����id����A���U��
	 */
	public String cookie = new String();

	public Retrieve() {
	}

	// public String aliveIp = new String();
	public String token = new String();
	public String filename = new String();
	public String savefilename = new String();
	public String fileid[];
	public String rreqreadLine = new String();
	public String[] retrieveFileCount;
	public String viewmod = "";
	public String sdcardPath = Environment.getExternalStorageDirectory().toString() + File.separator + "KM" + "/";

	  // �]�wtoken
    public void settoken(String arg) {
        token = "token=" + arg;

    }

    public void setcookie(String arg) {
        cookie = arg;

    }
    /*
     *�o��O���o�ɮ�id,�z�L�ǤJ��token(�ɮת�������)�H�Υثe�̪�id,�h�|�^�Ǹ�token�������ɮ�id�^�h
     * �|�b�I�ᰵ�������ɮת��ʧ@
      */
    public String[] retrieve_req(String token, String mod) {
        String reretrieve[] = new String[5];
        try {

            String pathUrl = "http://" + AttachParameter.Homeip + "/wsgi/cms/retrieve/";
            HttpRequest request = HttpRequest.post(pathUrl);
            // �]�wcookie�öǤJtoken
            int responseCode = request.header("cookie", Login.latest_cookie).send("token=" + token + "&mod=" + mod).code();
            // �p�Gresponse�^�ЬOOK
            if (HttpURLConnection.HTTP_OK == responseCode) {
                // �o��N�|���o��token�������D���B���e�B�ɮת�id���ɮצW��
                rreqreadLine = request.readTimeout(5000).body();
                //rreqreadLine��X���Gret=0&subject=�n�n&content=20150303_160637.mp4&file0_0=94&file0_1=95&20150303_160637.mp4&
                boolean result = AttachParameter.chechsuccess(rreqreadLine);
                // ��z�粒��rreqreadLine��Jreretrieve[0]
                reretrieve[1] = rreqreadLine;
                if (result){ // check ret=0

                    reretrieve[0] = "true";

					/*
					 * �p�G���\ �Aresponse�p�U
					 * ret=0&subject=heyhr&content=Afwfew&file0_0
					 * =127&file0_1=128
					 */
                    retrieveFileCount = rreqreadLine.split("&");
                    if (retrieveFileCount.length > 3) {
                        fileid = new String[retrieveFileCount.length - 3];
                        // �o�̧쪺�O����b��Ʈw����ID�A�Ҧpfile0_0=127�Bfile0_1=128
                        for (int j = 0; j < fileid.length; j++) {
                            fileid[j] = retrieveFileCount[j + 3].substring(retrieveFileCount[j + 3].indexOf("=") + 1, retrieveFileCount[j + 3].length());

                        }
                    }

                } else { /*
						 * 2013/03/09 4$ ��L,�ˬdreretrive[1]���S��matcher
						 * true��ret=0,false��ret=1
						 */

                    reretrieve[0] = "false";
                }
            }// end if
            else{
                reretrieve[0] = "server error";
                //server�o�ͤ������~
                reretrieve[2] = "true";
                reretrieve[1] =rreqreadLine = request.readTimeout(5000).body();
            }

        }// end try

        catch (HttpRequest.HttpRequestException nullpoint) // �P�_�b���K�X���~
        {
            reretrieve[0] = "server error";
            //
            reretrieve[3] = "true";
            // LoginNPE = false;
            nullpoint.printStackTrace();
        } catch (Exception ex) {
            reretrieve[0] = "server error";
            reretrieve[4] = "true";
            ex.printStackTrace();

        }

        return reretrieve;
    }

    public String[] retrieve_req_for_d2d(String ip, String port, String token) {
        String reretrieve[] = new String[5];
        try {

            String pathUrl = "http://" + ip+":"+port+ "/KM/?token="+token;
            HttpRequest request = HttpRequest.post(pathUrl);
            // �]�wcookie�öǤJtoken
            int responseCode = request.code();
            // �p�Gresponse�^�ЬOOK
            if (HttpURLConnection.HTTP_OK == responseCode) {
                // �o��N�|���o��token�������D���B���e�B�ɮת�id���ɮצW��
                rreqreadLine = request.readTimeout(5000).body();
                boolean result = AttachParameter.chechsuccess(rreqreadLine);
                // ��z�粒��rreqreadLine��Jreretrieve[0]
                if (result){ // check ret=0

                    // ��z�粒��rreqreadLine��Jreretrieve[0]
                    //�R���Ĥ@��"&"�H�e����r ex.ret=0&/storage/emulated/0/KM/file_name0_0-4.mp4&
                    rreqreadLine=rreqreadLine.substring(rreqreadLine.indexOf("&")+1, rreqreadLine.length());
                    //�M��/KM/���e���r��
                    String delete=rreqreadLine.substring(0, rreqreadLine.indexOf("/KM"));
                    //�R����쪺�r�� �u�d/KM/ �H�᪺�r��
                    rreqreadLine=rreqreadLine.replace(delete, "");
                    reretrieve[0] = "true";
                    reretrieve[1] = rreqreadLine;
                    fileid = rreqreadLine.split("&");

                }else{
                    reretrieve[0]="false";
                    reretrieve[1] = rreqreadLine;
                }

            }// end if
            else{
                reretrieve[0]="server error";
                reretrieve[2] ="true";
            }
        }// end try
        catch (HttpRequest.HttpRequestException nullpoint) // �P�_�b���K�X���~
        {
            reretrieve[0] = "server error";
            //
            reretrieve[3] = "true";
            // LoginNPE = false;
            nullpoint.printStackTrace();
        } catch (Exception ex) {
            reretrieve[0] = "server error";
            reretrieve[4] = "true";
            ex.printStackTrace();

        }

        return reretrieve;
    }

    // �o��O�O�u���U���ɮת�metohd�A�n�ǤJ���O�ثe��id�B�ɮ�id(��array�s��)�B�H�έnŪ��array������(�]�N�Oint i �o�ӰѼ�)
    public String[] saveBinaryFile(String token, int i) throws IOException {
        String[] refile = new String[5];
        //[0]�P�_�O�_���\�B[1]body�B[2]�t�ο��~�B[3]�ҥ~�B�z-Request�B[4]�ҥ~�B�z-�������~
        String body;
        try {
            String pathUrl = "http://" + AttachParameter.Homeip + "/wsgi/cms/retrieve_file/";
            HttpRequest request = HttpRequest.post(pathUrl);
			/*
			 * �o��ϥ�key value���覡�ǭȡA��M�]�O�i�H�ӶǲΪ��@�Ӧr�����ǰe����T��A�@�_�A���O��key value�覡�]�����\Ū
			 * data�̫�s���ȥi��p�U data=[token=h3b5bh6 id=127]
			 */
            Map<String, String> data = new HashMap<String, String>();
            token = token.replaceFirst("token=", "");
            data.put("token", token);
            data.put("id", fileid[i] + ";");
			/*
			 * �]���qserver�����o�쪺�ɮ׬Ozip�A�ҥH�b�o�ݭn�θ����Y���覡���ozip���ɮ� form(data)���
			 * �n�ǰe�����e�Ӧ�data�o��key value�C�p�G�Ϊ��O�r�ꪺ�覡�ǰe���ܡA��send()
			 * stream()��ܨ��o�Ӧ�server��inputstream
			 */
            request.connectTimeout(30000).header("cookie", Login.latest_cookie).form(data);
            if(request.code()==200){
                filename = request.header("X-Sendfile");
                if(filename!=null){

                    filename =request.header("X-Sendfile");
                    String delete=filename.substring(0,filename.indexOf("_-")+2);
                    filename=filename.replace(delete, "");
                    checkencode(filename);
                    boolean[] checktype = new boolean[AttachParameter.filetype];
                    checktype = AttachParameter.checktype(filename);

                    if (checktype[AttachParameter.video]||checktype[AttachParameter.photo]) {
                        filename = token+"_"+filename.replace(".", "-_" + String.valueOf(i) + ".");
                    }
                    File output1 = new File(AttachParameter.sdcardPath+filename);
                    if(!output1.exists()){
                        request.receive(output1);

                    }
                    refile[0]="true";
                }else{
                    body=request.body();
                    refile[0]="false";
                    refile[1]=body;
                }
            }
            else{
                refile[0]="server error";
                refile[2]="true";
                File output = new File(AttachParameter.sdcardPath+"error.html");
                request.receive(output);
            }
        } catch (HttpRequest.HttpRequestException httpex) {
            refile[0]="server error";
            refile[3] = "true";
            httpex.printStackTrace();
        }catch (Exception ex) {
            refile[0]="server error";
            refile[4] = "true";

            ex.printStackTrace();

        }
        return refile;
    }
    public String[] saveBinaryFile_for_d2d(String token, int i, ContentResolver content) throws IOException {
        String[] refile = new String[5];
        String etag, content_length, update_length = null;
        Long actual_length;
        int downLength = 0;
        int record_number=100000;//�C0.3M
        byte[] buffer = new byte[1024];
        int record_times=0;
        String num_record[];
        try {
            String path=fileid[i].substring(0,fileid[i].indexOf("/KM/")+4);
            String name=fileid[i].substring(fileid[i].indexOf("/KM/")+4,fileid[i].length());
            String pathUrl = "http://" + AttachParameter.connect_ip+":"+ AttachParameter.connect_port +path+ URLEncoder.encode(name, "utf-8").replaceAll("\\+", "%20");
            HttpRequest request = HttpRequest.get(pathUrl);
			/*
			 * �o��ϥ�key value���覡�ǭȡA��M�]�O�i�H�ӶǲΪ��@�Ӧr�����ǰe����T��A�@�_�A���O��key value�覡�]�����\Ū
			 * data�̫�s���ȥi��p�U data=[token=h3b5bh6 id=127]
			 */

			/*
			 * �]���qserver�����o�쪺�ɮ׬Ozip�A�ҥH�b�o�ݭn�θ����Y���覡���ozip���ɮ� form(data)���
			 * �n�ǰe�����e�Ӧ�data�o��key value�C�p�G�Ϊ��O�r�ꪺ�覡�ǰe���ܡA��send()
			 * stream()��ܨ��o�Ӧ�server��inputstream
			 */
            if(request.code() == 200){
                //20160905�[�^�Ǫ�����
                etag = request.header(request.HEADER_ETAG);
                content_length = request.header(request.HEADER_CONTENT_LENGTH);
                //donotthing
                String response =request.body();

                refile[0]="true";
                filename =fileid[i];
                refile[1]=filename;
                String delete=filename.substring(0,filename.indexOf(String.valueOf(i)+"-")+2);
                savefilename=filename.replace(delete, "");
                filename=savefilename;
                checkencode(savefilename);
                boolean[] checktype = new boolean[AttachParameter.filetype];
                checktype = AttachParameter.checktype(savefilename);

                //20160903���Ϊ��[�Jchecktype[AttachParameter.photo](token)
                if (checktype[AttachParameter.video]||checktype[AttachParameter.photo]) {
                    savefilename = token+"_"+savefilename.replace(".", "-_" + String.valueOf(i) + ".");
                }
//                File output1 = new File(sdcardPath_2+savefilename);
                File output1 = new File(AttachParameter.sdcardPath+savefilename);
                //====20160907�Ǫ����Χ��A���if�n�h��"!"
                if(output1.exists()){
                    //===============20160905�[�^�Ǫ�����============//
                    actual_length = output1.length();
                    if (actual_length.equals(Long.valueOf(content_length))){
                        //�N����ɮפw�g����U���A���L
                    }else{
                        //���X�o�@�����U��������
                        Cursor get_length_record = content.query(Uri.parse("content://tab.list.d2d/user_data"), new String[]{UserSchema._LENGTH_RECORD}, "messagetoken='"+token+"'", null, null);

                        if(get_length_record.getCount()>0){
                            get_length_record.moveToFirst();
                            if(get_length_record.getString(0)!=null){
                                num_record=get_length_record.getString(0).split("&");
                                String length_record[]=num_record[i].split("=");
                                downLength= Integer.valueOf(length_record[1]);
                            }else{
                                downLength=0;
                            }

                        }
                        get_length_record.close();

                        request = HttpRequest.get(pathUrl);
                        request .header("Referer","http://" + AttachParameter.connect_ip+":"+AttachParameter.connect_port)
                                .header("Range", "bytes="+downLength+"-"+ String.valueOf((Integer.valueOf(content_length)-1)))
                                .header("Connection","Keep-Alive")
                                .header("If-Range",etag)
                                .header("Content-Length",content_length)
                                .header("referer",pathUrl);

                        InputStream is = new BufferedInputStream(request.buffer());

                        RandomAccessFile threadfile = new RandomAccessFile(output1, "rwd");
                        threadfile.seek(downLength);

                        int offset = 0;

                        while ((offset = is.read(buffer, 0, 1024)) != -1) {
                            threadfile.write(buffer, 0, offset);
                            downLength += offset;

                            //�C�@���������I
                            if((record_number*record_times)<=downLength&&downLength<(record_number*(record_times+1))){
                                Cursor update_downrecord = content.query(Uri.parse("content://tab.list.d2d/user_data"), new String[]{UserSchema._ID, UserSchema._LENGTH_RECORD}, "messagetoken='" + token + "'", null, null);
                                if (update_downrecord.getCount() > 0) {
                                    update_downrecord.moveToFirst();
                                    //�ˬd�L�h�O�_�������L�A�n�N�����i���s(�Ҧp�C500�B1000�B1500������)
                                    if(update_downrecord.getString(1)!=null){
                                        //���άO�]���i�व�ѥi��O����2���ɮ�(file0_0�Pfile0_1)���U������,�ҥH���o�䤤�@������.ex. file0_0=XXX
                                        num_record=update_downrecord.getString(1).split("&");
                                        if(i==0){//�N��o��O�Ĥ@�����ɮת�length�n��s
                                            update_length="file0_0="+record_number*record_times+"&"+num_record[1];
                                        }else if(i==1){//�N��o��O�ĤG�����ɮת�length�n��s
                                            update_length=num_record[0]+"&file0_1="+record_number*record_times+"&";
                                        }

                                    }else{
                                        //�o�K�n�O���Ĥ@�����
                                        update_length="file0_0="+record_number*record_times+"&";
                                    }

                                    ContentValues values = new ContentValues();
                                    values.put(UserSchema._LENGTH_RECORD, "&"+update_length);
                                    int id_this = Integer.parseInt(update_downrecord.getString(0));
                                    String where = UserSchema._ID + " = " + id_this;
                                    content.update(Uri.parse("content://tab.list.d2d/user_data"), values, where, null);
                                    values = null;

                                }
                                update_downrecord.close();
                                record_times=record_times+1;
                            }
                        }
                        response =request.body();
                        threadfile.close();
                        is.close();
                    }
                }else {
                    //�ɮץ��Q�U���L,���i��o�@���N�O����

                    request = HttpRequest.get(pathUrl);
                    request.header("Referer","http://" + AttachParameter.connect_ip+":"+AttachParameter.connect_port+"/")
                            .header("Range", "bytes="+downLength+"-"+ String.valueOf((Integer.valueOf(content_length)-1)))
                            .header("Connection","Keep-Alive")
                            .header("If-Range",etag)
                            .header("referer",pathUrl);


                    BufferedInputStream is = new BufferedInputStream(request.buffer());

                    RandomAccessFile threadfile = new RandomAccessFile(
                            output1, "rwd");
                    threadfile.seek(downLength);

                    int offset = 0;

                    //while ((offset = is.read(buffer)!=-1){
                    while ((offset = is.read(buffer, 0, 1024)) != -1) {
                        threadfile.write(buffer, 0, offset);
                        downLength += offset;
                        if(downLength>270781){
                            System.out.println("5");
                        }

                        //�C�@���������I
                        if((record_number*record_times)<=downLength&&downLength<(record_number*(record_times+1))){
                            Cursor update_downrecord = content.query(Uri.parse("content://tab.list.d2d/user_data"), new String[]{UserSchema._ID, UserSchema._LENGTH_RECORD}, "messagetoken='" + token + "'", null, null);
                            if (update_downrecord.getCount() > 0) {
                                update_downrecord.moveToFirst();
                                //�ˬd�L�h�O�_�������L�A�n�N�����i���s(�Ҧp�C500�B1000�B1500������)
                                if(update_downrecord.getString(1)!=null){
                                    //���άO�]���i�व�ѥi��O����2���ɮ�(file0_0�Pfile0_1)���U������,�ҥH���o�䤤�@������.ex. file0_0=XXX
                                    num_record=update_downrecord.getString(1).split("&");
                                    if(i==0){//�N��o��O�Ĥ@�����ɮת�length�n��s
                                        update_length="file0_0="+record_number*record_times+"&"+num_record[1];
                                    }else if(i==1){//�N��o��O�ĤG�����ɮת�length�n��s
                                        update_length=num_record[0]+"&file0_1="+record_number*record_times+"&";
                                    }

                                }else{
                                    //�o�K�n�O���Ĥ@�����
                                    update_length="file0_0="+record_number*record_times+"&";
                                }

                                ContentValues values = new ContentValues();
                                values.put(UserSchema._LENGTH_RECORD, update_length);
                                int id_this = Integer.parseInt(update_downrecord.getString(0));
                                String where = UserSchema._ID + " = " + id_this;
                                content.update(Uri.parse("content://tab.list.d2d/user_data"), values, where, null);
                                values = null;

                            }
                            update_downrecord.close();
                            record_times=record_times+1;
                        }
                    }
                    response =request.body();
                    threadfile.close();
                    is.close();
                    //===============20160905�[�^�Ǫ�����============//
                }
            }
            else if (request.code()==404){
                refile[0]="false";
                refile[1]=request.body();
            }else{
                refile[0]="server error";
                refile[2]=request.body();
                File output = new File(AttachParameter.sdcardPath+"error.html");
                request.receive(output);
            }
        }catch (HttpRequest.HttpRequestException httpex) {
            refile[0]="server error";
            refile[3] = "true";
            httpex.printStackTrace();
        }catch (Exception ex) {
            refile[0]="server error";
            refile[4] = "true";

            ex.printStackTrace();

        }
        return refile;
    }

    public void checkencode(String datastr){
        try {
            if(datastr.equals(new String(datastr.getBytes("iso8859-1"), "iso8859-1"))){
                filename=new String(datastr.getBytes("iso8859-1"),"utf-8");
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public String check_file(String localtoken) {
        String reretrieve = new String();
        token = localtoken;
        try {
            String pathUrl = "http://" + AttachParameter.Homeip + "/wsgi/cms/check_file/?token="+token;

            HttpRequest request = HttpRequest.get(pathUrl);
            // �]�wcookie�öǤJtoken
            int responseCode = request.header("cookie", Login.latest_cookie).code();
            // �p�Gresponse�^�ЬOOK
            if (HttpURLConnection.HTTP_OK == responseCode) {
                // �o��N�|���o��token�������D���B���e�B�ɮת�id���ɮצW��
                reretrieve = request.body();

            }// end if
            else {
                reretrieve = request.body();
            }

        }// end try
        catch (NullPointerException nullpoint) // �P�_�b���K�X���~
        {
            nullpoint.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("retrieve_req failed");
        }

        return reretrieve;
    }
    public String check_file_for_m2m(String address, String token) {
        String reretrieve = new String();
        try {
            String pathUrl = "http://" + address + "/KM/?token="+token;

            HttpRequest request = HttpRequest.get(pathUrl);
            // �]�wcookie�öǤJtoken
            int responseCode = request.code();
            // �p�Gresponse�^�ЬOOK
            if (HttpURLConnection.HTTP_OK == responseCode) {
                // �o��N�|���o��token�������D���B���e�B�ɮת�id���ɮצW��
                reretrieve = request.body();

            }// end if
            else {
                reretrieve = request.body();
            }

        }// end try
        catch (NullPointerException nullpoint) // �P�_�b���K�X���~
        {
            nullpoint.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("retrieve_req failed");
        }

        return reretrieve;
    }
}
