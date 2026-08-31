package com.ribeiroautomoveis.ecommercecarros.repository;

import com.ribeiroautomoveis.ecommercecarros.model.Carro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarroRepository extends JpaRepository<Carro, Long> {

    List<Carro> findByModeloContainingIgnoreCase(String modelo);

}