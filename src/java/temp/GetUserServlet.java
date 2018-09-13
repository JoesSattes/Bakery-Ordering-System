/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package temp;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.StoragePath;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.naming.*;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import models.Account;
import models.Seller;

/**
 *
 * @author Sattaya Singkul
 */
@WebServlet(name = "GetUserServlet", urlPatterns = {"/GetUserServlet"})
public class GetUserServlet extends HttpServlet {

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
        //processRequest(request, response);
        doPost(request, response);
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
        PreparedStatement statement = null;
        try {
            if (conn == null || conn.isClosed()) {
                conn = webpro_14_prod.getConnection();
            }
            String text = "Message from servlet -> :"; //message you will recieve
            String profile = request.getParameter("profile");
            String banner = request.getParameter("banner");
            String product = request.getParameter("product");
            String store = request.getParameter("store");
            
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("account");
            String username = account.getUsername();
            System.out.println("START : "+username);
            
            StoragePath storagePath = (StoragePath) session.getAttribute("storagePath");
            storagePath.setUsername(username);
            PrintWriter out = response.getWriter();
            conn.setAutoCommit(false);
            if (profile != null) {
                storagePath.setProfile(profile);
                out.println(text + "profile:" + storagePath.getProfile());
                profile = null;
                statement = conn.prepareStatement("UPDATE account SET profile_photo = ? WHERE username = ?");
                statement.setString(1, storagePath.getProfile());
                statement.setString(2, username);
                statement.executeUpdate();
            }
            if (banner != null) {
                storagePath.setBanner(banner);
                out.println(text + "banner:" + storagePath.getBanner());
                banner = null;
                statement = conn.prepareStatement("UPDATE seller SET banner_photo = ? WHERE account_username = ?");
                statement.setString(1, storagePath.getBanner());
                statement.setString(2, username);
                statement.executeUpdate();
            }
            if (product != null) {
                System.out.println("IN IF :"+product);
                System.out.println("IN IF :"+username);
                storagePath.setProductDetail(product);
                session.setAttribute("storagePath", storagePath);
                out.println(text + "product:" + storagePath.getProductDetail(username).toString());
                product = null;
                System.out.println("FINISH");
            }
            if (store != null) {
                storagePath.setStorePath(store);
                out.println(text + "store:" + storagePath.getStorePath(username).toString());
                store = null;
                statement = conn.prepareStatement("INSERT INTO store_photo VALUES(?, ?)");
                statement.setString(1, username);
                System.out.println(storagePath.getStorePath(storagePath.getUsername()).get(storagePath.getStorePath(storagePath.getUsername()).size()-1));
                statement.setString(2, storagePath.getStorePath(storagePath.getUsername()).get(storagePath.getStorePath(storagePath.getUsername()).size()-1));
                statement.executeUpdate();
                
            }
        } catch (Exception ex) {
            Logger.getLogger(GetUserServlet.class.getName()).log(Level.SEVERE, null, ex);
            try {
                conn.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(GetUserServlet.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                conn.setAutoCommit(true);
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetUserServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
