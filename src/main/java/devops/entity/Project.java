package devops.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "project")
public class Project{

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    @Column(name = "project_id")
    Integer projectId;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "project")
    //@JoinColumn(name="projectId")
    Set<Plan> planSet ;



    @Column(name = "project_key")
    String key;
    @Column(name = "project_name")
    String name;


}
