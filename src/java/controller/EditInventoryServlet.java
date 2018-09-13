/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import models.Account;
import models.Product;
import models.StoragePath;

/**
 *
 * @author Sattaya Singkul
 */
@WebServlet(name = "EditInventoryServlet", urlPatterns = {"/EditInventoryServlet"})
public class EditInventoryServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    //Get Connection Database
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
            try {
                if (conn == null || conn.isClosed()) {
                    conn = webpro_14_prod.getConnection();
                }
                request.setCharacterEncoding("UTF-8");
                String modeAccess = request.getParameter("modeAccess");
                HttpSession session = request.getSession();
                Account account = (Account) session.getAttribute("account");
                String username = account.getUsername();

                String buttonSubmit = request.getParameter("submit");
                StoragePath storagePath = (StoragePath) session.getAttribute("storagePath");
                PreparedStatement pstm;
                PreparedStatement preparedStatement;
                String sqlInventory;
                if (modeAccess != null) {
                    if (modeAccess.contains("delete")) {
                        modeAccess = modeAccess.replace("delete", "");
                        sqlInventory = "DELETE FROM inventory WHERE id=?";
                        pstm = conn.prepareStatement(sqlInventory);
                        pstm.setInt(1, Integer.parseInt(modeAccess));
                        pstm.executeUpdate();
                    }
                }
                if (buttonSubmit != null) {
                    if (buttonSubmit.equals("Send")) {
                        String name = request.getParameter("productName");
                        double price = Double.parseDouble(request.getParameter("productPrice"));
                        String description = request.getParameter("productDescription");
                        int stock = Integer.parseInt(request.getParameter("productStock"));
                        String id = request.getParameter("productId");
                        System.out.println(id);
                        if (request.getParameter("productMinimum").equals("")) {
                            int minimum = 0;
                        } else {
                            int minimum = Integer.parseInt(request.getParameter("productMinimum"));
                        }
                        double delivery = Double.parseDouble(request.getParameter("productDelivery"));
                        Product product = new Product();
                        if (modeAccess.equals("add")) {
                            product.setProductName(name);
                            product.setProductPrice(price);
                            product.setProductStock(stock);
                            product.setProductDescription(description);
                            product.setDeliveryCost(delivery);
                            sqlInventory = "INSERT INTO inventory(name, price, description, stock, seller_account_username, minimum_amount, delivery_cost) VALUES(?,?,?,?,?,?,?)";
                            pstm = conn.prepareStatement(sqlInventory);
                            pstm.setString(1, product.getProductName());
                            pstm.setDouble(2, product.getProductPrice());
                            pstm.setString(3, product.getProductDescription());
                            pstm.setInt(4, product.getProductStock());
                            pstm.setString(5, username);
                            if (!request.getParameter("productMinimum").equals("")) {
                                int minimum = Integer.parseInt(request.getParameter("productMinimum"));
                                product.setMinimum(minimum);
                                pstm.setInt(6, product.getMinimum());
                            } else {
                                pstm.setString(6, null);
                            }
                            pstm.setDouble(7, product.getDeliveryCost());
                            pstm.executeUpdate();
                            sqlInventory = "SELECT id FROM inventory WHERE seller_account_username=? AND name=?";

                            pstm = conn.prepareStatement(sqlInventory);
                            pstm.setString(1, username);
                            pstm.setString(2, product.getProductName());
                            ResultSet rs = pstm.executeQuery();
                            rs.next();
                            if (storagePath.getProductDetail(username) != null) {
                                for (int i = 0; i < storagePath.getProductDetail(username).size(); i++) {
                                    String path = storagePath.getProductDetailIndex(username, i);
                                    preparedStatement = conn.prepareStatement("INSERT INTO product_photo VALUES (?,?)");
                                    preparedStatement.setInt(1, rs.getInt("id"));
                                    preparedStatement.setString(2, path);
                                    preparedStatement.executeUpdate();
                                }
                                storagePath.clearAllProductDetail();
                            }
                            session.setAttribute("storagePath", storagePath);
                        } else if (modeAccess.equals("delete")) {
                            sqlInventory = "DELETE FROM inventory WHERE id=?";
                            int intId = Integer.parseInt(id);
                            product.setProductId(intId);
                            pstm = conn.prepareStatement(sqlInventory);
                            pstm.setInt(1, product.getProductId());
                            pstm.executeUpdate();
                        } else if (modeAccess.equals("edit")) {
                            int intId = Integer.parseInt(id);
                            product.setProductId(intId);
                            product.setProductName(name);
                            product.setProductPrice(price);
                            product.setProductStock(stock);
                            product.setProductDescription(description);
                            product.setDeliveryCost(delivery);
                            sqlInventory = "UPDATE inventory SET name=?, price=?, description=?, minimum_amount=?, delivery_cost=?, stock=? WHERE seller_account_username=? and id=?;";
                            pstm = conn.prepareStatement(sqlInventory);
                            pstm.setString(1, product.getProductName());
                            pstm.setDouble(2, product.getProductPrice());
                            pstm.setString(3, product.getProductDescription());
                            if (!request.getParameter("productMinimum").equals("")) {
                                int minimum = Integer.parseInt(request.getParameter("productMinimum"));
                                product.setMinimum(minimum);
                                pstm.setInt(4, product.getMinimum());
                            } else {
                                pstm.setString(4, null);
                            }
                            pstm.setDouble(5, product.getDeliveryCost());
                            pstm.setInt(6, product.getProductStock());
                            pstm.setString(7, username);
                            pstm.setInt(8, intId);
                            pstm.executeUpdate();
                            if (storagePath.getProductDetail(username) != null) {
                                for (int i = 0; i < storagePath.getProductDetail(username).size(); i++) {
                                    String path = storagePath.getProductDetailIndex(username, i);
                                    preparedStatement = conn.prepareStatement("INSERT INTO product_photo VALUES (?,?)");
                                    preparedStatement.setInt(1, intId);
                                    preparedStatement.setString(2, path);
                                    preparedStatement.executeUpdate();
                                }
                                storagePath.clearAllProductDetail();
                            }
                            session.setAttribute("storagePath", storagePath);

                        }
                    }
                }
                response.sendRedirect("editShop.jsp");
            } catch (SQLException ex) {
                Logger.getLogger(EditInventoryServlet.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(EditInventoryServlet.class.getName()).log(Level.SEVERE, null, ex);
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
