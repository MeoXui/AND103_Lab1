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

import java.util.concurrent.TimeUnit;

public class Services {

    private final FirebaseAuth auth;
    Activity activity;
    FirebaseUser user;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String verificationID;

    public Services(Activity activity) {
        this.activity = activity;
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
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

    public String Callbacks(){
        final String[] otp = {""};
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential c) {
                        otp[0] = c.getSmsCode();
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
        return otp[0];
    }

    public void getOTP(String phone){
        PhoneAuthOptions pao = PhoneAuthOptions
                .newBuilder(auth)
                .setPhoneNumber("+84"+phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(pao);
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
}
