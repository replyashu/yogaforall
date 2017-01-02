package ashu.yogaforyou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ashu.yogaforyou.R;

/**
 * Created by apple on 01/01/17.
 */

public class HomeFragmentAdapter extends RecyclerView.Adapter<HomeFragmentAdapter.ViewHolder>{

    private Context context;
    private String [] yogaCat;
    private ViewHolder.ClickListener clickListener;

    public HomeFragmentAdapter(Context context, String [] categories, ViewHolder.ClickListener listener){
        this.context = context;
        this.yogaCat = categories;
        this.clickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHolder viewHolder;
        context = parent.getContext();
        // view = LayoutInflater.from(context).inflate(R.layout.alerts_item_grid, parent, false);
            view = LayoutInflater.from(context).inflate(R.layout.home_item_grid, parent, false);
        viewHolder = new ViewHolder(view, viewType, clickListener);
        view.setOnClickListener(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtCategoryName.setText(yogaCat[position]);
    }

    @Override
    public int getItemCount() {
        return yogaCat.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtCategoryName;
        private ClickListener listener;

        public ViewHolder(View itemView, int viewType, ClickListener listener) {
            super(itemView);
            txtCategoryName = (TextView) itemView.findViewById(R.id.txtTypeYoga);
            this.listener = listener;
//            Toolbar toolbar = (Toolbar)itemView;
//            toolbar.inflateMenu(R.menu.menu_alert_item);
//            toolbar.setOnMenuItemClickListener(this);

        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(getAdapterPosition());
        }

        public interface ClickListener{
            void onItemClick(int position);
        }

    }

}
