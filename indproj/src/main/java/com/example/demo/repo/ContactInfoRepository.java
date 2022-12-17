package com.example.demo.repo;

import com.example.demo.models.ContactInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContactInfoRepository extends CrudRepository<ContactInfo, Long> {
    List<ContactInfo> findByPochta(String pochta);
}
