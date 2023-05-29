package com.taverajd.bitacoradelgym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RepetitionMaximumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repetitionmaximum);

        loadMuscularGroups();
    }

    public void loadMuscularGroups(){
        // Definir las opciones que se cargarán en el spinner Grupo muscular
        Spinner spMG = findViewById(R.id.spinner_mg);
        String[] optionsMG = {"Seleccione...", "Espalda", "Glúteo", "Hombro", "Pecho", "Pierna"};
        ArrayAdapter<String> adapterMG = new ArrayAdapter<>(this, R.layout.style_spinner, optionsMG);
        spMG.setAdapter(adapterMG);

        // Definir las opciones que se cargarán en el spinner Ejercicio
        spMG.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] optionsE;
                String s_MG = spMG.getSelectedItem().toString();
                switch (position){
                    case 0:
                        // Lista vacía (Por defecto)
                        optionsE = new String[]{""};
                        loadExercises(optionsE, s_MG);
                        break;
                    case 1:
                        // Lista de ejercicios para Espalda
                        optionsE = new String[]{"Dominadas", "Rack pull", "Remo libre"};
                        loadExercises(optionsE, s_MG);
                        break;
                    case 2:
                        // Lista de ejercicios para Glúteo
                        optionsE = new String[]{"Hip thrust"};
                        loadExercises(optionsE, s_MG);
                        break;
                    case 3:
                        // Lista de ejercicios para Hombro
                        optionsE = new String[]{"Press militar"};
                        loadExercises(optionsE, s_MG);
                        break;
                    case 4:
                        // Lista de ejercicios para Pecho
                        optionsE = new String[]{"Fondos", "Press en declinado", "Press en inclinado", "Press en plano"};
                        loadExercises(optionsE, s_MG);
                        break;
                    case 5:
                        // Lista de ejercicios para Pierna
                        optionsE = new String[]{"Peso muerto", "Prensa", "Sentadilla hacka", "Sentadilla libre"};
                        loadExercises(optionsE, s_MG);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void loadExercises(String[] optionsE, String s_MG){
        // Referenciar los componentes del activity
        EditText et_dbWeight = findViewById(R.id.et_dbWeight);

        // Cargar las opciones del spinner Ejercicio según lo seleccionado en el spinner Grupo muscular
        Spinner spE = findViewById(R.id.spinner_e);
        ArrayAdapter<String> adapterE = new ArrayAdapter<>(getApplicationContext(), R.layout.style_spinner, optionsE);
        spE.setAdapter(adapterE);

        spE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s_E = spE.getSelectedItem().toString();
                // Validar y cargar la marca actual de la DB
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getApplicationContext(), "bitacoraDB", null, 1);
                SQLiteDatabase db = admin.getReadableDatabase();
                if (!spE.getSelectedItem().equals("")) {
                    Cursor cursor = db.rawQuery("select peso from recordsRM where nombreE='"+s_E+"' and nombreGM='"+s_MG+"'", null);
                    if (cursor.moveToFirst()) {
                        // Llenar el campo Peso (DB)
                        et_dbWeight.setText(cursor.getString(0));
                    } else {
                        // Informar que no existen registros sobre el ejercicio seleccionado
                        Toast.makeText(getApplicationContext(), "Aún no hay registro", Toast.LENGTH_SHORT).show();

                        // Limpiar el campo Peso
                        et_dbWeight.setText("");
                    }
                }
                db.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void saveRM(View v){
        // Referenciar los componentes del activity
        Spinner spMG = findViewById(R.id.spinner_mg);
        Spinner spE = findViewById(R.id.spinner_e);
        EditText et_nwWeight = findViewById(R.id.et_nwWeight);

        // Extraer los datos de los componentes
        String s_MG = spMG.getSelectedItem().toString();
        String s_E = spE.getSelectedItem().toString();
        String s_weight = et_nwWeight.getText().toString();

        // Validar el campo Peso
        if (s_MG.equals("Seleccione...")) {
            Toast.makeText(this, "Seleccione Grupo muscular y Ejercicio", Toast.LENGTH_SHORT).show();
        } else if (s_weight.isEmpty()) {
            et_nwWeight.setError("Campo obligatorio");
        } else {
            // Validar si existe o no un registro del ejercicio
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "bitacoraDB", null, 1);
            SQLiteDatabase db = admin.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from recordsRM where nombreE='"+s_E+"' and nombreGM='"+s_MG+"'", null);
            if (cursor.moveToFirst()) {
                // Actualizar la nueva marca en la DB local
                admin = new AdminSQLiteOpenHelper(this, "bitacoraDB", null, 1);
                db = admin.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("nombreGM", s_MG);
                cv.put("nombreE", s_E);
                cv.put("peso", s_weight);
                db.update("recordsRM", cv, "nombreGM='"+s_MG+"' and nombreE='"+s_E+"'", null);
                db.close();

                // Informar que el registro se realizó correctamente
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                // Limpiar el campo Peso
                et_nwWeight.setText("");
            } else {
                // Insertar la nueva marca en la DB local
                admin = new AdminSQLiteOpenHelper(this, "bitacoraDB", null, 1);
                db = admin.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("nombreGM", s_MG);
                cv.put("nombreE", s_E);
                cv.put("peso", s_weight);
                db.insert("recordsRM", null, cv);
                db.close();

                // Informar que el registro se realizó correctamente
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                // Limpiar el campo Peso
                et_nwWeight.setText("");
            }
        }
    }

    public void seePR(View v){
        Intent i = new Intent(this, PersonalRecordActivity.class);
        finish();
        startActivity(i);
    }
}