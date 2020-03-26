package com.gnet.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gnet.objetos.Articulo;
import com.guma.desarrollo.gnet.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterListArticulos extends RecyclerView.Adapter<AdapterListArticulos.MyViewHolder> implements Filterable {

    private Context context;
    private List<Articulo> productList;
    private List<Articulo> productListFiltered;
    private AdapterListArticulos.ContactsAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView Codigo,name;

        public TextView Venta_meta,Venta_real,venta_Cumplimiento;

        public TextView items_meta,items_real,items_Cumplimiento;

        public MyViewHolder(View v) {
            super(v);
            Codigo = (TextView) v.findViewById(R.id.idCodigoArticulo);
            name = (TextView) v.findViewById(R.id.IdNombrearticulo);

            Venta_meta = (TextView) v.findViewById(R.id.id_venta_meta);
            Venta_real = (TextView) v.findViewById(R.id.id_venta_real);
            venta_Cumplimiento = (TextView) v.findViewById(R.id.id_cumplimiento);

            items_meta = (TextView) v.findViewById(R.id.id_items_meta);
            items_real = (TextView) v.findViewById(R.id.id_items_real);
            items_Cumplimiento = (TextView) v.findViewById(R.id.id_items_Cumplimiento);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onContactSelected(productListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public AdapterListArticulos(Context context, List<Articulo> productList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.productList = productList;
        this.productListFiltered = productList;
    }

    @Override
    public AdapterListArticulos.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inbox, parent, false);
        return new AdapterListArticulos.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterListArticulos.MyViewHolder holder, final int position) {
        final Articulo p = productListFiltered.get(position);

        holder.name.setText(p.getmName());

        holder.Codigo.setText(p.getmCodigo());
        holder.Venta_meta.setText(("C$ ").concat(p.getmMeta_monto()));
        holder.Venta_real.setText(("C$ ").concat(p.getmReal_monto()));
        holder.venta_Cumplimiento.setText(("% ").concat(p.getMcump_monto()));

        holder.items_meta.setText(p.getmMeta_canti());
        holder.items_real.setText(p.getmReal_canti());
        holder.items_Cumplimiento.setText(("% ").concat(p.getMcump_canti()));
        //Tools.displayImageRound(context, holder.image, R.drawable.ic01);

    }

    @Override
    public int getItemCount() {
        return productListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    productListFiltered = productList;
                } else {
                    List<Articulo> filteredList = new ArrayList<>();
                    for (Articulo row : productList) {
                        if (row.getmName().toLowerCase().contains(charString.toLowerCase())) {

                            filteredList.add(row);
                        }
                    }
                    productListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productListFiltered = (ArrayList<Articulo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(Articulo product);
    }

}