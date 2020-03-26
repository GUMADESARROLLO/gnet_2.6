package com.gnet.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gnet.Adapters.AdapterListArticulos;
import com.gnet.objetos.Articulo;
import com.gnet.utils.ItemOffsetDecoration;
import com.gnet.utils.Tools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guma.desarrollo.gnet.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gnet.utils.Constant.GET_ITEMS;

public class ListProductoActivity extends AppCompatActivity implements AdapterListArticulos.ContactsAdapterListener {

    private RecyclerView recyclerView;
    private List<Articulo> productList;
    private AdapterListArticulos mAdapter;
    private SearchView searchView;
    private String category_id, category_name;
    private View parent_view;
    SwipeRefreshLayout swipeRefreshLayout = null;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    String user_id,user_name,user_empresa;
    MyApplication MyApp;

    String strMes,StrAnnio,StrFiltro,StrGrafica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);
        parent_view = findViewById(android.R.id.content);
        MyApp = MyApplication.getInstance();
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setRefreshing(true);
        Intent intent = getIntent();
        initToolbar();


        productList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        mAdapter = new AdapterListArticulos(this, productList, this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        user_id = MyApp.getUserId();
        user_name = MyApp.getUserName();
        user_empresa = MyApp.getUserEmpresa();


        strMes    = intent.getStringExtra("id_mes");
        StrAnnio  = intent.getStringExtra("id_annio");

        StrFiltro    = intent.getStringExtra("id_filtro");
        StrGrafica  = intent.getStringExtra("id_grafica");

        fetchData();
        onRefresh();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Metas por Articulos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setBarColor(this, R.color.colorPrimaryDark);
    }

    private void onRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                productList.clear();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Tools.isNetworkAvailable(ListProductoActivity.this)) {
                            swipeRefreshLayout.setRefreshing(false);
                            fetchData();
                        } else {
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(ListProductoActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, 1500);
            }
        });
    }

    private void fetchData() {
          swipeRefreshLayout.setRefreshing(false);
        new AsyncTask<Integer,Integer,String>() {
            public ProgressDialog pdialog;
            @Override
            protected void onPreExecute() {
                pdialog = ProgressDialog.show(ListProductoActivity.this, "","Sincronizando.", true);
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(Integer... para) {
                mStringRequest = new StringRequest(Request.Method.POST, GET_ITEMS, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response == null) {
                            Toast.makeText(ListProductoActivity.this, getResources().getString(R.string.failed_fetch_data), Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<Articulo> items = new Gson().fromJson(response, new TypeToken<List<Articulo>>() {
                        }.getType());


                        productList.clear();
                        productList.addAll(items);
                        mAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                        pdialog.dismiss();

                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Error",  error.getMessage());
                                Toast.makeText(ListProductoActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                swipeRefreshLayout.setRefreshing(false);
                                pdialog.dismiss();

                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Ruta", user_id);
                        params.put("Empresa", user_empresa);
                        params.put("Mes", strMes);
                        params.put("Annio", StrAnnio);
                        params.put("Filtro", StrFiltro);
                        params.put("Grafica", StrGrafica);
                        return params;
                    }

                };
                mRequestQueue = Volley.newRequestQueue(ListProductoActivity.this);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_producto, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onContactSelected(Articulo obj) {

        inputDialog(obj);


    }
    public void inputDialog(Articulo obj) {
        final TextView txt01,txt02,txt03,txt04,txt05,txt06;

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);

        View mView = layoutInflaterAndroid.inflate(R.layout.input_dialog, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(mView);

        txt01 = mView.findViewById(R.id.txt01);
        txt02 = mView.findViewById(R.id.txt02);
        txt03 = mView.findViewById(R.id.txt03);
        txt04 = mView.findViewById(R.id.txt04);
        txt05 = mView.findViewById(R.id.txt05);
        txt06 = mView.findViewById(R.id.txt06);




        txt01.setText("C$ " + obj.getmMeta_monto());
        txt02.setText("C$ " + obj.getmReal_monto());
        txt03.setText("% " + obj.getMcump_monto());

        txt04.setText(obj.getmMeta_canti());
        txt05.setText(obj.getmReal_canti());
        txt06.setText("% " + obj.getMcump_canti());
        alert.setCancelable(false);


        alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alert.create();
        alertDialog.show();

    }
}