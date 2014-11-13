package edu.pens.stegano.blackberry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class toko extends Activity {
	Button home, next;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toko);
        home = (Button) findViewById(R.id.buttonTokoSatu);
        next = (Button) findViewById(R.id.buttonTokoDua);
        home.setOnClickListener(new click01());
  //      next.setOnClickListener(new click02());
    }
    class click01 implements Button.OnClickListener{
    	public void onClick(View v){
    		Intent i = new Intent (toko.this,menuawal.class);
    		startActivity(i);
    	}
    }
 /*   class click02 implements Button.OnClickListener{
    	public void onClick(View v){
    		Intent i = new Intent (toko.this,tokodua.class);
    		finish();
    		startActivity(i);
    	}
    }*/
}