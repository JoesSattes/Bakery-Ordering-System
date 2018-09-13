package controller;

import models.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import java.util.ArrayList;

@WebServlet(name = "SignupServlet", urlPatterns = {"/SignupServlet"})
public class SignupServlet extends HttpServlet {

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

            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");
            if (password.equals(confirmPassword)) {
                String email = request.getParameter("email");
                String firstname = request.getParameter("firstname");
                String lastname = request.getParameter("lastname");
                String phone_number = request.getParameter("phone_number");
                String phone_number2 = request.getParameter("phone_number2");
                String username = request.getParameter("username");
                String accountType = request.getParameter("accountType");
                String birthday = request.getParameter("birthday");
                String address = request.getParameter("address");
                String district = request.getParameter("district");
                String province = request.getParameter("province");
                String postcode = request.getParameter("postcode");
                String storeName = request.getParameter("storeName");
                String storeDescription = request.getParameter("storeDescription");
                String storeAddress = request.getParameter("storeAddress");
                String storeTerm = request.getParameter("storeTerm");
                String storeType = request.getParameter("storeType");

                PreparedStatement statementUser = null;
                PreparedStatement statementAccount = null;
                PreparedStatement statementPhoneNumber = null;
                PreparedStatement statementCustomer = null;
                PreparedStatement statementAddress = null;
                PreparedStatement statementSeller = null;
                try {
                    if (conn == null || conn.isClosed()) {
                        conn = webpro_14_prod.getConnection();
                    }

                    User user = new User(firstname, lastname, phone_number, phone_number2, email);
                    statementUser = conn.prepareStatement("INSERT INTO user VALUES (?, ?, ?, ?, ?)");
                    statementUser.setString(1, user.getEmail());
                    statementUser.setString(2, user.getFirstname());
                    statementUser.setString(3, user.getLastname());
                    statementUser.setString(4, user.getPhoneNumber1());
                    statementUser.setString(5, user.getPhoneNumber2());
                    Account account = new Account(username, password);

                    statementAccount = conn.prepareStatement("INSERT INTO account VALUES(?, ?, NOW(), null, ?, ?, null)");
                    statementAccount.setString(1, account.getUsername());
                    statementAccount.setString(2, account.getPassword());
                    statementAccount.setString(3, user.getEmail());
                    statementAccount.setString(4, accountType);

                    if (accountType.equals("customer")) {
                        Customer customer = new Customer(birthday);
                        CustomerAddress customerAddress = new CustomerAddress(address, district, province, postcode);
                        ArrayList<CustomerAddress> customerAddressList = new ArrayList<CustomerAddress>();
                        customerAddressList.add(customerAddress);
                        customer.setCustomerAddress(customerAddressList);
                        statementCustomer = conn.prepareStatement("INSERT INTO customer VALUES('" + account.getUsername() + "', '" + customer.getBirthday() + "')");
                        statementAddress = conn.prepareStatement("INSERT INTO address VALUES(?, ?, ?, ?, ?)");
                        statementAddress.setString(1, account.getUsername());
                        statementAddress.setString(2, customer.getCustomerAddress().get(0).getAddress());
                        statementAddress.setString(3, customer.getCustomerAddress().get(0).getDistrict());
                        statementAddress.setString(4, customer.getCustomerAddress().get(0).getProvince());
                        statementAddress.setString(5, customer.getCustomerAddress().get(0).getPostcode());
                        statementUser.executeUpdate();
                        statementAccount.executeUpdate();
                        statementCustomer.executeUpdate();
                        statementAddress.executeUpdate();
                    } else {
                        Seller seller = new Seller(storeName, storeDescription, storeAddress, storeTerm);
                        seller.setStoreType(Integer.parseInt(storeType));
                        statementSeller = conn.prepareStatement("INSERT INTO seller VALUES(?, ?, ?, ?, ?, ?, null)");
                        statementSeller.setString(1, account.getUsername());
                        statementSeller.setString(2, seller.getStoreName());
                        statementSeller.setString(3, seller.getStoreAddress());
                        statementSeller.setString(4, seller.getStoreDescript());
                        statementSeller.setString(5, seller.getStoreTerm());
                        statementSeller.setInt(6, seller.getStoreType());
                        statementUser.executeUpdate();
                        statementAccount.executeUpdate();
                        statementSeller.executeUpdate();
                    }

                    response.sendRedirect("signin.html");
                } catch (Exception ex) {
                    Logger.getLogger(SignupServlet.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        if (statementUser != null) {
                            statementUser.close();
                        }
                        if (statementAccount != null) {
                            statementAccount.close();
                        }
                        if (statementPhoneNumber != null) {
                            statementPhoneNumber.close();
                        }
                        if (statementCustomer != null) {
                            statementCustomer.close();
                        }
                        if (statementAddress != null) {
                            statementAddress.close();
                        }
                        if (statementSeller != null) {
                            statementSeller.close();
                        }
                        if (conn != null) {
                            conn.close();
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(SignupServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
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
