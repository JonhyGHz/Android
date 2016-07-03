package com.example.jonathanhz.archivos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private ImageView image;
    private EditText texto;
    private Button boton_guardar;
    private Button boton_eliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.image_input);
        texto = (EditText) findViewById(R.id.texto);
        boton_guardar = (Button) findViewById(R.id.boton_guardar);
        boton_eliminar = (Button) findViewById(R.id.boton_eliminar);

        cargarFoto();
        cargarTexto();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //Creamos una carpeta en la memeria del terminal
                File imagesFolder = new File(
                        Environment.getExternalStorageDirectory(), "JZH");
                imagesFolder.mkdirs();
                //añadimos el nombre de la imagen
                File image_file = new File(imagesFolder, "foto.jpg");
                Uri uriSavedImage = Uri.fromFile(image_file);
                //Le decimos al Intent que queremos grabar la imagen
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                //Lanzamos la aplicacion de la camara con retorno (forResult)
                startActivityForResult(intent, 1);
            }
        });

        boton_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String texto_string = texto.getText().toString();
                try
                {
                    //FileOutputStream file_out = openFileOutput("texto.txt",MODE_PRIVATE);
                    OutputStreamWriter osw = new OutputStreamWriter(openFileOutput("texto.txt",MODE_PRIVATE));
                    //Se escribe el texto en el archivo
                    osw.write(texto_string);
                    osw.flush();
                    osw.close();
                    //Mensaje
                    Toast.makeText(getApplicationContext(),"Guardado",Toast.LENGTH_LONG).show();
                }catch(Exception e)
                {
                    Log.e("JZH - Error guardar: ",e.getMessage());
                }
            }
        });

        boton_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                texto.setText("");
                image.setImageResource(R.drawable.loggin_chiquito);
                try
                {
                    deleteFile("texto.txt");
                    File file = new File(Environment.getExternalStorageDirectory()+
                            "/JZH/"+"foto.jpg");
                    file.delete();
                }catch(Exception e)
                {
                    Log.e("JZH - Error Eliminar: ",e.getMessage());
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {
            //Creamos un bitmap con la imagen recientemente
            //almacenada en la memoria
            Bitmap bMap = BitmapFactory.decodeFile(
                    Environment.getExternalStorageDirectory()+
                            "/JZH/"+"foto.jpg");
            //Añadimos el bitmap al imageView para
            //mostrarlo por pantalla
            image.setImageBitmap(bMap);
        }
    }

    private void cargarTexto()
    {
       try
       {
           BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("texto.txt")));
           String linea = br.readLine();
           String texto_string = "";
           while(linea != null)
           {
               texto_string += linea + "\n";
               linea = br.readLine();
           }
           br.close();
           texto.setText(texto_string);

       }catch(Exception e)
       {
           Log.e("JZH-Error cargarArchivos: ",e.getMessage());
           texto.setText("");
       }
    }

    private void cargarFoto()
    {
        File file = new File(Environment.getExternalStorageDirectory()+
                    "/JZH/"+"foto.jpg");
        if(file.exists())
        {
            Bitmap bMap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+ "/JZH/"+"foto.jpg");
            //Añadimos el bitmap al imageView para
            //mostrarlo por pantalla
            image.setImageBitmap(bMap);
        }else{
            image.setImageResource(R.drawable.loggin_chiquito);
        }
    }
}
