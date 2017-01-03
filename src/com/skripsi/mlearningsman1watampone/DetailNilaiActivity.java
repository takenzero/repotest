package com.skripsi.mlearningsman1watampone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class DetailNilaiActivity  extends Activity {
	JSONArray v_json = null;
	JSONArray x_json = null;
	public String id, judul, nama, nis, st, np, ns, link;
	
	private ProgressDialog pDialog;
	
	TextView txt_nama;
	TextView txt_judul;
	TextView txt_nilaipg;
	TextView txt_nilaiessay;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.panel_detailnilai);
        
        Intent in = getIntent();
        nis = in.getStringExtra("par_nis");
        id = in.getStringExtra("par_id");
        judul = in.getStringExtra("par_judul");
        nama = in.getStringExtra("par_nama");
        
        Koneksi konek = new Koneksi();
        link = konek.link_koneksi();
        
        new CekTopik().execute();
        
	}
	class CekTopik extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailNilaiActivity.this);
            pDialog.setMessage("Cek status Anda ..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        
        protected String doInBackground(String... args) {

            String link_url = link+"/json-mlearning/cek-tugas.php?idx="+id+"&nis="+nis;
    		JSONParser jParser = new JSONParser();
    		JSONObject json = jParser.AmbilJson(link_url);
    		
    		try {
    			v_json = json.getJSONArray("cek");
    			
    			for(int i = 0; i < v_json.length(); i++){
    				JSONObject ar = v_json.getJSONObject(i);
    				st = ar.getString("st");
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
                	if (st.trim().equals("belum")){
                		showAlertDialog(DetailNilaiActivity.this, "Status", "Maaf, Anda belum mengerjakan tugas/quiz pada topik ini.\n" +
                				"Silahkan pilih menu Tugas/Quiz untuk mengerjakan soal.");
                	} else {
                		new AmbilData().execute();
                	}
                }
            });
        }
	
        class AmbilData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailNilaiActivity.this);
            pDialog.setMessage("Sedang memuat nilai Anda ..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        
        protected String doInBackground(String... args) {

            String link_url = link+"/json-mlearning/view-nilai.php?id="+id+"&nis="+nis;
    		JSONParser jParser = new JSONParser();
    		JSONObject json = jParser.AmbilJson(link_url);
    		
    		try {
    			x_json = json.getJSONArray("nilai");
    			
    			for(int i = 0; i < x_json.length(); i++){
    				JSONObject ar = x_json.getJSONObject(i);
    				
    				np = ar.getString("nilai_pg");
    				ns = ar.getString("nilai_essay");
    				
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
                	txt_nama = (TextView)findViewById(R.id.dnilai_matapelajaran);
                	txt_judul = (TextView)findViewById(R.id.dnilai_topik);
                	txt_nilaipg = (TextView)findViewById(R.id.dnilai_pg);
                	txt_nilaiessay = (TextView)findViewById(R.id.dnilai_essay);
                
                	txt_nama.setText(nama);
                	txt_judul.setText(judul);
                	txt_nilaipg.setText(np);
                	txt_nilaiessay.setText(ns);
                }
            });
 
        }
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
}