package com.example.almacen;

/**
 * CLASE HandlerPapeleria SE IMPLEMENTA EL USO DE EL HELPER PARA PODER CREAR LA BASE DE
 * DATOS Y CREAR LAS TABLAS DE LA MISMA
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import static android.provider.BaseColumns._ID;
public class HandlerPapeleria extends SQLiteOpenHelper
{
	private String query = "CREATE TABLE producto ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
			"nombre TEXT, existencia INTEGER, precioCompra DOUBLE, precioVenta DOUBLE, rfc  TEXT,"+
				"id_compra INTEGER, FECHA DATETIME);";
	private String query2 = "CREATE TABLE usuarios ( ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
				"user TEXT, password TEXT);";
	
	private String query3 = "INSERT INTO usuarios (ID,user,password) VALUES (1,'jonathan','jona')";
	private String query4 = "INSERT INTO usuarios (ID,user,password) VALUES (2,'erick','erick')";
	private String query5 = "INSERT INTO usuarios (ID,user,password) VALUES (3,'victor','victor')";
	
	private static final String DATABASE = "Papeleria_Ala";
	
	private static final int VERSION = 1;
	
	public HandlerPapeleria(Context ctx)
	{
		super(ctx,DATABASE,null,VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		
		db.execSQL(query);
		db.execSQL(query2);
		
		db.execSQL(query3);
		db.execSQL(query4);
		db.execSQL(query5);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int version_ant, int version_nueva)
	{
		db.execSQL("DROP TABLE IF EXISTS producto");
		db.execSQL("DROP TABLE IF EXISTS usuarios");
		
		db.execSQL(query);
		db.execSQL(query2);
		
		
	}
	
}