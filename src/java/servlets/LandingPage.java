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
import java.util.Date;
import javax.servlet.ServletException;
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
public class LandingPage extends HttpServlet
{

    private DbManager manager;
    
    @Override
    public void init() throws ServletException
    {
        this.manager = (DbManager)super.getServletContext().getAttribute("dbmanager");
        
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(false);
        
        User user = (User)session.getAttribute("user");
        Date lastLogin = (Date)session.getAttribute("lastLogin");
        String applicationPath = (String)session.getAttribute("applicationPath");
        String webInfPath = (String)session.getAttribute("webInfPath");
        
        try (PrintWriter out = response.getWriter())
        {
            PHUtils.pageHeader(out);
            
            out.println("<div class=\"alert alert-success\" align = \"center\">");
            out.println("   Benvenuto <strong>"+ user.getUserName() +"</strong><br>");
            if (Utils.dateToString(lastLogin) != null)
            {
                out.println("   La tua ultima visita risale al " + Utils.dateToString(lastLogin));
            }
            else
            {
                out.println("   Questa è la prima volta che ti connetti.");
            }
            out.println("</div>");
            out.println("<div class=\"navbar navbar-inverse\">");
            out.println("   <div class=\"container\">");
            out.println("       <div class=\"navbar-header\">");
            out.println("           <a class=\"navbar-brand\" href=\"#\">Primo progetto</a>");
            out.println("       </div>");
            out.println("       <div class=\"navbar-collapse collapse\">");
            out.println("           <form class=\"navbar-form navbar-left\" action=\"ProcessButtons\" role=\"search\" method=\"post\">");
            out.println("               <button type=\"submit\" class=\"btn btn-default\" name=\"btn\" value=\"creaGruppo\">Crea gruppo</button>");
            out.println("               <button type=\"submit\" class=\"btn btn-default\" name=\"btn\" value=\"visualizzaGruppi\">Visualizza gruppi</button>");
            out.println("               <button type=\"submit\" class=\"btn btn-default\" name=\"btn\" value=\"scegliAvatar\">Scegli avatar</button>");
            if (manager.userHasInvitations(user.getUserID()))
            {
                out.println("               <button type=\"submit\" class=\"btn btn-success\" name=\"btn\" value=\"invitiAiGruppi\">Inviti ai gruppi</button>");
            }
            else
            {
                out.println("               <button type=\"submit\" class=\"btn btn-default\" name=\"btn\" value=\"invitiAiGruppi\" >Inviti ai gruppi</button>");
                
            }
            out.println("               <button type=\"submit\" class=\"btn btn-default\" name=\"btn\" value=\"logout\">Logout</button>");
            out.println("           </form>");
            out.println("       </div>");
            out.println("   </div>");
            out.println("</div>");

            
            PHUtils.pageFooter(out);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        processRequest(request, response);
    }

  
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        processRequest(request, response);
    }

  
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }

}
