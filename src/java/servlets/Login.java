/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlets;

import db.DbManager;
import db.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import support.PHUtils;
import support.Utils;

/**
 *
 * @author giorgio
 */
public class Login extends HttpServlet
{

    private DbManager manager;
    private String className;
    
    @Override
    public void init() throws ServletException
    {
        this.manager = (DbManager)super.getServletContext().getAttribute("dbmanager");
        
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

        String userName = (String)request.getParameter("userName");
        String userPwd = (String)request.getParameter("userPwd");
        String applicationPath;
        String webInfPath;
        String groupsStoragePath;
        String tmpPath;
       
        className = this.getClass().getCanonicalName();
        
        applicationPath = getServletContext().getRealPath("/");
        webInfPath = getServletContext().getRealPath("/WEB-INF");
        groupsStoragePath = getServletContext().getRealPath("/WEB-INF/groupsStorage");
        tmpPath = getServletContext().getRealPath("/WEB-INF/tmp");
        
        Date lastLogin;
        
        User user = null;
        try
        {
            user = manager.authenticate(userName, userPwd);
        }
        catch(SQLException e)
        {
            Utils.toLog(getClass().getName() + ": " + e.getMessage(), "e");
            throw new ServletException(e);
        }
        
        if (user == null)
        {
            try (PrintWriter out = response.getWriter())
            {
                response.setContentType("text/html;charset=UTF-8");
                PHUtils.pageHeader(out);
                
                PHUtils.printAlert(out, request, "Nome utente o password non validi!", "index.html");
                
                PHUtils.pageFooter(out);
                out.close();
            }
        }
        else
        {
            
            lastLogin = getLastLoginFromCookie(request, response, userName);
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("lastLogin", lastLogin);
            session.setAttribute("applicationPath", applicationPath);
            session.setAttribute("webInfPath", webInfPath);
            session.setAttribute("groupsStoragePath", groupsStoragePath);
            session.setAttribute("tmpPath", tmpPath);
           
            try (PrintWriter out = response.getWriter())
            {
                response.setContentType("text/html;charset=UTF-8");
                PHUtils.pageHeader(out);

                out.println("<div class=\"alert alert-success\" align = \"center\">");
                out.println("   Utente <strong>"+ userName +"</strong> connesso con successo.");
                out.println("</div>");
                
                PHUtils.pageFooter(out);
                response.setHeader("refresh", "1; url=\"LandingPage\"");
                //response.sendRedirect("LandingPage");
                out.close();
                
            }
        }
    }
    
    private Date getLastLoginFromCookie(HttpServletRequest request, HttpServletResponse response, String userName)
    {
        Date lastLogin = new Date();
        Cookie[] cookies = request.getCookies();
        Cookie cookie;
        Date today = Calendar.getInstance().getTime();
        
        if (cookies != null) 
        {
            for (int i = 0; i < cookies.length; i++)
            {
                cookie = cookies[i];
                if (cookie.getName().equals(userName))
                {
                    try
                    {
                        lastLogin = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(cookie.getValue());
                    }
                    catch (ParseException e)
                    {
                        Utils.toLog(e.getMessage(), "w");
                    }
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
                else
                {
                    lastLogin = null;
                }
            }

            cookie = new Cookie(userName, Utils.dateToString(today));
            cookie.setMaxAge(86400);
            response.addCookie(cookie);
        }
        return lastLogin;
    }

    private void synchonized(String className)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
