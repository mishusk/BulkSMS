package bulksms.tdd.tddbulksms.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import bulksms.tdd.tddbulksms.R;
import bulksms.tdd.tddbulksms.model.MessageModel;

/**
 * Created by y34h1a on 3/18/17.
 */


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterVH>{
    private ArrayList<MessageModel> messageList = new ArrayList<>();

    public MessageAdapter(ArrayList<MessageModel> messageList){
        this.messageList = messageList;
    }


    @Override
    public MessageAdapterVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycleview_message_list, parent, false);
        return new MessageAdapterVH(itemView);
    }

    @Override
    public void onBindViewHolder(MessageAdapterVH holder, int position) {
        holder.tvMessage.setText(messageList.get(position).getMessage());
    }

    public void add (int position, MessageModel MessageModel){
        messageList.add(position, MessageModel);
        notifyItemInserted(position);
    }

    public void delete(int position){
        messageList.remove(position);
        notifyItemRemoved(position);
    }


    public void reAddMatchedRide(ArrayList<MessageModel> messageList){
        this.messageList = messageList;
        notifyItemRangeChanged(0, messageList.size());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }


    public class MessageAdapterVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvMessage;
        Button btCopy;
        public MessageAdapterVH(View itemView) {
            super(itemView);
            tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            btCopy = (Button) itemView.findViewById(R.id.btCopy);
            itemView.setOnClickListener(this);
            btCopy.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
