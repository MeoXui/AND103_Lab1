package fpoly.huynkph38086.and103_lab1;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Services {

    private final FirebaseAuth auth;
    private final Activity activity;
    private FirebaseUser user;
    private final FirebaseFirestore db;
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String verificationID, otp;

    public Services(Activity activity) {
        this.activity = activity;
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential c) {
                otp = c.getSmsCode();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s,
                                   @NonNull PhoneAuthProvider.ForceResendingToken t) {
                verificationID = s;
            }
        };
        db = FirebaseFirestore.getInstance();
    }

    public FirebaseUser getUser() {
        return user;
    }

    public void signin(String email, String password, Class next){
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                user = auth.getCurrentUser();
                Toast.makeText(activity, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                activity.startActivity(new Intent(activity, next));
            } else {
                Log.w(TAG,"signInWithEmail:failure", task.getException());
                Toast.makeText(activity, "Đăng nhập thật bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void signup(String email, String password){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                user = auth.getCurrentUser();
                Toast.makeText(activity, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                activity.finish();
            } else {
                Log.w(TAG,"createUserWithEmail:failure", task.getException());
                Toast.makeText(activity, "Đăng ký thật bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendforget(String email){
        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                Toast.makeText(activity, "Vui lòng kiếm tra hộp thư", Toast.LENGTH_SHORT).show();
            else Toast.makeText(activity, "Gửi thật bại", Toast.LENGTH_SHORT).show();
        });
    }

    public String getOTP(String phone){
        PhoneAuthOptions pao = PhoneAuthOptions
                .newBuilder(auth)
                .setPhoneNumber("+84"+phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(pao);
        return otp;
    }

    public void verifyOTP(String code, Class next){
        PhoneAuthCredential pac = PhoneAuthProvider.getCredential(verificationID, code);
        auth.signInWithCredential(pac).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                user = auth.getCurrentUser();
                Toast.makeText(activity, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                activity.startActivity(new Intent(activity, next));
                activity.finish();
            } else Log.w(TAG,"signInWithCredential:failure", task.getException());
        });
    }

    public void signout(){
        auth.signOut();
        activity.finish();
    }

    public void writeSample() {
        CollectionReference cities = db.collection("cities");

        Map<String, Object> data1 = new HashMap<>();
        data1.put("name", "San Francisco");
        data1.put("state", "CA");
        data1.put("country", "USA");
        data1.put("capital", false);
        data1.put("population", 860000);
        data1.put("regions", Arrays.asList("west_coast", "norcal"));
        cities.document("SF").set(data1);

        Map<String, Object> data2 = new HashMap<>();
        data2.put("name", "Los Angeles");
        data2.put("state", "CA");
        data2.put("country", "USA");
        data2.put("capital", false);
        data2.put("population", 3900000);
        data2.put("regions", Arrays.asList("west_coast", "socal"));
        cities.document("LA").set(data2);

        Map<String, Object> data3 = new HashMap<>();
        data3.put("name", "Washington D.C.");
        data3.put("state", null);
        data3.put("country", "USA");
        data3.put("capital", true);
        data3.put("population", 680000);
        data3.put("regions", Arrays.asList("east_coast"));
        cities.document("DC").set(data3);

        Map<String, Object> data4 = new HashMap<>();
        data4.put("name", "Tokyo");
        data4.put("state", null);
        data4.put("country", "Japan");
        data4.put("capital", true);
        data4.put("population", 9000000);
        data4.put("regions", Arrays.asList("kanto", "honshu"));
        cities.document("TOK").set(data4);

        Map<String, Object> data5 = new HashMap<>();
        data5.put("name", "Beijing");
        data5.put("state", null);
        data5.put("country", "China");
        data5.put("capital", true);
        data5.put("population", 21500000);
        data5.put("regions", Arrays.asList("jingjinji", "hebei"));
        cities.document("BJ").set(data5);
    }

    public void writeData(String name, String state, String country, String capital,
                          int population, String... regions){
        CollectionReference cities = db.collection("cities");

        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("state", state);
        data.put("country", country);
        data.put("capital", capital);
        data.put("population", population);
        data.put("regions", Arrays.asList(regions));
        cities.document("BJ").set(data);
    }

    public void readData(String TAG) {
        DocumentReference docRef = db.collection("cities").document("SF");
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }
}
