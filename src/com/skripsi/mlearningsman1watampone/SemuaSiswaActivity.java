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

public class SemuaSiswaActivity extends ListActivity {
	
	static String in_nis = "nis";
	static String in_nama = "nama";
	static String in_jk = "kelamin";
	
	public String id, link;
	
	JSONArray str_json = null;
	
	private ProgressDialog pDialog;
	ArrayList<HashMap<String, String>> data_map = new ArrayList<HashMap<String, String>>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.panel_semuasiswa);
        
        Intent in = getIntent();
        id = in.getStringExtra("par_kode");
        
        Koneksi konek = new Koneksi();
        link = konek.link_koneksi();
        
        new AmbilData().execute();
    }
    
    class AmbilData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SemuaSiswaActivity.this);
            pDialog.setMessage("Memuat semua siswa ..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        
        protected String doInBackground(String... args) {

            String link_url = link+"/json-mlearning/siswa.php?id="+id;
    		JSONParser jParser = new JSONParser();
    		JSONObject json = jParser.AmbilJson(link_url);
    		
    		try {
    			str_json = json.getJSONArray("siswa");
    			
    			for(int i = 0; i < str_json.length(); i++){
    				JSONObject ar = str_json.getJSONObject(i);
    				
    				HashMap<String, String> map = new HashMap<String, String>();

    				String nis = ar.getString("nis");
    				String nama = ar.getString("nama");
    				String jk = ar.getString("kelamin");
    				
    				map.put(in_nis, nis);
    				map.put(in_nama, nama);
    				map.put(in_jk, jk);

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
							SemuaSiswaActivity.this, data_map,
							R.layout.list_semuasiswa, new String[] { in_nis, in_nama, in_jk }, new int[] {R.id.siswa_nis, R.id.siswa_nama, R.id.siswa_jk });
                    setListAdapter(adapter);
                }
            });
 
        }
    }
    
    

}