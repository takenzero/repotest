package com.skripsi.mlearningsman1watampone;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfilActivity  extends Activity {
	JSONArray json_profil = null;
	public String nis, nama, kelas, alamat, tlahir, tglahir, jk, agama, ayah, ibu, masuk, email, tlp, jabatan, foto, link;
	
	private ProgressDialog pDialog;
	
	TextView txt_nis;
	TextView txt_nama;
	TextView txt_kelas;
	TextView txt_alamat;
	TextView txt_tlahir;
	TextView txt_tglahir;
	TextView txt_jk;
	TextView txt_agama;
	TextView txt_ayah;
	TextView txt_ibu;
	TextView txt_masuk;
	TextView txt_email;
	TextView txt_tlp;
	TextView txt_jabatan;
	ImageView img_foto;
	
	Bitmap bm;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.panel_profil);
        
        Intent in = getIntent();
        nis = in.getStringExtra("par_kode");
        
        Koneksi konek = new Koneksi();
        link = konek.link_koneksi();
        
        new AmbilData().execute();
	}
	
	class AmbilData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProfilActivity.this);
            pDialog.setMessage("Memuat profil Anda ..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        
        protected String doInBackground(String... args) {

            String link_url = link+"/json-mlearning/profil.php?nis="+nis;
    		JSONParser jParser = new JSONParser();
    		JSONObject json = jParser.AmbilJson(link_url);
    		
    		try {
    			json_profil = json.getJSONArray("profil");
    			
    			for(int i = 0; i < json_profil.length(); i++){
    				JSONObject ar = json_profil.getJSONObject(i);
    				
    				nis = ar.getString("nis");
    				nama = ar.getString("nama");
    				kelas = ar.getString("kelas");
    				alamat = ar.getString("alamat");
    				tlahir = ar.getString("tlahir");
    				tglahir = ar.getString("tglahir");
    				jk = ar.getString("kelamin");
    				agama = ar.getString("agama");
    				ayah = ar.getString("ayah");
    				ibu = ar.getString("ibu");
    				masuk = ar.getString("masuk");
    				email = ar.getString("email");
    				tlp = ar.getString("tlp");
    				jabatan = ar.getString("jabatan");
    				foto = ar.getString("foto");
    				
    				BitmapFactory.Options bmOptions;
    				bmOptions = new BitmapFactory.Options();
    				bmOptions.inSampleSize = 1;
    				bm = LoadImage(link+"/foto_siswa/medium_"+foto, bmOptions);
    			}
    		} catch (JSONException e) {
    			e.printStackTrace();
    		}
            return null;
        }
        
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                	txt_nis = (TextView)findViewById(R.id.profil_nis);
                	txt_nama = (TextView)findViewById(R.id.profil_nama);
                	txt_kelas = (TextView)findViewById(R.id.profil_kelas);
                	txt_alamat = (TextView)findViewById(R.id.profil_alamat);
                	txt_tlahir = (TextView)findViewById(R.id.profil_tlahir);
                	txt_tglahir = (TextView)findViewById(R.id.profil_tglahir);
                	txt_jk = (TextView)findViewById(R.id.profil_kelamin);
                	txt_agama = (TextView)findViewById(R.id.profil_agama);
                	txt_ayah = (TextView)findViewById(R.id.profil_ayah);
                	txt_ibu = (TextView)findViewById(R.id.profil_ibu);
                	txt_masuk = (TextView)findViewById(R.id.profil_masuk);
                	txt_email = (TextView)findViewById(R.id.profil_email);
                	txt_tlp = (TextView)findViewById(R.id.profil_tlp);
                	txt_jabatan = (TextView)findViewById(R.id.profil_jabatan);
                	img_foto = (ImageView)findViewById(R.id.profil_foto);
                	
                	txt_nis.setText(nis);
                	txt_nama.setText(nama);
                	txt_kelas.setText(kelas);
                	txt_alamat.setText(alamat);
                	txt_tlahir.setText(tlahir);
                	txt_tglahir.setText(tglahir);
                	txt_jk.setText(jk);
                	txt_agama.setText(agama);
                	txt_ayah.setText(ayah);
                	txt_ibu.setText(ibu);
                	txt_masuk.setText(masuk);
                	txt_email.setText(email);
                	txt_tlp.setText(tlp);
                	txt_jabatan.setText(jabatan);
                	img_foto.setImageBitmap(bm);
                }
            });
 
        }
    }
	
	private Bitmap LoadImage(String URL, BitmapFactory.Options options)
	{       
		Bitmap bitmap = null;
		InputStream in = null;       
		try {
			in = OpenHttpConnection(URL);
			bitmap = BitmapFactory.decodeStream(in, null, options);
			in.close();
		} catch (IOException e1) {
		}
		return bitmap;               
	}

	private InputStream OpenHttpConnection(String strURL) throws IOException{
		InputStream inputStream = null;
		URL url = new URL(strURL);
		URLConnection conn = url.openConnection();

		try{
			HttpURLConnection httpConn = (HttpURLConnection)conn;
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				inputStream = httpConn.getInputStream();
			}
		}
		catch (Exception ex)
		{
		}
		return inputStream;
	}
	
}