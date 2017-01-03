package com.skripsi.mlearningsman1watampone;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadFileActivity extends Activity {
    public String matapelajaran, judul, nama, link;
	
    private ProgressDialog mProgressDialog;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
   
    File rootDir = Environment.getExternalStorageDirectory();
   
    public String fileName;
    public String fileURL;
   
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panel_downloadfile);
        
        Intent a = getIntent();
        matapelajaran = a.getStringExtra("par_matapelajaran");
        judul = a.getStringExtra("par_judul");
        nama = a.getStringExtra("par_nama");
        
        TextView mp = (TextView)findViewById(R.id.downloadfile_matapelajaran);
        TextView jd = (TextView)findViewById(R.id.downloadfile_judul);
        TextView nf = (TextView)findViewById(R.id.downloadfile_namafile);
        
        mp.setText(matapelajaran);
        jd.setText(judul);
        nf.setText(nama);
        
        Koneksi konek = new Koneksi();
        link = konek.link_koneksi();
        
        fileName = nama;
        fileURL = link+"/files_materi/"+fileName;
        
        Button btn_download = (Button)findViewById(R.id.downloadfile_btndownload);
        btn_download.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
		        checkAndCreateDirectory("/Materi_Download");
		       
		        new DownloadFileAsync().execute(fileURL);
			}
		});
        
    }
 
    class DownloadFileAsync extends AsyncTask<String, String, String> {
       
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }
       
        @Override
        protected String doInBackground(String... aurl) {

            try {
                URL u = new URL(fileURL);
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();
                int lenghtOfFile = c.getContentLength();
               
                FileOutputStream f = new FileOutputStream(new File(rootDir + "/Materi_Download/", fileName));
                InputStream in = c.getInputStream();

                byte[] buffer = new byte[1024];
                int len1 = 0;
                long total = 0;
               
                while ((len1 = in.read(buffer)) > 0) {
                    total += len1;
                    publishProgress("" + (int)((total*100)/lenghtOfFile));
                    f.write(buffer, 0, len1);
                }
                f.close();
               
            } catch (Exception e) {
                Log.d("Download file", e.getMessage());
            }
           
            return null;
        }
       
        protected void onProgressUpdate(String... progress) {
             Log.d("Download file",progress[0]);
             mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            Toast.makeText(DownloadFileActivity.this, "Download file selesai ..", Toast.LENGTH_LONG).show();
            finish();
        }
    }
   
    public void checkAndCreateDirectory(String dirName){
        File new_dir = new File( rootDir + dirName );
        if( !new_dir.exists() ){
            new_dir.mkdirs();
        }
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("File sedang di-download ..");
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMax(100);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(true);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }
    }
}