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
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class TugasActivity extends ListActivity {
	public String nis, link;
	
	JSONArray str_json = null;
	
	private ProgressDialog pDialog;
	ArrayList<HashMap<String, String>> data_map = new ArrayList<HashMap<String, String>>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.panel_tugas);
        
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
            pDialog = new ProgressDialog(TugasActivity.this);
            pDialog.setMessage("Memuat data tugas ..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        
        protected String doInBackground(String... args) {

            String link_url = link+"/json-mlearning/tugas.php?nis="+nis;
    		JSONParser jParser = new JSONParser();
    		JSONObject json = jParser.AmbilJson(link_url);
    		
    		try {
    			str_json = json.getJSONArray("materi");
    			
    			for(int i = 0; i < str_json.length(); i++){
    				JSONObject ar = str_json.getJSONObject(i);
    				
    				HashMap<String, String> map = new HashMap<String, String>();
    				
    				String id = ar.getString("id");
    				String nama = ar.getString("nama");
    				String jum = ar.getString("jum");
    				
    				map.put("id", id);
    				map.put("nama", nama);
    				map.put("jum", jum);

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
							TugasActivity.this, data_map,
							R.layout.list_tugas, new String[] { "id", "nama", "jum" }, new int[] {R.id.tugas_id, R.id.tugas_nama, R.id.tugas_jum });
                    setListAdapter(adapter);
                    ListView lv = getListView();
                    lv.setOnItemClickListener(new OnItemClickListener() {
                    	@Override
            			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                    		String v_id = ((TextView) view.findViewById(R.id.tugas_id)).getText().toString();
            				
            				Intent in = new Intent(TugasActivity.this, TopikTugasActivity.class);
            				Bundle b = new Bundle();
            				b.putString("par_id", v_id);
            				b.putString("par_nis", nis);
            				in.putExtras(b);
            				startActivity(in);
            			}
                    });
                }
            });
 
        }
    }
    
    

}