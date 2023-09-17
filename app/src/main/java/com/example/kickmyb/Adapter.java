package com.example.kickmyb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kickmyb.http.RetrofitUtil;
import com.example.kickmyb.http.Service;

import org.kickmyb.transfer.HomeItemResponse;
import org.kickmyb.transfer.TaskDetailResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    public List<HomeItemResponse> tacheList;
    String errorMessage;
    public Context context;
    ProgressDialog progressD;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tV, date, tempsEcoule, pourcentage;
        public LinearLayout layout;
        public ViewHolder(LinearLayout view) {
            super(view);
            // Define click listener for the ViewHolder's View
            layout = view.findViewById(R.id.layout1);
            tV = view.findViewById(R.id.nom);
            date = view.findViewById(R.id.date_picker);
            tempsEcoule = view.findViewById(R.id.temps);
            pourcentage = view.findViewById(R.id.pourcent);
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public Adapter(Context context) {
        this.context = context;
        tacheList = new ArrayList<>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LinearLayout view = (LinearLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_accueil, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        HomeItemResponse tacheCourante = this.tacheList.get(position);

        viewHolder.tV.setText(tacheCourante.name);
        viewHolder.pourcentage.setText(String.valueOf(tacheCourante.percentageDone));
        viewHolder.date.setText(tacheCourante.deadline.toString());
        viewHolder.tempsEcoule.setText(String.valueOf(tacheCourante.percentageTimeSpent));
        viewHolder.layout.setOnClickListener(v -> {
          String text1 = context.getString(R.string.detail1);
            String text2 = context.getString(R.string.detail2);
            progressD = ProgressDialog.show(context, text1,
                    text2, true);
            if(errorMessage == null){
                Service service = RetrofitUtil.get();
                service.detail(tacheCourante.id).enqueue(new Callback<TaskDetailResponse>() {
                    @Override
                    public void onResponse(Call<TaskDetailResponse> call, Response<TaskDetailResponse> response) {
                        progressD.dismiss();
                        Intent i = new Intent(viewHolder.layout.getContext(),ActivityConsultation.class);
                        i.putExtra("id", tacheCourante.id);
                        i.putExtra("nom", tacheCourante.name);
                        i.putExtra("pourcentage", String.valueOf(tacheCourante.percentageDone));
                        i.putExtra("deadline", tacheCourante.deadline.toString());
                        i.putExtra("tempsEcoule", String.valueOf(tacheCourante.percentageTimeSpent));
                        viewHolder.itemView.getContext().startActivity(i);
                    }

                    @Override
                    public void onFailure(Call<TaskDetailResponse> call, Throwable t) {
                        progressD.dismiss();
                    if(t.getMessage().equals("Failed to connect to /10.0.2.2:8080"))
                    {
                        errorMessage =  context.getString(R.string.erreurReseau);
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                    }
                        }
                });
            }

        });
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return tacheList.size();
    }
}
