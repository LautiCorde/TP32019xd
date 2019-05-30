package com.example.tp3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    void CategoriaPresionado(View cat){
        startActivity(new Intent(MainActivity.this, Categoria.class));
    }
    void NombrePresionado(View nom){

        startActivity(new Intent(MainActivity.this, Nombre.class));
    }
    void GeoPresionado(View geo){
        startActivity(new Intent(MainActivity.this, Geo.class));
    }
}
