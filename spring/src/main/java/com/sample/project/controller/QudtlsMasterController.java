package com.sample.project.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sample.project.dto.QudtlsMasterRepository;
import com.sample.project.entity.QudtlsMaster;
import com.sample.project.service.QudtlsMasterService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class QudtlsMasterController {

	LocalDate now = LocalDate.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	String formattedDate = now.format(formatter);

	@Autowired
	private QudtlsMasterService qudtlsMasterService;

	@Autowired
	private QudtlsMasterRepository qudtlsMasterRepository;

	@PostMapping("/storeSearch")
	@ResponseBody
	public ResponseEntity<?> storeSearch(@RequestParam String keyword, HttpSession session) {
		session.setAttribute("lastSearchKeyword", keyword);

		Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
		List<QudtlsMaster> results = qudtlsMasterRepository.searchByKeyword(keyword, pageable).getContent();

		return ResponseEntity.ok(results);
	}

	@GetMapping("/download/csv")
	public void downloadCSV(HttpServletResponse response, HttpSession session) {
		String keyword = (String) session.getAttribute("lastSearchKeyword");

		List<QudtlsMaster> records;
		if (keyword != null && !keyword.isEmpty()) {
			Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
			records = qudtlsMasterRepository.searchByKeyword(keyword, pageable).getContent();
		} else {
			records = qudtlsMasterRepository.findAll();
		}

		generateAndReturnCSV(records, response);
	}

	private void generateAndReturnCSV(List<QudtlsMaster> records, HttpServletResponse response) {
		response.setContentType("text/csv; charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"List_" + formattedDate + ".csv\"");

		try {
			response.getWriter().write("\uFEFF");
			response.getWriter().println("ID,Name,Age,Job,Rank,Reason");
			for (QudtlsMaster record : records) {
				response.getWriter().println(record.getId() + "," + record.getName() + "," + record.getAge() + ","
						+ record.getJob() + "," + record.getRank() + "," + record.getReason());
			}
			response.getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/qudtlsMaster")
	public String listQudtls(Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Page<QudtlsMaster> qudtlsPage = qudtlsMasterService.findPaginated(page, size);

		model.addAttribute("qudtlsemf", qudtlsPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", qudtlsPage.getTotalPages());
		model.addAttribute("totalItems", qudtlsPage.getTotalElements());

		return "qudtlsMaster";
	}

	@GetMapping("/qudtls/details/{id}")
	public String showDetails(@PathVariable("id") Long id, Model model) {
		QudtlsMaster qudtls = qudtlsMasterService.getQudtlsById(id);
		model.addAttribute("qudtls", qudtls);
		return "qudtlsDetails";
	}

	@GetMapping("/qudtlsMaster/search")
	public String searchQudtls(Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String keyword) {
		Pageable pageable = PageRequest.of(page, size);
		Page<QudtlsMaster> qudtlsPage = qudtlsMasterService.searchQudtls(keyword, pageable);

		model.addAttribute("qudtlsemf", qudtlsPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", qudtlsPage.getTotalPages());
		model.addAttribute("totalItems", qudtlsPage.getTotalElements());
		model.addAttribute("searchKeyword", keyword);

		return "qudtlsMaster";
	}

	@GetMapping("/search")
	@ResponseBody
	public ResponseEntity<?> search(@RequestParam String name) {
		List<QudtlsMaster> searchResults = qudtlsMasterService.searchByName(name);
		if (searchResults.isEmpty()) {
			return ResponseEntity.ok(Collections.singletonMap("exists", false));
		} else {
			Map<String, Object> responseBody = new HashMap<>();
			responseBody.put("exists", true);
			responseBody.put("results", searchResults);
			return ResponseEntity.ok(responseBody);
		}
	}

	@PostMapping("/qudtlsRegister")
	public ResponseEntity<?> registerQudtlsMaster(@RequestParam("name") String name, @RequestParam("age") int age,
			@RequestParam("job") String job, @RequestParam("rank") String rank, @RequestParam("reason") String reason) {
		QudtlsMaster qudtlsMaster = new QudtlsMaster();
		qudtlsMaster.setName(name);
		qudtlsMaster.setAge(age);
		qudtlsMaster.setJob(job);
		qudtlsMaster.setRank(rank);
		qudtlsMaster.setReason(reason);

		QudtlsMaster savedQudtlsMaster = qudtlsMasterService.createQudtlsMaster(qudtlsMaster);

		if (savedQudtlsMaster != null) {
			return ResponseEntity.ok("Registration successful");
		} else {
			return ResponseEntity.badRequest().body("Registration failed");
		}
	}

	@GetMapping("/qudtlsRegister")
	public String showRegistrationForm(Model model) {
		model.addAttribute("qudtlsMaster", new QudtlsMaster());
		return "qudtlsRegister";
	}

}
