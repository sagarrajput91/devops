package devops.repository;

import devops.entity.Plan;
import devops.entity.PlanVariable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanVariableRepository extends JpaRepository<PlanVariable, Integer> {


}
