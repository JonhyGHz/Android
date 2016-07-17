package com.example.gatito;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

@SuppressLint("ShowToast")
public class MainActivity extends ActionBarActivity implements OnClickListener
{
	//Variables
	private ImageButton btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9,btnLimpiar;
	private boolean jugador = true,ganador = false;
	private int contador = 0;
	private int [] jugadorGato = new int[9];

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Declaration do one do the Image Button
		btn1 = (ImageButton)findViewById(R.id.btn1);
		btn2 = (ImageButton)findViewById(R.id.btn2);
		btn3 = (ImageButton)findViewById(R.id.btn3);
		btn4 = (ImageButton)findViewById(R.id.btn4);
		btn5 = (ImageButton)findViewById(R.id.btn5);
		btn6 = (ImageButton)findViewById(R.id.btn6);
		btn7 = (ImageButton)findViewById(R.id.btn7);
		btn8 = (ImageButton)findViewById(R.id.btn8);
		btn9 = (ImageButton)findViewById(R.id.btn9);
		btnLimpiar = (ImageButton)findViewById(R.id.limpiar);
		
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		btn5.setOnClickListener(this);
		btn6.setOnClickListener(this);
		btn7.setOnClickListener(this);
		btn8.setOnClickListener(this);
		btn9.setOnClickListener(this);
		btnLimpiar.setOnClickListener(this);
		btnLimpiar.setEnabled(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		return super.onOptionsItemSelected(item);
	}
	// met for playing one
	public void validarJugador(boolean temJugador, ImageButton boton, int posicion)
	{
		if(temJugador == true)
		{
			boton.setImageResource(R.drawable.tacha);
			jugador = false;
			boton.setEnabled(false);
			jugadorGato[posicion] = 1;
		}
		else if(temJugador == false)
		{
			boton.setImageResource(R.drawable.circle);
			jugador = true;
			boton.setEnabled(false);
			jugadorGato[posicion] = 0;
		}
	}
	
	public void ganador()
	{
		ganador = true;
	}
	@SuppressLint("ShowToast")
	public void validarGanador()
	{
		if((contador >= 5 && contador <= 9))
		{
			if(!btn1.isEnabled() && !btn2.isEnabled() && !btn3.isEnabled())
			{
				if((jugadorGato[0] == jugadorGato[1]) && (jugadorGato[0] == jugadorGato[2]))
				{
					mensajeGanador();
					ganador();
				}
			}
			if(!btn1.isEnabled() && !btn5.isEnabled() && !btn9.isEnabled())
			{
				if((jugadorGato[0] == jugadorGato[4]) && (jugadorGato[0] == jugadorGato[8]))
				{
					mensajeGanador();
					ganador();
				}
			}
			if(!btn1.isEnabled() && !btn4.isEnabled() && !btn7.isEnabled())
			{
				if((jugadorGato[0] == jugadorGato[3]) && (jugadorGato[0] == jugadorGato[6]))
				{
					mensajeGanador();
					ganador();
				}
			}
			if(!btn2.isEnabled() && !btn5.isEnabled() && !btn8.isEnabled())
			{
				if((jugadorGato[1] == jugadorGato[4]) && (jugadorGato[1] == jugadorGato[7]))
				{
					mensajeGanador();
					ganador();
				}
			}
			if(!btn3.isEnabled() && !btn5.isEnabled() && !btn7.isEnabled())
			{
				if((jugadorGato[2] == jugadorGato[4]) && (jugadorGato[2] == jugadorGato[6]))
				{
					mensajeGanador();
					ganador();
				}
			}
			if(!btn3.isEnabled() && !btn6.isEnabled() && !btn9.isEnabled())
			{
				if((jugadorGato[2] == jugadorGato[5]) && (jugadorGato[2] == jugadorGato[8]))
				{
					mensajeGanador();
					ganador();
				}
			}
			if(!btn4.isEnabled() && !btn5.isEnabled() && !btn6.isEnabled())
			{
				if((jugadorGato[3] == jugadorGato[4]) && (jugadorGato[3] == jugadorGato[5]))
				{
					mensajeGanador();
					
				}
			}
			if(!btn7.isEnabled() && !btn8.isEnabled() && !btn9.isEnabled())
			{
				if((jugadorGato[6] == jugadorGato[7]) && (jugadorGato[6] == jugadorGato[8]))
				{
					mensajeGanador();
					ganador();
				}
			}
			
		}
		if(contador == 9 && ganador == false)
		{
			AlertDialog.Builder msj = new AlertDialog.Builder(this);
			msj.setMessage(getResources().getString(R.string.text_empate));
			msj.setTitle("Registro Coffe Tec");
			msj.setCancelable(false);
			msj.setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					@SuppressWarnings("unused")
					Intent i = new Intent(Intent.ACTION_MAIN);finish();
				}
				
			});
			msj.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					btnLimpiar.setEnabled(true);
					dialog.cancel();
				}
				
			});
			AlertDialog alert = msj.create();
			alert.show();
		}
	}

	public void mensajeGanador()
	{
		AlertDialog.Builder msj = new AlertDialog.Builder(this);
		msj.setMessage("¿Desea Continuar?");
		msj.setTitle("WINNER");
		msj.setIcon(R.drawable.winner);
		msj.setCancelable(false);
		msj.setNegativeButton("Exit", new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				@SuppressWarnings("unused")
				Intent i = new Intent(Intent.ACTION_MAIN);finish();
			}
			
		});
		msj.setPositiveButton("Continue", new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				btnLimpiar.setEnabled(true);
				dialog.cancel();
			}
			
		});
		AlertDialog alert = msj.create();
		alert.show();
		
	}
	@Override
	public void onClick(View v) 
	{
		//you to play go
		
		switch(v.getId())
		{
			case R.id.btn1:
				validarJugador(jugador,btn1,0);
				contador++;
				//Log.e(""+contador, " "+contador);
				validarGanador();
				break;
			case R.id.btn2:
				validarJugador(jugador,btn2,1);
				contador++;
				//Log.e(""+contador, " "+contador);
				validarGanador();
				break;
			case R.id.btn3:
				validarJugador(jugador,btn3,2);
				contador++;
				//Log.e(""+contador, " "+contador);
				validarGanador();
				break;
			case R.id.btn4:
				validarJugador(jugador,btn4,3);
				contador++;
				//Log.e(""+contador, " "+contador);
				validarGanador();
				break;
			case R.id.btn5:
				validarJugador(jugador,btn5,4);
				contador++;
				//Log.e(""+contador, " "+contador);
				validarGanador();
				break;
			case R.id.btn6:
				validarJugador(jugador,btn6,5);
				contador++;
				//Log.e(""+contador, " "+contador);
				validarGanador();
				break;
			case R.id.btn7:
				validarJugador(jugador,btn7,6);
				contador++;
				//Log.e(""+contador, " "+contador);
				validarGanador();
				break;
			case R.id.btn8:
				validarJugador(jugador,btn8,7);
				contador++;
				//Log.e(""+contador, " "+contador);
				validarGanador();
				break;
			case R.id.btn9:
				validarJugador(jugador,btn9,8);
				contador++;
				//Log.e(""+contador, " "+contador);
				validarGanador();
				break;
			case R.id.limpiar:
				reiniciar(this);
				break;
		}
	}
	
	//metodo para reiniciar los valores a los predefinidos
	public static void reiniciar(Activity actividad)
	{
		Intent in = new Intent();
		in.setClass(actividad,actividad.getClass());
		actividad.startActivity(in);
		actividad.finish();
	}
}
