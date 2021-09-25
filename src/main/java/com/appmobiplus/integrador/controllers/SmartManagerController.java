package com.appmobiplus.integrador.controllers;

import com.appmobiplus.integrador.firebase.FirestoreConfig;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.FirestoreException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

@RestController
public class SmartManagerController {
    private DocumentReference db;
    private static ArrayList<Map<String, Object>> data;

    @GetMapping(path = "/smartManager")
    public @ResponseBody Object getSmartManagerData(@RequestParam Map<String, String> params) {

        if (db == null) {
            db = getDocumentReference(params);
        }

        return data;
    }

    private DocumentReference getDocumentReference(Map<String, String> params) {
        DocumentReference db = FirestoreConfig.getFirestoreDB().collection("DB")
                .document(params.get("idCompany"))
                .collection("Grupos")
                .document(params.get("idGroup"));

        System.out.println("Configurando");

        db.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirestoreException e) {
                if (e != null) {
                    return;
                }

                data = (ArrayList<Map<String, Object>>) documentSnapshot.get("data");
            }
        });

        return db;
    }
}
