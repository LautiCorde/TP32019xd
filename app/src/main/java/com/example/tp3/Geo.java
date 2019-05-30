package com.example.tp3;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Geo extends AppCompatActivity {

    EditText x;
    EditText y;
    EditText radio;
    Button botongeo;
    ArrayList<String> listaCategorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo);
        botongeo = findViewById(R.id.BotonNombre);

        x = findViewById(R.id.XID);
        y = findViewById(R.id.YID);
        radio = findViewById(R.id.RadioID);


        listaCategorias = new ArrayList<>();
        tareaAsincronica miTarea = new tareaAsincronica();
        miTarea.execute();
    }

    public void BotonGeoPresionado(View v) {

        Bundle paquete = new Bundle();

        paquete.putString("CoordenadasX", x.getText().toString());
        paquete.putString("CoordenadasY", y.getText().toString());
        paquete.putString("Radio", radio.getText().toString());

        String categorias = "";

        for (String s : listaCategorias) {
            categorias += s + ",";
        }

        categorias = categorias.substring(0, categorias.length() - 1);

        paquete.putString("categorias", categorias);

        Intent intent = new Intent(Geo.this, ResultadoGeo.class);
        intent.putExtras(paquete);
        startActivity(intent);
    }

    public void procesarJSONleido(InputStreamReader streamLeido) {
        JsonReader JSONleido = new JsonReader(streamLeido);
        try {
            JSONleido.beginObject();
            while (JSONleido.hasNext()) {
                String nombreElementoActual = JSONleido.nextName();
                if (nombreElementoActual.equals("cantidad_de_categorias")) {
                    int cantidadCategoria = JSONleido.nextInt();
                    //Log.d("LecturaJSON", "La cant de categorias es " + cantidadCategoria);

                } else {
                    JSONleido.beginArray();
                    while (JSONleido.hasNext()) {
                        JSONleido.beginObject();
                        while (JSONleido.hasNext()) {
                            nombreElementoActual = JSONleido.nextName();
                            if (nombreElementoActual.equals("nombre_normalizado")) {
                                String valorElementoActual = JSONleido.nextString();
                                //Log.d("LecturaJSON", "valor" + valorElementoActual);
                                listaCategorias.add(valorElementoActual);
                            } else {
                                JSONleido.skipValue();
                            }
                        }
                        JSONleido.endObject();
                    }
                    JSONleido.endArray();
                }
            }
        } catch (Exception error) {
            Log.d("LecturaJSON", "hubo un error" + error.getMessage());
        }
    }


    class tareaAsincronica extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL miRuta = new URL("http://epok.buenosaires.gob.ar/getCategorias");
                HttpURLConnection miConexion = (HttpURLConnection) miRuta.openConnection();
                Log.d("AccesoApi", "Me Conecto");
                if (miConexion.getResponseCode() == 200) {

                    Log.d("AccesoApi", "Conexion Ok");
                    InputStream cuerpoRespuesta = miConexion.getInputStream();
                    InputStreamReader lectorRespuesta = new InputStreamReader(cuerpoRespuesta, "UTF-8");
                    procesarJSONleido(lectorRespuesta);

                } else {

                    Log.d("AccesoApi", "error");
                }
                miConexion.disconnect();

            } catch (Exception Error) {

                Log.d("AccesoApi", "Hubo un error al conectarse " + Error.getMessage());

            }

            return null;
        }

    }


}
