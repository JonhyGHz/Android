package com.mehuljoisar.lockscreen;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mehuljoisar.lockscreen.utils.LockscreenService;
import com.mehuljoisar.lockscreen.utils.LockscreenUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class LockScreenActivity extends Activity implements
		LockscreenUtils.OnLockStatusChangedListener {

	// User-interface
	private Button btnUnlock;
	private EditText pass;
	// Member variables
	private LockscreenUtils mLockscreenUtils;

	//Variable auxiliar
	private int cont;
	//variable para sonido
	public MediaPlayer mp;
	//Variables para asiganar los parametros de configuracion
	private String pin;
	private String telefono;
	private String correo;

	// Set appropriate flags to make the screen appear over the keyguard


	@Override
	public void onAttachedToWindow() {

		super.onAttachedToWindow();
		this.getWindow().setType(LayoutParams.TYPE_KEYGUARD_DIALOG);

	}

	private boolean configuracion(){
		File archivo;

		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("config.txt")));
			/*archivo = new File("config.txt");
			if(archivo.exists())
			{
				return true;
			}
			Log.e("JZH","no existe archivo de configuracion");
			return false;*/
			Log.e("JZH","existe archivo de configuracion");
			return true;

		}catch(Exception e)
		{
			Log.e("JZH","config: "+e.getMessage());
			return false;
		}
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lockscreen);
		if(configuracion())
		{
			Log.e("JZH","onCreate");
			init();

			// unlock screen in case of app get killed by system
			if (getIntent() != null && getIntent().hasExtra("kill")
					&& getIntent().getExtras().getInt("kill") == 1) {
				enableKeyguard();
				unlockHomeButton();
			} else {

				try {
					// disable keyguard
					disableKeyguard();

					// lock home button
					lockHomeButton();

					// start service for observing intents
					startService(new Intent(this, LockscreenService.class));

					// listen the events get fired during the call
					StateListener phoneStateListener = new StateListener();
					TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					telephonyManager.listen(phoneStateListener,
							PhoneStateListener.LISTEN_CALL_STATE);

				} catch (Exception e) {
				}

			}
		}else{

			btnUnlock = (Button) findViewById(R.id.btn_entrar);
			btnUnlock.setText("Comenzar");

			//ponemos en invisible los demas componentes
			Button btn1 = (Button) findViewById(R.id.btn_uno);
			Button btn2 = (Button) findViewById(R.id.btn_dos);
			Button btn3 = (Button) findViewById(R.id.btn_tres);
			Button btn4 = (Button) findViewById(R.id.btn_cuatro);
			Button btn5 = (Button) findViewById(R.id.btn_cinco);
			Button btn6 = (Button) findViewById(R.id.btn_seis);
			Button btn7 = (Button) findViewById(R.id.btn_siete);
			Button btn8 = (Button) findViewById(R.id.btn_ocho);
			Button btn9 = (Button) findViewById(R.id.btn_nueve);
			Button btn0 = (Button) findViewById(R.id.btn_cero);
			Button btnBorrar = (Button) findViewById(R.id.btn_borrar);
			EditText etPass = (EditText) findViewById(R.id.editText);
			btn1.setVisibility(View.GONE);
			btn2.setVisibility(View.GONE);
			btn3.setVisibility(View.GONE);
			btn4.setVisibility(View.GONE);
			btn5.setVisibility(View.GONE);
			btn6.setVisibility(View.GONE);
			btn7.setVisibility(View.GONE);
			btn8.setVisibility(View.GONE);
			btn9.setVisibility(View.GONE);
			btn0.setVisibility(View.GONE);

			btnBorrar.setVisibility(View.GONE);
			etPass.setVisibility(View.GONE);
			btnUnlock.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intentConfig = new Intent(getApplicationContext(),ConfigActivity.class);

					startActivity(intentConfig);
				}
			});
		}

	}

	private void init() {
		mLockscreenUtils = new LockscreenUtils();
		btnUnlock = (Button) findViewById(R.id.btn_entrar);
		pass = (EditText) findViewById(R.id.editText);
		cont = 0;
		leerArchivo();
		btnUnlock.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// unlock home button and then screen on button press
				String texto = pass.getText().toString();
				Log.e("JZH","evento del boton");

				if(texto.equals(pin))
				{
					unlockHomeButton();

				}else if(cont == 4){
					//Mandar SMS con geoposicionamiento
					cont = 0;
					enviarEmail();
					enviarSMS();
					reproducirAlarma();

				}else{
					pass.setText("");
					pass.setHint("Contraseña Incorrecta");
					Toast.makeText(getApplicationContext(),"Contraseña incorrecta: ",Toast.LENGTH_LONG).show();
					cont++;
				}
			}
		});
	}

	private void reproducirAlarma() {
		mp = MediaPlayer.create(this, R.raw.alarm);
		mp.start();
	}

	private void stopAlarma() {
		mp.stop();
	}

	public void asignar(View view) {
		String aux;
		Button boton = (Button) view;
		aux = pass.getText().toString();
		pass.setText(aux + boton.getText());

	}

	public void borrar(View view) {
		pass.setText("");
	}
	// Handle events of calls and unlock screen if necessary
	private class StateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				unlockHomeButton();
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				break;
			}
		}
	};

	// Don't finish Activity on Back press
	@Override
	public void onBackPressed() {
		return;
	}

	// Handle button clicks
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {

		if(keyCode == KeyEvent.KEYCODE_HOME)
		{
			Log.e("Home Button","Clicked");
		}
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			finish();
		}
		return false;
	}

	// handle the key press events here itself
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP
				|| (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)
				|| (event.getKeyCode() == KeyEvent.KEYCODE_POWER)) {
			return false;
		}
		if ((event.getKeyCode() == KeyEvent.KEYCODE_HOME)) {

			return true;
		}
		return false;
	}

	// Lock home button
	public void lockHomeButton() {
		mLockscreenUtils.lock(LockScreenActivity.this);
	}

	// Unlock home button and wait for its callback
	public void unlockHomeButton() {
		try{
			Log.e("JZH","unlockHomeButton");
			mLockscreenUtils.unlock();
		}catch(Exception e)
		{
			Log.e("JZH","unlockHomeButton excepcion");
		}


	}

	// Simply unlock device when home button is successfully unlocked
	@Override
	public void onLockStatusChanged(boolean isLocked) {
		try
		{
			if (!isLocked) {
				unlockDevice();
			}
		}catch(Exception e)
		{
			Log.e("JZH","excepcion");
		}

	}

	@Override
	protected void onStop() {
		super.onStop();
		unlockHomeButton();
	}

	@SuppressWarnings("deprecation")
	private void disableKeyguard() {
		Log.e("JZH","teclado deshabilitado");
		KeyguardManager mKM = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		KeyguardManager.KeyguardLock mKL = mKM.newKeyguardLock("IN");
		mKL.disableKeyguard();
	}

	@SuppressWarnings("deprecation")
	private void enableKeyguard() {
		Log.e("JZH","teclado habilitado");
		KeyguardManager mKM = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		KeyguardManager.KeyguardLock mKL = mKM.newKeyguardLock("IN");
		mKL.reenableKeyguard();
	}
	
	//Simply unlock device by finishing the activity
	private void unlockDevice()
	{
		finish();
	}

	public String mi_ubicacion()
	{

		Log.e("EZH","MainActivity - mi_ubicacion 1");
		// si esta desactivado el GPS del celular
		// instancia de la clase ServicioGPS
		ServiceGPS servicioGPS = new ServiceGPS(this);
		Log.e("EZH","MainActivity - mi_ubicacion 2");
		Log.e("EZH","UBICACION: "+servicioGPS.get_ubicacion());
		Log.e("EZH","MainActivity - mi_ubicacion 3");
		return servicioGPS.get_ubicacion();
	}

	private void leerArchivo()
	{
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("config.txt")));
			String linea = br.readLine();
			br.close();
			//Separamos el contacto
			StringTokenizer st  = new StringTokenizer(linea, ",");
			pin = st.nextToken();
			String nombre = st.nextToken();
			telefono = st.nextToken();
			correo = st.nextToken();

			Log.e("JZH",pin);
			Log.e("JZH",telefono);
			Log.e("JZH",correo);

		}catch(Exception e)
		{
			Log.e("JZH- cargarArchivos:",e.getMessage());

		}
	}
	String ubicacion;
	private void enviarSMS()
	{
		String mensaje = "";
		String num_telefono = "7451038646";
		ServicioGPS gps = new ServicioGPS(getApplicationContext());

		if(gps.isGPSEnabled || gps.isNetworkEnabled)
		{
			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			Log.e("EZH","Latitude: "+latitude);
			Log.e("EZH","Longitude: "+longitude);

			ubicacion = "http://maps.google.com/?ll="+latitude+","+longitude;
			Log.e("EZH",ubicacion);
			mensaje = "Se a tratado de desbloquear tu telefono mas de 5 veces. "+ubicacion;
		}else{
			ubicacion = "";
			mensaje = "Se a tratado de desbloquear tu telefono mas de 5 veces!";
		}

		// obtener le numero de telefono del archivo
		// mandar msj SMS
		Log.e("EZH",num_telefono+"*");
		Log.e("EZH",mensaje+"**");
		sendSMS(telefono,mensaje);
	}
	private void sendSMS(String numeroTelefono, String mensaje)
	{
		Log.e("EZH","sendSMS 1");
		SmsManager sms = SmsManager.getDefault();
		Log.e("EZH","sendSMS 2");
		sms.sendTextMessage(numeroTelefono,null,mensaje,null,null);
		Log.e("EZH","sendSMS 3");
	}

	public void llamar()
	{
		String num_telefono = "7451038646";

		// realizar llamada
		try
		{
			Log.e("EZH","Llamaar - 1: "+num_telefono+"*");
			Uri numero = Uri.parse("tel:"+num_telefono);
			Log.e("EZH","Llamaar - 2");
			Intent intent = new Intent(Intent.ACTION_CALL,numero);
			startActivityForResult(intent,0);
		}catch (ActivityNotFoundException a)
		{
			Log.e("EZH","No se pudo realizar la llamada");
		}
	}

	public void enviarEmail()
	{
		try{
			EmailSender emailSender = new EmailSender();
			emailSender.execute("jonhyTherz@gmail.com","Jonhy1394Therz*","jonathantherz@outlook.es","LockScreenApp Dice","Se a tratado de desbloquear tu telefono mas de 5 veces. "+ubicacion);

		}catch(Exception e){
			Log.e("JZH","Error: "+e.getMessage());
		}

	}

}