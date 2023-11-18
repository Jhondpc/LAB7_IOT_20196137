package com.example.lab7_iot.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.lab7_iot.R;
import com.example.lab7_iot.databinding.ActivityGestorDeSalonBinding;
import com.example.lab7_iot.entity.Usuario;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class GestorDeSalonActivity extends AppCompatActivity {

    FirebaseFirestore db;
    Usuario usuarioRegistrado;
    ActivityGestorDeSalonBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGestorDeSalonBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

/*
        db.collection("usuariosRegistrados")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for(QueryDocumentSnapshot u : task.getResult()){
                            Usuario user = u.toObject(Usuario.class);
                            if(user.getEmail().equals(email)){
                                usuarioRegistrado = user;
                                break;
                            }
                        }
                    }
                });

 */
    }
}