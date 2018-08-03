package com.secure.mks.myrealmsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.secure.mks.myrealmsample.app.AppConstants;
import com.secure.mks.myrealmsample.app.MyApplication;
import com.secure.mks.myrealmsample.model.SampleItem;
import com.secure.mks.myrealmsample.realm.SampleMapper;
import com.secure.mks.myrealmsample.service.SampleService;
import com.secure.mks.myrealmsample.ui.SampleDBDataActivity;
import com.secure.mks.myrealmsample.ui.fcm.FcmDataBaseActivity;
import com.secure.mks.myrealmsample.ui.fcmlist.FcmListValueActivity;
import com.secure.mks.myrealmsample.utills.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.txt_phone)
    TextView txtPhone;
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.bt_save)
    Button btSave;
    @BindView(R.id.btn_local)
    Button btnLocal;
    @BindView(R.id.btn_fcm)
    Button btnFcm;
    @BindView(R.id.btn_fcm_list)
    Button btnFcmList;


    private List<SampleItem> sampleItemList = new ArrayList<>();
    private DeviceUtils deviceUtils;


    DatabaseReference databaseReference;
    private static final int REQUEST_READ_RUNTIME_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        deviceUtils = MyApplication.getMyApplication().getDeviceUtils();

        if (deviceUtils.isNetworkAvailable(this)) {
//            Toast.makeText(this, "No internet connetion", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, SampleService.class);
            startService(intent);
        } else {
            Toast.makeText(this, "No internet connetion", Toast.LENGTH_SHORT).show();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference(AppConstants.LOGIN);

    }

    @OnClick({R.id.bt_save, R.id.btn_local, R.id.btn_fcm, R.id.btn_fcm_list})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_save:
                String name = edtName.getText().toString();
                String phone = edtPhone.getText().toString();

                if (name == null || name.equals("") || name.length() < 4) {
                    Toast.makeText(this, "Enter valid name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phone == null || phone.equals("") || phone.length() < 4) {
                    Toast.makeText(this, "Enter valid phone", Toast.LENGTH_SHORT).show();
                    return;
                }

                sampleItemList.clear();
                sampleItemList.add(new SampleItem(name, phone));
                SampleItem sampleItem = new SampleItem();
                sampleItem.setName(name);
                sampleItem.setPhone(phone);

                if (deviceUtils.isNetworkAvailable(this)) {
                    String id = databaseReference.push().getKey();
                    databaseReference.child(id).setValue(sampleItem);
                    Intent intent = new Intent(this, SampleService.class);
                    startService(intent);
                } else {
                    SampleMapper.saveData(sampleItemList);
//                    MyApplication.getMyApplication().getErrorDialog(this);
                }

                edtName.setText("");
                edtPhone.setText("");


                break;

            case R.id.btn_local:
                startActivity(new Intent(getApplicationContext(), SampleDBDataActivity.class));
                break;
            case R.id.btn_fcm:
                startActivity(new Intent(getApplicationContext(), FcmDataBaseActivity.class));
                break;
            case R.id.btn_fcm_list:
                startActivity(new Intent(getApplicationContext(), FcmListValueActivity.class));
                break;
        }


    }
}
