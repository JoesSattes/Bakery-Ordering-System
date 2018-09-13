package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            int total = 12;
            String search_from = request.getParameter("search_from");
            if (search_from == null){
                search_from = "product";
            }
            String keyword = request.getParameter("keyword");
            String sort = request.getParameter("sort");
            String search = "";
            
            if (search_from.equals("product")) {
                search = "SELECT DISTINCT id, shop_name, name, price, inventory.description, stock, seller_account_username FROM inventory JOIN seller ON inventory.seller_account_username = seller.account_username LEFT OUTER JOIN tag ON tag.inventory_id = inventory.id ";
            } else {
                search = "SELECT shop_name, address, description, type, account_username FROM seller JOIN store_type ON seller.store_type_id = store_type.id ";
            }
            
            if (!keyword.equals("")) {
                if (search_from.equals("product")) {
                    search += " WHERE name LIKE '%" + keyword + "%' OR tag LIKE '%" + keyword + "%'";
                } else {
                    search += " WHERE shop_name LIKE '%" + keyword + "%' OR type LIKE '"+ keyword +"'";
                }
            }
            if (sort == null){
                sort = "name";
            }
            if (sort.equals("name")){
                search += "ORDER BY name";
            } else if (sort.equals("shop name (A-Z)")){
                search += "ORDER BY shop_name";
            } else if (sort.equals("shop name (Z-A)")){
                search += "ORDER BY shop_name DESC";
            } else if (sort.equals("price (max-min)")){
                search += "ORDER BY price desc";
            } else if (sort.equals("price (min-max)")){
                search += "ORDER BY price";
            }
            String url = request.getRequestURL().append('?').append(request.getQueryString()).substring(request.getContextPath().length()+22);
            //String url = request.getRequestURL().append('?').append(request.getQueryString()).substring(request.getContextPath().length()+37);
            String spageid = request.getParameter("page");
            System.out.println(url);
            System.out.println("spageid "+spageid);
            int pageid = 1;
            if (spageid != null){
                pageid = Integer.parseInt(spageid);
            }
            
            if (pageid != 1){
                pageid -= 1;
                pageid = pageid*total+1;
            }
            
            url = url.substring(0, url.length()-6-spageid.length()); // delete "&page=__"
            System.out.println(url);
            String searchAll = search;
            search += " LIMIT "+Integer.toString(pageid-1)+", "+Integer.toString(total);
            request.setAttribute("url", url);
            System.out.println(search);
            request.setAttribute("search", search);
            request.setAttribute("keyword", keyword);
            request.setAttribute("searchAll", searchAll);
            request.setAttribute("search_from", search_from);
            request.setAttribute("before_page", spageid);
            RequestDispatcher pg = request.getRequestDispatcher("viewProduct.jsp");
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
