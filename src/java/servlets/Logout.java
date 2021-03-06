/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlets;

import db.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import support.PHUtils;

/**
 *
 * @author giorgio
 */
public class Logout extends HttpServlet
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute("user");
       
        if (session != null)
        {
            session.removeAttribute("user");
            session.removeAttribute("lastLogin");
            session.removeAttribute("applicationPath");
            session.removeAttribute("webInfPath");
            session.removeAttribute("groupsStoragePath");
            session.removeAttribute("groupID");
            session.invalidate();
        }
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter())
        {
            PHUtils.pageHeader(out);
            out.println("<meta HTTP-EQUIV=\"REFRESH\" content=\"1; url=" + request.getContextPath() + "/index.html\">");
            out.println("<div class=\"alert alert-success\" align = \"center\">");
            out.println("   Arrivederci <strong>"+ user.getUserName() +"!</strong>.");
            out.println("</div>");
            PHUtils.pageFooter(out);
        }
    }
}
