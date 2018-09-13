package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import models.*;

@WebServlet(name = "PaymentServlet", urlPatterns = {"/PaymentServlet"})
public class PaymentServlet extends HttpServlet {

//    @Resource(name = "creditCard")
//    private DataSource creditCard;

//    private Connection connCreditCard;
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

            request.setCharacterEncoding("UTF-8");
            ResultSet rs = null;
            PreparedStatement preparedStatement = null;
            PreparedStatement preparedStatement2 = null;
            PreparedStatement preparedStatement3 = null;
            PreparedStatement preparedStatement4 = null;
            PreparedStatement preparedStatement5 = null;
            PreparedStatement preparedStatement6 = null;
            PreparedStatement preparedStatement7 = null;
            PreparedStatement preparedStatement8 = null;
            
            HttpSession session = request.getSession();
            ShoppingBasket cart = (ShoppingBasket) session.getAttribute("cart");
            try {
                if (conn == null || conn.isClosed()) {
                    conn = webpro_14_prod.getConnection();
                }
//                if (connCreditCard == null || connCreditCard.isClosed()) {
//                    connCreditCard = creditCard.getConnection();
//                }
                conn.setAutoCommit(false);
                
                
                Account account = (Account) session.getAttribute("account");
                Customer customer = (Customer) session.getAttribute("customer");
                String description = request.getParameter("description");
                String selectAddress = request.getParameter("selectAddress");
                String deliveryType = request.getParameter("deliveryType");
                double total_price = Double.parseDouble(request.getParameter("total_price"));
                if (description == null || description.equals("")) {
                    description = null;
                }
                
                String pay_type = request.getParameter("pay_type");
                
                cart.setBasketDescription(description);
                cart.setAddress(selectAddress);
                cart.setDeliveryType(deliveryType);

                String username = account.getUsername();
                preparedStatement = conn.prepareStatement("INSERT INTO shopping_basket VALUES (null,?,NOW(),false,?,?,?)");
                preparedStatement.setString(1, cart.getBasketDescription());
                preparedStatement.setString(2, username);
                preparedStatement.setString(3, cart.getAddress());
                preparedStatement.setString(4, cart.getDeliveryType());
                preparedStatement.executeUpdate();
                
                preparedStatement2 = conn.prepareStatement("SELECT id FROM shopping_basket WHERE order_completion_time = (SELECT max(order_completion_time) FROM shopping_basket WHERE customer_account_username = ?) AND customer_account_username = ?");
                preparedStatement2.setString(1, username);
                preparedStatement2.setString(2, username);
                rs = preparedStatement2.executeQuery();
                rs.next();
                String id = rs.getString("id");
                
                if (pay_type.equals("card")) {
//                    String cardNum1 = request.getParameter("cardNum1");
//                    String cardNum2 = request.getParameter("cardNum2");
//                    String cardNum3 = request.getParameter("cardNum3");
//                    String cardNum4 = request.getParameter("cardNum4");
//                    String cvv = request.getParameter("cvv");
//                    String month = request.getParameter("month");
//                    String year = request.getParameter("year");
//                    preparedStatement3 = connCreditCard.prepareStatement("SELECT * FROM credit_card WHERE card_no=? AND cvv=?");
//                    preparedStatement3.setString(1, cardNum1 + cardNum2 + cardNum3 + cardNum4);
//                    preparedStatement3.setString(2, cvv);
//                    rs = preparedStatement3.executeQuery();
//                    if (rs.next()) {
//                        if (rs.getDouble("balance") - total_price >= 0) {
//                            preparedStatement4 = connCreditCard.prepareStatement("UPDATE credit_card SET balance=? WHERE card_no=?");
//                            preparedStatement4.setDouble(1, rs.getDouble("balance") - total_price);
//                            preparedStatement4.setString(2, rs.getString("card_no"));
//                            preparedStatement4.executeUpdate();
//                            for (ItemInBasket i : cart.getBasket()){
//                                i.setStatus("paid");
//                            }
//                            Payment payment = new Payment();
//                            payment.setPaymentType("card");
//                            preparedStatement5 = conn.prepareStatement("INSERT INTO payment VALUES (null, ?, null, ?)");
//                            preparedStatement5.setString(1, pay_type);
//                            preparedStatement5.setString(2, id);
//                            preparedStatement5.executeUpdate();
//                            
//                        } else {
//                            System.out.println("ERROR");
//                        }
//                    }
                } else if (pay_type.equals("COD")) {
                    for (ItemInBasket i : cart.getBasket()){
                        i.setStatus("preparing");
                    }
                    Payment payment = new Payment();
                    payment.setPaymentType("COD");
                    preparedStatement6 = conn.prepareStatement("INSERT INTO payment VALUES (null, ?, null, ?)");
                    preparedStatement6.setString(1, pay_type);
                    preparedStatement6.setString(2, id);
                    preparedStatement6.executeUpdate();
                }
                preparedStatement7 = conn.prepareStatement("INSERT INTO order_item VALUES (?,?,?,?,?,?,?)");
                preparedStatement7.setString(1, id);
                for (ItemInBasket i : cart.getBasket()) {
                    preparedStatement7.setInt(2, i.getInventoryId());
                    preparedStatement7.setInt(3, i.getItemNo());
                    preparedStatement7.setInt(4, i.getItemAmount());
                    preparedStatement7.setDouble(5, i.getItemPrice());
                    preparedStatement7.setString(6, i.getStatus());
                    preparedStatement7.setString(7, Double.toString(i.getDeliveryCost()));
                    preparedStatement7.executeUpdate();
                    preparedStatement8 = conn.prepareStatement("UPDATE inventory SET stock = stock-? WHERE id=? AND stock-? >= 0");
                    preparedStatement8.setInt(1, i.getItemAmount());
                    preparedStatement8.setInt(2, i.getInventoryId());
                    preparedStatement8.setInt(3, i.getItemAmount());
                    preparedStatement8.executeUpdate();
                }
                
                conn.commit();
                
            } catch (SQLException ex) {
                try {
                    Logger.getLogger(PaymentServlet.class.getName()).log(Level.SEVERE, null, ex);
                    conn.rollback();
                } catch (SQLException ex1) {
                    Logger.getLogger(PaymentServlet.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } finally {
                try {
                    conn.setAutoCommit(true);
                    cart.clearAllItem();
                    session.setAttribute("cart", cart);
                    
                    if (conn != null) {
                        conn.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                    if (preparedStatement2 != null) {
                        preparedStatement2.close();
                    }
                    if (preparedStatement3 != null) {
                        preparedStatement3.close();
                    }
                    if (preparedStatement4 != null) {
                        preparedStatement4.close();
                    }
                    if (preparedStatement5 != null) {
                        preparedStatement5.close();
                    }
                    if (preparedStatement6 != null) {
                        preparedStatement6.close();
                    }
                    if (preparedStatement7 != null) {
                        preparedStatement7.close();
                    }
                    if (preparedStatement8 != null) {
                        preparedStatement8.close();
                    }
//                    if (connCreditCard != null){
//                        connCreditCard.close();
//                    }
                    
                    RequestDispatcher pg = request.getRequestDispatcher("orderStatus.jsp");
                    pg.forward(request, response);
                } catch (SQLException ex) {
                    Logger.getLogger(PaymentServlet.class.getName()).log(Level.SEVERE, null, ex);
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
