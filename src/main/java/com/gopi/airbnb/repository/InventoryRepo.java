package com.gopi.airbnb.repository;

import com.gopi.airbnb.entitys.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepo extends JpaRepository<Inventory,Long> {
}
