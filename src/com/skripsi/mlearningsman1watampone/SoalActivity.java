package com.skripsi.mlearningsman1watampone;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class SoalActivity extends TabActivity {
	// TabSpec Names
	private static final String PG_SPEC = "SOAL PG";
	private static final String ESSAY_SPEC = "SOAL ESSAY";
	
	public String id, nis, nomor, soal;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.panel_soal);
        
        Intent in = getIntent();
        id = in.getStringExtra("par_id");
        nis = in.getStringExtra("par_nis");
        nomor = in.getStringExtra("par_nomor");
        soal = in.getStringExtra("par_soal");
        
        TabHost tabHost = getTabHost();
        
        TabSpec pgSpec = tabHost.newTabSpec(PG_SPEC);
        pgSpec.setIndicator(PG_SPEC, getResources().getDrawable(R.drawable.pg));
        Intent pgIntent = new Intent(this, SoalPgActivity.class);
        Bundle bundle_pg = new Bundle();
        bundle_pg.putString("par_id", id);
        bundle_pg.putString("par_nis", nis);
        bundle_pg.putString("par_nomor", nomor);
        bundle_pg.putString("par_soal", soal);
        pgSpec.setContent(pgIntent.putExtras(bundle_pg));
        
        TabSpec essaySpec = tabHost.newTabSpec(ESSAY_SPEC);
        essaySpec.setIndicator(ESSAY_SPEC, getResources().getDrawable(R.drawable.essay));
        Intent essayIntent = new Intent(this, SoalEssayActivity.class);
        Bundle bundle_essay = new Bundle();
        bundle_essay.putString("par_id", id);
        bundle_essay.putString("par_nis", nis);
        bundle_essay.putString("par_nomor", nomor);
        essaySpec.setContent(essayIntent.putExtras(bundle_essay));
        
        tabHost.addTab(pgSpec);
        tabHost.addTab(essaySpec);
    }
    
}