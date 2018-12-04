package com.felixpy.codetrial.wishtodo.repository;

import com.felixpy.codetrial.wishtodo.domain.Wisher;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Wisher entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WisherRepository extends JpaRepository<Wisher, Long> {

}
