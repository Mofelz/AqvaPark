package com.example.demo.repo;

import com.example.demo.models.Booking;
import com.example.demo.models.Cart;
import com.example.demo.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Iterable<Cart> findAllByBooking(Booking booking);
    Cart findCartByBookingAndProduct(Booking booking, Product product);
}
