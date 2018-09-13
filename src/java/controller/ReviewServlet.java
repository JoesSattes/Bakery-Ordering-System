package controller;

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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import models.*;

@WebServlet(name = "ReviewServlet", urlPatterns = {"/ReviewServlet"})
public class ReviewServlet extends HttpServlet {

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
            String comment = request.getParameter("comment");
            String voteString = request.getParameter("rating");
            String inventoryId = request.getParameter("inventoryId");
            int itemNo = Integer.parseInt(request.getParameter("itemNo"));
            int shoppingBasketId = Integer.parseInt(request.getParameter("shoppingBasketId"));
            ItemInBasket item = new ItemInBasket();
            item.setItemNo(itemNo);
            item.setShoppingBasketId(shoppingBasketId);
            Review review = new Review();
            review.setComment(comment);
            if (voteString != null){
                int vote = Integer.parseInt(voteString);
                review.setVote(vote);
            }
            PreparedStatement statementReview = null;
            
            try {
                if (conn == null || conn.isClosed()){
                    conn = webpro_14_prod.getConnection();
                }
                statementReview = conn.prepareStatement("INSERT INTO review VALUES (null, ?, ?, ?, ?)");
                statementReview.setInt(1, review.getVote());
                statementReview.setString(2, review.getComment());
                statementReview.setInt(3, item.getShoppingBasketId());
                statementReview.setInt(4, item.getItemNo());
                statementReview.executeUpdate();
                response.sendRedirect("productDetail?id="+inventoryId);
            } catch (SQLException ex) {
                Logger.getLogger(ReviewServlet.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (statementReview != null)
                        statementReview.close();
                    if (conn != null)
                        conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SignupServlet.class.getName()).log(Level.SEVERE, null, ex);
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
