package android.androidVNC.ui;

import android.androidVNC.Helpers.CourseRecyclerHelper;
import android.androidVNC.R;
import android.androidVNC.adapters.CoursesListAdapter;
import android.androidVNC.models.Course;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity implements CourseRecyclerHelper.RecyclerItemTouchHelperListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Course> courseList = new ArrayList<Course>();
    private static CoursesListAdapter coursesListAdapter;
    Bundle restartBundle;
    FloatingActionButton floatingActionButtonAdd;
    FloatingActionButton floatingActionButtonTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        restartBundle = savedInstanceState;
        recyclerView = findViewById(R.id.recycler_view);
        floatingActionButtonAdd = findViewById(R.id.add_course_fab);
        floatingActionButtonTest = findViewById(R.id.test_fab);
        courseList = new ArrayList<>();
        coursesListAdapter = new CoursesListAdapter(this, courseList);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(coursesListAdapter);

         // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new CourseRecyclerHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(MainActivity.this);
                View mView = layoutInflaterAndroid.inflate(R.layout.student_add_course_dialog, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilderUserInput.setView(mView);

                final EditText courename =  mView.findViewById(R.id.course_name);
                final EditText courecode =  mView.findViewById(R.id.course_code);
                final EditText coureday =  mView.findViewById(R.id.course_day);
                final EditText starttime =  mView.findViewById(R.id.start_time);
                final EditText endtime =  mView.findViewById(R.id.end_time);

                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                // ToDo get user input here
                                Course newCourse = new Course();
                                newCourse.setCourseName(courename.getText().toString());
                                newCourse.setCourseCode(courecode.getText().toString());
                                newCourse.setCourseDay(coureday.getText().toString());
                                newCourse.setStartTime(starttime.getText().toString());
                                newCourse.setEndTime(endtime.getText().toString());

                                courseList.add(newCourse);
                                coursesListAdapter.notifyDataSetChanged();

                            }
                        })

                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();
            }
        });

        floatingActionButtonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start the dummy activity here
                startActivity(new Intent(MainActivity.this, ControlPhoneActivity.class));
                finish();

            }
        });

        // making http call and fetching menu json
        prepareCourseList();

    }


    public void prepareCourseList(){

        Course course = new Course();
        course.setCourseCode(" ");
        course.setCourseName("No course added ");
        course.setStartTime(" ");
        course.setEndTime(" ");

        courseList.add(course);  courseList.add(course);

    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CoursesListAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = courseList.get(viewHolder.getAdapterPosition()).getCourseName();

            // backup of removed item for undo purpose
            final Course deletedCourse = courseList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            coursesListAdapter.removeCourse(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option


            Snackbar snackbar = Snackbar
                    .make(recyclerView, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // undo is selected, restore the deleted item
                    coursesListAdapter.restoreCourse(deletedCourse, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }


}
