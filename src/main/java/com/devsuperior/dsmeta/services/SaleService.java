package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	public Page<SaleMinDTO> getReport(String minDate, String maxDate, String sellerName, Pageable pageable){

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		LocalDate result = today.minusYears(1L);

		if(minDate.isEmpty() && maxDate.isEmpty()){
			maxDate = today.format(dateTimeFormatter);
			minDate = result.format(dateTimeFormatter);
		}else if(minDate.isEmpty()){
			minDate = result.format(dateTimeFormatter);
		}else if(maxDate.isEmpty()){
			maxDate = today.format(dateTimeFormatter);
		}

		Page<Sale> page = repository.getReport(LocalDate.parse(minDate), LocalDate.parse(maxDate), sellerName, pageable);

		return page.map(SaleMinDTO::new);
	}

}
