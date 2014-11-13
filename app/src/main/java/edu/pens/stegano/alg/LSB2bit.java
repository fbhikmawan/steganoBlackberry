package edu.pens.stegano.alg;

import java.util.Vector;

import edu.pens.stegano.blackberry.BlackberryActivity;
import android.graphics.Color;
import android.util.Log;

/**
 * Algoritma Inti dari ssStego.
 * 
 */
public class LSB2bit {
	private static int[] binary = { 16, 8, 0 };	
	private static byte[] satu = { (byte) 0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01 };
	public static String END_MESSAGE_COSTANT = "#!@";
	public static String START_MESSAGE_COSTANT = "@!#";

	/**
	 * Fungsi ini bertugas untuk menyisipkan pesan kedalam cover image setelah
	 * proses Spread Spectrum.
	 * @param oneDPix adalah array pixel (memuat argb).
	 * @param imgCols adalah lebar gambar.
	 * @param imgRows adalah tinggi gambar.
	 * @param str pesan yang akan disisipkan.
	 * @return mengeluarkan berupa array tiap komponen warna (R,G dan B).
	 */
	public static byte[] embedMessage(int[] oneDPix, int imgCols, int imgRows, String str, String key) {
	//public static byte[] embedMessage(int[] oneDPix, int imgCols, int maxwidth, int imgRows, int maxheight, String str, String key) {
		//int[] color = new int[imgRows * imgCols * 3];
		//color = ambilKomponenWarna(oneDPix, imgCols, imgRows);
				
		byte[] msg = str.getBytes();
		Log.v("Pesan dari Client", "" + str);
		
		long startTime1 = System.nanoTime();
		int[] msgspreading = Spreading(msg, BlackberryActivity.banyakspread); //spreading pesan sebanyak 4kali, tiap elemen array tsb hanya bernilai satu bit.
		long stopTime1 = System.nanoTime();
		Log.v("Lama Eksekusi Spreading (ns)", "" + (stopTime1 - startTime1));
		
		int LCGsebanyak = (msgspreading.length)/8; //banyaknya perulangan proses LCG.
		
		long startTime2 = System.nanoTime();
		int[] LCGKunci = LCG(key, LCGsebanyak); //bangkitkan kunci menggunakan LCG, tiap elemen array tsb hanya bernilai satu bit.
		long stopTime2 = System.nanoTime();
		Log.v("Lama Eksekusi PN (ns)", "" + (stopTime2 - startTime2));
		
		long startTime3 = System.nanoTime();
		int[] hasilXOR = XORkan(msgspreading, LCGKunci); //mengXOR Pseudorandom Number dengan bit-bit pesan.
		long stopTime3 = System.nanoTime();
		Log.v("Lama Eksekusi Modulasi XOR (ns)", "" + (stopTime3 - startTime3));
		
		int channels = 3;
		byte[] result = new byte[imgRows * imgCols * channels];
		int msgIndex = 0;
		int resultIndex = 0;
		boolean msgEnded = false;
		long startTime4 = System.nanoTime();
		for (int row = 0; row < imgRows; row++) {
			for (int col = 0; col < imgCols; col++) {
				int element = row * imgCols + col;
				byte tmp = 0;
				//if (element==maxwidth*maxheight) break; //nentukan dimensi maksimal gmbr
				for (int channelIndex = 0; channelIndex < channels; channelIndex++) { //dimualai dari R(16), G(8) terus B(0)
					if (!msgEnded) {						
						tmp = (byte) (((((oneDPix[element] >> binary[channelIndex]) & 0xFF) & 0xFE) | 
								hasilXOR[msgIndex++]));	
						if (msgIndex == hasilXOR.length) {
							msgEnded = true;
						}
					} else {
						tmp = (byte) ((((oneDPix[element] >> binary[channelIndex]) & 0xFF)));
					}
					result[resultIndex++] = tmp;
				}
			}
		}
		long stopTime4 = System.nanoTime();
		Log.v("Lama Eksekusi penyisipan ke komponen warna (ns)", "" + (stopTime4 - startTime4));
		return result;
	}
	
