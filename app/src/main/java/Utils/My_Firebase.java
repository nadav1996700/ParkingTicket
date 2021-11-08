package Utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class My_Firebase {
    private static My_Firebase instance;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storage_reference = storage.getReference();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference database_reference = database.getReference();
    private String account_number;

    private My_Firebase() {
    }

    public static My_Firebase getInstance() {
        return instance;
    }

    public static My_Firebase initHelper() {
        if (instance == null)
            instance = new My_Firebase();
        return instance;
    }

    public DatabaseReference getReference() {
        return database_reference;
    }

    public void setReference(String ref) {
        this.database_reference = database.getReference(ref);
    }

    public StorageReference getStorage_reference() {
        return storage_reference;
    }

    public void setStorage_reference(String ref) {
        this.storage_reference = storage.getReferenceFromUrl(ref);
    }

    public String getAccountNumber() {
        return account_number;
    }

    public void setAccountNumber(String account) {
        this.account_number = account;
    }
}
