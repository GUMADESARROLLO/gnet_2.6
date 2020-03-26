package com.gnet.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gnet.Adapters.AdapterListMora;
import com.gnet.objetos.ObjetoVencidoPorCliente;
import com.gnet.utils.ItemOffsetDecoration;
import com.gnet.utils.Tools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guma.desarrollo.gnet.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gnet.utils.Constant.GET_MORA_CLIENTE;

public class MoraPorClienteActivity extends AppCompatActivity implements AdapterListMora.ContactsAdapterListener{
    private RecyclerView recyclerView;
    private List<ObjetoVencidoPorCliente> productList;
    private AdapterListMora mAdapter;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    MyApplication MyApp;
    String user_id,user_name,user_empresa;


    EditText et_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp = MyApplication.getInstance();
        setContentView(R.layout.activity_mora_por_cliente);
        Intent intent = getIntent();
        productList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        mAdapter = new AdapterListMora(this, productList, this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        user_id = MyApp.getUserId();
        user_name = MyApp.getUserName();
        user_empresa = MyApp.getUserEmpresa();

        et_search = findViewById(R.id.et_search);


        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        fetchData();
        onRefresh();
    }
    private void onRefresh() {
        productList.clear();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Tools.isNetworkAvailable(MoraPorClienteActivity.this)) {
                    fetchData();
                } else {
                    Toast.makeText(MoraPorClienteActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }

            }
        }, 1500);
    }
    private void fetchData() {
        new AsyncTask<Integer,Integer,String>() {
            public ProgressDialog pdialog;
            @Override
            protected void onPreExecute() {
                pdialog = ProgressDialog.show(MoraPorClienteActivity.this, "","Sincronizando.", true);
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(Integer... para) {
                mStringRequest = new StringRequest(Request.Method.POST, GET_MORA_CLIENTE, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response == null) {
                            Toast.makeText(MoraPorClienteActivity.this, getResources().getString(R.string.failed_fetch_data), Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<ObjetoVencidoPorCliente> items = new Gson().fromJson(response, new TypeToken<List<ObjetoVencidoPorCliente>>() {
                        }.getType());


                        productList.clear();
                        productList.addAll(items);
                        mAdapter.notifyDataSetChanged();
                        pdialog.dismiss();

                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Error",  error.getMessage());
                                Toast.makeText(MoraPorClienteActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

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
                mRequestQueue = Volley.newRequestQueue(MyApp);
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
    public void onContactSelected(ObjetoVencidoPorCliente obj) {



    }
}
