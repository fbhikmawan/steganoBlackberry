package edu.pens.stegano.blackberry;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.pens.stegano.interkoneksi.CustomHttpClient;

public class BlackberryActivity extends Activity implements OnClickListener{
	EditText un, pswd;
	Button sign, reset, daftar;
	public static String mstrun;
	public static String mstrpass;
	public static final int banyakspread = 4;
	public static String URL = "http://192.168.43.88:8080/blackberryStore/";
	public static String daftarbelanja = URL + "daftarbelanja.jsp";
	public static String authlogin = URL + "authlogin.jsp";
	public static String isidatabase = URL + "isidatabase.jsp";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        un = (EditText) findViewById(R.id.EditText1);
        pswd = (EditText) findViewById(R.id.EditText2);
        sign = (Button) findViewById(R.id.btSign);
        reset = (Button) findViewById(R.id.btReset);
        daftar = (Button) findViewById(R.id.btDaftar);
        sign.setOnClickListener(this);
        reset.setOnClickListener(this);
        daftar.setOnClickListener(this);
    }
    
    public void onClick(View v){
    	switch (v.getId()){
    	case R.id.btSign:
    		String strun = un.getText().toString();
    		String strpass = pswd.getText().toString();
    		String md5passKK = md5(strpass);
    		mstrun = strun;
    		mstrpass = strpass;
    		
    		long startauth = System.nanoTime();
    		String status = lakukanlogin(strun, strpass, md5passKK);
    		long stopauth = System.nanoTime();
    		Log.v("Lama Autentifikasi (ns)", "" + (stopauth - startauth));
    		
    		if(status.equals("1")){
    			un.setText("");
    	    	pswd.setText("");
    			Intent i = new Intent (BlackberryActivity.this,menuawal.class); 
        		startActivity(i);
    		}
    	    else {
    	    	Toast.makeText(getBaseContext(), "Login yg anda masukkan salah, coba lagi", Toast.LENGTH_LONG).show();
    	    	un.setText("");
    	    	pswd.setText("");
    	    }
    		break;
    	case R.id.btReset:
    		un.setText("");
    		pswd.setText("");
    		break;
    	case R.id.btDaftar:
    		Intent i = new Intent (BlackberryActivity.this,daftar.class);
    		startActivity(i);
    		break;
    	default:
    		break;
    	}
    }
    
    public String lakukanlogin(String strun, String strpass, String strmd5pass){
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
    	postParameters.add(new BasicNameValuePair("username", strun));
    	postParameters.add(new BasicNameValuePair("md5password", strmd5pass));

    	String response = null;
    	try {
    	    response = CustomHttpClient.executeHttpPost(authlogin, postParameters);
    	    String res=response.toString();
    	    res= res.replaceAll("\\s+","");
    	    return res;
    	} catch (Exception e) {
    		return e.toString();
    	}
    }
    
    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
     
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();
     
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}