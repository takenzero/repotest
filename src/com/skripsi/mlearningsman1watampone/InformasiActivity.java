package com.skripsi.mlearningsman1watampone;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class InformasiActivity  extends Activity {
	JSONArray v_json = null;
	JSONArray q_json = null;
	public String id, nis, st, status, link;
	
	private ProgressDialog pDialog;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.panel_info);
        
        Intent in = getIntent();
        nis = in.getStringExtra("par_nis");
        id = in.getStringExtra("par_id");
        
        Koneksi konek = new Koneksi();
        link = konek.link_koneksi();
        
        new CekTopik().execute();
        
        Button btn_kerjakan = (Button)findViewById(R.id.info_btn);
        btn_kerjakan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				new UpdateStatus().execute();
			}
		});
	}
	class CekTopik extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(InformasiActivity.this);
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
                	if (st.trim().equals("sudah")){
                		showAlertDialog(InformasiActivity.this, "Status", "Maaf, Anda telah mengerjakan tugas/quiz pada topik ini.\n" +
                				"Silahkan pilih menu Nilai untuk melihat nilai tugas/quiz pada topik ini.");
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
	
	class UpdateStatus extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(InformasiActivity.this);
            pDialog.setMessage("Update status ..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        
        protected String doInBackground(String... args) {

            String link_url = link+"/json-mlearning/sudah-mengerjakan.php?id="+id+"&nis="+nis;
    		JSONParser jParser = new JSONParser();
    		JSONObject json = jParser.AmbilJson(link_url);
    		
    		try {
    			q_json = json.getJSONArray("status");
    			
    			for(int i = 0; i < q_json.length(); i++){
    				JSONObject ar = q_json.getJSONObject(i);
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
                		Intent in = new Intent(InformasiActivity.this, SoalActivity.class);
                		Bundle b = new Bundle();
                		b.putString("par_id", id);
                		b.putString("par_nis", nis);
                		in.putExtras(b);
                		startActivity(in);
                		
                		finish();
                	}else{
                		showAlertDialog(InformasiActivity.this, "Status", "Gagal");
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
    			}
    		});
    		alertDialog.show();
        }
	}

}