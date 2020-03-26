package com.gnet.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.gnet.utils.Tools;
import com.gnet.utils.ViewAnimation;
import com.google.android.material.navigation.NavigationView;
import com.guma.desarrollo.gnet.R;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.gnet.utils.Constant.GET_RECENT_INFO;

public class DashboardMenu extends AppCompatActivity {

    private ActionBar actionBar;
    private Toolbar toolbar;
    private Menu menu_navigation;
    private DrawerLayout drawer;
    private View navigation_header;
    private boolean is_account_mode = false;

    SwipeRefreshLayout swipeRefreshLayout = null;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    TextView txtNombreRuta,txtCodRuta,txtMetaMonto,txtMontoReal,txtCumpliMonto,txtCantMeta,txtRealCant,txtCumpliCant;
    TextView menu_name,menu_empresa;
    MyApplication MyApp;
    String user_id,user_name,user_empresa;

    private ImageView bt_toggle_text,bt_toggle_text02;
    private View lyt_expand_text,lyt_expand_text02;

    private static final String[] MESES_ANNIO = {
            "Enero", "Febrero", "Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"
    };
    private static final String[] ANNIOS = {
            "2018", "2019", "2020"
    };
    public static final int[] CUSTOMS_COLORS = {
            rgb("#058DC7"), rgb("#50B432")
    };
    public static final int[] CUSTOMS_COLORS_02 = {
            rgb("#058DC7"), rgb("#f8b109")
    };

    String strMes,StrAnnio;


    String str_cumplimiento_venta="0",str_cumplimiento_items="0";
    ArrayList<PieEntry> data_set_Chart_01,data_set_Chart_02;
    PieChart pieChart01,pieChart02;
    MaterialSpinner spinner01,spinner02;
    ImageButton btn_refresh,btn_articulo;

