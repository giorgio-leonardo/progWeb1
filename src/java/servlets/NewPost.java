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
public class NewPost extends HttpServlet
{

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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
            out.println("<h3 align=\"center\"><span class=\"label label-info\">Nuovo post</span></h3>");
            out.println("<div class=\"row\" style=\"background-color:" + Utils.gridBgColor + "; border:0px\">");
            out.println("   <form action=\"RequestUpload\" method=\"post\" ENCTYPE=\"multipart/form-data\">");
            out.println("       <div class=\"col-md-10\" style=\"background-color:" + Utils.gridBgColor + "; border:0px\">");
            
            out.println("           <div class=\"row\" style=\"background-color:" + Utils.gridBgColor + "; border:0px\">");
            
            out.println("               <div class=\"col-md-3\" style=\"background-color:" + Utils.gridBgColor + "; border:0px\">");
            out.println("                   <div class=\"thumbnail\">");
            out.println("                       <div class=\"caption\">");
            out.println("                           <h4 align=\"center\">" + user.getUserName() + "</h4>");
            out.println("                       </div>");
            out.println("                       <img src=\"" + user.getUserAvatar() + "\">");
            out.println("                       <br><p align=\"center\">Nuovo post</p>");
            out.println("                   </div>");
            out.println("               </div>");
            out.println("               <div class=\"col-md-9\" style=\"background-color:" + Utils.gridBgColor + "; border:0px\">");
            out.println("               <textarea rows=\"9\" cols=\"79\" name=\"postText\" style=\"resize:none\"></textarea>");
            out.println("               <input class=\"btn btn-default\" type=\"file\" name=\"file1\"> ");
            out.println("               <input class=\"btn btn-default\" type=\"file\" name=\"file2\"> ");
            out.println("               <input class=\"btn btn-default\" type=\"file\" name=\"file3\"> ");
            out.println("           </div>");
            out.println("</div>");
            
            out.println("       </div>");
            out.println("       <div class=\"col-md-2\"  style=\"background-color:" + Utils.gridBgColor + "; border:0px\">");
            out.println("           <br><div class=\"btn-group-vertical\">");
            out.println("               <button type=\"submit\" class=\"btn btn-default\" name=\"btn\" value=\"npInvia\">Invia</button>");
            out.println("               <button type=\"submit\" class=\"btn btn-default\" name=\"btn\" value=\"npAnnulla\">Annulla</button>");
            out.println("           </div>");
            out.println("       </div>");
            out.println("</div>");
            
            out.println("</form>");
            
            
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
