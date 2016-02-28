package own.alessio.com.robotbugs;


import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    /**
     * Reference for the shared preferences.
     */
    private static final String SHARED_PREFERENCES = "shared_preferences";

    /**
     * Key strings for the shared preferences.
     */
    private static final String POPULATION_KEY = "population_size_key";
    private static final String SPEED_KEY = "speed_key";
    private static final String JUMP_KEY = "jump_kep";
    private static final String FINGER_KEY = "finger_kep";
    private static final String FEEDBACK_KEY = "feedback_key";
    private static final String FIELD_KEY="field_key";
    private static final String INSECT_KEY = "insect_key";

    /**
     * the dynamic of the robots.
     */
    private static final long TIME_UNIT_TRANSLATE = 24;    // Default translation animation time length.
    private static final long TIME_UNIT_TURN = 32;         // Default rotation animation time length.
    private static final float THRESHOLD_DISTANCE = 50.0F; // Length of the robot visual field.
    private static final float THRESHOLD_COS = 0.0F;       // Angular visual field (cos(+/-90deg) = 0;
    private static final float UNIT_ANGLE = 45.0F;         // Unit angular movements.
    private static final float DISTANCE_CATCHED = 2.0F;    // Distance at witch the finger is "eaten" as multiple of the robot size.

    /**
     * Population size.
     */
    private static final int DEFAULT_COUNT = 5;            // Default number of robots.
    private static final int COUNT_UI_PARAM_ID = 0;        // Identifier of the robot count parameter.
    private int count = DEFAULT_COUNT;                     // Robot count

    /**
     * Speed.
     */
    private static final float DEFAULT_SPEED = 1.0F;        // Default speed as multiple of unit translation and rotation time.
    private long timeUnitTranslate = TIME_UNIT_TRANSLATE;   // Translation animation time length.
    private long timeUnitTurn = TIME_UNIT_TURN;             // Rotation animation time length.

    /**
     * Jump control.
     */
    private static final double DISPLACEMENT_UNIT = 1.0D;   // Default displacement as multiple of the robot size.
    private static final float DEFAULT_JUMP = 1.0F;         // Jump coefficient.
    private double displacementUnit = DISPLACEMENT_UNIT;    // Default displacement

    /**
     * How the robots react to the finger presence.
     */
    private static final int DEFAULT_FINGER = 0;            // Default finger reaction identifier.
    private int fingerReaction = DEFAULT_FINGER;            // Finger reation identifier, 0 = catch, 1 = escape, 2 = indifferent.

    /**
     * Background.
     */
    private static final int DEFAULT_FIELD = R.drawable.stars_background; // Default background.
    private static final int FIELD_UI_PARAM_ID = 1;     // Field parameter identifier.
    private int field = DEFAULT_FIELD;                  // Play field background.

    /**
     * Feedback.
     */
    private static final boolean DEFAULT_FEEDBACK = true; // As default the feedback is on.
    private boolean feedback = DEFAULT_FEEDBACK;            // true = feedback on, false = mute.

    /**
     * robot bug appearance.
     */
    private static final int DEFAULT_INSECT = R.drawable.ant;       // Ant is the default.
    private static final int RANDOM_INSECT = R.drawable.random_bug; // If this is selected the bug is chosed randomly.
    private int insectRes = DEFAULT_INSECT;                         // Bug appearance.

    /**
     * FenceWidget size control.
     */

    /**
     * Sizes as multiple of the device height and width.
     */
    private static final float TOP_MARG_VERT = 0.0F;    // Margin top in vertical configuration.
    private static final float TOP_MARG_HOR = 0.05F;    // Margin top in horizontal configuration.
    private static final float HEIGHT_VERT = 0.85F;     // Play field height in vertical configuration.
    private static final float HEIGHT_HOR = 0.8F;       // Play field height in horizontal configuration.
    private static final float LEFT_MARG_VERT = 0.05F;  // Margin left in vertical configuration.
    private static final float LEFT_MARG_HOR = 0.05F;   // Margin left in horizontal configuration.
    private static final float WIDTH = 0.9F;            // Play field width.

    /**
     * Absolute sizes in pizels.
     */
    private static final int TOP_ADD_MARG_VERT = 70; // Additional margin top in vertical configuration.
    private static final int RIGHT_ADD_MARG_HOR = 70;// Additional margin right in horizontal configuration.
    private static final int TOP_ADD_MARG_HOR = 50; // Additional margin top in horizontal configuration.

    /**
     * Finger detection.
     */
    private boolean isFingerDown = false;  // Is the finger touching the screen ?
    private float fingX = 0.0F;     // Finger x coordinate.
    private float fingY = 0.0F;     // Finger y coordinate.

    /**
     * Sounds.
     */
    private MediaPlayer beepSound; // Sound played as the finger leaves the play field.
    private MediaPlayer woopSound; // sound played as the finger is eaten.


    private ToggleButton startStop; // Start/stop game toggle button.

    private FenceWidget mFenceWidget;  // Play field widget.

    private RelativeLayout rl;  // Game relative layout.

    Toast toastOut;  // Toast shown as the finger leaves or the finger is eaten.

    /**
     * The ant widget is a widget that can show any robot bug. I used ants as I began developing the app.
     */
    ArrayList<AntWidget> antWidgetArrayList = new ArrayList<>();

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderFragment() {
    }

    /**
     * Initialize the game variables.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        rl = (RelativeLayout) rootView.findViewById(R.id.my_rl);

        /**
         * Set up ants and play field.
         */
        setWorld();

        /**
         * Set up the start/stop toggle button.
         */
        startStop = (ToggleButton) rootView.findViewById(R.id.runWorldBtn);
        startStop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /**
                 * Start the game only if the button is checked and the parameters weren't updated.
                 */
                if (isChecked && !resetWorld()) {
                    runWorld();
                    /**
                     * Otherwise reset play field and robots.
                     */
                } else {
                    resetWorld();
                }
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        try{
        if(getUserVisibleHint()) {
            resetWorld();
        }
            /**
             * Initialize the media player sounds.
             */
            beepSound = MediaPlayer.create(getActivity(), R.raw.beep_sound);
            woopSound = MediaPlayer.create(getActivity(), R.raw.woop_sound);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        Log.i("RobotBugs", "Placeholder fragment onResume called");
    }

    @Override
    public void onPause() {
        super.onPause();
        //if(!getUserVisibleHint()) {
        try{
            /**
             * Stop the game if running.
             */
            if (startStop.isChecked()) {
                startStop.setChecked(false);
            } else {
                resetWorld();
            }
            /**
             * Release media player sounds.
             */
            beepSound.release();
            woopSound.release();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        Log.i("RobotBugs","Placeholder fragment onPause called.");

    }

    /**
     * Set up play field and robot bugs.
     */
    public void setWorld() {
        /**
         * Detects the display sizes.
         */
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        /**
         * Take the parameters from the shared preferences.
         */
        updateParameters();

        int fenceHeight;
        int fenceWidth;
        int fenceTopPadding;
        int fenceLeftPadding;

        /**
         * Calculates the play field sizes.
         */
        if(displayMetrics.heightPixels > displayMetrics.widthPixels) {
            fenceTopPadding = (int) (toDensPx(displayMetrics.heightPixels) * TOP_MARG_VERT);
            fenceLeftPadding = (int) (toDensPx(displayMetrics.widthPixels) * LEFT_MARG_VERT);
            fenceHeight = (int) (toDensPx(displayMetrics.heightPixels) * HEIGHT_VERT - TOP_ADD_MARG_VERT);
            fenceWidth = (int) (toDensPx(displayMetrics.widthPixels) * WIDTH);
        } else {
            fenceTopPadding = (int) (toDensPx(displayMetrics.heightPixels) * TOP_MARG_HOR);
            fenceLeftPadding = (int) (toDensPx(displayMetrics.widthPixels) * LEFT_MARG_HOR);
            fenceHeight = (int) (toDensPx(displayMetrics.heightPixels) * HEIGHT_HOR - TOP_ADD_MARG_HOR);
            fenceWidth = (int) (toDensPx(displayMetrics.widthPixels) * WIDTH - RIGHT_ADD_MARG_HOR);
        }

        /**
         * Add the play field to the relative layout, also add the robot bugs.
         */
        mFenceWidget = addFence(fenceLeftPadding, fenceWidth, fenceTopPadding, fenceHeight);
        mFenceWidget.setField(getActivity(),field);

        /**
         * Detect finger and set up reaction as the finger leaves the screen.
         */
        mFenceWidget.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    /**
                     * As the finger touch the screen detect its position.
                     */
                    case MotionEvent.ACTION_DOWN:
                        isFingerDown = true;
                        fingX = event.getX();
                        fingY = event.getY();
                        Log.i("Action Down x", event.getX() + "");
                        Log.i("Action Down y", event.getY() + "");
                        break;
                    /**
                     * As the finger moves update its position and if the finger goes out of the
                     * play field show the "Finger out" toast.
                     */
                    case MotionEvent.ACTION_MOVE:
                        fingX = event.getX();
                        fingY = event.getY();
                        if (feedback) fingerOut(v, fingX, fingY);
                        break;
                    /**
                     * As the finger leaves the screen show the "Finger lifted" toast.
                     */
                    case MotionEvent.ACTION_UP:
                        if (feedback) fingerLifetd();
                        isFingerDown = false;
                        Log.i("Action Up x", event.getX() + "");
                        Log.i("Action Up y", event.getY() + "");
                        break;
                }
                return true;
            }
        });
    }

    /**
     * Start playing the game.
     */
    private void runWorld() {
        for (AntWidget antWidget: antWidgetArrayList) {
            act(antWidget,antWidget.nextMove(mFenceWidget),antWidget.nextTurn());
        }
    }

    /**
     * Update the world parameters. The returned hash map will tell which setting was updated.
     */
    public boolean resetWorld() {

        HashMap<Integer,Boolean> uiParamChanged = updateParameters();
        boolean returnValue = false;

        if(uiParamChanged.get(FIELD_UI_PARAM_ID)) {
            startStop.setChecked(false);
            if(mFenceWidget != null) {
                mFenceWidget.setField(getActivity(),field);
            }
        }

        if(uiParamChanged.get(COUNT_UI_PARAM_ID)) {
            startStop.setChecked(false);
            addAnts(count, mFenceWidget);
            returnValue = true;
        }

        return returnValue;
    }

    /**
     * Update parameters from shared preferences.
     */
    private HashMap<Integer,Boolean> updateParameters() {

        HashMap<Integer,Boolean> uiParamsChanged = new HashMap<>();


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES,0);
        int retCount = sharedPreferences.getInt(POPULATION_KEY, DEFAULT_COUNT);
        int mInsectRes = sharedPreferences.getInt(INSECT_KEY,DEFAULT_INSECT);
        if(retCount != count || mInsectRes != insectRes) {
            count = retCount;
            insectRes = mInsectRes;
            uiParamsChanged.put(COUNT_UI_PARAM_ID,true);
        } else {
            uiParamsChanged.put(COUNT_UI_PARAM_ID,false);
        }

        Float speed = sharedPreferences.getFloat(SPEED_KEY, DEFAULT_SPEED);
        timeUnitTranslate = (long) ((float)TIME_UNIT_TRANSLATE*speed);
        timeUnitTurn = (long) ((float)TIME_UNIT_TURN*speed);

        Float jump = sharedPreferences.getFloat(JUMP_KEY, DEFAULT_JUMP);
        displacementUnit = (double) jump * DISPLACEMENT_UNIT;

        fingerReaction = sharedPreferences.getInt(FINGER_KEY, DEFAULT_FINGER);

        int retField = sharedPreferences.getInt(FIELD_KEY,DEFAULT_FIELD);
        if(retField != field) {
            field = retField;
            uiParamsChanged.put(FIELD_UI_PARAM_ID,true);
        } else {
            uiParamsChanged.put(FIELD_UI_PARAM_ID,false);
        }

        feedback = sharedPreferences.getBoolean(FEEDBACK_KEY, DEFAULT_FEEDBACK);
        Log.i("RobotBugs", "feedback = " + feedback);

        return uiParamsChanged;
    }

    /**
     * Add the play field to the relative layout, also add the robot bugs.
     */
    private FenceWidget addFence(int left,int width,int top,int height) {
        FenceWidget fenceWidget = new FenceWidget(getActivity(),width,height,left,top);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = fenceWidget.getXMin();
        layoutParams.topMargin = fenceWidget.getYMin();
        fenceWidget.setLayoutParams(layoutParams);
        rl.addView(fenceWidget);
        addAnts(count, fenceWidget);
        return fenceWidget;
    }

    /**
     * Add the robot bugs to the relative layout.
     */
    private void addAnts(int mCount, FenceWidget fenceWidget) {
        /**
         * First clean the relative layout.
         */
        if(!antWidgetArrayList.isEmpty()) {
            for (AntWidget antWidget : antWidgetArrayList) {
                rl.removeView(antWidget);
            }
            antWidgetArrayList.clear();
        }
        /**
         * If the user didn't choose the random bug add bug according to the choice.
         */
        if(insectRes != RANDOM_INSECT) {
            for (int i = 0; i < mCount; i++) {
                AntWidget wAnt = addAnt(fenceWidget);
                wAnt.setInsectRes(getActivity(), insectRes);
                antWidgetArrayList.add(wAnt);
            }
            /**
             * Otherwise choose the bugs randomly.
             */
        } else {
            for (int i = 0; i < mCount; i++) {
                AntWidget wAnt = addAnt(fenceWidget);
                InsectsAdapter insectsAdapter = new InsectsAdapter(getActivity());
                wAnt.setInsectRes(getActivity(), insectsAdapter.getRandomRes());
                antWidgetArrayList.add(wAnt);
            }
        }
    }

    /**
     * Put a new ant into the world.
     */
    private AntWidget addAnt(final FenceWidget myFenceWidget) {
        AntWidget mAnt = new AntWidget(getActivity(), 360.0f * (float) Math.random(), new AntWidget.NextMovement() {
            @Override
            public AntMovements.TurnInt nextTurn(AntWidget antWidget) {

                if(isFingerDown && lookForward(antWidget,antWidgetArrayList) &&
                        myFenceWidget.canMove(antWidget,displacementUnit) ) {
                    return toFinger(antWidget);
                } else {

                    switch ((int) Math.round(Math.random())) {
                        case 0:
                            return AntMovements.TurnInt.RIGHT;
                        case 1:
                            return AntMovements.TurnInt.LEFT;
                        default:
                            return AntMovements.TurnInt.NONE;
                    }
                }
            }

            @Override
            public AntMovements.MoveInt nextMove(AntWidget antWidget) {
                if(myFenceWidget.canMove(antWidget,displacementUnit) &&
                        lookForward(antWidget,antWidgetArrayList)) {
                    return AntMovements.MoveInt.FORWARD;
                }
                else {
                    return AntMovements.MoveInt.STAY;
                }
            }
        });

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = myFenceWidget.getRandomX(mAnt);
        params.topMargin = myFenceWidget.getRandomY(mAnt);
        mAnt.setLayoutParams(params);
        rl.addView(mAnt);
        return mAnt;
    }

    /**
     * HELPER METHODS.
     */

    /**
     * You lifted up your finger.
     */
    private void fingerLifetd() {
        beepSound.start();

        TextView textView = new TextView(getActivity());
        textView.setText(getString(R.string.phf_finger_lifted));
        textView.setTextSize(getResources().getDimension(R.dimen.toast_size));
        textView.setTextColor(Color.parseColor("#212121"));
        if (toastOut != null) toastOut.cancel();
        toastOut = new Toast(getActivity());
        toastOut.setView(textView);
        toastOut.setDuration(Toast.LENGTH_SHORT);
        toastOut.show();
    }

    /**
     * Your finger moved out from the frame.
     */
    private void fingerOut(View mV,float fX,float fY) {
        if((fX>mV.getMeasuredWidth()) ||
                (fY>mV.getMeasuredHeight()) ||
                (fX <0.0F) ||
                (fY < 0.0F)) {
            beepSound.start();

            TextView textView = new TextView(getActivity());
            textView.setText(getString(R.string.phf_out));
            textView.setTextSize(getResources().getDimension(R.dimen.toast_size));
            textView.setTextColor(Color.parseColor("#212121"));
            if (toastOut != null) toastOut.cancel();
            toastOut = new Toast(getActivity());
            toastOut.setView(textView);
            toastOut.setDuration(Toast.LENGTH_SHORT);
            toastOut.show();
        }
    }

    /**
     * Your finger has been eaten.
     */
    private void fingerEaten(AntWidget mmAnt,float mIntX, float mIntY) {
            if(dist((int) mIntX,(int) mIntY) < (mmAnt.getSize()*DISTANCE_CATCHED)) {
                woopSound.start();

                ImageView imageView = new ImageView(getActivity());
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getActivity(), R.drawable.bite_icon));

                if (toastOut != null) toastOut.cancel();
                toastOut = new Toast(getActivity());
                toastOut.setView(imageView);
                toastOut.setDuration(Toast.LENGTH_SHORT);
                toastOut.show();
            }
    }

    /**
     * Return turn movement according to the selected finger reaction.
     */
    private AntMovements.TurnInt toFinger(AntWidget antWidget) {

        switch (fingerReaction) {
            case 0:
                return followFinger(antWidget);
            case 1:
                return escapeFinger(antWidget);
            case 2:
                return indifferentFinger(antWidget);
            default:
                return AntMovements.TurnInt.NONE;
        }

    }

    /**
     * Turn the bug to follow the finger.
     */
    private AntMovements.TurnInt followFinger(AntWidget antWidget) {
        float dispX = fingX - antWidget.getLeft();
        float dispY = fingY - antWidget.getTop();

        if(feedback) fingerEaten(antWidget,dispX,dispY);

        float sinDisp = sinAbs(dispX,dispY);
        float cosDisp = cosAbs(dispX, dispY);

        if((sinDisp != -2.0f) && (cosDisp != -2.0f)) {
            float dirX = calcXDisp(antWidget, AntMovements.MoveInt.FORWARD);
            float dirY = calcYDisp(antWidget, AntMovements.MoveInt.FORWARD);

            float deltaSin = sinAbs(dispX, dispY) * cosAbs(dirX, dirY) - cosAbs(dispX, dispY) * sinAbs(dirX, dirY);

            if (deltaSin < 0) {
                return AntMovements.TurnInt.LEFT;
            } else if (deltaSin > 0) {
                return AntMovements.TurnInt.RIGHT;
            } else {
                return AntMovements.TurnInt.NONE;
            }
        }
        return AntMovements.TurnInt.NONE;
    }

    /**
     * Turn the bug to escape the finger.
     */
    private AntMovements.TurnInt escapeFinger(AntWidget antWidget) {
        float dispX = fingX - antWidget.getLeft();
        float dispY = fingY - antWidget.getTop();

        if(feedback) fingerEaten(antWidget,dispX,dispY);

        float sinDisp = sinAbs(dispX,dispY);
        float cosDisp = cosAbs(dispX, dispY);

        if((sinDisp != -2.0f) && (cosDisp != -2.0f)) {
            float dirX = calcXDisp(antWidget, AntMovements.MoveInt.FORWARD);
            float dirY = calcYDisp(antWidget, AntMovements.MoveInt.FORWARD);

            float deltaSin = sinAbs(dispX, dispY) * cosAbs(dirX, dirY) - cosAbs(dispX, dispY) * sinAbs(dirX, dirY);

            if (deltaSin < 0) {
                return AntMovements.TurnInt.RIGHT;
            } else if (deltaSin > 0) {
                return AntMovements.TurnInt.LEFT;
            } else {
                return AntMovements.TurnInt.NONE;
            }
        }
        return AntMovements.TurnInt.NONE;
    }

    /**
     * Be indifferent to finger, but ne aware of its position and if it comes across eat it!!!
     */
    private AntMovements.TurnInt indifferentFinger(AntWidget antWidget) {
        float dispX = fingX - antWidget.getLeft();
        float dispY = fingY - antWidget.getTop();

        if(feedback) fingerEaten(antWidget,dispX,dispY);
        switch ((int) Math.round(Math.random())) {
            case 0:
                return AntMovements.TurnInt.RIGHT;
            case 1:
                return AntMovements.TurnInt.LEFT;
            default:
                return AntMovements.TurnInt.NONE;
        }
    }

    /**
     * Check around yourself and see if there is another bug in front of you.
     */
    private boolean lookForward(AntWidget mAnt,ArrayList<AntWidget> mAntWidgets) {

        int maxDispX = calcXDisp(mAnt, AntMovements.MoveInt.FORWARD);
        int maxDispY = calcYDisp(mAnt, AntMovements.MoveInt.FORWARD);

        int distX,distY;

        for(AntWidget antWidget: mAntWidgets) {
            if(!mAnt.equals(antWidget)) {
                distY = antWidget.getTop() - mAnt.getTop();
                distX = antWidget.getLeft() - mAnt.getLeft();
                if (dist(distX, distY) <=
                        AntMovements.lengthOf(AntMovements.MoveInt.FORWARD)*THRESHOLD_DISTANCE) {
                    if (cosAngle(distX, distY, maxDispX, maxDispY) > THRESHOLD_COS) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Float scalar product of two integer 2D vectors coordinates.
     */
    private float prdScal(int dx1, int dy1, int dx2, int dy2) {
        return (float) (dx1*dx2 + dy1*dy2);
    }

    /**
     * Float norm of an integer 2D vector coordinates.
     */
    private float dist(int dx, int dy) {
        return (float) Math.sqrt((double) (dx*dx + dy*dy));
    }

    /**
     * Angle cosine.
     */
    private float cosAngle(int dx1,int dy1, int dx2, int dy2) {
        float d1 = dist(dx1,dy1);
        float d2 = dist(dx2,dy2);
        if((d1>0.0f) && (d2>0.0f)) {
            return prdScal(dx1,dy1,dx2,dy2)/(d1*d2);
        }
        return 1.0f;
    }

    /**
     * Sine of the angle between the horizontal line and the vector coordinates.
     */
    private float sinAbs(float dx,float dy) {
        float d = dist((int)dx,(int)dy);
        ;
        if(d>0.0f) {
            return dy/d;
        }
        return -2.0f;
    }

    /**
     * Cosine of the angle between the horizontal line and the vector coordinates.
     */
    private float cosAbs(float dx,float dy) {
        float d = dist((int)dx,(int)dy);
        ;
        if(d>0.0f) {
            return dx/d;
        }
        return -2.0f;
    }

    /**
     * Calculates bug displacement along X.
     */
    private int calcXDisp(AntWidget mAnt, AntMovements.MoveInt moveInt) {
        return (int) (Math.round((double) mAnt.getMeasuredWidth() *displacementUnit
                * Math.sin((Math.PI / 180) * mAnt.getOrientation()))
                * AntMovements.lengthOf(moveInt));
    }

    /**
     * Calculates bug displacement along Y.
     */
    private int calcYDisp(AntWidget mAnt, AntMovements.MoveInt moveInt) {
        return -(int) (Math.round((double) mAnt.getMeasuredHeight() *displacementUnit
                * Math.cos((Math.PI / 180) * mAnt.getOrientation()))
                * AntMovements.lengthOf(moveInt));
    }

    /**
     * Converts dp into px.
     */
    private float toDensPx(float px) {
        Resources resources = this.getResources();
        DisplayMetrics displayMetrics =resources.getDisplayMetrics();
        try {
            return px / (displayMetrics.densityDpi / 160.0f);
        } catch (ArithmeticException e) {
            e.printStackTrace();
            return 1.0f;
        }
    }

    /**
     * MOVEMENTS DEFINITION.
     */

    /**
     * Animates ant translation, then on animation end calls for rotation.
     */
    public void act(final AntWidget mAnt, final AntMovements.MoveInt moveInt, final AntMovements.TurnInt turnInt) {
        /**
         * Retrive ant orientation.
         */
        float direction = mAnt.getOrientation();

        /**
         * Calculate direction.
         */
        int dispY = calcYDisp(mAnt, moveInt);
        int dispX = calcXDisp(mAnt, moveInt);

        final int newPosY = mAnt.getTop() + dispY;
        final int newPosX = mAnt.getLeft() + dispX;

        TranslateAnimation translateAnimation = new TranslateAnimation(0,
                dispX,
                0,
                dispY);
        translateAnimation.setFillEnabled(true);
        translateAnimation.setDuration(timeUnitTranslate);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAnt.layout(newPosX, newPosY, newPosX + mAnt.getMeasuredWidth(), newPosY + mAnt.getMeasuredHeight());
                turnMe(mAnt, turnInt, newPosX, newPosY);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mAnt.startAnimation(translateAnimation);
    }

    /**
     * Rotation called as translation occurred.
     */
    public void turnMe(final AntWidget mAnt, final AntMovements.TurnInt turn,
                       final int newPosX, final int newPosY) {
        RotateAnimation rotateAnimation = new RotateAnimation(
                0,
                UNIT_ANGLE * AntMovements.angleOf(turn),
                mAnt.getMeasuredWidth() / 2,
                mAnt.getMeasuredHeight() / 2);
        rotateAnimation.setFillEnabled(true);
        rotateAnimation.setDuration(timeUnitTurn);
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAnt.setOrientation(mAnt.getOrientation() + UNIT_ANGLE * AntMovements.angleOf(turn));
                mAnt.invalidate();
                mAnt.layout(newPosX, newPosY, newPosX + mAnt.getMeasuredWidth(), newPosY + mAnt.getMeasuredHeight());
                if (startStop.isChecked()) {
                    act(mAnt, mAnt.nextMove(mFenceWidget), mAnt.nextTurn());
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mAnt.startAnimation(rotateAnimation);
    }
}
