/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import support.PHUtils;
import support.Utils;

/**
 *
 * @author giorgio
 */
public class Avatar extends HttpServlet
{

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter())
        {
            PHUtils.pageHeader(out);
            out.println("<h3 align=\"center\"><span class=\"label label-info\">Scegli l\'avatar</span></h3>");
            out.println("<form action=\"RequestUploadAvatar\" method=\"post\" ENCTYPE=\"multipart/form-data\">");
            out.println("   <div class=\"row\" style=\"background-color:" + Utils.gridBgColor + "; border:0px\">");
            out.println("       <div class=\"col-md-10\" style=\"background-color:" + Utils.gridBgColor + "; border:0px\">");
            out.println("               <input class=\"btn btn-default\" type=\"file\" name=\"file1\" accept=\"image/png\"> ");
            out.println("       </div>");
            out.println("       <div class=\"col-md-2\"  style=\"background-color:" + Utils.gridBgColor + "; border:0px\">");
            out.println("           <br><div class=\"btn-group-vertical\">");
            out.println("               <button type=\"submit\" class=\"btn btn-default\" name=\"btn\" value=\"avInvia\">Invia</button>");
            out.println("               <button type=\"submit\" class=\"btn btn-default\" name=\"btn\" value=\"avAnnulla\">Annulla</button>");
            out.println("           </div>");
            out.println("       </div>");
            out.println("   </div>");
            out.println("</form>");
            PHUtils.pageFooter(out);
        }
        catch(Exception e)
        {
            Utils.toLog("ERR", e.getLocalizedMessage());
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
