package com.gnet.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gnet.objetos.ObjetoVencidoPorCliente;
import com.guma.desarrollo.gnet.R;

import java.util.ArrayList;
import java.util.List;


public class AdapterListMora extends RecyclerView.Adapter<AdapterListMora.MyViewHolder> implements Filterable {

    private Context context;
    private List<ObjetoVencidoPorCliente> productList;
    private List<ObjetoVencidoPorCliente> productListFiltered;
    private AdapterListMora.ContactsAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name;

        public TextView Venta_meta,Venta_real,venta_Cumplimiento;

        public TextView items_real,items_Cumplimiento;

        public MyViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.IdNombrearticulo);

            Venta_meta = (TextView) v.findViewById(R.id.id_venta_meta);
            Venta_real = (TextView) v.findViewById(R.id.id_venta_real);
            venta_Cumplimiento = (TextView) v.findViewById(R.id.id_cumplimiento);

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

    public AdapterListMora(Context context, List<ObjetoVencidoPorCliente> productList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.productList = productList;
        this.productListFiltered = productList;
    }

    @Override
    public AdapterListMora.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_por_mora, parent, false);
        return new AdapterListMora.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterListMora.MyViewHolder holder, final int position) {
        final ObjetoVencidoPorCliente p = productListFiltered.get(position);

       holder.name.setText(p.getNombre());

        holder.Venta_meta.setText(p.getNoVencidos());
        holder.Venta_real.setText(p.getDias30());
        holder.venta_Cumplimiento.setText(p.getDias60());
        holder.items_real.setText(p.getDias90());
        holder.items_Cumplimiento.setText(p.getDias120());
        //Tools.displayImageRound(context, holder.image, R.drawable.ic01);*/

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
                    List<ObjetoVencidoPorCliente> filteredList = new ArrayList<>();
                    for (ObjetoVencidoPorCliente row : productList) {
                        if (row.getNombre().toLowerCase().contains(charString.toLowerCase())) {

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
                productListFiltered = (ArrayList<ObjetoVencidoPorCliente>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(ObjetoVencidoPorCliente product);
    }

}