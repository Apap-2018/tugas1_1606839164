package com.apap.tugas1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.service.JabatanService;

@Controller
public class JabatanController {
	
	@Autowired
	private JabatanService jabatanService;
	
	@RequestMapping(value = "/jabatan/tambah", method = RequestMethod.GET)
	private String addJabatan(Model model) {
		model.addAttribute("title", "Tambah Jabatan");
		model.addAttribute("jabatan", new JabatanModel());
		return "descadd-jabatan";
	}
	
	@RequestMapping(value = "/jabatan/tambah", method = RequestMethod.POST)
	private String addJabatanSubmit(@ModelAttribute JabatanModel jabatan, Model model){
		model.addAttribute("title", "Tambah Jabtan");
		jabatanService.addJabatan(jabatan);
		return "add-jabatan";
		
	}
	
	@RequestMapping(value = "/jabatan/view", method = RequestMethod.GET)
	private String viewJabatan(String idJabatan, Model model) {
		model.addAttribute("title", "Lihat Jabatan");
		Long id = Long.parseLong(idJabatan);
		
		JabatanModel jabatan = jabatanService.findJabatanById(id);
		
		
		model.addAttribute("jabatan", jabatan);
		return "detail-jabatan";
	}
	
	@RequestMapping(value = "/jabatan/ubah", method = RequestMethod.GET)
	private String updateJabatan(@RequestParam("idJabatan") long id, Model model) {
		
		model.addAttribute("title", "Ubah Jabatan");
		JabatanModel jabatan = jabatanService.findJabatanById(id);
		model.addAttribute("jabatan", jabatan);
		System.out.println(id);
		
		
		return "update-jabatan";
	}
	
	
	@RequestMapping(value = "/jabatan/ubah", method = RequestMethod.POST)
	private String updateJabatanSubmit(@ModelAttribute JabatanModel jabatan, Model model){
		model.addAttribute("title", "Ubah Jabatan");
		
		jabatanService.addJabatan(jabatan);
		return "add-jabatan";
		
	}
	
	@RequestMapping(value = "/jabatan/hapus", method = RequestMethod.GET)
	private String deleteJabatan(@RequestParam ("idJabatan") long id, Model model){
		model.addAttribute("title", "Hapus Jabatan");
		JabatanModel jabatan = jabatanService.findJabatanById(id);
		
		if(jabatan.getPegawaiList().size() == 0) {
			jabatanService.deleteJabatanById(id);
			return "delsuccess-jabatan";
		}
		
		else {
			return "delfail-jabatan";
		}
	}
		
	@RequestMapping(value = "/jabatan/viewall", method = RequestMethod.GET)
	public String viewAllJabatan(Model model) {
		model.addAttribute("title", "Lihat Semua Jabatan");
		model.addAttribute("listJabatan", jabatanService.findAllJabatan());
		return "viewall-jabatan";
	}
}