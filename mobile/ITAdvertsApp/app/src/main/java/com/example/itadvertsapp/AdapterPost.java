package com.example.itadvertsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.HolderPost> {

    private Context context;
    private ArrayList<ModelPost> postArrayList;

    public AdapterPost(Context context, ArrayList<ModelPost> arrayList) {
        this.context = context;
        this.postArrayList = arrayList;
    }

    @NonNull
    @Override
    public HolderPost onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_layout, parent, false);
        return new HolderPost(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPost holder, int position) {
        ModelPost model = postArrayList.get(position);

        //get data
        String id = model.getId();
        String title = model.getTitle();
        String company = model.getCompany();
        String category = model.getCategory();
        String salary_from = model.getSalary_from();
        String salary_to = model.getSalary_to();
        String location = model.getLocation();
        String formatted_date = model.getFormatted_date();

        //set data

        holder.postTitle.setText(title);
        holder.companyTV.setText(company);
        holder.locationTV.setText(location);
        holder.categoryTV.setText(category);
        holder.salaryFrom.setText(salary_from);
//        holder.salaryTo.setText(salary_to);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostContentActivity.class);
                intent.putExtra("postId", id);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    class HolderPost extends RecyclerView.ViewHolder{

        ImageButton btn_more;
        TextView postTitle, companyTV, locationTV, categoryTV,
                salaryFrom, salaryTo;

        public HolderPost(@NonNull View itemView) {
            super(itemView);

            //region UI view init
            btn_more = itemView.findViewById(R.id.btn_more);
            postTitle = itemView.findViewById(R.id.postTitle);
            companyTV = itemView.findViewById(R.id.companyTV);
            locationTV = itemView.findViewById(R.id.locationTV);
            categoryTV = itemView.findViewById(R.id.categoryTV);
            salaryFrom = itemView.findViewById(R.id.contentSalary);

            //endregion
        }
    }
}