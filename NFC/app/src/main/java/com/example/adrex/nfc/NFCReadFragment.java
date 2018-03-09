package com.example.adrex.nfc;

import android.app.DialogFragment;
import android.content.Context;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class NFCReadFragment extends DialogFragment {

    public static final String TAG = NFCReadFragment.class.getSimpleName();

    private TextView mTvMessage;
    private Listener mListener;

    public NFCReadFragment() {
        // Required empty public constructor
    }

    public static NFCReadFragment newInstance() {
        return new NFCReadFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nfcread, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        mTvMessage = (TextView) view.findViewById(R.id.tv_message);
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

    public void onNfcDetected(NfcV ndef){
        readFromNFC(ndef);
    }

    private void readFromNFC(NfcV ndef) {

        //TODO
        Toast.makeText(getActivity(),"Read",Toast.LENGTH_LONG).show();
    }
}
