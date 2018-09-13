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
import models.Account;
import models.CustomerAddress;

@WebServlet(name = "ConfirmationServlet", urlPatterns = {"/ConfirmationServlet"})
public class ConfirmationServlet extends HttpServlet {

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
            String description = request.getParameter("description");
            String deliveryType = request.getParameter("deliveryType");
            HttpSession session = request.getSession();
            PreparedStatement statementCustomerAddress = null;
            String selectAddress;
            try {
                if (!request.getParameter("address").equals("")) {
                    String address = request.getParameter("address");
                    String district = request.getParameter("district");
                    String province = request.getParameter("province");
                    String postcode = request.getParameter("postcode");

                    if (conn == null || conn.isClosed()) {
                        conn = webpro_14_prod.getConnection();
                    }
                    Account account = (Account) session.getAttribute("account");
                    CustomerAddress customerAddress = new CustomerAddress(address, district, province, postcode);
                    statementCustomerAddress = conn.prepareStatement("INSERT INTO address VALUES(?, ?, ?, ?, ?)");
                    statementCustomerAddress.setString(1, account.getUsername());
                    statementCustomerAddress.setString(2, customerAddress.getAddress());
                    statementCustomerAddress.setString(3, customerAddress.getDistrict());
                    statementCustomerAddress.setString(4, customerAddress.getProvince());
                    statementCustomerAddress.setString(5, customerAddress.getPostcode());
                    statementCustomerAddress.executeUpdate();
                    models.Customer customer = (models.Customer) session.getAttribute("customer");
                    customer.addAddress(customerAddress);
                    selectAddress = customerAddress.getAddress()+" "+customerAddress.getDistrict()+" "+customerAddress.getProvince()+" "+customerAddress.getPostcode();
                
                } else {
                    selectAddress = request.getParameter("selectAddress");
                }
                request.setAttribute("selectAddress", selectAddress);
                request.setAttribute("deliveryType", deliveryType);
                request.setAttribute("description", description);
                RequestDispatcher pg = request.getRequestDispatcher("payment.jsp");
                pg.forward(request, response);
            } catch (SQLException ex) {
                Logger.getLogger(PaymentServlet.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (statementCustomerAddress != null)
                        statementCustomerAddress.close();
                    if (conn != null)
                        conn.close();
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
