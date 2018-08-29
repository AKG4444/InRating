package top.inrating.poststat.ui;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import top.inrating.poststat.BR;

/**
 * Created by alexandr on 13.12.17.
 */

public class RVScrollViewModel extends BaseObservable {

    private int moreAmountLeft;
    private int moreAmountRight = 0;

    @Bindable
    public int getMoreAmountLeft() {
        return moreAmountLeft;
    }

    @Bindable
    public int getMoreAmountRight() {
        return moreAmountRight;
    }

    public void setMoreAmountLeft(int moreAmountLeft) {
        this.moreAmountLeft = moreAmountLeft;
        notifyPropertyChanged(BR.moreAmountLeft);
    }

    public void setMoreAmountRight(int moreAmountRight) {
        this.moreAmountRight = moreAmountRight;
        notifyPropertyChanged(BR.moreAmountRight);
        Log.d("test", "set more amount right: "+moreAmountRight);
    }

    public RVScrollViewModel(int moreAmountLeft, int moreAmountRight) {
        this.moreAmountLeft = moreAmountLeft;
        this.moreAmountRight = moreAmountRight;
    }
}