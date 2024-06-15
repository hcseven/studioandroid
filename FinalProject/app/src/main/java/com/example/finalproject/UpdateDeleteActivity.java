package com.example.finalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalproject.databinding.ActivityBookBinding;
import com.example.finalproject.databinding.ActivityUpdateDeleteBinding;

public class UpdateDeleteActivity extends AppCompatActivity {

    TextView pageNumber, input, title;

    int bookID;
    ActivityUpdateDeleteBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateDeleteBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        pageNumber = binding.updatePagenumber;
        input = binding.updateInput;
        title = binding.updateTitle;

        Intent intent = getIntent();
        int inputID = intent.getIntExtra("inputID", 0);
        SQLiteDatabase database = this.openOrCreateDatabase("BookApp", MODE_PRIVATE, null);

        //veritabanımdan güncellemeye uygun, güncellenebilecek verileri alıp, edittextlerime aktarıyorum.
        try {
            System.out.println("INPUT ID : " + inputID);
            String sqlcom = "SELECT * FROM input WHERE id = " + inputID;
            Cursor cursor = database.rawQuery(sqlcom, null);
            int inputtextID = cursor.getColumnIndex("Input");
            int pagenumberID = cursor.getColumnIndex("Pagenumber");
            int titleID = cursor.getColumnIndex("Title");
            int inputbookID = cursor.getColumnIndex("Kitapid");
            cursor.moveToNext();
            bookID = cursor.getInt(inputbookID);
            title.setText(cursor.getString(titleID));
            input.setText(cursor.getString(inputtextID));
            pageNumber.setText(String.valueOf(cursor.getInt(pagenumberID)));
            cursor.close();
        } catch (Exception e) {

        }

        //bu update button. değerler değiştikten sonra kontrollerimi yapıp, veritabanında güncelliyorum.
        binding.updateUpdatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num;
                if (pageNumber.getText().toString().equals(""))
                    num = -1;
                else
                    num = Integer.parseInt(pageNumber.getText().toString());

                if (input.getText().toString().equals("") || title.getText().toString().equals(""))
                    Toast.makeText(getBaseContext(), "Can't be null value", Toast.LENGTH_SHORT).show();
                else
                {
                    database.execSQL("UPDATE input SET Pagenumber = ?, Input = ?,  Title = ? WHERE id = ?",
                            new Object[]{Integer.parseInt(pageNumber.getText().toString()),
                                    input.getText().toString(),  title.getText().toString(), inputID});
                    goActivity();
                }
            }
        });

        //bu delete button. burada alert dialog da kullandım. eğer kullanıcı sil tuşuna basarsa dialog ekranı
        // karşısına gelecek ve sil derse input silinecek.
        binding.updateDeletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder( UpdateDeleteActivity.this )
                        .setTitle( "Input Deleting" )
                        .setMessage( "Do you want delete this input?" )
                        .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                database.delete("input", "id = " + inputID, null );
                                Toast.makeText(getBaseContext(), "Input Deleted", Toast.LENGTH_SHORT).show();
                                finish();
                                goActivity();
                            }
                        })
                        .setNegativeButton( "No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        } )
                        .show();


            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //bu da g,ncelleme veya silme işlemlerinden sonra bizim yeniden ait olduğu kitabın alıntılarına gitmemizi
    // sağlamak için yazdığım fonksiyon.
    public void goActivity()
    {
        Intent intent = new Intent(this, BookActivity.class);
        intent.putExtra("BookID", bookID);
        startActivity(intent);
    }

}