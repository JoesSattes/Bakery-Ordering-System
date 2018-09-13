package controller;

import models.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;

@WebServlet(name = "SigninServlet", urlPatterns = {"/SigninServlet"})
public class SigninServlet extends HttpServlet {

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

                HttpSession session = request.getSession();
                
                
                //add filter var
                boolean customerFlag = false;
                boolean storeFlag = false;
                boolean adminFlag = false;

                if (session.getAttribute("username") == null) { // check if user has signed in already

                    String username = request.getParameter("username");
                    String password = request.getParameter("password");

                    Statement statement = null;
                    ResultSet rs = null;
                    PreparedStatement preparedStatement = null;

                    // check username (user can use both username and email) and password
                    try {
                        preparedStatement = conn.prepareStatement("SELECT * FROM account WHERE (username=? OR user_email=?) AND password=?");
                        preparedStatement.setString(1, username);
                        preparedStatement.setString(2, username);
                        preparedStatement.setString(3, password);
                        System.out.println(username);
                        System.out.println(password);
                        rs = preparedStatement.executeQuery();
                        if (rs.next()) { // if user filled in the right username and password
                            username = rs.getString("username");
                            session.setAttribute("username", username);
                            session.setAttribute("password", password);
                            Account account = new Account();
                            User user = new User();

                            if (rs.getString("account_type") == null) {
                                response.sendRedirect("signin.html");
                            }else if (rs.getString("account_type") != null && rs.getString("account_type").equals("customer")) {

                                Customer customer = new Customer();

                                statement = conn.createStatement();
                                rs = statement.executeQuery("SELECT username, password, email, firstname, lastname, birthday, phone_number1, phone_number2 FROM user, account, customer WHERE user.email = account.user_email AND account.username = customer.account_username AND username = '" + username + "'");
                                rs.next();
                                account.setUsername(rs.getString("username"));
                                account.setPassword(rs.getString("password"));
                                user.setEmail(rs.getString("email"));
                                user.setFirstname(rs.getString("firstname"));
                                user.setLastname(rs.getString("lastname"));
                                user.setPhoneNumber1(rs.getString("phone_number1"));
                                user.setPhoneNumber2(rs.getString("phone_number2"));
                                customer.setBirthday(rs.getString("birthday"));
                                rs = statement.executeQuery("SELECT * FROM address WHERE customer_account_username = '" + username + "'");
                                ArrayList<CustomerAddress> customerAddressList = new ArrayList<CustomerAddress>();
                                customer.setCustomerAddress(customerAddressList);
                                while (rs.next()) {
                                    CustomerAddress customerAddress = new CustomerAddress();
                                    customerAddress.setAddress(rs.getString("address"));
                                    customerAddress.setDistrict(rs.getString("district"));
                                    customerAddress.setProvince(rs.getString("province"));
                                    customerAddress.setPostcode(rs.getString("postcode"));
                                    customer.addAddress(customerAddress);
                                }
                                StoragePath storagePath = new StoragePath();
                                session.setAttribute("storagePath", storagePath);
                                session.setAttribute("customer", customer);
                                
                                //add filter set customer
                                customerFlag = true;
                storeFlag = false;
                adminFlag = false;
                session.setAttribute("customerFlag", customerFlag);
                session.setAttribute("storeFlag", storeFlag);
                session.setAttribute("adminFlag", adminFlag);
                                
                            } else if (rs.getString("account_type") != null && rs.getString("account_type").equals("seller")) {
                                Seller seller = new Seller();
                                statement = conn.createStatement();
                                rs = statement.executeQuery("SELECT username, password, email, firstname, lastname, phone_number1, phone_number2, shop_name, address, description, term_of_use, store_type_id, banner_photo FROM user, account, seller WHERE user.email = account.user_email AND account.username = seller.account_username AND username = '" + username + "'");
                                rs.next();
                                account.setUsername(rs.getString("username"));
                                account.setPassword(rs.getString("password"));
                                user.setFirstname(rs.getString("firstname"));
                                user.setLastname(rs.getString("lastname"));
                                user.setPhoneNumber1(rs.getString("phone_number1"));
                                user.setPhoneNumber2(rs.getString("phone_number2"));
                                user.setEmail(rs.getString("email"));
                                seller.setStoreName(rs.getString("shop_name"));
                                seller.setStoreAddress(rs.getString("address"));
                                seller.setStoreDescript(rs.getString("description"));
                                seller.setStoreTerm(rs.getString("term_of_use"));
                                seller.setStoreType(rs.getInt("store_type_id"));
                                session.setAttribute("seller", seller);
                                StoragePath storagePath = new StoragePath();
                                session.setAttribute("storagePath", storagePath);

                                //add filter set seller
                                                customerFlag = false;
                storeFlag = true;
                adminFlag = false;
                session.setAttribute("customerFlag", customerFlag);
                session.setAttribute("storeFlag", storeFlag);
                session.setAttribute("adminFlag", adminFlag);
                            }
                            preparedStatement = conn.prepareStatement("UPDATE account SET last_login_date = NOW() WHERE username = ?");
                            preparedStatement.setString(1, account.getUsername());
                            preparedStatement.executeUpdate();
                            session.setAttribute("account", account);
                            session.setAttribute("user", user);
                            
                            response.sendRedirect("index.jsp");
                        } else {
                            response.sendRedirect("signin.html");
                        }

                    } catch (SQLException ex) {
                        Logger.getLogger(SigninServlet.class.getName()).log(Level.SEVERE, null, ex);
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
                            if (statement != null) {
                                statement.close();
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(SigninServlet.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    response.sendRedirect("index.jsp");
                }
                
            } catch (Exception ex) {
                Logger.getLogger(SigninServlet.class.getName()).log(Level.SEVERE, null, ex);
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
