package com.secure.mks.myrealmsample.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.secure.mks.myrealmsample.app.AppConstants;
import com.secure.mks.myrealmsample.app.MyApplication;
import com.secure.mks.myrealmsample.model.SampleItem;
import com.secure.mks.myrealmsample.model.SampleModel;
import com.secure.mks.myrealmsample.realm.SampleMapper;
import com.secure.mks.myrealmsample.utills.DeviceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class SampleService extends Service {


    private DatabaseReference databaseReference;
    private List<SampleItem> sampleDBItemList = new ArrayList<>();
    private List<SampleItem> sampleFCMItemList = new ArrayList<>();
    private DeviceUtils deviceUtils;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        deviceUtils = MyApplication.getMyApplication().getDeviceUtils();

        if (deviceUtils.isNetworkAvailable(this)) {
            databaseReference = FirebaseDatabase.getInstance().getReference(AppConstants.LOGIN);
            fetchFCMDatabase();
            Log.e("sample update", "" + SampleMapper.getSampleItem());
            if (SampleMapper.getSampleItem() != null && SampleMapper.getSampleItem().size() > 0) {

                sampleDBItemList.clear();
                sampleDBItemList.addAll(SampleMapper.getSampleItem());


                boolean isUpdate = true;
                for (int i = 0, size = sampleDBItemList.size(); i < size; i++) {
                    SampleItem sampleItem = sampleDBItemList.get(i);
                    for (int j = 0; j < sampleFCMItemList.size(); j++) {
                        SampleItem sampleItemFCM = sampleFCMItemList.get(i);
                        if (sampleItem.getName().equals(sampleItemFCM.getName())) {
                            isUpdate = false;
                        }
                    }
                    if (isUpdate) {
                        String id = databaseReference.push().getKey();
                        databaseReference.child(id).setValue(sampleItem);
                    }
                }

                Realm realm = Realm.getInstance(MyApplication.getMyApplication().getConfig());
                try {
                    realm.beginTransaction();
                    RealmResults<SampleModel> modelList = realm.where(SampleModel.class).findAll();
                    modelList.deleteAllFromRealm();
                    realm.commitTransaction();

                } finally {
                    realm.close();
                }

            }
        } else {
            Toast.makeText(getApplicationContext(), "No internet connetion", Toast.LENGTH_SHORT).show();
        }


        return START_NOT_STICKY;
    }

    private void fetchFCMDatabase() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                sampleFCMItemList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    SampleItem sampleItem =dataSnapshot.getValue(SampleItem.class);
                    Map<String, Object> message = (Map<String, Object>) snapshot.getValue();
                    SampleItem sampleItem = new SampleItem((String) message.get("name"), (String) message.get("phone"));
                    sampleFCMItemList.add(sampleItem);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Fcm database failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
