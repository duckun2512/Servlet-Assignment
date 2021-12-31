package com.example.assignmentjava.controller;

import com.example.assignmentjava.entity.Product;
import com.example.assignmentjava.repository.JpaRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditProductServlet extends HttpServlet {

    JpaRepository<Product> productJpaRepository = new JpaRepository<>(Product.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Product product = productJpaRepository.findById(id);
            if (product == null) {
                resp.getWriter().println("Product is not found!");
            } else {
                req.setAttribute("product", product);
                req.getRequestDispatcher("/admin/product/edit.jsp").forward(req, resp);
            }
        } catch (Exception ex) {
            resp.getWriter().println("Bad request");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setCharacterEncoding("UTF-8");
            int id = Integer.parseInt(req.getParameter("id"));
            Product product = productJpaRepository.findById(id);
            if (product == null) {
                resp.getWriter().println("Product is not found or has been deleted!");
            } else {
                String name = req.getParameter("name");
                double price = Double.parseDouble(req.getParameter("price"));
                String description = req.getParameter("description");
                String thumbnail = req.getParameter("thumbnail");
                Date editDate = new Date();
                int status = Integer.parseInt(req.getParameter("status"));
                // Update giá trị mới
                product.setName(name);
                product.setPrice(price);
                product.setThumbnail(thumbnail);
                product.setDescription(description);
                product.setEditDate(editDate);
                product.setStatus(status);
                // Update database
                productJpaRepository.update(product);
                resp.sendRedirect("/admin/product/list");
            }
        } catch (Exception e) {
            resp.getWriter().println("Error");
        }
    }
}
