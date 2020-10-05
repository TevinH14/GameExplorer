package com.example.gameexplorer.fragment.settingFragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gameexplorer.R;
import com.example.gameexplorer.firebaseHelper.FirebaseStorageHelper;
import com.example.gameexplorer.firebaseHelper.RealTimeDatabaseHelper;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsFragment extends Fragment implements AbsListView.OnItemClickListener,
        FirebaseStorageHelper.UserImageFinisher, RealTimeDatabaseHelper.NameFinished  {
   private  String[] mTitles = new String[] {"Change Profile Image","Delete Account", "Delete All Games In Favorites"};

    public static SettingsFragment newInstance() {

        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getView() != null && getContext() != null){
            new FirebaseStorageHelper(this);
            FirebaseStorageHelper.downloadImage();

            new RealTimeDatabaseHelper(this);
            RealTimeDatabaseHelper.loadName();

            ListView listView = getView().findViewById(R.id.lv_settingList);
            listView.setOnItemClickListener(this);

            ArrayAdapter adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_list_item_1,android.R.id.text1,mTitles);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onGotImage(Bitmap bitmap) {
        if(getView() != null) {
            CircleImageView civ = getView().findViewById(R.id.iv_profileImage_s);
            if (bitmap != null) {
                civ.setImageBitmap(bitmap);
            }
            else {
                civ.setImageResource(R.drawable.person_placeholder);
            }
        }
    }

    @Override
    public void OnNamePost(String _name) {
        if(getView() != null){
            TextView textView = getView().findViewById(R.id.tv_name_s);
            if (_name != null){
                textView.setText(_name);
            }
            else {
                textView.setText(R.string.user);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
