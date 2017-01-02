package ashu.yogaforyou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import ashu.yogaforyou.R;

/**
 * Created by apple on 01/01/17.
 */

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder>{

    private Context context;
    private String [] strExerciseName;
    private ViewHolder.ClickListener clickListener;
    private int listType;
    private int lastPosition = -1;


    public ExerciseAdapter(Context context, String [] strExerciseName, ViewHolder.ClickListener listener, int viewType){
        this.context = context;
        this.strExerciseName = strExerciseName;
        this.clickListener = listener;
        this.listType = viewType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHolder viewHolder;
        context = parent.getContext();
        // view = LayoutInflater.from(context).inflate(R.layout.alerts_item_grid, parent, false);
        view = LayoutInflater.from(context).inflate(R.layout.exercise_list, parent, false);

        viewHolder = new ViewHolder(view, viewType, clickListener);
        view.setOnClickListener(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtCategoryName.setText(strExerciseName[position]);
        setAnimation(holder.itemView, position);

    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return strExerciseName.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtCategoryName;
        private ClickListener listener;

        public ViewHolder(View itemView, int viewType, ExerciseAdapter.ViewHolder.ClickListener listener) {
            super(itemView);
            txtCategoryName = (TextView) itemView.findViewById(R.id.txtExerciseName);
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
