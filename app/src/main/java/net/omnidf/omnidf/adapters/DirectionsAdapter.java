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

public class DirectionsAdapter extends RecyclerView.Adapter<DirectionsAdapter.ViewHolder>{

    List<Feature> directions;

    public DirectionsAdapter(List<Feature> directions) {
        this.directions = directions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflatedItemDirectionsWalk;
        inflatedItemDirectionsWalk = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_directions_walk, viewGroup, false);

        return new ViewHolder(inflatedItemDirectionsWalk);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.textDirections.setText(directions.get(i).getProperties().optString("node-name"));

    }

    @Override
    public int getItemCount() {
        return directions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageDirectionType;
        TextView textDirections;

        public ViewHolder(View itemView) {
            super(itemView);

            imageDirectionType = (ImageView) itemView.findViewById(R.id.imageDirectionType);
            textDirections = (TextView) itemView.findViewById(R.id.textDirection);
        }
    }

}
