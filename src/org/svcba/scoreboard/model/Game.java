package org.svcba.scoreboard.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.svcba.scoreboard.NormalActivity;

import android.widget.Toast;

public class Game implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final public int GAME_NORMAL = 0;
	static final public int GAME_PAUSE = 1;
	static final public int GAME_OVER = 2;
	static final public int COLOR_DARK = 1;
	static final public int COLOR_WHITE = 2;
	static final public int MIN_PER_QTR = 12;
	private Team _hometeam;
	private Team _awayteam;
	private List<Action> _actions;
	private Action _tempaction;
	
	private int _homescore;
	private int _awayscore;
	private int _homefoul;
	private int _awayfoul;
	private int _hometimeout;
	private int _awaytimeout;
	private int _homecolor;
	private int _awaycolor;
	
	private int _quarter;
	private int _ot;
	private int _time;
	private int _timeout;
	private int _status;
	private boolean _ref_timeout;
	
	public Game()
	{
		_homecolor = COLOR_DARK;
		_awaycolor = COLOR_WHITE;
		_actions = new ArrayList<Action>();
		_homescore = 0;
		_awayscore = 0;
		_homefoul = 0;
		_awayfoul = 0;
		_hometimeout = 0;
		_awaytimeout = 0;
		
		_quarter = 1;
		_ot = 0;
		_time = MIN_PER_QTR*60;
		_timeout = 0;
		_status = 0;
		_ref_timeout = false;
	}
	
	public int getHomeColor()
	{
		return _homecolor;
	}
	
	public int getAwayColor()
	{
		return _awaycolor;
	}
	
	public int getHomeScore()
	{
		return _homescore;
	}
	
	public int getAwayScore()
	{
		return _awayscore;
	}
	
	public int getHomeTimeout()
	{
		return _hometimeout;
	}
	
	public int getAwayTimeout()
	{
		return _awaytimeout;
	}
	
	public int getHomeFoul()
	{
		return _homefoul;
	}
	
	public int getAwayFoul()
	{
		return _awayfoul;
	}
	
	public void setHomeTeam(Team team)
	{
		_hometeam = team;
	}
	
	public Team getHomeTeam()
	{
		return _hometeam;
	}
	
	public void setAwayTeam(Team team)
	{
		_awayteam = team;
	}
	
	public Team getAwayTeam()
	{
		return _awayteam;
	}
	
	public List<Action> getActions()
	{
		return _actions;
	}
	
	public void addAction(Action action)
	{
		_actions.add(action);
	}
	
	public void undoAction()
	{
		removeActionFromTheEnd(0);		
	}
	// pos from 0 to size-1, and 0 -> size-1.
	public boolean removeActionFromTheEnd(int pos)
	{
		boolean result = false;   // false means fail.
		
		if (_actions.size()>pos && pos>=0)
		{
			// The display order is the reserve of recording order.
			int arrayIndex = _actions.size()-1-pos;			
			Action action = _actions.get(arrayIndex);
			
			switch (action.getAction())			
			{
				case Action.SHOOT_1:					
					if (action.getResult() == Action.MADE)
					{
						if( action.getSide() == Action.HOME)
						{
							this.addHomeScore(-1);
						}
						else if ( action.getSide() == Action.AWAY)
						{
							this.addAwayScore(-1);
						}
					}
					else if (action.getResult() == Action.MISS)
					{
						//Do nothing
					}
					break;
				case Action.SHOOT_2:
					if (action.getResult() == Action.MADE)
					{
						if( action.getSide() == Action.HOME)
						{
							this.addHomeScore(-2);
						}
						else if ( action.getSide() == Action.AWAY)
						{
							this.addAwayScore(-2);
						}
					}
					else if (action.getResult() == Action.MISS)
					{
						//Do nothing
					}
					break;
				case Action.SHOOT_3:
					if (action.getResult() == Action.MADE)
					{
						if( action.getSide() == Action.HOME)
						{
							this.addHomeScore(-3);
						}
						else if ( action.getSide() == Action.AWAY)
						{
							this.addAwayScore(-3);
						}
					}
					else if (action.getResult() == Action.MISS)
					{
						//Do nothing
					}
					break;
				case Action.TIMEOUT:
					if( action.getSide() == Action.HOME)
					{
						this.addHomeTimeout(-1);
					}
					else if ( action.getSide() == Action.AWAY)
					{
						this.addHomeTimeout(-1);
					}
					break;
				case Action.FOUL:
					// Q: How to deal with Player 's foul??
					// A: for the final stat, it will go through all the action, since the action has been removed the personal data will be correct at that time. 
					// TODO: need to check the 5 fouls stuff, that is also taken care of, since it will go through the action too.
					if( action.getSide() == Action.HOME)
					{
						//p = players.get((String)action.getPlayer().get("name"));
						this.addHomeFoul(-1);
					}
					else if ( action.getSide() == Action.AWAY)
					{
						this.addAwayFoul(-1);
					}
					break;

				default:
					//Do nothing. 
					break;
			}
			_actions.remove(arrayIndex);
			result = true;
		}
		return result;
	}
	
	public void setHomeColor(int color)
	{
		_homecolor = color;
	}
	public void setAwayColor(int color)
	{
		_awaycolor = color;
	}
	
	public void addHomeScore(int score)
	{
		_homescore += score;
	}
	
	public void addAwayScore(int score)
	{
		_awayscore += score;
	}
	
	public void addHomeFoul(int foul)
	{
		_homefoul += foul;
	}
	
	public void addAwayFoul(int foul)
	{
		_awayfoul += foul;
	}
	
	public void addHomeTimeout(int timeout)
	{
		_hometimeout += timeout;
		_status = 1;
		_timeout = 20;
	}
	
	public void addAwayTimeout(int timeout)
	{
		_awaytimeout += timeout;
		_status = 1;
		_timeout = 20;
	}
	
	public void runOneSec()
	{
		if (_ref_timeout)
			return ;
		if (_status == 0)
			_time --;
		else if (_status == 1)
			_timeout --;
		else
			return;
		if (_status == 0 && _time == 0)
		{
			if (_quarter == 1)
			{
				_quarter = 2;
				_time = MIN_PER_QTR * 60;
				_timeout = 30;
				_status = 1;
			}
			else if (_quarter == 2)
			{
				_quarter = 3;
				_time = MIN_PER_QTR*60;
				_timeout = 2*60;
				_status = 1;
				_homefoul = 0;
				_awayfoul = 0;
			}
			else if (_quarter == 3)
			{
				_quarter = 4;
				_time = MIN_PER_QTR*60;
				_timeout = 30;
				_status = 1;
			}
			else if (_quarter == 4 && _ot == 0)
			{
				if (_homescore != _awayscore)
				{
					_status = 2;
					return;
				}
				else
				{
					_ot = 1;
					_time = 2*60;
					_timeout = 30;
					_status = 1;
				}
			}
			else if (_ot > 0)
			{
				_ot ++;
				_time = 2 * 60;
				_timeout = 30;
				_status = 1;
			}
		}
		if (_status == 1 && _timeout == 0)
		{
			_status = 0;
		}
}
	
	public String getCurrentTime()
	{
		String result = "";
		int minute = _time/60;
		if (minute < 10)
		{
			result += "0";
		}
		result += minute;
		result += ":";
		int second = _time%60;
		if (second < 10)
		{
			result += "0";
		}
		result += second;
		return result;
	}
	
	public int getCurrentTimeInt()
	{
		return _time;
	}
	
	public String getCurrentTimeout()
	{
		String result = "";
		int m = _timeout / 60;
		if (m < 10)
		{
			result += "0";
		}
		result += m;
		result += ":";
		int s = _timeout % 60;
		if (s < 10)
		{
			result += "0";
		}
		result += s;
		return result;
	}
	
	public String getCurrentQuarter()
	{
		String result = "";
		if (_ot == 0)
		{
			result += "Q" + _quarter;
		}
		else
		{
			result += "OT" + _ot;
		}
		return result;
	}
	
	public int getCurrentQuarterInt()
	{
		return _quarter + _ot;
	}
	public int getGameStatus()
	{
		return _status;
	}
	public void setTempAction(Action action)
	{
		_tempaction = action;
	}
	public Action getTempAction()
	{
		return _tempaction;
	}
	public List<Action> getLastActions()
	{
		List<Action> result = new ArrayList<Action>();
		int size = _actions.size();
		for (int i = 0; i < size; i ++)
		{
			result.add(_actions.get(size-1-i));
		}
		return result;
	}
	

	public int getFoulTimes(String name)
	{
		int result = 0;
		for (Action action : _actions)
		{
			if (action.getAction() == Action.FOUL && action.getPlayer().get("name").equals(name))
			{
				result ++;
			}
		}
		return result;
	}
	public void setRefTimeout()
	{
		_ref_timeout = true;
	}
	public void unsetRefTimeout()
	{
		_ref_timeout=false;
	}
}
