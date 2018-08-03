package com.secure.mks.myrealmsample.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.secure.mks.myrealmsample.R;
import com.secure.mks.myrealmsample.model.SampleItem;
import com.secure.mks.myrealmsample.realm.SampleMapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SampleDBDataActivity extends AppCompatActivity implements SampleAdapter.SampleClickManager {


    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.tv_no_record)
    TextView tvNoRecord;

    private List<SampleItem> itemList = new ArrayList<>();
    private SampleAdapter sampleAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acrivity_sample_data);
        ButterKnife.bind(this);

        rvList.setVisibility(View.GONE);
        tvNoRecord.setVisibility(View.VISIBLE);

        rvList.setLayoutManager(new LinearLayoutManager(this));
        sampleAdapter = new SampleAdapter(this, this);
        rvList.setAdapter(sampleAdapter);

        if (SampleMapper.getSampleItem() != null) {
            itemList.addAll(SampleMapper.getSampleItem());
        }

        if (itemList != null && itemList.size() > 0) {

            rvList.setVisibility(View.VISIBLE);
            tvNoRecord.setVisibility(View.GONE);
            sampleAdapter.setList(itemList);


        }

    }

    @Override
    public void onItemClickManager(SampleItem item, int position) {
        Toast.makeText(this, item.getName(), Toast.LENGTH_SHORT).show();
    }
}
