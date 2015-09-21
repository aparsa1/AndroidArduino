package com.luc.bluetooth;


import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
/**
 * Created by aziz on 11/26/2014.
 */
public  class CallAPI {

    private static final DefaultHttpClient client = new DefaultHttpClient();


    public static SlaveCommand get (String url)
    {
        SlaveCommand command =new SlaveCommand();

        HttpGet getRequest = new HttpGet(url);

        try {

            HttpResponse getResponse = client.execute(getRequest);


            final int statusCode = getResponse.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {

                //  return statusCode;

            }

            HttpEntity getResponseEntity = getResponse.getEntity();

            if (getResponseEntity != null) {

                String xmlData="";
                try {

                    xmlData = EntityUtils.toString(getResponseEntity);
                     command.setCommand(xmlData);


                }

                catch (Exception e) {

                    return null;
                }

            }

        }

        catch (IOException e) {

            getRequest.abort();
            return null;
        }

        return command;

    }
    public static Object get(String url,Class<?> type ) {

        HttpGet getRequest = new HttpGet(url);

        try {

            HttpResponse getResponse = client.execute(getRequest);


            final int statusCode = getResponse.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {

                //  return statusCode;

            }

            HttpEntity getResponseEntity = getResponse.getEntity();

            if (getResponseEntity != null) {

                String xmlData="";
                try {

                    xmlData = EntityUtils.toString(getResponseEntity);
                    Serializer serializer = new Persister();

                    Reader reader = new StringReader(xmlData);
                    //    if(type.equals(OrderRepresentation.class))
                    //    return xmlData;
                    return  serializer.read(type, reader, false);


                }

                catch (Exception e) {

                    return e.getMessage();
                }

            }

        }

        catch (IOException e) {

            getRequest.abort();
            return e.getMessage();
        }

        return null;

    }

    public static Object post(String url,Class<?> type,Object payload ) {

        HttpPost postRequest = new HttpPost(url);

        try {
            //First convert the object to xml and write it in a file
            Serializer serializer = new Persister();
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File (sdCard.getAbsolutePath());
            dir.mkdirs();
            File result = new File(dir,"payload.xml");
            serializer.write(payload, result);

            //Read xml from file and put it in a variable
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(result));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
            }
            catch (IOException e) {
                //You'll need to add proper error handling here
            }


            //Add the xml data to httpPost
            StringEntity se = new StringEntity(text.toString(), HTTP.UTF_8);
            //  postRequest.addHeader("content-type", "application/json");
            postRequest.setHeader("Content-Type","application/xml;charset=UTF-8");
            postRequest.setEntity(se);
            HttpResponse getResponse = client.execute(postRequest);


            final int statusCode = getResponse.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {

                return null;

            }

            HttpEntity getResponseEntity = getResponse.getEntity();

            if (getResponseEntity != null) {

                String xmlData="";
                try {

                    xmlData = EntityUtils.toString(getResponseEntity);


                    Reader reader = new StringReader(xmlData);
                    //  return xmlData;
                    return  serializer.read(type, reader, false);


                }

                catch (Exception e) {

                    Logger log = Logger.getLogger("my.logger");
                    log.setLevel(Level.ALL);
                    ConsoleHandler handler = new ConsoleHandler();
                    handler.setLevel(Level.ALL);
                    handler.setFormatter(new SimpleFormatter());
                    log.addHandler(handler);
                    log.fine(e.getMessage()); }

            }

        }

        catch (IOException e) {

            postRequest.abort();
            return e.getMessage();
        } catch (Exception e) {
            //  e.printStackTrace();
            return e.getMessage();
        }

        return null;

    }



    public static Object delete(String url,Class<?> type ) {

        HttpDelete getRequest = new HttpDelete(url);

        try {

            HttpResponse getResponse = client.execute(getRequest);


            final int statusCode = getResponse.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {

                //  return statusCode;

            }

            HttpEntity getResponseEntity = getResponse.getEntity();

            if (getResponseEntity != null) {

                String xmlData="";
                try {

                    xmlData = EntityUtils.toString(getResponseEntity);
                    Serializer serializer = new Persister();

                    Reader reader = new StringReader(xmlData);
                    // return xmlData;
                    return  serializer.read(type, reader, false);


                }

                catch (Exception e) {
                    return e.getMessage();

                }

            }

        }

        catch (IOException e) {

            getRequest.abort();
            return e.getMessage();
        }

        return null;

    }

}
