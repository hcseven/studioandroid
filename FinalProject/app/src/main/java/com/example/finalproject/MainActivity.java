package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.databinding.ActivityBookBinding;
import com.example.finalproject.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //view binding yapıyorum
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //ana aktivitemin üzerinde 3 fragement kullanıyorum. ve bunları replace fragement fonksiyonu ile çağırıyorum.
        replaceFragment(new HomeFragment(0));

        //oluşturduğum bottombar ile tıkladığım fragementin oluşmasını sağlıyorum.
        binding.mybottomBar.setOnItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.nav_Home)
            {
                replaceFragment(new HomeFragment(0));
            }
            else if (menuItem.getItemId() == R.id.nav_AddBook)
            {
                replaceFragment(new AddBookFragment());
            }
            else if (menuItem.getItemId() == R.id.nav_Profile)
            {
                replaceFragment(new ProfileFragment());
            }

            return true;
        });


    }

    //burada fragmentin oluşmasını sağlıyorum.
    public void replaceFragment(Fragment fragment)
    {
        FrameLayout frame = findViewById(R.id.myframe);
        frame.removeAllViews();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.myframe, fragment);
        fragmentTransaction.commit();
    }

    //burada menumu activity'me bağlıyorum.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.order_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //order menudeki seçeneklere göre home fragmenta durum bilgisini vererek çağırıyorum ki değişen bilgilerle
    // yeniden fragment oluşsun ve sayfa güncellensin. ancak bunu kitap ekleme ve profil fragmentlarında yapmaması
    // için de bir kontrol fonksiyonu yazıp geçerli fragmentı alıyorum ve eğer HomeFragment değilse bu işlemleri
    // uygulamıyorum.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.order_oldnew && getVisibleFragment() instanceof HomeFragment)
        {
            replaceFragment(new HomeFragment(1));
        }
        else if(item.getItemId() == R.id.order_nameza && getVisibleFragment() instanceof HomeFragment)
        {
            replaceFragment(new HomeFragment(2));
        }
        else if(item.getItemId() == R.id.order_nameaz && getVisibleFragment() instanceof HomeFragment)
        {
            replaceFragment(new HomeFragment(3));
        }
        else if(item.getItemId() == R.id.order_willread && getVisibleFragment() instanceof HomeFragment)
        {
            replaceFragment(new HomeFragment(4));
        }


        return super.onOptionsItemSelected(item);
    }


    //geçerli fragmentı alıyorum.
    private Fragment getVisibleFragment() {
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }
}