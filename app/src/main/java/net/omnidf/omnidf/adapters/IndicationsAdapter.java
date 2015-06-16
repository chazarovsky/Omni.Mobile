package net.omnidf.omnidf.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.omnidf.omnidf.pojos.Features;
import net.omnidf.omnidf.R;

import java.util.ArrayList;

public class IndicationsAdapter extends RecyclerView.Adapter<IndicationsAdapter.ViewHolder>{

    ArrayList<Features> indications;

    public IndicationsAdapter(ArrayList<Features> indications) {
        this.indications = indications;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = null;
        if(i == 0){
        v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_indications, viewGroup, false); }
        else if(i == 1){
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_indications_metro, viewGroup, false);
        }

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.imageIndicationType.setImageResource(R.drawable.testicon);
        viewHolder.textIndication.setText(indications.get(i).getIndication());
    }

    @Override
    public int getItemCount() {
        return indications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageIndicationType;
        TextView textIndication;

        public ViewHolder(View itemView) {
            super(itemView);

            imageIndicationType = (ImageView) itemView.findViewById(R.id.imageIndicationType);
            textIndication = (TextView) itemView.findViewById(R.id.textIndication);
        }
    }

}
