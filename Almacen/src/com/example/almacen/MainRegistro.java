package com.example.almacen;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Button;

public class MainRegistro extends Activity implements OnClickListener
{
	private TextView tvVista;
	private Button btnNuevo;
	private Button btnEliminar;
	private Button btnLista;
	private SQLite sqlite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registro);
	
		tvVista = (TextView) findViewById(R.id.tvResultado);
		btnNuevo = (Button) findViewById(R.id.btnNuevo);
		btnEliminar = (Button) findViewById(R.id.btnEliminar);
		btnLista = (Button) findViewById(R.id.btnLista);
		
		btnNuevo.setOnClickListener(this);
		btnEliminar.setOnClickListener(this);
		btnLista.setOnClickListener(this);
		
		tvVista.setTextSize(25);
		//recuperamos el id del producto
		
		Intent i = getIntent();
		Bundle bundle = i.getExtras();
		if(bundle != null)
		{
			
			int id = bundle.getInt("_ID");
			sqlite = new SQLite(this);
			sqlite.abrir();
			
			Cursor cursor = sqlite.getRegistroProducto(id);
			ArrayList<String> reg = sqlite.getFormatListPro(cursor);
			//int p = reg.size() - 1;
			tvVista.setText(reg.get(0));
		}
		
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId())
		{
		case R.id.btnNuevo:
			Intent iMain = new Intent( MainRegistro.this, MainProducto.class );				
			startActivity( iMain );
			break;
		case R.id.btnEliminar:
			//Confirmamos si desea eliminar el producto
			new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.txtBtnEliminar))
			.setMessage(getResources().getString(R.string.txtMessage))
			.setPositiveButton("Si",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					//Obtenemos el id del registro
					int posicionInicial = tvVista.getText().toString().indexOf("[") + 1; 
					int posicionFinal = tvVista.getText().toString().indexOf("]",posicionInicial);
					
					String resultado =  tvVista.getText().toString().substring(posicionInicial, posicionFinal);
					
					if(sqlite.eliminar(Integer.valueOf(resultado)))
					{
						vaciarDatos();
						verRegistros();
					}
				}
			})
			.setNegativeButton("No",null).show();
			
			break;
		case R.id.btnLista:
			verRegistros();
			break;
		}
	}
	
	public void verRegistros()
	{
		Intent iRegs = new Intent(MainRegistro.this,MainRegistros.class );				
						startActivity( iRegs );
	}
	
	public void vaciarDatos()
	{
		tvVista.setText("Regostro Eliminado");
	}
}
