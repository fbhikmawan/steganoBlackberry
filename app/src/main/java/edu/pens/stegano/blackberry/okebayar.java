package edu.pens.stegano.blackberry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class okebayar extends Activity{
	private Button bmenuutama, blogout;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.okebayar);
        bmenuutama = (Button) findViewById (R.id.bmenuutama);
        bmenuutama.setOnClickListener(new kembali());
        blogout = (Button) findViewById (R.id.blogout);
        blogout.setOnClickListener(new logout());
    }
    
    class kembali implements Button.OnClickListener{
    	public void onClick(View v){
    		Intent i = new Intent (okebayar.this,menuawal.class);
			finish();
			startActivity(i);
    	}
    }
    
    class logout implements Button.OnClickListener{
    	public void onClick(View v){
    		Intent i = new Intent (okebayar.this,BlackberryActivity.class);
			finish();
			startActivity(i);
    	}
    }
}
    