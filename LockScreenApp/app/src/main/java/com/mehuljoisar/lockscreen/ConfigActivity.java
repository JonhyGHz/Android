package com.mehuljoisar.lockscreen;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.widget.TextView;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class ConfigActivity extends Activity {

    private Button botonConfig;
    private Button contacto;
    private EditText etPin;
    private TextView twTelefono;
    private TextView twNombre;
    private TextView twCorreo;

    public static final int PICK_CONTACT_REQUEST = 1 ;
    private Uri contactUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        contacto = (Button) findViewById(R.id.selectionButton);
        botonConfig = (Button)findViewById(R.id.guardar);

        etPin = (EditText) findViewById(R.id.texto);
        twTelefono = (TextView) findViewById(R.id.contactPhone);
        twNombre = (TextView) findViewById(R.id.contactName);
        twCorreo = (TextView) findViewById(R.id.correo);

        cargarTexto();

        contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Crear un intent para seleccionar un contacto del dispositivo
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                /*Iniciar la actividad esperando respuesta a través del canal PICK_CONTACT_REQUEST*/
                startActivityForResult(i, PICK_CONTACT_REQUEST);
            }
        });

        botonConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Guardamos en el fichero de configuracion
                guardarConfiguracion();
            }
        });
    }

    private void renderContact(Uri uri) {
        //Instancias de los componentes del activity_main
        TextView contactName = (TextView)findViewById(R.id.contactName);
        TextView contactPhone = (TextView)findViewById(R.id.contactPhone);

        //Setear componentes
        contactName.setText(getName(uri));
        contactPhone.setText(getPhone(uri));
        //contactPic.setImageBitmap(getPhoto(uri));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                //Capturar el contacto
                contactUri = intent.getData();
                //Procesamiento del contacto
                renderContact(contactUri);
            }
        }
    }

    private String getName(Uri uri) {
        String name = null;
        /*Obtener una instancia del Content Resolver*/
        ContentResolver contentResolver = getContentResolver();
        /*Consultar el nombre del contacto*/
        Cursor c = contentResolver.query(uri, new String[]{Contacts.DISPLAY_NAME},
                null,
                null,
                null);
        //Obtenemos el nombre del contacto
        if(c.moveToFirst()){name = c.getString(0);}
        //Cerramos el cursor
        c.close();
        return name;
    }

    private String getPhone(Uri uri) {
        //Variables temporales para el id y el teléfono
        String id = null;
        String phone = null;
        /************* PRIMERA CONSULTA ************/
        /*Obtener el _ID del contacto*/
        Cursor contactCursor = getContentResolver().query(uri, new String[]{Contacts._ID},
                null,
                null,
                null);
        if (contactCursor.moveToFirst()) {
            id = contactCursor.getString(0);
        }
        contactCursor.close();

        /************* SEGUNDA CONSULTA ************/
        //Sentencia WHERE para especificar que solo deseamos números de telefonía móvil
        String selectionArgs = Phone.CONTACT_ID + " = ? AND " +
                Phone.TYPE+"= " + Phone.TYPE_MOBILE;
        /*
        Obtener el número telefónico
         */
        Cursor phoneCursor = getContentResolver().query(
                Phone.CONTENT_URI,
                new String[] { Phone.NUMBER },
                selectionArgs,
                new String[] { id },
                null
        );
        if (phoneCursor.moveToFirst()) {
            phone = phoneCursor.getString(0);
        }
        phoneCursor.close();

        return phone;
    }

    private Bitmap getPhoto(Uri uri) {
        Bitmap photo = null;
        String id = null;
        /************* CONSULTA ************/
        Cursor contactCursor = getContentResolver().query(
                uri,
                new String[]{ContactsContract.Contacts._ID},
                null,
                null,
                null);

        if (contactCursor.moveToFirst()) {
            id = contactCursor.getString(0);
        }
        contactCursor.close();

        /*
        Usar el método de clase openContactPhotoInputStream()
         */
        try {
            Uri contactUri = ContentUris.withAppendedId(
                    Contacts.CONTENT_URI,
                    Long.parseLong(id));

            InputStream input = Contacts.openContactPhotoInputStream(
                    getContentResolver(),
                    contactUri);

            if (input != null) {
                /*
                Dar formato tipo Bitmap a los bytes del BLOB
                correspondiente a la foto
                 */
                photo = BitmapFactory.decodeStream(input);
                input.close();
            }

        } catch (IOException iox) {
            Log.e("JZH","IOException: "+iox.getMessage());
        }
        return photo;
    }

    private void guardarConfiguracion()
    {
        //Obtenemos la informacion de configuracion
        String pin = etPin.getText().toString();
        String tel = twTelefono.getText().toString();
        String email = twCorreo.getText().toString();
        if(!pin.equals(""))
        {
            if(!tel.equals("Teléfono"))
            {
                if(!email.equals(""))
                {
                    try {

                        //Creamos el archivo de configuracion
                        OutputStreamWriter osw = new OutputStreamWriter(openFileOutput("config.txt",MODE_WORLD_READABLE));
                        //Se escribe el texto en el archivo
                        osw.write(pin+","+getName(contactUri)+","+getPhone(contactUri)+","+email);
                        osw.flush();
                        osw.close();
                        //Mensaje
                        Toast.makeText(getApplicationContext(),"Guardado",Toast.LENGTH_LONG).show();

                    }catch(Exception e)
                    {
                        Log.e("JZH","Exception: "+e.getMessage());
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Debe introduccir un correo",Toast.LENGTH_LONG).show();
                }

            }else{
                Toast.makeText(getApplicationContext(),"Selecione un contacto",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Debe de introducir un pin",Toast.LENGTH_LONG).show();
        }
    }

    private void cargarTexto()
    {

        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("config.txt")));
            String linea = br.readLine();
            br.close();
            //Separamos el contacto
            StringTokenizer st  = new StringTokenizer(linea, ",");
            etPin.setText(st.nextToken());
            twNombre.setText(st.nextToken());
            twTelefono.setText(st.nextToken());
            twCorreo.setText(st.nextToken());

        }catch(Exception e)
        {
            Log.e("JZH- cargarArchivos:",e.getMessage());
            twNombre.setText("Nombre");
            twTelefono.setText("Teléfono");
            etPin.setText("");
            twCorreo.setText("");
        }

    }
}
