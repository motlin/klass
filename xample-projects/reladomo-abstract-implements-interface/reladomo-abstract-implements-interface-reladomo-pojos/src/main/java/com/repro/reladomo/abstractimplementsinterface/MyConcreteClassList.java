package com.repro.reladomo.abstractimplementsinterface;
import com.gs.fw.finder.Operation;
import java.util.*;
public class MyConcreteClassList extends MyConcreteClassListAbstract
{
	public MyConcreteClassList()
	{
		super();
	}

	public MyConcreteClassList(int initialSize)
	{
		super(initialSize);
	}

	public MyConcreteClassList(Collection c)
	{
		super(c);
	}

	public MyConcreteClassList(Operation operation)
	{
		super(operation);
	}
}
