package com.secure.mks.myrealmsample.ui.fcmlist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
import butterknife.OnClick;

public class FcmListValueActivity extends AppCompatActivity implements SampleAdapter.SampleClickManager {

    @BindView(R.id.button)
    Button button;
    @BindView(R.id.rv_fcm_list)
    RecyclerView rvFcmList;

    DatabaseReference databaseReference;
    @BindView(R.id.button2)
    Button button2;
    @BindView(R.id.tv_sample)
    TextView tvSample;

    private SampleAdapter sampleAdapter;
    private List<SampleItem> sampleItemList = new ArrayList<>();
    private DeviceUtils deviceUtils;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcm_list_value);
        ButterKnife.bind(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        button2.setVisibility(View.GONE);
        tvSample.setVisibility(View.VISIBLE);
        rvFcmList.setVisibility(View.GONE);

        rvFcmList.setLayoutManager(new LinearLayoutManager(this));
        sampleAdapter = new SampleAdapter(this, this);
        rvFcmList.setAdapter(sampleAdapter);

        deviceUtils = MyApplication.getMyApplication().getDeviceUtils();

        if (deviceUtils.isNetworkAvailable(this)) {
            fetchList();
        } else {
            MyApplication.getMyApplication().getErrorDialog(this);
        }


    }


    public void getListValue() {
        List<SampleItem> sampleItemList = new ArrayList<>();
        sampleItemList.add(new SampleItem("MS", "1234567890"));
        sampleItemList.add(new SampleItem("MK", "9874561230"));
        sampleItemList.add(new SampleItem("MP", "0147820596"));
        sampleItemList.add(new SampleItem("m?", "3698521475"));

        databaseReference.child("user").setValue(sampleItemList);


    }

    @OnClick({R.id.button, R.id.button2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button:
                fetchList();
                break;
            case R.id.button2:
                getListValue();
                break;
        }
    }

    private void fetchList() {


        Query myTopPostsQuery = databaseReference.child("user").orderByChild("name");

        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> message = (Map<String, Object>) snapshot.getValue();
                    SampleItem sampleItem = new SampleItem((String) message.get("name"), (String) message.get("phone"));

                    sampleItemList.add(sampleItem);
                }

                if (sampleItemList != null && sampleItemList.size() > 0) {
                    sampleAdapter.setList(sampleItemList);
                    rvFcmList.setVisibility(View.VISIBLE);
                    tvSample.setVisibility(View.GONE);
                } else {
                    rvFcmList.setVisibility(View.GONE);
                    tvSample.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        tvSample.setVisibility(View.VISIBLE);
        rvFcmList.setVisibility(View.GONE);
    }


    @Override
    public void onItemClickManager(SampleItem item, int position) {

    }
}
