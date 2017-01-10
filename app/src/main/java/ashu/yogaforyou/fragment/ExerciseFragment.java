package ashu.yogaforyou.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.Locale;

import ashu.yogaforyou.R;

/**
 * Created by apple on 01/01/17.
 */

public class ExerciseFragment extends Fragment {

    private View view;
    private String exerciseName;
    private String purpose;
    private String process;

    private int res;

    private TextView headerName;
    private TextView txtPurpose;
    private TextView txtProcess;
    private Button btnSpeakProcess;

    private SimpleDraweeView exerciseImage;

    private TextToSpeech t1;

    private Button btnOk;
    private SeekBar seekPitch;
    private SeekBar seekSpeech;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private int speechRate, pitchRate;
    public ExerciseFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_exercise_main, container,false);
        exerciseName = getArguments().getString("name");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        initializeLayoutVariables();
        choseRightExercise(exerciseName);

        populateView();
    }

    private void initializeLayoutVariables(){
        headerName = (TextView) view.findViewById(R.id.headerName);
        txtPurpose = (TextView) view.findViewById(R.id.txtPurpose);
        txtProcess = (TextView) view.findViewById(R.id.txtProcess);

        exerciseImage = (SimpleDraweeView) view.findViewById(R.id.my_image_view);

        btnSpeakProcess = (Button) view.findViewById(R.id.readProcess);

        sp = getActivity().getSharedPreferences("setting", Context.MODE_PRIVATE);
        editor = sp.edit();
        t1 = new TextToSpeech(getActivity().getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        speechRate = sp.getInt("speech", 1);
        pitchRate = sp.getInt("pitch", 1);

        t1.setPitch(pitchRate);
        t1.setSpeechRate(speechRate);



    }

    private void populateView(){
        headerName.setText(exerciseName);

        txtPurpose.setText(purpose);
        txtProcess.setText(process);
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithResourceId(res).build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(imageRequest.getSourceUri())
                .setAutoPlayAnimations(true)
                .build();
        exerciseImage.setController(controller);

        btnSpeakProcess.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                longClick();
                return true;
            }
        });

        btnSpeakProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toSpeak = txtProcess.getText().toString();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });


    }

    private void longClick(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_control_speech);
        // Set dialog title
        dialog.setTitle("Control Speech");
        dialog.show();

        btnOk = (Button) dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        seekPitch = (SeekBar) dialog.findViewById(R.id.seekPitch);

        seekSpeech = (SeekBar) dialog.findViewById(R.id.seekSpeech);

        seekSpeech.setProgress(speechRate);
        seekPitch.setProgress(pitchRate);

        seekSpeech.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i += 1;
                speechRate = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt("speech", speechRate);
                editor.commit();
            }
        });

        seekPitch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i += 1;
                pitchRate = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt("pitch", pitchRate);
                editor.commit();
            }
        });

        btnOk = (Button) dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t1.setSpeechRate(speechRate);
                t1.setPitch(pitchRate);
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
    }

    private void choseRightExercise(String name){
        if(name.contains("Salutation")){
            res = R.raw.sun;
            purpose = getString(R.string.sun_salutation_purpose);
            process = getString(R.string.sun_salutation_process);
        }

        if(name.contains("Seated")){
            res = R.raw.seatedangle;
            purpose = getString(R.string.seated_angle_purpose);
            process = getString(R.string.seated_angle_process);
        }

        if(name.contains("Plough")){
            res = R.raw.plough;
            purpose = getString(R.string.plough_purpose);
            process = getString(R.string.plough_process);
        }

        if(name.contains("Shoulder")){
            res = R.raw.shoulder;
            purpose = getString(R.string.shoulder_stand_purpose);
            process = getString(R.string.shoulder_stand_process);
        }

        if(name.contains("Both")){
            res = R.raw.bothbigtoes;
            purpose = getString(R.string.both_big_toe_purpose);
            process = getString(R.string.both_big_toe_process);
        }

        if(name.contains("Upward")){
            res = R.raw.upbow;
            purpose = getString(R.string.upward_purpose);
            process = getString(R.string.upward_purpose);
        }

        if(name.contains("Extended")){
            res = R.raw.sideangle;
            purpose = getString(R.string.extended_purpose);
            process = getString(R.string.extended_process);
        }

        if(name.contains("Headstand")){
            res = R.raw.headstand;
            purpose = getString(R.string.headstand_purpose);
            process = getString(R.string.headstand_process);
        }

        if(name.contains("Waist")){
            res = R.raw.waistroll;
            purpose = getString(R.string.waist_purpose);
            process = getString(R.string.waist_purpose);
        }

        if(name.contains("Feet")){
            res = R.raw.headtofeet;
            purpose = getString(R.string.headfeet_purpose);
            process = getString(R.string.headfeet_process);
        }

        if(name.contains("Cradle")){
            res = R.raw.headstand;
            purpose = getString(R.string.rock_cradle_purpose);
            process = getString(R.string.rock_cradle_process);
        }

        if(name.contains("Knee")){
            res = R.raw.headknee;
            purpose = getString(R.string.headknee_purpose);
            process = getString(R.string.headknee_process);
        }

        if(name.contains("Yoga")){
            res = R.raw.yogasitup;
            purpose = getString(R.string.situp_purpose);
            process = getString(R.string.situp_process);
        }

        if(name.contains("Side")){
            res = R.raw.sidebend;
            purpose = getString(R.string.sidebend_purpose);
            process = getString(R.string.sidebend_process);
        }

        if(name.contains("Half")){
            res = R.raw.halflordofthefishes;
            purpose = getString(R.string.halflord_purpose);
            process = getString(R.string.halflord_process);
        }

        if(name.contains("Pigeon")){
            res = R.raw.pidgeon;
            purpose = getString(R.string.pigeon_purpose);
            process = getString(R.string.pigeon_process);
        }
        if(name.contains("Warm-Up")){
            res = R.raw.lw;
            purpose = getString(R.string.legwarm_purpose);
            process = getString(R.string.legwarm_process);
        }
        if(name.equalsIgnoreCase("Lotus Pose")){
            res = R.raw.lotus;
            purpose = getString(R.string.lotus_purpose);
            process = getString(R.string.lotus_process);
        }
        if(name.equalsIgnoreCase("Forward Bend")){
            res = R.raw.forwardbend;
            purpose = getString(R.string.forward_purpose);
            process = getString(R.string.forward_process);
        }

        if(name.contains("Tree")){
            res = R.raw.tree;
            purpose = getString(R.string.tree_purpose);
            process = getString(R.string.tree_process);
        }

        if(name.contains("Warrior")){
            res = R.raw.warrior;
            purpose = getString(R.string.warrior_purpose);
            process = getString(R.string.warrior_process);
        }

        if(name.contains("Intense")){
            res = R.raw.intenseeaststretchpose;
            purpose = getString(R.string.intense_purpose);
            process = getString(R.string.intense_process);
        }

        if(name.contains("Balance")){
            res = R.raw.balanceexercise;
            purpose = getString(R.string.balance_purpose);
            process = getString(R.string.balance_process);
        }

        if(name.contains("Triangle")){
            res = R.raw.trianglebb;
            purpose = getString(R.string.triangle_purpose);
            process = getString(R.string.triangle_process);
        }

        if(name.contains("Shooting")){
            res = R.raw.shootingbow;
            purpose = getString(R.string.shooting_purpose);
            process = getString(R.string.shooting_process);
        }

        if(name.contains("Swan")){
            res = R.raw.swan;
            purpose = getString(R.string.swan_purpose);
            process = getString(R.string.swan_process);
        }


        if(name.contains("Palm")){
            res = R.raw.palmpress;
            purpose = getString(R.string.palm_purpose);
            process = getString(R.string.palm_process);
        }

        if(name.equalsIgnoreCase("Bent Arm Stretch")){
            res = R.raw.bentarmstretch;
            purpose = getString(R.string.bent_arm_purpose);
            process = getString(R.string.bent_arm_process);
        }

        if(name.contains("Undulating")){
            res = R.raw.undulatingspinestretch;
            purpose = getString(R.string.undulating_purpose);
            process = getString(R.string.undulating_process);
        }

        if(name.equalsIgnoreCase("Half Lotus")){
            res = R.raw.pronehalflotus;
            purpose = getString(R.string.half_lotus_purpose);
            process = getString(R.string.half_lotus_process);
        }

        if(name.contains("Woodchopper")){
            res = R.raw.woodchopper;
            purpose = getString(R.string.woodchopper_purpose);
            process = getString(R.string.woodchopper_process);
        }

        if(name.contains("Downward")){
            res = R.raw.downwardfacingdog;
            purpose = getString(R.string.down_dog_purpose);
            process = getString(R.string.down_dog_process);
        }

        if(name.contains("Upward")){
            res = R.raw.upwardfacingdog;
            purpose = getString(R.string.upward_purpose);
            process = getString(R.string.upward_process);
        }

        if(name.equalsIgnoreCase("Reclining Side Twist")){
            res = R.raw.recliningsidetwist;
            purpose = getString(R.string.recline_purpose);
            process = getString(R.string.recline_process);
        }

        if(name.contains("Cat")){
            res = R.raw.tc;
            purpose = getString(R.string.cat_purpose);
            process = getString(R.string.cat_process);
        }

        if(name.equalsIgnoreCase("Neck Stretch Variation")){
            res = R.raw.neckstretchvariation;
            purpose = getString(R.string.neck_stretch_purpose);
            process = getString(R.string.neck_stretch_process);
        }

        if(name.contains("Squeeze")){
            res = R.raw.kneesqueezestretch;
            purpose = getString(R.string.knee_squeeze_purpose);
            process = getString(R.string.knee_squeeze_process);
        }

        if(name.contains("Shoulder")){
            res = R.raw.shoulderlift;
            purpose = getString(R.string.shoulder_lift_purpose);
            process = getString(R.string.shoulder_lift_process);
        }

        if(name.contains("Lion")){
            res = R.raw.lion;
            purpose = getString(R.string.lion_purpose);
            process = getString(R.string.lion_process);
        }


        if(name.contains("Alternate")){
            res = R.raw.alternatenosebreathing;
            purpose = getString(R.string.alternate_purpose);
            process = getString(R.string.alternate_process);
        }

        if(name.contains("Sitkari")){
            res = R.raw.sitkaribreathing;
            purpose = getString(R.string.sitkari_purpose);
            process = getString(R.string.sitkari_process);
        }
        if(name.contains("Sithali")){
            res = R.raw.sithalibreathing;
            purpose = getString(R.string.sithali_purpose);
            process = getString(R.string.sithali_process);
        }

        if(name.contains("Ujjayi")){
            res = R.raw.ujjayabreathing;
            purpose = getString(R.string.ujjayi_purpose);
            process = getString(R.string.ujjayi_process);
        }

        if(name.contains("Bheda")){
            res = R.raw.suryabhedabreathing;
            purpose = getString(R.string.surya_bheda_purpose);
            process = getString(R.string.surya_bheda_process);
        }

        if(name.contains("Bhastrika")){
            res = R.raw.bhastrikabreathing;
            purpose = getString(R.string.bhastrika_purpose);
            process = getString(R.string.bhastrika_process);
        }

        if(name.contains("Uddiyana")){
            res = R.raw.uddiyanabanda;
            purpose = getString(R.string.uddiyana_purpose);
            process = getString(R.string.uddiyana_process);
        }

        if(name.contains("Moola")){
            res = R.raw.moola;
            purpose = getString(R.string.moola_purpose);
            process = getString(R.string.moola_process);
        }

    }

}
