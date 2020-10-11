package com.example.moneypay;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<ListItem>listItems;
    private Context context;

    public MyAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_recycler_view,parent,false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ListItem listitem=listItems.get(position);
        holder.firstName.setText("First Name:- "+listitem.getFirst_Name());
        holder.LastName.setText("Last Name:- "+listitem.getLast_Name());
        holder.email.setText("Email:- "+listitem.getEmail());
        holder.id.setText("ID - "+listitem.getId());
        Picasso.get().load(listitem.getImg_url()).into(holder.imageView6);

        holder.layoutRecycler.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("DELETE!!!");
                builder.setMessage("Do You Want To Delete?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int newPosition = holder.getAdapterPosition();
                listItems.remove(newPosition);
                notifyItemRemoved(newPosition);
                notifyItemRangeChanged(newPosition,listItems.size());

                Toast.makeText(context,  "Item at Position " + (newPosition + 1) + " Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // DO SOMETHING HERE
                        dialog.cancel();

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
//        holder.layoutRecycler.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//
//
//                return false;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView email;
        TextView firstName;
        TextView LastName;
        ImageView  imageView6;
        ConstraintLayout layoutRecycler;
LinearLayout linearLayout2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id=itemView.findViewById(R.id.id);
            email=itemView.findViewById(R.id.email);
            firstName= itemView.findViewById(R.id.firstName);
            LastName = itemView.findViewById(R.id.LastName);
            imageView6 = itemView.findViewById(R.id.imageView6);
            layoutRecycler=itemView.findViewById(R.id.layoutRecycler);
            linearLayout2=itemView.findViewById(R.id.linearLayout2);
        }
    }
}
