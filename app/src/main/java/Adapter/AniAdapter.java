package Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import Model.Animal;
import my.edu.tarc.arfun.R;

/**
 * Created by mengu on 11/1/2018.
 */

public class AniAdapter extends RecyclerView.Adapter<AniAdapter.MyViewHolder> {
    private Context mContext;
    private List<Animal> animalList;
    private TextToSpeech tts;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, species;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            species = (TextView) view.findViewById(R.id.species);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public AniAdapter(Context mContext, List<Animal> animalList) {
        this.mContext = mContext;
        this.animalList = animalList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Animal animal = animalList.get(position);
        holder.name.setText(animal.getName());
        holder.species.setText(animal.getSpecies());

        // loading album cover using Glide library
        Picasso.with(mContext).load(animal.getImageLink()).fit().into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                final String text = holder.name.getText().toString();
                if( text.equals("")){
                    Toast.makeText(mContext, "The input field must not be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                tts = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            if (!tts.isSpeaking()) {
                                tts.setLanguage(Locale.US);
                                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return animalList.size();
    }
}
