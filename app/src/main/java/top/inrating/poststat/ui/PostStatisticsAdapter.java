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

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import top.inrating.poststat.R;
import top.inrating.poststat.databinding.PostStatisticsItemBinding;
import top.inrating.poststat.model.PostStatistics;


public class PostStatisticsAdapter extends
        RecyclerView.Adapter<PostStatisticsAdapter.PostStatisticsViewHolder> {

    List<? extends PostStatistics> mPostStatisticsList;

    @Nullable
    private final PostStatisticsClickCallback mPostStatisticsClickCallback;

    public PostStatisticsAdapter(@Nullable PostStatisticsClickCallback clickCallback) {
        mPostStatisticsClickCallback = clickCallback;
    }

    public void setPostStatisticsList(final List<? extends PostStatistics> postStatisticsList) {
        if (mPostStatisticsList == null) {
            mPostStatisticsList = postStatisticsList;
            notifyItemRangeInserted(0, postStatisticsList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mPostStatisticsList.size();
                }

                @Override
                public int getNewListSize() {
                    return postStatisticsList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mPostStatisticsList.get(oldItemPosition).getId() ==
                            postStatisticsList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    PostStatistics newPostStatistics = postStatisticsList.get(newItemPosition);
                    PostStatistics oldPostStatistics = mPostStatisticsList.get(oldItemPosition);

                    return newPostStatistics.getPostId() == oldPostStatistics.getPostId()
                            && newPostStatistics.getUsersNicknames().equals(oldPostStatistics.getUsersNicknames())
                            && newPostStatistics.getUsersAvatars().equals(oldPostStatistics.getUsersAvatars());
                }
            });
            mPostStatisticsList = postStatisticsList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public PostStatisticsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PostStatisticsItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.post_statistics_item,
                        parent, false);
        binding.setCallback(mPostStatisticsClickCallback);
        return new PostStatisticsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(PostStatisticsViewHolder holder, int position) {
        holder.binding.setPoststatistics(mPostStatisticsList.get(position));
        holder.binding.setRVScrollViewModel(new RVScrollViewModel(0, 0));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mPostStatisticsList == null ? 0 : mPostStatisticsList.size();
    }

    static class PostStatisticsViewHolder extends RecyclerView.ViewHolder {

        final PostStatisticsItemBinding binding;

        public PostStatisticsViewHolder(PostStatisticsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
