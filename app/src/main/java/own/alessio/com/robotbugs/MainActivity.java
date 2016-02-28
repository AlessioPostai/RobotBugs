package own.alessio.com.robotbugs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    /**
     * Reference for the shared preferences.
     */
    private static final String SHARED_PREFERENCES = "shared_preferences";

    /**
     * Key strings for the shared preferences.
     */
    private static final String FEEDBACK_KEY = "feedback_key"; // Feedback on/off variable key for shared preferences.

    private static final String ANDROID_SWITCHER = "android:switcher:";

    private int oldPostion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        /*
      The {@link android.support.v4.view.PagerAdapter} that will provide
      fragments for each of the sections. We use a
      {@link FragmentPagerAdapter} derivative, which will keep every
      loaded fragment in memory. If this becomes too memory intensive, it
      may be best to switch to a
      {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        /*
      The {@link ViewPager} that will host the section contents.
     */
        /**
         * Non swipeable adapter, I switched the swipe off in order to allow the game with the finger.
         */
        final NonSwipeableViewPager mViewPager = (NonSwipeableViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment oldFragment = getActiveFragment(oldPostion,mViewPager.getId());
                Fragment newFragment = getActiveFragment(position,mViewPager.getId());
                oldFragment.setUserVisibleHint(false);
                oldFragment.onPause();
                newFragment.setUserVisibleHint(true);
                newFragment.onResume();
                oldPostion = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * Radio list options menu for the option feedback or mute.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, 0);

        boolean feedback = sharedPreferences.getBoolean(FEEDBACK_KEY, true);

        if(feedback) {
            MenuItem feesbackYes = menu.findItem(R.id.menu_feedback);
            feesbackYes.setChecked(true);
        } else {
            MenuItem feedbackNo = menu.findItem(R.id.menu_mute);
            feedbackNo.setChecked(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        switch (id) {
            case R.id.menu_feedback:
                item.setChecked(true);
                editor.putBoolean(FEEDBACK_KEY,true);
                break;
            case R.id.menu_mute:
                item.setChecked(true);
                editor.putBoolean(FEEDBACK_KEY,false);
                break;
        }
        editor.apply();
        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position) {
                case 0:
                    return PlaceholderFragment.newInstance(position + 1);
                case 1:
                    return InsectsFragment.newInstance(position+1);
                case 2:
                    return ControlFragment.newInstance(position+1);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab_field);
                case 1:
                    return getString(R.string.tab_settings);
                case 2:
                    return getString(R.string.tab_instects_chooser);
            }
            return null;
        }
    }

    /**
     * HELPER METHODS.
     */

    /**
     * I manipulated the onPause and onResume fragments methods to allow immediate parameters updating.
     */
    private Fragment getActiveFragment(int pos, int viewPagerId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return fragmentManager.findFragmentByTag(ANDROID_SWITCHER + viewPagerId + ":" + pos);
    }
}
