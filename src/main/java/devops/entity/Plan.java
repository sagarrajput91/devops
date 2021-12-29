package devops.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "plan")
public class Plan {

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    @Column(name = "plan_id")
    Integer planId;

    @Column(name = "plan_name")
    String name;

    @Column(name = "plan_key")
    String key;

    @Column(name = "plan_description")
    String description;

    @ManyToOne
    @JoinColumn(name="project_id", referencedColumnName = "project_id", insertable = false, updatable = false)
    Project project;

}
