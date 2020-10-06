package com.th3md.babyneeds;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.th3md.babyneeds.adapter.RecycleViewAdapter;
import com.th3md.babyneeds.data.DatabaseHandler;
import com.th3md.babyneeds.model.BabyNeeds;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Button saveItem;
    private EditText title;
    private EditText qty;
    private EditText color;
    private EditText size;
    private DatabaseHandler db;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DatabaseHandler(this);

        if(db.getCount() > 0){
            startActivity(new Intent(MainActivity.this,ListActivity.class));
            finish();
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createPopupDialog();
                //updateView();

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
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
                        startActivity(new Intent(MainActivity.this,ListActivity.class));
                    }
                },600);

                }else{
                    Snackbar.make(v, "Empty Fields Not Allowed :D", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
