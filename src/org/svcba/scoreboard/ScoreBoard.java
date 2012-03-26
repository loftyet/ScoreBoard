package org.svcba.scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.svcba.scoreboard.dialog.AddPlayerActivity;
import org.svcba.scoreboard.model.Game;
import org.svcba.scoreboard.model.Roster;
import org.svcba.scoreboard.model.Team;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

public class ScoreBoard extends TabActivity {
	
	private Roster _roster;
	private Game _game;
	
	private String user = "";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
       
        Bundle extra = getIntent().getExtras();
        user = extra.getString(Login.USER);
        
        SVCBAApp app = (SVCBAApp)getApplicationContext();
        _roster = app.getRoster();
        _game = app.getGame();
        
        initData(user);
        
        Resources res = getResources();
        
        // a little hint about using the app
        Toast.makeText(getApplicationContext(),res.getString(R.string.select_player_help),Toast.LENGTH_LONG).show();
        
        Spinner spin = (Spinner)findViewById(R.id.spin_hometeam);
        ArrayAdapter<CharSequence> adapter;
        if (user.equalsIgnoreCase("svcba")){
          adapter = ArrayAdapter.createFromResource(this, R.array.team_name_array, android.R.layout.simple_spinner_item);
        } else
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.test_team_name_array, android.R.layout.simple_spinner_item);
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setOnItemSelectedListener(new OnItemSelectedListener(){
        	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
        	{
        		Team team = _roster.getTeamAt(pos);
        		_game.setHomeTeam(team);
                GridView gv = (GridView)findViewById(R.id.grid_hometeam_starting_lineup);
                //SimpleAdapter sa = new SimpleAdapter(parent.getContext(), _game.getHomeTeam().getOnCourt(), R.layout.starting_lineup, new String[]{"name", "avator"}, new int[]{R.id.name, R.id.img});
                SimpleAdapter sa = new SimpleAdapter(parent.getContext(), _game.getHomeTeam().getOnCourt(), R.layout.starting_lineup, new String[]{"name"}, new int[]{R.id.name});
                //Log.d("Scoreboard : home:", String.valueOf( _game.getAwayTeam().getOnCourt().size()));
                
                gv.setAdapter(sa);
            	ListView lv = (ListView)findViewById(R.id.list_hometeam_player);
                //sa = new SimpleAdapter(parent.getContext(), _game.getHomeTeam().getOffCourt(),R.layout.list_player,new String[]{"name","avator"},new int[]{R.id.list_player_name,R.id.list_player_img});
                sa = new SimpleAdapter(parent.getContext(), _game.getHomeTeam().getOffCourt(),R.layout.list_player,new String[]{"name"},new int[]{R.id.list_player_name});
                lv.setAdapter(sa);
        	}
        	public void onNothingSelected(AdapterView<?> parent)
        	{
        		
        	}
        });
        spin.setAdapter(adapter);
        
        spin = (Spinner)findViewById(R.id.spin_awayteam);
        if (user.equalsIgnoreCase("svcba")){
        	adapter = ArrayAdapter.createFromResource(this, R.array.team_name_array, android.R.layout.simple_spinner_item);
        } else {
         	adapter = ArrayAdapter.createFromResource(this, R.array.test_team_name_array, android.R.layout.simple_spinner_item);
        }
        	
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(new OnItemSelectedListener(){
        	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
        	{
        		Team team = _roster.getTeamAt(pos);
        		_game.setAwayTeam(team);
                GridView gv = (GridView)findViewById(R.id.grid_awayteam_starting_lineup);
                //SimpleAdapter sa = new SimpleAdapter(parent.getContext(), _game.getAwayTeam().getOnCourt(), R.layout.starting_lineup, new String[]{"name", "avator"}, new int[]{R.id.name, R.id.img});
                SimpleAdapter sa = new SimpleAdapter(parent.getContext(), _game.getAwayTeam().getOnCourt(), R.layout.starting_lineup, new String[]{"name"}, new int[]{R.id.name});
                
                gv.setAdapter(sa);
            	ListView lv = (ListView)findViewById(R.id.list_awayteam_player);
                //sa = new SimpleAdapter(parent.getContext(), _game.getAwayTeam().getOffCourt(),R.layout.list_player,new String[]{"name","avator"},new int[]{R.id.list_player_name,R.id.list_player_img});
            	sa = new SimpleAdapter(parent.getContext(), _game.getAwayTeam().getOffCourt(),R.layout.list_player,new String[]{"name"},new int[]{R.id.list_player_name});
                lv.setAdapter(sa);
        	}
        	public void onNothingSelected(AdapterView<?> parent)
        	{
        		
        	}
        });
        
        spin = (Spinner)findViewById(R.id.spin_hometeam_color);
        adapter = ArrayAdapter.createFromResource(this, R.array.team_color_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setSelection(0);
        spin.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
			{
				if (pos == 0)
					_game.setHomeColor(Game.COLOR_DARK);
				else if (pos == 1)
					_game.setHomeColor(Game.COLOR_WHITE);
			}

			public void onNothingSelected(AdapterView<?> arg0)
			{
				
			}
        	
        });
        
        spin = (Spinner)findViewById(R.id.spin_awayteam_color);
        adapter = ArrayAdapter.createFromResource(this, R.array.team_color_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setSelection(1);
        spin.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
			{
				if (pos == 0)
					_game.setAwayColor(Game.COLOR_DARK);
				else if (pos == 1)
					_game.setAwayColor(Game.COLOR_WHITE);
			}

			public void onNothingSelected(AdapterView<?> arg0)
			{
				
			}
        	
        });

        GridView gridview = (GridView)findViewById(R.id.grid_hometeam_starting_lineup);
        //SimpleAdapter sa = new SimpleAdapter(this, _game.getHomeTeam().getOnCourt(), R.layout.starting_lineup, new String[]{"name", "avator"}, new int[]{R.id.name, R.id.img});
        SimpleAdapter sa = new SimpleAdapter(this, _game.getHomeTeam().getOnCourt(), R.layout.starting_lineup, new String[]{"name"}, new int[]{R.id.name});
        gridview.setAdapter(sa);
        gridview.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) 
			{
				_game.getHomeTeam().moveFromCourt(pos);
                GridView gv = (GridView)findViewById(R.id.grid_hometeam_starting_lineup);
                //SimpleAdapter sa = new SimpleAdapter(parent.getContext(), _game.getHomeTeam().getOnCourt(), R.layout.starting_lineup, new String[]{"name", "avator"}, new int[]{R.id.name, R.id.img});
                SimpleAdapter sa = new SimpleAdapter(parent.getContext(), _game.getHomeTeam().getOnCourt(), R.layout.starting_lineup, new String[]{"name"}, new int[]{R.id.name});
                
                gv.setAdapter(sa);
            	ListView lv = (ListView)findViewById(R.id.list_hometeam_player);
                //sa = new SimpleAdapter(parent.getContext(), _game.getHomeTeam().getOffCourt(),R.layout.list_player,new String[]{"name","avator"},new int[]{R.id.list_player_name,R.id.list_player_img});
                sa = new SimpleAdapter(parent.getContext(), _game.getHomeTeam().getOffCourt(),R.layout.list_player,new String[]{"name"},new int[]{R.id.list_player_name});
                
                lv.setAdapter(sa);
			}
        	
        });
        
        ListView listview = (ListView)findViewById(R.id.list_hometeam_player);
        //sa = new SimpleAdapter(this, _game.getHomeTeam().getOffCourt(),R.layout.list_player,new String[]{"name","avator"},new int[]{R.id.list_player_name,R.id.list_player_img});
        sa = new SimpleAdapter(this, _game.getHomeTeam().getOffCourt(),R.layout.list_player,new String[]{"name"},new int[]{R.id.list_player_name});
        
        listview.setAdapter(sa);
        listview.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) 
			{
				Team team = _game.getHomeTeam();
				team.moveToCourt(pos);
                GridView gv = (GridView)findViewById(R.id.grid_hometeam_starting_lineup);
                //SimpleAdapter sa = new SimpleAdapter(parent.getContext(), _game.getHomeTeam().getOnCourt(), R.layout.starting_lineup, new String[]{"name", "avator"}, new int[]{R.id.name, R.id.img});
                SimpleAdapter sa = new SimpleAdapter(parent.getContext(), _game.getHomeTeam().getOnCourt(), R.layout.starting_lineup, new String[]{"name"}, new int[]{R.id.name});
                
                gv.setAdapter(sa);
            	ListView lv = (ListView)findViewById(R.id.list_hometeam_player);
                //sa = new SimpleAdapter(parent.getContext(), _game.getHomeTeam().getOffCourt(),R.layout.list_player,new String[]{"name","avator"},new int[]{R.id.list_player_name,R.id.list_player_img});
                sa = new SimpleAdapter(parent.getContext(), _game.getHomeTeam().getOffCourt(),R.layout.list_player,new String[]{"name"},new int[]{R.id.list_player_name});
                
                lv.setAdapter(sa);
			}
        });

        gridview = (GridView)findViewById(R.id.grid_awayteam_starting_lineup);
        //sa = new SimpleAdapter(this, _game.getAwayTeam().getOnCourt(), R.layout.starting_lineup, new String[]{"name", "avator"}, new int[]{R.id.name, R.id.img});
        sa = new SimpleAdapter(this, _game.getAwayTeam().getOnCourt(), R.layout.starting_lineup, new String[]{"name"}, new int[]{R.id.name});
        
        gridview.setAdapter(sa);
        gridview.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) 
			{
				_game.getAwayTeam().moveFromCourt(pos);
                GridView gv = (GridView)findViewById(R.id.grid_awayteam_starting_lineup);
                //SimpleAdapter sa = new SimpleAdapter(parent.getContext(), _game.getAwayTeam().getOnCourt(), R.layout.starting_lineup, new String[]{"name", "avator"}, new int[]{R.id.name, R.id.img});
                SimpleAdapter sa = new SimpleAdapter(parent.getContext(), _game.getAwayTeam().getOnCourt(), R.layout.starting_lineup, new String[]{"name"}, new int[]{R.id.name});
                
                gv.setAdapter(sa);
            	ListView lv = (ListView)findViewById(R.id.list_awayteam_player);
                //sa = new SimpleAdapter(parent.getContext(), _game.getAwayTeam().getOffCourt(),R.layout.list_player,new String[]{"name","avator"},new int[]{R.id.list_player_name,R.id.list_player_img});
                sa = new SimpleAdapter(parent.getContext(), _game.getAwayTeam().getOffCourt(),R.layout.list_player,new String[]{"name"},new int[]{R.id.list_player_name});
                
                lv.setAdapter(sa);
			}
        	
        });
        
        listview = (ListView)findViewById(R.id.list_awayteam_player);
