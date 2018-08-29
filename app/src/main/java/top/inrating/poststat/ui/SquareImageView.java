package top.inrating.poststat.ui;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import top.inrating.poststat.R;

/**
 * Created by alexandr on 12.12.17.
 */

public class SquareImageView extends AppCompatImageView {
  private final String TAG = "SquareImageView";

  private final int RATIO = 5;

  private Context context;

  public SquareImageView(Context context) {
    super(context);
  }

  public SquareImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    int h;
    int w;
    View root = getRootView();
    View hrvContainer = root.findViewById(R.id.hrv_container);
    Log.d("test", "hrv container "+(hrvContainer==null?"not found":"found"));
    if (hrvContainer != null) {
      h = hrvContainer.getHeight();
      w = hrvContainer.getWidth();
      Log.d("test", "hrv container width = "+w);
    } else {
      Rect windowRect = new Rect();
      getWindowVisibleDisplayFrame(windowRect);
      h = windowRect.bottom - windowRect.top;
      w = windowRect.right - windowRect.left;
    }

    int width = w / RATIO;

    setMeasuredDimension(width, width);
  }
}
