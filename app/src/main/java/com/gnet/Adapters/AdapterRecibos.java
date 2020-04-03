package com.gnet.Adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gnet.objetos.FacturaVencidas;
import com.gnet.objetos.Recibos;
import com.guma.desarrollo.gnet.R;

import java.util.List;


public class AdapterRecibos extends RecyclerView.Adapter<AdapterRecibos.MyViewHolder> {

    private LayoutInflater inflater;
    private List<Recibos> Facturas;


    public AdapterRecibos(Context ctx, List<Recibos> MyFacturas){

        inflater = LayoutInflater.from(ctx);
        this.Facturas = MyFacturas;
    }

    @Override
    public AdapterRecibos.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_inbox_facturas_vencidas, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(AdapterRecibos.MyViewHolder h, int p) {
        final Recibos f = Facturas.get(p);

      /*  h.Nombre.setText(f.getNOMBRE());

        h.Factura.setText(("Fact. ").concat(f.get()));
        h.SaldoFactura.setText(("C$ ").concat(f.getSALDO_LOCAL()));
        h.FechaDeVencimiento.setText(("FdV. ").concat(f.getFECHA_VENCE()));
        h.DiasVencidos.setText(f.getDVencidos().concat(" Dias."));
        h.FechaFactura.setText(f.getFECHA());*/



       // int iconColor = ((Integer.valueOf(f.getDVencidos()) <= 0) ? android.graphics.Color.RED : R.color.light_green_300);
       // h.imgVencidas.setColorFilter(iconColor, PorterDuff.Mode.MULTIPLY);

    }

    @Override
    public int getItemCount() {
        return Facturas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Nombre,Factura,SaldoFactura,FechaDeVencimiento,DiasVencidos,FechaFactura;

        ImageView imgVencidas;

        public MyViewHolder(View i) {
            super(i);

            Nombre =  i.findViewById(R.id.txtNombre);
            Factura= i.findViewById(R.id.txtFactura);
            FechaFactura = i.findViewById(R.id.txtFFactura);
            SaldoFactura =  i.findViewById(R.id.txtSaldoFactura);
            imgVencidas = i.findViewById(R.id.imgVencidas);
            FechaDeVencimiento = i.findViewById(R.id.txtFdV);
            DiasVencidos = i.findViewById(R.id.txtDvencidos);

        }

    }
}