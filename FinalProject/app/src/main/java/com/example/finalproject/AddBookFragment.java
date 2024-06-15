package com.example.finalproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.databinding.FragmentAddBookBinding;
import com.google.android.material.snackbar.Snackbar;

public class AddBookFragment extends Fragment {

    Button btn;
    EditText txtedition, txtbookname, txtnumberofpages, txtwritername;
    TextView txtpoint;
    CheckBox readcheck, willreadcheck;
    FragmentAddBookBinding binding;
    SeekBar pointseek;
    Spinner spinner;
    int isread;
    int booktypeid;
    int point;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        return inflater.inflate(R.layout.fragment_add_book, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Ekleyeceğim kitaba dair bilgileri içeren elemanlarımdan referansları aldım
        btn = view.findViewById(R.id.addbook_AddButton);
        txtedition = view.findViewById(R.id.addbook_edition);
        txtbookname = view.findViewById(R.id.addbook_bookname);
        txtpoint = view.findViewById(R.id.addbook_point);
        txtnumberofpages = view.findViewById(R.id.addbook_numberofpages);
        txtwritername = view.findViewById(R.id.addbook_writername);
        spinner = view.findViewById(R.id.addbook_booktype);
        readcheck = view.findViewById(R.id.chkbox_read);
        willreadcheck = view.findViewById(R.id.chkbox_willread);
        pointseek = view.findViewById(R.id.seekbar_point);

        //default tanımlamalarımı yaptım
        booktypeid = -1;
        readcheck.setChecked(true);
        isread = 1;

        //iki tane checkboxım var. okudu veya okuyacak. ikisini de takip ediyorum. onlara göre
        //read state ve diğer checkboxın seçilme durumunu düzenliyorum.
        readcheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    willreadcheck.setChecked(false);
                    isread = 1;
                    System.out.println("okey : " + isread);
                }
            }
        });
        willreadcheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    readcheck.setChecked(false);
                    isread = 2;
                }

            }
        });

        //kitap türünü daha sonra kullanacağım için bir id ile almak istedim ve bunu da spinner tanımlayarak
        //bir bar oluşturup yaptım.
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //spinner argümanlarımı xml dosyasında verdim. position -1 dememin nedeni, spinner argümanlarının
                //ilkini "choose book type" yapmam spinner'da bu yazının görünmesi için.
                booktypeid = position - 1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //kullanıcının kitaba dair puanını seekbar üzerinden alıyorum. seekbar değiştirildiğinde üzerindeki text ile
        // puanı da kullanıcıya gösteriyorum.
        pointseek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtpoint.setText("Your Point : " + progress);
                point = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //eğer değerlerimden herhangi biri null ise bu şekilde veritabanıma vermemek için bir Toast mesajı
                // kullanıyorum.
                if (txtbookname.getText().toString().isEmpty() || txtwritername.getText().toString().isEmpty() || booktypeid == -1 ||
                        txtedition.getText().toString().isEmpty() || txtnumberofpages.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(), "Can't be null value", Toast.LENGTH_LONG ).show();
                }
                else
                {
                    // eğer kullanıcı veri yazma iznine sahip değilse izin sorar
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                1);
                    }
                    //izne sahipse verileri veritabanına yazar
                    else {
                        try {
                            //değerlerim de doğru döndükten sonra artık veritabanıma verilerimi kaydedip, sonra da
                            // Edittext'lerimi boşaltıyorum.
                            SQLiteDatabase database = getActivity().openOrCreateDatabase("BookApp", Context.MODE_PRIVATE, null);
                            database.execSQL("CREATE TABLE IF NOT EXISTS book (id INTEGER PRIMARY KEY AUTOINCREMENT, Name VARCHAR, " +
                                    "Writer VARCHAR, Readdate DATE DEFAULT CURRENT_DATE, Booktypeid INTEGER, Readstateid INTEGER, " +
                                    "Numberofpages INTEGER, Edition VARCHAR, Point DECIMAL)");
                            database.execSQL("INSERT INTO book(Name, Writer, Booktypeid, Readstateid, Numberofpages, Edition, Point)" +
                                    "VALUES(?, ?, ?, ?, ?, ?, ?)", new Object[]{txtbookname.getText().toString(),
                                    txtwritername.getText().toString(), booktypeid, isread,
                                    Integer.parseInt(txtnumberofpages.getText().toString()),
                                    txtedition.getText().toString(), point});

                            txtedition.setText("");
                            txtbookname.setText("");
                            txtnumberofpages.setText("");
                            txtwritername.setText("");
                        } catch (Exception e) {

                        }
                    }

                    }


                }

        });

    }

}