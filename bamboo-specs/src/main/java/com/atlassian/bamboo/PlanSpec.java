import com.atlassian.bamboo.specs.util.BambooServer;

@BambooSpec
public class PlanSpec {

    public Plan plan() {
        final Plan plan = new Plan(new Project()
                .key(new BambooKey("PROJECTKEY"))
                .name("PRJ"),
                "PLAN1E",
                new BambooKey("PLANKEY1"))
                .description("hello")
                .pluginConfigurations(new ConcurrentBuilds())
                .stages(new Stage("COMMITTED3")
                        .jobs(new Job("Job Name",
                                new BambooKey("JOBKEY"))
                                .artifacts(new Artifact()
                                        .name("Build results")
                                        .copyPattern("**/*")
                                        .location("target"))
                                .tasks(new VcsCheckoutTask()
                                        .checkoutItems(new CheckoutItem().defaultRepository()))))
                .planRepositories(new GitRepository()
                        .name("devops-service")
                        .url("https://github.com/sagarrajput91/devops.git")
                        .branch("master")
                        .authentication(new UserPasswordAuthentication("sagarrajput91")
                                .password("BAMSCRT@0@0@TfNpXIjO8wzAnv0TZgqLK+GZ+ikC+MnGWuPL3uUvTmAjcyNpRdKWF5Ohj0VsXPnG"))
                        .changeDetection(new VcsChangeDetection()))

                .planBranchManagement(new PlanBranchManagement()
                        .delete(new BranchCleanup()))
                .forceStopHungBuilds();
        return plan;
    }

    public PlanPermissions planPermission() {
        final PlanPermissions planPermission = new PlanPermissions(new PlanIdentifier("PROJECTKEY", "PLANKEY1"))
                .permissions(new Permissions()
                        .userPermissions("bamboo", PermissionType.EDIT, PermissionType.VIEW_CONFIGURATION, PermissionType.VIEW, PermissionType.ADMIN, PermissionType.CLONE, PermissionType.BUILD)
                        .groupPermissions("bamboo-admin", PermissionType.ADMIN, PermissionType.VIEW_CONFIGURATION, PermissionType.BUILD, PermissionType.CLONE, PermissionType.VIEW, PermissionType.EDIT)
                        .loggedInUserPermissions(PermissionType.VIEW)
                        .anonymousUserPermissionView());
        return planPermission;
    }

    public static void main(String... argv) {
        //By default credentials are read from the '.credentials' file.
        BambooServer bambooServer = new BambooServer("http://localhost:8085");
        final PlanSpec planSpec = new PlanSpec();

        final Plan plan = planSpec.plan();
        bambooServer.publish(plan);

        final PlanPermissions planPermission = planSpec.planPermission();
        bambooServer.publish(planPermission);
    }
}
