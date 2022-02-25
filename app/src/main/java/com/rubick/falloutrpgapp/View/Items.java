package com.rubick.falloutrpgapp.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rubick.falloutrpgapp.Model.Ammo;
import com.rubick.falloutrpgapp.Model.Atribute;
import com.rubick.falloutrpgapp.Model.UserData;
import com.rubick.falloutrpgapp.R;
import com.rubick.falloutrpgapp.Service.PreferenceData;

import java.util.ArrayList;
import java.util.Objects;

public class Items extends AppCompatActivity {

    public UserData userdata;
    private ImageView soundBt;
    private RecyclerView recyclerView;
    private ArrayList<Ammo> DefaultAmmo;
    private int changeValue;

    @Override
    public void onBackPressed() {
    }

    //Swipe screen variables
    private float x1,x2;
    static final int MIN_DISTANCE = 150;

    //TODO: create save and load items methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_items);

        userdata = PreferenceData.LoadUserData(this);
        DefaultAmmo = userdata.getUserAmmo();
        if(DefaultAmmo == null){
            setAmmo();
        }
        setItems();
        InitiateSoundImage();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceData.SaveUserData(getApplicationContext(), userdata);
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
                    Intent status = new Intent(getApplicationContext(), Status.class);
                    startActivity(status);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setAmmo(){
        DefaultAmmo = new ArrayList<>();
        DefaultAmmo.add(new Ammo(".38", 0));
        DefaultAmmo.add(new Ammo("10mm", 0));
        DefaultAmmo.add(new Ammo(".308", 0));
        DefaultAmmo.add(new Ammo("Shotgun Shell", 0));
        DefaultAmmo.add(new Ammo(".45", 0));
        DefaultAmmo.add(new Ammo("5.56mm", 0));
        DefaultAmmo.add(new Ammo("5mm", 0));
        DefaultAmmo.add(new Ammo(".44 Magnum", 0));
        DefaultAmmo.add(new Ammo("Fusion Cell", 0));
        DefaultAmmo.add(new Ammo(".50", 0));
        DefaultAmmo.add(new Ammo("Flame fuel", 0));
        DefaultAmmo.add(new Ammo("Flare", 0));
        DefaultAmmo.add(new Ammo("Railway sp", 0));
        DefaultAmmo.add(new Ammo("Gamma rnd", 0));
        DefaultAmmo.add(new Ammo("Syringer", 0));
        DefaultAmmo.add(new Ammo("Plasma Cte", 0));
        userdata.setUserAmmo(DefaultAmmo);
    }

    private void InitiateSoundImage(){
        if(userdata.isSoundOn()){
            soundBt.setImageResource(R.drawable.sound_on);
        }else{
            soundBt.setImageResource(R.drawable.sound_off);
        }
    }

    public void setItems(){
        soundBt = findViewById(R.id.soundItemBt);

        recyclerView = findViewById(R.id.recycle);
        Items.Adapter adapter = new Items.Adapter(userdata.getUserAmmo()); //Dentro vai o array de atributos
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
    }

    public class Adapter extends RecyclerView.Adapter<ViewHolder> {
        private ArrayList<Ammo> ammos;

        public Adapter(ArrayList<Ammo> ammos) {
            this.ammos = ammos;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Items.ViewHolder(getLayoutInflater().inflate(R.layout.atributes_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Ammo currentAmmo = ammos.get(position);
            holder.bind(currentAmmo);
        }

        @Override
        public int getItemCount() {
            return ammos.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Ammo ammo) {
            TextView name = itemView.findViewById(R.id.atributeName);
            TextView value = itemView.findViewById(R.id.attributeValue);
            ImageView sub = itemView.findViewById(R.id.subBt);
            ImageView add = itemView.findViewById(R.id.addBt);

            name.setText(ammo.getName());


            value.setText(decimalNumber(ammo.getTotal()));

            value.setOnClickListener(v -> {
                OpenAddPopup(ammo, itemView);
            });

            sub.setOnClickListener(v -> {
                SubSoundFX(name.getText().toString());
                int intValue = Integer.parseInt(String.valueOf(value.getText()));
                if (intValue > 0) {
                    value.setText(decimalNumber(intValue - 1));
                    ammo.setTotal(intValue-1);
                }
            });

            add.setOnClickListener(v -> {
                AddSoundFX(name.getText().toString());
                int intValue = Integer.parseInt(String.valueOf(value.getText()));
                value.setText(decimalNumber(intValue + 1));
                ammo.setTotal(intValue+1);
            });
        }
    }

    private void AddSoundFX(String attributeName) {
        if(userdata.isSoundOn()) {
            //TODO: Set add ammo sound
        }
    }

    private void SubSoundFX(String attributeName) {
        if(userdata.isSoundOn()) {
            //TODO: Set sub ammo sound
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

    public void OpenAddPopup(Ammo ammo, View itemView){
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
            ChangeStatusValue(ammo, itemView);
            dialog.dismiss();
        });

        sub.setOnClickListener(view -> {
            changeValue = Integer.parseInt(String.valueOf(valueToChange.getText())) * -1;
            ChangeStatusValue(ammo, itemView);
            dialog.dismiss();
        });

        cancelTask.setOnClickListener(view -> {
            changeValue = 0;
            dialog.dismiss();
        });

    }

    public void ChangeStatusValue(Ammo ammo, View itemView){
        TextView value = itemView.findViewById(R.id.attributeValue);
        int intValue = Integer.parseInt(String.valueOf(value.getText()));
        if(intValue + changeValue < 0){
            value.setText("00");
            ammo.setTotal(0);
        }else{
            value.setText(decimalNumber(intValue + changeValue));
            ammo.setTotal(intValue + changeValue);
        }
    }

}