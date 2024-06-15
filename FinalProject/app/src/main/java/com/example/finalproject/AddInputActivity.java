package com.example.finalproject;

import static java.security.AccessController.getContext;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import com.example.finalproject.databinding.ActivityAddInputBinding;
import com.example.finalproject.databinding.ActivityMainBinding;

public class AddInputActivity extends AppCompatActivity {

    SQLiteDatabase database;
    ActivityAddInputBinding binding;
    EditText pageNumber, input, title;
    TextView txtName;
    int bookid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ViewBinding işlemimi yapıyorum.
        binding = ActivityAddInputBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ////Ekleyeceğim girdiye dair referansları aldım
        pageNumber = binding.addinputPagenumber;
        input = binding.addinputInput;
        title = binding.addinputTitle;
        txtName = binding.addinputBookname;

        //kitabımın id'sini ve inputumun türünü diğer sayfalardan seçilerek buraya gönderdiğim için öncelikle o
        // değerleri alıyorum
        Intent intent = getIntent();
        int inputtype = intent.getIntExtra("inputtype", 3);
        bookid = intent.getIntExtra("bookid", 0);

        database = this.openOrCreateDatabase("BookApp", MODE_PRIVATE, null);

        try {
            //girdi eklenen kitabın ismini veritabanından alıp, üst kısımdaki textview'e yazdırıyorum.
            database = this.openOrCreateDatabase("BookApp", MODE_PRIVATE, null);
            String sqlcom = "SELECT * FROM book WHERE id = " + bookid;
            Cursor cursor = database.rawQuery(sqlcom, null);
            int bookcursorid = cursor.getColumnIndex("Name");
            cursor.moveToNext();
            String bookname = cursor.getString(bookcursorid);
            cursor.close();

            txtName.setText(bookname);
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Database Error", Toast.LENGTH_SHORT).show();
            goActivity();
        }

        //kullanıcı inceleme ve not gibi inputlarda sayfa sayısı girmek ister mi istemez mi emin olamadım bundan
        // dolayı yorum satırında bıraktım bu kısmı.
        /*
        if (inputtype != 1)
            pageNumber.setVisibility(View.INVISIBLE);
        */


        binding.addinputAddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num;
                //kullanıcı sayfa sayısı girmek istemezse -1'e eşitliyorum. ileride buna dair bir kontrol yaparsam
                // buna göre ayarlama şansım olabilir.
                if (pageNumber.getText().toString().equals(""))
                    num = -1;
                else
                    num = Integer.parseInt(pageNumber.getText().toString());
                //boş değerler varsa toast mesajı kullanıyorum ve kullancının doldurmasını bekliyorum
                if (input.getText().toString().equals("") || title.getText().toString().equals(""))
                    Toast.makeText(getBaseContext(), "Can't be null value", Toast.LENGTH_SHORT).show();

                //değerlerim sorunsuzsa kaydediyorum. sonra da buradan kitaba dair inputların gösterildiği sayfaya
                    // gidiyorum.
                else
                {
                    database.execSQL("INSERT INTO input(Kitapid, Pagenumber, Input, Inputtype, Title) " +
                            "VALUES(?, ?, ?, ?, ?)", new Object[]{bookid, num,  input.getText().toString(), inputtype
                            , title.getText().toString()});
                    goActivity();
                }

            }
        });

    }

    //kitaplara dair girdilerin bulunduğu sayfaya gitmek için yazdım.
    public void goActivity()
    {
        Intent intent = new Intent(this, BookActivity.class);
        intent.putExtra("BookID", bookid);
        startActivity(intent);
    }

}