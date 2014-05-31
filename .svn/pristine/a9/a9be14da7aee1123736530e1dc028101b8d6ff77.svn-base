/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlets;

import db.DbManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

/**
 *
 * @author giorgio
 */
public class RequestDownload extends HttpServlet
{

    private String uploadDir;
    private String storagePath;
    private DbManager manager;
    
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
    
        this.uploadDir = config.getInitParameter("uploadDir");
        if (uploadDir == null)
        {
            throw new ServletException("Please supply uploadDir parameter");
        }
        this.manager = (DbManager)super.getServletContext().getAttribute("dbmanager");
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession(false);
        
        String webInfPath = (String)session.getAttribute("webInfPath");
        Integer groupID = (Integer)session.getAttribute("groupID");
        String fileName = (String)request.getParameter("file");
        String groupName = manager.getGroupName(groupID);
        
        storagePath = webInfPath + "/" + uploadDir + "/" + groupName;
        fileName = storagePath + "/" + fileName;
        
        File f = new File(fileName);
        FileInputStream fis = new FileInputStream(f);
        
        ServletContext context = getServletContext();
        String mimeType = context.getMimeType(fileName);
        if (mimeType == null)
        {
            mimeType = "application/octet-stream";
        }
        
        response.setContentType(mimeType);
        response.setContentLength((int) f.length());
        
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", f.getName());
        response.setHeader(headerKey, headerValue);
        
        OutputStream os = response.getOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
         
        while ((bytesRead = fis.read(buffer)) != -1)
        {
            os.write(buffer, 0, bytesRead);
        }
         
        fis.close();
        os.close();  
    }
}
