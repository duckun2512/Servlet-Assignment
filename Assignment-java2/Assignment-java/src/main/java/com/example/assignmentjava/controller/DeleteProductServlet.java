package com.example.assignmentjava.controller;

import com.example.assignmentjava.entity.Product;
import com.example.assignmentjava.repository.JpaRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteProductServlet extends HttpServlet {
    JpaRepository<Product> productJpaRepository = new JpaRepository<>(Product.class);

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Product product = productJpaRepository.findById(id);
            if (product == null) {
                resp.getWriter().println("Product is not found!");
            } else {
                productJpaRepository.delete(id);
                resp.getWriter().println("Delete Successful!");
            }
        } catch (Exception ex) {
            resp.getWriter().println("Bad request");
        }
    }
}
