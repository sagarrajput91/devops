package devops.service;

import devops.entity.Plan;
import devops.entity.Project;
import org.springframework.stereotype.Service;

@Service
public interface BambooService {

    Project createProject(Project project);

    void createPlan(Plan plan);

    /*void createPlanPermission(PlanPermissions planPermissions);*/

    void publish( String projectKey,String planKey);

    void createLinkedrepository();
}
