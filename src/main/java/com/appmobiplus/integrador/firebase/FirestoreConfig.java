package com.appmobiplus.integrador.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FirestoreConfig {
    private static final String chavePrivada = "config/plataforma-plus-e5050-firebase-adminsdk-zie5e-90ab2a0373.json";

    public static void initialize() {
        try {
            InputStream serviceAccount = new FileInputStream(chavePrivada);
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Firestore getFirestoreDB() {
        return FirestoreClient.getFirestore();
    }
}
