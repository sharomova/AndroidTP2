package tp2.ca.qc.bdeb.trainer.vue;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tp2.ca.qc.bdeb.trainer.R;
import tp2.ca.qc.bdeb.trainer.modele.Cource;
import tp2.ca.qc.bdeb.trainer.modele.Utilisateur;

/**
 * Created by Olga on 2015-11-21.
 */
public class ArrayAdapterHistoire extends ArrayAdapter<Cource> {
    Context context;

    public ArrayAdapterHistoire(Context context, int resourceId,
                              List<Cource> items) {
        super(context, resourceId, items);
        this.context = context;
    }
    /* Classe holder qui contiendra l'élément de la ligne */
    private class NainHolder {

        TextView txtDate;
        TextView txtDistance;
        ImageView type;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NainHolder holder = null;
        Cource rowItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_un_cource, null);
            holder = new NainHolder();
            holder.txtDate = (TextView) convertView.findViewById(R.id.un_cource_date);
            holder.txtDistance = (TextView) convertView.findViewById(R.id.un_cource_distance);
            holder.type = (ImageView) convertView.findViewById(R.id.un_cource_image);

            convertView.setTag(holder);
        } else
            holder = (NainHolder) convertView.getTag();

        holder.txtDate.setText(rowItem.getDate());
        holder.txtDistance.setText(rowItem.getDistance() + " km");
        if (rowItem.getType().equals(Inscription.RUN)) {
            holder.type.setImageResource(R.drawable.clicknrun);
        } else if (rowItem.getType().equals(Inscription.VELO)) {
            holder.type.setImageResource(R.drawable.bicycle);
        }

        return convertView;
    }
}
