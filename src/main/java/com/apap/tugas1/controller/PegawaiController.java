package com.apap.tugas1.controller;

import java.util.ArrayList;
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
		model.addAttribute("title", "Home");
		model.addAttribute("listJabatan", jabatanService.getJabatanList());
		model.addAttribute("listInstansi", instansiService.getInstansiList());
		return "home";
	}
	
	@RequestMapping(value = "/pegawai", method = RequestMethod.GET)
	private String viewPegawai(@RequestParam("nip") String nip, Model model) {
		model.addAttribute("title", "Lihat Pegawai");
		PegawaiModel pegawai = pegawaiService.findPegawaiByNip(nip);
		
		double gajiAwal = 0;
		double tunjangan = pegawai.getInstansi().getProvinsi().getPresentaseTunjangan();
		for(JabatanModel jabatan : pegawai.getJabatanList()) {
			double gaji = jabatan.getGajiPokok();
			if(gajiAwal < gaji) {
				gajiAwal = gaji;
			}
		}
		
		double gajiTotal = Math.round(gajiAwal + (tunjangan * 0.01 * gajiAwal));
		
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("gajiPegawai", gajiTotal);
		return "view-pegawai";
	}
	
    @RequestMapping(value = "/pegawai/ubah")
    public String ubahPegawai (@RequestParam(value = "nip") String nip, Model model) {
    	model.addAttribute("title", "Ubah Pegawai");
        PegawaiModel pegawai = pegawaiService.findPegawaiByNip(nip);

        model.addAttribute("listProvinsi", provinsiService.getListProv());
        model.addAttribute("listInstansi", instansiService.getInstansiList());
        model.addAttribute("listJabatan", jabatanService.findAllJabatan());
        model.addAttribute("pegawai", pegawai);
        return "update-pegawai";
    }
    
    @RequestMapping(value = "/pegawai/ubah", method = RequestMethod.POST)
    private String ubahPegawaiPost (@ModelAttribute PegawaiModel pegawai, Model model) {
    	model.addAttribute("title", "Ubah Pegawai");
        String nip = "";
        nip += pegawai.getInstansi().getId();

        String[] tglLahir = pegawai.getTanggalLahir().toString().split("-");
        String tglLahirString = tglLahir[2] + tglLahir[1] + tglLahir[0].substring(2, 4);
        nip += tglLahirString;

        nip += pegawai.getTahunMasuk();
        int cnt = 1;
        for (PegawaiModel pegawaiInstansi : pegawai.getInstansi().getPegawaiInstansi()) {
            if (pegawaiInstansi.getTahunMasuk().equals(pegawai.getTahunMasuk()) && pegawaiInstansi.getTanggalLahir().equals(pegawai.getTanggalLahir()) && pegawaiInstansi.getId() != pegawai.getId()) {
                cnt += 1;
            }
        }
        nip += "0" + cnt;
        pegawai.setNip(nip);
        pegawaiService.addPegawai(pegawai);
        model.addAttribute("pegawai", pegawai);
        return "update-pegawai-success";
    }
    
	@RequestMapping(value= "/pegawai/cari", method=RequestMethod.GET)
	private String cariPegawaiPost(
			@RequestParam(value="idProvinsi", required = false) String idProvinsi,
			@RequestParam(value="idInstansi", required = false) String idInstansi,
			@RequestParam(value="idJabatan", required = false) String idJabatan,
			Model model) {
		
		model.addAttribute("title", "Cari Pegawai");
		model.addAttribute("listProvinsi", provinsiService.getListProv());
		model.addAttribute("listInstansi", instansiService.getInstansiList());
		model.addAttribute("listJabatan", jabatanService.getJabatanList());
		List<PegawaiModel> listPegawai = pegawaiService.getListPegawai();
		
		if ((idProvinsi==null || idProvinsi.equals("")) && (idInstansi==null||idInstansi.equals("")) && (idJabatan==null||idJabatan.equals(""))) {
			return "search-pegawai";
		}
		else {
			if (idProvinsi!=null && !idProvinsi.equals("")) {
				List<PegawaiModel> temp = new ArrayList<PegawaiModel>();
				for (PegawaiModel peg: listPegawai) {
					if (((Long)peg.getInstansi().getProvinsi().getId()).toString().equals(idProvinsi)) {
						temp.add(peg);
					}
				}
				listPegawai = temp;
				model.addAttribute("idProvinsi", Long.parseLong(idProvinsi));
			}
			else {
				model.addAttribute("idProvinsi", "");
			}
			if (idInstansi!=null&&!idInstansi.equals("")) {
				List<PegawaiModel> temp = new ArrayList<PegawaiModel>();
				for (PegawaiModel peg: listPegawai) {
					if (((Long)peg.getInstansi().getId()).toString().equals(idInstansi)) {
						temp.add(peg);
					}
				}
				listPegawai = temp;
				model.addAttribute("idInstansi", Long.parseLong(idInstansi));
			}
			else {
				model.addAttribute("idInstansi", "");
			}
			if (idJabatan!=null&&!idJabatan.equals("")) {
				List<PegawaiModel> temp = new ArrayList<PegawaiModel>();
				for (PegawaiModel peg: listPegawai) {
					for (JabatanModel jabatan:peg.getJabatanList()) {
						if (((Long)jabatan.getId()).toString().equals(idJabatan)) {
							temp.add(peg);
							break;
						}
					}
					
				}
				listPegawai = temp;
				model.addAttribute("idJabatan", Long.parseLong(idJabatan));
			}
			else {
				model.addAttribute("idJabatan", "");
			}
		}
		model.addAttribute("listPegawai",listPegawai);
		return "search-pegawai";	
	}
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.GET)
	private String addPegawai(Model model) {
		model.addAttribute("title", "Tambah Pegawai");
		PegawaiModel pegawai = new PegawaiModel();
		model.addAttribute("pegawai", new PegawaiModel());
		pegawai.setInstansi(new InstansiModel());
		
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("instansiList", instansiService.getInstansiList());
		model.addAttribute("listProvinsi", provinsiService.getListProv());
		model.addAttribute("listJabatan", jabatanService.findAllJabatan());
		return "descadd-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST)
		private String addPegawaiSubmit(@ModelAttribute PegawaiModel pegawai, Model model) {
			model.addAttribute("title", "Tambah Pegawai");
			String NIP = ""; 
			NIP += pegawai.getInstansi().getId();
		
			String[]tanggalLahir = pegawai.getTanggalLahir().toString().split("-");
			String tanggalLahirStr = tanggalLahir[2] + tanggalLahir[1] + tanggalLahir[0].substring(2, 4);
			
			NIP += tanggalLahirStr;
			NIP += pegawai.getTahunMasuk();
		
			int counter = 1;
			for(PegawaiModel instansiPegawai:pegawai.getInstansi().getPegawaiInstansi()) {
				if(instansiPegawai.getTahunMasuk().equals(pegawai.getTahunMasuk()) && instansiPegawai
						.equals(pegawai.getTanggalLahir())) {
					counter += 1;
				}
			}
			
			NIP += "0" + counter;
			pegawai.setNip(NIP);
			pegawaiService.addPegawai(pegawai);
			model.addAttribute("pegawai", pegawai);
			return "add-pegawai";
}
	
	@RequestMapping("/pegawai/termuda-tertua")
	private String viewTuaMuda(@RequestParam("idInstansi") long id, Model model) {
		model.addAttribute("title", "Lihat Tua Muda");
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