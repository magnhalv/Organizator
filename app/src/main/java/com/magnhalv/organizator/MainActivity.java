package com.magnhalv.organizator;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.magnhalv.organizator.fragments.CalendarFragment;
import com.magnhalv.organizator.fragments.TodoListFragment;

public class MainActivity extends AppCompatActivity {

    private TodoListFragment todoListFragment;
    private CalendarFragment calendarFragment;
    private FragmentManager fragmentMananger;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setMainContent(todoListFragment);
                    return true;
                case R.id.navigation_dashboard:
                    setMainContent(calendarFragment);
                    return true;
                case R.id.navigation_notifications:

                    return true;
            }
            return false;
        }

    };

    private void setMainContent(Fragment fragment) {
        FragmentTransaction transaction = fragmentMananger.beginTransaction();
        transaction.replace(R.id.main_content, fragment);
        transaction.commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentMananger = getFragmentManager();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        todoListFragment = new TodoListFragment();
        calendarFragment = new CalendarFragment();
        setMainContent(todoListFragment);
    }

}