	/**
	 * Fungsi ini bertugas untuk mengambil kembali pesan yang ada di
	 * stegoimage mellaui SpreadSpectrum.
	 * @param oneDPix adalah array byte sudah berupa R, G dan B.
	 * @param imgCols adalah lebar gambar.
	 * @param imgRows adalah tinggi gambar.
	 * @return mengeluarkan pesan yang telah diambil berupa gabungan nomor
	 * kartu kredit dan passsword yang dipisahkan dengan "&&&".
	 */
	public static String extractMessage(byte[] oneDPix, int imgCols, int imgRows) { //var oneDPix sudah berupa RGB
		String builder = "";
		String key = "fatchul";
		
		long startTime5 = System.nanoTime();
		int[] databyte = ambilbitawal(oneDPix);
		long stopTime5 = System.nanoTime();
		Log.v("Lama Eksekusi Ambil Semua LSB (ns)", "" + (stopTime5 - startTime5));
		
		int LCGsebanyak = (oneDPix.length)/8;
		int[] LCGKunci = LCG(key, LCGsebanyak);
		
		long startTime6 = System.nanoTime();
		int[] hasilXOR = XORkan(databyte, LCGKunci);
		long stopTime6 = System.nanoTime();
		Log.v("Lama Eksekusi XOR (ns)", "" + (stopTime6 - startTime6));
		
		Vector<Byte> v = new Vector<Byte>();
        int tmpint, index = 0, indexhasil = 0, posisibyte = 0;
        int ukuranarray = hasilXOR.length;
        byte tmpbyte = 0x00;
        byte[] hasil = new byte[ukuranarray/2];
        long startTime7 = System.nanoTime();
        for(int i=0; i < ukuranarray/4; i++){
        	tmpint = hasilXOR[index];
        	index = index + 4;		
        	if(tmpint == 1){
        		tmpbyte = (byte) (tmpbyte | satu[posisibyte]);
        		posisibyte++;
        	}
        	if(tmpint == 0){
        		posisibyte++;
        	}
        	if(posisibyte == 8){
        		hasil[indexhasil++]=tmpbyte;
        		posisibyte=0;
        		tmpbyte = 0x00;
        	}
        }
        long stopTime7 = System.nanoTime();
		Log.v("Lama Eksekusi Despreading Empat Kali (ns)", "" + (stopTime7 - startTime7));
        for(int i=0; i<hasil.length; i++){
			v.addElement(new Byte(hasil[i]));
			byte[] nonso = { (v.elementAt(v.size() - 1)).byteValue() };
			String str = new String(nonso);
			if (builder.endsWith(END_MESSAGE_COSTANT)) {
				break;
			} else {
				builder = builder + str;
				if (builder.length() == START_MESSAGE_COSTANT.length() && !START_MESSAGE_COSTANT.equals(builder)) {
					builder = null;
					break;
				}
			}
        }
        if (builder != null)
			builder = builder.substring(START_MESSAGE_COSTANT.length(), builder.length() - END_MESSAGE_COSTANT.length());
        return builder;
	}
	
	public static int[] ambilbitawal(byte[] oneDPix) { //var oneDPix sudah berupa RGB
		int[] result = new int[oneDPix.length];
		for (int i = 0; i < (oneDPix.length); i++) {
			result[i] = (oneDPix[i] & 0x01);
		}
		return result;

	}
	
	public static int[] XORkan(int[] a, int[] b) { //var oneDPix sudah berupa RGB
		int[] c = new int[a.length];
		for(int i=0; i<b.length;i++){
			c[i]=a[i]^b[i];
		}
		return c;
	}
	
