package com.example.russianrun;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

public class MainActivity extends Activity {

    //Объявим переменные компонентов
    Button button;
    Button buttonChek;
    Button buttonNext;
    EditText editText;
    EditText editText1;
    EditText editText2;
    String solution;
    int currentRecord = 0;
    Drawable drawable;

    //Переменная для работы с БД
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        //Найдем компоненты в XML разметке
        button = (Button) findViewById(R.id.button);
        buttonChek = (Button) findViewById(R.id.button_check);
        buttonNext = (Button) findViewById(R.id.button_next);

        editText  = (EditText) findViewById(R.id.editText);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText.setEnabled(false);
        editText1.setEnabled(false);
        drawable = editText2.getBackground();


        //Пропишем обработчик клика кнопки
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String request = "";
                String task = "";
                String work = "";
                editText2.setBackground(drawable);

                Cursor cursor = mDb.rawQuery("SELECT * FROM tasks", null);
                cursor.moveToFirst();
                request = cursor.getString(1);
                task = cursor.getString(2);
                work = cursor.getString(3);
                solution = cursor.getString(4);
                currentRecord = cursor.getInt(0);
                cursor.close();

                editText.setText(request);
                editText1.setText(task);
                editText2.setText(work);
            }
        });

        buttonChek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editText = editText2.getText().toString().replaceAll(" ", "");
                String solutionText = solution.replaceAll(" ", "");
                if (solutionText.equals(editText)) {
                    editText2.setBackgroundColor(Color.GREEN);
                } else {
                    editText2.setBackgroundColor(Color.YELLOW);
                }
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String request = "";
                String task = "";
                String work = "";
                editText2.setBackground(drawable);

                Cursor cursor = mDb.rawQuery("SELECT * FROM tasks WHERE tasks.id > " +
                        String.valueOf(currentRecord), null);
                cursor.moveToFirst();
                request = cursor.getString(1);
                task = cursor.getString(2);
                work = cursor.getString(3);
                solution = cursor.getString(4);
                currentRecord = cursor.getInt(0);
                cursor.close();

                editText.setText(request);
                editText1.setText(task);
                editText2.setText(work);
            }
        });


    }
}