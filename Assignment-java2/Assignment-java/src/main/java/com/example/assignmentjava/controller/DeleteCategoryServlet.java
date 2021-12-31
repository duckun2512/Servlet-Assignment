package com.example.assignmentjava.controller;

import com.example.assignmentjava.entity.Category;
import com.example.assignmentjava.entity.Product;
import com.example.assignmentjava.repository.JpaRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class DeleteCategoryServlet extends HttpServlet {
    private JpaRepository<Category> categoryJpaRepository = new JpaRepository<>(Category.class);
    private JpaRepository<Product> productJpaRepository = new JpaRepository<>(Product.class);

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Category category = categoryJpaRepository.findById(id);
            List<Product> productListByCategoryId = productJpaRepository.findByForeignKeyId(id);
            if (category == null){
                resp.getWriter().println("Category is not found!");
            }else if (productListByCategoryId == null){
                categoryJpaRepository.delete(id);
                resp.getWriter().println("Delete Successful!");
            } else {
                for (Product product: productListByCategoryId) {
                    productJpaRepository.delete(product.getId());
                }
                categoryJpaRepository.delete(id);
                resp.getWriter().println("Delete Successful!");
            }
        }catch (Exception e){
            resp.getWriter().println("Bad request");
        }
    }
}