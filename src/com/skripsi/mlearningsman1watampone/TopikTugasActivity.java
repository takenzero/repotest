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

public class TopikTugasActivity extends ListActivity {
	public String id, nis, v_id, link;
	
	JSONArray str_json = null;
	public static final int progress_bar_type = 0;
	private ProgressDialog pDialog;
	ArrayList<HashMap<String, String>> data_map = new ArrayList<HashMap<String, String>>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.panel_topik);
        
        Intent in = getIntent();
        id = in.getStringExtra("par_id");
        nis = in.getStringExtra("par_nis");
        
        Koneksi konek = new Koneksi();
        link = konek.link_koneksi();
        
        new AmbilData().execute();
    }
    
    class AmbilData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TopikTugasActivity.this);
            pDialog.setMessage("Memuat topik tugas/quiz ..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        
        protected String doInBackground(String... args) {

            String link_url = link+"/json-mlearning/topik-quiz.php?id="+id;
    		JSONParser jParser = new JSONParser();
    		JSONObject json = jParser.AmbilJson(link_url);
    		
    		try {
    			str_json = json.getJSONArray("list_topik");
    			
    			for(int i = 0; i < str_json.length(); i++){
    				JSONObject ar = str_json.getJSONObject(i);
    				
    				HashMap<String, String> map = new HashMap<String, String>();
    				
    				String id = ar.getString("id");
    				String judul = "Judul : "+ar.getString("judul");
    				String tanggal = "Tanggal Posting : "+ar.getString("tgl");
    				
    				map.put("judul", judul);
    				map.put("tgl", tanggal);
    				map.put("id", id);

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
							TopikTugasActivity.this, data_map,
							R.layout.list_topik, new String[] { "id", "judul", "tgl" }, new int[] { R.id.topik_id, R.id.topik_judul, R.id.topik_tanggal });
                    setListAdapter(adapter);
                    
                    ListView lv = getListView();
                    lv.setOnItemClickListener(new OnItemClickListener() {
                    	@Override
            			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
            				v_id = ((TextView) view.findViewById(R.id.topik_id)).getText().toString();
            				
            				Intent in = new Intent(TopikTugasActivity.this, InformasiActivity.class);
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