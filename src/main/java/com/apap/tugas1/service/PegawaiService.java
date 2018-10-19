package com.apap.tugas1.service;

import com.apap.tugas1.model.PegawaiModel;

public interface PegawaiService {
	PegawaiModel findPegawaiByNip(String nip);
}