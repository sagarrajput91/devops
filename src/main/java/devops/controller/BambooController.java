package devops.controller;


import devops.entity.Plan;
import devops.entity.PlanPermissions;
import devops.entity.PlanVariable;
import devops.entity.Project;
import devops.service.BambooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/bamboo")
public class BambooController {

    @Autowired
    BambooService service;

    @PostMapping("/project")
    Project createProject(@RequestBody Project project ){
        return service.createProject(project);
    }


    @PostMapping("/project/plan")
    void createPlan(@RequestBody Plan plan ){
        service.createPlan(plan);
    }

/*    @PostMapping("/project/planpermission")
    void createPlanPermission(@RequestBody PlanPermissions planPermissions){
        service.createPlanPermission(planPermissions);
    }*/

    @PostMapping("/project/{proj-key}/plan/{plan-key}/publish")
    void publish(@PathVariable("proj-key") String projectKey,@PathVariable("plan-key") String planKey){
        service.publish(projectKey,planKey);
    }

    @PostMapping("/linkedrepository")
    void createLinkedrepository(){
        service.createLinkedrepository();
    }

    @PostMapping("/project/{proj-key}/plan/{plan-key}/planvariable")
    void createPlanvariable(@PathVariable("proj-key") String projectKey,@PathVariable("plan-key") String planKey,@RequestBody PlanVariable planVariable){
        service.createPlanvariable(projectKey,planKey,planVariable);
    }
}
