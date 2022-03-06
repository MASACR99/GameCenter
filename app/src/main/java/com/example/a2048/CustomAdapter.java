package com.example.a2048;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.a2048.MainActivity.USERID;
import static com.example.a2048.MainActivity.ids;
import static com.example.a2048.MainActivity.userTb;
import static com.example.a2048.MainActivity.users;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private boolean buttons;
    public PopupWindow popupWindow;
    private int pressedId;
    CustomAdapter adap = this;
    private MainActivity main;
    private View viewer;

    public CustomAdapter(ArrayList<String> list, Context context, boolean buttons, MainActivity main) {
        this.list = list;
        this.context = context;
        this.buttons = buttons;
        this.main = main;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        viewer = convertView;
        if (viewer == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            viewer = inflater.inflate(R.layout.my_listview, null);
        }

        //Handle TextView and display string from your list
        TextView tvContact = viewer.findViewById(R.id.listView_text);
        tvContact.setText(list.get(position));

        //Handle buttons and add onClickListeners
        Button selectButton = viewer.findViewById(R.id.listView_select);
        Button editButton = viewer.findViewById(R.id.listView_edit);
        Button deleteButton = viewer.findViewById(R.id.listView_delete);
        if(buttons && position < list.size() - 1) {
            selectButton.setVisibility(Button.VISIBLE);
            editButton.setVisibility(Button.VISIBLE);
            deleteButton.setVisibility(Button.VISIBLE);
            selectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pressedId = ids.get(position);
                    //popup to ask for new username?
                    //create a popup that asks a user for it's username
                    // inflate the layout of the popup window
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.password, null);

                    Button button = popupView.findViewById(R.id.doneButton);
                    TextView text = popupView.findViewById(R.id.passwordAsk);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //checked if the user with id pressedId has the same password as the inputted one
                            if(userTb.findById(pressedId).password.equals(text.getText().toString())){
                                USERID = pressedId;
                                Toast.makeText(context, "Selected user " + userTb.findById(USERID).username, Toast.LENGTH_SHORT).show();
                            }
                            popupWindow.dismiss();
                        }
                    });

                    // create the popup window
                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    popupWindow = new PopupWindow(popupView, width, height, focusable);
                    // show the popup window
                    // which view you pass in doesn't matter, it is only used for the window tolken
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                }
            });
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //popup to ask for new username?
                    //create a popup that asks a user for it's username
                    // inflate the layout of the popup window
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.change_username, null);

                    // create the popup window
                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                    USERID = ids.get(position);
                    Button button = popupView.findViewById(R.id.button);
                    TextView text = popupView.findViewById(R.id.usernameInput);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            User user = userTb.findById(USERID);
                            //Insert the new user only if the username is unique
                            if(user != null){
                                userTb.updateName(user.uid,text.getText().toString());
                                list.set(position, text.getText().toString());
                                notifyDataSetChanged();
                            }
                            popupWindow.dismiss();
                        }
                    });
                    // show the popup window
                    // which view you pass in doesn't matter, it is only used for the window tolken
                    popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                }
            });
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Delete the selected username
                    pressedId = ids.get(position);
                    //popup to ask for new username?
                    //create a popup that asks a user for it's username
                    // inflate the layout of the popup window
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.password, null);

                    Button button = popupView.findViewById(R.id.doneButton);
                    TextView text = popupView.findViewById(R.id.passwordAsk);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //checked if the user with id pressedId has the same password as the inputted one
                            if(userTb.findById(pressedId).password.equals(text.getText().toString())){
                                if(USERID == ids.get(position)){
                                    USERID = -1;
                                }
                                userTb.delete(userTb.findByName(list.get(position)));
                                ids.remove(position);
                                list.remove(position);
                                users = userTb.getAll();
                                notifyDataSetChanged();
                            }
                            popupWindow.dismiss();
                        }
                    });

                    // create the popup window
                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    popupWindow = new PopupWindow(popupView, width, height, focusable);
                    // show the popup window
                    // which view you pass in doesn't matter, it is only used for the window tolken
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                }
            });
        }else{
            selectButton.setVisibility(Button.GONE);
            editButton.setVisibility(Button.GONE);
            deleteButton.setVisibility(Button.GONE);
        }

        return viewer;
    }

    public void notifyDataChange(String username){
        try {
            list.add(list.size()-1,username);
            notifyDataSetChanged();
        }catch (Exception ex){}
    }
}
