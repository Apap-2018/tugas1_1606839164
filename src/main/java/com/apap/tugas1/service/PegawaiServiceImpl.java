package com.apap.tugas1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.repository.PegawaiDb;

@Service
@Transactional
public class PegawaiServiceImpl implements PegawaiService {
	
	@Autowired
	private PegawaiDb pegawaiDb;

	@Override
	public PegawaiModel findPegawaiByNip(String nip) {
		return pegawaiDb.findByNip(nip);
	}
}