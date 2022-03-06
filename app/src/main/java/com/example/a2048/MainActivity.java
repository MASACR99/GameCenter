package com.example.a2048;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    private Database db;
    public static int USERID = 0;
    public static ArrayList<Integer> ids;
    public static String UserName = null;
    public static PegScoreDao pegTb;
    public static TwentyScoreDao twentyTb;
    public static UserDao userTb;
    public boolean inMenu = false;
    public CustomAdapter adapterUsers;
    public PopupWindow popupWindow;
    private Context mContext;
    public static List<User> users;
    private MainActivity main;

    public MainActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = this;
        mContext = getApplicationContext();
        setContentView(R.layout.main_menu);
        lv=(ListView) findViewById(R.id.listView);
        //Initialize database
        Database db = Room.databaseBuilder(getApplicationContext(), Database.class, "games-database").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        //get all daos
        pegTb = db.pegDao();
        twentyTb = db.twentyDao();
        userTb = db.userDao();
        /* Uncomment to wipe clean the tables
        for(TwentyScore tw : twentyTb.getAll()){
            twentyTb.delete(tw);
        }
        for(PegScore peg : pegTb.getAll()){
            pegTb.delete(peg);
        }
        for(User usr : userTb.getAll()){
            userTb.delete(usr);
        }*/
        //get default user (the one at position 0 always)
        users = userTb.getAll();
        if(!users.isEmpty()) {
            USERID = users.get(0).uid;
            UserName = users.get(0).username;
        }else{
            USERID = -1;
        }
        //start the menu
        menu0();
    }

    private void menu0(){
        String[] value = {"Choose game","See scores","Users"};
        inMenu = false;
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1,android.R.id.text1,value);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                switch(position){
                    case 0:
                        //Change adapter for one to choose between 2048 or Peg solitaire
                        String[] value = new String[]{"2048","Peg solitaire","Go back"};
                        inMenu = true;
                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1,android.R.id.text1,value);
                        lv.setAdapter(adapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                                    long arg3) {
                                switch(position){
                                    case 0:
                                        //load 2048 (aka SplashTFE)
                                        if(users.isEmpty() || USERID == -1){
                                            Toast.makeText(getApplicationContext(),"No user made or selected!", Toast.LENGTH_LONG).show();
                                        }else {
                                            Intent intent1 = new Intent();
                                            intent1.setClass(getApplicationContext(), GameSetup.class);
                                            startActivity(intent1);
                                        }
                                        break;
                                    case 1:
                                        //load PegSolitaire (aka SplashPeg)
                                        if(users.isEmpty() || USERID == -1){
                                            Toast.makeText(getApplicationContext(),"No user made or selected!", Toast.LENGTH_LONG).show();
                                        }else {
                                            Intent intent2 = new Intent();
                                            intent2.setClass(getApplicationContext(), PegSolitaireSetup.class);
                                            startActivity(intent2);
                                        }
                                        break;
                                    default:
                                        //Go back
                                        menu0(); //This method is sketchy AF, im probably gonna end up redoing it
                                        break;
                                }
                            }
                        });
                        break;
                    case 1:
                        //Will load a scores menu
                        //with one for each game
                        //one global showing top 5 of each
                        //and one menu that lets you search by your user (the selected user)
                        inMenu = true;
                        scoresMenu();
                        break;
                    case 2:
                        //Settings, this will just have a menu to select a user or add one
                        //open a list of usernames (maybe even ask for passwords), and a + button on
                        //the bottom right to add new users
                        inMenu = true;
                        List<User> userList = userTb.getAll();

                        ids = new ArrayList<>();
                        String[] valu = new String[userList.size()+1];
                        if(valu.length > 1) {
                            for (int i = 0; i < valu.length-1; i++) {
                                ids.add(userList.get(i).uid);
                                valu[i] = userList.get(i).username;
                            }
                        }
                        valu[valu.length - 1] = "Go back";
                        ArrayList<String> list = new ArrayList<String>(Arrays.asList(valu));
                        adapterUsers = new CustomAdapter(list, mContext,true,main);
                        lv.setAdapter(adapterUsers);
                        //ArrayAdapter<String> adapte=new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1,android.R.id.text1,valu);
                        //lv.setAdapter(adapte);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                                    long arg3) {
                                menu0();
                            }
                        });
                        break;
                    default:
                        //Something went either completely fucking sideways or idk what is going on
                        break;
                }
            }
        });
    }

    private void scoresMenu(){
        String[] values = new String[]{"2048 scores", "Peg solitaire scores", "Global scores", "My scores", "Go back"};
        ArrayAdapter<String> adapters=new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1,android.R.id.text1,values);
        lv.setAdapter(adapters);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                String[] values;
                ArrayAdapter<String> adapters;
                switch(position){
                    case 0:
                        values = new String[]{"3x3", "4x4", "5x5", "Global scores", "Go back"};
                        adapters=new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1,android.R.id.text1,values);
                        lv.setAdapter(adapters);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                                switch(position){
                                    case 0:
                                        load2048size(3);
                                        break;
                                    case 1:
                                        load2048size(4);
                                        break;
                                    case 2:
                                        load2048size(5);
                                        break;
                                    case 3:
                                        String values[];
                                        ArrayAdapter<String> adapters;
                                        List<TwentyScore> top2048;
                                        top2048 = twentyTb.returnTopScores(10);
                                        values = new String[top2048.size()+1];
                                        if(values.length > 1) {
                                            for (int i = 0; i < values.length-1; i++) {
                                                int user = top2048.get(i).user;
                                                values[i] = "User: " + userTb.findById(user).username + " Score: " + top2048.get(i).score;
                                            }
                                        }
                                        values[values.length - 1] = "Go back";
                                        adapters=new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1,android.R.id.text1,values);
                                        lv.setAdapter(adapters);
                                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                                                    long arg3) {
                                                switch(position){
                                                    default:
                                                        //Go back
                                                        scoresMenu(); //This method is sketchy AF, im probably gonna end up redoing it
                                                        break;
                                                }
                                            }
                                        });
                                        break;
                                    case 4:
                                        scoresMenu();
                                        break;
                                    default:
                                        scoresMenu();
                                        break;
                                }
                            }
                        });
                        break;
                    case 1:
                        //TODO: The user will select between the board types or global
                        values = new String[]{"French board", "German board", "English board", "Diamond board", "Global", "Go back"};
                        adapters=new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1,android.R.id.text1,values);
                        lv.setAdapter(adapters);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                                switch(position){
                                    case 0:
                                        loadPegBoard("French");
                                        break;
                                    case 1:
                                        loadPegBoard("German");
                                        break;
                                    case 2:
                                        loadPegBoard("English");
                                        break;
                                    case 3:
                                        loadPegBoard("Diamond");
                                        break;
                                    case 4:
                                        //load the top 10 scores of peg solitarire
                                        String values[];
                                        ArrayAdapter<String> adapters;
                                        List<PegScore> topPeg;
                                        topPeg = pegTb.returnTopScores(10);
                                        values = new String[topPeg.size()+1];
                                        if(values.length > 1) {
                                            for (int i = 0; i < values.length-1; i++) {
                                                int user = topPeg.get(i).user;
                                                values[i] = "User: " + userTb.findById(user).username + " Time: " + getTimeInString(topPeg.get(i).time);
                                            }
                                        }
                                        values[values.length - 1] = "Go back";
                                        adapters=new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1,android.R.id.text1,values);
                                        lv.setAdapter(adapters);
                                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                                                    long arg3) {
                                                switch(position){
                                                    default:
                                                        //Go back
                                                        scoresMenu(); //This method is sketchy AF, im probably gonna end up redoing it
                                                        break;
                                                }
                                            }
                                        });
                                        break;
                                    case 5:
                                        scoresMenu();
                                        break;
                                    default:
                                        scoresMenu();
                                        break;
                                }
                            }
                        });
                        break;
                    case 2:
                        //load the top 5 scores of 2048 and peg solitaire
                        int user;
                        List<TwentyScore> top52048 = twentyTb.returnTopScores(5);
                        List<PegScore> top5Peg = pegTb.returnTopScores(5);
                        String[] values1 = new String[1 + top52048.size()];
                        values1[0] = "Top 5 2048 scores";
                        if(top52048.size() != 0){
                            for(int i = 0; i < values1.length-1; i++){
                                user = top52048.get(i).user;
                                values1[i+1] = "User: " + userTb.findById(user).username + " Score: " + top52048.get(i).score;
                            }
                        }else{
                            values1 = new String[]{values1[0],"None"};
                        }
                        String[] values2 = new String[1 +top5Peg.size()];
                        values2[0] = "Top 5 peg solitaire scores";
                        if(top5Peg.size() != 0){
                            for(int i = 0; i < values2.length-1; i++){
                                user = top5Peg.get(i).user;
                                values2[i+1] = "User: " + userTb.findById(user).username + " Time: " + getTimeInString(top5Peg.get(i).time);
                            }
                        }else{
                            values2 = new String[]{values2[0],"None"};
                        }
                        values = new String[values1.length+values2.length+1];
                        values[values.length-1] = "Go back";
                        if(top52048.size() != 0 || top5Peg.size() != 0) {
                            for (int i = 0; i < values.length - 1; i++) {
                                if(i < values1.length) {
                                    values[i] = values1[i];
                                }else{
                                    values[i] = values2[i- values1.length];
                                }
                            }
                        }
                        adapters=new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1,android.R.id.text1,values);
                        lv.setAdapter(adapters);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                                    long arg3) {
                                switch(position){
                                    default:
                                        //Go back
                                        scoresMenu(); //This method is sketchy AF, im probably gonna end up redoing it
                                        break;
                                }
                            }
                        });
                        break;
                    case 3:
                        List<TwentyScore> top2048User = twentyTb.returnUserScores(USERID);
                        List<PegScore> topPegUser = pegTb.returnUserScores(USERID);
                        String data[] = new String[top2048User.size() + topPegUser.size() + 1];
                        for (int i = 0; i < data.length-1; i++) {
                            if(i < top2048User.size()){
                                data[i] = "Score: " + top2048User.get(i).score + " Time: " + getTimeInString(top2048User.get(i).time) +
                                        " Size: " + top2048User.get(i).size+"x"+top2048User.get(i).size + " Target score: "
                                        + top2048User.get(i).maxScore;
                            }else{
                                data[i] = "Time: " + getTimeInString(topPegUser.get(i-top2048User.size()).time) + " Modality: " + topPegUser.get(i-top2048User.size()).modality;
                            }
                        }
                        data[data.length-1] = "Go back";
                        adapters=new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1,android.R.id.text1,data);
                        lv.setAdapter(adapters);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                                    long arg3) {
                                switch(position){
                                    default:
                                        //Go back
                                        scoresMenu(); //This method is sketchy AF, im probably gonna end up redoing it
                                        break;
                                }
                            }
                        });
                        break;
                    default:
                        //Go back
                        menu0();
                        break;
                }
            }
        });
    }

    private void load2048size(int size){
        String values[];
        ArrayAdapter<String> adapters;
        List<TwentyScore> top2048;
        top2048 = twentyTb.returnTopBySize(10,size);
        values = new String[top2048.size()+1];
        if(values.length > 1) {
            for (int i = 0; i < values.length-1; i++) {
                int user = top2048.get(i).user;
                values[i] = "User: " + userTb.findById(user).username + " Score: " + top2048.get(i).score;
            }
        }
        values[values.length - 1] = "Go back";
        adapters=new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1,android.R.id.text1,values);
        lv.setAdapter(adapters);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                switch(position){
                    default:
                        //Go back
                        scoresMenu(); //This method is sketchy AF, im probably gonna end up redoing it
                        break;
                }
            }
        });
    }

    private void loadPegBoard(String board){
        String values[];
        ArrayAdapter<String> adapters;
        List<PegScore> topPeg;
        topPeg = pegTb.returnTopByModality(10,board);
        values = new String[topPeg.size()+1];
        if(values.length > 1) {
            for (int i = 0; i < values.length-1; i++) {
                int user = topPeg.get(i).user;
                values[i] = "User: " + userTb.findById(user).username + " Time: " + getTimeInString(topPeg.get(i).time);
            }
        }
        values[values.length - 1] = "Go back";
        adapters=new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1,android.R.id.text1,values);
        lv.setAdapter(adapters);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                switch(position){
                    default:
                        //Go back
                        scoresMenu(); //This method is sketchy AF, im probably gonna end up redoing it
                        break;
                }
            }
        });
    }

    private String getTimeInString(long time){
        int millis = (int) (time%1000);
        int secs = (int) ((time/1000)%60);
        int minutes = (int) ((time/1000)/60);


        // Format the seconds into hours, minutes,
        // and seconds.
        String timeString
                = String
                .format(Locale.getDefault(),
                        "%d:%02d:%02d",
                        minutes, secs, millis);
        return timeString;
    }

    public void onAddUserPress(View view){
        //create a popup that asks a user for it's username
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.add_user_popup, null);

        Button button = popupView.findViewById(R.id.button);
        TextView text = popupView.findViewById(R.id.usernameInput);
        TextView password = popupView.findViewById(R.id.userPasswordInput);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = userTb.findByName(text.getText().toString());
                //Insert the new user only if the username is unique
                if(user == null && !text.getText().toString().equals("") && !password.getText().toString().equals("")){
                    userTb.insertAll(new User(text.getText().toString(),password.getText().toString()));
                    users = userTb.getAll();
                    USERID = users.get(users.size()-1).uid;
                    ids.add(USERID);
                    Toast.makeText(getApplicationContext(), "Selected user " + userTb.findById(USERID).username, Toast.LENGTH_SHORT).show();
                    adapterUsers.notifyDataChange(users.get(users.size()-1).username);
                }else if(user == null){
                    Toast.makeText(getApplicationContext(), "Could not create user, username already exists or any of the inputs are empty", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        Log.d("Je", "onBackPressed Called");
        if(inMenu){
            menu0();
        }else{
            finish();
        }
    }
}