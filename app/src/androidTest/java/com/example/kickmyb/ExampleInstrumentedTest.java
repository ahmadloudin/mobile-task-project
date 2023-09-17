package com.example.kickmyb;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kickmyb.transfer.SigninRequest;
import org.kickmyb.transfer.SigninResponse;
import org.kickmyb.transfer.SignupRequest;

import static org.junit.Assert.*;

import com.example.kickmyb.http.RetrofitUtil;
import com.example.kickmyb.http.Service;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.kickmyb", appContext.getPackageName());
    }



    @Test
    public void testCookie() throws IOException{
        Service service = RetrofitUtil.get();
        for(int i = 0;i < 10; i++) {
            SigninRequest signinRequest = new SigninRequest();
            signinRequest.password = "allo";
            signinRequest.username = "bonjour";
            Call<SigninResponse> call = service.signIn(signinRequest);
            Response<SigninResponse> response = call.execute();
            String resultat = response.body().username;
            Log.i("RESULTAT", resultat);
        }
    }
}