package com.gnet.utils;

import com.gnet.Config;

public class Constant {
    private static final String BASE_URL = Config.ADMIN_PANEL_URL;
    public static final String GET_RECENT_INFO = BASE_URL + "/estadistica_ruta";
    public static final String GET_ITEMS = BASE_URL + "/estadistica_articulos_ruta";
    public static final String GET_MORA_RUTA = BASE_URL + "/mora_por_ruta";
    public static final String GET_FACTURA_VENCIDA = BASE_URL + "/facturas_vencidas";
    public static final String GET_MORA_CLIENTE = BASE_URL + "/mora_por_cliente";
    public static final String NORMAL_LOGIN_URL = BASE_URL + "/Login";
    public static final int DELAY_PROGRESS_DIALOG = 2000;
    public static final String CATEGORY_ARRAY_NAME = "result";
    public static final String MSG = "msg";
    public static final String SUCCESS = "success";

    public static final String USER_NAME = "name";
    public static final String USER_ID = "user_id";
    public static int GET_SUCCESS_MSG;

}
