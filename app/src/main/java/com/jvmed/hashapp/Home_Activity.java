package com.jvmed.hashapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Home_Activity extends AppCompatActivity {

    private EditText edtOriginal;
    private TextView edtMD5;
    private TextView edtSHA1;
    private TextView edtSHA356;
    private ClipboardManager clipboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initialize();
    }

    private void sendMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void initialize() {
        edtOriginal = (EditText)findViewById(R.id.edtOriginal);
        edtMD5 = (TextView)findViewById(R.id.edtMD5);
        edtSHA1 = (TextView)findViewById(R.id.edtSHA1);
        edtSHA356 = (TextView)findViewById(R.id.edtSHA256);

        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        edtOriginal.setText("");
        edtMD5.setText(convertMD5(edtOriginal.getText().toString()));
        edtSHA1.setText(convertSHA1(edtOriginal.getText().toString()));
        edtSHA356.setText(convertSHA256(edtOriginal.getText().toString()));

        edtOriginal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtMD5.setText(convertMD5(edtOriginal.getText().toString()));
                edtSHA1.setText(convertSHA1(edtOriginal.getText().toString()));
                edtSHA356.setText(convertSHA256(edtOriginal.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });
    }

    private String convertSHA256(String cadena) {
        return bin2hex(getHash(cadena)).toLowerCase();
    }

    private byte[] getHash(String cadena) {
        MessageDigest digest=null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }

        if (digest != null)
            digest.reset();

        if (digest != null) {
            return digest.digest(cadena.getBytes());
        }
        else
            return null;
    }

    private String bin2hex(byte[] data) {
        return String.format("%0" + (data.length * 2) + "X", new BigInteger(1, data));
    }

    private String convertSHA1(String string) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
            digest.update(string.getBytes("iso-8859-1"), 0, string.length());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] sha1hash = new byte[0];
        if (digest != null) {
            sha1hash = digest.digest();
        }
        return convertToHex(sha1hash);
    }

    private String convertToHex(byte[] sha1hash) {
        StringBuilder buf = new StringBuilder();
        for (byte b : sha1hash) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    private String convertMD5(String string) {
        final String MD5 = "MD5";
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(string.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
        return "";
    }

    public void getButtonClick (View view) {
        ClipData clip;
        switch (view.getId()) {
            case R.id.btnOriginal:
                clip = ClipData.newPlainText("jv", edtOriginal.getText().toString());
                clipboard.setPrimaryClip(clip);
                sendMessage(getResources().getString(R.string.original_copied));
                break;

            case R.id.btnMD5:
                clip = ClipData.newPlainText("jv", edtMD5.getText().toString());
                clipboard.setPrimaryClip(clip);
                sendMessage(getResources().getString(R.string.md5_copied));
                break;

            case R.id.btnSHA1:
                clip = ClipData.newPlainText("jv", edtSHA1.getText().toString());
                clipboard.setPrimaryClip(clip);
                sendMessage(getResources().getString(R.string.sha1_copied));
                break;

            case R.id.btnSHA256:
                clip = ClipData.newPlainText("jv", edtSHA356.getText().toString());
                clipboard.setPrimaryClip(clip);
                sendMessage(getResources().getString(R.string.sha256_copied));
                break;
        }
    }
}
