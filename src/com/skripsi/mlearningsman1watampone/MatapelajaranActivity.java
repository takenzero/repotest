package com.skripsi.mlearningsman1watampone;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

public class MatapelajaranActivity extends ListActivity {
	public String nis, link;
	
	JSONArray str_json = null;
	
	private ProgressDialog pDialog;
	ArrayList<HashMap<String, String>> data_map = new ArrayList<HashMap<String, String>>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.panel_matapelajaran);
        
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
            pDialog = new ProgressDialog(MatapelajaranActivity.this);
            pDialog.setMessage("Memuat data matapelajaran Anda ..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        
        protected String doInBackground(String... args) {

            String link_url = link+"/json-mlearning/mata-pelajaran.php?nis="+nis;
    		JSONParser jParser = new JSONParser();
    		JSONObject json = jParser.AmbilJson(link_url);
    		
    		try {
    			str_json = json.getJSONArray("matapelajaran");
    			
    			for(int i = 0; i < str_json.length(); i++){
    				JSONObject ar = str_json.getJSONObject(i);
    				
    				HashMap<String, String> map = new HashMap<String, String>();

    				String nama = ar.getString("nama");
    				String pengajar = ar.getString("pengajar");
    				
    				map.put("nama", nama);
    				map.put("pengajar", pengajar);

    				data_map.add(map);
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
                	ListAdapter adapter = new SimpleAdapter(
							MatapelajaranActivity.this, data_map,
							R.layout.list_matapelajaran, new String[] { "nama", "pengajar" }, new int[] {R.id.matapelajaran_nama, R.id.matapeljaran_pengajar });
                    setListAdapter(adapter);
                }
            });
 
        }
    }
    
    

}