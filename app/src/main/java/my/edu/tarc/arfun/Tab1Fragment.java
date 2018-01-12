package my.edu.tarc.arfun;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Adapter.AniAdapter;
import Model.Animal;

public class Tab1Fragment extends Fragment {
    private static final String TAG = "Tab1Fragment";
    private RecyclerView recycler;
    private AniAdapter adapter;
    private List<Animal> animalList;
    private List<Animal> animalListCopy ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_fragment,container,false);
        recycler = (RecyclerView)view.findViewById(R.id.recycler);
        animalList = new ArrayList<>();
        animalListCopy = new ArrayList<>();
        adapter = new AniAdapter(this.getContext(),animalList);

        final SearchView simpleSearch = (SearchView)view.findViewById(R.id.searchView);
        simpleSearch.setQueryHint("E.g. Tiger");
        simpleSearch.setIconifiedByDefault(false);
        simpleSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String search){
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase(Locale.getDefault());
                animalList.clear();
                if(newText.length() == 0){

                    animalList.addAll(animalListCopy);
                }else {
                    for(Animal wp : animalListCopy){
                        if (wp.getName().toLowerCase(Locale.getDefault()).contains(newText)) {
                            animalList.add(wp);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(view.getContext(), 2);
        recycler.setLayoutManager(mLayoutManager);
        recycler.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("Animal").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                animalList.clear();
                for(DataSnapshot snap : dataSnapshot.getChildren()){
                    Animal animal = snap.getValue(Animal.class);
                    Log.d("GetAnimal",animal.getName());
                    animalList.add(animal);
                }
                animalListCopy.addAll(animalList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DB","Read Failed : "+databaseError.getCode());
            }
        });
        return view;
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)
                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}



