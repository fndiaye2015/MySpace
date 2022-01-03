package com.example.myspace;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class Inscription extends AppCompatActivity {

    MaterialEditText userName, email, password, mobile;
    RadioGroup radioGroup;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        userName = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mobile = findViewById(R.id.mobile);
        radioGroup = findViewById(R.id.radiogp);
        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String txtUserName = userName.getText().toString();
               String txtEmail = email.getText().toString();
               String txtPassword = password.getText().toString();
               String txtMobile = mobile.getText().toString();
               if(TextUtils.isEmpty(txtUserName) || TextUtils.isEmpty(txtEmail)
                    || TextUtils.isEmpty(txtPassword) || TextUtils.isEmpty(txtMobile)) {
                   //Snackbar.make(view,"Tous les champs sont requis",Snackbar.LENGTH_LONG).show();
                   Toast.makeText(Inscription.this, "Tous les champs sont requis", Toast.LENGTH_SHORT).show();
               }
               else {
                   int genderId = radioGroup.getCheckedRadioButtonId();
                   RadioButton selected_Gender = radioGroup.findViewById(genderId);
                   if(selected_Gender == null) {
                       //Snackbar.make(view,"Selectionner le sexe !",Snackbar.LENGTH_LONG).show();
                       Toast.makeText(Inscription.this, "Selectionner le sexe !", Toast.LENGTH_SHORT).show();
                   }
                   else {
                       String selectGender = selected_Gender.getText().toString();
                       registerNewAccount(txtUserName,txtEmail,txtPassword,txtMobile,selectGender);
                   }
               }
            }
        });
    }

    private void registerNewAccount(String username, String email, String password, String mobile, String gender) {
        final ProgressDialog progressDialog = new ProgressDialog(Inscription.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Inscrire un nouveau compte");
        progressDialog.show();
        String uRl = "https://myspace-fode.000webhostapp.com/inscription.php"; //url_online
        //String uRl = "http://10.0.2.2/myspace_api/inscription.php"; //url_test_local

        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Successfully Registered")) {
                    progressDialog.dismiss();
                    //Snackbar.make(response,Inscription.this,Snackbar.LENGTH_LONG).show();
                    Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Inscription.this, MainActivity.class));
                    finish();
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("info","Erreur");
                progressDialog.dismiss();
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("username",username);
                param.put("email",email);
                param.put("psw",password);
                param.put("mobile",mobile);
                param.put("gender",gender);
                return param;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(Inscription.this).addToRequestQueue(request);
    }
}