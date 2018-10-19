package com.apap.tugas1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.PegawaiService;


@Controller
public class PegawaiController {
	@Autowired
	private PegawaiService pegawaiService;
	
	@Autowired
	private JabatanService jabatanService;
	
	@RequestMapping("/")
	private String home(Model model) {
		model.addAttribute("jabatanList", jabatanService.getJabatanList());
		return "home";
	}
	
	@RequestMapping("/pegawai")
	private String viewPegawai(@RequestParam("nip") String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.findPegawaiByNip(nip);
		
		double gajiAwal = 0;
		double tunjangan = pegawai.getInstansi().getProvinsi().getPresentaseTunjangan();
		for(JabatanModel jabatan : pegawai.getJabatanList()) {
			double gaji = jabatan.getGajiPokok();
			if(gajiAwal < gaji) {
				gajiAwal = gaji;
			}
		}
		
		double gajiTotal = gajiAwal + ((tunjangan/100) * gajiAwal);
		
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("gajiPegawai", gajiTotal);
		return "view-pegawai";
	}
}