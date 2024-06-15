package com.example.finalproject;

import android.content.ClipData;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.databinding.InputItemBinding;

import java.util.ArrayList;


public class InputAdapter extends RecyclerView.Adapter<InputAdapter.InputHolder> {

    ArrayList<Inputs> inputs;

    //constructor oluşturdum ki ihtiyacım olan verileri diğer activityden rahatça alabileyim.
    public InputAdapter(ArrayList<Inputs> inputs)
    {
        this.inputs = inputs;
    }

    //burada kullanacağım xml dosyasını belirtiyorum ki recycler view'de bu dosyaya erişsin ve ona göre
    // görünüm oluştursun.
    @NonNull
    @Override
    public InputHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemview = inflater.inflate(R.layout.input_item, parent, false);
        return new InputHolder(itemview);
    }


    //burada değerlerim ile xml dosyamdaki elementlerimi bağlıyorum ki view oluşturulduğunda bu değerler gözüksün.
    @Override
    public void onBindViewHolder(@NonNull InputHolder holder, int position) {
        holder.Name.setText(inputs.get(position).Input);
        holder.Title.setText(inputs.get(position).Title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext() , UpdateDeleteActivity.class);
                intent.putExtra("inputID", inputs.get(position).id );
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    //burada recycler view içerisinde xml dosyamdaki görünümden kaç tane oluşturulacağını belirtiyorum. ben direk
    // aldığım arraylist kadar oluşturulmasını istiyorum.
    @Override
    public int getItemCount() {
        return inputs.size();
    }

    //burada xml dosyamdaki elementlerimi bind ederek holder sınıfım üzerinden erişilmesini sağlıyorum.
    public  class InputHolder extends RecyclerView.ViewHolder{

        TextView Name, Title;
        public InputHolder(View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.input_text);
            Title = itemView.findViewById(R.id.input_title);
        }

    }
}
