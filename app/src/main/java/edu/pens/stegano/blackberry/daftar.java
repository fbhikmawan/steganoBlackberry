package edu.pens.stegano.blackberry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class daftar extends Activity {
	EditText un, pswd, nm, almt, eml, ntelp;
	Button daftar, reset;
	public static String username, password, nama, alamat, email, nomortelepon;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daftar);
        un = (EditText) findViewById(R.id.editTextDaftar1);
        pswd = (EditText) findViewById(R.id.editTextDaftar2);
        nm = (EditText) findViewById(R.id.editTextDaftar3);
        almt = (EditText) findViewById(R.id.editTextDaftar4);
        eml = (EditText) findViewById(R.id.editTextDaftar5);
        ntelp = (EditText) findViewById(R.id.editTextDaftar6);
        daftar = (Button) findViewById(R.id.buttonDaftar1);
        reset = (Button) findViewById(R.id.buttonDaftar2);
        daftar.setOnClickListener(new click01());
        reset.setOnClickListener(new click02());
    }
    
    class click01 implements Button.OnClickListener{
    	public void onClick(View v){
    		username = un.getText().toString();
    		password = pswd.getText().toString();
    		nama = nm.getText().toString();
    		alamat = almt.getText().toString();
    		email = eml.getText().toString();
    		nomortelepon = ntelp.getText().toString();
    		Intent i = new Intent (daftar.this,konfirm.class);
    		startActivity(i);
    	}
    }
    class click02 implements Button.OnClickListener{
    	public void onClick(View v){
    		un.setText("");
    		pswd.setText("");
    		nm.setText("");
    		almt.setText("");
    		eml.setText("");
    		ntelp.setText("");
    	}
    }
    class click03 implements Button.OnClickListener{
    	public void onClick(View v){
    		Intent i = new Intent (daftar.this,BlackberryActivity.class);
    		startActivity(i);
    	}
    }
}