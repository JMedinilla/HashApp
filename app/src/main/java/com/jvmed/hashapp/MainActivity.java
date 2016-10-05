package com.jvmed.hashapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edit_original;
    TextView edit_md5;
    TextView edit_sha1;
    TextView edit_sha2;
    Button btnORI;
    Button btnMD5;
    Button btnSHA1;
    Button btnSHA256;
    ClipboardManager clipboard;
    ClipData clip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializar();

        suscripciones();
    }

    private void suscripciones() {
        btnORI.setOnClickListener(this);
        btnMD5.setOnClickListener(this);
        btnSHA1.setOnClickListener(this);
        btnSHA256.setOnClickListener(this);

        edit_original.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //NONE
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edit_md5.setText(escribirmd5(edit_original.getText().toString()));
                edit_sha1.setText(escribirsha1(edit_original.getText().toString()));
                edit_sha2.setText(escribirsha2(edit_original.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {
                //NONE
            }
        });
    }

    private void enviarMensaje(String cad) {
        Toast.makeText(getApplicationContext(), cad, Toast.LENGTH_SHORT).show();
    }

    private void inicializar() {
        edit_original = (EditText)findViewById(R.id.edit_original);
        edit_md5 = (TextView)findViewById(R.id.edit_md5);
        edit_sha1 = (TextView)findViewById(R.id.edit_sha1);
        edit_sha2 = (TextView)findViewById(R.id.edit_sha2);
        btnORI = (Button)findViewById(R.id.btnORI);
        btnMD5 = (Button)findViewById(R.id.btnMD5);
        btnSHA1 = (Button)findViewById(R.id.btnSHA1);
        btnSHA256 = (Button)findViewById(R.id.btnSHA256);

        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        edit_original.setText("");
        edit_md5.setText(escribirmd5(edit_original.getText().toString()));
        edit_sha1.setText(escribirsha1(edit_original.getText().toString()));
        edit_sha2.setText(escribirsha2(edit_original.getText().toString()));
    }

    private String escribirsha2(String cadena) {
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

    private String escribirsha1(String cadena) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
            digest.update(cadena.getBytes("iso-8859-1"), 0, cadena.length());
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

    private String escribirmd5(String cadena) {
        final String MD5 = "MD5";
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(cadena.getBytes());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnORI:
                clip = ClipData.newPlainText("jv", edit_original.getText().toString());
                clipboard.setPrimaryClip(clip);
                enviarMensaje("Se ha copiado la cadena original al portapapeles");
                break;

            case R.id.btnMD5:
                clip = ClipData.newPlainText("jv", edit_md5.getText().toString());
                clipboard.setPrimaryClip(clip);
                enviarMensaje("Se ha copiado la cadena MD5 al portapapeles");
                break;

            case R.id.btnSHA1:
                clip = ClipData.newPlainText("jv", edit_sha1.getText().toString());
                clipboard.setPrimaryClip(clip);
                enviarMensaje("Se ha copiado la cadena SHA-1 al portapapeles");
                break;

            case R.id.btnSHA256:
                clip = ClipData.newPlainText("jv", edit_sha2.getText().toString());
                clipboard.setPrimaryClip(clip);
                enviarMensaje("Se ha copiado la cadena SHA-256 al portapapeles");
                break;
        }
    }
}
