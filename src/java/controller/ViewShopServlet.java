package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet(name = "ViewShopServlet", urlPatterns = {"/ViewShopServlet"})
public class ViewShopServlet extends HttpServlet {

    private Connection conn;

    @Override
    public void init() {
        conn = (Connection) getServletContext().getAttribute("connection");
    }
    
    @Resource(name = "webpro_14_prod")
    private DataSource webpro_14_prod;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String seller_username = request.getParameter("seller_username");
            System.out.println(seller_username);
            Statement statement = null;
            ResultSet rs = null;
            try {
                if (conn == null || conn.isClosed()){
                    conn = webpro_14_prod.getConnection();
                }
                System.out.println("HELLO");
                statement = conn.createStatement();
                rs = statement.executeQuery("SELECT * FROM seller WHERE account_username = '"+seller_username+"'");
                rs.next();
                request.setAttribute("seller_username", seller_username);
                request.setAttribute("shop_name", rs.getString("shop_name"));
                request.setAttribute("address", rs.getString("address"));
                request.setAttribute("description", rs.getString("description"));
                rs = statement.executeQuery("SELECT type FROM store_type WHERE id = "+rs.getInt("store_type_id"));
                rs.next();
                request.setAttribute("type", rs.getString("type"));
                RequestDispatcher pg = request.getRequestDispatcher("viewShop.jsp");
                pg.forward(request, response);
                
            } catch (SQLException ex) {
                Logger.getLogger(ViewShopServlet.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (statement != null)
                        statement.close();
                    if (rs != null)
                        rs.close();
                    if (conn != null)
                        conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ViewShopServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
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
            throws ServletException, IOException {
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
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
