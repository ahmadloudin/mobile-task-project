package com.example.kickmyb.http;

import org.kickmyb.transfer.AddTaskRequest;
import org.kickmyb.transfer.HomeItemResponse;
import org.kickmyb.transfer.SigninRequest;
import org.kickmyb.transfer.SigninResponse;
import org.kickmyb.transfer.SignupRequest;
import org.kickmyb.transfer.TaskDetailPhotoResponse;
import org.kickmyb.transfer.TaskDetailResponse;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {


    @POST("api/id/signup")
    Call<SigninResponse> signUp(@Body SignupRequest signupRequest);

    @POST("api/id/signin")
    Call<SigninResponse> signIn(@Body SigninRequest signinRequest);

    @POST("api/id/signout")
    Call<String> signOut();

    @POST("api/add")
    Call<String> add(@Body AddTaskRequest add);

    @GET("api/home")
    Call<List<HomeItemResponse>> home();

    @GET("api/detail/{id}")
    Call<TaskDetailResponse> detail(@Path("id") long id);

    @GET("api/progress/{taskId}/{value}")
    Call<String> progress(@Path("taskId")long id, @Path("value") int valeur);

}
