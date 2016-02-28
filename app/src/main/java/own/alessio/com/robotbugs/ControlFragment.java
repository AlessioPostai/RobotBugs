package own.alessio.com.robotbugs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * RobotBugs Created by alessio on 13.2.16.
 */
public class ControlFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static ControlFragment newInstance(int controlFragmentNumber) {
        ControlFragment controlFragment = new ControlFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, controlFragmentNumber);
        controlFragment.setArguments(args);
        return controlFragment;
    }

    public ControlFragment() {
    }

    /**
     * Population size.
     */
    private static final int DEFAULT_POPULATION = 5;
    private static final int MINIMUM_POPULATION = 1;
    private static final int MAXIMUM_POPULATION = 25;

    /**
     * Options default values.
     */
    private static final float DEFAULT_SPEED = 1.0F;
    private static final float DEFAULT_JUMP = 1.0F;
    private static final int DEFAULT_FINGER = 0;
    private static final int DEFAULT_FIELD = R.drawable.stars_background;

    /**
     * Shared preferences identifier and key values.
     */
    private static final String SHARED_PREFERENCES = "shared_preferences";
    private static final String POPULATION_KEY = "population_size_key";
    private static final String SPEED_KEY = "speed_key";
    private static final String JUMP_KEY = "jump_kep";
    private static final String FINGER_KEY = "finger_kep";
    private static final String FIELD_KEY="field_key";

    /**
     * Size threshold for small and large sizes.
     */
    private static final int DISPLAY_SIZE_THRESHOLD = 500;

    /**
     * Population size selector.
     */
    private Spinner populationSpinner;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> arrayList;

    /**
     * Radio group selectors.
     */
    RadioGroup speedChooser;
    RadioGroup jumpChooser;
    RadioGroup fingerChooser;
    RadioGroup fieldChooser;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.control_fragment, container, false);

        /**
         * Initialize selectors.
         */
        populationSpinner = (Spinner) view.findViewById(R.id.population_spinner);
        speedChooser = (RadioGroup) view.findViewById(R.id.speed_chooser);
        jumpChooser = (RadioGroup) view.findViewById(R.id.jump_chooser);
        fingerChooser = (RadioGroup) view.findViewById(R.id.finger_chooser);
        fieldChooser = (RadioGroup) view.findViewById(R.id.field_chooser);

        /**
         * POPULATION SIZE SPINNER.
         * Creates the array list for population sizes spinner, values between a minimum and a maximum values.
         */
        arrayList = new ArrayList<>();
        for(int i=MINIMUM_POPULATION;i<=MAXIMUM_POPULATION;i++) {
            arrayList.add(String.format("%d",i));
        }

        arrayAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.population_spinner_item,
                arrayList);

        populationSpinner.setAdapter(arrayAdapter);

        populationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(POPULATION_KEY, Integer.parseInt(arrayList.get(position)));
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**
         * SPEED CHOOSER.
         */
        speedChooser.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                float speed;
                if (checkedId == R.id.speed_slow) {
                    speed = 1.5F;
                } else if (checkedId == R.id.speed_normal) {
                    speed = 1.0F;
                } else {
                    speed = 0.5F;
                }
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat(SPEED_KEY, speed);
                editor.apply();
            }
        });

        /**
         * JUMP CHOOSER.
         */
        jumpChooser.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                float jump;
                if(checkedId == R.id.jump_long) {
                    jump = 2.0F;
                } else if (checkedId == R.id.jump_short) {
                    jump = 1.5F;
                } else if (checkedId == R.id.jump_xlong) {
                    jump = 3.0F;
                } else  {
                    jump = 1.0F;
                }
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat(JUMP_KEY, jump);
                editor.apply();
            }
        });

        /**
         * FINGER REACTION CHOOSER.
         */
        fingerChooser.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int fingerReaction;
                if(checkedId == R.id.finger_indifferent) {
                    fingerReaction = 2;
                } else if (checkedId == R.id.finger_escape) {
                    fingerReaction = 1;
                } else {
                    fingerReaction = 0;
                }
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(FINGER_KEY, fingerReaction);
                editor.apply();
            }
        });

        /**
         * PLAY FIELD CHOOSER.
         */
        fieldChooser.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int field;
                if(checkedId == R.id.field_food) {
                    field = R.drawable.food_background;
                } else if(checkedId == R.id.field_stars) {
                    field = R.drawable.stars_background;
                } else {
                    field = R.drawable.towel_background;
                }
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(FIELD_KEY,field);
                editor.apply();
            }
        });

      return view;
    }

    /**
     * As the control fragment resumes we need to set the spinner and all radio list up to the
     * current value.
     */
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES, 0);

        int currentPopulation = sharedPreferences.getInt(POPULATION_KEY, DEFAULT_POPULATION);
        populationSpinner.setSelection(arrayAdapter.getPosition(String.format("%d", currentPopulation)));

        float speed = sharedPreferences.getFloat(SPEED_KEY, DEFAULT_SPEED);

        if(speed == 1.5F) {
            speedChooser.check(R.id.speed_slow);
        } else if (speed == 0.5F) {
            speedChooser.check(R.id.speed_fast);
        } else {
            speedChooser.check(R.id.speed_normal);
        }

        float jump = sharedPreferences.getFloat(JUMP_KEY, DEFAULT_JUMP);
        if(jump == 2.0F) {
            jumpChooser.check(R.id.jump_long);
        } else if (jump == 1.5F) {
            jumpChooser.check(R.id.jump_short);
        } else if (jump == 3.0F) {
            jumpChooser.check(R.id.jump_xlong);
        } else {
            jumpChooser.check(R.id.jump_none);
        }

        int finger = sharedPreferences.getInt(FINGER_KEY, DEFAULT_FINGER);
        if(finger == 2) {
            fingerChooser.check(R.id.finger_indifferent);
        } else if (finger == 1) {
            fingerChooser.check(R.id.finger_escape);
        } else {
            fingerChooser.check(R.id.finger_follow);
        }

        int field = sharedPreferences.getInt(FIELD_KEY,DEFAULT_FIELD);
        if(field == R.drawable.food_background) {
            fieldChooser.check(R.id.field_food);
        } else if (field == R.drawable.stars_background) {
            fieldChooser.check(R.id.field_stars);
        } else {
            fieldChooser.check(R.id.field_towel);
        }

        Log.i("RobotBugs", "ControlFragment on resume called");
    }

    private boolean isLarge() {
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        return  displayMetrics.widthPixels > DISPLAY_SIZE_THRESHOLD ||
                displayMetrics.heightPixels > DISPLAY_SIZE_THRESHOLD;
    }
}