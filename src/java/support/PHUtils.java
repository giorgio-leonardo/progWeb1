/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package support;

import db.DbManager;
import db.Invitation;
import db.PostFile;
import db.User;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static support.Utils.gridBgColor;

/**
 *
 * @author giorgio
 */
public class PHUtils
{
     public static void pageHeader(PrintWriter out)
    {
        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"it\">");
        out.println("   <head>");
        out.println("   <div class=\"row\">");
        out.println("       <div class=\"col-md-1\" style=\"background-color:#ffffff; border:0px\"></div>");
        out.println("       <div class=\"col-md-10\" style=\"background-color:"+ gridBgColor + "\">");
        out.println("           <meta charset=\"utf-8\">");
        out.println("           <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">");
        out.println("           <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        out.println("           <meta name=\"description\" content=\"\">");
        out.println("           <meta name=\"author\" content=\"\">");
        out.println("           <link rel=\"shortcut icon\" href=\"docs-assets/ico/favicon.png\">");
        out.println("           <title>Programmazione web. Primo progetto.</title>");
        out.println("           <link href=\"dist/css/bootstrap.css\" rel=\"stylesheet\">");
        out.println("           <link href=\"dist/css/bootstrap-theme.min.css\" rel=\"stylesheet\">");
        out.println("           <link href=\"dist/css/theme.css\" rel=\"stylesheet\">");
        out.println("           <link href=\"dist/css/grid.css\" rel=\"stylesheet\">");
        out.println("           <div class=\"page-header\">");
        out.println("               <h1 align=\"center\">");
        out.println("                   Introduzione alla programmazione per il Web<br>");
        out.println("                   <small>Primo progetto</small>");
        out.println("               </h1>");
        out.println("           </div>");
        out.println("   </head>");
    }
    
    public static void printAlertNoRedir(PrintWriter out, HttpServletRequest request, String message)
    {
        out.println("<div class=\"alert alert-danger\" align = \"center\">");
        out.println("   <strong>Errore!</strong> " + message + ".");
        out.println("</div>");
    }
    
    public static void printAlert(PrintWriter out, HttpServletRequest request, String message, String redirect)
    {
        out.println("<meta HTTP-EQUIV=\"REFRESH\" content=\"1; url=" + request.getContextPath() + "/" + redirect + "\">");
        out.println("<div class=\"alert alert-danger\" align = \"center\">");
        out.println("   <strong>Errore!</strong> " + message + ".");
        out.println("</div>");
    }
    
    public static void printInviesTable(PrintWriter out, HttpServletRequest request, HttpServletResponse response, DbManager manager, Integer userID) throws ServletException
    {
        ArrayList<Invitation> invitations = new ArrayList<Invitation>();
        
        try
        {
            invitations = manager.getInvitationsForUser(userID);
        }
        catch (SQLException e)
        {
            Utils.toLog(e.getMessage(), "e");
            throw new ServletException(e);
        }

        out.println("<div class=\"row\" style=\"background-color:" + Utils.gridBgColor + "; border:0px\">");
        out.println("   <div class=\"col-md-10\" style=\"background-color:" + Utils.gridBgColor + "; border:0px\">");
        
        Iterator<Invitation> i = invitations.iterator();
        while(i.hasNext())
        {
            Invitation u = i.next();
            out.println("       <div class=\"input-group\">");
            out.println("           <input type=\"text\" class=\"form-control\" value=\"" + manager.getGroupName(u.getGroupID()) + "\" readonly>");
            out.println("               <span class=\"input-group-addon\">");
            out.println("               <input type=\"checkbox\" name=\"chk\" value=\"" + u.getGroupID() + "\">");
            out.println("           </span>");
            out.println("       </div>");
        }
        out.println("   </div>");
            
        out.println("</div>");
    }
    
    public static void printGroupsTable(PrintWriter out, HttpServletRequest request, HttpServletResponse response, DbManager manager, Integer userID) throws ServletException
    {
        ArrayList<GroupsForUser> groups = new ArrayList<GroupsForUser>();
        try
        {
            groups = manager.getGroupsForUser(userID);
        }
        catch(SQLException e)
        {
            Utils.toLog(e.getMessage(), "e");
            throw new ServletException(e);
        }
        
        Iterator<GroupsForUser> i = groups.iterator();
        int j = 0;
        while(i.hasNext())
        {
            GroupsForUser g = i.next();
            out.println("       <div class=\"input-group\">");
            if (g.getUserIsAdmin())
                out.println("           <input type=\"text\" class=\"form-control\" style=\"color:#00ff00\" value=\"" + g.getGroupName() + "\" readonly>");
            else
                out.println("           <input type=\"text\" class=\"form-control\" value=\"" + g.getGroupName() + "\" readonly>");
            
           /*out.println("               <span class=\"input-group-addon\"");
            out.println("                   <span class=\"label label-default\">Data ultimo post:</span>");
            out.println("               </span>");
            */
            out.println("               <span class=\"input-group-addon\">");
            try
            {
                out.println("                  <a style=\"text-decoration:none\" href=\"\" onclick=\" return false\">Data ultimo post: <span class=\"badge\">"+manager.getLastestPostDate(g.getGroupId())+"</span></a>");
            }
            catch(SQLException e)
            {
                Utils.toLog(e.getMessage(), "e");
                throw new ServletException(e);
            }
            out.println("               </span>");
            out.println("               <span class=\"input-group-addon\">");
            if (j == 0)
                out.println("                   <input type=\"radio\" name=\"rdb\" value=\"" + g.getGroupId() + "\" checked=\"checked\">");
            else
                out.println("                   <input type=\"radio\" name=\"rdb\" value=\"" + g.getGroupId() + "\">");
            out.println("               </span>");
            out.println("       </div>");
            j++;
        }
    }
    
