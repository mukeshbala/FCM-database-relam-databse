package com.secure.mks.myrealmsample.realm;

import android.content.Context;

import com.secure.mks.myrealmsample.app.MyApplication;
import com.secure.mks.myrealmsample.model.SampleItem;
import com.secure.mks.myrealmsample.model.SampleModel;
import com.secure.mks.myrealmsample.ui.SampleDBDataActivity;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class SampleMapper {

    public static void saveData(List<SampleItem> sampleItemList) {
        Realm realm = Realm.getInstance(MyApplication.getMyApplication().getConfig());

        try {

            for (int i = 0; i < sampleItemList.size(); i++) {
                SampleItem sampleItem = sampleItemList.get(i);
                SampleModel model = new SampleModel();
                model.setName(sampleItem.getName());
                model.setPhone(sampleItem.getPhone());

                realm.beginTransaction();
                realm.insertOrUpdate(model);
                realm.commitTransaction();
            }

        } finally {
            realm.close();
        }
    }

    public static List<SampleItem> getSampleItem() {

        Realm realm = Realm.getInstance(MyApplication.getMyApplication().getConfig());
        List<SampleItem> itemList = new ArrayList<>();
        try {
            realm.beginTransaction();
            RealmResults<SampleModel> modelList = realm.where(SampleModel.class).findAll();
            realm.commitTransaction();
            for (int i = 0; i < modelList.size(); i++) {
                SampleModel model = modelList.get(i);
                SampleItem sampleItem = new SampleItem();
                sampleItem.setName(model.getName());
                sampleItem.setPhone(model.getPhone());
                itemList.add(sampleItem);
            }
            return itemList;
        } finally {
            realm.close();
        }
    }
}
