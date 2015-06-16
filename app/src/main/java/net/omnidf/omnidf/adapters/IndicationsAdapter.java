package net.omnidf.omnidf.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cocoahero.android.geojson.Feature;

import net.omnidf.omnidf.R;

import java.util.List;

public class IndicationsAdapter extends RecyclerView.Adapter<IndicationsAdapter.ViewHolder>{

    List<Feature> indications;

    public IndicationsAdapter(List<Feature> indications) {
        this.indications = indications;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflatedItem;
        inflatedItem = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_indications_walk, viewGroup, false);

        return new ViewHolder(inflatedItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.textIndication.setText(indications.get(i).getProperties().optString("node-name"));

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
