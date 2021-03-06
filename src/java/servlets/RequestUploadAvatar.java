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
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;
import support.Utils;

/**
 *
 * @author giorgio
 */
public class RequestUploadAvatar extends HttpServlet
{

    private String uploadDir;
    private String avatarsPath;
    private DbManager manager;
    
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        this.manager = (DbManager)super.getServletContext().getAttribute("dbmanager");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession(false);
        String applicationPath = (String)session.getAttribute("applicationPath");
        String tmpPath = (String)session.getAttribute("tmpPath");
        User user = (User)session.getAttribute("user");
        
        avatarsPath = tmpPath + "/";
        
        MultipartRequest multi = new MultipartRequest(request, avatarsPath, 1024*1024*10, "UTF-8", new DefaultFileRenamePolicy());
        String btn = multi.getParameter("btn");
        
        switch (btn)
        {
            case "avInvia":
                try
                {
                    saveAvatar(multi, user.getUserID(), response);
                }
                catch (SQLException ex)
                {
                    Logger.getLogger(RequestUploadAvatar.class.getName()).log(Level.SEVERE, null, ex);
                }
                response.sendRedirect("LandingPage");
                break;
            case "avAnnulla":
                annulla(multi);
                response.sendRedirect("LandingPage");
                break;
        }
    }
    
    private void saveAvatar(MultipartRequest multi, Integer userID, HttpServletResponse response) throws ServletException, SQLException
    {
        Enumeration files = multi.getFileNames();
        if (files.hasMoreElements())
        {
            String name = (String)files.nextElement();
            String fileName = multi.getOriginalFileName(name);
            String av = "";
            
            File f = new File(avatarsPath + fileName); 
            
            try
            {
                av = Utils.imageToBase64(f, "png");
            }
            catch (IOException e)
            {
                
            }
            manager.updateUserAvatar(userID, av);
            
            f.delete();
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
            
            File f = new File(avatarsPath + fileName);
            f.delete();
        }
    }
}
