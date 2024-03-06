package com.repro.reladomo.abstractimplementsinterface;
import com.gs.fw.finder.Operation;
import java.util.*;
public class UserList extends UserListAbstract
{
	public UserList()
	{
		super();
	}

	public UserList(int initialSize)
	{
		super(initialSize);
	}

	public UserList(Collection c)
	{
		super(c);
	}

	public UserList(Operation operation)
	{
		super(operation);
	}
}
