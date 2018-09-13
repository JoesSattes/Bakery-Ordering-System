package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

@WebServlet(name = "ViewDailyReportServlet", urlPatterns = {"/ViewDailyReportServlet"})
public class ViewDailyReportServlet extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String date = request.getParameter("date");
            System.out.println(date);
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("account");
            System.out.println(account.getUsername());
            String sql = "SELECT DATE_FORMAT(shopping_basket.order_completion_time, '%d') 'date', DATE_FORMAT(shopping_basket.order_completion_time, '%M') 'Month',inventory.name, sum(order_item.amount) 'amount', sum(order_item.price) 'price' FROM order_item JOIN inventory ON (order_item.inventory_id = inventory.id) JOIN shopping_basket ON (order_item.shopping_basket_id = shopping_basket.id) WHERE seller_account_username = '"+account.getUsername()+"' AND DATE(shopping_basket.order_completion_time) = '"+date+"' GROUP BY inventory.id";
            System.out.println(sql);
            request.setAttribute("queryString", sql);
            RequestDispatcher pg = request.getRequestDispatcher("dailyReport.jsp");
            pg.forward(request, response);
            
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
