/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.inrating.poststat.ui;

import android.content.res.TypedArray;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.adapters.AbsListViewBindingAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.databinding.library.baseAdapters.BR;

import top.inrating.poststat.R;
import top.inrating.poststat.utills.Common;


public class BindingAdapters {

    @BindingAdapter("visibleGone")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("visibleInvisible")
    public static void visInvis(View view, boolean vis) {
        view.setVisibility(vis ? View.VISIBLE : View.INVISIBLE);
    }

    @BindingAdapter({"setAllImgsResIds", "setThisImgResIdIndex"})
    public static void resIdIndexToDrawable(View view, TypedArray resIds, int resIdIndex) {

        if (resIdIndex < 0) ((ImageView) view).setImageDrawable(null);
        else {
            int resId = resIds.getResourceId(resIdIndex, 0);
            ((ImageView) view).setImageResource(resId);
        }
    }

    @BindingAdapter({"setAllTxtsResIds", "setThisTxtResIdIndex"})
    public static void txtIndexIdToDrawable(View view, String[] txts, int txtIndex) {

        if (txts == null || txtIndex < 0 || txtIndex >= txts.length) ((TextView) view).setText("");
        else ((TextView) view).setText(txts[txtIndex]);
    }

    @BindingAdapter({"setUsersNicknames", "setUsersAvatars", "setScrollViewModel"})
    public static void setAdapter(View view, String names, String avatars, RVScrollViewModel scrollViewModel) {
        RecyclerView recyclerView = (RecyclerView) view;

        if (scrollViewModel != null) scrollViewModel.setMoreAmountRight(0);

        final LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(recyclerView.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);

        final HorizontalListAdapter horizontalListAdapter = new HorizontalListAdapter(names, avatars,
                recyclerView.getContext()); //getApplication());

        recyclerView.setAdapter(horizontalListAdapter);

        if (horizontalListAdapter.getItemCount() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            Log.d("test", "set adapter, items count = "+horizontalListAdapter.getItemCount());
        } else recyclerView.setVisibility(View.GONE);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            final LinearLayoutManager layoutManager = horizontalLayoutManager;
            final HorizontalListAdapter adapter = horizontalListAdapter;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (scrollViewModel != null) {
                    int max = adapter.getItemCount() - 1;
                    int last = layoutManager.findLastVisibleItemPosition();
                    int first = layoutManager.findFirstVisibleItemPosition();
                    Log.d("test", "visible position first " + first +
                            " last " + last + " and max " + max);
                    int num = max - last;
                    if (num < 0 ) num = 0;
                    if (first < 0) first = 0;
                    scrollViewModel.setMoreAmountRight(num);
                    scrollViewModel.setMoreAmountLeft(first);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Do something
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    // Do something
                } else {
                    // Do something
                }
            }
        });
        recyclerView.invalidate();
        Log.d("test", "set adapter, names = "+names);
        Log.d("test", "set adapter, avatars = "+avatars);
        Log.d("test", "set adapter, context is "+(recyclerView.getContext()==null?"null":"not null"));

        //horizontalListAdapter.notifyDataSetChanged();
    }

}