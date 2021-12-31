package com.example.assignmentjava.controller;

import com.example.assignmentjava.entity.Category;
import com.example.assignmentjava.entity.Product;
import com.example.assignmentjava.repository.JpaRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CreateProductServlet extends HttpServlet {

    private JpaRepository<Product> productJpaRepository = new JpaRepository<>(Product.class);
    private JpaRepository<Category> categoryJpaRepository = new JpaRepository<>(Category.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Category> categoryList = categoryJpaRepository.findAll();

        req.setAttribute("categoryList", categoryList);
        req.getRequestDispatcher("/admin/product/form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setCharacterEncoding("UTF-8");
            String name = req.getParameter("name");
            double price = Double.parseDouble(req.getParameter("price"));
            String description = req.getParameter("description");
            String thumbnail = req.getParameter("thumbnail");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date startedDate = new Date();
            int categoryId = Integer.parseInt(req.getParameter("categoryId"));
            int status = Integer.parseInt(req.getParameter("status"));

            // Validate data
            Product product = new Product(name, description, thumbnail, price, startedDate, status);
            if (product.isValid()) {
                productJpaRepository.save(product);
                resp.sendRedirect("/admin/product/list");
            } else {
                HashMap<String, String> errors = product.getErrors();
                List<Category> categoryList = categoryJpaRepository.findAll();

                req.setAttribute("errors", errors);
                req.setAttribute("categoryList", categoryList);
                // Trả về thông tin product bị lỗi
                req.setAttribute("product", product);
                req.getRequestDispatcher("/admin/product/form.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Error");
        }
    }
}
