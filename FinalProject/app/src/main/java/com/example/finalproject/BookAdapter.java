package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.databinding.BookItemBinding;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookHolder> {

    ArrayList<Books> mybooks;

    //constructor oluşturdum ki kitaplara dair verileri diğer activityden rahatça alabileyim.
    public BookAdapter(ArrayList<Books> mybooks){
        this.mybooks = mybooks;
    }

    //burada kullanacağım xml dosyasını belirtiyorum ki recycler view'de bu dosyaya erişsin ve ona göre
    // görünüm oluştursun.
    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemview = inflater.inflate(R.layout.book_item, parent, false);
        return new BookHolder(itemview);
    }

    //burada değerlerim ile xml dosyamdaki elementlerimi bağlıyorum ki view oluşturulduğunda bu değerler gözüksün.
    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position) {
        holder.Name.setText(mybooks.get(position).Name);
        holder.Readdate.setText(mybooks.get(position).Readdate);
        holder.Writer.setText(mybooks.get(position).Writer);

        //burada kitapların üzerine tıklanabilmesini ve tıklanınca o kitaba dair inputların olduğu sayfaya
        // geçilmesini sağlıyorum.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext() , BookActivity.class);
                intent.putExtra("BookID", mybooks.get(position).id );
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    //burada recycler view içerisinde xml dosyamdaki görünümden kaç tane oluşturulacağını belirtiyorum. ben direk
    // aldığım arraylist kadar oluşturulmasını istiyorum.
    @Override
    public int getItemCount() {

        return mybooks.size();
    }

    //burada da xml dosyamdaki elementlerimi bind ederek holder sınıfım üzerinden erişilmesini sağlıyorum.
    public static class BookHolder extends RecyclerView.ViewHolder{

        TextView Name, Readdate, Writer;
        public BookHolder(View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.bookitem_name);
            Readdate = itemView.findViewById(R.id.bookitem_date);
            Writer = itemView.findViewById(R.id.bookitem_writer);
        }
    }

}
