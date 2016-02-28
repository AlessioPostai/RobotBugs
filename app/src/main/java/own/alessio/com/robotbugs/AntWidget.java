package own.alessio.com.robotbugs;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * MyWorld Created by alessio on 24.1.16.
 * The ant widget is used for all types of robot bugs.
 */
public class AntWidget extends View {

    /**
     * This is an interface used to define the robot bug behaviour.
     */
    public interface NextMovement{
        AntMovements.TurnInt nextTurn(AntWidget antWidget);
        AntMovements.MoveInt nextMove(AntWidget antWidget);
    }

    /**
     * Context.
     */
    Context context;

    /**
     * Interface.
     */
    private NextMovement nextMovement;

    /**
     * Includes ant actions.
     */

    private float bmpSize;

    /**
     * Bug size.
     */
    private final static float M_SIZE = 28.0f;
    private static float SIZE;

    /**
     * Scaling factor.
     */
    private float scaleFactor = 1;

    /**
     * Bug orientation.
     */
    private float orientation;

    /**
     * Bitmap for the bug appearance.
     */
    Bitmap antBmp;

    public AntWidget(Context mContext,
                     float mOrientation,
                     NextMovement mNextMovement) {
        super(mContext);

        this.context = mContext;
        antBmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ant);
        bmpSize = Math.max(antBmp.getWidth(), antBmp.getHeight());
        SIZE = toPixel(M_SIZE);

        this.orientation = mOrientation;
        this.scaleFactor = SIZE/bmpSize;
        this.nextMovement = mNextMovement;

        setMinimumWidth((int)(SIZE));
        setMinimumHeight((int) (SIZE));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec) {
        setMeasuredDimension(
                getSuggestedMinimumWidth(),
                getSuggestedMinimumHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        canvas.translate(SIZE * .5f, SIZE * .5f);
        canvas.rotate(orientation);
        canvas.scale(scaleFactor, scaleFactor);

        canvas.drawBitmap(antBmp, -bmpSize * 0.5f, -bmpSize * 0.5f, paint);
    }

    /**
     * HELPER METHODS.
     */

    /**
     * Returns the downward component (y component) of the ant direction.
     */
    public double getDownward() {
        return  -Math.cos((double) this.getOrientation() *(Math.PI/180.0));
    }

    /**
     * Returns the rightward component (x component) of the ant direction.
     */
    public double getRightward() {
        return  Math.sin((double) this.getOrientation() * (Math.PI / 180.0));
    }

    /**
     * Converts dp into px.
     */
    private float toPixel(float dp) {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics =resources.getDisplayMetrics();
        return dp*(displayMetrics.densityDpi/160.0f);
    }

    /**
     * GETTER METHODS.
     */

    /**
     * Return the bug orientation.
     */
    public float getOrientation() {
        return orientation;
    }

    /**
     * Return the bug size.
     */
    public float getSize() {
        return SIZE;
    }

    /**
     * SETTER METHODS.
     */

    /**
     * Set the bug orientation.
     */
    public void setOrientation(float mOrientation) {
        orientation = mOrientation;
    }

    public void setInsectRes(Context mContext,int insectRes) {
        antBmp = BitmapFactory.decodeResource(mContext.getResources(), insectRes);
        bmpSize = Math.max(antBmp.getWidth(), antBmp.getHeight());
        this.scaleFactor = SIZE/bmpSize;
        this.invalidate();
    }

    /**
     * ANT BEHAVIOUR.
     */

    /**
     * How ant decide the next turn.
     */
    public AntMovements.TurnInt nextTurn() {
        return nextMovement.nextTurn(this); // Taken from interface implementation.
    }

    /**
     * How ant decide the next translation.
     */
    public AntMovements.MoveInt nextMove(FenceWidget fenceWidget) {
        return nextMovement.nextMove(this); // Taken from interface implementation.
    }
}