package com.apap.tugas1.service;

import java.util.List;

import com.apap.tugas1.model.JabatanModel;

public interface JabatanService {
	void addJabatan(JabatanModel jabatan);
	JabatanModel findJabatanById(long id);
	List<JabatanModel> getJabatanList();
	void deleteJabatanById(long idJabatan);
	List<JabatanModel> findAllJabatan();
}