    NavigationView nav_view_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_dashboard);

        initToolbar();
        initNavigationMenu();
        MyApp = MyApplication.getInstance();
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setRefreshing(true);

        txtNombreRuta   = findViewById(R.id.txtNombreRuta);
        txtCodRuta      = findViewById(R.id.txtCodRuta);

        txtMetaMonto    = findViewById(R.id.txtMetaMonto);
        txtMontoReal    = findViewById(R.id.txtMontoReal);
        txtCumpliMonto  = findViewById(R.id.txtCumpliMonto);
        txtCantMeta     = findViewById(R.id.txtCantMeta);
        txtRealCant     = findViewById(R.id.txtRealCant);
        txtCumpliCant   = findViewById(R.id.txtCumpliCant);

        btn_refresh     = findViewById(R.id.btnIc_refresh);
        btn_articulo    = findViewById(R.id.idbtn_articulo);


        Calendar cal = Calendar.getInstance();

        spinner01 = findViewById(R.id.spinner01);
        spinner01.setItems(MESES_ANNIO);
        spinner01.setSelectedIndex(cal.get(Calendar.MONTH));

        spinner02 = findViewById(R.id.spinner02);
        spinner02.setItems(ANNIOS);
        spinner02.setSelectedIndex(Arrays.asList(ANNIOS).indexOf(String.valueOf(cal.get(Calendar.YEAR))));



        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData();
            }
        });



        bt_toggle_text =findViewById(R.id.bt_toggle_text);
        lyt_expand_text = findViewById(R.id.lyt_expand_text);
        lyt_expand_text.setVisibility(View.GONE);

        bt_toggle_text02 =findViewById(R.id.bt_toggle_text02);
        lyt_expand_text02 = findViewById(R.id.lyt_expand_text02);
        lyt_expand_text02.setVisibility(View.GONE);



        user_id = MyApp.getUserId();
        user_name = MyApp.getUserName();
        user_empresa = MyApp.getUserEmpresa();

        fetchData();
        onRefresh();

        pieChart01 = findViewById(R.id.piechart);
        pieChart02 = findViewById(R.id.piechart02);

        SettingsCharts(new PieDataSet(data_set_Chart_01,""),pieChart01,CUSTOMS_COLORS);
        SettingsCharts(new PieDataSet(data_set_Chart_02,""),pieChart02,CUSTOMS_COLORS_02);

        toggleSectionText(bt_toggle_text);
        toggleSectionText02(bt_toggle_text02);


        pieChart01.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
        {
            @Override
            public void onValueSelected(Entry e, Highlight h)
            {
                if (e == null)
                    return;

                Intent intent = new Intent(DashboardMenu.this, ListProductoActivity.class);
                PieEntry pe01 = (PieEntry) e;
                intent.putExtra("id_mes", strMes);
                intent.putExtra("id_annio", StrAnnio);
                intent.putExtra("id_filtro", pe01.getLabel());
                intent.putExtra("id_grafica", "Monto");
                startActivity(intent);
            }

            @Override
            public void onNothingSelected()
            {

            }
        });


        pieChart02.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
        {
            @Override
            public void onValueSelected(Entry e, Highlight h)
            {
                if (e == null)
                    return;
                Intent intent = new Intent(DashboardMenu.this, ListProductoActivity.class);
                PieEntry pe02 = (PieEntry) e;

                intent.putExtra("id_mes", strMes);
                intent.putExtra("id_annio", StrAnnio);
                intent.putExtra("id_filtro", pe02.getLabel());
                intent.putExtra("id_grafica", "Items");
                startActivity(intent);
            }

            @Override
            public void onNothingSelected()
            {

            }
        });

        btn_articulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DashboardMenu.this, ListProductoActivity.class);
                intent.putExtra("id_mes", strMes);
                intent.putExtra("id_annio", StrAnnio);
                intent.putExtra("id_filtro", "");
                intent.putExtra("id_grafica", "");
                startActivity(intent);

            }
        });


    }
    private void SettingsCharts(PieDataSet pieDataSet, PieChart pieChart,int[] CUSTOMS_COLORS){
        pieDataSet.setColors(CUSTOMS_COLORS);
        pieDataSet.setValueTextSize(25);
        pieChart.setData(new PieData(pieDataSet));
        pieChart.animateXY(1000, 1000);
        pieChart.invalidate();
        Legend l = pieChart.getLegend();
        pieChart.getDescription().setEnabled(false);
        l.setEnabled(false);

    }

    public static int rgb(String hex) {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return Color.rgb(r, g, b);
    }
    private void toggleSectionText(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_text, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    // Tools.nestedScrollTo(nested_scroll_view, lyt_expand_text);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_text);
        }
    }
    private void toggleSectionText02(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_text02, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    // Tools.nestedScrollTo(nested_scroll_view, lyt_expand_text);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_text02);
        }
    }
    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }
    private void onRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Tools.isNetworkAvailable(DashboardMenu.this)) {
                            swipeRefreshLayout.setRefreshing(false);
                            fetchData();
                        } else {
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(DashboardMenu.this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, 1500);
            }
        });
    }
    private void fetchData() {
        swipeRefreshLayout.setRefreshing(false);
        data_set_Chart_01 = new ArrayList<>();
        data_set_Chart_02 = new ArrayList<>();
        strMes = String.valueOf(new ArrayList<String>(Arrays.asList(MESES_ANNIO)).indexOf(spinner01.getText()) + 1);
        StrAnnio = String.valueOf(spinner02.getText());

        txtMetaMonto.setText("...");
        txtMontoReal.setText("...");
        txtCumpliMonto.setText("...");

        txtCantMeta.setText("...");
        txtRealCant.setText("...");
        txtCumpliCant.setText("...");

        mStringRequest = new StringRequest(Request.Method.POST, GET_RECENT_INFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject origen_datos = new JSONObject(response);


                    JSONArray js_data_venta_monto = origen_datos.getJSONArray("data_venta_monto");
                    JSONArray js_data_venta_cantidad = origen_datos.getJSONArray("data_venta_cantidad");

                    str_cumplimiento_venta = js_data_venta_monto.getJSONObject(0).optString("mCumpliento");
                    str_cumplimiento_items = js_data_venta_cantidad.getJSONObject(0).optString("mCumpliento");


                    txtNombreRuta.setText(user_id.concat(" - ").concat(user_name));
                    txtCodRuta.setText(user_empresa);



                    menu_name.setText(user_id.concat(" - ").concat(user_name));
                    menu_empresa.setText(user_empresa);

                    txtMetaMonto.setText(("C$ ").concat(js_data_venta_monto.getJSONObject(0).optString("mMeta")));
                    txtMontoReal.setText(("C$ ").concat(js_data_venta_monto.getJSONObject(0).optString("mRetal")));
                    txtCumpliMonto.setText(("% ").concat(str_cumplimiento_venta));

                    txtCantMeta.setText(js_data_venta_cantidad.getJSONObject(0).optString("mMeta"));
                    txtRealCant.setText(js_data_venta_cantidad.getJSONObject(0).optString("mRetal"));
                    txtCumpliCant.setText(("% ").concat(str_cumplimiento_items));



                    float Faltante = 100 - Float.parseFloat(str_cumplimiento_venta);
                    Faltante = (Faltante <= 0) ? 0 : Faltante;
                    data_set_Chart_01.add(new PieEntry(Float.parseFloat(str_cumplimiento_venta), "REAL"));
                    data_set_Chart_01.add(new PieEntry(Faltante, "META"));
                    pieChart01.notifyDataSetChanged();
                    pieChart01.invalidate();


                    float Faltante02 = 100 - Float.parseFloat(str_cumplimiento_items);
                    Faltante02 = (Faltante02 <= 0) ? 0 : Faltante02;
                    data_set_Chart_02.add(new PieEntry(Float.parseFloat(str_cumplimiento_items), "REAL"));
                    data_set_Chart_02.add(new PieEntry(Faltante02, "META"));
                    pieChart02.notifyDataSetChanged();
                    pieChart02.invalidate();


                } catch (Exception e) {
                    Log.e("Error",  e.getMessage());
                    Toast.makeText(DashboardMenu.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error",  error.getMessage());
                        Toast.makeText(DashboardMenu.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Ruta", user_id);
                params.put("Empresa", user_empresa);
                params.put("Mes", strMes);
                params.put("Annio", StrAnnio);
                return params;
            }

        };

        mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueue.add(mStringRequest);
    }
    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Resumen");
        Tools.setBarColor(this, R.color.colorPrimaryDark);
    }

    private void initNavigationMenu() {
        final NavigationView nav_view = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                //updateCounter(nav_view);
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                onItemNavigationClicked(item);
                return true;
            }
        });

        // open drawer at start
        //drawer.openDrawer(GravityCompat.START);
        //updateCounter(nav_view);
        menu_navigation = nav_view.getMenu();


        // navigation header
        navigation_header = nav_view.getHeaderView(0);
        menu_name       = navigation_header.findViewById(R.id.menu_name);
        menu_empresa    = navigation_header.findViewById(R.id.menu_empresa);

    }

    private void onItemNavigationClicked(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_about:
                startActivity(new Intent(getBaseContext(), ActivitySplash.class));
                break;
            case R.id.nav_all_items:
                Intent intent = new Intent(DashboardMenu.this, ListProductoActivity.class);
                intent.putExtra("id_mes", strMes);
                intent.putExtra("id_annio", StrAnnio);
                intent.putExtra("id_filtro", "");
                intent.putExtra("id_grafica", "");
                startActivity(intent);
                break;

            case R.id.nav_saldos_vencidos:
                Intent iMora = new Intent(DashboardMenu.this, MoraActivity.class);
                iMora.putExtra("id_mes", strMes);
                iMora.putExtra("id_annio", StrAnnio);
                iMora.putExtra("id_filtro", "");
                iMora.putExtra("id_grafica", "");
                startActivity(iMora);
                break;


            case R.id.nav_action_close:
                new AlertDialog.Builder(DashboardMenu.this)
                        .setTitle("Notificaciones")
                        .setMessage("¿Salir de la Aplicación?")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MyApp.saveIsLogin(false);

                                MyApp.saveLogin("", "", "");
                                finish();
                            }
                        }).setNegativeButton("NO",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
                break;
        }

    }

    private void updateCounter(NavigationView nav) {
        if (is_account_mode) return;
        Menu m = nav.getMenu();
        /*((TextView) m.findItem(R.id.nav_all_inbox).getActionView().findViewById(R.id.text)).setText("75");
        ((TextView) m.findItem(R.id.nav_inbox).getActionView().findViewById(R.id.text)).setText("68");

        TextView badgePrioInbx = (TextView) m.findItem(R.id.nav_priority_inbox).getActionView().findViewById(R.id.text);
        badgePrioInbx.setText("3 new");
        badgePrioInbx.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));

        TextView badgeSocial = (TextView) m.findItem(R.id.nav_social).getActionView().findViewById(R.id.text);
        badgeSocial.setText("51 new");
        badgeSocial.setBackgroundColor(getResources().getColor(R.color.green_500));

        ((TextView) m.findItem(R.id.nav_spam).getActionView().findViewById(R.id.text)).setText("13");*/
    }
}