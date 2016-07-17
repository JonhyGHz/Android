package com.example.almacen;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class MainProducto extends Activity implements OnClickListener
{

	//Variables para fecha
    private int año;
    private int mes;
    private int dia;
    
    //Variables de la activity
    private EditText etNombre;
    private EditText etExistencia;
    private EditText etPrecioC;
    private EditText etPrecioV;
    private EditText etRfc;
    private EditText etIdCompra;
    private TextView tvFecha;
    private Button btnRegistrar;
    private Button btnCerrar;
    private Button btnRegistros;
    private SQLite sqlite;
    
    private DatePickerDialog.OnDateSetListener mDateSetListener =
	        new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					 año = year;
		             mes = monthOfYear;
		             dia = dayOfMonth;		             
		             tvFecha.setText( ((dia<10)?"0"+dia:dia) + "/" + ((mes<10)?"0"+mes:mes) + "/" + año);
				}
	        };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_productos);
		
		//se les da vida a los componentes de la activity
		etNombre = (EditText)findViewById(R.id.etNombre);
		etExistencia = (EditText)findViewById(R.id.etExistencia);
		etPrecioC = (EditText)findViewById(R.id.etPrecioC);
		etPrecioV = (EditText)findViewById(R.id.etPrecioV);
		etRfc = (EditText)findViewById(R.id.etRfc);
		etIdCompra = (EditText)findViewById(R.id.etIdCompra);
		tvFecha = (TextView) findViewById(R.id.etFecha);
		tvFecha.setOnClickListener(this);
		btnRegistrar = (Button)findViewById(R.id.btnRegistrar);
		btnRegistrar.setOnClickListener(this);
		btnCerrar = (Button) findViewById(R.id.btnCerrar);
		btnCerrar.setOnClickListener(this);
		btnRegistros = (Button) findViewById(R.id.btnRegistros);
		btnRegistros.setOnClickListener(this);
		
		
		//Obtiene fecha actual y coloca en el textview
		Calendar c = Calendar.getInstance();
        año = c.get(Calendar.YEAR);
        mes = c.get(Calendar.MONTH);
        dia = c.get(Calendar.DAY_OF_MONTH);
        
      //muestra la fecha de la forma 00/00/0000
        tvFecha.setText( ((dia<10)?"0"+dia:dia) + "/" + ((mes<10)?"0"+mes:mes) + "/" + año);
        
        sqlite = new SQLite(this);
        sqlite.abrir();
		
	}
	
	@SuppressLint("NewApi")
	public void verDatePicker()
	{
		DatePickerDialog d = new DatePickerDialog( this , mDateSetListener, año, mes, dia);
		d.show();
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		
		switch(v.getId())
		{
		case R.id.btnRegistrar:
			if(!etNombre.getText().toString().isEmpty() && !etExistencia.getText().toString().isEmpty() 
					&& !etPrecioC.getText().toString().isEmpty() && !etPrecioV.getText().toString().isEmpty() 
					&& !etRfc.getText().toString().isEmpty() && !etIdCompra.getText().toString().isEmpty() 
					&& !tvFecha.getText().toString().isEmpty()){
				if(sqlite.addRegistroProducto(etNombre.getText().toString(),etExistencia.getText().toString(),etPrecioC.getText().toString(),etPrecioV.getText().toString()
						,etRfc.getText().toString(),etIdCompra.getText().toString(),tvFecha.getText().toString())){
					
					//Obtenemos el id del ultimo registro
					vacionDatos();
					int id = sqlite.getUltimoID();
					Bundle b = new Bundle();
					b.putInt("_ID", id);
					Intent int1 = new Intent(MainProducto.this, MainRegistro.class );
					int1.putExtras(b);
					startActivity(int1);
				}else{
					Toast.makeText(getBaseContext(), "Error: Comprueba datos",Toast.LENGTH_SHORT).show();
				}
				
			}else{
				Toast.makeText(getBaseContext(), "Error: Campos vacios",Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btnRegistros:
			vacionDatos();
			Intent iRegs = new Intent( MainProducto.this, MainRegistros.class );				
			startActivity( iRegs );	
			break;
		case R.id.btnCerrar:
			sqlite.cerrar(); 
			finish();
			break;
		
		case R.id.etFecha:
			verDatePicker();
			break;
		
		}
		
	}
	public void vacionDatos()
	{
		etNombre.setText("");
		etExistencia.setText("");
		etPrecioC.setText("");
		etPrecioV.setText("");
		etRfc.setText("");
		etIdCompra.setText("");
	}
	
}
