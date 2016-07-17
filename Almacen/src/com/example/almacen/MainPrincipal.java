package com.example.almacen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


@SuppressLint("NewApi")
public class MainPrincipal extends Activity implements OnClickListener
{

	private EditText etUsuario;
	private EditText etPassword;
	private Button btnIniciar;
	
	private SQLite sqlite;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_principal);
		etUsuario = (EditText) findViewById(R.id.etUsuario);
		etPassword = (EditText) findViewById(R.id.etPassword);
		btnIniciar = (Button) findViewById(R.id.btnIniciar);
		
		sqlite = new SQLite( this );
		sqlite.abrir();
		
		btnIniciar.setOnClickListener(this);
		
		//et1.setText(papeleria.leerUsuario());
	}
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
		case R.id.btnIniciar:
			if((!etUsuario.getText().toString().isEmpty()) && !etPassword.getText().toString().isEmpty())
			{
				String user = etUsuario.getText().toString();
				String pass = etPassword.getText().toString();
				
				String r = sqlite.leerUsuario(user, pass);
				if(!r.equals("error"))
				{
					Toast t = Toast.makeText(getApplication(),"Bienvenido: "+r,Toast.LENGTH_LONG);
					t.show();
					//papeleria.cerrar();
					vacionDatos();
					Intent int1 = new Intent(MainPrincipal.this, MainProducto.class );
					startActivity(int1);
				}
				else
				{
					Toast t = Toast.makeText(getApplication(),"ERROR: No hay usuarios",Toast.LENGTH_LONG);
					t.show();
				}
				
				
			}
			else
			{
				Toast t = Toast.makeText(getApplication(),"campos vacios",Toast.LENGTH_LONG);
				t.show();
			}
			break;
		
		}
		
	}
	public void vacionDatos()
	{
		etUsuario.setText("");
		etPassword.setText("");
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch(item.getItemId())
    	{
    		case R.id.menu_acerca_de:
    			Toast.makeText(getApplicationContext(),
    					getResources().getString(R.string.texto_menu_acerca_de),
    					Toast.LENGTH_LONG).show();
    			return true;
    		default:
    			return super.onOptionsItemSelected(item);
    	}
    }
	
}
