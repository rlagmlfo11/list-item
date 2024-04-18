package com.sample.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sample.project.dto.QudtlsMasterRepository;
import com.sample.project.entity.QudtlsMaster;

@Service
public class QudtlsMasterService {

	@Autowired
	private QudtlsMasterRepository qudtlsMasterRepository;

	public List<QudtlsMaster> getAllQudtls() {
		return qudtlsMasterRepository.findAll();
	}

	public QudtlsMaster getQudtlsById(Long id) {
		QudtlsMaster getQudtlsById = qudtlsMasterRepository.findById(id).get();
		return getQudtlsById;
	}

	public void updateQudtls(QudtlsMaster qudtlsMaster) {
		qudtlsMasterRepository.save(qudtlsMaster);
	}

	public Page<QudtlsMaster> findPaginated(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		return qudtlsMasterRepository.findAll(pageable);
	}

	public Page<QudtlsMaster> searchQudtls(String keyword, Pageable pageable) {
		if (keyword != null && !keyword.isEmpty()) {
			return qudtlsMasterRepository.searchByKeyword(keyword, pageable);
		} else {
			return qudtlsMasterRepository.findAll(pageable);
		}
	}

	public List<QudtlsMaster> searchByName(String name) {
		return qudtlsMasterRepository.findByName(name);
	}

	public QudtlsMaster createQudtlsMaster(QudtlsMaster qudtlsMaster) {
		return qudtlsMasterRepository.save(qudtlsMaster);
	}

}
