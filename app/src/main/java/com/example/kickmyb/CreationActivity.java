package com.example.kickmyb;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.kickmyb.databinding.ActivityAccueilBinding;
import com.example.kickmyb.databinding.ActivityCreationBinding;
import com.example.kickmyb.http.RetrofitUtil;
import com.example.kickmyb.http.Service;
import com.google.android.material.navigation.NavigationView;

import org.kickmyb.transfer.AddTaskRequest;
import org.kickmyb.transfer.HomeItemResponse;
import org.kickmyb.transfer.SignupRequest;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreationActivity extends AppCompatActivity {

    private ActivityCreationBinding binding;
    EditText editText;
    Calendar calendar;
    ProgressDialog progressD;
    TextView username;

    String message;
    Singleton singleton = com.example.kickmyb.Singleton.getInstance();
    private ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);

        binding = ActivityCreationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        navigationView();
        selectDate();
        binding.btnCreation.setOnClickListener(v -> {
            String text1 = getString(R.string.task1);
            String text2 = getString(R.string.task2);
            progressD = ProgressDialog.show(CreationActivity.this, text1,
                    text2, true);
            addTask();
        });

    }
    public void addTask() {

        Service service = RetrofitUtil.get();
        AddTaskRequest add = new AddTaskRequest();
        add.name = binding.nom.getText().toString();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = simpleDateFormat.parse(binding.dateId.getText().toString());
                add.deadline = date;
                service.add(add).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            progressD.dismiss();
                            Intent intent = new Intent(CreationActivity.this, AccueilActivity.class);
                            startActivity(intent);
                        } else {
                            if (response.errorBody() != null) {
                                progressD.dismiss();
                                try {
                                    message = response.errorBody().string().replace("\"", "");
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                switch (message) {
                                    case "Empty":
                                        String errorMessage = getString(R.string.erreurNom);
                                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();;
                                        break;
                                    case "TooShort":
                                        errorMessage = getString(R.string.erreurTacheLength);
                                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                                        break;
                                    case "Existing":
                                        errorMessage = getString(R.string.erreurTacheExist);
                                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                                        break;
                                    default:
                                        Toast.makeText(getApplicationContext(), "Erreur inconnu", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }

                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                        if(t.getMessage().equals("Failed to connect to /10.0.2.2:8080"))
                        {
                            progressD.dismiss();
                            String errorMessage = getString(R.string.erreurReseau);
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } catch (ParseException e) {
                progressD.dismiss();
                String errorMessage = getString(R.string.erreurDate);
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        }


    //}
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
                    Intent intent = new Intent(CreationActivity.this,AccueilActivity.class);
                    startActivity(intent);
                    break;

                case R.id.nav_ajoutTache:
                    Intent intent1 = new Intent(CreationActivity.this, CreationActivity.class);
                    startActivity(intent1);
                    break;

                case R.id.nav_deconnexion:
                    String text1 = getString(R.string.signOut1);
                    String text2 = getString(R.string.signOut2);
                    progressD = ProgressDialog.show(CreationActivity.this, text1,
                            text2, true);
                    Service service = RetrofitUtil.get();
                    service.signOut().enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            progressD.dismiss();
                            Intent intent2 = new Intent(CreationActivity.this,MainActivity.class);
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
    private void selectDate() {
        editText = findViewById(R.id.date_id);
        calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            updateLabel();
        };

        editText.setOnClickListener(view -> {
            new DatePickerDialog(CreationActivity.this, date
                    , calendar.get(Calendar.YEAR)
                    , calendar.get(Calendar.MONTH)
                    , calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void updateLabel() {
        String format = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CANADA);
        editText.setText(dateFormat.format(calendar.getTime()));
    }
}
