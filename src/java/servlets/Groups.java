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
public class Groups extends HttpServlet
{

    private DbManager manager;
    
    @Override
    public void init() throws ServletException
    {
        this.manager = (DbManager)super.getServletContext().getAttribute("dbmanager");
        
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute("user");
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter())
        {
            PHUtils.pageHeader(out);
            out.println("<h3 align=\"center\"><span class=\"label label-info\">Gruppi a cui partecipi</span></h3>");
            out.println("<div class=\"row\" style=\"background-color:" + Utils.gridBgColor + "; border:0px\">");
            out.println("   <form action=\"ProcessButtons\" method=\"post\">");
            out.println("       <div class=\"col-md-10\" style=\"background-color:" + Utils.gridBgColor + "; border:0px\">");
            //out.println("           <input type=\"text\" class=\"form-control\" placeholder=\"Nome gruppo\" name=\"groupName\">");
            PHUtils.printGroupsTable(out, request, response, manager, user.getUserID());
            
            out.println("       </div>");
            out.println("       <div class=\"col-md-2\"  style=\"background-color:" + Utils.gridBgColor + "; border:0px\">");
            out.println("           <div class=\"btn-group-vertical\">");
            out.println("               <button type=\"submit\" class=\"btn btn-default\" name=\"btn\" value=\"grpFUConferma\">Vai al gruppo</button>");
            out.println("               <button type=\"submit\" class=\"btn btn-default\" name=\"btn\" value=\"grpFUModifica\">Modifica gruppo</button>");
            out.println("               <button type=\"submit\" class=\"btn btn-default\" name=\"btn\" value=\"Annulla\">Annulla</button>");
            out.println("           </div>");
            out.println("       </div>");
            out.println("   </div>");
            out.println("   </form>");
            
            PHUtils.pageFooter(out);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>

}
