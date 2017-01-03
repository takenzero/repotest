package com.skripsi.mlearningsman1watampone;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailEssayActivity  extends Activity {
	JSONArray json_essay = null;
	JSONArray json_simpan = null;
	
	public String nis, id, v_nomor, v_soal, v_st, v_gambar, v_ada, jwb, status, link;
	
	private ProgressDialog pDialog;
	private ProgressDialog sDialog;
	Button btn_save;
	EditText edtJwb;
	Bitmap bm;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.panel_detailessay);
        
        Intent in = getIntent();
        id = in.getStringExtra("par_id");
        nis = in.getStringExtra("par_nis");
        v_nomor = in.getStringExtra("par_nomor");
        
        edtJwb = (EditText)findViewById(R.id.detailessay_jawaban);
        
        Koneksi konek = new Koneksi();
        link = konek.link_koneksi();
        
        new AmbilData().execute();
        
        btn_save = (Button)findViewById(R.id.detailessay_btnsave);
        btn_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String a = edtJwb.getText().toString();
				if (a.length() > 0){
					new SimpanJawaban().execute();
				} else {
					Toast.makeText(DetailEssayActivity.this, "Jawaban belum diisi.", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	class AmbilData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailEssayActivity.this);
            pDialog.setMessage("Memuat soal yang dipilih ..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        
        protected String doInBackground(String... args) {

            String link_url = link+"/json-mlearning/detail-soalessay.php?id="+id+"&nis="+nis;
    		JSONParser jParser = new JSONParser();
    		JSONObject json = jParser.AmbilJson(link_url);
    		
    		try {
    			json_essay = json.getJSONArray("soal");
    			
    			for(int i = 0; i < json_essay.length(); i++){
    				JSONObject ar = json_essay.getJSONObject(i);
    				
    				v_st = ar.getString("st");
    				v_ada = ar.getString("ada");
    				v_soal = ar.getString("pertanyaan");
    				v_gambar = ar.getString("gambar");
    				
    				if (v_ada.trim().equals("ada")){
    					BitmapFactory.Options bmOptions;
        				bmOptions = new BitmapFactory.Options();
        				bmOptions.inSampleSize = 1;
        				bm = LoadImage(link+"/foto_soal/medium_"+v_gambar, bmOptions);
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
                	TextView txt_nomor = (TextView)findViewById(R.id.detailessay_nomor);
                	TextView txt_soal = (TextView)findViewById(R.id.detailessay_soal);
                	ImageView gbr = (ImageView)findViewById(R.id.detailessay_gambar);
                	
                	if (v_st.trim().equals("belum")){
                		txt_nomor.setText(v_nomor);
                		txt_soal.setText(v_soal);
                		if (v_ada.trim().equals("ada")){
                			gbr.setImageBitmap(bm);
                		}
                	}else{
                		showAlertDialog(DetailEssayActivity.this, "Maaf", "Soal ini telah dikerjakan !!");
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
            sDialog = new ProgressDialog(DetailEssayActivity.this);
            sDialog.setMessage("Simpan jawaban ..");
            sDialog.setIndeterminate(false);
            sDialog.setCancelable(false);
            sDialog.show();
        }
        
        protected String doInBackground(String... args) {
        	jwb = edtJwb.getText().toString();
            String link_url = link+"/json-mlearning/simpan-essay.php?";
            List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("nis", nis));
			params.add(new BasicNameValuePair("idq", id));
			params.add(new BasicNameValuePair("jwb", jwb));
			
            JSONParser jParser = new JSONParser();
    		JSONObject json = jParser.KirimJson(link_url, "POST", params);
    		
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
            sDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                	edtJwb.setText("");
                	if (status.trim().equals("ok")){
                		Toast.makeText(DetailEssayActivity.this, "Jawaban telah tersimpan.", Toast.LENGTH_SHORT).show();
                		DetailEssayActivity.this.finish();
                	}else{
                		Toast.makeText(DetailEssayActivity.this, "Maaf, terjadi kesalahan.", Toast.LENGTH_SHORT).show();
                	}
                }
            });
 
        }
    }
}