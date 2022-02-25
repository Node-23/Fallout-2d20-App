package com.rubick.falloutrpgapp.View;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rubick.falloutrpgapp.Model.Atribute;
import com.rubick.falloutrpgapp.Model.UserData;
import com.rubick.falloutrpgapp.R;
import com.rubick.falloutrpgapp.Service.PreferenceData;

import java.util.ArrayList;
import java.util.Objects;

public class Status extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Atribute> DefaultAttributes;
    private ImageView soundBt;
    public UserData userdata;
    private MediaPlayer startActivity;
    private MediaPlayer addCaps;
    private MediaPlayer subCaps;
    private MediaPlayer addHP;
    private MediaPlayer subHP;
    private MediaPlayer upBtn;
    private MediaPlayer downBtn;
    private MediaPlayer soundBtFX;
    private int changeValue;

    //Swipe screen variables
    private float x1,x2;
    static final int MIN_DISTANCE = 250;

    //TODO: old TV filter on screen
    //TODO: save data when change activity

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_status);

        userdata = PreferenceData.LoadUserData(this);
        if(userdata == null){
            userdata = new UserData();
            setAttributes();
            userdata.setSoundOn(true);
            PreferenceData.SaveUserData(this, userdata);
        }

        setItems();
        InitiateSoundImage();

        //Play start sound FX
        startActivity.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    Intent item = new Intent(getApplicationContext(), Items.class);
                    startActivity(item);
                }
//                else
//                {
//                    // consider as something else - a screen tap for example
//                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceData.SaveUserData(getApplicationContext(), userdata);
    }

    private void InitiateSoundImage(){
        if(userdata.isSoundOn()){
            soundBt.setImageResource(R.drawable.sound_on);
        }else{
            soundBt.setImageResource(R.drawable.sound_off);
        }
    }

    private void setSoundImage(){
        Log.d("TESTE", "SOUND ON: " + userdata.isSoundOn());
        if(userdata.isSoundOn()){
            soundBt.setImageResource(R.drawable.sound_off);
            userdata.setSoundOn(false);
        }else{
            soundBt.setImageResource(R.drawable.sound_on);
            userdata.setSoundOn(true);
        }
    }

    private void AddSoundFX(String attributeName) {
        if(userdata.isSoundOn()) {
            switch (attributeName) {
                case "HP":
                    addHP.start();
                    break;
                case "Caps":
                    addCaps.start();
                    break;
                default:
                    upBtn.start();
                    break;
            }
        }
    }

    private void SubSoundFX(String attributeName) {
        if(userdata.isSoundOn()) {
            switch (attributeName) {
                case "HP":
                    subHP.start();
                    break;
                case "Caps":
                    subCaps.start();
                    break;
                default:
                    downBtn.start();
                    break;
            }
        }
    }

    private void setAttributes() {
        DefaultAttributes = new ArrayList<>();
        DefaultAttributes.add(new Atribute("HP", 0));
        DefaultAttributes.add(new Atribute("Lucky points", 0));
        DefaultAttributes.add(new Atribute("Caps", 0));
        DefaultAttributes.add(new Atribute("XP", 0));
        userdata.setUserAttributes(DefaultAttributes);
    }

    private void setItems() {
        soundBt = findViewById(R.id.soundBt);
        recyclerView = findViewById(R.id.recycle);
        Adapter adapter = new Adapter(userdata.getUserAttributes()); //Dentro vai o array de atributos
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        //sound items
        startActivity = MediaPlayer.create(this, R.raw.start_home_activity);
        addCaps = MediaPlayer.create(this, R.raw.add_caps);
        subCaps = MediaPlayer.create(this, R.raw.sub_caps);
        addHP = MediaPlayer.create(this, R.raw.add_hp);
        subHP = MediaPlayer.create(this, R.raw.sub_hp);
        upBtn = MediaPlayer.create(this, R.raw.btn_1);
        downBtn = MediaPlayer.create(this, R.raw.btn_2);
        soundBtFX = MediaPlayer.create(this, R.raw.get_pistol);

        soundBt.setOnClickListener(v -> {
            setSoundImage();
            soundBtFX.start();
        });
    }

    public class Adapter extends RecyclerView.Adapter<ViewHolder> {
        private ArrayList<Atribute> attributes;

        public Adapter(ArrayList<Atribute> attributes) {
            this.attributes = attributes;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(getLayoutInflater().inflate(R.layout.atributes_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Atribute currentAtribute = attributes.get(position);
            holder.bind(currentAtribute);
        }

        @Override
        public int getItemCount() {
            return attributes.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Atribute atribute) {
            TextView name = itemView.findViewById(R.id.atributeName);
            TextView value = itemView.findViewById(R.id.attributeValue);
            ImageView sub = itemView.findViewById(R.id.subBt);
            ImageView add = itemView.findViewById(R.id.addBt);

            name.setText(atribute.getName());


            value.setText(decimalNumber(atribute.getValue()));

            value.setOnClickListener(v -> {
                OpenAddPopup(atribute, itemView);
            });

            sub.setOnClickListener(v -> {
                SubSoundFX(name.getText().toString());
                int intValue = Integer.parseInt(String.valueOf(value.getText()));
                if (intValue > 0) {
                    value.setText(decimalNumber(intValue - 1));
                    atribute.setValue(intValue-1);
                }
            });

            add.setOnClickListener(v -> {
                AddSoundFX(name.getText().toString());
                int intValue = Integer.parseInt(String.valueOf(value.getText()));
                value.setText(decimalNumber(intValue + 1));
                atribute.setValue(intValue+1);
            });
        }
    }

    private String decimalNumber(int value) {
        String valueTxT;
        if (value < 10) {
            valueTxT = "0" + value;
        } else {
            valueTxT = String.valueOf(value);
        }
        return valueTxT;
    }

    public void OpenAddPopup(Atribute atribute, View itemView){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View addTaskView = getLayoutInflater().inflate(R.layout.change_popup, null);
        EditText valueToChange = (EditText) addTaskView.findViewById(R.id.valueToChange);
        Button add = (Button) addTaskView.findViewById(R.id.addBt);
        Button sub = (Button) addTaskView.findViewById(R.id.subBt);
        Button cancelTask = (Button) addTaskView.findViewById(R.id.cancelBt);

        dialogBuilder.setView(addTaskView);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        add.setOnClickListener(view -> {
            changeValue = Integer.parseInt(String.valueOf(valueToChange.getText()));
            ChangeStatusValue(atribute, itemView);
            dialog.dismiss();
        });

        sub.setOnClickListener(view -> {
            changeValue = Integer.parseInt(String.valueOf(valueToChange.getText())) * -1;
            ChangeStatusValue(atribute, itemView);
            dialog.dismiss();
        });

        cancelTask.setOnClickListener(view -> {
            changeValue = 0;
            dialog.dismiss();
        });

    }

    public void ChangeStatusValue(Atribute atribute, View itemView){
        TextView value = itemView.findViewById(R.id.attributeValue);
        int intValue = Integer.parseInt(String.valueOf(value.getText()));
        if(intValue + changeValue < 0){
            value.setText("00");
            atribute.setValue(0);
        }else{
            value.setText(decimalNumber(intValue + changeValue));
            atribute.setValue(intValue + changeValue);
        }
    }
}