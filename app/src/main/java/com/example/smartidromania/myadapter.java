package com.example.smartidromania;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.time.Instant;

import de.hdodenhof.circleimageview.CircleImageView;

public class myadapter extends FirebaseRecyclerAdapter<Person, myadapter.myviewholder> {


    public myadapter(@NonNull FirebaseRecyclerOptions<Person> options) {
        super(options);
    }

    //initializare obiectelor pentru extragerea din firebase cu ajutorul clasei Person
    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull Person person) {
        holder.nametext.setText(person.getLastName());
        holder.prenumetext.setText(person.getFirstName());
        holder.bDayText.setText(person.getbDay());
        holder.adresstext.setText(person.getAddress());
        holder.permistext.setText(person.getDriveLicence());
        holder.medicaltext.setText(person.getMedical());
        holder.financiartext.setText(person.getFinance());
        holder.gentext.setText(person.getGender());

        String Uimage = person.getmImageUrl();

        Picasso.get().load(Uimage).into(holder.img1);

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow, parent, false);
        return new myviewholder(view);
    }


    public void stopListening() {
    }

    // clasa pentru obiectele item din singlerow.xml
    class myviewholder extends RecyclerView.ViewHolder {
        CircleImageView img1;
        TextView nametext, adresstext, bDayText, prenumetext, permistext, medicaltext, financiartext, gentext;

        public myviewholder(@NonNull View mView) {
            super(mView);
            nametext = (TextView) mView.findViewById(R.id.nametext);
            adresstext = (TextView) mView.findViewById(R.id.adresstext);
            bDayText = (TextView) mView.findViewById(R.id.bDayText);
            prenumetext = (TextView) mView.findViewById(R.id.prenumetext);
            permistext = (TextView) mView.findViewById(R.id.permistext);
            medicaltext = (TextView) mView.findViewById(R.id.medicaltext);
            financiartext = (TextView) mView.findViewById(R.id.financiartext);
            gentext = (TextView) mView.findViewById(R.id.gentext);
            img1 = (CircleImageView) mView.findViewById(R.id.imageViewPerson);
        }
    }


}
