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

public class ListDownloadActivity extends ListActivity {
	public String id, nama, link;
	
	JSONArray str_json = null;
	public static final int progress_bar_type = 0;
	private ProgressDialog pDialog;
	ArrayList<HashMap<String, String>> data_map = new ArrayList<HashMap<String, String>>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.panel_listdownload);
        
        Intent in = getIntent();
        id = in.getStringExtra("par_id");
        nama = in.getStringExtra("par_nama");
        
        Koneksi konek = new Koneksi();
        link = konek.link_koneksi();
        
        new AmbilData().execute();
    }
    
    class AmbilData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ListDownloadActivity.this);
            pDialog.setMessage("Memuat list download materi ..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        
        protected String doInBackground(String... args) {

            String link_url = link+"/json-mlearning/list-download.php?id="+id;
    		JSONParser jParser = new JSONParser();
    		JSONObject json = jParser.AmbilJson(link_url);
    		
    		try {
    			str_json = json.getJSONArray("list_download");
    			
    			for(int i = 0; i < str_json.length(); i++){
    				JSONObject ar = str_json.getJSONObject(i);
    				
    				HashMap<String, String> map = new HashMap<String, String>();
    				
    				String judul = ar.getString("judul");
    				String tanggal = "Diupload tanggal : "+ar.getString("tgl");
    				String nama = ar.getString("nama");
    				
    				map.put("judul", judul);
    				map.put("tgl", tanggal);
    				map.put("nama", nama);

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
							ListDownloadActivity.this, data_map,
							R.layout.list_listdownload, new String[] { "judul", "tgl", "nama" }, new int[] { R.id.listdownlaod_judul, R.id.listdownload_tanggal, R.id.listdownload_nama });
                    setListAdapter(adapter);
                    
                    ListView lv = getListView();
                    lv.setOnItemClickListener(new OnItemClickListener() {
                    	@Override
            			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
            				String v_nama = ((TextView) view.findViewById(R.id.listdownload_nama)).getText().toString();
            				String v_judul = ((TextView) view.findViewById(R.id.listdownlaod_judul)).getText().toString();
            				
            				Intent in = new Intent(ListDownloadActivity.this, DownloadFileActivity.class);
            				Bundle b = new Bundle();
            				b.putString("par_matapelajaran", nama);
            				b.putString("par_judul", v_judul);
            				b.putString("par_nama", v_nama);
            				in.putExtras(b);
            				startActivity(in);
                    	}
                    });
                }
            }); 
        }
    }
}