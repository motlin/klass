package com.repro.reladomo.abstractimplementsinterface;
import com.gs.fw.finder.Operation;
import java.util.*;
public class MyAbstractClassList extends MyAbstractClassListAbstract
{
	public MyAbstractClassList()
	{
		super();
	}

	public MyAbstractClassList(int initialSize)
	{
		super(initialSize);
	}

	public MyAbstractClassList(Collection c)
	{
		super(c);
	}

	public MyAbstractClassList(Operation operation)
	{
		super(operation);
	}
}
