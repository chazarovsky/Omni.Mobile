package net.omnidf.omnidf.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cocoahero.android.geojson.Feature;
import com.cocoahero.android.geojson.Point;

import net.omnidf.omnidf.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DirectionsAdapter extends RecyclerView.Adapter<DirectionsAdapter.ViewHolder> {

    List<Feature> directions;
    int adapterSize;

    public DirectionsAdapter(List<Feature> directions) {
        this.directions = directions;
        JSONObject properties = new JSONObject();
        try {
            properties.put("transport-type", "Walk");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Point point = new Point(38.889462878011365, -77.03525304794312);
        Feature feature = new Feature(point);
        feature.setProperties(properties);
        directions.add(feature);
        adapterSize = directions.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View walkingDirectionsStepView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_directions, viewGroup, false);

        return new ViewHolder(walkingDirectionsStepView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (i == 0) {
            viewHolder.imageDirectionType.setImageResource(R.drawable.ic_walkround);
            viewHolder.textDirections
                    .setText("Dirígete desde tu origen hasta el metro: " +
                            directions.get(i).getProperties().optString("node-name"));
        } else if (i == adapterSize - 1) {
            viewHolder.imageDirectionType.setImageResource(R.drawable.ic_walkround);
            viewHolder.textDirections.setText("Camina del metro " +
                    directions.get(i - 1).getProperties().optString("node-name") + " hacia tu destino");
        } else {
            viewHolder.imageDirectionType.setImageResource(R.drawable.ic_metroround);
            viewHolder.textDirections
                    .setText(directions.get(i - 1).getProperties().optString("node-name") + " > "
                            + directions.get(i).getProperties().optString("node-name"));
        }
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
