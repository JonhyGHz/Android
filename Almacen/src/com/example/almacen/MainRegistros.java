package com.example.almacen;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainRegistros extends Activity implements OnItemClickListener
{

	private ListView listView;
	private ArrayAdapter<String> adaptador ;	
	private SQLite sqlite;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registros);
		
		listView = (ListView) findViewById( R.id.lstRegistros );
		//Abre conexion a sqlite
		sqlite = new SQLite( this );
		sqlite.abrir();
		
		//se muestran todo los registros
		Cursor cursor = sqlite.getRegistrosProductos();
		
		ArrayList<String> lista = sqlite.getFormatListPro(cursor);
		adaptador = new ArrayAdapter<String>( this ,android.R.layout.simple_list_item_1  , lista);
		listView.setAdapter( adaptador );
		listView.setOnItemClickListener( this );
		if( lista.size()== 0 )
		{
			Toast.makeText(getBaseContext(), "No existen registros"  ,Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		Object object = listView.getItemAtPosition( position );
		//Se extrae el ID = [X] 
		int posicionInicial = object.toString().indexOf("[") + 1; 
		int posicionFinal = object.toString().indexOf("]",posicionInicial); 
		String resultado =  object.toString().substring(posicionInicial, posicionFinal);		
		//ejecuta nueva actividad
		
		Bundle b = new Bundle();
		b.putInt("_ID", Integer.valueOf(resultado) );
		Intent iRegs = new Intent(MainRegistros.this, MainRegistro.class );	
		iRegs.putExtras(b);
		startActivity( iRegs );	
	}
	
}
