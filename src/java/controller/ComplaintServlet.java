package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import models.Account;
import models.Complaint;

@WebServlet(name = "ComplaintServlet", urlPatterns = {"/ComplaintServlet"})
public class ComplaintServlet extends HttpServlet {

    private Connection conn;

    public void init() {
        conn = (Connection) getServletContext().getAttribute("connection");
    }

    private DataSource getWebpro_14_prod() throws NamingException {
        Context c = new InitialContext();
        return (DataSource) c.lookup("java:comp/env/webpro_14_prod");
    }
    
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            request.setCharacterEncoding("UTF-8");
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("account");
            String username = account.getUsername();
            PreparedStatement preparedStatement = null;
            String description = request.getParameter("complaint");
            String select = request.getParameter("select");
            String head = request.getParameter("head");
            try {
                if (conn == null || conn.isClosed()) {
                    conn = getWebpro_14_prod().getConnection();
                }
                description = "<"+select+"> "+head+" : "+description;
                Complaint complaint = new Complaint();
                complaint.setComplaintDescription(description);
                complaint.setAccountUsername(username);
                
                preparedStatement = conn.prepareStatement("INSERT INTO complaint VALUES (null, ?, NOW(), ?)");
                preparedStatement.setString(1, complaint.getComplaintDescription());
                preparedStatement.setString(2, complaint.getAccountUsername());
                preparedStatement.executeUpdate();
                
            } catch (Exception ex) {
                Logger.getLogger(ComplaintServlet.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (conn != null)
                        conn.close();
                    if (preparedStatement != null)
                        preparedStatement.close();
                    String complete = "complete";
                    request.setAttribute("complete", complete);
                    RequestDispatcher pg = request.getRequestDispatcher("complaint.jsp");
                    pg.forward(request, response);
                } catch (SQLException ex) {
                    Logger.getLogger(ComplaintServlet.class.getName()).log(Level.SEVERE, null, ex);
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
