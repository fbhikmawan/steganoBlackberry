package edu.pens.stegano.blackberry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;


public class menu extends Activity implements OnClickListener{
	boolean[] item = new boolean[5];
	public static int totalBayar = 0;
	CheckBox C9900, C9790, C9360, C9300, C8520;
	Button bayar, home;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        C9900 = (CheckBox) findViewById (R.id.cbC9900);
        C9790 = (CheckBox) findViewById (R.id.cbC9790);
        C9360 = (CheckBox) findViewById (R.id.cbC9360);
        C9300 = (CheckBox) findViewById (R.id.cbC9300);
        C8520 = (CheckBox) findViewById (R.id.cbC8520);
        bayar = (Button) findViewById(R.id.btBayar);
        home = (Button) findViewById(R.id.btHome);
        C9900.setOnClickListener(this);
        C9790.setOnClickListener(this);
        C9360.setOnClickListener(this);
        C9300.setOnClickListener(this);
        C8520.setOnClickListener(this);
        bayar.setOnClickListener(this);
        home.setOnClickListener(this);
        
        for(int i=0; i < item.length; i++){
        	item[i] = false;
        }
    }
    
    public void onClick(View v){
    	switch (v.getId()){
    	case R.id.cbC9900:
    		if (((CheckBox)v).isChecked())   			
    			item[0] = true;
    		else
    			item[0] = false;
    		break;
    	case R.id.cbC9790:
    		if (((CheckBox)v).isChecked())   			
    			item[1] = true;
    		else
    			item[1] = false;
    		break;
    	case R.id.cbC9360:
    		if (((CheckBox)v).isChecked())   			
    			item[2] = true;
    		else
    			item[2] = false;
    		break;
    	case R.id.cbC9300:
    		if (((CheckBox)v).isChecked())   			
    			item[3] = true;
    		else
    			item[3] = false;
    		break;
    	case R.id.cbC8520:
    		if (((CheckBox)v).isChecked())   			
    			item[4] = true;
    		else
    			item[4] = false;
    		break;
    	case R.id.btBayar:
    		doTotalBayar();
    		Intent ibayar = new Intent (menu.this,pembayaran.class);
    		startActivity(ibayar);
    		finish();
    		break;
    	case R.id.btHome:
    		Intent ihome = new Intent (menu.this,menuawal.class);
    		startActivity(ihome);
    		break;
    	default:
    		break;
    	}
    }
    
    	public void doTotalBayar(){
    		totalBayar=0;
    		for(int i=0; i<item.length;i++){
    			switch (i){
    	    	case 0:
    	    		if(item[i]) totalBayar=totalBayar+5500000; 
   	    			break;
    	    	case 1:
    	    		if(item[i]) totalBayar=totalBayar+3999000; 
   	    			break;
    	    	case 2:
    	    		if(item[i]) totalBayar=totalBayar+3389000; 
   	    			break;
    	    	case 3:
    	    		if(item[i]) totalBayar=totalBayar+2399000; 
   	    			break;
    	    	case 4:
    	    		if(item[i]) totalBayar=totalBayar+1800000; 
   	    			break;
    	    	default:
    	    		break;
    			}
    		}
    	}
}