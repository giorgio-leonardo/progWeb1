/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package listeners;

import db.DbManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import sun.security.pkcs11.Secmod;
import support.Utils;



public class WebAppContextListener implements ServletContextListener
{

    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        String dbUrl = (String)sce.getServletContext().getInitParameter("dbUrl");
        
        try
        {
            DbManager manager = new DbManager(dbUrl);
            sce.getServletContext().setAttribute("dbmanager", manager);
        }
        catch(SQLException e)
        {
            Utils.toLog(getClass().getName() + ": " + e.getMessage(), "e");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
        DbManager.shutdown();
    }
    
}
