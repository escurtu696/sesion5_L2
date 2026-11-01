package com.CMEPPS.demo.core.driven_ports;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CMEPPS.demo.core.domain.Todo;

public interface TodoRepository extends JpaRepository < Todo, Long > {
    List < Todo > findByUserName(String user);
}