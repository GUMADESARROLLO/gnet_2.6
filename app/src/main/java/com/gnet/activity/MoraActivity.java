package com.gnet.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gnet.Adapters.AdapterFacturaVencidas;
import com.gnet.objetos.FacturaVencidas;
import com.gnet.objetos.ObjetoVencidoPorRuta;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guma.desarrollo.gnet.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gnet.utils.Constant.GET_FACTURA_VENCIDA;
import static com.gnet.utils.Constant.GET_MORA_RUTA;

public class MoraActivity extends AppCompatActivity {

    TextView NOV,D30,D60,D90,D120,M120,TVN;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    String user_id,user_name,user_empresa;
    MyApplication MyApp;

    private List<FacturaVencidas> listFacturasVencidas;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mora);

        NOV             = findViewById(R.id.id_novencido);
        D30             = findViewById(R.id.id_D30);
        D60             = findViewById(R.id.id_D60);
        D90             = findViewById(R.id.id_D90);
        D120            = findViewById(R.id.id_D120);
        M120            = findViewById(R.id.id_M120);
        TVN             = findViewById(R.id.id_TVENCIDO);

        MyApp = MyApplication.getInstance();

        user_id = MyApp.getUserId();
        user_name = MyApp.getUserName();
        user_empresa = MyApp.getUserEmpresa();

        listFacturasVencidas = new ArrayList<>();


        findViewById(R.id.id_call_cliente_mora).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent iMora02 = new Intent(MoraActivity.this, MoraPorClienteActivity.class);
                startActivity(iMora02);
            }
        });


        NOV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewFacturasVencidas("0");
            }
        });

        D30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewFacturasVencidas("30");
            }
        });

        D60.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewFacturasVencidas("60");
            }
        });

        D90.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewFacturasVencidas("90");
            }
        });

        D120.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewFacturasVencidas("120");
            }
        });

        M120.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewFacturasVencidas("130");
            }
        });


        fetchData();

    }

    private void fetchData() {
        new AsyncTask<Integer,Integer,String>() {
            public ProgressDialog pdialog;
            @Override
            protected void onPreExecute() {
                pdialog = ProgressDialog.show(MoraActivity.this, "","Sincronizando.", true);
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(Integer... para) {
                mStringRequest = new StringRequest(Request.Method.POST, GET_MORA_RUTA, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response == null) {
                            Toast.makeText(MoraActivity.this, getResources().getString(R.string.failed_fetch_data), Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<ObjetoVencidoPorRuta> items = new Gson().fromJson(response, new TypeToken<List<ObjetoVencidoPorRuta>>() {
                        }.getType());
                        NOV.setText(items.get(0).getNoVencidos());
                        D30.setText(items.get(0).getDias30());
                        D60.setText(items.get(0).getDias60());
                        D90.setText(items.get(0).getDias90());
                        D120.setText(items.get(0).getDias120());
                        M120.setText(items.get(0).getMas120());
                        TVN.setText(String.valueOf(items.get(0).getmTotal()));
                        pdialog.dismiss();

                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                NOV.setText("N/D");
                                D30.setText("N/D");
                                D60.setText("N/D");
                                D90.setText("N/D");
                                D120.setText("N/D");
                                M120.setText("N/D");

                                Log.e("Error",  error.getMessage());
                                Toast.makeText(MoraActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                                pdialog.dismiss();

                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Ruta", user_id);
                        params.put("Empresa", user_empresa);
                        return params;
                    }

                };
                mRequestQueue = Volley.newRequestQueue(MoraActivity.this);
                mRequestQueue.add(mStringRequest);
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
            }

        }.execute();




    }

    @SuppressLint("StaticFieldLeak")
    private void ViewFacturasVencidas(final String s) {
        listFacturasVencidas.clear();
        new AsyncTask<Integer,Integer,String>() {
            public ProgressDialog pdialog;
            @Override
            protected void onPreExecute() {
                pdialog = ProgressDialog.show(MoraActivity.this, "","Sincronizando.", true);
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(Integer... para) {
                mStringRequest = new StringRequest(Request.Method.POST, GET_FACTURA_VENCIDA, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response == null) {
                            Toast.makeText(MoraActivity.this, getResources().getString(R.string.failed_fetch_data), Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<FacturaVencidas> items = new Gson().fromJson(response, new TypeToken<List<FacturaVencidas>>() {
                        }.getType());

                        listFacturasVencidas.clear();
                        listFacturasVencidas.addAll(items);


                        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(MoraActivity.this);
                        View mView = layoutInflaterAndroid.inflate(R.layout.input_dialog_factura_vencidas, null);
                        RecyclerView recyclerView = mView.findViewById(R.id.recyclerView);
                        AdapterFacturaVencidas mAdapter = new AdapterFacturaVencidas(MoraActivity.this, listFacturasVencidas);
                        recyclerView.setAdapter(mAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));


                        AlertDialog.Builder alert = new AlertDialog.Builder(MoraActivity.this,android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
                        alert.setView(mView);
                        alert.setCancelable(false);


                        alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                        AlertDialog alertDialog = alert.create();
                        alertDialog.show();
                        pdialog.dismiss();

                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Error",  error.getMessage());
                                Toast.makeText(MoraActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                pdialog.dismiss();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("DiasMora", s);
                        params.put("Ruta",user_id);
                        return params;
                    }

                };
                mRequestQueue = Volley.newRequestQueue(MoraActivity.this);
                mRequestQueue.add(mStringRequest);
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
            }

        }.execute();




    }


}
