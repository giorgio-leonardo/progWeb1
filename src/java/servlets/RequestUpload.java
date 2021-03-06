/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import db.DbManager;
import db.PostFile;
import db.User;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpSession;
import support.Utils;

/**
 *
 * @author giorgio
 */
public class RequestUpload extends HttpServlet
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession(false);
        String webInfPath = (String)session.getAttribute("webInfPath");
        Integer groupID = (Integer)session.getAttribute("groupID");
        User user = (User)session.getAttribute("user");
        
        String groupName = manager.getGroupName(groupID);
        storagePath = webInfPath + "/" + uploadDir + "/" + groupName;
        
        MultipartRequest multi = new MultipartRequest(request, storagePath, 1024*1024*10, "UTF-8", new DefaultFileRenamePolicy());
        String btn = multi.getParameter("btn");
        
        switch (btn)
        {
            case "npInvia":
                try
                {
                    aggiungiPost(multi, user.getUserID(), groupID, response);
                }
                catch (SQLException e)
                {
                     Utils.toLog(this.getClass().getName() + ": " + e.getMessage(), "e");
                throw new ServletException(e);
                }
                response.sendRedirect("ViewGroup");
                break;
            case "npAnnulla":
                annulla(multi);
                response.sendRedirect("ViewGroup");
                break;
        }
    }
    
    private void aggiungiPost(MultipartRequest multi, Integer userID, Integer groupID, HttpServletResponse response) throws ServletException, SQLException
    {
        String postText = multi.getParameter("postText");
        Integer postID;
        try
        {
            postID = manager.insertPost(groupID, userID, postText);
        }
        catch (SQLException e)
        {
            Utils.toLog(this.getClass().getName() + ": " + e.getMessage(), "e");
            throw new ServletException(e);
        }
        
        ArrayList<PostFile> postFiles = new ArrayList<PostFile>();
        
        Enumeration files = multi.getFileNames();
        while (files.hasMoreElements())
        {
            PostFile postFile = new PostFile();
            
            String name = (String)files.nextElement();
            String fileName = multi.getFilesystemName(name);
            String originalFilename = multi.getOriginalFileName(name);
            String type = multi.getContentType(name);
            
            if (originalFilename != null) 
            {
                postFile.setPostID(postID);
                postFile.setPostFileOriginalFileName(originalFilename);
                postFile.setPostFileFileSystemName(fileName);
                postFile.setPostFileType(type);
                
                postFiles.add(postFile);
            }
        }
        
        if (postFiles.size() > 0 )
        {
            manager.insertPostFiles(postFiles);
        }
    }
    
    private void annulla(MultipartRequest multi)
    {
        Enumeration files = multi.getFileNames();
        while (files.hasMoreElements())
        {
            PostFile postFile = new PostFile();
            
            String name = (String)files.nextElement();
            String fileName = multi.getFilesystemName(name);
            
            File f = new File(storagePath + "/" + fileName);
            f.delete();
        }
    }
}
