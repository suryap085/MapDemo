package com.map.mapview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.map.mapview.R;
import com.map.mapview.modal.Company;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.Viewholder> {

    private Context context;
    private List<Company> companyModelArrayList;

    // Constructor
    public CompanyAdapter(Context context, List<Company> companyModelArrayList) {
        this.context = context;
        this.companyModelArrayList = companyModelArrayList;
    }

    @NonNull
    @Override
    public CompanyAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.company_item, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyAdapter.Viewholder holder, int position) {
        Company model = companyModelArrayList.get(position);
        holder.companyName.setText(model.getCompany_name());
        holder.txtratebar.setText(model.getAvg_rating());
        holder.companyDes.setText(model.getCompany_description());
        Picasso.get().load(model.getCompany_image_url()).into(holder.imgview);
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return companyModelArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private ImageView imgview;
        private TextView companyName, txtratebar, companyDes;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imgview = itemView.findViewById(R.id.imgview);
            companyName = itemView.findViewById(R.id.companyName);
            txtratebar = itemView.findViewById(R.id.txtratebar);
            companyDes = itemView.findViewById(R.id.companyDes);
        }
    }
}
