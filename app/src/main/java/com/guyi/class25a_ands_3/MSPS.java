package com.guyi.class25a_ands_3;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MSPS {

    static MSPS instance;
    private SharedPreferences prefs;

    public static MSPS me() {
        return instance;
    }

    private MSPS(Context context) throws GeneralSecurityException, IOException {
//        prefs = context
//                .getApplicationContext()
//                .getSharedPreferences("preferences_file", Context.MODE_PRIVATE);


        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        prefs = EncryptedSharedPreferences.create(
                context,
                "secret_shared_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    public static MSPS init(Context context) {
        if (instance == null) {
            try {
                instance = new MSPS(context);
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    public void putString(String key, String value) {
        prefs.edit().putString(key, value).apply();
    }

    public String getString(String key, String def) {
        return prefs.getString(key, def);
    }

    public void putBoolean(String key, boolean value) {
        prefs.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean def) {
        return prefs.getBoolean(key, def);
    }

    public void putInt(String key, int value) {
        prefs.edit().putInt(key, value).apply();
    }

    public int getInt(String key, int def) {
        return prefs.getInt(key, def);
    }
    
}
