package own.alessio.com.robotbugs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * RobotBugs Created by alessio on 13.2.16.
 */
public class InsectsFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Shared preferences identifier and keys values.
     */
    private static final String SHARED_PREFERENCES = "shared_preferences";
    private static final String INSECT_KEY = "insect_key";

    public static InsectsFragment newInstance(int controlFragmentNumber) {
        InsectsFragment insectsFragment = new InsectsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,controlFragmentNumber);
        insectsFragment.setArguments(args);
        return insectsFragment;
    }

    public InsectsFragment() {
    }


    /**
     * Robot bugs grid view.
     */

    GridView gridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.insects_fragment,container,false);

        gridView = (GridView) view.findViewById(R.id.insects_chooser);
        final InsectsAdapter insectsAdapter = new InsectsAdapter(getActivity());

        gridView.setAdapter(insectsAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int insectResource = insectsAdapter.getInsectRes(position);

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(INSECT_KEY, insectResource);
                editor.apply();

                ImageView imageView = new ImageView(getActivity());
                imageView.setImageResource(insectResource);
                Toast toast = new Toast(getActivity());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(imageView);
                toast.show();
            }
        });

        return view;
    }
}
