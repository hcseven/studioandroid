package com.example.finalproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.finalproject.databinding.FragmentHomeBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class HomeFragment extends Fragment {

    int State;

    public HomeFragment(int State)
    {
        this.State = State;
    }
    ArrayList<Books> BookList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, true);

        //adaptera vereceğim book türündeki listemi oluşturuyorum.
        BookList = new ArrayList<Books>();
        //listemi fonksiyon içerisinde dolduruyorum.
        BookList = getBooks(BookList);

        //recycler view ve adapter oluşturup, recycler view'ın düzenini ayarlıyorum ve en sonda adapterımı recycler
        // viewa atıyorum.
        RecyclerView recyclerView = view.findViewById(R.id.homerecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        BookAdapter adapter = new BookAdapter(BookList);
        recyclerView.setAdapter(adapter);

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    //veritabanımdaki book tablosundan benim kullandığım değerleri alıyorum.
    public ArrayList<Books> getBooks(ArrayList<Books> BookList)
    {
        try{
            SQLiteDatabase database = this.getActivity().openOrCreateDatabase("BookApp", Context.MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS book (id INTEGER PRIMARY KEY AUTOINCREMENT, Name VARCHAR, " +
                    "Writer VARCHAR, Readdate DATE DEFAULT CURRENT_DATE, Booktypeid INTEGER, Readstateid INTEGER, " +
                    "Numberofpages INTEGER, Edition VARCHAR, Point DECIMAL)" );

            Cursor cursor = database.rawQuery(getSqlCom(), null);
            int indexid = cursor.getColumnIndex("id");
            int index = cursor.getColumnIndex("Writer");
            int index2 = cursor.getColumnIndex("Name");
            int index3 = cursor.getColumnIndex("Readdate");
            while (cursor.moveToNext()){

               BookList.add( new Books(cursor.getInt(indexid), cursor.getString(index2), cursor.getString(index),
                       cursor.getString(index3)));

            }
            return BookList;


        }catch (Exception e)
        {
            System.out.println(e);
        }
        return BookList;
    }

    //bu fonksiyonu durumlara göre sql komutu almak için yazdım. isme, yeniliğe veya sonra okunacak işaretlenme
    // durumuna göre seçilen değere göre bir sql komutu döndürüyorum. bu değeri de main activity'deki order menuye
    // göre yapıyorum.
    public String getSqlCom()
    {

        if (State == 0)
            return ("SELECT * FROM book ORDER BY id DESC");
        else if (State == 1)
            return ("SELECT * FROM book ORDER BY id ASC");
        else if (State == 2)
            return ("SELECT * FROM book ORDER BY Upper(Name) DESC");
        else if (State == 3)
            return ("SELECT * FROM book ORDER BY Upper(Name) ASC");
        else if (State == 4)
            return ("SELECT * FROM book WHERE Readstateid = 2");
        else
            return ("SELECT * FROM book");
    }
}