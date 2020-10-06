package com.th3md.babyneeds;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.th3md.babyneeds.adapter.RecycleViewAdapter;
import com.th3md.babyneeds.data.DatabaseHandler;
import com.th3md.babyneeds.model.BabyNeeds;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecycleViewAdapter recycleViewAdapter;
    private List<BabyNeeds> needsList;
    private DatabaseHandler db;

    private FloatingActionButton fab;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Button saveItem;

    private EditText title;
    private EditText qty;
    private EditText color;
    private EditText size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        db = new DatabaseHandler(this);

        fab = findViewById(R.id.fab);

        needsList = new ArrayList<>();
        needsList = db.getNeeds();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        needsList = db.getNeeds();
        recycleViewAdapter = new RecycleViewAdapter(this,needsList);
        recyclerView.setAdapter(recycleViewAdapter);
        recycleViewAdapter.notifyDataSetChanged();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopupDialog();
            }
        });

    }

    private void createPopupDialog() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup,null);
        title = view.findViewById(R.id.itemTitle);
        qty = view.findViewById(R.id.itemQty);
        color = view.findViewById(R.id.itemColor);
        size = view.findViewById(R.id.itemSize);
        saveItem = view.findViewById(R.id.save_item);
        builder.setView(view);

        dialog = builder.create();

        dialog.show();

        saveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!title.getText().toString().isEmpty()
                        && !qty.getText().toString().isEmpty()
                        && !color.getText().toString().isEmpty()
                        && !size.getText().toString().isEmpty()){
                    BabyNeeds need = new BabyNeeds(
                            title.getText().toString().trim(),
                            Integer.parseInt(qty.getText().toString().trim()),
                            color.getText().toString().trim(),
                            Integer.parseInt(size.getText().toString().trim())
                    );
                    db.addNeed(need);
                    Snackbar.make(v, "Saved !", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            startActivity(new Intent(ListActivity.this,ListActivity.class));
                            finish();
                        }
                    },1200);

                }else{
                    Snackbar.make(v, "Empty Fields Not Allowed :D", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

}
