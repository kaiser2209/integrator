package com.appmobiplus.integrador.firebase;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
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
        InputStream serviceAccount = new FileInputStream("config/integrador-c5039-firebase-adminsdk-j1wws-58a3695ecb.json");
        credentials = GoogleCredentials.fromStream(serviceAccount);
        options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .setProjectId("integrador-c5039")
                .build();

        FirebaseApp.initializeApp(options);

        db = FirestoreClient.getFirestore();

        try {
            save();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            readChanges();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
}
