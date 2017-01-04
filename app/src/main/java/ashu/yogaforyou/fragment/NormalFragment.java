package ashu.yogaforyou.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import ashu.yogaforyou.R;
import ashu.yogaforyou.adapter.ExerciseAdapter;
import ashu.yogaforyou.global.AppController;

/**
 * Created by apple on 01/01/17.
 */

public class NormalFragment extends Fragment implements ExerciseAdapter.ViewHolder.ClickListener{

    private RecyclerView recyclerView;
    private View view;
    private String [] normalExerciseList = new String[]{
        "Salutation To Sun", "Seated Angle", "Plough Pose", "Shoulder Stand Pose", "Both Big Toe Pose",
        "Upward Bow Pose", "Extended Side Angle","Headstand Pose", "Waist Roll",
            "Head to Feet Pose", "Rock The Cradle", "Head Knee Pose", "Yoga Sit Up",
            "Side Bend Pose", "Half Lord of the Fishes Pose", "Pigeon Pose", "Leg Warm-Up",
            "Lotus Pose", "Forward Bend", "The Tree", "The Warrior",
            "The Intense East Stretch Pose", "Balance Exercise",
            "The Triangle Pose", "The Shooting Bow", "The Swan"
    };

    private static int isList = 1;
    private FloatingActionButton floatingActionButton;

    private Tracker mTracker;

    public NormalFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_exercise, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeLayoutVariables();
        populateRecyclerView();

        AppController controller = (AppController) getActivity().getApplication();
        mTracker = controller.getDefaultTracker();

    }

    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName("Normal");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Normal")
                .setAction("Open")
                .build());
    }

    private void initializeLayoutVariables(){
        recyclerView = (RecyclerView) view.findViewById(R.id.exerciseRecycler);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isList == 1){
                    floatingActionButton.setImageResource(R.drawable.list);
                    isList = 0;
                }
                else {
                    floatingActionButton.setImageResource(R.drawable.grid);
                    isList = 1;
                }


                populateRecyclerView();
            }
        });
    }

    private void populateRecyclerView(){
        //   alertsAdapter = new AlertsAdapter(alerts, siteInfoSparseArray, parentFragment, this ,isList);

//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
//        if(isList ==1) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);
//        }else {
//            AlertsAdapter adapter = new AlertsAdapter(alerts, siteInfoSparseArray, parentFragment, this ,isList);
//            recyclerView.setLayoutManager(gridLayoutManager);
//            recyclerView.setAdapter(adapter);
//        }


        ExerciseAdapter adapter = new ExerciseAdapter(getActivity(), normalExerciseList, this, isList);
        if(isList == 1) {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }else {
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onItemClick(int position) {

        Fragment fragment=new ExerciseFragment();
        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("name", normalExerciseList[position]);
        fragment.setArguments(bundle);
        ft.replace(R.id.content_main, fragment).addToBackStack("Normal");
        ft.commit();
    }
}
