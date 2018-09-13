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
import models.*;

@WebServlet(name = "ViewEachProductServlet", urlPatterns = {"/productDetail"})
public class ViewEachProductServlet extends HttpServlet {

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
            String id = request.getParameter("id");
            Statement statement = null;
            ResultSet rs = null;
            try {
                if (conn == null || conn.isClosed()){
                    conn = webpro_14_prod.getConnection();
                }
                
                statement = conn.createStatement();
                Product product = new Product();
                rs = statement.executeQuery("SELECT * FROM inventory WHERE id = '"+id+"'");
                rs.next();
                product.setProductName(rs.getString("name"));
                product.setProductPrice(rs.getDouble("price"));
                product.setProductDescription(rs.getString("description"));
                product.setProductStock(rs.getInt("stock"));
                product.setDeliveryCost(rs.getDouble("delivery_cost"));
                product.setProductId(rs.getInt("id"));
                if (rs.getString("minimum_amount") != null){
                    product.setMinimum(rs.getInt("minimum_amount"));
                    System.out.println(product.getMinimum());
                }
                request.setAttribute("product", product);
                Account account = new Account();
                account.setUsername(rs.getString("seller_account_username"));
                request.setAttribute("seller_account", account);
                rs = statement.executeQuery("SELECT * FROM seller WHERE account_username = '"+account.getUsername()+"'");
                rs.next();
                Seller seller = new Seller();
                seller.setStoreName(rs.getString("shop_name"));
                seller.setStoreAddress(rs.getString("address"));
                seller.setStoreDescript(rs.getString("description"));
                seller.setStoreTerm(rs.getString("term_of_use"));
                request.setAttribute("seller_seller", seller);
                RequestDispatcher pg = request.getRequestDispatcher("viewEachProduct.jsp");
                pg.forward(request, response);
                
            } catch (SQLException ex) {
                Logger.getLogger(ViewEachProductServlet.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (statement != null)
                        statement.close();
                    if (rs != null)
                        rs.close();
                    if (conn != null)
                        conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ViewEachProductServlet.class.getName()).log(Level.SEVERE, null, ex);
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
