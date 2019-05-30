package com.example.tp3;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterViewAnimator;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Permission;
import java.util.ArrayList;

public class Categoria extends AppCompatActivity {



    ArrayList listaCategorias;
    ListView miListaDeCategorias;
    ArrayAdapter miAdaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        listaCategorias = new ArrayList<>();
        miListaDeCategorias = findViewById(R.id.ListViewMiLista);
        miListaDeCategorias.setOnItemClickListener(escuhadorPorListView);
        Log.d("AccesoApi", "Conexion Ok");
        miAdaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaCategorias);
        tareaAsincronica miTarea = new tareaAsincronica();
        miTarea.execute();
        Log.d("AccesoApi", "Termine la ejecucion");
    }

     AdapterView.OnItemClickListener escuhadorPorListView=new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int PosicionSeleccionada, long id) {
             Bundle paquete = new Bundle();
             // paquete.putString("PosicionSeleccionada", ""+ PosicionSeleccionada);
             paquete.putString("ElementoSeleccionado", ""+ listaCategorias.get(PosicionSeleccionada));

             Intent actividadBuscar = new Intent(Categoria.this, buscar.class);
             actividadBuscar.putExtras(paquete);

             startActivity(actividadBuscar);
         }
     };

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
                            if (nombreElementoActual.equals("nombre")) {
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
            } } catch(Exception error){
                Log.d("LecturaJSON", "hubo un error" + error.getMessage()); } }





    class tareaAsincronica extends AsyncTask<Void,Void,Void> {
   @Override
        protected Void doInBackground(Void...voids){
        try{
            URL miRuta=new URL("http://epok.buenosaires.gob.ar/getCategorias");
            HttpURLConnection miConexion=(HttpURLConnection) miRuta.openConnection();
            Log.d("AccesoApi","Me Conecto");
            if (miConexion.getResponseCode()==200){

                Log.d("AccesoApi","Conexion Ok");
                InputStream cuerpoRespuesta=miConexion.getInputStream();
                InputStreamReader lectorRespuesta= new InputStreamReader(cuerpoRespuesta, "UTF-8");
                    procesarJSONleido(lectorRespuesta);

            } else {

                Log.d("AccesoApi","error");
            }
            miConexion.disconnect();

        } catch(Exception Error){

            Log.d("AccesoApi","Hubo un error al conectarse "+Error.getMessage());

        }

return null;
   }

        @Override
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
            miListaDeCategorias.setAdapter(miAdaptador);
        }

    }


}

