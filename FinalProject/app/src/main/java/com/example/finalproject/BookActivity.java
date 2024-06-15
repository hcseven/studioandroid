package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.finalproject.databinding.ActivityBookBinding;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {

    int bookid;
    InputAdapter adapter;
    ActivityBookBinding binding;
    ArrayList<Inputs> inputs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        bookid = intent.getIntExtra("BookID", -1);

        getInputs(0);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    public void getInputs(int inputtype)
    {
        try{
            inputs = new ArrayList<Inputs>();
            SQLiteDatabase database = this.openOrCreateDatabase("BookApp", Context.MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS input (id INTEGER PRIMARY KEY AUTOINCREMENT, Kitapid " +
                    "INTEGER, Pagenumber INTEGER, Input VARCHAR, Inputtype INTEGER, Title VARCHAR)");
            String sqlcom = "";
            if (inputtype == 1 || inputtype == 2 || inputtype == 3)
                sqlcom = "SELECT * FROM input WHERE Kitapid = " + bookid + " AND Inputtype = " + inputtype;
            else
                sqlcom = "SELECT * FROM input WHERE Kitapid = " + bookid;
            Cursor cursor = database.rawQuery(sqlcom, null);
            int index = cursor.getColumnIndex("Input");
            int titleindex = cursor.getColumnIndex("Title");
            int kitapid = cursor.getColumnIndex("Kitapid");
            int inputtypeid = cursor.getColumnIndex("Inputtype");
            int inputid = cursor.getColumnIndex("id");
            while (cursor.moveToNext())
            {
                inputs.add(new Inputs(cursor.getInt(inputid), cursor.getString(titleindex), cursor.getString(index),
                        cursor.getInt(kitapid), cursor.getInt(inputtypeid)));
            }
            cursor.close();

            binding.recyc.setLayoutManager(new LinearLayoutManager(this));
            adapter = new InputAdapter(inputs);
            binding.recyc.setAdapter(adapter);

        }
        catch (Exception e) {

        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.addmenu_review)
        {
            Intent intent = new Intent(this, AddInputActivity.class);
            intent.putExtra("inputtype", 2);
            intent.putExtra("bookid", bookid);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.addmenu_note)
        {
            Intent intent = new Intent(this, AddInputActivity.class);
            intent.putExtra("inputtype", 3);
            intent.putExtra("bookid", bookid);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.addmenu_quote)
        {
            Intent intent = new Intent(this, AddInputActivity.class);
            intent.putExtra("inputtype", 1);
            intent.putExtra("bookid", bookid);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.showmenu_note)
        {
            getInputs(3);
        }
        else if(item.getItemId() == R.id.showmenu_quote)
        {
            getInputs(1);
        }
        else if(item.getItemId() == R.id.showmenu_review)
        {
            getInputs(2);
        }


        return super.onOptionsItemSelected(item);
    }
}