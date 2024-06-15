package com.example.finalproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.time.Year;
import java.util.ArrayList;
import java.util.Date;

public class ProfileFragment extends Fragment {

    SQLiteDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //view'a erişmek için inflate ettim.
        View view = inflater.inflate(R.layout.fragment_profile, container, true);
        database = this.getActivity().openOrCreateDatabase("BookApp", Context.MODE_PRIVATE, null );

        Cursor cursor = database.rawQuery("SELECT * FROM book", null);

        //en çok okunan türleri almak ve bağlamak için yazdığım fonksiyon
        getMostReadType(view);

        //en çok okunan yazarları almak için yazdığım fonksiyon. ancak yazar adlarına göre gittiğinden ve string
        // kıyaslamasında sorun yaşanması muhtemel olduğundan bunu devre dışı bıraktım.
        //getMostReadWriter(view);

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

//veritabanından verileri alıyorum. daha sonra resources/values/booktypes içerisindeki string arrayimden kitap
// türlerini alıyorum. ve bu türlerle sıralı bir şekilde biri türün adını, biri okunma sayısını tutan iki array
// oluşturuyorum ve bu arraylari BookTypeAdapter ile recycler view'ıma bind ediyorum.
    public void getMostReadType(View view)
    {
        try {
            Cursor cursor = database.rawQuery("SELECT Booktypeid, COUNT(*) AS typeNum FROM book GROUP BY Booktypeid ORDER" +
                    " BY typeNum DESC", null);

            int idbooktype = cursor.getColumnIndex("Booktypeid");
            int idnumtype = cursor.getColumnIndex("typeNum");

            ArrayList<String> myBookTypes = new ArrayList<String>();
            ArrayList<Integer> valueRead = new ArrayList<Integer>();

            String[] bookTypes =  getResources().getStringArray(R.array.booktype);

            while (cursor.moveToNext())
            {
                myBookTypes.add(bookTypes[cursor.getInt(idbooktype) + 1]);
                valueRead.add(cursor.getInt(idnumtype));
            }
            cursor.close();
            RecyclerView recyclerView = view.findViewById(R.id.booktype_recy);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            BookTypeAdapter adapter = new BookTypeAdapter(myBookTypes, valueRead);
            recyclerView.setAdapter(adapter);
        }catch (Exception e)
        {

        }

    }

    /*
    public void getMostReadWriter(View view)
    {
        Cursor cursor = database.rawQuery("SELECT Writer, COUNT(*) AS writerNum FROM book GROUP BY Writer ORDER" +
                " BY writerNum DESC", null);

        int idWriter = cursor.getColumnIndex("Writer");
        int idWriterNum = cursor.getColumnIndex("writerNum");

        ArrayList<String> Writers = new ArrayList<String>();
        ArrayList<Integer> valueRead = new ArrayList<Integer>();

        while (cursor.moveToNext())
        {;
            Writers.add(cursor.getString(idWriter));
            valueRead.add(cursor.getInt(idWriterNum));
        }
        RecyclerView recyclerView = view.findViewById(R.id.writer_recy);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        BookTypeAdapter adapter = new BookTypeAdapter(Writers, valueRead);
        recyclerView.setAdapter(adapter);

    }
*/
}