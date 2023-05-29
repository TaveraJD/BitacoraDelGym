package com.taverajd.bitacoradelgym;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

public class PersonalRecordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalrecord);

        loadMuscularGroups();
    }

    public void loadMuscularGroups(){
        // Definir las opciones que se cargarán en el spinner Grupo muscular
        Spinner spMG = findViewById(R.id.spinner_mg);
        String[] optionsMG = {"Seleccione...", "Abdomen", "Antebrazo", "Bícep", "Espalda", "Glúteo",
                "Hombro", "Pantorrilla", "Pecho", "Pierna", "Trapecio", "Trícep"};
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
                        // Lista de ejercicios para Abdomen
                        optionsE = new String[]{"Crunches", "Elevación de piernas", "Plancha", "Plancha lateral"};
                        loadExercises(optionsE, s_MG);
                        break;
                    case 2:
                        // Lista de ejercicios para Antebrazo
                        optionsE = new String[]{"Extensión de muñeca", "Flexión de muñeca"};
                        loadExercises(optionsE, s_MG);
                        break;
                    case 3:
                        // Lista de ejercicios para Bícep
                        optionsE = new String[]{"Curl con agarre neutro", "Curl con agarre prono", "Curl con agarre supino"};
                        loadExercises(optionsE, s_MG);
                        break;
                    case 4:
                        // Lista de ejercicios para Espalda
                        optionsE = new String[]{"Dominadas", "Jalón al pecho", "Remo libre", "Remo en polea", "Pull over"};
                        loadExercises(optionsE, s_MG);
                        break;
                    case 5:
                        // Lista de ejercicios para Glúteo
                        optionsE = new String[]{"Elevación lateral", "Elevación posterior", "Hip thrust"};
                        loadExercises(optionsE, s_MG);
                        break;
                    case 6:
                        // Lista de ejercicios para Hombro
                        optionsE = new String[]{"Elevación frontal", "Elevación lateral", "Elevación posterior", "Press militar"};
                        loadExercises(optionsE, s_MG);
                        break;
                    case 7:
                        // Lista de ejercicios para Pantorrilla
                        optionsE = new String[]{"Elevación de talón de pie", "Elevación de talón sentado"};
                        loadExercises(optionsE, s_MG);
                        break;
                    case 8:
                        // Lista de ejercicios para Pecho
                        optionsE = new String[]{"Aperturas", "Fondos", "Press en declinado", "Press en inclinado", "Press en plano"};
                        loadExercises(optionsE, s_MG);
                        break;
                    case 9:
                        // Lista de ejercicios para Pierna
                        optionsE = new String[]{"Peso muerto", "Prensa", "Sentadilla hacka", "Sentadilla libre", "Sentadilla sumo", "Tijera"};
                        loadExercises(optionsE, s_MG);
                        break;
                    case 10:
                        // Lista de ejercicios para Trapecio
                        optionsE = new String[]{"Encogimientos", "Remo al mentón"};
                        loadExercises(optionsE, s_MG);
                        break;
                    case 11:
                        // Lista de ejercicios para Trícep
                        optionsE = new String[]{"Extensión ascendente", "Extensión descendente", "Extensión frontal", "Fondos"};
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
        EditText et_dbSets = findViewById(R.id.et_dbSets);
        EditText et_dbReps = findViewById(R.id.et_dbReps);

        // Cargar las opciones del spinner Ejercicio según lo seleccionado en el spinner Grupo muscular
        Spinner spE = findViewById(R.id.spinner_e);
        ArrayAdapter<String> adapterE = new ArrayAdapter<>(getApplicationContext(), R.layout.style_spinner, optionsE);
        spE.setAdapter(adapterE);

        spE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s_E = spE.getSelectedItem().toString();
                // Validar y cargar las marcas actuales de la DB
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getApplicationContext(), "bitacoraDB", null, 1);
                SQLiteDatabase db = admin.getReadableDatabase();
                if (!spE.getSelectedItem().equals("")) {
                    Cursor cursor = db.rawQuery("select peso, series, repeticiones from recordsPR where nombreE='"+s_E+"' and nombreGM='"+s_MG+"'", null);
                    if (cursor.moveToFirst()) {
                        // Llenar los campos Peso, Series y Repeticiones (DB)
                        et_dbWeight.setText(cursor.getString(0));
                        et_dbSets.setText(cursor.getString(1));
                        et_dbReps.setText(cursor.getString(2));
                    } else {
                        // Informar que no existen registros sobre el ejercicio seleccionado
                        Toast.makeText(getApplicationContext(), "Aún no hay registro", Toast.LENGTH_SHORT).show();

                        // Limpiar los campos Peso, Series y Repeticiones
                        et_dbWeight.setText("");
                        et_dbSets.setText("");
                        et_dbReps.setText("");
                    }
                }
                db.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void savePR(View v){
        // Referenciar los componentes del activity
        Spinner spMG = findViewById(R.id.spinner_mg);
        Spinner spE = findViewById(R.id.spinner_e);
        EditText et_nwWeight = findViewById(R.id.et_nwWeight);
        EditText et_nwSets = findViewById(R.id.et_nwSets);
        EditText et_nwReps = findViewById(R.id.et_nwReps);

        // Extraer los datos de los componentes
        String s_MG = spMG.getSelectedItem().toString();
        String s_E = spE.getSelectedItem().toString();
        String s_weight = et_nwWeight.getText().toString();
        String s_sets = et_nwSets.getText().toString();
        String s_reps = et_nwReps.getText().toString();

        // Validar los campos Peso, Series y Repeticiones
        if (s_MG.equals("Seleccione...")) {
            Toast.makeText(this, "Seleccione Grupo muscular y Ejercicio", Toast.LENGTH_SHORT).show();
        } else if (s_weight.isEmpty()) {
            et_nwWeight.setError("Campo obligatorio");
        } else if (s_sets.isEmpty()) {
            et_nwSets.setError("Campo obligatorio");
        } else if (s_reps.isEmpty()) {
            et_nwReps.setError("Campo obligatorio");
        } else {
            // Validar si existe o no un registro del ejercicio
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "bitacoraDB", null, 1);
            SQLiteDatabase db = admin.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from recordsPR where nombreE='"+s_E+"' and nombreGM='"+s_MG+"'", null);
            if (cursor.moveToFirst()) {
                // Actualizar las nuevas marcas en la DB local
                admin = new AdminSQLiteOpenHelper(this, "bitacoraDB", null, 1);
                db = admin.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("nombreGM", s_MG);
                cv.put("nombreE", s_E);
                cv.put("peso", s_weight);
                cv.put("series", s_sets);
                cv.put("repeticiones", s_reps);
                db.update("recordsPR", cv, "nombreGM='"+s_MG+"' and nombreE='"+s_E+"'", null);
                db.close();

                // Informar que el registro se realizó correctamente
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                // Limpiar los campos Peso, Series y Repeticiones
                et_nwWeight.setText("");
                et_nwSets.setText("");
                et_nwReps.setText("");
            } else {
                // Insertar las nuevas marcas en la DB local
                admin = new AdminSQLiteOpenHelper(this, "bitacoraDB", null, 1);
                db = admin.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("nombreGM", s_MG);
                cv.put("nombreE", s_E);
                cv.put("peso", s_weight);
                cv.put("series", s_sets);
                cv.put("repeticiones", s_reps);
                db.insert("recordsPR", null, cv);
                db.close();

                // Informar que el registro se realizó correctamente
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                // Limpiar los campos Peso, Series y Repeticiones
                et_nwWeight.setText("");
                et_nwSets.setText("");
                et_nwReps.setText("");
            }
        }
    }

    public void seeRM(View v){
        Intent i = new Intent(this, RepetitionMaximumActivity.class);
        finish();
        startActivity(i);
    }
}