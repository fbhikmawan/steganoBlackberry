package edu.pens.stegano.blackberry;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.pens.stegano.interkoneksi.CustomHttpClient;

public class konfirm extends Activity{
	TextView kmun, kmpswd, kmnm, kmalmt, kmeml, kmntelp;
	Button lanjut;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.konfirm);
    	kmun = (TextView) findViewById (R.id.textViewKonfirm3);
    	kmpswd = (TextView) findViewById (R.id.textViewKonfirm5);
    	kmnm = (TextView) findViewById (R.id.textViewKonfirm7);
    	kmalmt = (TextView) findViewById (R.id.textViewKonfirm9);
    	kmeml = (TextView) findViewById (R.id.textViewKonfirm11);
    	kmntelp = (TextView) findViewById (R.id.textViewKonfirm13);
    	kmun.setText(daftar.username);
    	kmpswd.setText(daftar.password);
    	kmnm.setText(daftar.nama);
    	kmalmt.setText(daftar.alamat);
    	kmeml.setText(daftar.email);
    	kmntelp.setText(daftar.nomortelepon);
    	lanjut = (Button) findViewById(R.id.buttonKonfirm1);
    	lanjut.setOnClickListener(new click06());
    }
    
    class click06 implements Button.OnClickListener{
    	public void onClick(View v){
    		String status = lakukanisidata();
    		if(status.equals("1")){
    			Toast.makeText(getBaseContext(), "Anda telah terdaftar, silahkan login",Toast.LENGTH_LONG).show();
    			Intent i = new Intent (konfirm.this,BlackberryActivity.class);
    			finish();
        		startActivity(i);
    		} else {
    			Toast.makeText(getBaseContext(), "Mohon maaf, terjadi kesalahan sistem. Silahkan Ulangi",Toast.LENGTH_LONG).show();
    			Intent i = new Intent (konfirm.this, daftar.class);
        		finish();
        		startActivity(i);
    		}	
    	}
    }
    
    public String lakukanisidata(){
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
    	postParameters.add(new BasicNameValuePair("username", daftar.username));
    	postParameters.add(new BasicNameValuePair("password", daftar.password));
    	postParameters.add(new BasicNameValuePair("nama", daftar.nama));
    	postParameters.add(new BasicNameValuePair("alamat", daftar.alamat));
    	postParameters.add(new BasicNameValuePair("email", daftar.email));
    	postParameters.add(new BasicNameValuePair("nomortelepon", daftar.nomortelepon));
    	String res = null;
    	String response = null;
    	try {
    	    response = CustomHttpClient.executeHttpPost(BlackberryActivity.isidatabase, postParameters);
    	    res=response.toString();
    	    res= res.replaceAll("\\s+","");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return res;
    }
}