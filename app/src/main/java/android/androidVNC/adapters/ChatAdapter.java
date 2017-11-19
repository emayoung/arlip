package android.androidVNC.adapters;

import android.androidVNC.R;
import android.androidVNC.models.Course;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Bless on 10/16/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private Context context;
    private List<Course> courseList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description, price;
        public TextView thumbnail;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.description);
            price = view.findViewById(R.id.price);
            thumbnail = view.findViewById(R.id.thumbnail);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }


    public ChatAdapter(Context context, List<Course> courseList) {
        this.context = context;
        this.courseList = courseList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Course course = courseList.get(position);
        holder.name.setText(course.getCourseName());
        holder.description.setText(course.getCourseCode());
        holder.price.setText(course.getStartTime() + " - " + course.getEndTime());

    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public void removeCourse(int position) {
        courseList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreCourse(Course course, int position) {
        courseList.add(position, course);
        // notify item added by position
        notifyItemInserted(position);
    }

}
