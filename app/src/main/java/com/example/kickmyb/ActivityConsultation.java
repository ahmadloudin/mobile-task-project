package com.example.kickmyb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.kickmyb.databinding.ActivityAccueilBinding;
import com.example.kickmyb.databinding.ActivityConsultationBinding;
import com.example.kickmyb.http.RetrofitUtil;
import com.example.kickmyb.http.Service;
import com.google.android.material.navigation.NavigationView;

import org.kickmyb.transfer.HomeItemResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityConsultation extends AppCompatActivity {

    SeekBar seekbar;
    TextView textView;
    ProgressDialog progressD;
    TextView username;
    Singleton singleton = com.example.kickmyb.Singleton.getInstance();
    private ActivityConsultationBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation);
        binding = ActivityConsultationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        navigationView();
        setSeekbar();

        binding.nomTache.setText(getIntent().getStringExtra("nom"));
        binding.pourcent.setText(getIntent().getStringExtra("pourcentage"));
        binding.deadline.setText(getIntent().getStringExtra("deadline"));
        binding.tempsEcoule.setText(getIntent().getStringExtra("tempsEcoule"));

        binding.btnUpdate.setOnClickListener(v -> {
            String text1 = getString(R.string.update1);
            String text2 = getString(R.string.update2);
            progressD = ProgressDialog.show(ActivityConsultation.this, text1,
                    text2, true);
            progress();
        });
    }

    public void progress() {

        Service service = RetrofitUtil.get();
        Long id = Long.valueOf(getIntent().getLongExtra("id", 0));
        int value;
        if(textView.getText().toString() != ""){
           value = Integer.parseInt(textView.getText().toString());
            service.progress(id, value).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    progressD.dismiss();
                    Intent intent = new Intent(ActivityConsultation.this, AccueilActivity.class);
                    startActivity(intent);
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    progressD.dismiss();
                    if(t.getMessage().equals("Failed to connect to /10.0.2.2:8080"))
                    {
                        String errorMessage = getString(R.string.erreurReseau);
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
            service.progress(id, 0).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    progressD.dismiss();
                    Intent intent = new Intent(ActivityConsultation.this, AccueilActivity.class);
                    startActivity(intent);
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    progressD.dismiss();
                    if(t.getMessage().equals("Failed to connect to /10.0.2.2:8080"))
                    {
                        String errorMessage = getString(R.string.erreurReseau);
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }


    }

    private void navigationView(){
        NavigationView nV = binding.navView;
        DrawerLayout d1 = binding.drawerLayout;
        View header = nV.getHeaderView(0);
        username = header.findViewById(R.id.header);
        username.setText(singleton.getText());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, d1, R.string.drawer_open, R.string.drawer_closed ){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.drawer_closed);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(R.string.drawer_open);
            }
        };

        d1.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

        nV.setNavigationItemSelectedListener(item -> {
            int i = item.getItemId();

            switch(i){
                case R.id.nav_accueil:
                    Intent intent = new Intent(ActivityConsultation.this,AccueilActivity.class);
                    startActivity(intent);
                    break;

                case R.id.nav_ajoutTache:
                    Intent intent1 = new Intent(ActivityConsultation.this, CreationActivity.class);
                    startActivity(intent1);
                    break;

                case R.id.nav_deconnexion:
                    String text1 = getString(R.string.signOut1);
                    String text2 = getString(R.string.signOut2);
                    progressD = ProgressDialog.show(ActivityConsultation.this, text1,
                            text2, true);
                    Service service = RetrofitUtil.get();
                    service.signOut().enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            progressD.dismiss();
                            Intent intent2 = new Intent(ActivityConsultation.this,MainActivity.class);
                            startActivity(intent2);
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            progressD.dismiss();
                            if(t.getMessage().equals("Failed to connect to /10.0.2.2:8080"))
                            {
                                String errorMessage = getString(R.string.erreurReseau);
                                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    break;
            }
            d1.closeDrawers();
            return false;
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){

            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        actionBarDrawerToggle.syncState();

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    public void setSeekbar(){

        seekbar = findViewById(R.id.seekbar);
        textView = findViewById(R.id.pourcentage);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
