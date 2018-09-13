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
import java.sql.Statement;
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
import models.User;

/**
 *
 * @author User
 */
@WebServlet(name = "SigninAdminServlet", urlPatterns = {"/SigninAdminServlet"})
public class SigninAdminServlet extends HttpServlet {

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
            Statement statement = null;
            ResultSet rs = null;
            PreparedStatement preparedStatement = null;

            try {
                if (conn == null || conn.isClosed()) {
                    conn = webpro_14_prod.getConnection();
                }
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                preparedStatement = conn.prepareStatement("SELECT * FROM account WHERE username=? AND password=? AND account_type is null");
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                rs = preparedStatement.executeQuery();

                //filter var
                HttpSession session = request.getSession();

                //add filter var
                boolean customerFlag = false;
                boolean storeFlag = false;
                boolean adminFlag = false;

                if (rs.next()) {
                    customerFlag = false;
                    storeFlag = false;
                    adminFlag = true;
                    session.setAttribute("admin", username);
                    session.setAttribute("customerFlag", customerFlag);
                    session.setAttribute("storeFlag", storeFlag);
                    session.setAttribute("adminFlag", adminFlag);
                    response.sendRedirect("adminIndex.jsp");
                } else {
                    response.sendRedirect("loginAdmin.jsp");
                }
            } catch (Exception ex) {
                Logger.getLogger(SigninAdminServlet.class.getName()).log(Level.SEVERE, null, ex);
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
