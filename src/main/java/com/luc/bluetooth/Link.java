package com.luc.bluetooth;

import org.simpleframework.xml.Element;

public class Link
{
	// ----------------------------
	// Attributes
	// ----------------------------
    @Element(name="action",required = false)
	private String action;

    @Element(name="verb",required = false)
	private String verb;

    @Element(name="url",required = false)
	private String url;

	// ----------------------------
	// Constructor
	// ----------------------------

	public Link( )
	{
		super( );
	}

	public Link( String action, String url, String verb )
	{
		super( );
		this.action = action;
		this.url = url;
		this.verb = verb;
	}

	// ----------------------------
	// Methods
	// ----------------------------

	public String getAction( )
	{
		return action;
	}

	public void setAction( String action )
	{
		this.action = action;
	}

	public String getUrl( )
	{
		return url;
	}

	public void setUrl( String url )
	{
		this.url = url;
	}

	public String getVerb( )
	{
		return verb;
	}

	public void setVerb( String verb )
	{
		this.verb = verb;
	}
}