package own.alessio.com.robotbugs;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * RobotBugs Created by alessio on 20.2.16.
 * A specific array adapter for robot bugs.
 */
public class InsectsAdapter extends BaseAdapter {

    private Context myContext;

    public InsectsAdapter(Context c) {
        this.myContext = c;
    }

    @Override
    public int getCount() {
        return mThumbNailsId.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if(convertView == null) {
            imageView = new ImageView(myContext);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setScaleX(0.4F);
            imageView.setScaleY(0.4F);
            imageView.setPadding(5,5,5,5);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbNailsId[position]);

        return imageView;
    }

    private Integer[] mThumbNailsId = {
            R.drawable.ant,
            R.drawable.lady_bug,
            R.drawable.strange_bug,
            R.drawable.bug_bug,
            R.drawable.spider,
            R.drawable.thermite,
            R.drawable.bugy_bug,
            R.drawable.random_bug
    };


    /**
     * GETTER METHODS.
     */
    public int getInsectRes(int position) {
        if(position>=0 &&
                position < mThumbNailsId.length) {
            return mThumbNailsId[position];
        }
        return mThumbNailsId[0];
    }


    public int getRandomRes() {
        return mThumbNailsId[(int) (Math.random()*(double) (mThumbNailsId.length-1))];
    }


}
