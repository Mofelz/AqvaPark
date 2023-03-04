package com.example.demo.repo;

import com.example.demo.models.Booking;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookingRepository extends JpaRepository<Booking, Long> {
    Iterable<Booking> findAllByUser(User user);
    Booking findTopByTableNumberOrderByIdDesc(int tableNumber);
    Booking findTopByUserOrderByIdDesc(User user);
}

