package com.appmobiplus.integrador.firebase;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.FirestoreException;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class DocumentReferenceAttributes {
    private DocumentReference documentReference;
    private String idCompany;
    private String idGroup;
    private Map<String, Object> data;

    public DocumentReferenceAttributes(String idCompany, String idGroup) {
        this.idCompany = idCompany;
        this.idGroup = idGroup;
    }

    public DocumentReference getDocumentReference() {
        return documentReference;
    }

    public void setDocumentReference(DocumentReference documentReference) {
        this.documentReference = documentReference;
    }

    public String getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(String idCompany) {
        this.idCompany = idCompany;
    }

    public String getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DocumentReferenceAttributes)) {
            return false;
        }

        DocumentReferenceAttributes other = (DocumentReferenceAttributes) obj;

        return this.idCompany.equals(other.getIdCompany()) && this.idGroup.equals(other.getIdGroup());
    }
}
