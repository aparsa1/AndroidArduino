package com.luc.bluetooth;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;




public class BookRepresentation
{
	// ----------------------------
	// Attributes
	// ----------------------------
    @Element(required = false)
	private String name;

    @Element(required = false)
	private String author;

    @Element(required = false)
	private Integer isbn;

    @Element(required = false)
	private String type;

    @Element(required = false)
	private Double price;

	// ----------------------------
	// Constructor
	// ----------------------------
	public BookRepresentation( )
	{

	}

	// ----------------------------
	// Methods
	// ----------------------------

	public String getName( )
	{
		return name;
	}

	public void setName( String name )
	{
		this.name = name;
	}

	public String getAuthor( )
	{
		return author;
	}

	public void setAuthor( String author )
	{
		this.author = author;
	}

	public Integer getIsbn( )
	{
		return isbn;
	}

	public void setIsbn( Integer isbn )
	{
		this.isbn = isbn;
	}

	public Double getPrice( )
	{
		return price;
	}

	public void setPrice( Double price )
	{
		this.price = price;
	}

	public String getType( )
	{
		return type;
	}

	public void setType( String type )
	{
		this.type = type;
	}
}