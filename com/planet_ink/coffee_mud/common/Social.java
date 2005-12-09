package com.planet_ink.coffee_mud.common;

import java.util.*;
import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.utils.*;

// requires nothing to load
/* 
   Copyright 2000-2005 Bo Zimmerman

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
public class Social implements Environmental
{
	private String Social_name;
	private String You_see;
	private String Third_party_sees;
	private String Target_sees;
	private String See_when_no_target;
    private String MSPfile="";
	private int sourceCode=CMMsg.MSG_OK_ACTION;
	private int othersCode=CMMsg.MSG_OK_ACTION;
	private int targetCode=CMMsg.MSG_OK_ACTION;

	public String ID() { return "Social"; }
	public String name(){ return Social_name;}
	public String Name(){return name();}
	public void setName(String newName){Social_name=newName;}
	public String You_see(){return You_see;};
	public String Third_party_sees(){return Third_party_sees;};
	public String Target_sees(){return Target_sees;};
	public String See_when_no_target(){return See_when_no_target;};
	public int sourceCode(){return sourceCode;}
	public int othersCode(){return othersCode;}
	public int targetCode(){return targetCode;}
	public void setYou_see(String str){You_see=str;};
	public void setThird_party_sees(String str){Third_party_sees=str;};
	public void setTarget_sees(String str){Target_sees=str;};
	public void setSee_when_no_target(String str){See_when_no_target=str;};
	public void setSourceCode(int code){sourceCode=code;}
	public void setOthersCode(int code){othersCode=code;}
	public void setTargetCode(int code){targetCode=code;}
	public boolean targetable(){return name().endsWith(" <T-NAME>");}
	public long getTickStatus(){return Tickable.STATUS_NOT;}
    public String MSPfile(){return MSPfile;}
    public void setMSPfile(String newFile){MSPfile=newFile;}

	public boolean invoke(MOB mob,
						  Vector commands,
						  Environmental target,
						  boolean auto)
	{
		String targetStr="";
		if((commands.size()>1)
        &&(!((String)commands.elementAt(1)).equalsIgnoreCase("SELF"))
        &&(!((String)commands.elementAt(1)).equalsIgnoreCase("ALL")))
			targetStr=(String)commands.elementAt(1);

		Environmental Target=target;
		if(Target==null)
			Target=mob.location().fetchFromRoomFavorMOBs(null,targetStr,Item.WORN_REQ_ANY);
		if((Target!=null)&&(!Sense.canBeSeenBy(Target,mob)))
		   Target=null;

        String mspFile=((MSPfile!=null)&&(MSPfile.length()>0))?CommonStrings.msp(MSPfile,10):"";
        
		String You_see=You_see();
		if((You_see!=null)&&(You_see.trim().length()==0)) 
            You_see=null;
        
		String Third_party_sees=Third_party_sees();
		if((Third_party_sees!=null)&&(Third_party_sees.trim().length()==0)) 
            Third_party_sees=null;
        
		String Target_sees=Target_sees();
		if((Target_sees!=null)&&(Target_sees.trim().length()==0)) Target_sees=null;
        
		String See_when_no_target=See_when_no_target();
		if((See_when_no_target!=null)&&(See_when_no_target.trim().length()==0)) 
            See_when_no_target=null;
        
		if((Target==null)&&(targetable()))
		{
			FullMsg msg=new FullMsg(mob,null,this,(auto?CMMsg.MASK_GENERAL:0)|sourceCode(),See_when_no_target,CMMsg.NO_EFFECT,null,CMMsg.NO_EFFECT,null);
			if(mob.location().okMessage(mob,msg))
				mob.location().send(mob,msg);
		}
		else
		if(Target==null)
		{
			FullMsg msg=new FullMsg(mob,null,this,(auto?CMMsg.MASK_GENERAL:0)|sourceCode(),(You_see==null)?null:You_see+mspFile,CMMsg.NO_EFFECT,null,othersCode(),(Third_party_sees==null)?null:Third_party_sees+mspFile);
			if(mob.location().okMessage(mob,msg))
				mob.location().send(mob,msg);
		}
		else
		{
			FullMsg msg=new FullMsg(mob,Target,this,(auto?CMMsg.MASK_GENERAL:0)|sourceCode(),(You_see==null)?null:You_see+mspFile,targetCode(),(Target_sees==null)?null:Target_sees+mspFile,othersCode(),(Third_party_sees==null)?null:Third_party_sees+mspFile);
			if(mob.location().okMessage(mob,msg))
			{
				mob.location().send(mob,msg);
				if(target instanceof MOB)
				{
					MOB tmob=(MOB)target;
					if((name().toUpperCase().startsWith("SMILE"))
					&&(mob.charStats().getStat(CharStats.CHARISMA)>=16)
					&&(mob.charStats().getMyRace().ID().equals(tmob.charStats().getMyRace().ID()))
					&&(Dice.rollPercentage()==1)
					&&(mob.charStats().getStat(CharStats.GENDER)!=('N'))
					&&(tmob.charStats().getStat(CharStats.GENDER)!=('N'))
					&&(mob.charStats().getStat(CharStats.GENDER)!=tmob.charStats().getStat(CharStats.GENDER)))
					{
						Ability A=CMClass.getAbility("Disease_Smiles");
						if((A!=null)&&(target.fetchEffect(A.ID())==null))
							A.invoke(tmob,tmob,true,0);
					}
				}
			}
		}
		return true;
	}

	public CMMsg makeChannelMsg(MOB mob,
								int channelInt,
								String channelName,
								Vector commands,
								boolean makeTarget)
	{
		String targetStr="";
		if((commands.size()>1)
        &&(!((String)commands.elementAt(1)).equalsIgnoreCase("SELF"))
        &&(!((String)commands.elementAt(1)).equalsIgnoreCase("ALL")))
			targetStr=(String)commands.elementAt(1);
		Environmental Target=null;
		if((Target==null)&&(targetStr.length()>0))
		{
		    String targetMud="";
		    if(targetStr.indexOf("@")>0)
		        targetMud=targetStr.substring(targetStr.indexOf("@")+1);
		    else
				Target=CMMap.getPlayer(targetStr);
			
			if(((Target==null)&&(makeTarget))||((targetMud.length()>0)&&((CMClass.I3Interface().i3online())&&(CMClass.I3Interface().isI3channel(channelName)))))
			{
				Target=CMClass.getMOB("StdMOB");
				Target.setName(targetStr);
				((MOB)Target).setLocation(CMMap.getRandomRoom());
			}
			if((Target!=null)&&(!Sense.isSeen(Target)))
			   Target=null;
		}

        String mspFile=((MSPfile!=null)&&(MSPfile.length()>0))?CommonStrings.msp(MSPfile,10):"";
        String You_see=You_see();
        if((You_see!=null)&&(You_see.trim().length()==0)) 
            You_see=null;
        
        String Third_party_sees=Third_party_sees();
        if((Third_party_sees!=null)&&(Third_party_sees.trim().length()==0)) 
            Third_party_sees=null;
        
        String Target_sees=Target_sees();
        if((Target_sees!=null)&&(Target_sees.trim().length()==0)) Target_sees=null;
        
		String See_when_no_target=See_when_no_target();
		if((See_when_no_target!=null)&&(See_when_no_target.trim().length()==0)) 
            See_when_no_target=null;
        
		FullMsg msg=null;
		String str=makeTarget?"":"^Q^<CHANNEL \""+channelName+"\"^>["+channelName+"] ";
		String end=makeTarget?"":"^</CHANNEL^>^?^.";
        if(end.length()==0) mspFile="";
		if((Target==null)&&(targetable()))
			msg=new FullMsg(mob,null,this,CMMsg.MASK_CHANNEL|sourceCode(),str+See_when_no_target+end,CMMsg.NO_EFFECT,null,CMMsg.NO_EFFECT,null);
		else
		if(Target==null)
			msg=new FullMsg(mob,null,this,CMMsg.MASK_CHANNEL|sourceCode(),str+You_see+end+mspFile,CMMsg.NO_EFFECT,null,CMMsg.MASK_CHANNEL|(CMMsg.TYP_CHANNEL+channelInt),str+Third_party_sees+end+mspFile);
		else
			msg=new FullMsg(mob,Target,this,CMMsg.MASK_CHANNEL|sourceCode(),str+You_see+end+mspFile,CMMsg.MASK_CHANNEL|(CMMsg.TYP_CHANNEL+channelInt),str+Target_sees+end+mspFile,CMMsg.MASK_CHANNEL|(CMMsg.TYP_CHANNEL+channelInt),str+Third_party_sees+end+mspFile);
		return msg;
	}

	public String description(){return "";}
	public void setDescription(String str){}
	public String displayText(){return "";}
	public void setDisplayText(String str){}
	protected static final EnvStats envStats=(EnvStats)CMClass.getShared("DefaultEnvStats");
	public EnvStats envStats(){return envStats;}
	public EnvStats baseEnvStats(){return envStats;}

	public void recoverEnvStats(){}
	public void setBaseEnvStats(EnvStats newBaseEnvStats){}
	public CMObject newInstance()
	{ return new Social();}
	public int compareTo(Object o){ return CMClass.classID(this).compareToIgnoreCase(CMClass.classID(o));}

	private static final String[] CODES={"CLASS","NAME"};
	public String[] getStatCodes(){return CODES;}
	private int getCodeNum(String code){
		for(int i=0;i<CODES.length;i++)
			if(code.equalsIgnoreCase(CODES[i])) return i;
		return -1;
	}
	public String getStat(String code){
		switch(getCodeNum(code))
		{
		case 0: return ID();
		case 1: return name();
		}
		return "";
	}
	public void setStat(String code, String val)
	{
		switch(getCodeNum(code))
		{
		case 0: return;
		case 1: setName(val); break;
		}
	}
	public boolean sameAs(Environmental E)
	{
		if(!(E instanceof Social)) return false;
		String name=Social_name.toUpperCase().trim();
		if(name.indexOf(" ")>=0)
			name=name.substring(0,name.indexOf(" ")+1);
		if(((Social)E).Social_name.toUpperCase().startsWith(name))
			return true;
		if((((Social)E).Social_name.toUpperCase().equals(name.trim())))
		   return true;
		return false;
	}
	private void cloneFix(Social E){}

	public CMObject copyOf()
	{
		try
		{
			Social E=(Social)this.clone();
			E.cloneFix(this);
			return E;

		}
		catch(CloneNotSupportedException e)
		{
			return this.newInstance();
		}
	}
	public void setMiscText(String newMiscText){}
	public String text(){return "";}

	public void affectEnvStats(Environmental affected, EnvStats affectableStats)	{}
	public void affectCharStats(MOB affectedMob, CharStats affectableStats)	{}
	public void affectCharState(MOB affectedMob, CharState affectableMaxState)	{}
	public void executeMsg(Environmental myHost, CMMsg msg){}
	public boolean okMessage(Environmental myHost, CMMsg msg){	return true;}
	public boolean tick(Tickable ticking, int tickID)	{ return true;	}
	public int maxRange(){return Integer.MAX_VALUE;}
	public int minRange(){return 0;}

	public String image(){return "";}
    public String rawImage(){return "";}
	public void setImage(String newImage){}
	public void addEffect(Ability to){}
	public void addNonUninvokableEffect(Ability to){}
	public void delEffect(Ability to){}
	public int numEffects(){ return 0;}
	public Ability fetchEffect(int index){return null;}
	public Ability fetchEffect(String ID){return null;}
	public void addBehavior(Behavior to){}
	public void delBehavior(Behavior to){}
	public int numBehaviors(){return 0;}
	public Behavior fetchBehavior(int index){return null;}
	public Behavior fetchBehavior(String ID){return null;}
	public boolean isGeneric(){return false;}
}
