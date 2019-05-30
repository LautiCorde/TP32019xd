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

public class ResultadoNombre extends AppCompatActivity {
    ArrayList<String> listarta = new ArrayList();
    ListView miListadeResultados;
    ArrayAdapter miAdaptador;
    String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultados_nombres);
        Bundle paquete = this.getIntent().getExtras();
        nombre = paquete.getString("Nombre");
        miAdaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listarta);
        miListadeResultados = findViewById(R.id.miListadeResultados);
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
                                Log.d("LecturaJson", "valor " + valorElementoActual);
                                Log.d("LecturaJSON", "SE ACERCA AL IF");
                                if (valorElementoActual.contains(nombre)) {
                                    Log.d("LecturaJSON", "ENTRO AL IF");
                                    listarta.add(valorElementoActual);
                                    Log.d("LecturaJSON", "Se agrego a la lista");
                                }
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
                URL miRuta = new URL("http://epok.buenosaires.gob.ar/buscar/?texto=" + nombre);
                Log.d("Julian", "El nombre de la cat es " + nombre);
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
            miListadeResultados.setAdapter(miAdaptador);
        }
    }
}


