/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package filters;

import db.DbManager;
import db.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import support.PHUtils;
import support.Utils;

/**
 *
 * @author giorgio
 */
public class AdminFilter implements Filter
{
    
    private FilterConfig filterConfig = null;
    
    @Override
    public void init(FilterConfig filterConfig)
    {        
        this.filterConfig = filterConfig;
        if (filterConfig != null)
        {
            Utils.toLog("AdminFilter:Initializing filter", "i");
        }
    }
   
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest)request;

        if ( (req.getServletPath().endsWith("ModifyGroup") ) || ((req.getServletPath().endsWith("PrintPDF"))))
        {
            HttpServletResponse res = (HttpServletResponse)response;
            
            HttpSession session =  req.getSession(false);
            DbManager manager = (DbManager)filterConfig.getServletContext().getAttribute("dbmanager");
            Integer groupID = (Integer)session.getAttribute("groupID");
            User user = (User)session.getAttribute("user");
            
            if(manager.isUserAdmin(user.getUserID(), groupID))
            {
                chain.doFilter(request, response);
            }
            else
            {
                try (PrintWriter out = res.getWriter())
                {
                    PHUtils.pageHeader(out);
                    PHUtils.printAlert(out, req, "Non sei autorizzato ad eseguire questa operazione!", "LandingPage");
                    PHUtils.pageFooter(out);
                    out.close();
                }
            }
            Utils.toLog("AdminFilter:doFilter()", "i");
        }
        else
        {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy()
    {        
        filterConfig = null;
    }
}
