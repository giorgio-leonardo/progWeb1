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
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;
import support.PHUtils;
import support.Utils;

/**
 *
 * @author giorgio
 */
public class ProcessButtons extends HttpServlet
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
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        
        
        String btnValue = (String)request.getParameter("btn");
        
        switch(btnValue)
        {
            case "creaGruppo":
                response.sendRedirect("CreateGroup");
                break;
            case "Annulla":
                response.sendRedirect("LandingPage");
                break;
            case "visualizzaGruppi":
                response.sendRedirect("Groups");
                break;
            case "grpConferma":
                Integer i;
                i = insertGroup(request, response);
                insertInvitations(request, response, i);
                response.sendRedirect("LandingPage");
                break;
            case "grpFUConferma":
                viewGroup(request, response);
                break;
            case "grpMGConferma":
                modifyGroup(request, response);
                response.sendRedirect("LandingPage");
                break;
            case "grpFUModifica":
                modifyGroupPage(request, response);
                break;
            case "invitiAiGruppi":
                response.sendRedirect("AcceptInvitations");
                break;
            case "aciConferma":
                acceptInvitations(request, response);
                response.sendRedirect("LandingPage");
                break;
            case "vgAggiungiPost":
                response.sendRedirect("NewPost");
                break;
            case "scegliAvatar":
                response.sendRedirect("Avatar");
                break;
            case "vgStampaPDF":
                
            case "download":
                {
                    RequestDispatcher rd = request.getRequestDispatcher("RequestDownload");
                    rd.forward(request, response);
                }
                break;
            case "logout":
                {
                    RequestDispatcher rd = request.getRequestDispatcher("Logout");
                    rd.forward(request, response);
                }
                break;
        }
    }
    
    
    private synchronized void acceptInvitations(HttpServletRequest request, HttpServletResponse response) throws ServletException
    {
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute("user");
        
        if (request.getParameterValues("chk") != null)
        {
            List<String> chks = Arrays.asList(request.getParameterValues("chk"));
            Iterator<String> si = chks.iterator();
            while(si.hasNext())
            {
                String s = si.next();
                try
                {
                    manager.acceptInvitations(Integer.parseInt(s), user.getUserID(), false);
                }
                catch(SQLException e)
                {
                    Utils.toLog(getClass().getName() + ": " + e.getMessage(), "e");
                    throw new ServletException(e);
                }
            }
        }
    }
        
    
    private synchronized void modifyGroup(HttpServletRequest request, HttpServletResponse response)
    {
        HttpSession session = request.getSession(false);
        Integer groupID = (Integer)session.getAttribute("groupID");
        String newName = (String)request.getParameter("groupName");
        String groupsStoragePath = (String)session.getAttribute("groupsStoragePath");
        
        String oldName="";
        try
        {
            oldName = manager.getGroupName(groupID);
        }
        catch (ServletException ex)
        {
            Logger.getLogger(ProcessButtons.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (!newName.equals(""))
        {
            Utils.modifyGroupFolder(groupsStoragePath, oldName, newName);
            try
            {
                manager.modfyGroupName(groupID, newName);
            }
            catch (SQLException ex)
            {
                Logger.getLogger(ProcessButtons.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            PrintWriter out = null;
            try
            {
                out = response.getWriter();
            }
            catch (IOException ex)
            {
                Logger.getLogger(ProcessButtons.class.getName()).log(Level.SEVERE, null, ex);
            }
            PHUtils.pageHeader(out);
            PHUtils.printAlert(out, request, "Il nome del gruppo non può essere vuoto", "CreateGroup");
            PHUtils.pageFooter(out);
        }
    }
    
    private synchronized void modifyGroupPage(HttpServletRequest request, HttpServletResponse response)
    {
        HttpSession session = request.getSession(false);
        Integer groupID = Integer.parseInt((String)request.getParameter("rdb"));
        if (session.getAttribute("groupID") != null)
        {
            session.removeAttribute("groupID");
        }
        session.setAttribute("groupID", groupID);
            
        try
        {
            response.sendRedirect("ModifyGroup");
        }
        catch(IOException e)
        {
            
        }
    }
    
    private synchronized void insertInvitations(HttpServletRequest request, HttpServletResponse response, Integer groupID) throws ServletException
    {
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute("user");
        String[] chks = request.getParameterValues("chk");
        ArrayList<User> users = new ArrayList<User>();
        
        try
        {
            manager.insertUserInGroup(groupID, user.getUserID());
        }
        catch(SQLException e)
        {
            Utils.toLog(getClass().getName() + ": " + e.getMessage(), "e");
            throw new ServletException(e);
        }
        
        if (chks != null)
        {
            for(String s: chks)
            {
                try
                {
                    users.add(manager.getUserFromUserName(s));
                }
                catch(SQLException e)
                {
                    Utils.toLog(getClass().getName() + ": " + e.getMessage(), "e");
                    throw new ServletException(e);
                }
            }
            try
            {
                manager.insertIvites(groupID, users);
            }
            catch(SQLException e)
            {
                Utils.toLog(getClass().getName() + ": " + e.getMessage(), "e");
                throw new ServletException(e);
            }
        }
    }
    
    private synchronized Integer insertGroup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Integer ret = 0;
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute("user");
        String groupsStoragePath = (String)session.getAttribute("groupsStoragePath");
        
        String groupName = request.getParameter("groupName");
        if (!groupName.equals(""))
        {
            try
            {
                ret = manager.createGroup(user.getUserID(), groupName);
                Utils.createGroupFolder(groupsStoragePath, groupName);
            }
            catch (SQLException e)
            {
                Utils.toLog(getClass().getName() + ": " + e.getMessage(), "e");
                throw new ServletException(e);
            }
        }
        else
        {
            PrintWriter out = response.getWriter();
            PHUtils.pageHeader(out);
            PHUtils.printAlert(out, request, "Il nome del gruppo non può essere vuoto", "CreateGroup");
            PHUtils.pageFooter(out);
        }
        
        return ret;
    }
    
    private synchronized void viewGroup(HttpServletRequest request, HttpServletResponse response)
    {
        HttpSession session = request.getSession(false);
        Integer groupID = Integer.parseInt((String)request.getParameter("rdb"));
        if (session.getAttribute("groupID") != null)
        {
            session.removeAttribute("groupID");
        }
        session.setAttribute("groupID", groupID);
            
        try
        {
            response.sendRedirect("ViewGroup");
        }
        catch(IOException e)
        {
            
        }
    }
}
