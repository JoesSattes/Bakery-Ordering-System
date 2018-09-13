package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.ShoppingBasket;
import models.ItemInBasket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.sql.DataSource;

@WebServlet(name = "AddProductServlet", urlPatterns = {"/AddProductServlet"})
public class AddProductServlet extends HttpServlet {

    private Connection conn;

    public void init() {
        conn = (Connection) getServletContext().getAttribute("connection");
    }

    @Resource(name = "webpro_14_prod")
    private DataSource webpro_14_prod;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            try {
                if (conn == null || conn.isClosed()) {
                    conn = webpro_14_prod.getConnection();
                }
            } catch (Exception ex) {
                Logger.getLogger(SigninServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            String a = request.getHeader("Referer").substring(request.getContextPath().length() + 22);
            //String a = request.getHeader("Referer").substring(request.getContextPath().length() + 37);
            System.out.println(a);
            String from = request.getParameter("from");
            Boolean check = false;
            int id = Integer.parseInt(request.getParameter("add"));
            String am = request.getParameter("amount");
            int amount = 1;
            if (am != null) {
                amount = Integer.parseInt(am);
            }
            HttpSession session = request.getSession();
            ShoppingBasket cart = (ShoppingBasket) session.getAttribute("cart");

            ResultSet rs = null;
            PreparedStatement preparedStatement = null;

            ItemInBasket item = new ItemInBasket();
            item.setInventoryId(id);
            
            try {
                preparedStatement = conn.prepareStatement("SELECT * FROM inventory WHERE id = ?");
                preparedStatement.setInt(1, id);
                rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    if (cart != null){
                        for (ItemInBasket i : cart.getBasket()) {
                            if (i.getInventoryId() == id) {
                                i.setItemAmount(i.getItemAmount() + amount);
                                check = true;
                                break;
                            }
                        }
                    } else {
                        cart = new ShoppingBasket();
                        cart.setBasket(new ArrayList <ItemInBasket> ());
                    }
                    if (!check) {
                        item.setItemNo(cart.getBasket().size()+1);
                        item.setItemAmount(amount);
                        item.setItemPrice(rs.getFloat("price"));
                        item.setItemName(rs.getString("name"));
                        item.setDeliveryCost(rs.getFloat("delivery_cost"));
                        item.setMinimum(rs.getInt("minimum_amount"));
                        cart.addItem(item);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(AddProductServlet.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(SigninServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            session.setAttribute("cart", cart);
            response.sendRedirect(a);
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
