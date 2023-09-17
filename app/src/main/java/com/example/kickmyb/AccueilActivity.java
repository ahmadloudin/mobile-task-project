package com.example.kickmyb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kickmyb.databinding.ActivityAccueilBinding;
import com.example.kickmyb.databinding.ActivityMainBinding;
import com.example.kickmyb.http.RetrofitUtil;
import com.example.kickmyb.http.Service;
import com.google.android.material.navigation.NavigationView;

import org.kickmyb.transfer.AddTaskRequest;
import org.kickmyb.transfer.HomeItemResponse;
import org.kickmyb.transfer.SignupRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccueilActivity extends AppCompatActivity {

    private ActivityAccueilBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    ProgressDialog progressD;
    Adapter adapter;
    Singleton singleton = com.example.kickmyb.Singleton.getInstance();
    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_accueil);
        binding = ActivityAccueilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        navigationView();
        initRecycler();
        String text1 = getString(R.string.getTask1);
        String text2 = getString(R.string.getTask2);
        progressD = ProgressDialog.show(AccueilActivity.this, text1,
                text2, true);
        home();

    }


    public void home() {


        Service service = RetrofitUtil.get();
        service.home().enqueue(new Callback<List<HomeItemResponse>>() {


            @Override
            public void onResponse(Call<List<HomeItemResponse>> call, Response<List<HomeItemResponse>> response) {
                    progressD.dismiss();
                    adapter.tacheList.addAll(response.body());
                    adapter.notifyDataSetChanged();

            }


            @Override
            public void onFailure(Call<List<HomeItemResponse>> call, Throwable t) {
                progressD.dismiss();
                if(t.getMessage().equals("Failed to connect to /10.0.2.2:8080"))
                {
                    String errorMessage = getString(R.string.erreurReseau);
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });

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
                    Intent intent = new Intent(AccueilActivity.this,AccueilActivity.class);
                    startActivity(intent);
                    break;

                case R.id.nav_ajoutTache:
                    Intent intent1 = new Intent(AccueilActivity.this, CreationActivity.class);
                    startActivity(intent1);
                    break;

                case R.id.nav_deconnexion:
                    String text1 = getString(R.string.signOut1);
                    String text2 = getString(R.string.signOut2);
                    progressD = ProgressDialog.show(AccueilActivity.this, text1,
                            text2, true);
                    Service service = RetrofitUtil.get();
                    service.signOut().enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            progressD.dismiss();
                            Intent intent2 = new Intent(AccueilActivity.this,MainActivity.class);
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
    private void initRecycler(){
        RecyclerView recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new Adapter(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       MenuInflater inflater =  getMenuInflater();
       inflater.inflate(R.menu.option_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        if(item.getItemId() == R.id.menu_creation){
            Intent intent = new Intent(this, CreationActivity.class);
            startActivity(intent);
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


}
