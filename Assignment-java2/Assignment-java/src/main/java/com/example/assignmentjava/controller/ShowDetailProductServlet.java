package com.example.assignmentjava.controller;

import com.example.assignmentjava.entity.Product;
import com.example.assignmentjava.repository.JpaRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShowDetailProductServlet extends HttpServlet {

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
                req.getRequestDispatcher("/admin/product/detail.jsp").forward(req, resp);
            }
        } catch (Exception ex) {
            resp.getWriter().println("Bad request");
        }
    }
}
