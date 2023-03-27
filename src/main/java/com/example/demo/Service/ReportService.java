package com.example.demo.Service;

import com.example.demo.models.Booking;
import com.example.demo.repo.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ReportService {
    @Autowired
    private BookingRepository bookingRepository;

    public List<Booking> listAll() {
        return bookingRepository.findAll(Sort.by("tableNumber").ascending());
    }
}