	public static int[] LCG(String k,int ulangi) { //var oneDPix sudah berupa RGB
		int a=17, c=7, m=84, intKey = 0;
    	int[] XnHasil = new int[ulangi];
		byte[] byteKey = k.getBytes();		
		for (int i = 0; i<byteKey.length; i++){
			if(i==0){
				intKey = (byteKey[i]^byteKey[i+1]);
				i++;
				continue;
			}
			intKey=intKey^byteKey[i];
		}
		
		for (int i=0; i<ulangi ; i++){
			if(i==0){
				XnHasil[i]=(a*intKey+c)%m;
				continue;
			}
			XnHasil[i]=(a*XnHasil[i-1]+c)%m;	
		}
		int posisibyte=7, letakLCG = 0;
		int[] LCGtotal = new int[(XnHasil.length)*8];
		for(int i=0; i<XnHasil.length;i++){
			for(int j=0; j<8; j++){
				LCGtotal[letakLCG++] = ((XnHasil[i] >> posisibyte--) & 0x01);	
			}
			posisibyte=7;
		}
		return LCGtotal;
	}
	
	/**
	 * Convert the byte array to an int array.
	 * @param b The byte array.
	 * @return The int array.
	 */

	public static int[] byteArrayToIntArray(byte[] b) {
		Log.v("Size byte array", b.length+"");
		int size=b.length / 3;
		Log.v("Size Int array",size+"");
		System.runFinalization();
		System.gc();
		Log.v("FreeMemory", Runtime.getRuntime().freeMemory()+"");
		int[] result = new int[size];
		int off = 0;
		int index = 0;
		while (off < b.length) {
			result[index++] = byteArrayToInt(b, off);
			off = off + 3;
		}
		return result;
	}

	/**
	 * Convert the byte array to an int.
	 * 
	 * @param b
	 *            The byte array
	 * @return The integer
	 */
	public static int byteArrayToInt(byte[] b) {
		return byteArrayToInt(b, 0);
	}

	/**
	 * Convert the byte array to an int starting from the given offset.
	 * 
	 * @param b
	 *            The byte array
	 * @param offset
	 *            The array offset
	 * @return The integer
	 */
	public static int byteArrayToInt(byte[] b, int offset) {
		int value = 0x00000000;
		for (int i = 0; i < 3; i++) {
			int shift = (3 - 1 - i) * 8;
			value |= (b[i + offset] & 0x000000FF) << shift;
		}
		value = value & 0x00FFFFFF;
		return value;
	}

	/**
	 * Convert integer array representing [argb] values to byte array
	 * representing [rgb] values
	 * 
	 * @param array Integer array representing [argb] values.
	 * @return byte Array representing [rgb] values.
	 */

	public static byte[] convertArray(int[] array) { //var array mrpkn tipe data Color
		byte[] newarray = new byte[array.length * 3]; //dikali tiga karena nanti mengambil tiga komponen, RGB.
		for (int i = 0; i < array.length; i++) { //looping tiap elemen Color var array
			newarray[i * 3] = (byte) ((array[i] >> 16) & 0xFF); //Red
			newarray[i * 3 + 1] = (byte) ((array[i] >> 8) & 0xFF); //Green
			newarray[i * 3 + 2] = (byte) ((array[i]) & 0xFF); //Blue
		}
		return newarray;
	}
	
	public static int[] Spreading (byte[] pesan, int k){
		int[] array = new int[(pesan.length*8*k)];
		byte b = 0;
        int geserkanan = 7, insert = 0;
        for(int count=0; count < pesan.length; count++){
	        for (int i=0; i<8 ; i++){
	        	b = (byte) ((pesan[count] >> geserkanan--) & 0x01);
	        	if (b == 0){
	        		for(int j=0; j<k; j++){
	        			array[insert] = 0;
	        			insert++;
	        		}
	        	}
	        	if (b == 1){
	        		for(int j=0; j<k; j++){
	        			array[insert] = 1;
	        			insert++;
	        		}
	        	}
	        }
	        geserkanan = 7;
        }
        return array;
	}
	
	public static int[]ambilKomponenWarna(int[] oneDPix, int imgRows, int imgCols) {
		int resultIndex = 0;
		int[] color = new int[imgRows * imgCols * 3];
		for (int row = 0; row < imgRows; row++) {
			for (int col = 0; col < imgCols; col++) {
				int element = row * imgCols + col;
				color[resultIndex++] = Color.red(oneDPix[element]);
				color[resultIndex++] = Color.green(oneDPix[element]);
				color[resultIndex++] = Color.blue(oneDPix[element]);
			}
		}
		return color;
	}
}
