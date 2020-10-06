package com.th3md.babyneeds.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.th3md.babyneeds.MainActivity;
import com.th3md.babyneeds.R;
import com.th3md.babyneeds.data.DatabaseHandler;
import com.th3md.babyneeds.model.BabyNeeds;


import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

    private Context context;
    private List<BabyNeeds> babyNeeds;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RecycleViewAdapter(Context context, List<BabyNeeds> babyNeedsList) {
        this.context = context;
        this.babyNeeds = babyNeedsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.need_row,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BabyNeeds need = babyNeeds.get(position);
        holder.title.setText(need.getTitle());
        holder.qty.setText(String.valueOf(need.getQty()));
        holder.color.setText(need.getColor());
        holder.size.setText(String.valueOf(need.getSize()));
        holder.dateAdded.setText(need.getDateAdded());

    }


    @Override
    public int getItemCount() {
        return babyNeeds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private TextView qty;
        private TextView color;
        private TextView size;
        private TextView dateAdded;

        private DatabaseHandler db;

        //private int id;

        private ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            title = itemView.findViewById(R.id.title_item);
            qty = itemView.findViewById(R.id.qty_item);
            color = itemView.findViewById(R.id.color_item);
            size = itemView.findViewById(R.id.size_item);
            dateAdded = itemView.findViewById(R.id.date_item);

            Button edit = itemView.findViewById(R.id.editButton);
            Button delete = itemView.findViewById(R.id.deleteButton);

            db = new DatabaseHandler(context);

            edit.setOnClickListener(this);
            delete.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {


            switch (v.getId()){
                case R.id.editButton:
                    updateItem(getAdapterPosition());
                    break;
                case R.id.deleteButton:
                    deleteItem(getAdapterPosition());
                    break;
            }
        }

        private void deleteItem(final int position){

            builder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation,null);

            Button noButton = view.findViewById(R.id.no_conf);
            Button yesButton = view.findViewById(R.id.yes_conf);

            builder.setView(view);

            dialog = builder.create();

            dialog.show();

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BabyNeeds need = babyNeeds.get(position);
                    db.deleteNeed(need.getId());
                    babyNeeds.remove(position);
                    notifyItemRemoved(position);
                    dialog.dismiss();
                    if(babyNeeds.size() == 0){
                        context.startActivity(new Intent(context, MainActivity.class));
                        ((Activity) context).finish();
                    }
                }
            });

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        private void updateItem(final int position){
            builder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.popup,null);

            final TextView title = view.findViewById(R.id.itemTitle);
            final TextView qty = view.findViewById(R.id.itemQty);
            final TextView color = view.findViewById(R.id.itemColor);
            final TextView size= view.findViewById(R.id.itemSize);

            Button save = view.findViewById(R.id.save_item);

            final BabyNeeds need = babyNeeds.get(position);

            title.setText(need.getTitle());
            qty.setText(String.valueOf(need.getQty()));
            color.setText(need.getColor());
            size.setText(String.valueOf(need.getSize()));

            builder.setView(view);

            dialog = builder.create();
            dialog.show();

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final BabyNeeds updatedNeed = new BabyNeeds();
                    updatedNeed.setId(need.getId());
                    updatedNeed.setTitle(title.getText().toString().trim());
                    updatedNeed.setQty(Integer.parseInt(qty.getText().toString()));
                    updatedNeed.setColor(color.getText().toString().trim());
                    updatedNeed.setSize(Integer.parseInt(size.getText().toString().trim()));

                    if(!title.getText().toString().isEmpty()
                    && !qty.getText().toString().isEmpty()
                    && !color.getText().toString().isEmpty()
                    && !size.getText().toString().isEmpty()){
                        db.updateNeed(updatedNeed);
                        Snackbar.make(view,"Updated !",Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();

                        babyNeeds.get(position).setTitle(updatedNeed.getTitle());
                        babyNeeds.get(position).setQty(updatedNeed.getQty());
                        babyNeeds.get(position).setColor(updatedNeed.getColor());
                        babyNeeds.get(position).setSize(updatedNeed.getSize());
                        notifyDataSetChanged();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        },600);
                    }else{
                        Snackbar.make(view,"Fields Empty !",Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                }
            });
        }
    }
}
