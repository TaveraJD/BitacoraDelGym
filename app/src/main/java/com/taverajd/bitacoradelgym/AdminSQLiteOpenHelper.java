package com.taverajd.bitacoradelgym;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    public AdminSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear la tabla de las Marcas Personales PR
        db.execSQL("create table recordsPR(nombreGM text, nombreE text, peso text, series text, repeticiones text)");

        // Crear la tabla de las Marcas Máximas RM
        db.execSQL("create table recordsRM(nombreGM text, nombreE text, peso text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
