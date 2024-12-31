package com.guyi.class25a_ands_3;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    // https://github.com/thiagohernandes/cryptography-asym-sym/blob/master/src/main/java/com/security/cryptograph/service/AesCryptoService.java?source=post_page-----9a3a3396dd3--------------------------------
    // https://github.com/thiagohernandes/cryptography-asym-sym/blob/master/src/test/java/com/security/cryptograph/service/AesCryptoServiceTest.java?source=post_page-----9a3a3396dd3--------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        updateUI();
//        write();

        try {
            String encrypted = encrypt("Guy");
            String decrypted = decrypt(encrypted);
            Log.d("pttt", encrypted);
            Log.d("pttt", decrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        try {
            String encrypted = encrypt("Elon Musk is a prominent entrepreneur and business magnate known for his influential role in several cutting-edge industries. Born on June 28, 1971, in Pretoria, South Africa, Musk co-founded Zip2 and X.com, which later became PayPal, revolutionizing online payments. He is the CEO and lead designer of SpaceX, a company dedicated to reducing space transportation costs and enabling the colonization of Mars. Additionally, Musk serves as the CEO and product architect of Tesla, Inc., driving advancements in electric vehicles and sustainable energy solutions. Beyond these ventures, he has initiated projects like Neuralink, which focuses on developing brain–machine interfaces, and The Boring Company, aimed at improving urban transportation through tunnel construction. Musk's visionary approach and relentless pursuit of innovation have made him a central figure in shaping the future of technology and space exploration.");
            saveToFile(encrypted);
            String fromFile = readFromFile();
            String decrypted = decrypt(fromFile);
            Log.d("pttt", decrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void updateUI() {
        boolean isPro = MSPS.me().getBoolean("IS_PRO", false);
        int coins = MSPS.me().getInt("COINS", 0);
        ((TextView) findViewById(R.id.info)).setText("isPro = " + isPro + "\n" + coins + " coins");
    }

    private void write() {
//        MSPS.me().putString("UUID", "GUY1111");
//        MSPS.me().putBoolean("IS_PRO1", false);
//        MSPS.me().putBoolean("IS_PRO2", false);
        MSPS.me().putInt("COINS1", 4);
        MSPS.me().putInt("COINS2", 4);
    }


    private static final int pswdIterations = 10;
    private static final int keySize = 128;
    private static final String cypherInstance = "AES/CBC/PKCS5Padding";
    private static final String secretKeyInstance = "PBKDF2WithHmacSHA1";
    private static final String key = "sampleKey";
    private static final String AESSalt = "exampleSalt";
    private static final String initializationVector = "8119745113154120";

    private String encrypt(String textToEncrypt) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(getRaw(key, AESSalt), "AES");
        Cipher cipher = Cipher.getInstance(cypherInstance);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(initializationVector.getBytes()));
        byte[] encrypted = cipher.doFinal(textToEncrypt.getBytes());
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    }

    public static String decrypt(String textToDecrypt) throws Exception {
        byte[] encryted_bytes = Base64.decode(textToDecrypt, Base64.DEFAULT);
        SecretKeySpec skeySpec = new SecretKeySpec(getRaw(key, AESSalt), "AES");
        Cipher cipher = Cipher.getInstance(cypherInstance);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(initializationVector.getBytes()));
        byte[] decrypted = cipher.doFinal(encryted_bytes);
        return new String(decrypted, "UTF-8");
    }

    private static byte[] getRaw(String plainText, String salt) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(secretKeyInstance);
            KeySpec spec = new PBEKeySpec(plainText.toCharArray(), salt.getBytes(), pswdIterations, keySize);
            return factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }


    private void saveToFile(String data) throws IOException {
        Path internalDir = getFilesDir().toPath();
        Path filePath = internalDir.resolve("notes");
        Files.write(filePath, data.getBytes(StandardCharsets.UTF_8));
    }

    private String readFromFile() throws IOException {
        Path internalDir = getFilesDir().toPath();
        Path filePath = internalDir.resolve("notes");
        byte[] bytes = Files.readAllBytes(filePath);
        return new String(bytes, StandardCharsets.UTF_8);
    }



}