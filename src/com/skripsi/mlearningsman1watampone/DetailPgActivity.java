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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class DetailPgActivity  extends Activity implements OnCheckedChangeListener{
	JSONArray json_pg = null;
	JSONArray json_simpan = null;
	public String nis, id, jwb, v_nomor, v_soal, v_a, v_b, v_c, v_d, v_st, v_gambar, v_ada, v_kunci, status, link;
	public int param, kosong;
	private ProgressDialog pDialog;
	Button btn_save;
	
	Bitmap bm;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.panel_detailpg);
        
        Intent in = getIntent();
        id = in.getStringExtra("par_id");
        nis = in.getStringExtra("par_nis");
        v_nomor = in.getStringExtra("par_nomor");
        kosong = in.getIntExtra("par_kosong", 0);
        jwb = "T";
        param = 0;
        
        Koneksi konek = new Koneksi();
        link = konek.link_koneksi();
        
        new AmbilData().execute();
        
        RadioGroup rd = (RadioGroup)findViewById(R.id.detailpg_radio);
        rd.setOnCheckedChangeListener(this);
        
        btn_save = (Button)findViewById(R.id.detailpg_btnsave);
        btn_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if (jwb.equals("T")){
					Toast.makeText(DetailPgActivity.this, "Jawaban belum dipilih.", Toast.LENGTH_SHORT).show();
				}else{
					if (jwb.equals(v_kunci)){
						param = 1;
					}else{
						param = 0;
					}
					
					new SimpanJawaban().execute();
				}
			}
		});
	}
	
	class AmbilData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailPgActivity.this);
            pDialog.setMessage("Memuat soal yang dipilih ..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        
        protected String doInBackground(String... args) {

            String link_url = link+"/json-mlearning/detail-soalpg.php?id="+id+"&nis="+nis;
    		JSONParser jParser = new JSONParser();
    		JSONObject json = jParser.AmbilJson(link_url);
    		
    		try {
    			json_pg = json.getJSONArray("soal");
    			
    			for(int i = 0; i < json_pg.length(); i++){
    				JSONObject ar = json_pg.getJSONObject(i);
    				
    				v_st = ar.getString("st");
    				v_ada = ar.getString("ada");
    				v_soal = ar.getString("pertanyaan");
    				v_a = ar.getString("pil_a");
    				v_b = ar.getString("pil_b");
    				v_c = ar.getString("pil_c");
    				v_d = ar.getString("pil_d");
    				v_kunci = ar.getString("kunci");
    				v_gambar = ar.getString("gambar");
    				
    				if (v_ada.trim().equals("ada")){
    					BitmapFactory.Options bmOptions;
        				bmOptions = new BitmapFactory.Options();
        				bmOptions.inSampleSize = 1;
        				bm = LoadImage(link+"/foto_soal_pilganda/medium_"+v_gambar, bmOptions);
    				}
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
                	TextView txt_nomor = (TextView)findViewById(R.id.detailpg_nomor);
                	TextView txt_soal = (TextView)findViewById(R.id.detailpg_soal);
                	TextView txt_a = (TextView)findViewById(R.id.detailpg_a);
                	TextView txt_b = (TextView)findViewById(R.id.detailpg_b);
                	TextView txt_c = (TextView)findViewById(R.id.detailpg_c);
                	TextView txt_d = (TextView)findViewById(R.id.detailpg_d);
                	ImageView gbr = (ImageView)findViewById(R.id.detailpg_gambar);
                	
                	if (v_st.trim().equals("belum")){
                		txt_nomor.setText(v_nomor);
                		txt_soal.setText(v_soal);
                		txt_a.setText(v_a);
                		txt_b.setText(v_b);
                		txt_c.setText(v_c);
                		txt_d.setText(v_d);
                		if (v_ada.trim().equals("ada")){
                			gbr.setImageBitmap(bm);
                		}
                	}else{
                		showAlertDialog(DetailPgActivity.this, "Maaf", "Soal ini telah dikerjakan !!");
                	}
                }
            });
 
        }
        @SuppressWarnings("deprecation")
		public void showAlertDialog(Context context, String title, String message) {
    		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
    		alertDialog.setTitle(title);
    		alertDialog.setMessage(message);    		
    		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
    			public void onClick(final DialogInterface dialog, final int which) {
    				finish();
    			}
    		});
    		alertDialog.show();
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
	
	class SimpanJawaban extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailPgActivity.this);
            pDialog.setMessage("Simpan jawaban ..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        
        protected String doInBackground(String... args) {

            String link_url = link+"/json-mlearning/simpan-pg.php?id="+id+"&nis="+nis+"&param="+param+"&kosong="+kosong;
    		JSONParser jParser = new JSONParser();
    		JSONObject json = jParser.AmbilJson(link_url);
    		
    		try {
    			json_simpan = json.getJSONArray("status");
    			
    			for(int i = 0; i < json_simpan.length(); i++){
    				JSONObject ar = json_simpan.getJSONObject(i);
    				
    				status = ar.getString("status");
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
                	if (status.trim().equals("ok")){
                		Toast.makeText(DetailPgActivity.this, "Jawaban tersimpan.", Toast.LENGTH_SHORT).show();
                		finish();
                	}else{
                		Toast.makeText(DetailPgActivity.this, "Gagal menyimpan jawaban.", Toast.LENGTH_SHORT).show();
                	}
                }
            });
 
        }
    }
	
	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		switch (arg1){
		case R.id.detailpg_a :
			jwb = "A";
			break;
		case R.id.detailpg_b :
			jwb = "B";
			break;
		case R.id.detailpg_c :
			jwb = "C";
			break;
		case R.id.detailpg_d :
			jwb = "D";
			break;
		}
	}
}