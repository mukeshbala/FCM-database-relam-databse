package com.secure.mks.myrealmsample.ui.fcm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.secure.mks.myrealmsample.R;
import com.secure.mks.myrealmsample.app.MyApplication;
import com.secure.mks.myrealmsample.model.SampleItem;
import com.secure.mks.myrealmsample.ui.SampleAdapter;
import com.secure.mks.myrealmsample.utills.DeviceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FcmDataBaseActivity extends AppCompatActivity implements SampleAdapter.SampleClickManager {

    @BindView(R.id.rv_fcm_db)
    RecyclerView rvFcmDb;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;

    private SampleAdapter sampleAdapter;
    private List<SampleItem> sampleItemList = new ArrayList<>();
    private DatabaseReference databaseReference;

    private DeviceUtils deviceUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acrivity_fcm_db);
        ButterKnife.bind(this);

        deviceUtils = MyApplication.getMyApplication().getDeviceUtils();
        databaseReference = FirebaseDatabase.getInstance().getReference("login");
        rvFcmDb.setVisibility(View.GONE);
        tvNoData.setVisibility(View.VISIBLE);

        rvFcmDb.setLayoutManager(new LinearLayoutManager(this));
        sampleAdapter = new SampleAdapter(this, this);
        rvFcmDb.setAdapter(sampleAdapter);

        if (deviceUtils.isNetworkAvailable(this)) {
            fetData();
        } else {
            MyApplication.getMyApplication().getErrorDialog(this);
        }


    }

    public void fetData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sampleItemList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Map<String, Object> message = (Map<String, Object>) snapshot.getValue();
                    SampleItem sampleItem = new SampleItem((String) message.get("name"), (String) message.get("phone"));
                    sampleItemList.add(sampleItem);
                }

                if (sampleItemList != null && sampleItemList.size() > 0) {
                    sampleAdapter.setList(sampleItemList);
                    rvFcmDb.setVisibility(View.VISIBLE);
                    tvNoData.setVisibility(View.GONE);
                } else {
                    rvFcmDb.setVisibility(View.GONE);
                    tvNoData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                rvFcmDb.setVisibility(View.GONE);
                tvNoData.setVisibility(View.VISIBLE);
                Toast.makeText(FcmDataBaseActivity.this, "Failur", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onItemClickManager(SampleItem item, int position) {
        Toast.makeText(this, item.getName(), Toast.LENGTH_SHORT).show();

    }
}
