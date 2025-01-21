package com.devsuperior.dsmeta.dto;

import com.devsuperior.dsmeta.projections.SaleSumProjection;

public class SaleSumDTO {
    private String name;
    private Double amount;

    public SaleSumDTO(String name, Double sum) {
        this.name = name;
        this.amount = sum;
    }

    public SaleSumDTO(SaleSumProjection projection){
        name = projection.getName();
        amount = projection.getTotal();
    }

    public String getName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSum(Double amount) {
        this.amount = amount;
    }
}
