package com.example.emaildot;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static java.lang.Math.pow;

public class MainActivity extends AppCompatActivity{

    String servidorSeleccionado;
    String[] valores = {"gmail.com","outlok.com","yahoo.com","hotmail.com","personalizado"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setContentView(R.layout.activity_main);


        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valores));
        spinner.setSelection(0);

        EditText myTextBox = (EditText) findViewById(R.id.editText);
        EditText personalizado = (EditText) findViewById(R.id.personalizadoText);
        personalizado.setVisibility(View.GONE);
        EditText visualizador = (EditText) findViewById(R.id.viewText);
        visualizador.setSingleLine(false);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                servidorSeleccionado = valores[position];
                if(servidorSeleccionado == "personalizado"){
                    personalizado.setVisibility(View.VISIBLE);
                } else {
                    personalizado.setVisibility(View.GONE);
                    personalizado.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        myTextBox.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { }
            public void beforeTextChanged(CharSequence s, int start,int count, int after) { }
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView myOutputBox = (TextView) findViewById(R.id.editText);
                // myOutputBox.setText(s);
                List<String> resultado = emailDots(s.toString());
                //Toast.makeText(MainActivity.this, resultado.toString(),Toast.LENGTH_LONG).show();

                String correo = spinner.getSelectedItem().toString();
                if(servidorSeleccionado == "personalizado"){
                    correo =  personalizado.getText().toString();
                }

                String listString = s.toString() + "@" + correo + "\n";
                Log.i("mostrar",resultado.toString());

                for (int i = 1; i < resultado.size(); ++i)  {
                    listString += resultado.get(i) + "@" + correo + "\n";
                }
                visualizador.setText(listString);

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected ArrayList<String> emailDots(String email){

        //Toast.makeText(MainActivity.this, texto,Toast.LENGTH_LONG).show();

        if (email.length() == 1) {
            // caso base 1 string de longitud 1,  se devuelve una sola combinación
            ArrayList<String> add = new ArrayList<String>();
            add.add(email);
            Log.i("info",add.toString());
            return add;
        }
        else {
            //  caso recursivo se hace con todas las combinaciones de substring
            ArrayList<String> result = new ArrayList<String>();
            // el mismo valor de email es una combinación válida
            result.add(email);

            for (int i = 1; i < email.length(); ++i) {

                // cabeza string
                String first = email.substring(0,i);
                // resto del string
                String rest = email.substring(i);
                //llamada recursiva para las combinaciones del resto
                List emailDotsRest = emailDots(rest);

                // para cada elemento de la llamada recursiva,
                // se agrega al resultado la concatenación de la primera parte + PUNTO + resto
                emailDotsRest.forEach(e -> result.add(first + "." + e));

            }
            Log.i("infobucle",result.toString());
            return result;

        }


    }
}