package com.alba.cycleruncoach.repository.jpa;

import com.alba.cycleruncoach.domain.DailyCheckIn;
import com.alba.cycleruncoach.repository.DailyCheckInRepository;
import org.springframework.stereotype.Repository;
import com.alba.cycleruncoach.exception.DuplicateResourceException;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaDailyCheckInRepositoryAdapter
        implements DailyCheckInRepository {

    private final SpringDataDailyCheckInJpaRepository repository;

    public JpaDailyCheckInRepositoryAdapter(
            SpringDataDailyCheckInJpaRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public void save(DailyCheckIn dailyCheckIn) {
        validateDailyCheckIn(dailyCheckIn);

        if (repository.existsById(dailyCheckIn.getId())) {
            throw new DuplicateResourceException(
                    "A daily check-in with ID "
                            + dailyCheckIn.getId()
                            + " already exists. Please use a different ID."
            );
        }

        repository.save(DailyCheckInJpaMapper.toEntity(dailyCheckIn));
    }

    @Override
    public List<DailyCheckIn> findAll() {
        return repository.findAll()
                .stream()
                .map(DailyCheckInJpaMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<DailyCheckIn> findById(Long id) {
        validateId(id);

        return repository.findById(id)
                .map(DailyCheckInJpaMapper::toDomain);
    }

    @Override
    public boolean update(DailyCheckIn dailyCheckIn) {
        validateDailyCheckIn(dailyCheckIn);

        if (!repository.existsById(dailyCheckIn.getId())) {
            return false;
        }

        repository.save(DailyCheckInJpaMapper.toEntity(dailyCheckIn));
        return true;
    }

    @Override
    public boolean deleteById(Long id) {
        validateId(id);

        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }

    private void validateDailyCheckIn(DailyCheckIn dailyCheckIn) {
        if (dailyCheckIn == null) {
            throw new IllegalArgumentException(
                    "DailyCheckIn cannot be null"
            );
        }
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "DailyCheckIn id must be greater than zero"
            );
        }
    }
}
