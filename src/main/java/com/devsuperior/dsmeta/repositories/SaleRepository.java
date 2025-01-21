package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.dto.SaleSellerDTO;
import com.devsuperior.dsmeta.projections.SaleSumProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query(value = "SELECT obj " +
            "FROM Sale obj JOIN FETCH obj.seller " +
            "WHERE obj.date BETWEEN :minDate AND :maxDate " +
            "AND UPPER(obj.seller.name) LIKE UPPER(CONCAT('%',:sellerName,'%')) ",
    countQuery = "SELECT COUNT(obj) FROM Sale obj JOIN obj.seller " +
            "WHERE obj.date BETWEEN :minDate AND :maxDate " +
            "AND UPPER(obj.seller.name) LIKE UPPER(CONCAT('%',:sellerName,'%')) ")
    Page<Sale> getReport(LocalDate minDate, LocalDate maxDate, String sellerName, Pageable pageable);


    @Query(nativeQuery = true, value =
          "SELECT tb_seller.name, SUM(tb_sales.amount) AS total FROM tb_sales " +
            "INNER JOIN tb_seller ON tb_seller.id = tb_sales.seller_id " +
                "WHERE tb_sales.date BETWEEN :minDate AND :maxDate " +
          "GROUP BY tb_seller.name")
    List<SaleSumProjection> getSummary(LocalDate minDate, LocalDate maxDate);
}
