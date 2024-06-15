package com.example.finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookTypeAdapter extends RecyclerView.Adapter<BookTypeAdapter.BookTypeHolder> {

    ArrayList<String> booktypes;
    ArrayList<Integer> readValues;

    //constructor oluşturdum ki ihtiyacım olan verileri diğer activityden rahatça alabileyim.
    public BookTypeAdapter(ArrayList<String> booktypes, ArrayList<Integer> readValues)
    {
        this.booktypes = booktypes;
        this.readValues = readValues;

    }

    //burada kullanacağım xml dosyasını belirtiyorum ki recycler view'de bu dosyaya erişsin ve ona göre
    // görünüm oluştursun.
    @NonNull
    @Override
    public BookTypeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemview = inflater.inflate(R.layout.booktype, parent, false);
        return new BookTypeAdapter.BookTypeHolder(itemview);
    }

    //burada değerlerim ile xml dosyamdaki elementlerimi bağlıyorum ki view oluşturulduğunda bu değerler gözüksün.
    @Override
    public void onBindViewHolder(@NonNull BookTypeHolder holder, int position) {
        holder.Name.setText(booktypes.get(position));
        if (readValues.get(position) > 1)
            holder.Num.setText(readValues.get(position).toString() + " Books");
        else
            holder.Num.setText(readValues.get(position).toString() + " Book");
    }

    //burada recycler view içerisinde xml dosyamdaki görünümden kaç tane oluşturulacağını belirtiyorum. ben direk
    // aldığım arraylist kadar oluşturulmasını istiyorum.
    @Override
    public int getItemCount() {
        return booktypes.size();
    }

    //burada da xml dosyamdaki elementlerimi bind ederek holder sınıfım üzerinden erişilmesini sağlıyorum.
    public static class BookTypeHolder extends RecyclerView.ViewHolder{

        TextView Name, Num;
        public BookTypeHolder(View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.booktype_name);
            Num = itemView.findViewById(R.id.booktype_num);
        }
    }
}
