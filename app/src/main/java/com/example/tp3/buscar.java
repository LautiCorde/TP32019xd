package com.example.tp3;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class buscar extends AppCompatActivity {
    ArrayList<String> listanombre = new ArrayList();
    ListView miListadeNombres;
    ArrayAdapter miAdaptador;
    String cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);
        Bundle datosRecibidos = this.getIntent().getExtras();
        Log.d("LecturaJSON", datosRecibidos.getString("ElementoSeleccionado"));
        cat = datosRecibidos.getString("ElementoSeleccionado");
        miAdaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listanombre);
        miListadeNombres = findViewById(R.id.miListadeNombres);
        new tareaAsincronica().execute();
    }

    public void procesarJSONleido(InputStreamReader streamLeido) {
        JsonReader JSONleido = new JsonReader(streamLeido);
        try {
            JSONleido.beginObject();
            while (JSONleido.hasNext()) {
                String nombreElementoActual = JSONleido.nextName();
                Log.d("LecturaJSON", nombreElementoActual);
                if (nombreElementoActual.equals("instancias")) {
                    JSONleido.beginArray();
                    while (JSONleido.hasNext()) {
                        JSONleido.beginObject();
                        while (JSONleido.hasNext()) {
                            nombreElementoActual = JSONleido.nextName();
                            Log.d("LecturaJSON", nombreElementoActual);
                            if (nombreElementoActual.equals("nombre")) {
                                String valorElementoActual = JSONleido.nextString();
                                Log.d("LecturaJSON", "valor " + valorElementoActual);
                                listanombre.add(valorElementoActual);
                            } else {
                                JSONleido.skipValue();
                            }
                        }
                        JSONleido.endObject();
                    }
                    JSONleido.endArray();
                } else {
                    JSONleido.skipValue();
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
                URL miRuta = new URL("http://epok.buenosaires.gob.ar/buscar/?texto=" + cat);
                Log.d("Julian", "El nombre de la cat es " + cat);
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

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            miListadeNombres.setAdapter(miAdaptador);
        }
    }
}

