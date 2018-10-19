package com.apap.tugas1.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.service.InstansiService;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.service.ProvinsiService;


@Controller
public class PegawaiController {
	@Autowired
	private PegawaiService pegawaiService;
	
	@Autowired
	private JabatanService jabatanService;
	
	@Autowired
	private InstansiService instansiService;

	@Autowired
	private ProvinsiService provinsiService;
	
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
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.GET)
	private String add(Model model) {
		model.addAttribute("pegawai", new PegawaiModel());
		model.addAttribute("listOfProvinsi", provinsiService.getListProv());
		model.addAttribute("listOfJabatan", jabatanService.getJabatanList());
		return "descadd-pegawai";
	}

	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST)
	private String addPegawai(@ModelAttribute PegawaiModel pegawai, Model model) {
		String nip = "";
		nip += pegawai.getInstansi().getId();

		String[] tlSplit= pegawai.getTanggalLahir().toString().split("-");
		nip += tlSplit[2] + tlSplit[1] + tlSplit[0].substring(2, 4);

		System.out.println(nip);
		nip += pegawai.getTahunMasuk();

		int count = 1;
		for(PegawaiModel i: pegawai.getInstansi().getPegawaiInstansi()) {
			if (i.getTanggalLahir().equals(pegawai.getTanggalLahir()) && i.getTahunMasuk().equals(pegawai.getTahunMasuk())){
				count += 1;
				if (count >= 10) {
					count = count % 10;
				}
			}
		}
		nip += "0" + count;
		pegawai.setNip(nip);
		pegawaiService.addPegawai(pegawai);;
		model.addAttribute("pegawai", pegawai);
		return "add-pegawai";
	}
	
	@RequestMapping("/pegawai/termuda-tertua")
	private String viewTuaMuda(@RequestParam("idInstansi") long id, Model model) {
		InstansiModel instansi = instansiService.getInstansiById(id);
		List<PegawaiModel> listPegawai = instansi.getPegawaiInstansi();
		
		PegawaiModel pegawaiTermuda;
		PegawaiModel pegawaiTertua;
		
		if(listPegawai.size() > 0) {
			pegawaiTermuda = listPegawai.get(0);
			pegawaiTertua = listPegawai.get(0);
			
			for(PegawaiModel pegawai : listPegawai) {
				Date tanggalLahir = pegawai.getTanggalLahir();
				if(tanggalLahir.after(pegawaiTermuda.getTanggalLahir())) {
					pegawaiTermuda = pegawai;
				}
				if(tanggalLahir.before(pegawaiTertua.getTanggalLahir())) {
					pegawaiTertua = pegawai;
				}
			}
			model.addAttribute("pegawaiTermuda", pegawaiTermuda);
			model.addAttribute("pegawaiTertua", pegawaiTertua);
		}
		
		return "view-tuamuda";
		}
}