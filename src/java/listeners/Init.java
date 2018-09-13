package listeners;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

public class Init implements ServletContextListener {
    
    private Connection conn;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            conn = getWebpro_14_prod().getConnection();
            sce.getServletContext().setAttribute("connection", conn);
        } catch (Exception ex) {
            Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            conn.close();
        } catch (Exception ex) {
            Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private DataSource getWebpro_14_prod() throws NamingException {
        Context c = new InitialContext();
        return (DataSource) c.lookup("java:comp/env/webpro_14_prod");
    }
    
    
}
