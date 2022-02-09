import com.atlassian.bamboo.specs.api.BambooSpec;
import com.atlassian.bamboo.specs.api.builders.BambooKey;
import com.atlassian.bamboo.specs.api.builders.Variable;
import com.atlassian.bamboo.specs.api.builders.permission.PermissionType;
import com.atlassian.bamboo.specs.api.builders.permission.Permissions;
import com.atlassian.bamboo.specs.api.builders.permission.PlanPermissions;
import com.atlassian.bamboo.specs.api.builders.plan.Job;
import com.atlassian.bamboo.specs.api.builders.plan.Plan;
import com.atlassian.bamboo.specs.api.builders.plan.PlanIdentifier;
import com.atlassian.bamboo.specs.api.builders.plan.Stage;
import com.atlassian.bamboo.specs.api.builders.plan.artifact.Artifact;
import com.atlassian.bamboo.specs.api.builders.plan.branches.BranchCleanup;
import com.atlassian.bamboo.specs.api.builders.plan.branches.PlanBranchManagement;
import com.atlassian.bamboo.specs.api.builders.plan.configuration.ConcurrentBuilds;
import com.atlassian.bamboo.specs.api.builders.project.Project;
import com.atlassian.bamboo.specs.builders.task.CheckoutItem;
import com.atlassian.bamboo.specs.builders.task.VcsCheckoutTask;
import com.atlassian.bamboo.specs.util.BambooServer;
import com.atlassian.bamboo.specs.builders.task.ScriptTask;

@BambooSpec
public class PlanSpec {

    public Plan plan() {
        final Plan plan = new Plan(new Project()
                .key(new BambooKey("DEMOPROJECTKEY5"))
                .name("DEMOPROJECT5"),
                "PLAN5",
                new BambooKey("PLANKEY5"))
                .description("plan5")
                .pluginConfigurations(new ConcurrentBuilds())
                .stages(new Stage("Stage1")
                        .jobs(new Job("Job1",
                                new BambooKey("JOBKEY"))
                                .artifacts(new Artifact()
                                        .name("Build results")
                                        .copyPattern("**/*")
                                        .location("target"))
                                .tasks(new VcsCheckoutTask()
                                        .checkoutItems(new CheckoutItem().defaultRepository()),
                                        new ScriptTask()
                                                .description("task1")
                                                .inlineBody("echo 'task1'"),
                                        new ScriptTask()
                                                .description("task2")
                                                .inlineBody("echo \"task2\""),
                                        new ScriptTask()
                                                .description("task3")
                                                .inlineBody("echo \"task3\"\necho ${bamboo.sequence}")
                                )))
                .linkedRepositories("devops-service")

                .variables(new Variable("sequence",
                        "task2 task1 task3"))
                .planBranchManagement(new PlanBranchManagement()
                        .delete(new BranchCleanup()))
                .forceStopHungBuilds();
        return plan;
    }

    public PlanPermissions planPermission() {
        final PlanPermissions planPermission = new PlanPermissions(new PlanIdentifier("DEMOPROJECTKEY5", "PLANKEY5"))
                .permissions(new Permissions()
                        .userPermissions("bamboo", PermissionType.EDIT, PermissionType.VIEW_CONFIGURATION, PermissionType.VIEW, PermissionType.ADMIN, PermissionType.CLONE, PermissionType.BUILD)
                        .groupPermissions("bamboo-admin", PermissionType.ADMIN, PermissionType.VIEW_CONFIGURATION, PermissionType.BUILD, PermissionType.CLONE, PermissionType.VIEW, PermissionType.EDIT)
                        .loggedInUserPermissions(PermissionType.VIEW)
                        .anonymousUserPermissionView());
        return planPermission;
    }

    public static void main(String... argv) {
        //By default credentials are read from the '.credentials' file.
/*        BambooServer bambooServer = new BambooServer("http://localhost:8086");
        final PlanSpec planSpec = new PlanSpec();

        final Plan plan = planSpec.plan();
        bambooServer.publish(plan);

        final PlanPermissions planPermission = planSpec.planPermission();
        bambooServer.publish(planPermission);*/


        // deployment project
        Deployment deployment = new Deployment(new PlanIdentifier("DEMOPROJECTKEY5", "PLANKEY5"), "My deployment project coming from bamboo spec")
                .releaseNaming(new ReleaseNaming("release-1.1")
                        .autoIncrement(true))
                .environments(new Environment("QA")
                        .tasks(new ArtifactDownloaderTask()
                                .artifacts(new DownloadItem()
                                        .allArtifacts(true)), new ScriptTask()
                                .inlineBody("echo hello"), new ScpTask()
                                .host("myserver")
                                .username("admin")
                                .authenticateWithPassword("admin")
                                .fromArtifact(new ArtifactItem()
                                        .allArtifacts())
                                .toRemotePath("/remote-dir")));



        bambooServer.publish(deployment);
    }
}