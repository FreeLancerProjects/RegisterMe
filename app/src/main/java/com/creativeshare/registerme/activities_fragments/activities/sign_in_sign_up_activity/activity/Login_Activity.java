package com.creativeshare.registerme.activities_fragments.activities.sign_in_sign_up_activity.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.creativeshare.registerme.R;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.registerme.activities_fragments.activities.sign_in_sign_up_activity.fragments.Fragment_Language;
import com.creativeshare.registerme.activities_fragments.activities.sign_in_sign_up_activity.fragments.Fragment_Login;
import com.creativeshare.registerme.activities_fragments.activities.sign_in_sign_up_activity.fragments.Fragment_Signup;
import com.creativeshare.registerme.language.Language_Helper;
import com.creativeshare.registerme.models.UserModel;
import com.creativeshare.registerme.preferences.Preferences;
import com.creativeshare.registerme.share.Common;
import com.creativeshare.registerme.tags.Tags;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.jkb.vcedittext.VerificationCodeEditText;

import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;


public class Login_Activity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private Fragment_Login fragmentLogin;
    private Fragment_Signup fragmentSignup;
    private Fragment_Language fragment_language;

    private int fragment_counter = 0;
    private Preferences preferences;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String id;
    private String vercode;
    private FirebaseAuth mAuth;
    private Dialog dialog;
     private VerificationCodeEditText verificationCodeEditText;
    private ProgressDialog dialo;
private UserModel userModel;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private void authn() {

        mAuth= FirebaseAuth.getInstance();
        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
             //  super.onCodeSent(s, forceResendingToken);
                id=s;
                mResendToken=forceResendingToken;
                Log.e("authid",id);
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                Log.e("code",phoneAuthCredential.getSmsCode());
//phoneAuthCredential.getProvider();
                if(phoneAuthCredential.getSmsCode()!=null){
                    verificationCodeEditText.setText(phoneAuthCredential.getSmsCode());
                 siginwithcredental(phoneAuthCredential);}


            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("llll",e.getMessage());
            }


        };

    }
    private void verfiycode(String code) {

        if(id!=null){
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(id,code);
        siginwithcredental(credential);}
    }

    private void siginwithcredental(PhoneAuthCredential credential) {
        dialo = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
         dialo.dismiss();
         dialog.dismiss();
         if(task.isSuccessful()){
             preferences = Preferences.getInstance();
             preferences.create_update_userdata(Login_Activity.this,userModel);
             // activity.NavigateToHomeActivity();
             NavigateToHomeActivity();
         }

            }
        });
    }

    public void sendverficationcode(String phone, String phone_code, UserModel userModel) {
        dialog.show();
        this.userModel=userModel;
        Log.e("kkk",phone_code+phone);
        final Runnable mUpdateResults = new Runnable() {
            public void run() {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(phone_code+phone,10, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,  mCallbacks);
            }
        };
        mUpdateResults.run();

    }
    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(Language_Helper.updateResources(base, Preferences.getInstance().getLanguage(base)));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CreateSignAlertDialog();
        authn();

        preferences = Preferences.getInstance();
        Paper.init(this);

        if (savedInstanceState == null) {
            fragmentManager = this.getSupportFragmentManager();

            if (preferences.isLanguageSelected(this))
            {
                if (preferences.getSession(this).equals(Tags.session_login))
                {
                    NavigateToHomeActivity();
                }else
                    {
                        DisplayFragmentLogin();

                    }

            }else
                {
                   DisplayFragmentLanguage();
                }

        }

        getDataFromIntent();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null && intent.hasExtra("sign_up"))
        {
            boolean isSign_in = intent.getBooleanExtra("sign_up",true);
            if (!isSign_in)
            {
                DisplayFragmentSignUp();

            }
        }
    }


    private void DisplayFragmentLanguage()
    {

        fragment_language = Fragment_Language.newInstance();

        if (fragment_language.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_language).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment_language, "fragment_language").addToBackStack("fragment_language").commit();
        }
    }

    private void DisplayFragmentLogin()
    {

        fragment_counter += 1;
        if (fragmentLogin == null) {
            fragmentLogin = Fragment_Login.newInstance();
        }
        if (fragmentLogin.isAdded()) {
            fragmentManager.beginTransaction().show(fragmentLogin).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentLogin, "fragmentLogin").addToBackStack("fragmentLogin").commit();
        }
    }

    public void DisplayFragmentSignUp()
    {

        fragment_counter += 1;

        if (fragmentSignup == null) {
            fragmentSignup = Fragment_Signup.newInstance();
        }
        if (fragmentSignup.isAdded()) {
            fragmentManager.beginTransaction().show(fragmentSignup).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentSignup, "fragmentSignup").addToBackStack("fragmentSignup").commit();
        }
    }

    public void NavigateToHomeActivity()
    {
        Intent intent = new Intent(Login_Activity.this, Home_Activity.class);
        startActivity(intent);
        finish();
    }



    public void Back() {
        if (fragment_counter == 1) {
            finish();
        } else {
            fragment_counter -= 1;
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        Back();
    }


    public void RefreshActivity(String selected_language) {
        Log.e("lang",selected_language);
        Paper.book().write("lang",selected_language);
        preferences.setIsLanguageSelected(this);
        Language_Helper.setNewLocale(this,selected_language);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
    public void CreateSignAlertDialog() {
        dialog = new Dialog(this, R.style.CustomAlertDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_login);
        LinearLayout ll = dialog.findViewById(R.id.ll);
verificationCodeEditText=dialog.findViewById(R.id.edt_ver);
        ll.setBackgroundResource(R.drawable.custom_bg_login);
        Button confirm=dialog.findViewById(R.id.btn_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
vercode=verificationCodeEditText.getText().toString();
if(TextUtils.isEmpty(vercode)){
    verificationCodeEditText.setError(getResources().getString(R.string.field_req));
}
else {
    Log.e("code",vercode);
    verfiycode(vercode);

}
            }
        });
    }
}
