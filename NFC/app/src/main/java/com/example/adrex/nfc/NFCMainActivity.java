package com.example.adrex.nfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Locale;

public class NFCMainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    public static final String TAG = NFCMainActivity.class.getSimpleName();

    private ImageView mImageView;
    private NfcAdapter mNfcAdapter;
    private TextToSpeech mTts;
    private HashMap<Integer,String> records;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcmain);
        mImageView = findViewById(R.id.logo);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLogo();
            }
        });
        mTts = new TextToSpeech(this, this);
        initNfc();
        initHash();
    }

    private void initNfc(){
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(mNfcAdapter == null || !mNfcAdapter.isEnabled()){
            Snackbar.make(findViewById(R.id.main_layout),R.string.message_nfc_disabled, Snackbar.LENGTH_LONG).show();
            textToSpeechProduct("NFC desactivado");
        }
    }

    private void onClickLogo(){
        textToSpeechProduct("¡Leroy for all!");
    }

    private void initHash(){
        records = new HashMap<>();
        records.put(100,"Martillo, 17.2 euros");
        records.put(-20,"Mesa, 200 euros");
        records.put(37,"Alicate, 10 euros");
        records.put(-108,"Mesa de noche, 90 euros");
        records.put(-60,"Bañera, 450 euros");
        records.put(0,"Un bloste, que ¿qué es un bloste?");
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected,tagDetected,ndefDetected};

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if(mNfcAdapter!= null)
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mNfcAdapter!= null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        if(tag != null){
            int uid = tag.getId()[0];
            Log.e("TAG", ""+uid);
            if(records.get(uid)!= null){
                textToSpeechProduct(records.get(uid));
            }else{
                textToSpeechProduct(records.get(0));
            }
        }
    }

    private void textToSpeechProduct(String productName){
        mTts.speak(productName, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Locale loc = new Locale ("spa", "ESP");
            int result = mTts.setLanguage(loc);

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("404","Language is not available.");
            }
        } else {
            Log.e("404", "Could not initialize TextToSpeech.");
            Intent installIntent = new Intent();
            installIntent.setAction(
                    TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
            startActivity(installIntent);
        }
    }
}
