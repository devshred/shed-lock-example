package org.devshred.lock

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant
import javax.persistence.EntityManager
import javax.persistence.LockModeType
import javax.persistence.PersistenceContext
import javax.persistence.PersistenceContextType.TRANSACTION
import javax.transaction.Transactional


@Component
class SimpleTaskScheduler(@PersistenceContext(type = TRANSACTION) val entityManager: EntityManager) {
    private val log = LoggerFactory.getLogger(SimpleTaskScheduler::class.java)

    @Transactional
    @Scheduled(cron = "0/5 * * * * ?")
    @SchedulerLock(name = "SimpleTaskScheduler_updatePerson", lockAtLeastFor = "PT4S", lockAtMostFor = "PT20S")
    fun updatePerson() {
        val person = entityManager.find(Person::class.java, 1, LockModeType.OPTIMISTIC)
        log.info("found person ${person.name} (v${person.version})")
        person.lastUpdated = Instant.now()
        entityManager.persist(person)
    }
}