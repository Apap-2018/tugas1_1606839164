package com.apap.tugas1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apap.tugas1.model.PegawaiModel;

public interface PegawaiDb extends JpaRepository<PegawaiModel, Long>{
	PegawaiModel findByNip(String nip);
}