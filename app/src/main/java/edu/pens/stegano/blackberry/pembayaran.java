package edu.pens.stegano.blackberry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.pens.stegano.alg.LSB2bit;


public class pembayaran extends Activity{
	private Bitmap sourceBitmap;
	private TextView TotalBayar;
	private Button BayarSekarang, Batal;
	private EditText etnokk, etpasskk;
	private String END_MESSAGE_COSTANT = "#!@";
	private String START_MESSAGE_COSTANT = "@!#";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pembayaran);
        TotalBayar = (TextView) findViewById (R.id.tvTotalBayar);
        etnokk = (EditText) findViewById(R.id.etNomor);
		etpasskk = (EditText) findViewById(R.id.etPswd);
        BayarSekarang = (Button) findViewById (R.id.btBayarSekarang);
        Batal = (Button) findViewById (R.id.btBatal);
        TotalBayar.setText("Rp"+menu.totalBayar+",00");
        
        BayarSekarang.setOnClickListener(new doBayar());
        Batal.setOnClickListener(new doBatal());
    }
    
    class doBayar implements Button.OnClickListener{
    	public void onClick(View v){
    		String noKK = etnokk.getText().toString();
    		String passKK = etpasskk.getText().toString();
    		
    		boolean isValid = false;
    		isValid = cekValiditasKartu(noKK);
    		
    		if(isValid){
    			try {
    				long start = System.nanoTime();
	    			loadImage();
	    			String destpath = embed(noKK, passKK);
	    			long stop = System.nanoTime();
	    			Log.v("Lama Eksekusi Penyisipan Client (ns)", "" + (stop - start));
	    			
	    			kirimkeserver(destpath);
	    			long stop2 = System.nanoTime();
	    			Log.v("Lama Eksekusi Keseluruhan sistem (ns)", "" + (stop2 - start));
	    			Toast.makeText(getBaseContext(), "SSStego Berhasil",Toast.LENGTH_LONG).show();
	    			Intent i = new Intent (pembayaran.this,okebayar.class);
	        		startActivity(i);
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}
    		} else {
    			Toast.makeText(getBaseContext(), "Nomor Kartu Kredit tidak Valid, silahkan ulangi lagi",Toast.LENGTH_SHORT).show(); 
    			etnokk.setText("");
    			etpasskk.setText("");
    		}
    	}
    }
    
    class doBatal implements Button.OnClickListener{
    	public void onClick(View v){
    		Intent ibatal = new Intent (pembayaran.this,menuawal.class); 
    		finish();
    		startActivity(ibatal);
    	}
    }
    
    public void loadImage(){
    	try {
    		Resources res = getResources();
			sourceBitmap = BitmapFactory.decodeResource(res, R.drawable.cross);
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    public String embed(String kartukredit, String password) {
		Uri result=null;
		String msg = kartukredit+ "&&&" +password;
		String key = BlackberryActivity.mstrpass;
		//String key = "brahim";
		msg += END_MESSAGE_COSTANT;
		msg = START_MESSAGE_COSTANT + msg;
		
		///// hitung dimensi pixel yang hanya dibutuhkan saja /////
		double bthpixel = msg.length()*8*BlackberryActivity.banyakspread/3;
		int maxwidth = (int) Math.ceil(Math.sqrt(bthpixel)); 
		int maxheight = maxwidth;
		///////////////////////////////////
		
		int width = sourceBitmap.getWidth();
		int height = sourceBitmap.getHeight();
		int[] oneD = new int[width * height];
		sourceBitmap.getPixels(oneD, 0, width, 0, 0, width, height); //oneD int array berisi ARGB tiap pixel
		int density=sourceBitmap.getDensity();
		sourceBitmap.recycle();
		
		/*
		//////  hasilkan dimensi gambar terstego hanya yang dibutuhkan saja //////////////
		byte[] byteImage = LSB2bit.embedMessage(oneD, width, maxwidth, height, maxheight, msg, key);
		oneD=null;
		sourceBitmap=null;
		int[] oneDMod = LSB2bit.byteArrayToIntArray(byteImage);
		byteImage=null;
		Bitmap destBitmap = Bitmap.createBitmap(maxwidth, maxheight, Config.ARGB_8888);
		destBitmap.setDensity(density);
		int masterIndex = 0;
		for (int j = 0; j < maxheight; j++){
			for (int i = 0; i < maxwidth; i++){
				// The unique way to write correctly the sourceBitmap, android bug!!!
				destBitmap.setPixel(i, j, Color.argb(0xFF,
						oneDMod[masterIndex] >> 16 & 0xFF,
						oneDMod[masterIndex] >> 8 & 0xFF,
						oneDMod[masterIndex++] & 0xFF));

			}
		}
		/////// akhir dari hasilkan gambar hanya yang dibutuhkan ///////////////
		*/
		
		//////  hasilkan dimensi gambar terstego seutuhnya /////////////
		byte[] byteImage = LSB2bit.embedMessage(oneD, width, height, msg, key);
		oneD=null;
		sourceBitmap=null;
		int[] oneDMod = LSB2bit.byteArrayToIntArray(byteImage);
		byteImage=null;
		Bitmap destBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		destBitmap.setDensity(density);
		int masterIndex = 0;
		for (int j = 0; j < height; j++){
			for (int i = 0; i < width; i++){
				// The unique way to write correctly the sourceBitmap, android bug!!!
				destBitmap.setPixel(i, j, Color.argb(0xFF,
						oneDMod[masterIndex] >> 16 & 0xFF,
						oneDMod[masterIndex] >> 8 & 0xFF,
						oneDMod[masterIndex++] & 0xFF));

			}
		}
		/////// akhir dari hasilkan gambar seutuhnya ///////////////
		
		/*////// Mengambil komponen warna setelah Embedding ///////
		int[] color = new int[width * height * 3];
		color = LSB2bit.ambilKomponenWarna(oneDMod, width, height);
		color = null;
		/////////////////////////////////////////////////////////*/
		
		String destPath = "/mnt/sdcard/ssstego.png";
		OutputStream fout = null;
		try {
			Log.v("Path", destPath);
			fout = new FileOutputStream(destPath);			
			destBitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
			result=Uri.parse("file://"+destPath);			
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, result)); 
			fout.flush();
			fout.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		destBitmap.recycle();
		return destPath;
	}
    
    public void kirimkeserver(String destPath){
    	File f = new File(destPath);
		Boolean isTheFileExist = false; 
		isTheFileExist = f.exists();
		
		String status;
		if(isTheFileExist){
			status = "The file is exist";
		} else {
			status = "Didnot Exist";
		}
		Log.i("Existance of the File",status);
		
		HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(BlackberryActivity.daftarbelanja);
        String res = null, hasil = null;
        
        try {
            MultipartEntity entity = new MultipartEntity();
 
            entity.addPart("username", new StringBody(BlackberryActivity.mstrun));
            entity.addPart("totalharga", new StringBody(""+menu.totalBayar));
            entity.addPart("data", new FileBody(f));
 
            httppost.setEntity(entity);
            HttpResponse response = httpclient.execute(httppost);
                
            res = EntityUtils.toString(response.getEntity());
            hasil = res.replace("\n", "").replace(" ", "");
            Log.i("Pesan dari server", hasil);
        }
        catch (ClientProtocolException e) {
            Log.d("[Jin]", e.getMessage(),e);
        }
        catch (IOException e) {
            Log.d("[Jin]", e.getMessage(),e);
        }
    }
    
    public static String konversiStreamkeString(InputStream is){
    	BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    	StringBuilder sb = new StringBuilder();
    	String line=null;
    	
    	try{
    		while((line=reader.readLine()) !=null){
    			sb.append((line + "\n"));
    		}
    	} catch (IOException e){
    		
    	} finally {
    		try {
    			is.close();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    	return sb.toString();
    }
    
    public boolean cekValiditasKartu(String noKK){
    	long startTimeLuhn = System.nanoTime();
		int sum = 0; 
		int digit = 0; 
		int addend = 0; 
		boolean timesTwo = false;
		for (int i = noKK.length () - 1; i >= 0; i--) {
			digit = Integer.parseInt (noKK.substring (i, i + 1));
			if (timesTwo) { 
				addend = digit * 2; 
				if (addend > 9) { 
					addend -= 9; 
				} 
			} else { 
				addend = digit; 
			} 
			sum += addend; 
			timesTwo = !timesTwo; 
		}
		int modulus = sum % 10;
		long stopTimeLuhn = System.nanoTime();
		Log.v("Lama Eksekusi Luhn(ns)", "" + (stopTimeLuhn - startTimeLuhn));
		return ((modulus == 0) && (noKK.length() == 16));
    }
}