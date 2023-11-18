package com.example.lab7_iot.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.lab7_iot.R;
import com.example.lab7_iot.databinding.ActivityMainBinding;
import com.example.lab7_iot.entity.Usuario;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseFirestore db;
    Usuario usuarioRegistrado;

    Boolean registrado = false;
    private final static String TAG = "msg-test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        binding.button2.setOnClickListener(view -> {
            String emailIngresado = ((TextInputEditText) binding.inputEmail.getEditText()).getText().toString();
            String contrasenaIngresada = ((TextInputEditText) binding.inputPassword.getEditText()).getText().toString();
            Log.d(TAG, "asfasf");

            db.collection("gestoresDeSalonDeBelleza")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for(QueryDocumentSnapshot u : task.getResult()){
                                Usuario user = u.toObject(Usuario.class);
                                if(user.getEmail().equals(emailIngresado) && user.getPassword().equals(contrasenaIngresada)){
                                    usuarioRegistrado = user;
                                    registrado = true;
                                    Log.d(TAG, "usuario encontrado");
                                    break;
                                }
                            }
                        }
                    });
            if(registrado){
                Intent intent = new Intent(this, GestorDeSalonActivity.class);
                intent.putExtra("email", emailIngresado);
                startActivity(intent);
            }else{
                Log.d(TAG, "se crea un nuevo cliente");

                Intent intent = AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.drawable.pucp)
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.EmailBuilder().build()
                        ))
                        .setIsSmartLockEnabled(false)
                        .build();
                signInLauncher.launch(intent);

            }
        });
    }
    ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    if (auth.getCurrentUser() != null) {
                        String email = auth.getCurrentUser().getEmail();
                        String nombreApellido = auth.getCurrentUser().getDisplayName();

                        Usuario user = new Usuario();
                        user.setEmail(email);
                        user.setRol("Cliente");
                        user.setNombreApellido(nombreApellido);

                        db.collection("clientes")
                                .document("email")
                                .set(user)
                                .addOnSuccessListener(unused -> {
                                    Log.d("msg-test","Data guardada exitosamente");
                                })
                                .addOnFailureListener(e -> e.printStackTrace());

                        Intent intent = new Intent(MainActivity.this, GestorDeSalonActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);



                    } else {
                        Log.d(TAG, "User null");
                    }
                } else {
                    Log.d(TAG, "Canceló el Log-in");
                }
            }
    );
}