package org.creditto.core_banking.domain.recipient.repository;

import org.creditto.core_banking.domain.recipient.entity.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipientRepository extends JpaRepository<Recipient, Long> {
}
