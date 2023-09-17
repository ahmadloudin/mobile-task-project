package com.example.kickmyb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kickmyb.databinding.ActivityConsultationBinding;
import com.example.kickmyb.databinding.ActivityInscriptionBinding;
import com.example.kickmyb.databinding.ActivityMainBinding;
import com.example.kickmyb.http.RetrofitUtil;
import com.example.kickmyb.http.Service;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.kickmyb.transfer.HomeItemResponse;
import org.kickmyb.transfer.SigninResponse;
import org.kickmyb.transfer.SignupRequest;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityInscription extends AppCompatActivity {

    private ActivityInscriptionBinding binding;

    EditText editText2;
    ProgressDialog progressD;
    Singleton singleton = com.example.kickmyb.Singleton.getInstance();
    String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        binding = ActivityInscriptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EditText editText = findViewById(R.id.motdepasse);
        EditText editText1 = findViewById(R.id.confirmation);


        binding.btnInscription.setOnClickListener(v -> {
            if (editText.getText().toString().equals(editText1.getText().toString())) {
                String text1 = getString(R.string.signUp1);
                String text2 = getString(R.string.signUp2);
                progressD = ProgressDialog.show(ActivityInscription.this, text1,
                        text2, true);
                httpSignUp();
            }
            else{
                String message = getString(R.string.erreurMdpIdentique);
                Toast.makeText(ActivityInscription.this, message, Toast.LENGTH_LONG).show();
            }

        });

    }
    public void httpSignUp() {

        Service service = RetrofitUtil.get();
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.password = binding.motdepasse.getText().toString();
        signupRequest.username = binding.utilisateur.getText().toString();
        editText2 = findViewById(R.id.utilisateur);

        service.signUp(signupRequest).enqueue(new Callback<SigninResponse>() {
            @Override
            public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {
                if (response.isSuccessful()) {
                    progressD.dismiss();
                    Intent i = new Intent(ActivityInscription.this, AccueilActivity.class);
                    singleton.setText(editText2.getText().toString());
                    startActivity(i);
                } else {
                    if (response.errorBody() != null) {
                        progressD.dismiss();
                        try {
                            message = response.errorBody().string().replace("\"", "");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        switch (message) {
                            case "UsernameTooShort":
                                String errorMessage = getString(R.string.erreurUserLength);
                                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                                break;
                            case "PasswordTooShort":
                                errorMessage = getString(R.string.erreurPasswordLength);
                                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                                break;
                            case "UsernameAlreadyTaken":
                                errorMessage = getString(R.string.erreurUsernameTaken);
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
                public void onFailure (Call < SigninResponse > call, Throwable t){
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