package com.planet_ink.coffee_mud.core.collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Vector;
/*
Copyright 2000-2010 Bo Zimmerman

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
public class FilteredEnumeration<K> implements Enumeration<K>
{
	private Enumeration<K>  enumer;
	private Filterer<K> 	filterer;
	private K 				nextElement = null;
	private boolean 		initialized = false;

	public FilteredEnumeration(Enumeration<K> eset, Filterer<K> fil) 
    {
		enumer=eset;
		filterer=fil;
    }
    
	public void setFilterer(Filterer<K> fil) 
    {
		filterer=fil;
    }

	private void stageNextElement()
	{
		nextElement = null;
		while((nextElement==null) && (enumer.hasMoreElements()))
		{
			nextElement = enumer.nextElement();
			if(filterer.equals(nextElement))
				return;
		}
	}
	
	private void initialize()
	{
		if(!initialized)
		{
			stageNextElement();
			initialized=true;
		}
	}
	
    public boolean hasMoreElements() 
    { 
    	if(!initialized)
    		initialize();
    	return nextElement!=null;
    }
    
    public K nextElement() 
    {
    	if(!hasMoreElements())
    		throw new NoSuchElementException();
    	K element = nextElement;
    	stageNextElement();
    	return element;
    }
}