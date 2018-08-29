package top.inrating.poststat.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by alexandr on 09.12.17.
 */

public class CustomLayout extends LinearLayout {
    private final String TAG = "CustomLayout";
    private Context context;

    public CustomLayout(Context context) {
        super(context);
    }

    public CustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Rect windowRect = new Rect();
        getWindowVisibleDisplayFrame(windowRect);
        int h = windowRect.bottom - windowRect.top;
        int w = windowRect.right - windowRect.left;
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int hmargins = Math.round(2 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        w = w - hmargins;
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(w/5, heightSize);
    }
}

