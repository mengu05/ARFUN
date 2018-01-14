package my.edu.tarc.arfun;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Model.Animal;

public class PuzzleActivity extends Activity {

    private static int screenWidth;
    private static int screenHeight;
    private  static ArrayList<Animal> qArray;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024,1024);
        DisplayMetrics metrics =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("Animal").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                qArray = new ArrayList<Animal>();
                for(DataSnapshot snap : dataSnapshot.getChildren()){
                    qArray.add(snap.getValue(Animal.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DB","Read Failed : "+databaseError.getCode());
            }
        });

        setContentView(new MainView(this));
    }

    public static int getScreenWidth() {return screenWidth;}

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static ArrayList<Animal> getImageUrls() {
        return qArray;
    }



}
