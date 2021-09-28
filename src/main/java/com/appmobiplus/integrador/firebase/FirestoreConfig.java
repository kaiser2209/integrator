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
    //Caminho da chave privada para acesso ao Firestore
    private static final String chavePrivada = "config/plataforma-plus-e5050-firebase-adminsdk-zie5e-90ab2a0373.json";

    public static void initialize() {
        try {
            //Lê o arquivo contendo a chave privada
            InputStream serviceAccount = new FileInputStream(chavePrivada);
            //Define as credenciais usando o arquivo de configuração
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            //Inicializa as opções do Firebase
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();
            //Inicializa o Firebase
            FirebaseApp.initializeApp(options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Retora a referência ao banco de dados Firestore
    public static Firestore getFirestoreDB() {
        return FirestoreClient.getFirestore();
    }
}
