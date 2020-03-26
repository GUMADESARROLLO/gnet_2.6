package com.gnet.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.gnet.utils.Constant;
import com.gnet.utils.Tools;
import com.gnet.utils.validation.Rule;
import com.gnet.utils.validation.Validator;
import com.gnet.utils.validation.annotation.Password;
import com.gnet.utils.validation.annotation.Required;
import com.gnet.utils.validation.annotation.TextRule;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.guma.desarrollo.gnet.R;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONObject;

public class Login extends AppCompatActivity implements Validator.ValidationListener{

    private View parent_view;
    String strEmail, strPassword,strId, strNombre,IdCompany,strMessage,strDevice;
    @Required(order = 1)
    TextInputEditText Usuario;;


    @Required(order = 3)
    @Password(order = 4, message = "Enter a Valid Password")
    @TextRule(order = 5, minLength = 5, message = "Enter a Password Correctly")
    TextInputEditText Contrasenna;

    private Validator validator;
    Button btnSingIn;
    MaterialSpinner spinner;
    MyApplication MyApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        parent_view = findViewById(android.R.id.content);
        btnSingIn = findViewById(R.id.btnOn);

        Usuario = findViewById(R.id.txtUsuario);
        Contrasenna = findViewById(R.id.txtContra);
        Tools.setSystemBarColor(this);
        MyApp = MyApplication.getInstance();
        ((View) findViewById(R.id.sign_up)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(parent_view, "Desarrollado por IT", Snackbar.LENGTH_SHORT).show();
            }
        });


        btnSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validator.validateAsync();
                MyApp.saveType("normal");

            }
        });


        spinner = findViewById(R.id.spinner);
        spinner.setItems("UNIMARK", "GUMAPHARMA", "INNOVA");
        validator = new Validator(this);
        validator.setValidationListener(this);


    }
    @Override
    public void onValidationSucceeded() {
        strEmail = Usuario.getText().toString();
        strPassword = Contrasenna.getText().toString();
        IdCompany = spinner.getText().toString();
        strDevice = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();


        if (Tools.isNetworkAvailable(Login.this)) {
            new MyTaskLoginNormal().execute(Constant.NORMAL_LOGIN_URL+"/" + strEmail + "/"  + strPassword+ "/"  + IdCompany+ "/"  + strDevice );
        }
    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();
        if (failedView instanceof TextInputEditText) {
            failedView.requestFocus();
            ((TextInputEditText) failedView).setError(message);

        } else {
            Toast.makeText(this, "Registro No Guardado", Toast.LENGTH_SHORT).show();
        }
    }
    public void setResult() {

        if (Constant.GET_SUCCESS_MSG == 0) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(R.string.whops);
            dialog.setMessage(R.string.login_failed);
            dialog.setPositiveButton(R.string.dialog_ok, null);
            dialog.setCancelable(false);
            dialog.show();

        } else if (Constant.GET_SUCCESS_MSG == 2) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(R.string.whops);
            dialog.setMessage(R.string.login_disabled);
            dialog.setPositiveButton(R.string.dialog_ok, null);
            dialog.setCancelable(false);
            dialog.show();

        } else {
            MyApp.saveIsLogin(true);

            MyApp.saveLogin(strId, strNombre, IdCompany);

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(R.string.login_title);
            dialog.setMessage(R.string.login_success);
            dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(getBaseContext(), DashboardMenu.class));
                    finish();
                }
            });
            dialog.setCancelable(false);
            dialog.show();

        }
    }
    private class MyTaskLoginNormal extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Login.this);
            progressDialog.setTitle(getResources().getString(R.string.title_please_wait));
            progressDialog.setMessage(getResources().getString(R.string.login_process));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return Tools.getJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (null == result || result.length() == 0) {

            } else {

                try {

                   JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.CATEGORY_ARRAY_NAME);
                    JSONObject objJson = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        if (objJson.has(Constant.MSG)) {
                            strMessage = objJson.getString(Constant.MSG);
                            Constant.GET_SUCCESS_MSG = objJson.getInt(Constant.SUCCESS);
                        } else {
                            Constant.GET_SUCCESS_MSG = objJson.getInt(Constant.SUCCESS);
                            strNombre = objJson.getString(Constant.USER_NAME);
                            strId = objJson.getString(Constant.USER_ID);
                            //strImage = objJson.getString("normal");

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (null != progressDialog && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        setResult();
                    }
                }, Constant.DELAY_PROGRESS_DIALOG);
            }

        }
    }


}
