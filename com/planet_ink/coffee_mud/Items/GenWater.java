package com.planet_ink.coffee_mud.Items;

import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.common.*;
import com.planet_ink.coffee_mud.utils.*;
import java.util.*;

public class GenWater extends StdDrink
{
	protected String	readableText="";
	public GenWater()
	{
		super();
		myID=this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
		name="a generic puddle of water";
		baseEnvStats.setWeight(2);
		displayText="a generic puddle of water sits here.";
		description="Looks like a puddle";
		baseGoldValue=5;
		capacity=0;
		amountOfThirstQuenched=250;
		amountOfLiquidHeld=2000;
		amountOfLiquidRemaining=2000;
		setMaterial(EnvResource.RESOURCE_LEATHER);
		recoverEnvStats();
	}

	public Environmental newInstance()
	{
		return new GenWater();
	}
	public boolean isGeneric(){return true;}

	public String text()
	{
		return Generic.getPropertiesStr(this,false);
	}
	public int liquidType(){
		if((material()&EnvResource.MATERIAL_MASK)==EnvResource.MATERIAL_LIQUID)
			return material();
		if(Util.s_int(readableText)==0) return EnvResource.RESOURCE_FRESHWATER;
		return Util.s_int(readableText);
	}
	public void setLiquidType(int newLiquidType){readableText=""+newLiquidType;}

	public String readableText(){return readableText;}
	public void setReadableText(String text){readableText=text;}
	public void setMiscText(String newText)
	{
		miscText="";
		Generic.setPropertiesStr(this,newText,false);
		recoverEnvStats();
	}
}
