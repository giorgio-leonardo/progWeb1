/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package support;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServlet;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import sun.misc.BASE64Encoder;

/**
 *
 * @author giorgio
 */
public class Utils extends HttpServlet
{
    private static final org.apache.log4j.Logger log = Logger.getLogger(Utils.class);
    public static final String gridBgColor = "#c0c0c0";
      
    public static String dateToString(Date date)
    {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        if (date != null)
            return df.format(date);
        else
            return null;
    }
    
    public static String timeStampToString(Timestamp ts)
    {
        String s = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(ts);
        return s;
    }
 
    public static void toLog(String message, String level)
    {
        level = level.toLowerCase();
        PropertyConfigurator.configure(Utils.class.getClassLoader().getResource("support/log4j.properties"));
        
        switch(level)
        {
            case "f":
                log.fatal(message);
                break;
            case "e":
                log.error(message);
                break;
            case "w":
                log.warn(message);
                break;
            case "i":
                log.info(message);
                break;
            case "d":
                log.debug(message);
                break;
        }
    }
  
    public static void modifyGroupFolder(String path, String oldName, String newName)
    {
        JOptionPane.showMessageDialog(null, path + "/" + oldName);
        JOptionPane.showMessageDialog(null, path + "/" + newName);
        
        File of = new File(path + "/" + oldName);
        File nf = new File(path + "/" + newName);
            
        of.renameTo(nf);
    }
    
    public static void createGroupFolder(String path, String groupName)
    {
        File dir = new File(path + "/" + groupName);
        dir.mkdir();
    }
    
    public static String clobToString(Clob cl) throws SQLException 
    {
        Reader stream = cl.getCharacterStream();
        BufferedReader reader = new BufferedReader(stream);
        StringBuffer sb = new StringBuffer();
        String line = null;
        try
        {
            while ((line=reader.readLine())!=null)
            {
                sb.append(line);
            }
            stream.close();
        }
        catch (IOException ex)
        {
            java.util.logging.Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
   
        if (sb.length()>0)
        {
            return sb.toString();
        }
   
        return "";
    }
  
    public static String removeHTML(String s)
    {
        String noHTMLString = s.replaceAll("\\<.*?\\>", "");
        
        noHTMLString = noHTMLString.replaceAll("r", "<br/>");
        noHTMLString = s.replaceAll("n", " ");
        noHTMLString = s.replaceAll("'", "&#39;");
        noHTMLString = s.replaceAll("\"", "&quot;");
        return noHTMLString;
    }
    
    public static String parseNewLine(String s)
    {
        StringBuffer sb = new StringBuffer(s);
        try
        {
        
            int c1 = 0;
            int c2 = 0;
            for (int i = 0; i < sb.length() - 2; i++)
            {
                c1 = (int)sb.charAt(i);
                c2 = (int)sb.charAt(i+1);
                if (c1 == 13 && c2 == 10)
                    sb.replace(i, i + 2, "<br>");
            }
            Utils.toLog("info", sb.toString());
        
        }
        catch (Exception e)
        {
            Utils.toLog("info", e.getLocalizedMessage());
        }
        return sb.toString();
        
    }
    
    public static String parseLinks(String string)
    {
        String string1 = removeHTML(string);
        String string2 = parseNewLine(string1);
        
        StringBuffer sb = new StringBuffer(string2);
        String openHref = "<a href=\"http://";
        String closeHref = "\">";
        String closeA = "</a>";
        String tmp = "";
        
        Boolean primo = false;
        
        int s = 0;
        int e = 0;
        
        for (int i = 0; i < sb.length(); i++)
        {
            if ( (sb.charAt(i) == '$') && (sb.charAt(i + 1) == '$') )
            {
                if (!primo)
                {
                    s = i + openHref.length();
                    sb.replace(i, i + 2, openHref);
                    primo = true;
                }
                else
                {
                    e = i;
                    sb.replace(i, i + 2, closeHref);
                    primo = false;
                    tmp = sb.substring(s, e);
                    sb.insert(e + 2, tmp);
                    sb.insert((e + 2) + tmp.length(), closeA);
                }
            }
        }
        
        return sb.toString();
    }
    
    public static String imageToBase64(File f, String type) throws IOException
    {
        String ret = null;
        BufferedImage img = ImageIO.read(f);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        
        try
        {
            ImageIO.write(img, type, bos);
            byte[] bosByte = bos.toByteArray();
            
            BASE64Encoder encoder = new BASE64Encoder();
            ret = encoder.encode(bosByte);
            bos.close();
        }
        catch (IOException e)
        {
            toLog("Utils.imageToBase64()\n\t" + e.getLocalizedMessage(), "e");
        }
        
        return ret;
    }
}
