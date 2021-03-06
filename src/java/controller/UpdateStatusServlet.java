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
import javax.sql.DataSource;

@WebServlet(name = "UpdateStatusServlet", urlPatterns = {"/UpdateStatusServlet"})
public class UpdateStatusServlet extends HttpServlet {
    
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
            String item_no = request.getParameter("item_no");
            System.out.println(item_no);
            String shopping_basket_id = request.getParameter("shopping_basket_id");
            System.out.println(shopping_basket_id);
            PreparedStatement pstm=null;
            try {
                if (conn == null || conn.isClosed()){
                    conn = webpro_14_prod.getConnection();
                }
                pstm = conn.prepareStatement("UPDATE order_item SET status = 'received' WHERE item_no=? AND shopping_basket_id=?");
                pstm.setInt(1, Integer.parseInt(item_no));
                pstm.setInt(2, Integer.parseInt(shopping_basket_id));
                pstm.executeUpdate();
                response.sendRedirect("orderStatus.jsp");
            } catch (SQLException ex) {
                Logger.getLogger(UpdateStatusServlet.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (conn != null)
                    conn.close();
                    if (pstm != null)
                        pstm.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UpdateStatusServlet.class.getName()).log(Level.SEVERE, null, ex);
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
