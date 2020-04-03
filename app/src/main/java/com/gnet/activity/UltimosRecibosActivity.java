package com.gnet.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gnet.Adapters.AdapterFacturaVencidas;
import com.gnet.objetos.FacturaVencidas;
import com.gnet.objetos.Recibos;
import com.gnet.utils.ItemOffsetDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guma.desarrollo.gnet.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gnet.utils.Constant.GET_FACTURA_VENCIDA;

public class UltimosRecibosActivity extends AppCompatActivity {

    MyApplication MyApp;
    private List<Recibos> listRecibos;
    String user_id,user_name,user_empresa;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ultimos_recibos);
        MyApp = MyApplication.getInstance();

       /* user_id = MyApp.getUserId();
        user_name = MyApp.getUserName();
        user_empresa = MyApp.getUserEmpresa();
        listRecibos = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
     //   mAdapter = new AdapterListArticulos(this, productList, this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
       // recyclerView.setAdapter(mAdapter);
        ViewRecibos();*/

    }

    private void ViewRecibos() {
        listRecibos.clear();
        new AsyncTask<Integer,Integer,String>() {
            public ProgressDialog pdialog;
            @Override
            protected void onPreExecute() {
                pdialog = ProgressDialog.show(UltimosRecibosActivity.this, "","Sincronizando.", true);
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(Integer... para) {
                mStringRequest = new StringRequest(Request.Method.POST, GET_FACTURA_VENCIDA, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response == null) {
                            Toast.makeText(UltimosRecibosActivity.this, getResources().getString(R.string.failed_fetch_data), Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<Recibos> items = new Gson().fromJson(response, new TypeToken<List<Recibos>>() {
                        }.getType());

                        listRecibos.clear();
                        listRecibos.addAll(items);
                        pdialog.dismiss();

                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Error",  error.getMessage());
                                Toast.makeText(UltimosRecibosActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                pdialog.dismiss();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Ruta",user_id);
                        params.put("Empresa",user_empresa);
                        return params;
                    }

                };
                mRequestQueue = Volley.newRequestQueue(UltimosRecibosActivity.this);
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
