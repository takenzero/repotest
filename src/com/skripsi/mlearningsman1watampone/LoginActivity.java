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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	
	protected static final Exception AmbilData = null;
	public String var_usr,var_pass,stts,nis, link;
	EditText usr, psw;
	JSONArray str_login = null;
	
	private ProgressDialog pDialog;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.panel_login);
        
        Koneksi konek = new Koneksi();
        link = konek.link_koneksi();
        
        usr = (EditText) findViewById(R.id.txt_username);
        psw = (EditText) findViewById(R.id.txt_pass);
		
        Button submit = (Button) findViewById(R.id.btn_login);
        submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				var_usr = usr.getText().toString();
				var_pass = psw.getText().toString();
				
				usr.setText("");
				psw.setText("");
				
				new AmbilData().execute();
			}
		});
    }
    
    class AmbilData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Login ..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        
        protected String doInBackground(String... args) {
			
            String link_url = link+"/json-mlearning/login.php?usr="+var_usr+"&psw="+var_pass;
    		JSONParser jParser = new JSONParser();
    		JSONObject json = jParser.AmbilJson(link_url);
    		
    		try {
				str_login = json.getJSONArray("statuslogin");
				
				for(int i = 0; i < str_login.length(); i++){
					JSONObject ar = str_login.getJSONObject(i);
					
					stts = ar.getString("st");
					nis = var_usr;
	        		
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
                	if (stts.trim().equals("ok")){
                		Intent ni = new Intent(LoginActivity.this, MenuUtamaActivity.class);
						ni.putExtra("par_kode", nis);
						startActivity(ni);
						finish();
                	}else{
                		showAlertDialog(LoginActivity.this, "Login Gagal", "Maaf, Username atau password salah");
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