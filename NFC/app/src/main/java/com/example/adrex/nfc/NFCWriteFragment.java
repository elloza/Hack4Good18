package com.example.adrex.nfc;


import android.app.DialogFragment;
import android.content.Context;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.io.IOException;


public class NFCWriteFragment extends DialogFragment {
    public static final String TAG = NFCReadFragment.class.getSimpleName();

    private TextView mTvMessage;
    private ProgressBar mProgress;
    private Listener mListener;

    public NFCWriteFragment() {
        // Required empty public constructor
    }

    public static NFCWriteFragment newInstance() {
        return new NFCWriteFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nfcwrite, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        mTvMessage = view.findViewById(R.id.tv_message);
        mProgress = view.findViewById(R.id.progress);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (NFCMainActivity)context;
        mListener.onDialogDisplayed();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.onDialogDimissed();
    }

    public void onNfcDetected(NfcV ndef, String messageToWrite){

        mProgress.setVisibility(View.VISIBLE);
        writeToNfc(ndef,messageToWrite);
    }

    private void writeToNfc(NfcV ndef, String message){

        mTvMessage.setText(getString(R.string.message_write_progress));
        if (ndef != null) {
            try {
                ndef.connect();
                byte[] cmd = new byte[]{
                        (byte) 0x00, // Flags
                        (byte) 0x21, // Command: Write multiple blocks
                        (byte) 0x03,

                };
                ndef.transceive(cmd); //transceive tanto para leer como para escribir
                ndef.close();
                //Write Successful
                mTvMessage.setText(getString(R.string.message_write_success));
            }catch (IOException e){
                e.printStackTrace();
                mTvMessage.setText("ERROR");
            }

        }
    }

}
