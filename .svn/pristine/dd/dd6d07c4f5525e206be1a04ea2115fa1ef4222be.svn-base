/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package filters;

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
import support.Utils;

/**
 *
 * @author giorgio
 */
public class FiltroLogin implements Filter
{
    
    private static final boolean debug = true;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;
   
      
    public void init(FilterConfig filterConfig)
    {        
        this.filterConfig = filterConfig;
        
    }
       
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        /*
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;
        HttpSession session = req.getSession(false);
        PrintWriter out = res.getWriter();
        String path = req.getServletPath();
        
        res.setContentType("text/html;charset=UTF-8");
        
        if (!path.startsWith("/Login"))
        {
            User user = (User)session.getAttribute("user");
        
            if (user != null)
            {
                Utils.pageHeader(out);
                Utils.printAlertNoRedir(out, req, "Un utente è già connesso al sistema");
                Utils.pageFooter(out);
            }
        }
        Utils.toLog(this.getClass().getName() + ": "  + req.getContextPath(), "i");
        */
        chain.doFilter(request, response);
    }

    public void destroy()
    {
        this.filterConfig = null;
    }
}
