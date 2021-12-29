package devops.repository;

import devops.entity.Plan;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Integer> {

    Plan findByKey(String key);

}
