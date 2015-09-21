package com.luc.bluetooth;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;


@Root(name="BooksRequestedRepresentation")
public class BooksRequestedRepresentation
{

    @ElementList(entry="books",inline=true)
	private List< BookRepresentation > books;

    @ElementList(entry="links",inline=true)
    protected List< Link > links;


    public List< Link > getLinks( )
    {
        return links;
    }

    public Link getLinkByAction(String action)
    {
        for(Link l : links)
        {
            if (l.getAction().equals(action))
                return l;
        }
        return null;
    }
    public void setLinks( List< Link > links )
    {
        if ( null != links )
        {
            this.links = links;
        }
    }
	public BooksRequestedRepresentation( )
	{
		super( );
	}

	// ----------------------------
	// Methods
	// ----------------------------

	public List< BookRepresentation > getBooks( )
	{
		return books;
	}

	public void setBooks( List< BookRepresentation > books )
	{
		this.books = books;
	}
}