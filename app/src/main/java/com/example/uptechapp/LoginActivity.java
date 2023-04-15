package com.example.uptechapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginUser";
    private RelativeLayout layoutGoogle;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private ActivityResultLauncher<IntentSenderRequest> activityResultLauncher;
    private Dialog progressBar;
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {

        layoutGoogle = findViewById(R.id.signGoogle);

        progressBar = new Dialog(this);
        progressBar.setContentView(R.layout.dialog_layout);
        progressBar.setCancelable(false);
        progressBar.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressBar.findViewById(R.id.dialogText);
        dialogText.setText(R.string.progressBarLogging);

        layoutGoogle.setOnClickListener(v -> googleSignIn());

        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {

            Log.i(TAG, result.toString());

            if (result.getResultCode() == RESULT_OK) {

                try {

                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                    String idToken = credential.getGoogleIdToken();

                    if (idToken != null) {
                        String email = credential.getId();
                        Log.i(TAG, "EMAIL - " + email);

                        Log.i(TAG, "DATA " + credential.getPhoneNumber());
                        Log.i(TAG, "DATA-NAME" + credential.getProfilePictureUri() + " " + credential.getGivenName() + " " + credential.getDisplayName());

                        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean(getString(R.string.account_logging), true);
                        editor.apply();

                        Log.i(TAG, "BOOLEAN " + sharedPref.getBoolean(getString(R.string.account_logging), false));

                    } else {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                    }

                } catch (ApiException e) {
                    progressBar.dismiss();

                    Toast.makeText(this, "API: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    progressBar.dismiss();
                    Log.i(TAG, e.getMessage());

                    Toast.makeText(this, "Something went wrong with getting data", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Something went wrong. Try later", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void googleSignIn() {

        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, result -> {
                    try {

                        IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(
                                result.getPendingIntent().getIntentSender()
                        ).build();

                        activityResultLauncher.launch(intentSenderRequest);

                    } catch (Exception e) {
                        Toast.makeText(this, "Exception " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(this, e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}