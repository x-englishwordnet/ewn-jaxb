package org.ewn.jaxb;

import javax.xml.bind.Unmarshaller;

public abstract class Base extends Unmarshaller.Listener
{
	protected Object parent;

	public void afterUnmarshal(Unmarshaller unmarshaller, Object parent)
	{
		this.parent = parent;
	}

	public Object getParent()
	{
		return parent;
	}
}