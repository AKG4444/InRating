package top.inrating.poststat.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import top.inrating.poststat.R;

/**
 * Created by alexandr on 09.12.17.
 */

public class CustomLayoutPadded extends LinearLayout {
    private final String TAG = "CustomLayoutPadded";
    private final int RATIO = 5;
    private Context context;
    private int paddingRight;

    public CustomLayoutPadded(Context context) {
        super(context);
    }

    public CustomLayoutPadded(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLayoutPadded(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int h;
        int w = 0;
        View root = getRootView();
        final View hrvContainer = root.findViewById(R.id.hrv_container);

        if (hrvContainer != null) {
            h = hrvContainer.getHeight();
            w = hrvContainer.getWidth();
            Log.d("test", "hrv container width = "+w);
        }
        if (w == 0) {
            Rect windowRect = new Rect();
            getWindowVisibleDisplayFrame(windowRect);
            h = windowRect.bottom - windowRect.top;
            w = windowRect.right - windowRect.left;
        }

        w = w + 4;
        int width = w / RATIO;
        View v = getChildAt(1); // textview
        int tvHeight = 0;
        if (v != null) {
            tvHeight = v.getHeight();
        }
        getChildAt(0).measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY));
        setMeasuredDimension(width, width+tvHeight);
    }
}

