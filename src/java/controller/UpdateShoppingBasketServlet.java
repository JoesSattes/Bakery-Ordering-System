package controller;

import models.ItemInBasket;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.ShoppingBasket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@WebServlet(name = "UpdateShoppingBasketServlet", urlPatterns = {"/UpdateShoppingBasketServlet"})
public class UpdateShoppingBasketServlet extends HttpServlet {

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

            String checkout = request.getParameter("checkout");
            System.out.println(checkout);
            String[] id = request.getParameterValues("id");
            String[] amount = request.getParameterValues("amount");
            HttpSession session = request.getSession();
            ShoppingBasket cart = (ShoppingBasket) session.getAttribute("cart");
            if (cart == null){
                cart = new ShoppingBasket();
            }
            cart.getBasket().clear();

            ResultSet rs = null;
            PreparedStatement preparedStatement = null;

            ItemInBasket item;
            if (id != null){
            try {
                for (int i = 0; i < id.length; i++) {
                    item = new ItemInBasket();
                    item.setInventoryId(Integer.parseInt(id[i]));
                    preparedStatement = conn.prepareStatement("SELECT * FROM inventory WHERE id = ?");
                    preparedStatement.setInt(1, Integer.parseInt(id[i]));
                    rs = preparedStatement.executeQuery();
                    rs.next();
                    item.setItemNo(cart.getBasket().size() + 1);
                    item.setItemAmount(Integer.parseInt(amount[i]));
                    item.setItemPrice(rs.getFloat("price"));
                    item.setItemName(rs.getString("name"));
                    item.setDeliveryCost(rs.getFloat("delivery_cost"));
                    item.setMinimum(rs.getInt("minimum_amount"));
                    cart.addItem(item);
                }
                session.setAttribute("cart", cart);
                Map<String, Double> checkBasket = new HashMap<>();
                if (checkout == null) {
                    response.sendRedirect("viewShoppingBasket.jsp");
                } else {
                    for (ItemInBasket i : cart.getBasket()){
                        preparedStatement = conn.prepareStatement("SELECT seller_account_username, delivery_cost FROM inventory WHERE id=?");
                        preparedStatement.setInt(1, i.getInventoryId());
                        rs = preparedStatement.executeQuery();
                        rs.next();
                        if (!checkBasket.containsKey(rs.getString("seller_account_username"))){
                            checkBasket.put(rs.getString("seller_account_username"), rs.getDouble("delivery_cost"));
                        } else {
                            if (checkBasket.get(rs.getString("seller_account_username")) < rs.getDouble("delivery_cost")){
                                checkBasket.put(rs.getString("seller_account_username"), rs.getDouble("delivery_cost"));
                            }
                        }
                    }
                    double sum = 0;
                    for (double i : checkBasket.values()){
                        sum += i;
                    }
                    session.setAttribute("total_delivery_cost", sum);
                    response.sendRedirect("confirmation.jsp");
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

            } else {
                cart.getBasket().clear();
                session.setAttribute("cart", cart);
                response.sendRedirect("index.jsp");
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
