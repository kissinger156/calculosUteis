package com.severocarlos.recyclerview.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.severocarlos.recyclerview.R;

public class PdgActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdg);

        TextView viewPdg = findViewById(R.id.text_site);

        viewPdg.setOnClickListener(sendListener);
    }

    private View.OnClickListener sendListener = v -> {
        Uri uri = Uri.parse("https://www.tuasaude.com/gordura-corporal-ideal/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    };
}