//        sa = new SimpleAdapter(this, _game.getAwayTeam().getOffCourt(),R.layout.list_player,new String[]{"name","avator"},new int[]{R.id.list_player_name,R.id.list_player_img});
        sa = new SimpleAdapter(this, _game.getAwayTeam().getOffCourt(),R.layout.list_player,new String[]{"name"},new int[]{R.id.list_player_name});

        listview.setAdapter(sa);
        listview.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) 
			{
				Team team = _game.getAwayTeam();
				team.moveToCourt(pos);
                GridView gv = (GridView)findViewById(R.id.grid_awayteam_starting_lineup);
                //SimpleAdapter sa = new SimpleAdapter(parent.getContext(), _game.getAwayTeam().getOnCourt(), R.layout.starting_lineup, new String[]{"name", "avator"}, new int[]{R.id.name, R.id.img});
                SimpleAdapter sa = new SimpleAdapter(parent.getContext(), _game.getAwayTeam().getOnCourt(), R.layout.starting_lineup, new String[]{"name"}, new int[]{R.id.name});
                
                gv.setAdapter(sa);
            	ListView lv = (ListView)findViewById(R.id.list_awayteam_player);
                //sa = new SimpleAdapter(parent.getContext(), _game.getAwayTeam().getOffCourt(),R.layout.list_player,new String[]{"name","avator"},new int[]{R.id.list_player_name,R.id.list_player_img});
                sa = new SimpleAdapter(parent.getContext(), _game.getAwayTeam().getOffCourt(),R.layout.list_player,new String[]{"name"},new int[]{R.id.list_player_name});
                
                lv.setAdapter(sa);
			}
        });
        
        TabHost tabhost = getTabHost();
        tabhost.addTab(tabhost.newTabSpec("tab_1").setIndicator(res.getText(R.string.hometeam),res.getDrawable(R.drawable.ic_tab_home)).setContent(R.id.tab_hometeam));
        tabhost.addTab(tabhost.newTabSpec("tab_2").setIndicator(res.getText(R.string.awayteam),res.getDrawable(R.drawable.ic_tab_away)).setContent(R.id.tab_awayteam));
        tabhost.addTab(tabhost.newTabSpec("tab_3").setIndicator(res.getText(R.string.game),res.getDrawable(R.drawable.ic_tab_game)).setContent(R.id.tab_game));
        
        Button button = (Button)findViewById(R.id.btn_setup_done);
        button.setOnClickListener(new OnClickListener(){

			public void onClick(View view)
			{
				if (_game.getHomeColor() == _game.getAwayColor())
				{
					showError(view, R.string.err_same_color);
					return ;
				}
				if (_game.getHomeTeam() == _game.getAwayTeam())
				{
					showError(view, R.string.err_same_team);
					return ;
				}
				Team team = _game.getHomeTeam();
				for (Map<String, Object> p : team.getOnCourt())
				{
					if (p.get("number").equals(-1))
					{
						showError(view, R.string.err_hometeam_starting_lineup_not_full);
						return ;
					}
				}
				team = _game.getAwayTeam();
				for (Map<String, Object> p : team.getOnCourt())
				{
					if (p.get("number").equals(-1))
					{
						showError(view, R.string.err_awayteam_starting_lineup_not_full);
						return ;
					}
				}
				Intent intent = new Intent(view.getContext(),ReadyActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
				
			}
        	private void showError(View view, int message)
        	{
        		Toast.makeText(view.getContext(), message, Toast.LENGTH_LONG).show();
        	}
        });
        
       
    }
    
    protected void onResume()
    {
    	super.onResume();
    	
        ListView listview = (ListView)findViewById(R.id.list_hometeam_player);
//        SimpleAdapter sa = new SimpleAdapter(this, _game.getHomeTeam().getOffCourt(),R.layout.list_player,new String[]{"name","avator"},new int[]{R.id.list_player_name,R.id.list_player_img});
        SimpleAdapter sa = new SimpleAdapter(this, _game.getHomeTeam().getOffCourt(),R.layout.list_player,new String[]{"name"},new int[]{R.id.list_player_name});

        listview.setAdapter(sa);
    	
        listview = (ListView)findViewById(R.id.list_awayteam_player);
        //sa = new SimpleAdapter(this, _game.getAwayTeam().getOffCourt(),R.layout.list_player,new String[]{"name","avator"},new int[]{R.id.list_player_name,R.id.list_player_img});
        sa = new SimpleAdapter(this, _game.getAwayTeam().getOffCourt(),R.layout.list_player,new String[]{"name"},new int[]{R.id.list_player_name});
        
        listview.setAdapter(sa);
        
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.menu_quit:
        	//System.exit(0);
            finish();
        	return true;
        case R.id.menu_add_player:
        	Intent intent = new Intent(this, AddPlayerActivity.class);
        	startActivity(intent);
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    private void initData(String user)
    {
    	
    	Resources res = getResources();
    	
    	String teams[] = res.getStringArray(R.array.team_name_array);
    	
    	String[] players0 = res.getStringArray(R.array.ShanghaiJiaoDa);
    	String[] players1 = res.getStringArray(R.array.DaChengLvSuo);
    	String[] players2 = res.getStringArray(R.array.ILG);
    	String[] players3 = res.getStringArray(R.array.TaiJiaoDa);
    	String[] players4 = res.getStringArray(R.array.Tri_City_A);
    	String[] players5 = res.getStringArray(R.array.Wolfpack);
    	String[] players6 = res.getStringArray(R.array.Stanford);
    	String[] players7 = res.getStringArray(R.array.QingLong);
    	String[] players8 = res.getStringArray(R.array.Xuanwu);
    	String[] players9 = res.getStringArray(R.array.No9);
    	String[] players10 = res.getStringArray(R.array.YoungBlood);
    	String[] players11 = res.getStringArray(R.array.Kubila);
    	String[] players12 = res.getStringArray(R.array.Tri_City_Dragon);
    	String[] players13 = res.getStringArray(R.array.Weiming);
    	String[] players14 = res.getStringArray(R.array.Gladiators);
    	String[] players15 = res.getStringArray(R.array.Titans);
    	/*
    	List<String[]> player_list = new ArrayList<String[]>();
    	player_list.add(players0);
    	player_list.add(players1);
    	player_list.add(players2);
    	player_list.add(players3);
    	player_list.add(players4);
    	player_list.add(players5);
    	player_list.add(players6);
    	player_list.add(players7);
    	player_list.add(players8);
    	player_list.add(players9);
    	player_list.add(players10);
    	player_list.add(players11);
    	player_list.add(players12);
    	player_list.add(players13);
    	player_list.add(players14);
    	player_list.add(players15);
    	
        Map<String, String[]> svcba_players = new HashMap<String,String[]>();
    	for(int i = 0; i<teams.length;i++){
    		svcba_players.put(teams[i], player_list.get(i));
    	}
    	
    	Team team;
    	if (user.equalsIgnoreCase("svcba")){
    		 Iterator<Map.Entry<String,String[]>> it = svcba_players.entrySet().iterator();
    		 while(it.hasNext()){
    			 team = new Team();
        		 Map.Entry<String, String[]> pairs = (Map.Entry<String, String[]>)it.next();
    			 team.setName(pairs.getKey());
    			 String[] current_players = pairs.getValue();
    			 for(String current_player:current_players){
    				 Map<String, Object> player = new HashMap<String, Object>();
    		    		player.put("name", current_player);
    		    		//player.put("number", i+1);
    		    		//player.put("avator", R.drawable.avator);
    		    		team.addPlayer(player);
    			 }
    			 _roster.addTeam(team);
    		     
    		 }
    		 
    		 // To set up the game
    		 team = new Team();
    		 it = svcba_players.entrySet().iterator();
    		 Map.Entry<String, String[]> pairs = (Map.Entry<String, String[]>)it.next();
			 team.setName(pairs.getKey());
			 String[] current_players = pairs.getValue();
			 for(String current_player:current_players){
				 Map<String, Object> player = new HashMap<String, Object>();
		    		player.put("name", current_player);
		    		team.addPlayer(player);
			 }
    		 _game.setHomeTeam(team);
		     _game.setAwayTeam(team);
    		
    	} else {
    		team = new Team();
    		team.setName(res.getStringArray(R.array.test_team_name_array)[0]);
	    	String[] players = res.getStringArray(R.array.knicks);
	    	for(int i = 0; i < players.length; i++)
	    	{
	    		Map<String, Object> player = new HashMap<String, Object>();
	    		player.put("name", players[i]);
	    		player.put("number", i+1);
	    		player.put("avator", R.drawable.avator);
	    		team.addPlayer(player);
	    	}
	    	_roster.addTeam(team);
	    	_game.setHomeTeam(team);
	    	_game.setAwayTeam(team);
	    	
	    	team = new Team();
	    	team.setName(res.getStringArray(R.array.test_team_name_array)[1]);
	    	players = res.getStringArray(R.array.spurs);
	    	for(int i = 0; i < players.length; i++)
	    	{
	    		Map<String, Object> player = new HashMap<String, Object>();
	    		player.put("name", players[i]);
	    		player.put("number", i+1);
	    		player.put("avator", R.drawable.avator);
	    		team.addPlayer(player);
	    	}
	    	_roster.addTeam(team);
    	}
    	*/
    	
    	if (user.equalsIgnoreCase("svcba")){
	    	//team.setName(res.getStringArray(R.array.team_name_array)[0]);
    		Team team = new Team();
	    	team.setName(teams[0]);
	    	//String[] players = res.getStringArray(R.array.ShanghaiJiaoDa);
	    	String[] players = players0;
	    	for(int i = 0; i < players.length; i++)
	    	{
	    		Map<String, Object> player = new HashMap<String, Object>();
	    		player.put("name", players[i]);
	    		player.put("number", i+1);
	    		player.put("avator", R.drawable.avator);
	    		team.addPlayer(player);
	    	}
	    	_roster.addTeam(team);
	    	_game.setHomeTeam(team);
	    	_game.setAwayTeam(team);
	    	
	    	team = new Team();
	    	team.setName(teams[1]);
	    	
	    	//team.setName(res.getStringArray(R.array.team_name_array)[1]);
	    	//players = res.getStringArray(R.array.DaChengLvSuo);
	    	players = players1;
	    	
	    	for(int i = 0; i < players.length; i++)
	    	{
	    		Map<String, Object> player = new HashMap<String, Object>();
	    		player.put("name", players[i]);
	    		player.put("number", i+1);
	    		player.put("avator", R.drawable.avator);
	    		team.addPlayer(player);
	    	}
	    	_roster.addTeam(team);
	    	
	    	team = new Team();
	    	team.setName(teams[2]);
	    	//team.setName(res.getStringArray(R.array.team_name_array)[2]);
	    	players = players2;
	    	//players = res.getStringArray(R.array.ILG);
	    	for(int i = 0; i < players.length; i++)
	    	{
	    		Map<String, Object> player = new HashMap<String, Object>();
	    		player.put("name", players[i]);
	    		player.put("number", i+1);
	    		player.put("avator", R.drawable.avator);
	    		team.addPlayer(player);
	    	}
	    	_roster.addTeam(team);
	
	    	team = new Team();
	    	team.setName(teams[3]);
	    	//team.setName(res.getStringArray(R.array.team_name_array)[3]);
	    	players = players3;
	    	//players = res.getStringArray(R.array.TaiJiaoDa);
	    	for(int i = 0; i < players.length; i++)
	    	{
	    		Map<String, Object> player = new HashMap<String, Object>();
	    		player.put("name", players[i]);
	    		player.put("number", i+1);
	    		player.put("avator", R.drawable.avator);
	    		team.addPlayer(player);
	    	}
	    	_roster.addTeam(team);
	    	
	    	
	    	team = new Team();
	    	team.setName(teams[4]);
	    	//team.setName(res.getStringArray(R.array.team_name_array)[4]);
	    	players = players4;
	    	for(int i = 0; i < players.length; i++)
	    	{
	    		Map<String, Object> player = new HashMap<String, Object>();
	    		player.put("name", players[i]);
	    		player.put("number", i+1);
	    		player.put("avator", R.drawable.avator);
	    		team.addPlayer(player);
	    	}
	    	_roster.addTeam(team);
	
	    	team = new Team();
	    	team.setName(teams[5]);
	    	//team.setName(res.getStringArray(R.array.team_name_array)[5]);
	    	//players = res.getStringArray(R.array.berkeley_player);
	    	players = players5;
	    	for(int i = 0; i < players.length; i++)
	    	{
	    		Map<String, Object> player = new HashMap<String, Object>();
	    		player.put("name", players[i]);
	    		player.put("number", i+1);
	    		player.put("avator", R.drawable.avator);
	    		team.addPlayer(player);
	    	}
	    	_roster.addTeam(team);
	    	
	    	team = new Team();
	    	team.setName(teams[6]);
	    	//team.setName(res.getStringArray(R.array.team_name_array)[6]);
	    	//players = res.getStringArray(R.array.sjsu_player);
	    	players = players6;
	    	for(int i = 0; i < players.length; i++)
	    	{
	    		Map<String, Object> player = new HashMap<String, Object>();
	    		player.put("name", players[i]);
	    		player.put("number", i+1);
	    		player.put("avator", R.drawable.avator);
	    		team.addPlayer(player);
	    	}
	    	_roster.addTeam(team);
	    	
	    	team = new Team();
	    	team.setName(teams[7]);
	    	//team.setName(res.getStringArray(R.array.team_name_array)[7]);
	    	//players = res.getStringArray(R.array.titan_player);
	    	players = players7;
	    	for(int i = 0; i < players.length; i++)
	    	{
	    		Map<String, Object> player = new HashMap<String, Object>();
	    		player.put("name", players[i]);
	    		player.put("number", i+1);
	    		player.put("avator", R.drawable.avator);
	    		team.addPlayer(player);
	    	}
	    	_roster.addTeam(team);
	    	
	    	team = new Team();
	    	team.setName(teams[8]);
	    	//team.setName(res.getStringArray(R.array.team_name_array)[8]);
	    	//players = res.getStringArray(R.array.ShanghaiJiaoDa);
	    	players = players8;
	    	for(int i = 0; i < players.length; i++)
	    	{
	    		Map<String, Object> player = new HashMap<String, Object>();
	    		player.put("name", players[i]);
	    		player.put("number", i+1);
	    		player.put("avator", R.drawable.avator);
	    		team.addPlayer(player);
	    	}
	    	_roster.addTeam(team);
	    	
	    	team = new Team();
	    	team.setName(teams[9]);
	    	//team.setName(res.getStringArray(R.array.team_name_array)[9]);
	    	//players = res.getStringArray(R.array.DaChengLvSuo);
	    	players = players9;
	    	for(int i = 0; i < players.length; i++)
	    	{
	    		Map<String, Object> player = new HashMap<String, Object>();
	    		player.put("name", players[i]);
	    		player.put("number", i+1);
	    		player.put("avator", R.drawable.avator);
	    		team.addPlayer(player);
	    	}
	    	_roster.addTeam(team);
	    	
	    	team = new Team();
	    	team.setName(teams[10]);
	    	//team.setName(res.getStringArray(R.array.team_name_array)[10]);
	    	//players = res.getStringArray(R.array.DaChengLvSuo);
	    	players = players10;
	    	for(int i = 0; i < players.length; i++)
	    	{
	    		Map<String, Object> player = new HashMap<String, Object>();
	    		player.put("name", players[i]);
	    		player.put("number", i+1);
	    		player.put("avator", R.drawable.avator);
	    		team.addPlayer(player);
	    	}
	    	_roster.addTeam(team);
	
	    	team = new Team();
	    	team.setName(teams[11]);
	    	//team.setName(res.getStringArray(R.array.team_name_array)[11]);
	    	//players = res.getStringArray(R.array.DaChengLvSuo);
	    	players = players11;
	    	for(int i = 0; i < players.length; i++)
	    	{
	    		Map<String, Object> player = new HashMap<String, Object>();
	    		player.put("name", players[i]);
	    		player.put("number", i+1);
	    		player.put("avator", R.drawable.avator);
	    		team.addPlayer(player);
	    	}
	    	_roster.addTeam(team);
	    	
	    	
	    	team = new Team();
	    	team.setName(teams[12]);
	    	//team.setName(res.getStringArray(R.array.team_name_array)[12]);
	    	players = players12;
	    	for(int i = 0; i < players.length; i++)
	    	{
	    		Map<String, Object> player = new HashMap<String, Object>();
	    		player.put("name", players[i]);
	    		player.put("number", i+1);
	    		player.put("avator", R.drawable.avator);
	    		team.addPlayer(player);
	    	}
	    	_roster.addTeam(team);
	
	    	team = new Team();
	    	team.setName(teams[13]);
	    	//team.setName(res.getStringArray(R.array.team_name_array)[13]);
	    	//players = res.getStringArray(R.array.berkeley_player);
	    	players = players13;
	    	for(int i = 0; i < players.length; i++)
	    	{
	    		Map<String, Object> player = new HashMap<String, Object>();
	    		player.put("name", players[i]);
	    		player.put("number", i+1);
	    		player.put("avator", R.drawable.avator);
	    		team.addPlayer(player);
	    	}
	    	_roster.addTeam(team);
	    	
	    	team = new Team();
	    	team.setName(teams[14]);
	    	//team.setName(res.getStringArray(R.array.team_name_array)[14]);
	    	//players = res.getStringArray(R.array.sjsu_player);
	    	players = players14;
	    	for(int i = 0; i < players.length; i++)
	    	{
	    		Map<String, Object> player = new HashMap<String, Object>();
	    		player.put("name", players[i]);
	    		player.put("number", i+1);
	    		player.put("avator", R.drawable.avator);
	    		team.addPlayer(player);
	    	}
	    	_roster.addTeam(team);
	    	
	    	team = new Team();
	    	team.setName(teams[15]);
	    	//team.setName(res.getStringArray(R.array.team_name_array)[15]);
	    	//players = res.getStringArray(R.array.titan_player);
	    	players = players15;
	    	for(int i = 0; i < players.length; i++)
	    	{
	    		Map<String, Object> player = new HashMap<String, Object>();
	    		player.put("name", players[i]);
	    		player.put("number", i+1);
	    		player.put("avator", R.drawable.avator);
	    		team.addPlayer(player);
	    	}
	    	_roster.addTeam(team);
	    	
    	} else if (user.equalsIgnoreCase("guest"))
    	{
    		Team team = new Team();
    		//Log.d("Scoreboard","get here?");
    		team.setName(res.getStringArray(R.array.test_team_name_array)[0]);
	    	String[] players = res.getStringArray(R.array.knicks);
	    	for(int i = 0; i < players.length; i++)
	    	{
	    		Map<String, Object> player = new HashMap<String, Object>();
	    		player.put("name", players[i]);
	    		player.put("number", i+1);
	    		player.put("avator", R.drawable.avator);
	    		team.addPlayer(player);
	    	}
	    	_roster.addTeam(team);
	    	_game.setHomeTeam(team);
	    	_game.setAwayTeam(team);
	    	
	    	team = new Team();
	    	team.setName(res.getStringArray(R.array.test_team_name_array)[1]);
	    	players = res.getStringArray(R.array.spurs);
	    	for(int i = 0; i < players.length; i++)
	    	{
	    		Map<String, Object> player = new HashMap<String, Object>();
	    		player.put("name", players[i]);
	    		player.put("number", i+1);
	    		player.put("avator", R.drawable.avator);
	    		team.addPlayer(player);
	    	}
	    	_roster.addTeam(team);
    	} else {
    		Log.d("","how could you get here?");
    	}
    		
    	
    }
    
}