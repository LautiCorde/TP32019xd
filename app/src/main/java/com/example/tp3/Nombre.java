package com.example.tp3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class Nombre extends AppCompatActivity {

    EditText cat;
    Button boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nombre);
        boton=findViewById(R.id.BotonNombre);

        cat = findViewById(R.id.nombreabuscar);


        }
        public void BusquedaPresionado(View v){

            Bundle paquete = new Bundle();

            paquete.putString("Nombre", cat.getText().toString());

            Intent intent = new Intent(Nombre.this, ResultadoNombre.class);
            intent.putExtras(paquete);
            startActivity(intent);

        }

}