    public static void printUsersTable(PrintWriter out, HttpServletRequest request, HttpServletResponse response, DbManager manager, Integer userID) throws ServletException
    {
        ArrayList<User> users = new ArrayList<User>();
        try
        {
            users = manager.getUsers(userID);
        }
        catch(SQLException e)
        {
            Utils.toLog(e.getMessage(), "e");
            throw new ServletException(e);
        }
        
        out.println("<div class=\"row\" style=\"background-color:" + Utils.gridBgColor + "; border:0px\">");
        out.println("   <div class=\"col-md-10\" style=\"background-color:" + Utils.gridBgColor + "; border:0px\">");
                
        Iterator<User> i = users.iterator();
        while(i.hasNext())
        {
            User u = i.next();
            out.println("       <div class=\"input-group\">");
            out.println("           <input type=\"text\" class=\"form-control\" value=\"" + u.getUserName() + "\" readonly>");
            out.println("               <span class=\"input-group-addon\">");
            out.println("               <input type=\"checkbox\" name=\"chk\" value=\"" + u.getUserName() + "\">");
            out.println("           </span>");
            out.println("       </div>");
        }
        out.println("   </div>");
            
        out.println("</div>");
        
    }
    
    public static void printUsersTableForModify(PrintWriter out, HttpServletRequest request, HttpServletResponse response, DbManager manager, Integer userID) throws ServletException
    {
        ArrayList<User> users = new ArrayList<User>();
        try
        {
            users = manager.getUsers(userID);
        }
        catch(SQLException e)
        {
            Utils.toLog(e.getMessage(), "e");
            throw new ServletException(e);
        }
        
        out.println("<div class=\"row\" style=\"background-color:" + Utils.gridBgColor + "; border:0px\">");
        out.println("   <div class=\"col-md-10\" style=\"background-color:" + Utils.gridBgColor + "; border:0px\">");
                
        Iterator<User> i = users.iterator();
        while(i.hasNext())
        {
            User u = i.next();
            out.println("       <div class=\"input-group\">");
            out.println("           <input type=\"text\" class=\"form-control\" value=\"" + u.getUserName() + "\" readonly>");
            out.println("               <span class=\"input-group-addon\">");
            out.println("               <input type=\"checkbox\" name=\"chk\" value=\"" + u.getUserName() + "\">");
            out.println("           </span>");
            out.println("       </div>");
        }
        out.println("   </div>");
            
        out.println("</div>");
        
    }
    
    public static void printPostsTable(PrintWriter out, final HttpSession session, DbManager manager, Integer groupID) throws ServletException
    {

        ArrayList<PostsForGroup> postsFG = new ArrayList<PostsForGroup>();
        String applicationPath = (String)session.getAttribute("applicationPath");
        
        try
        {
            postsFG = manager.getPostsForGroup(groupID);
        }
        catch(SQLException e)
        {
            Utils.toLog(e.getMessage(), "e");
            throw new ServletException(e);
        }
        
        Iterator<PostsForGroup> i = postsFG.iterator();
        while (i.hasNext())
        {
            PostsForGroup p = i.next();
            out.println("<div class=\"row\" style=\"background-color:" + Utils.gridBgColor + "; border:0px\">");

            out.println("   <div class=\"col-md-3\" style=\"background-color:" + Utils.gridBgColor + "; border:0px\">");
            out.println("       <div class=\"thumbnail\">");
            out.println("           <div class=\"caption\">");
            out.println("               <h4 align=\"center\">" + p.getUserName() + "</h4>");
            out.println("           </div>");
            out.println("           <img src=\"" + p.getUserAvatar()+ "\">");
            out.println("           <br><p align=\"center\">" + Utils.timeStampToString(p.getPostDataCreation()) + "</p>");
            out.println("       </div>");
            out.println("   </div>");
            out.println("   <div class=\"col-md-9\" style=\"background-color:" + Utils.gridBgColor + "; border:0px\">");
            out.println("   <div style=\"width:650px; height:195px; background-color:#ffffff; border:1px solid #00000\">" + Utils.parseLinks(p.getPostText()) + "</div>");

            ArrayList<PostFile> pf = new ArrayList<PostFile>();
            try
            {
                pf = manager.getFilesForPost(p.getPostID());
            }
            catch(SQLException e)
            {
                Utils.toLog(e.getMessage(), "e");
                throw new ServletException(e);
            }
            
            Iterator<PostFile> j = pf.iterator();
            while(j.hasNext())
            {
                PostFile f = new PostFile();
                f = j.next();
                /*
                out.println("   <div class=\"input-group\">");
                out.println("       <input type=\"text\" name=\"file\" value=\"" + f.getPostFileFileSystemName() + "\"class=\"form-control\">");
                out.println("       <span class=\"input-group-btn\">");
                out.println("           <button type=\"submit\" class=\"btn btn-default\" name=\"btn\" value=\"download\">download</button>");
                out.println("       </span>");
                out.println("   </div>");
*/
                out.println("   <span class=\"label label-info\" name=\"file\"><a style=\"color:#ffffff\" target=\"_self\" href=\"RequestDownload?file="+f.getPostFileFileSystemName()+"\">"+f.getPostFileFileSystemName()+"</a></span>");
            }
            
            out.println("   </div>");
            out.println("</div>");
            
            out.println("<hr>");
        }
        
    }
    
    public static void pageFooter(PrintWriter out)
    {
        out.println("           </div>");
        out.println("       <div class=\"col-md-1\" style=\"background-color:#ffffff; border:0px\"></div>");
        out.println("   </div");
        out.println("<footer>");
        out.println("   <p align=\"center\">Giorgio Leonardo. Matricola 151879</p>");
        out.println("</footer>");
        out.println("</html>");
    }
}
