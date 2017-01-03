package com.skripsi.mlearningsman1watampone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MenuUtamaActivity extends Activity {
	
	public String nis;
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.panel_menuutama);
        
        Bundle b = getIntent().getExtras();
		nis = b.getString("par_kode");
		
		TextView txt_datang = (TextView) findViewById(R.id.txt_datang);
		txt_datang.setText("Selamat datang, Anda login sebagai : "+nis);
		
        Button btn_profil = (Button) findViewById(R.id.btn_profil);
        Button btn_kelas = (Button) findViewById(R.id.btn_kelas);
        Button btn_matapelajaran = (Button) findViewById(R.id.btn_matapelajaran);
        Button btn_downloadmateri = (Button) findViewById(R.id.btn_Materi);
        Button btn_tugas = (Button) findViewById(R.id.btn_tugas);
        Button btn_nilai = (Button) findViewById(R.id.btn_nilai);
        Button btn_about = (Button) findViewById(R.id.btn_about);
        Button btn_exit = (Button) findViewById(R.id.btn_exit);
        
        btn_profil.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {						
				Intent i = new Intent(getApplicationContext(), ProfilActivity.class);
				i.putExtra("par_kode", nis);
				startActivity(i);
			}
		});

        btn_kelas.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {						
				Intent i = new Intent(getApplicationContext(), KelasActivity.class);
				i.putExtra("par_kode", nis);
				startActivity(i);
			}
		});

        btn_matapelajaran.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {						
				Intent i = new Intent(getApplicationContext(), MatapelajaranActivity.class);
				i.putExtra("par_kode", nis);
				startActivity(i);
			}
		});

        btn_downloadmateri.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {						
				Intent i = new Intent(getApplicationContext(), DownloadMateriActivity.class);
				i.putExtra("par_kode", nis);
				startActivity(i);
			}
		});

        btn_tugas.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {						
				Intent i = new Intent(getApplicationContext(), TugasActivity.class);
				i.putExtra("par_kode", nis);
				startActivity(i);
			}
		});

        btn_nilai.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {						
				Intent i = new Intent(getApplicationContext(), NilaiActivity.class);
				i.putExtra("par_kode", nis);
				startActivity(i);
			}
		});

        btn_about.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {						
				Intent i = new Intent(getApplicationContext(), AboutActivity.class);
				startActivity(i);
			}
		});

        btn_exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {	
				close();
			}
		});
	}
	
	public void close(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Apakah Anda Benar-Benar ingin keluar?")
        .setCancelable(false)
        .setPositiveButton("Ya",
        		new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int id) {
        			MenuUtamaActivity.this.finish();
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
         close();
        }   
        return super.onKeyDown(keyCode, event);
     }
}