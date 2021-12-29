package devops.service.impl;

import com.atlassian.bamboo.specs.api.builders.permission.PermissionType;
import com.atlassian.bamboo.specs.api.builders.permission.Permissions;
import com.atlassian.bamboo.specs.api.builders.permission.PlanPermissions;
import com.atlassian.bamboo.specs.api.builders.plan.Job;
import com.atlassian.bamboo.specs.api.builders.plan.Plan;
import com.atlassian.bamboo.specs.api.builders.plan.PlanIdentifier;
import com.atlassian.bamboo.specs.api.builders.plan.Stage;
import com.atlassian.bamboo.specs.api.builders.plan.artifact.Artifact;
import com.atlassian.bamboo.specs.api.builders.project.Project;
import com.atlassian.bamboo.specs.api.builders.repository.VcsRepository;
import com.atlassian.bamboo.specs.builders.repository.git.GitRepository;
import com.atlassian.bamboo.specs.builders.repository.git.UserPasswordAuthentication;
import com.atlassian.bamboo.specs.builders.repository.viewer.GitHubRepositoryViewer;
import com.atlassian.bamboo.specs.builders.task.VcsCheckoutTask;
import com.atlassian.bamboo.specs.util.BambooServer;
import devops.repository.PlanRepository;
import devops.repository.ProjectRepository;
import devops.service.BambooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BambooServiceImpl implements BambooService {


    @Value("${git.uriDevops}")
    String gitDevopsUrl ;

    @Value("${git.uriJenkins}")
    String gitJenkinsUrl ;

    @Value("${branchName}")
    String branchName;

    BambooServer bambooServer = new BambooServer("http://localhost:8085/");

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    PlanRepository planRepository;


    VcsRepository gitRepository() {

        return new GitRepository()
                .name("devops-service")
                //.url("git@bitbucket.org:your-company/your-repository.git")
                .url(gitDevopsUrl)
                .authentication(new UserPasswordAuthentication("sagarrajput91").password("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"))
                .branch(branchName);
    }


    VcsCheckoutTask gitRepositoryCheckoutTask() {
        return new VcsCheckoutTask()
                .addCheckoutOfDefaultRepository();
    }

    Artifact artifact() {
        return new Artifact("Build results")
                .location("target")
                .copyPattern("**/*");
    }

    PlanPermissions createPlanPermission(PlanIdentifier planIdentifier) {
        Permissions permission = new Permissions()
                .userPermissions("bamboo", PermissionType.ADMIN, PermissionType.CLONE, PermissionType.EDIT)
                .groupPermissions("bamboo-admin", PermissionType.ADMIN)
                .loggedInUserPermissions(PermissionType.VIEW)
                .anonymousUserPermissionView();
        return new PlanPermissions(planIdentifier.getProjectKey(), planIdentifier.getPlanKey()).permissions(permission);
    }

/*    public BitbucketServerRepository repository() {
        final BitbucketServerRepository repository = new BitbucketServerRepository()
                .name("My Linked repository")
                .repositoryViewer(new BitbucketServerRepositoryViewer())
                .server(new ApplicationLink()
                        .name("Bitbucket")
                        .id("c0e3a34a-0796-3201-994a-0c24f39fb323"))
                .projectKey("TST")
                .repositorySlug("stash")
                .sshPublicKey("ssh-rsa xxx... http://localhost:8085")
                .sshPrivateKey("BAMSCRT@0@0@5OeUyyy...")
                .sshCloneUrl("ssh://git@localhost:7999/tst/stash.git")
                .changeDetection(new VcsChangeDetection());
        return repository;
    }*/

    public GitRepository repository() {
        final GitRepository repository = new GitRepository()
                .name("Git Linked repository")
                .repositoryViewer(new GitHubRepositoryViewer()).authentication(new UserPasswordAuthentication(
                        "sagarrajput91").password("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")).url(gitJenkinsUrl).branch(branchName);
        return repository;
    }


    @Override
    public devops.entity.Project createProject(devops.entity.Project project) {
        return projectRepository.save(project);
    }

    @Override
    public void createPlan(devops.entity.Plan plan) {

/*        this.plan = new Plan(
                project,
                plan.getName(), plan.getKey())
                .description(plan.getDescription())
                .planRepositories(
                        gitRepository()
                )
                .stages(
                        new Stage("Stage 1").jobs(
                                new Job("Job Name", "JOBKEY")
                                        .tasks(
                                                gitRepositoryCheckoutTask()
                                        )
                                        .artifacts(artifact())
                        )
                );*/

        planRepository.save(plan);

    }

/*    @Override
    public void createPlanPermission(PlanPermissions planPermissions) {

    }*/


    private Project getProject(String projectKey){
        devops.entity.Project project = projectRepository.findByKey(projectKey);
        return new Project()
        .name(project.getName())
         .key(project.getKey());
    }

    private Plan getPlan(String projectKey,String planKey){

        devops.entity.Plan plan = planRepository.findByKey(planKey);

       return new Plan(
               getProject(projectKey),
                plan.getName(), plan.getKey())
                .description(plan.getDescription())
                .planRepositories(
                        gitRepository()
                )
                .stages(
                        new Stage("Stage 1").jobs(
                                new Job("Job Name", "JOBKEY")
                                        .tasks(
                                                gitRepositoryCheckoutTask()
                                        )
                                        .artifacts(artifact())
                        )
                ) ;

    }



    @Override
    public void publish(String projectKey,String planKey) {

        Plan plan= getPlan(projectKey,planKey);
        bambooServer.publish(plan);

        PlanPermissions planPermission = createPlanPermission(plan.getIdentifier());

        bambooServer.publish(planPermission);

    }

    @Override
    public void createLinkedrepository() {
        //bambooServer.publish(gitRepository());
        bambooServer.publish(repository());
    }
}
