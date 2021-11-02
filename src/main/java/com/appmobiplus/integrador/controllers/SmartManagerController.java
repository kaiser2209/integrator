package com.appmobiplus.integrador.controllers;

import com.appmobiplus.integrador.firebase.DocumentReferenceAttributes;
import com.appmobiplus.integrador.firebase.FirestoreConfig;
import com.appmobiplus.integrador.utils.WebServiceUtils;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.FirestoreException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.*;

@RestController
public class SmartManagerController {
    //Lista contendo todos listeners configurados
    private static final List<DocumentReferenceAttributes> documents = new ArrayList<>();

    @GetMapping(path = "/smartManager")
    public @ResponseBody Object getSmartManagerData(@RequestParam Map<String, String> params) {
        //Instancia uma nova referência a um documento usando os parâmetros idCompany e idGroup
        DocumentReferenceAttributes newDocument = new DocumentReferenceAttributes(params.get("idCompany"), params.get("idGroup"));
        if(!documents.contains(newDocument)) { //Verifica se não existe uma referência salva com os mesmos parâmetros idCompany e idGroup
            documents.add(newDocument); //Adiciona o nova referência na lista
            newDocument.setDocumentReference(WebServiceUtils.getDocumentReference(newDocument)); //Salva o caminho do banco de dados (Documento) do Firestore na referência criada

        }

        //Retorna os dados recuperados do Firestore para o documento com o idCompany e idGroup passados como parâmetro
        //Object retorno = documents.get(documents.indexOf(newDocument)).getData();
        Object retorno = documents.get(documents.indexOf(newDocument)).getGrupo();

        if (retorno == null) {
            Map<String, String> carregando = new HashMap<>();
            carregando.put("Erro", "Playlist sendo processada...");
            return carregando;
        }

        return retorno;
    }

    //Método que busca os dados salvos no banco de dados (Documento) do Firestore
    private DocumentReference getDocumentReference(DocumentReferenceAttributes document) {
        DocumentReference db = FirestoreConfig.getFirestoreDB().collection("DB")
                .document(document.getIdCompany())
                .collection("Grupos")
                .document(document.getIdGroup());

        System.out.println("Configurando");
        System.out.println("Company: " + document.getIdCompany());
        System.out.println("Group: " + document.getIdGroup());

        db.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirestoreException e) {
                System.out.println("Antes da verificação de erro");
                if (e != null) {
                    return;
                }

                System.out.println("Depois da verificação de erro");
                //System.out.println(documentSnapshot.getData());
                document.setData((Map<String, Object>) documentSnapshot.getData());
                //System.out.println(Arrays.toString(data.toArray()));
            }
        });

        return db;
    }

}
