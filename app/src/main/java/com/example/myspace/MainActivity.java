package com.example.myspace;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    MaterialEditText email, password;
    Button login, register;
    CheckBox loginState;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginState = findViewById(R.id.checkbox);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Inscription.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtEmail = email.getText().toString();
                String txtPassword = password.getText().toString();
                if (TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)) {
                    Toast.makeText(MainActivity.this, "Tous les champs sont requis", Toast.LENGTH_LONG).show();
                }
                else {
                    login(txtEmail,txtPassword);
                }
            }
        });


        String loginStatus = sharedPreferences.getString(getResources().getString(R.string.prefLoginState),"");
        if (loginStatus.equals("loggedin")){
            startActivity(new Intent(MainActivity.this, Home.class));
        }
    }

    private void login(final String email, final String password) {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Connexion");
        progressDialog.show();
        String uRl = "https://myspace-fode.000webhostapp.com/login.php";
        //String uRl = "http://10.0.2.2/myspace_api/login.php";

        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Connexion réussie !")) {
                    progressDialog.dismiss();
                    Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if(loginState.isChecked()) {
                        editor.putString(getResources().getString(R.string.prefLoginState),"loggedin");
                    }
                    else {
                        editor.putString(getResources().getString(R.string.prefLoginState),"loggedout");
                    }

                    editor.apply();
                    startActivity(new Intent(MainActivity.this, Home.class));
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("email",email);
                param.put("psw",password);
                return param;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(MainActivity.this).addToRequestQueue(request);
    }
}