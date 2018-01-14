package my.edu.tarc.arfun;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by User on 2/28/2017.
 */

public class MainFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MainFragment";
    private ImageButton imageAR;
    private ImageButton imageDraw;
    private ImageButton imagePuzzles;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment,container,false);
        imageAR = (ImageButton) view.findViewById(R.id.imageAR);
        imageDraw = (ImageButton) view.findViewById(R.id.imageDraw);
        imagePuzzles = (ImageButton) view.findViewById(R.id.imagePuzzles);
        imageAR.setOnClickListener(this);
        imageDraw.setOnClickListener(this);
        imagePuzzles.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.imageAR:
                intent = new Intent(getActivity(), ARActivity.class);
                startActivity(intent);
                break;
            case R.id.imagePuzzles:
                intent = new Intent(getActivity(), PuzzleActivity.class);
                startActivity(intent);
                break;
            case R.id.imageDraw:
                intent = new Intent(getActivity(), DrawActivity.class);
                startActivity(intent);
                break;

            default:

        }
    }
}
