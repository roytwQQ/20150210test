package com.example.roy.nfc_test;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // 處理由Android系統送出應用程式處理的intent filter內容
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            // 取得NdefMessage
            NdefMessage[] messages = getNdefMessages(getIntent());
            // 取得實際的內容
            byte[] payload = messages[0].getRecords()[0].getPayload();
            setNoteBody(new String(payload));
            // 往下送出該intent給其他的處理對象
            setIntent(new Intent());
        }

    }


    NdefMessage[] getNdefMessages(Intent intent) {
        // Parse the intent
        NdefMessage[] msgs = null;
        String action = intent.getAction();
        // 識別目前的action為何
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            // 取得parcelabelarrry的資料
            Parcelable[] rawMsgs =
                    intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            // 取出的內容如果不為null，將parcelable轉成ndefmessage
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[] {};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {
                        record
                });
                msgs = new NdefMessage[] {
                        msg
                };
            }
        } else {
            Log.d("Roy", "Unknown intent.");
            finish();
        }
        return msgs;
    }

    private void setNoteBody(String body) {
        textView.setText(body);
    }
}
