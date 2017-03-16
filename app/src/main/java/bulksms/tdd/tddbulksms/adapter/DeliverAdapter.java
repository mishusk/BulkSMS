package bulksms.tdd.tddbulksms.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import bulksms.tdd.tddbulksms.R;
import bulksms.tdd.tddbulksms.model.DeliveredInfo;
import bulksms.tdd.tddbulksms.model.InfoModel;

/**
 * Created by y34h1a on 3/7/17.
 */

public class DeliverAdapter extends RecyclerView.Adapter<DeliverAdapter.DeliverAdapterVH>{
    private ArrayList<InfoModel> deliveredInfos = new ArrayList<>();

    public DeliverAdapter(ArrayList<InfoModel> deliveredInfos){
        this.deliveredInfos = deliveredInfos;
    }


    @Override
    public DeliverAdapterVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_deliveredlist, parent, false);
        return new DeliverAdapterVH(itemView);
    }

    @Override
    public void onBindViewHolder(DeliverAdapterVH holder, int position) {
        holder.tvPhone.setText(deliveredInfos.get(position).getPhoneNumber());
        holder.tvTime.setText(deliveredInfos.get(position).getSendTime());
    }

    public void reAddMatchedRide(ArrayList<InfoModel> deliveredInfos){
        this.deliveredInfos = deliveredInfos;
        notifyItemRangeChanged(0, deliveredInfos.size());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return deliveredInfos.size();
    }

    public class DeliverAdapterVH extends RecyclerView.ViewHolder {
        TextView tvPhone;
        TextView tvTime;
        public DeliverAdapterVH(View itemView) {
            super(itemView);
            tvPhone = (TextView) itemView.findViewById(R.id.tvPhoneNum);
            tvTime = (TextView) itemView.findViewById(R.id.tvDeliverTime);
        }
    }
}