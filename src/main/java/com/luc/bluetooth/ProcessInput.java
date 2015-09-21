package com.luc.bluetooth;

import java.text.ParseException;

/**
 * Created by aziz on 4/2/15.
 */
public  class ProcessInput {

  public static String processTemperature(String input)
  {   String result="";
        result=input.substring(1);
      return result;
  }
    public static String processDistance(String input)
    {
        return input.substring(1).toString().trim()+" cm";

    }
   public static int processLight(String input)
   { int temp=0;
       try{
           temp=Integer.parseInt(input.substring(1).toString().trim());
       }
       catch (Exception e)
       {

       }
       return temp;
   }
}
