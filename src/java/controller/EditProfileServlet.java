package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import models.*;

@WebServlet(name = "EditProfileServlet", urlPatterns = {"/EditProfileServlet"})
public class EditProfileServlet extends HttpServlet {

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
            HttpSession session = request.getSession();
            String passwordCheck = request.getParameter("password");
            Customer customer = null;
            Seller seller = null;
            User user = (User) session.getAttribute("user");
            Account account = (Account) session.getAttribute("account");
            System.out.println(account.getUsername());
            String password = account.getPassword();
            String action = request.getParameter("action");
            
            System.out.println(passwordCheck);
            System.out.println(password);
            System.out.println(action);
            
            if (request.getParameter("account").equals("customer")){
                customer = (Customer) session.getAttribute("customer");
            } else if (request.getParameter("account").equals("seller")){
                seller = (Seller) session.getAttribute("seller");
            }

            PreparedStatement statementUser = null;
            PreparedStatement statementAccount = null;
            PreparedStatement statementSeller = null;
            PreparedStatement statementAddress = null;

            if (passwordCheck.equals(password)) {
                System.out.println("same password");
                try {
                    if (conn == null || conn.isClosed()) {
                        conn = webpro_14_prod.getConnection();
                    }
                    conn.setAutoCommit(false);
                    String username = account.getUsername();
                    String email = user.getEmail();
                    if (action.equals("edit profile")) {
                        String firstname = request.getParameter("firstname");
                        String lastname = request.getParameter("lastname");
                        String phone_number = request.getParameter("phone_number");
                        String phone_number2 = request.getParameter("phone_number2");
                        user.setFirstname(firstname);
                        user.setLastname(lastname);
                        user.setPhoneNumber1(phone_number);
                        user.setPhoneNumber2(phone_number2);
                        statementUser = conn.prepareStatement("UPDATE user SET firstname = ?, lastname = ?, phone_number1 = ?, phone_number2 = ? WHERE email = ?");
                        statementUser.setString(1, user.getFirstname());
                        statementUser.setString(2, user.getLastname());
                        statementUser.setString(3, user.getPhoneNumber1());
                        statementUser.setString(4, user.getPhoneNumber2());
                        statementUser.setString(5, email);
                        statementUser.executeUpdate();
                        System.out.println("update user");
                        if (customer != null) {
                            String[] address = request.getParameterValues("address");
                            String[] district = request.getParameterValues("district");
                            String[] province = request.getParameterValues("province");
                            String[] postcode = request.getParameterValues("postcode");
                            
                            ArrayList<CustomerAddress> customerAddressList = new ArrayList<CustomerAddress>();
                            CustomerAddress customerAddress;
                            for (int i = 0; i < address.length; i++) {
                                customerAddress = new CustomerAddress(address[i], district[i], province[i], postcode[i]);
                                customerAddressList.add(customerAddress);
                            }
                            customer.setCustomerAddress(customerAddressList);
                            statementAddress = conn.prepareStatement("DELETE FROM address WHERE customer_account_username = '" + username + "'");
                            statementAddress.executeUpdate();
                            statementAddress = conn.prepareStatement("INSERT INTO address VALUES (?, ?, ?, ?, ?)");
                            statementAddress.setString(1, username);
                            for (CustomerAddress i : customer.getCustomerAddress()) {
                                statementAddress.setString(2, i.getAddress());
                                statementAddress.setString(3, i.getDistrict());
                                statementAddress.setString(4, i.getProvince());
                                statementAddress.setString(5, i.getPostcode());
                                statementAddress.executeUpdate();
                            }
                            session.setAttribute("customer", customer);
                            session.setAttribute("user", user);
                            session.setAttribute("account", account);
                        } else if (seller != null) {
                            String storeName = request.getParameter("storeName");
                            String storeDescription = request.getParameter("storeDescript");
                            String storeTerm = request.getParameter("storeTerm");
                            String storeAddress = request.getParameter("storeAddress");
                            seller.setStoreName(storeName);
                            seller.setStoreTerm(storeTerm);
                            seller.setStoreAddress(storeAddress);
                            seller.setStoreDescript(storeDescription);
                            statementSeller = conn.prepareStatement("UPDATE seller SET shop_name = ?, address = ?, description = ?, term_of_use = ? WHERE account_username = ?");
                            statementSeller.setString(1, seller.getStoreName());
                            statementSeller.setString(2, seller.getStoreAddress());
                            statementSeller.setString(3, seller.getStoreDescript());
                            statementSeller.setString(4, seller.getStoreTerm());
                            statementSeller.setString(5, username);
                            statementSeller.executeUpdate();
                            System.out.println("update seller");
                        }
                    } else if (action.equals("change password")){
                        String newPassword = request.getParameter("new_password");
                        String confirmedPassword = request.getParameter("confirmed_password");
                        if (newPassword.equals(confirmedPassword)){
                            account.setPassword(newPassword);
                            statementAccount = conn.prepareStatement("UPDATE account SET password = ? WHERE username = ?");
                            statementAccount.setString(1, account.getPassword());
                            statementAccount.setString(2, username);
                            statementAccount.executeUpdate();
                        } else {
                            RequestDispatcher pg = request.getRequestDispatcher("viewProfile.jsp");
                            pg.forward(request, response);
                        }
                    }
                    conn.commit();
                    System.out.println("FINISH");
                } catch (Exception ex){
                    try {
                        conn.rollback();
                        System.out.println("rollback");
                    } catch (SQLException ex1) {
                        Logger.getLogger(EditProfileServlet.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                } finally {
                    try {
                        conn.setAutoCommit(true);
                        if (conn != null)
                            conn.close();
                        if (statementUser != null)
                            statementUser.close();
                        if (statementAccount != null)
                            statementAccount.close();
                        if (statementSeller != null)
                            statementSeller.close();
                        if (statementAddress != null)
                            statementAddress.close();
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(EditProfileServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("FINISH FINALLY");
                    response.sendRedirect("viewProfile.jsp");
                }
            } else {
            RequestDispatcher pg = request.getRequestDispatcher("viewProfile.jsp");
                            pg.forward(request, response);
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
