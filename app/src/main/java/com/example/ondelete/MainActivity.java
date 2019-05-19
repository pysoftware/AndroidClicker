package com.example.ondelete;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DataBase dbHelper;

    private Button buyCake;
    private Button buyNut;
    private Button buyIce;
    private Button button;

    private TextView text;
    private TextView countCakes;
    private TextView countIce;
    private TextView countNut;
    private TextView perSec;

    private int generalCount = 0;
    private int counter = 0;
    private int cakeCounter = 0;
    private int IceCounter = 0;
    private int NutCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DataBase(this);
        /** Needs when open app first time */
        if(dbHelper.getAllData())
            dbHelper.insertData(0,0,0,0,0);

        /** Load first row from main table DB */
        loadAllData();

        /** Get views */
        getDataViews();

        countCakes.setText(cakeCounter+"");
        countNut.setText(NutCounter+"");
        countIce.setText(IceCounter+"");
        perSec.setText(generalCount+"");
        text.setText(counter + "");

        /** Tap button */
        button.setOnClickListener(this);
        /**Buy cake button */
        buyCake.setOnClickListener(this);
        buyNut.setOnClickListener(this);
        buyIce.setOnClickListener(this);

        Thread thread = new Thread(){
            @Override
            public void run(){
                while (!isInterrupted()){
                    try {
                        sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                counter += (cakeCounter + NutCounter + IceCounter);
                                dbHelper.updateData("ScoreCount",counter);
                                text.setText(NumberFormat.getNumberInstance().format(counter));
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

    private void loadAllData() {
        counter = dbHelper.getData("ScoreCount");
        cakeCounter = dbHelper.getData("CakesCount");
        generalCount = dbHelper.getData("GeneralCount");
        IceCounter = dbHelper.getData("IceCreamCount");
        NutCounter = dbHelper.getData("NutCount");
    }

    /** Method to get views */
    private void getDataViews(){
        buyCake = findViewById(R.id.buyCake);
        buyIce = findViewById(R.id.buyCream);
        buyNut = findViewById(R.id.buyNut);

        button = findViewById(R.id.btn);
        text = findViewById(R.id.txt);
        perSec = findViewById(R.id.perSec);

        countCakes = findViewById(R.id.buyCakee);
        countIce = findViewById(R.id.buyIceCream);
        countNut = findViewById(R.id.buyNutt);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.buyCake:
                if (counter - 100 >= 0){
                    counter -= 100;
                    cakeCounter += 1;
                    generalCount += 1;
                    perSec.setText(generalCount+"");
                    dbHelper.updateData("GeneralCount",generalCount);
                    /** Update first row in DB */
                    dbHelper.updateData("CakesCount",cakeCounter);
                    countCakes.setText(cakeCounter+"");
                }
                break;
            case R.id.buyCream:
                if(counter - 500 >= 0){
                    counter -= 500;
                    IceCounter += 2;
                    generalCount += 2;
                    perSec.setText(generalCount+"");
                    dbHelper.updateData("GeneralCount",generalCount);
                    /** Update first row in DB */
                    dbHelper.updateData("IceCreamCount",IceCounter);
                    countIce.setText(IceCounter+"");
                }
                break;
            case R.id.buyNut:
                if(counter - 1000 >= 0) {
                    counter -= 1000;
                    NutCounter += 5;
                    generalCount += 5;
                    perSec.setText(generalCount+"");
                    dbHelper.updateData("GeneralCount",generalCount);
                    /** Update first row in DB */
                    dbHelper.updateData("NutCount", NutCounter);
                    countNut.setText(NutCounter + "");
                }
                break;
            /** Tap button */
            case R.id.btn:
                counter++;
                dbHelper.updateData("ScoreCount",counter);
                text.setText(NumberFormat.getNumberInstance().format(counter));
                break;
        }
    }
}
