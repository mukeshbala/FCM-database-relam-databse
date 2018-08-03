package com.secure.mks.myrealmsample.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.secure.mks.myrealmsample.R;
import com.secure.mks.myrealmsample.model.SampleItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.SampleViewHolder> {


    private Context context;
    private LayoutInflater inflater;
    private SampleClickManager clickManager;
    private List<SampleItem> sampleItemList;

    public SampleAdapter(Context context, SampleClickManager clickManager) {
        this.context = context;
        this.clickManager = clickManager;
        sampleItemList = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<SampleItem> itemList) {
        if (itemList == null) {
            return;
        }
        sampleItemList.clear();
        sampleItemList.addAll(itemList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SampleViewHolder(inflater.inflate(R.layout.item_db_list, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SampleViewHolder holder, int position) {
        SampleItem item = sampleItemList.get(position);
        holder.setDataView(item, position);

    }

    @Override
    public int getItemCount() {
        return sampleItemList.size();
    }

    public interface SampleClickManager {
        void onItemClickManager(SampleItem item, int position);
    }

    public class SampleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_mobile)
        TextView tvMobile;

        public SampleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }


        public void setDataView(SampleItem item, int position) {
            tvMobile.setText(item.getPhone());
            tvName.setText(item.getName());

        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            if (pos < 0) {
                return;
            }

            if (clickManager != null) {
                clickManager.onItemClickManager(sampleItemList.get(pos), pos);
            }
        }
    }
}
