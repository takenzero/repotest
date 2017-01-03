package com.skripsi.mlearningsman1watampone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class KelasActivity  extends Activity {
	JSONArray json_kelas = null;
	public String id, nk, wk, kk, nis, link;
	
	private ProgressDialog pDialog;
	
	TextView txt_nama;
	TextView txt_wali;
	TextView txt_ketua;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.panel_kelas);
        
        Intent in = getIntent();
        nis = in.getStringExtra("par_kode");
        
        Koneksi konek = new Koneksi();
        link = konek.link_koneksi();
        
        new AmbilData().execute();
        
        Button btn = (Button)findViewById(R.id.kelas_btnsiswa);
        btn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), SemuaSiswaActivity.class);
				i.putExtra("par_kode", id);
				startActivity(i);
			}
		});
	}
	
	class AmbilData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(KelasActivity.this);
            pDialog.setMessage("Memuat Kelas Anda ..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        
        protected String doInBackground(String... args) {

            String link_url = link+"/json-mlearning/kelas-anda.php?nis="+nis;
    		JSONParser jParser = new JSONParser();
    		JSONObject json = jParser.AmbilJson(link_url);
    		
    		try {
    			json_kelas = json.getJSONArray("kelas");
    			
    			for(int i = 0; i < json_kelas.length(); i++){
    				JSONObject ar = json_kelas.getJSONObject(i);
    				
    				id = ar.getString("id");
    				nk = ar.getString("nk");
    				wk = ar.getString("wk");
    				kk = ar.getString("kk");
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
                	txt_nama = (TextView)findViewById(R.id.kelas_nama);
                	txt_wali = (TextView)findViewById(R.id.kelas_wali);
                	txt_ketua = (TextView)findViewById(R.id.kelas_ketua);
                	
                	txt_nama.setText(nk);
                	txt_wali.setText(wk);
                	txt_ketua.setText(kk);
                }
            });
 
        }
    }
	
}