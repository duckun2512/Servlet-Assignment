package com.example.assignmentjava.util;

import com.example.assignmentjava.entity.Product;
import com.example.assignmentjava.repository.JpaRepository;

import java.util.List;

public class Example {
    public static void main(String[] args) {
        JpaRepository<Product> jpaRepository = new JpaRepository<>(Product.class);
//        Tạo và save vào database
//        Product p = new Product();
//        p.setName("Sp3");
//        p.setPrice(10000);
//        p.setThumbnail("anh3.png");
//        p.setDescription("Sp3");
//        p.setStatus(1);
//        jpaRepository.save(p);

//        Gọi danh sách product
//        List<Product> list = jpaRepository.findAll();
//        for (Product product: list) {
//            System.out.println(product.toString());
//        }

//        Update theo ID
//        Product product = jpaRepository.findById(1);
//        product.setName("Updated product 1");
//        jpaRepository.update(product);

//        Delete theo ID
//        jpaRepository.delete(product.getId());

//        Tìm kiếm product
        List<Product> list = jpaRepository.where("name","=","Sp2");
        for (Product p: list) {
            System.out.println(p.toString());
        }
    }
}
