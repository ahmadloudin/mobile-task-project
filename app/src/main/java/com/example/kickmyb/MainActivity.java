package com.example.kickmyb;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kickmyb.databinding.ActivityMainBinding;
import com.example.kickmyb.http.RetrofitUtil;
import com.example.kickmyb.http.Service;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;
import org.kickmyb.transfer.HomeItemResponse;
import org.kickmyb.transfer.SigninRequest;
import org.kickmyb.transfer.SigninResponse;
import org.kickmyb.transfer.SignupRequest;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    ProgressDialog progressD;
    Singleton singleton = com.example.kickmyb.Singleton.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.inscription.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActivityInscription.class );
            startActivity(intent);
        });

        binding.connexion.setOnClickListener(v -> {
            httpSignIn();
        });


    }
    public void httpSignIn(){
        String text1 = getString(R.string.signIn1);
        String text2 = getString(R.string.signIn2);
        progressD = ProgressDialog.show(MainActivity.this, text1,
                text2, true);
        Service service = RetrofitUtil.get();
        SigninRequest signinRequest = new SigninRequest();
        signinRequest.password = binding.password.getText().toString();
        signinRequest.username = binding.username.getText().toString();
        EditText editText = findViewById(R.id.username);
        service.signIn(signinRequest).enqueue(new Callback<SigninResponse>() {
            @Override
            public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {


                if (response.isSuccessful()) {
                    progressD.dismiss();
                    Intent i = new Intent(MainActivity.this, AccueilActivity.class);
                    singleton.setText(editText.getText().toString());
                    startActivity(i);
                } else {
                    if (response.errorBody() != null) {
                            progressD.dismiss();
                            String errorMessage = getString(R.string.erreurAuth);
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                    }

                }
            }
            @Override
            public void onFailure(Call<SigninResponse> call, Throwable t) {
                if(t.getMessage().equals("Failed to connect to /10.0.2.2:8080"))
                {
                    progressD.dismiss();
                    String errorMessage = getString(R.string.erreurReseau);
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}