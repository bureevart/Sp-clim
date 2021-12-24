package com.example.my_map;

import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CityDialogFragment extends DialogFragment {

    private TextView cityName;
    private TextView param2;
    private TextView param3;
    private TextView param4;
    private TextView param5;
    private TextView param6;
    private TextView param7;
    private TextView param8;
    private TextView param9;
    private TextView param10;
    private TextView param11;
    private TextView param12;
    private TextView param13;
    private TextView param14;
    private TextView param15;
    private TextView param16;
    private TextView param17;
    private TextView param18;
    private TextView param19;
    private TextView param20;

    private SQLiteDatabase database;

    private String cityNameStr;
    Button returnToMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        Window dialogWindow = dialog.getWindow();

        dialogWindow.setGravity(Gravity.CENTER);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        dialogWindow
                .setLayout(size.x - (int)getResources().getDimension(R.dimen.margin_x),
                        size.y - (int)getResources().getDimension(R.dimen.margin_y));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View dialogLayout = inflater.inflate(R.layout.dialog_city_info, container, false);

        cityName = (TextView) dialogLayout.findViewById(R.id.city_name);
        param2 = (TextView) dialogLayout.findViewById(R.id.param2);
        param3 = (TextView) dialogLayout.findViewById(R.id.param3);
        param4 = (TextView) dialogLayout.findViewById(R.id.param4);
        param5 = (TextView) dialogLayout.findViewById(R.id.param5);
        param6 = (TextView) dialogLayout.findViewById(R.id.param6);
        param7 = (TextView) dialogLayout.findViewById(R.id.param7);
        param8 = (TextView) dialogLayout.findViewById(R.id.param8);
        param9 = (TextView) dialogLayout.findViewById(R.id.param9);
        param10 = (TextView) dialogLayout.findViewById(R.id.param10);
        param11 = (TextView) dialogLayout.findViewById(R.id.param11);
        param12 = (TextView) dialogLayout.findViewById(R.id.param12);
        param13 = (TextView) dialogLayout.findViewById(R.id.param13);
        param14 = (TextView) dialogLayout.findViewById(R.id.param14);
        param15 = (TextView) dialogLayout.findViewById(R.id.param15);
        param16 = (TextView) dialogLayout.findViewById(R.id.param16);
        param17 = (TextView) dialogLayout.findViewById(R.id.param17);
        param18 = (TextView) dialogLayout.findViewById(R.id.param18);
        param19 = (TextView) dialogLayout.findViewById(R.id.param19);
        param20 = (TextView) dialogLayout.findViewById(R.id.param20);
        returnToMap = (Button)dialogLayout.findViewById(R.id.return_to_map);
        returnToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Cursor cursor = database.rawQuery("SELECT Field2, Field3, Field4, Field5, Field6, Field7, Field8, Field9, Field10, Field11, Field12, Field13, Field14, Field15, Field16, Field17, Field18, Field19, Field20 FROM Cities WHERE city_name = '" + cityNameStr +  "' ", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            param2.setText(cursor.getString(0));
            param3.setText(cursor.getString(1));
            param4.setText(cursor.getString(2));
            param5.setText(cursor.getString(3));
            param6.setText(cursor.getString(4));
            param7.setText(cursor.getString(5));
            param8.setText(cursor.getString(6));
            param9.setText(cursor.getString(7));
            param10.setText(cursor.getString(8));
            param11.setText(cursor.getString(9));
            param12.setText(cursor.getString(10));
            param13.setText(cursor.getString(11));
            param14.setText(cursor.getString(12));
            param15.setText(cursor.getString(13));
            param16.setText(cursor.getString(14));
            param17.setText(cursor.getString(15));
            param18.setText(cursor.getString(16));
            param19.setText(cursor.getString(17));
            param20.setText(cursor.getString(18));
            cursor.moveToNext();
        }
        cursor.close();

        cityName.setText(cityNameStr);

        return dialogLayout;
    }


    public void setCityName(String cityName) {
        cityNameStr = cityName;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

}
