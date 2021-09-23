package com.appmobiplus.integrador.firebase;

import com.google.api.client.googleapis.GoogleUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleOAuthConstants;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import com.google.protobuf.Api;

import javax.annotation.Nullable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Config {
    GoogleCredentials credentials;
    FirebaseOptions options;
    Firestore db;

    public Config() throws IOException {

        InputStream serviceAccount = new FileInputStream("config/plataforma-plus-e5050-firebase-adminsdk-zie5e-90ab2a0373.json");
        credentials = GoogleCredentials.fromStream(serviceAccount);

        options = FirebaseOptions.builder()
                .setCredentials(credentials)
                //.setProjectId("integrador-c5039")
                .build();

        FirebaseApp.initializeApp(options);

        db = FirestoreClient.getFirestore();

            //save();

            readCollections();
    }

    private void save() throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("teste").document("primeiro");

        Map<String, Object> data = new HashMap<>();

        data.put("nome", "Charles");
        data.put("sobrenome", "Furtado");

        ApiFuture<WriteResult> result = docRef.set(data);

        System.out.println(result.get().getUpdateTime());
    }

    private void read() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = db.collection("teste").get();

        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

        for(QueryDocumentSnapshot document : documents) {
            System.out.println("Nome: " + document.getString("nome"));
            System.out.println("Sobrenome: " + document.getString("sobrenome"));
        }
    }

    private void readChanges() throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("teste").document("primeiro");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed: " + e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    System.out.println("Nome: " + documentSnapshot.getString("nome"));
                    System.out.println("Sobrenome: " + documentSnapshot.getString("sobrenome"));
                } else {
                    System.out.println("Vazio");
                }
            }
        });
    }

    private void readCollections() {
        CollectionReference collection = db.collection("DBgeral");
        collection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirestoreException e) {
                List<QueryDocumentSnapshot> query = queryDocumentSnapshots.getDocuments();
                System.out.println(queryDocumentSnapshots.size());

                for(QueryDocumentSnapshot q : query) {
                    System.out.println(q.getId());
                }
            }
        });
    }
}
