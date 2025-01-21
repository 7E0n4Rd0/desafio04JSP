package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.devsuperior.dsmeta.dto.SaleSellerDTO;
import com.devsuperior.dsmeta.dto.SaleSumDTO;
import com.devsuperior.dsmeta.projections.SaleSumProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

	public Page<SaleSellerDTO> getReport(String minDate, String maxDate, String sellerName, Pageable pageable){
		LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());

		LocalDate max = maxDate.equals("") ? today : LocalDate.parse(maxDate);
		LocalDate min = minDate.equals("") ? today.minusYears(1L) : LocalDate.parse(minDate);

		Page<Sale> page = repository.getReport(min, max, sellerName, pageable);
		return page.map(SaleSellerDTO::new);
	}

	public List<SaleSumDTO> getSummary(String minDate, String maxDate){
		LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());

		LocalDate max = maxDate.equals("") ? today : LocalDate.parse(maxDate);
		LocalDate min = minDate.equals("") ? today.minusYears(1L) : LocalDate.parse(minDate);

		List<SaleSumProjection> result = repository.getSummary(min, max);
		List<SaleSumDTO> list = result.stream()
				.map(x -> new SaleSumDTO(x)).collect(Collectors.toList());
		return list;

	}

}
