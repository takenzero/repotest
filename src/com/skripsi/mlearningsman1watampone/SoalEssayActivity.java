package com.skripsi.mlearningsman1watampone;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SoalEssayActivity extends ListActivity {
	public String id, nis, v_id, v_nomor, status, link;
	
	private static final String TAG_ID = "id_quiz";
	private static final String TAG_SOAL = "soal";
	private static final String TAG_NOMOR = "nomor";
	
	JSONArray str_json = null;
	JSONArray json_upload = null;
	public static final int progress_bar_type = 0;
	private ProgressDialog pDialog;
	ArrayList<HashMap<String, String>> data_map = new ArrayList<HashMap<String, String>>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.essay_layout);
        
        Intent in = getIntent();
        id = in.getStringExtra("par_id");
        nis = in.getStringExtra("par_nis");
        status = "netral";
        
        Koneksi konek = new Koneksi();
        link = konek.link_koneksi();
        
        new AmbilData().execute();
    }
    
    class AmbilData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SoalEssayActivity.this);
            pDialog.setMessage("Memuat soal essay ..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        
        protected String doInBackground(String... args) {

            String link_url = link+"/json-mlearning/soal-essay.php?id_tq="+id;
    		JSONParser jParser = new JSONParser();
    		JSONObject json = jParser.AmbilJson(link_url);
    		
    		try {
    			str_json = json.getJSONArray("soal");
    			
    			for(int i = 0; i < str_json.length(); i++){
    				JSONObject ar = str_json.getJSONObject(i);
    				
    				HashMap<String, String> map = new HashMap<String, String>();
    				
    				String id = ar.getString(TAG_ID);
					String soal = ar.getString(TAG_SOAL);
					String nomor = "Soal Ke : "+ (i + 1);
    				
					if(soal.length() > 40){
						soal = soal.substring(0, 39) + "...";
					}
					
					map.put(TAG_ID, id);
					map.put(TAG_SOAL, soal);
					map.put(TAG_NOMOR, nomor);

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
							SoalEssayActivity.this, data_map,
							R.layout.list_essay, new String[] { TAG_NOMOR, TAG_SOAL, TAG_ID }, new int[] { R.id.essay_nomor, R.id.essay_soal, R.id.essay_id });
                    setListAdapter(adapter);
                    
                    ListView lv = getListView();
                    lv.setOnItemClickListener(new OnItemClickListener() {
                    	@Override
            			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
            				v_id = ((TextView) view.findViewById(R.id.essay_id)).getText().toString();
            				v_nomor = ((TextView) view.findViewById(R.id.essay_nomor)).getText().toString();
                    	
            				Intent in = new Intent(SoalEssayActivity.this, DetailEssayActivity.class);
            				Bundle b = new Bundle();
            				b.putString("par_id", v_id);
            				b.putString("par_nis", nis);
            				b.putString("par_nomor", v_nomor);
            				in.putExtras(b);
            				startActivity(in);
                    	}
                    });
                }
            }); 
        }
    }
    
    public void selesai(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Apakah Anda Benar-Benar ingin selesai mengerjakan tugas/quiz ini ?")
        .setCancelable(false)
        .setPositiveButton("Ya",
        		new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int id) {
        			new UploadJawaban().execute();
        		}
        	})
        .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int id) {
        			dialog.cancel();
        		}
        }).show();
    }
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
         selesai();
        }   
        return super.onKeyDown(keyCode, event);
     }
	
	class UploadJawaban extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SoalEssayActivity.this);
            pDialog.setMessage("Upload jawaban anda ..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        
        protected String doInBackground(String... args) {

            String link_url = link+"/json-mlearning/simpan-nilaipg.php?id="+id+"&nis="+nis;
    		JSONParser jParser = new JSONParser();
    		JSONObject json = jParser.AmbilJson(link_url);
    		
    		try {
    			json_upload = json.getJSONArray("status");
    			
    			for(int i = 0; i < json_upload.length(); i++){
    				JSONObject ar = json_upload.getJSONObject(i);
    				
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
                		Toast.makeText(SoalEssayActivity.this, "Jawaban telah diupload.", Toast.LENGTH_SHORT).show();
                		finish();
                	}else if (status.trim().equals("gagal")){
                		Toast.makeText(SoalEssayActivity.this, "Jawaban gagal diupload.", Toast.LENGTH_SHORT).show();
                	}else{
                		finish();
                	}
                }
            });
 
        }
        
    }
}