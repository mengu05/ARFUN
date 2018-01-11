package my.edu.tarc.arfun;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import Model.Animal;
import Model.Question;

/**
 * Created by User on 2/28/2017.
 */

public class Tab2Fragment extends Fragment implements View.OnDragListener, View.OnTouchListener, View.OnClickListener {
    private static final String TAG = "Tab2Fragment";

    private Button resetBtn;
    private Button answerBtn;
    private ArrayList<Animal> qArray;
    private String correctAns;
    private ImageView imgView;
    private Toast toast;
    private int qNo = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_fragment,container,false);

        imgView = (ImageView)view.findViewById(R.id.imgView);
        answerBtn =(Button)view.findViewById(R.id.btnAnswer);
        resetBtn = (Button)view.findViewById(R.id.btnReset);
        answerBtn.setOnClickListener(this);
        resetBtn.setOnClickListener(this);
        initualize();

        return view;
    }

    public void initualize(){
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("Animal").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                qArray = new ArrayList<Animal>();
                for(DataSnapshot snap : dataSnapshot.getChildren()){
                    qArray.add(snap.getValue(Animal.class));
                }
                clearViews();
                generate(qNo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DB","Read Failed : "+databaseError.getCode());
            }
        });
    }

    public void displayToast(String message){
        if(toast != null)
            toast.cancel();
        toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void clearViews(){
        LinearLayout layout = (LinearLayout)getView().findViewById(R.id.topLayout);
        if(layout.getChildCount()>0){
            layout.removeAllViews();
        }
        layout = (LinearLayout)getView().findViewById(R.id.botLayout);
        if(layout.getChildCount()>0){
            layout.removeAllViews();
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent){
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());
            String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
            ClipData data = new ClipData(view.getTag().toString(),mimeTypes,item);
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

            view.startDrag(data,shadowBuilder,view,0);
            view.setVisibility(View.INVISIBLE);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean onDrag(View view, DragEvent event){
        int action = event.getAction();
        switch(action){
            case DragEvent.ACTION_DRAG_STARTED:
                if(event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
                    return true;
                }
                return false;
            case DragEvent.ACTION_DRAG_ENTERED:
                view.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
                view.invalidate();
                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                view.getBackground().clearColorFilter();
                view.invalidate();
                return true;
            case DragEvent.ACTION_DROP:
                view.getBackground().clearColorFilter();
                view.invalidate();
                View v = (View) event.getLocalState();

                TextView dropped = (TextView) v;
                View dropTarget =  view;
                Object dropTag = dropTarget.getTag();
                if(dropTarget instanceof LinearLayout){
                    v.setVisibility(View.VISIBLE);
                    return true;
                }else if(dropTarget instanceof TextView){
                    if(dropTag!=null){
                        dropped.setVisibility(View.VISIBLE);
                        return true;
                    }else {
                        ((TextView) dropTarget).setText(dropped.getText());
                        dropTarget.setTag(dropped.getTag()+"_dropped");
                        return true;
                    }
                }
            case DragEvent.ACTION_DRAG_ENDED:
                view.getBackground().clearColorFilter();
                view.invalidate();
                if (event.getResult()) {

                }
                else {
                    View v1 = (View) event.getLocalState();
                    v1.setVisibility(View.VISIBLE);
                }
                return true;
            default:
                Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                break;
        }
        return false;
    }

    public void reset(){
        LinearLayout topLayout = (LinearLayout)getView().findViewById(R.id.topLayout);
        for(int i=0 ; i < topLayout.getChildCount() ; i++){
            TextView txt = (TextView) topLayout.getChildAt(i);
            txt.setVisibility(View.VISIBLE);
        }
        LinearLayout botLayout = (LinearLayout)getView().findViewById(R.id.botLayout);
        for(int i=0 ; i < botLayout.getChildCount() ; i++){
            TextView txt = (TextView) botLayout.getChildAt(i);
            txt.setText("");
            txt.setTag(null);
        }
    }

    public void generate(int qNo){
        correctAns = qArray.get(qNo).getName();
        String imageURL = qArray.get(qNo).getImageLink();
        Picasso.with(getContext()).load(imageURL).fit().into(imgView);

        //shuffle start
        char[] txtArray = correctAns.toCharArray();
        Random rng = new Random();
        for(int i = txtArray.length - 1; i > 0 ; i--){
            int index = rng.nextInt(i+1);
            char a = txtArray[index];
            txtArray[index] = txtArray[i];
            txtArray[i] = a;
        }
        //shuffle end
        int length = txtArray.length;
        LinearLayout topLayout = (LinearLayout)getView().findViewById(R.id.topLayout);
        for(int i=0 ; i < length ; i++){
            TextView txtView = new TextView(getContext());
            txtView.setText(String.valueOf(txtArray[i]));
            txtView.setTextSize(24);
            txtView.setGravity(Gravity.CENTER);
            txtView.setTag("tag_"+txtArray[i]);
            txtView.setOnTouchListener(this);
            txtView.setBackgroundDrawable(getResources().getDrawable(R.drawable.border));
            txtView.setWidth(115);
            txtView.setHeight(115);
            topLayout.addView(txtView);
        }
        LinearLayout botLayout = (LinearLayout)getView().findViewById(R.id.botLayout);
        for(int i=0 ; i < length ; i++){
            TextView txtView = new TextView(getContext());
            txtView.setText("");
            txtView.setTextSize(24);
            txtView.setGravity(Gravity.CENTER);
            txtView.setBackgroundDrawable(getResources().getDrawable(R.drawable.border));
            txtView.setOnDragListener(this);
            txtView.setWidth(115);
            txtView.setHeight(115);

            botLayout.addView(txtView);
        }
    }

    public String getTxtFromView(){
        String txt = "";
        LinearLayout botLayout = (LinearLayout)getView().findViewById(R.id.botLayout);
        if(botLayout!=null){
            for(int i = 0; i < botLayout.getChildCount();i++){
                View viewChild = botLayout.getChildAt(i);
                if(viewChild instanceof TextView){
                    txt = txt+((TextView)viewChild).getText().toString();
                }
            }
        }
        return txt;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnAnswer:
                if(getTxtFromView().equals(correctAns)){
                    displayToast("Correct");
                    clearViews();
                    ++qNo;
                    if(qNo<qArray.size()){
                        generate(qNo);
                    }else{
                        displayToast("QUIZ FINISHED");
                        resetBtn.setVisibility(View.INVISIBLE);
                        answerBtn.setText("Start Over");
                    }

                }else{
                    if(qNo<qArray.size()){
                        displayToast("Wrong");
                        reset();
                    }else{
                        resetBtn.setVisibility(View.VISIBLE);
                        answerBtn.setText("Answer");
                        qNo = 0;
                        generate(qNo);
                    }
                }
                break;
            case R.id.btnReset:
                reset();
                break;
        }
    }
}
