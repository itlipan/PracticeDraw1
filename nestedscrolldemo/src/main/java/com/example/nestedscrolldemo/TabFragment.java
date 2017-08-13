package com.example.nestedscrolldemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lee on 2017/8/12.
 */

public class TabFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_tab_content, container, false);
        initViewState(view);
        return view;
    }

    private void initViewState(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ListAdapter adapter = new ListAdapter();
        recyclerView.setAdapter(adapter);
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            data.add(String.valueOf(i));
        }
        adapter.setData(data);
    }

    static class ListAdapter extends RecyclerView.Adapter<TvViewHolder> {

        private List<String> mList = new ArrayList<>();

        @Override
        public TvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_content, parent, false);
            return new TvViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(TvViewHolder holder, int position) {
            TextView textView = (TextView) holder.itemView.findViewById(R.id.tv_title);
            textView.setText(mList.get(position));
        }

        void setData(List<String> list) {
            mList.clear();
            notifyDataSetChanged();
            mList.addAll(list);
            notifyItemRangeInserted(0, list.size());
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    static class TvViewHolder extends RecyclerView.ViewHolder {

        TvViewHolder(View itemView) {
            super(itemView);
        }
    }
}
