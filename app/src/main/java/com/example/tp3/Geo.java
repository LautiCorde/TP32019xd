package com.example.tp3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Geo extends AppCompatActivity {

    EditText x;
    EditText y;
    EditText radio;
    Button botongeo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo);
        botongeo = findViewById(R.id.BotonNombre);

        x = findViewById(R.id.XID);
        y = findViewById(R.id.YID);
        radio = findViewById(R.id.RadioID);

    }

    public void BotonGeoPresionado(View v) {

        Bundle paquete = new Bundle();

        paquete.putString("CoordenadasX", x.getText().toString());
        paquete.putString("CoordenadasY", y.getText().toString());
        paquete.putString("Radio", radio.getText().toString());

        Intent intent = new Intent(Geo.this, ResultadoGeo.class);
        intent.putExtras(paquete);
        startActivity(intent);

    }
}
