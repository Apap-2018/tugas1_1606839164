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
	
	@Override
	public void addPegawai(PegawaiModel pegawai) {
		pegawaiDb.save(pegawai);
	}

	@Override
	public void updatePegawai(PegawaiModel pegawai, long idPegawai) {
		PegawaiModel res = pegawaiDb.findById(idPegawai).get();
		
		res.setNama(pegawai.getNama());
		res.setNip(pegawai.getNip());
		res.setTempatLahir(pegawai.getTempatLahir());
		res.setTanggalLahir(pegawai.getTanggalLahir());
		res.setInstansi(pegawai.getInstansi());
		res.setJabatanList(pegawai.getJabatanList());
		res.setTahunMasuk(pegawai.getTahunMasuk());
		
		pegawaiDb.save(res);
	}

	@Override
	public void searchPegawai(long id) {
		// TODO Auto-generated method stub
		
	}
}