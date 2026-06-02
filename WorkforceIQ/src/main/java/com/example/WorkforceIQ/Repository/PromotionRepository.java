package com.example.WorkforceIQ.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.WorkforceIQ.entity.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {

}