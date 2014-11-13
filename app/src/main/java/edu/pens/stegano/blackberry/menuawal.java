package edu.pens.stegano.blackberry;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class menuawal extends Activity implements OnClickListener{
	Button produk, toko, servis, tips, konsul, logout;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuawal);
        produk = (Button) findViewById(R.id.btProduk);
        toko = (Button) findViewById(R.id.btTokoKami);
        servis = (Button) findViewById(R.id.btServis);
        tips = (Button) findViewById(R.id.btTips);
        konsul = (Button) findViewById(R.id.btKonsultasi);
        logout = (Button) findViewById(R.id.btLogout);
        produk.setOnClickListener(this);
        toko.setOnClickListener(this);
        logout.setOnClickListener(this);
/*      servis.setOnClickListener(new click03());
        tips.setOnClickListener(new click04());
        konsul.setOnClickListener(new click05());*/
    }
    
    public void onClick(View v){
    	switch (v.getId()){
    	case R.id.btProduk:
    		Intent iproduk = new Intent (menuawal.this,menu.class);
    		//Intent iproduk = new Intent (menuawal.this,listproduk.class);
    		startActivity(iproduk);
    		break;
    	case R.id.btTokoKami:
    		Intent itokokami = new Intent (menuawal.this,toko.class);
    		startActivity(itokokami);
    		break;
    	case R.id.btLogout:
    		Intent ilogout = new Intent (menuawal.this,BlackberryActivity.class);
    		BlackberryActivity.mstrun = null;
    		BlackberryActivity.mstrpass = null;
    		startActivity(ilogout);
    		break;
    	default:
    		break;
    	}
    }
}