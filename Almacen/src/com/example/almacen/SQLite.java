package com.example.almacen;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import static android.provider.BaseColumns._ID;

public class SQLite
{
	private HandlerPapeleria sqliteHelper;
	private SQLiteDatabase db;
	
	public SQLite(Context context)
	{
		sqliteHelper = new HandlerPapeleria( context );
	}
	
	@SuppressLint("NewApi")
	public void abrir()
	{
		Log.i("SQLite", "Se abre conexion a la base de datos " + sqliteHelper.getDatabaseName());
		db = sqliteHelper.getWritableDatabase(); 
	}
	
	@SuppressLint("NewApi")
	public void cerrar()
	{
		Log.i("SQLite", "Se cierra conexion a la base de datos " + sqliteHelper.getDatabaseName());
		sqliteHelper.close();
	}
	
	
	
	public String leerUsuario(String user, String pass)
	{
		String result = "error";
		String columnas[] = {"ID","user","password"};
		Cursor c = db.query("usuarios",columnas,null,null,null,null,null);
		int iu,ip;
		
		while(c.moveToNext())
		{
			iu = c.getColumnIndex("user");
			ip = c.getColumnIndex("password");
			if(user.equals(c.getString(iu)) && pass.equals(c.getString(ip)))
			{
				result = c.getString(iu);
			}
		}
		return result;
	}
	
	public boolean addRegistroProducto(String nom, String exist, String precioC, String precioV, String rfc, String idCompra, String fecha)
	{
		if(nom.length() > 0){
			
		ContentValues valores = new ContentValues();
		valores.put("nombre",nom);
		valores.put("existencia",exist);
		valores.put("precioCompra",precioC);
		valores.put("precioVenta",precioV);
		valores.put("rfc",rfc);
		valores.put("id_Compra",idCompra);
		valores.put("FECHA",fecha);
		return (db.insert("producto", null, valores) != -1) ?true:false;
		}
		else
		{
			return false;
		}
	}
	
	public int getUltimoID()
	{
		int id = -1;
		Cursor cursor = db.query( "producto", 
				new String[]{_ID},
				null, null, null,null,
				_ID+ " DESC ", "1");
		if( cursor.moveToFirst() )
		{
			do
			{
				id = cursor.getInt(0);
			} while ( cursor.moveToNext() );
		}
		return id;
	}
	
	public Cursor getRegistroProducto(int id){
		return db.query( "producto",				
				new String[]{
				"_ID",
				"nombre",
				"existencia",
				"precioCompra",
			    "precioVenta",
				"rfc",
				"id_Compra",
				"FECHA"},"_ID"+"="+id, null, null, null, null);
	}
	
	public ArrayList<String> getFormatListPro( Cursor cursor )
	{
		ArrayList<String> lista = new ArrayList<String>();
		String item = "";
		if(cursor.moveToFirst())
		{
			do
			{
				item += "ID: [" + cursor.getInt(0) + "]\r\n";
				item += "Nombre: " + cursor.getString(1) + "\r\n";
				item += "Existencia: " + cursor.getString(2) + "\r\n";
				item += "Precio Compra: " + cursor.getString(3) + "\r\n";
				item += "Precio Venta: " + cursor.getString(4) + "\r\n";
				item += "Proveedor: " + cursor.getString(5) + "";
				lista.add( item );
				item="";
			}while(cursor.moveToNext());
		}
		return lista;
	}
	
	public boolean eliminar(int id)
	{
		return  (db.delete("producto", "_ID"+ " = " + id ,  null) > 0) ? true:false;
	}
	
	public Cursor getRegistrosProductos()
	{
		return db.query( "producto",				
				new String[]{
				"_ID",
				"nombre",
				"existencia",
				"precioCompra",
			    "precioVenta",
				"rfc",
				"id_Compra",
				"FECHA"},null, null, null, null, null);
	}
}
