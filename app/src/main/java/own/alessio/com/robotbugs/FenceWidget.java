package own.alessio.com.robotbugs;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * MyWorld Created by alessio on 6.2.16.
 */
public class FenceWidget extends View {

    private int xRange, yRange,xMin,yMin;

    private Context context;

    private Bitmap backGround;

    public FenceWidget(Context mContext,
                       int mXRange,
                       int mYRange,
                       int mXmin,
                       int mYmin) {
        super(mContext);
        this.context = mContext;
        this.xRange = (int) toPixel(mXRange);
        this.yRange = (int) toPixel(mYRange);
        this.xMin = (int) toPixel(mXmin);
        this.yMin = (int) toPixel(mYmin);

        this.backGround = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.wood_background);
        setMinimumWidth(xRange);
        setMinimumHeight(yRange);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec) {
        setMeasuredDimension(
                getSuggestedMinimumWidth(),
                getSuggestedMinimumHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        Rect rect = new Rect(0,0,xRange,yRange);
        canvas.drawBitmap(backGround, rect, rect, paint);
    }

    /**
     * Return true if the bug can move, false if it can't.
     */
    public boolean canMove(AntWidget mAnt,double dispUnit) {
        return !(throughTop(mAnt,dispUnit) ||
                throughBottom(mAnt,dispUnit) ||
                throughRight(mAnt,dispUnit) ||
                throughLeft(mAnt,dispUnit));
    }

    /**
     * MOVEMENTS CRITERIA.
     */

    /**
     * Return true if
     * the robot bug is already on the top of the play field and is trying to cross it.
     */
    private boolean throughTop(AntWidget mAnt,double dispUnit) {
        return (((mAnt.getTop() - mAnt.getMeasuredHeight()*dispUnit)
                <= this.getTop()) && (mAnt.getDownward()<=0));
    }

    /**
     * Return true if
     * the robot bug is already on the bottom of the play field and is trying to cross it.
     */
    private boolean throughBottom(AntWidget mAnt,double dispUnit) {
        return (((mAnt.getTop() + 2*mAnt.getMeasuredHeight()*dispUnit)
                >= (this.getTop()+this.yRange))
                && (mAnt.getDownward()>=0));
    }

    /**
     * Return true if
     * the robot bug is already at the left border and is tryiing to cross it.
     */
    private boolean throughLeft(AntWidget mAnt,double dispUnit) {
        return (((mAnt.getLeft() - mAnt.getMeasuredWidth()*dispUnit)
                <= this.getLeft()) && (mAnt.getRightward()<=0));
    }

    /**
     * Return true if
     * the robot bug is already at the right border and is tryiing to cross it.
     */
    private boolean throughRight(AntWidget mAnt,double dispUnit) {
        return (((mAnt.getLeft() + 2*mAnt.getMeasuredWidth()*dispUnit)
                >= (this.getLeft()+this.xRange)) && (mAnt.getRightward()>=0));
    }

    /**
     * GETTER METHODS.
     */

    /**
     * Return the minimum x coordinate for the robot bug.
     */
    public int getXMin() {
        return xMin;
    }

    /**
     * Return the minimum y coordinate for the robot bug.
     */
    public int getYMin() {
        return yMin;
    }

    /**
     * Get a random x coordinate within the play field.
     */
    public int getRandomX(AntWidget mAnt) {
        return this.xMin + (int) (Math.random()*(double)(xRange-(int) mAnt.getSize()));
    }

    /**
     * Get a random y coordinate within the play field.
     */
    public int getRandomY(AntWidget mAnt) {
        return this.yMin + (int) (Math.random()*(double)(yRange-(int) mAnt.getSize()));
    }

    /**
     * Set the background bitmap.
     */
    public void setField(Context mContext, int resField) {
        this.backGround = BitmapFactory.decodeResource(mContext.getResources(),
                resField);
        this.invalidate();
    }

    /**
     * Converts dp into px.
     */
    private float toPixel(float dp) {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics =resources.getDisplayMetrics();
        return dp*(displayMetrics.densityDpi/160.0f);
    }
}
